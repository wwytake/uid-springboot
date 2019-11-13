package io.wwytake.uid.run;

import io.wwytake.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestApplication.class,
        properties = {"spring.profiles.active=mybatis,default"}
        ,webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@Slf4j
public  class MybaitsDefaultRunTest{

    @Autowired
    private UidGenerator defaultUidGenerator;

    @Test
    public void mybaitsTest(){
        log.info(defaultUidGenerator.getUID()+"");
        Assert.assertNotNull(defaultUidGenerator.getUID());
    }
}