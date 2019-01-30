package springboot.springbootrabibt.rabibt;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 */
@Component
@RabbitListener(queues = "hello")
public class MessageReceive {

    // 接收字符串类型的消息，消费后就不存在队列中
    @RabbitHandler
    public void processC(String str) {
        System.out.println("hello:"+str);
    }
    // 验证Direct模式消息是否可以被多次消费，如果控制台输出一个消息，则证明是一对一
    // 如果这样验证会报错，因为多个无法匹配到消息的消费队列
//    @RabbitHandler
//    public void processB(String str) {
//        System.out.println("Receive111:"+str);
//    }

    // 接收对象类型的消息，消费后就不存在队列中
    @RabbitHandler
    public void processObject(MessageEntity messageEntity) {
        System.out.println("Receive111:"+messageEntity.toString());
    }
}
