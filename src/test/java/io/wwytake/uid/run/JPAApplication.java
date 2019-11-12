package io.wwytake.uid.run;
import io.wwytake.uid.run.TestApplication;
import io.wwytake.uid.worker.WorkerNodeHandler;
import io.wwytake.uid.worker.jpa.WorkNodeJpaHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@SpringBootApplication
@Configuration
public  class JPAApplication{
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
    @Bean
    @Order(1)
    WorkerNodeHandler WorkNodeJpaHandler() {
        return new WorkNodeJpaHandler();
    }
}
