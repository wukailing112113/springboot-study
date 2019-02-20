package springboot.springbootrabibt.rocket;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class Producer1 {
    public Producer1() {
    }

    public static void main(String[] args) throws MQClientException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("tc_pro1");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setInstanceName("RFcluster");
        producer.setVipChannelEnabled(false);
        producer.start();

        for(int i = 0; i < 1; ++i) {
            try {
                Message msg = new Message("tc_demo", "TagA", ("Hello RocketMq " + i).getBytes("UTF-8"));
                SendResult sendResult = producer.send(msg);
                LocalTransactionExecuter var10000 = new LocalTransactionExecuter() {
                    public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
                        return null;
                    }
                };
                System.out.println(sendResult);
            } catch (Exception var6) {
                var6.printStackTrace();
                Thread.currentThread();
                Thread.sleep(1000L);
            }
        }

        producer.shutdown();
    }
}
