/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
    WorkerIdAssigner disposableWorkerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    @ConditionalOnProperty(name = "type", prefix = "wwytake.uid", havingValue = "cache")
    CachedUidGenerator cachedUidGenerator() {
        return new CachedUidGenerator(uidProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    DefaultUidGenerator defaultUidGenerator() {
        return new DefaultUidGenerator(uidProperties);
    }


    @Bean
    @ConditionalOnMissingBean()
    WorkerNodeHandler workNodeMybatisHandler() {
        return new DefaultWorkNodeHandler();
    }

}
