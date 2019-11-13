package io.wwytake.uid;

import io.wwytake.uid.worker.DisposableWorkerIdAssigner;
import io.wwytake.uid.worker.WorkerIdAssigner;
import io.wwytake.uid.worker.WorkerNodeHandler;


import io.wwytake.uid.worker.myabits.WorkNodeMybatisHandler;
import io.wwytake.uid.impl.CachedUidGenerator;
import io.wwytake.uid.impl.DefaultUidGenerator;
import io.wwytake.uid.impl.UidProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * UID 的自动配置
 *
 * @author tangyz
 */
@Configuration
@ConditionalOnClass({DefaultUidGenerator.class, CachedUidGenerator.class})
@EnableConfigurationProperties(UidProperties.class)
public class UidAutoConfigure {

    @Autowired
    private UidProperties uidProperties;

    @Bean
    @ConditionalOnMissingBean
    WorkerIdAssigner disposableWorkerIdAssigner(){
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    DefaultUidGenerator defaultUidGenerator() {
        return new DefaultUidGenerator(uidProperties);
    }

    @Bean
    CachedUidGenerator cachedUidGenerator() {
        return new CachedUidGenerator(uidProperties);
    }

    /**
     * description:
     * @author yingww
     */
    @Configuration
    @ConditionalOnClass({MybatisAutoConfiguration.class})
    @MapperScan({ "io.wwytake.uid.worker.dao" })
    public static class MybaitsAutoConfig{
//        add classpath*:io/wwytake/uid/**/*.xml in mybatis.mapper-locations
        @Bean
        @ConditionalOnMissingBean()
        WorkerNodeHandler workNodeMybatisHandler() {
            return new WorkNodeMybatisHandler();
        }

    }



}
