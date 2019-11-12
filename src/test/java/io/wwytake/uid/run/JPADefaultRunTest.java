package io.wwytake.uid.run;
import io.wwytake.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = JPAApplication.class,
        properties = {"spring.profiles.active=jpa,default"}
        ,webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@Slf4j
public  class JPADefaultRunTest{

    @Autowired
    private UidGenerator defaultUidGenerator;


    @Test
    public void uidtest(){
        for (int j = 0; j < 100; j++) {
            log.debug(defaultUidGenerator.getUID()+"");
            Assert.assertNotNull(defaultUidGenerator.getUID());
        }

    }
}
