package io.wwytake.uid.run;


import io.wwytake.uid.UidGenerator;
import io.wwytake.uid.worker.WorkerNodeHandler;
import io.wwytake.uid.worker.jpa.WorkNodeJpaHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringRunner;


public class RunTest {



    @SpringBootTest(classes = TestApplication.class,
            properties = {"spring.profiles.active=mybatis,default"}
            ,webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @RunWith(SpringRunner.class)
    @Slf4j
    public static class MybaitsDefaultRunTest{

        @Autowired
        private UidGenerator defaultUidGenerator;

        @Test
        public void mybaitsTest(){
            log.info(defaultUidGenerator.getUID()+"");
            Assert.assertNotNull(defaultUidGenerator.getUID());
        }
    }


    @SpringBootTest(classes = TestApplication.class,
            properties = {"spring.profiles.active=mybatis,cache"}
            ,webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @RunWith(SpringRunner.class)
    @Slf4j
    public static class MybaitsCacheRunTest{

        @Autowired
        private UidGenerator defaultUidGenerator;

        @Test
        public void uidtest(){
            log.info(defaultUidGenerator.getUID()+"");
            Assert.assertNotNull(defaultUidGenerator.getUID());
        }
    }


    @SpringBootTest(classes = JPAApplication.class,
            properties = {"spring.profiles.active=jpa,default"}
            ,webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @RunWith(SpringRunner.class)
    @Slf4j
    public static class JPADefaultRunTest{

        @Autowired
        private UidGenerator defaultUidGenerator;


        @Test
        public void uidtest(){
            for (int j = 0; j < 100; j++) {
                log.info(defaultUidGenerator.getUID()+"");
                Assert.assertNotNull(defaultUidGenerator.getUID());
            }

        }
    }
    @SpringBootApplication
    @Configuration
    public static class JPAApplication{
        public static void main(String[] args) {
            SpringApplication.run(TestApplication.class, args);
        }
        @Bean
        @Order(1)
        WorkerNodeHandler WorkNodeJpaHandler() {
            return new WorkNodeJpaHandler();
        }
    }

    @SpringBootTest(classes = JPAApplication.class,
            properties = {"spring.profiles.active=jpa,cache"}
            ,webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @RunWith(SpringRunner.class)
    @Slf4j
    public static class JPACacheRunTest{

        @Autowired
        private UidGenerator defaultUidGenerator;





        @Test
        public void uidtest(){
            log.info(defaultUidGenerator.getUID()+"");
            Assert.assertNotNull(defaultUidGenerator.getUID());
        }
    }


}
