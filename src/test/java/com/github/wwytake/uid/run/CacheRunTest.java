package com.github.wwytake.uid.run;

import com.github.wwytake.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest(classes = TestApplication.class,
        properties = {"spring.profiles.active=test,cache"}
        ,webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@Slf4j
public  class CacheRunTest {

    @Autowired
    private UidGenerator uidGenerator;

    @Test
    public void uidtest(){
        Set<Long> hashSet = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            Long uid = uidGenerator.getUID();
            Assert.assertNotNull(uid);
            Assert.assertFalse(hashSet.contains(uid));
            hashSet.add(uid);
        }

    }
}