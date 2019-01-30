package springboot.springbootrabibt.rabibt;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * RabbitMQ 发送消息对象，对象必须序列化
 */
@Data
public class MessageEntity implements Serializable {

    private Integer id;
    private String name;
    private Date createTime;

    @Override
    public String toString(){
        return this.getName()+"-"+this.getId();
    }

}
