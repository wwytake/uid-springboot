package com.github.wwytake.uid;

import com.github.wwytake.uid.impl.CachedUidGenerator;
import com.github.wwytake.uid.impl.DefaultUidGenerator;
import com.github.wwytake.uid.impl.UidProperties;
import com.github.wwytake.uid.worker.DisposableWorkerIdAssigner;
import com.github.wwytake.uid.worker.WorkerIdAssigner;
import com.github.wwytake.uid.worker.WorkerNodeHandler;
import com.github.wwytake.uid.worker.handler.DefaultWorkNodeHandler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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

        @Bean
        @ConditionalOnMissingBean()
        WorkerNodeHandler workNodeMybatisHandler() {
            return new DefaultWorkNodeHandler();
        }

}
