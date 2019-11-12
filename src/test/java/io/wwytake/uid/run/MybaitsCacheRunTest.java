import io.wwytake.uid.UidGenerator;
import io.wwytake.uid.run.TestApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestApplication.class,
        properties = {"spring.profiles.active=mybatis,cache"}
        ,webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@Slf4j
public  class MybaitsCacheRunTest{

    @Autowired
    private UidGenerator defaultUidGenerator;

    @Test
    public void uidtest(){
        log.debug(defaultUidGenerator.getUID()+"");
        Assert.assertNotNull(defaultUidGenerator.getUID());
    }
}