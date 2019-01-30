package springboot.springbootrabibt.rabibt;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 消息生产者
 */
@Component
public class Provider {

    @Autowired
    private AmqpTemplate template;

    public void send(){
        // 一对一发送消息.第一个参数消息队列的名称，现在是配置在mq控制台http://localhost:15672;第二个参数是消息对应的值
        template.convertAndSend("hello","hello,rabbit~");
        template.convertAndSend("user","hello,user~");


        // 一对一发送消息对象
//        MessageEntity messageEntity = new MessageEntity();
//        messageEntity.setId(1);
//        messageEntity.setName("hello");
//        messageEntity.setCreateTime(new Date());
//        template.convertAndSend("hello",messageEntity );
    }

    /**
     * 方法的第一个参数是交换机名称,第二个参数是发送的key,第三个参数是内容,RabbitMQ将会根据第二个参数去寻找有没有匹配此规则的队列,
     * 如果有,则把消息给它,如果有不止一个,则把消息分发给匹配的队列(每个队列都有消息!),显然在我们的测试中,参数2匹配了两个队列,
     * 因此消息将会被发放到这两个队列中,而监听这两个队列的监听器都将收到消息!
     * 那么如果把参数2改为topic.messages呢?显然只会匹配到一个队列
     *
     */
    public void sendToTopicQueue(){
        template.convertAndSend("exchange","topic.messages","hello,topic.message queue");
    }

    /**
     * 那前面已经介绍过了,Fanout Exchange形式又叫广播形式,
     * 因此我们发送到路由器的消息会使得绑定到该路由器的每一个Queue接收到消息,这个时候就算指定了Key,
     * 或者规则(即上文中convertAndSend方法的参数2),也会被忽略!
     */
    public void sendToFanoutQueue() {
        template.convertAndSend("fanoutExchange","aaaa","fanout_exchange_test");
    }
}
