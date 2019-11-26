package io.wwytake.uid;

import io.wwytake.uid.worker.DisposableWorkerIdAssigner;
import io.wwytake.uid.worker.WorkerIdAssigner;
import io.wwytake.uid.worker.WorkerNodeHandler;


import io.wwytake.uid.worker.handler.DefaultWorkNodeHandler;
import io.wwytake.uid.impl.CachedUidGenerator;
import io.wwytake.uid.impl.DefaultUidGenerator;
import io.wwytake.uid.impl.UidProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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
