package io.wwytake.uid;

import io.wwytake.uid.worker.DisposableWorkerIdAssigner;
import io.wwytake.uid.worker.WorkerIdAssigner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * UID 测试主类
 * 
 * @author tangyz
 */
@SpringBootApplication
public class UidTestApplication {
	
	public static void main(String[] args) {
	    SpringApplication.run(UidTestApplication.class, args);
	}
	
    @Bean
    @ConditionalOnMissingBean
    WorkerIdAssigner workerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }
}
