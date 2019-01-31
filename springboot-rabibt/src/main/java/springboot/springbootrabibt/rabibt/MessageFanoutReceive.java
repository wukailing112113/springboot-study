package springboot.springbootrabibt.rabibt;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "fanout.A")
public class MessageFanoutReceive {

    @RabbitHandler
    public void processC(String str) {
        System.out.println("fanout.message:"+str);
    }
}
