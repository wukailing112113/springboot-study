package springboot.springbootrabibt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.springbootrabibt.rabibt.Provider;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRabibtApplicationTests {

    @Autowired
    private Provider provider;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testSend(){
        provider.send();
    }
    @Test
    public void testSendTopic(){
        provider.sendToTopicQueue();
    }

    @Test
    public void testSendFanoutExchange(){
        provider.sendToFanoutQueue();
    }



}

