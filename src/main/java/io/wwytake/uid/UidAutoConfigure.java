package io.wwytake.uid;

import io.wwytake.uid.worker.DisposableWorkerIdAssigner;
import io.wwytake.uid.worker.WorkerIdAssigner;
import io.wwytake.uid.worker.WorkerNodeHandler;


import io.wwytake.uid.worker.jpa.WorkNodeJpaHandler;
import io.wwytake.uid.worker.myabits.WorkNodeMybatisHandler;
import io.wwytake.uid.impl.CachedUidGenerator;
import io.wwytake.uid.impl.DefaultUidGenerator;
import io.wwytake.uid.impl.UidProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.RepositoryConfigurationSource;


/**
 * UID 的自动配置
 *
 * @author tangyz
 */
@Configuration
@ConditionalOnClass({DefaultUidGenerator.class, CachedUidGenerator.class})
@MapperScan({ "io.wwytake.uid.worker.dao" })
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


    @Bean
	@ConditionalOnClass({MybatisAutoConfiguration.class})
    @ConditionalOnMissingBean
	WorkerNodeHandler workNodeMybatisHandler() {
        return new WorkNodeMybatisHandler();
    }

    @Configuration
    @ConditionalOnClass({RepositoryConfigurationSource.class})
    @EnableJpaRepositories
    @EntityScan("io.wwytake.uid.worker")
    public static class JPAAutoConfig{
        @Bean
        @ConditionalOnClass({RepositoryConfigurationSource.class})
        @ConditionalOnMissingBean
        WorkerNodeHandler workNodeJpaHandler() {
            return new WorkNodeJpaHandler();
        }
    }




}
