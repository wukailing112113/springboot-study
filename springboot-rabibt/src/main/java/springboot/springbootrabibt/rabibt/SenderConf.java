package springboot.springbootrabibt.rabibt;

import org.apache.catalina.startup.RealmRuleSet;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置消息队列
 */
@Configuration
public class SenderConf {


    //===============以下是验证Direct Exchange的队列==========
    @Bean
    public Queue queue() {
        return new Queue("hello");
    }

    @Bean
    public Queue userQueue(){
        return new Queue("user");
    }

    //===============以下是验证topic Exchange的队列==========
    @Bean
    public Queue queueMessage(){
        return new Queue("topic.message");
    }

    @Bean
    public Queue queueMessages(){
        return new Queue("topic.messages");
    }
    //===============以上是验证topic Exchange的队列==========


    //===============以下是验证Fanout Exchange的队列==========
    @Bean(name = "fanoutQueue")
    public Queue FanoutQueue(){
        return new Queue("fanout.A");
    }
    @Bean(name = "fanoutBQueue")
    public Queue FanoutBQueue(){
        return new Queue("fanout.B");
    }
    @Bean(name = "fanoutCQueue")
    public Queue FanoutCQueue(){
        return new Queue("fanout.C");
    }
    //===============以上是验证Fanout Exchange的队列==========

    /**
     * 配置交换机
     * @return
     */
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("exchange");
    }

    /**
     * Fanout Exchange形式又叫广播形式,因此我们发送到路由器的消息会使得绑定到该路由器的每一个Queue接收到消息,
     * 这个时候就算指定了Key,或者规则(即上文中convertAndSend方法的参数2),也会被忽略!
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanoutExchange");
    }

    /**
     * 交换机和队列进行绑定
     * @param queueMessage
     * @param exchange
     * 当生者生产消息的时候，会根据routingKey来匹配到某个交换机，从而将消息发送到具体的消息队列
     * @return
     */
    @Bean
    public Binding bindingExchange(Queue queueMessage,TopicExchange exchange){
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    /**
     * 将队列topic.messages与exchange绑定，binding_key为topic.#,模糊匹配
     * @param queueMessages
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindingExchanges(Queue queueMessages,TopicExchange exchange){
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");//*表示一个词,#表示零个或多个词
    }

    /**
     * 绑定队列到广播交换机
     * @param fanoutQueue
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding bindingFanoutExchangeA(@Qualifier("fanoutQueue")Queue fanoutQueue,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutQueue).to(fanoutExchange);
    }
    @Bean
    public Binding bindingFanoutExchangeB(@Qualifier("fanoutBQueue") Queue fanoutBQueue, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutBQueue).to(fanoutExchange);
    }
    @Bean
    public Binding bindingFanoutExchangeC(@Qualifier("fanoutCQueue")Queue fanoutCQueue,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutCQueue).to(fanoutExchange);
    }


}
