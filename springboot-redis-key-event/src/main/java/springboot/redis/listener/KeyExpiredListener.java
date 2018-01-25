package springboot.redis.listener;

import org.apache.log4j.Logger;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/** Redis Key 事件监听器，收到事件监听后，派发给业务线程去处理
 * http://blog.csdn.net/liuchuanhong1/article/details/70147149
 * http://blog.csdn.net/zpf0918/article/details/55511640
 * Created by Sawyer on 2018/1/22.
 */
public class KeyExpiredListener implements MessageListener {

    Logger logger = Logger.getLogger(KeyExpiredListener.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("redis key 过期事件通知");
        try {
            StringRedisSerializer strSerial = new StringRedisSerializer();
            String msgBody = strSerial.deserialize(message.getBody());
            String channel = strSerial.deserialize(message.getChannel());
            String strPatt = strSerial.deserialize(pattern);
            logger.debug("onPMessage pattern " + strPatt+ " " + channel + " " + msgBody);
        }catch (Exception e){
            logger.error(String.format("Redis KeyExpireListener onMessage() occurred an error, cause by %s", e.getMessage()));
        }
    }
}
