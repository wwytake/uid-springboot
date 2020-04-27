# uid-springboot  

[![Build Status](https://travis-ci.org/wwytake/uid-springboot.svg?branch=master)](https://travis-ci.org/wwytake/uid-springboot)  

==========================
## 前言

本组件是 [百度UID](https://github.com/baidu/uid-generator) 的一个派生版本，改造为基于spring boot 的版本.使用jdbc连接数据库,不依赖其他ORM框架


## 使用

#### 建表语句[mysql]

```sql
DROP TABLE IF EXISTS WORKER_NODE;
CREATE TABLE WORKER_NODE
(
ID BIGINT NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
HOST_NAME VARCHAR(64) NOT NULL COMMENT 'host name',
PORT VARCHAR(64) NOT NULL COMMENT 'port',
TYPE INT NOT NULL COMMENT 'node type: ACTUAL or CONTAINER',
LAUNCH_DATE DATE NOT NULL COMMENT 'launch date',
MODIFIED TIMESTAMP NOT NULL COMMENT 'modified time',
CREATED TIMESTAMP NOT NULL COMMENT 'created time',
PRIMARY KEY(ID)
)
 COMMENT='DB WorkerID Assigner for UID Generator',ENGINE = INNODB;

```
#### maven依赖
```xml
<dependency>
  <groupId>com.github.wwytake</groupId>
  <artifactId>uid-springboot</artifactId>
  <version>2.0.2-release</version>
</dependency>
```

#### 配置
```yaml
wwytake:
  uid: 
    timeBits: 29
    workerBits: 21
    seqBits: 13
    epochStr: "2018-11-26"
    type: "cache"
    CachedUidGenerator:          # 无此项,默认DefaultUidGenerator
          boost-power: 3          # RingBuffer size扩容参数, 可提高UID生成的吞吐量, 默认:3
          padding-factor: 50      # 指定何时向RingBuffer中填充UID, 取值为百分比(0, 100), 默认为50
          #schedule-interval: 60  # 默认:不配置此项, 即不实用Schedule线程. 如需使用, 请指定Schedule线程时间间隔, 单位:秒
```

#### [RunTest](src/test/java/io/wwytake/uid/run/DefaultRunTest.java)

```java
public  class DefaultRunTest {

    @Autowired
    private UidGenerator defaultUidGenerator;

    @Test
    public void mybaitsTest(){
        Set<Long> hashSet = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            Long uid = defaultUidGenerator.getUID();
            Assert.assertNotNull(uid);
            Assert.assertFalse(hashSet.contains(uid));
            hashSet.add(uid);
        }
    }
}
```
## 概述

UidGenerator是Java实现的，基于[Snowflake](https://github.com/twitter/snowflake)算法的唯一ID生成器。UidGenerator以组件形式工作在应用项目中,
支持自定义workerId位数和初始化策略, 从而适用于[docker](https://www.docker.com/)等虚拟化环境下实例自动重启、漂移等场景。

在实现上, UidGenerator通过借用未来时间来解决sequence天然存在的并发限制; 采用RingBuffer来缓存已生成的UID, 并行化UID的生产和消费，同时对CacheLine补齐，避免了由RingBuffer带来的硬件级「伪共享」问题. 最终单机QPS可达<font color=red>600万</font>。

依赖版本：[Java8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)及以上版本，[MySQL](https://dev.mysql.com/downloads/mysql/)(内置WorkerID分配器, 启动阶段通过DB进行分配; 如自定义实现, 则DB非必选依赖）。

Snowflake算法
-------------
![Snowflake](doc/snowflake.png)
Snowflake算法描述：指定机器 & 同一时刻 & 某一并发序列，是唯一的。据此可生成一个64 bits的唯一ID（long）。默认采用上图字节分配方式：

* sign(1bit)  

  固定1bit符号标识，即生成的UID为正数。

* delta seconds (28 bits)  

  当前时间，相对于时间基点"2016-05-20"的增量值，单位：秒，最多可支持约8.7年

* worker id (22 bits)  

  机器id，最多可支持约420w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃，后续可提供复用策略。

* sequence (13 bits)   

  每秒下的并发序列，13 bits可支持每秒8192个并发。



## 组件功能简述

UidGenerator在应用中是以 Spring 组件的形式提供服务。

- `DefaultUidGenerator`提供了最简单的Snowflake式的生成模式，但是并没有使用任何缓存来预存UID，在需要生成ID的时候即时进行计算。
- `CachedUidGenerator`是一个使用RingBuffer预先缓存UID的生成器，在初始化时就会填充整个RingBuffer，并在take()时检测到少于指定的填充阈值之后就会异步地再次填充RingBuffer（默认值为50%），另外可以启动一个定时器周期性检测阈值并及时进行填充。


CachedUidGenerator
-------------------
RingBuffer环形数组，数组每个元素成为一个slot。RingBuffer容量，默认为Snowflake算法中sequence最大值，且为2^N。可通过```boostPower```配置进行扩容，以提高RingBuffer读写吞吐量。

Tail指针、Cursor指针用于环形数组上读写slot：

* Tail指针

  表示Producer生产的最大序号(此序号从0开始，持续递增)。Tail不能超过Cursor，即生产者不能覆盖未消费的slot。当Tail已赶上curosr，此时可通过`rejectedPutBufferHandler`指定PutRejectPolicy。

* Cursor指针

  表示Consumer消费到的最小序号(序号序列与Producer序列相同)。Cursor不能超过Tail，即不能消费未生产的slot。当Cursor已赶上tail，此时可通过```rejectedTakeBufferHandler```指定TakeRejectPolicy。

![RingBuffer](doc/ringbuffer.png)

CachedUidGenerator采用了双RingBuffer，Uid-RingBuffer用于存储Uid、Flag-RingBuffer用于存储Uid状态(是否可填充、是否可消费)。由于数组元素在内存中是连续分配的，可最大程度利用CPU cache以提升性能。但同时会带来「伪共享」FalseSharing问题，为此在Tail、Cursor指针、Flag-RingBuffer中采用了CacheLine补齐方式。

![FalseSharing](doc/cacheline_padding.png)

#### RingBuffer填充时机 ####
* 初始化预填充

  RingBuffer初始化时，预先填充满整个RingBuffer。

* 即时填充

  Take消费时，即时检查剩余可用slot量(```tail``` - ```cursor```)，如小于设定阈值，则补全空闲slots。阈值可通过```paddingFactor```来进行配置，请参考Quick Start中CachedUidGenerator配置。

* 周期填充（默认不启用）

  通过Schedule线程，定时补全空闲slots。可通过```scheduleInterval```配置，以应用定时填充功能，并指定Schedule时间间隔。



### 关于UID比特分配的建议

对于**并发数要求不高、期望长期使用**的应用，可增加```timeBits```位数, 减少```seqBits```位数。例如节点采取用完即弃的 WorkerIdAssigner 策略，重启频率为12次/天，那么配置成 `{"workerBits":23,"timeBits":31,"seqBits":9}` 时，可支持28个节点以整体并发量14400 UID/s的速度持续运行68年。

对于**节点重启频率频繁、期望长期使用**的应用, 可增加```workerBits```和```timeBits```位数, 减少```seqBits```位数. 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为24*12次/天，那么配置成```{"workerBits":27,"timeBits":30,"seqBits":6}```时，可支持37个节点以整体并发量2400 UID/s的速度持续运行34年。

#### 吞吐量测试
在MacBook Pro（2.7GHz Intel Core i5, 8G DDR3）上进行了CachedUidGenerator（单实例）的UID吞吐量测试。

首先固定住workerBits为任选一个值(如20)，分别统计timeBits变化时(如从25至32，总时长分别对应1年和136年)的吞吐量，如下表所示:

|timeBits|25|26|27|28|29|30|31|32|
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|throughput|6,831,465|7,007,279|6,679,625|6,499,205|6,534,971|7,617,440|6,186,930|6,364,997|

![throughput1](doc/throughput1.png)

再固定住timeBits为任选一个值(如31)，分别统计workerBits变化时(如从20至29，总重启次数分别对应1百万和500百万)的吞吐量，如下表所示：

|workerBits|20|21|22|23|24|25|26|27|28|29|
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|throughput|6,186,930|6,642,727|6,581,661|6,462,726|6,774,609|6,414,906|6,806,266|6,223,617|6,438,055|6,435,549|

![throughput1](doc/throughput2.png)

由此可见, 不管如何配置, CachedUidGenerator总能提供**600万/s**的稳定吞吐量, 只是使用年限会有所减少。这真的是太棒了。

最后, 固定住workerBits和timeBits位数(如23和31), 分别统计不同数目(如1至8,本机CPU核数为4)的UID使用者情况下的吞吐量，

|workerBits|1|2|3|4|5|6|7|8|
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|throughput|6,462,726|6,542,259|6,077,717|6,377,958|7,002,410|6,599,113|7,360,934|6,490,969|

![throughput1](doc/throughput3.png)
