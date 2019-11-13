package io.wwytake.uid.jpa;
import io.wwytake.uid.jpa.jpa.WorkNodeJpaHandler;
import io.wwytake.uid.run.TestApplication;
import io.wwytake.uid.worker.WorkerNodeHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

@SpringBootApplication
@Configuration
public  class JPAApplication{
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Configuration
    @ConditionalOnClass({RepositoryConfigurationSource.class})
    @EnableJpaRepositories
    @EntityScan("io.wwytake.uid.worker")
    public static class JPAAutoConfig{
        @Bean
        WorkerNodeHandler workNodeJpaHandler() {
            return new WorkNodeJpaHandler();
        }
    }




}
