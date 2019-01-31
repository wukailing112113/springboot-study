package springboot.springbootrabibt.rabibt;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 * Direct模式 消息队列只能被一个队列消费。验证：建立两个类同时监听队列user.然后生产一条消息，最后发现只有一个监听类可以消费到这条消息
 */
@Component
@RabbitListener(queues = "user")
public class MessageReceive {

    // 接收字符串类型的消息，消费后就不存在队列中
    @RabbitHandler
    public void processC(String str) {
        System.out.println("user0:"+str);
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
