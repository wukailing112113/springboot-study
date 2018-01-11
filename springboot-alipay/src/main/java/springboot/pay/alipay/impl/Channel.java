package springboot.pay.alipay.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Enum - 终端
 */
public enum Channel {

    //1 PC web, 2 Android app, 3 iOS app， 4 微信

    PC((byte) 1, "PC web"),
    ANDROID((byte) 2, "Android app"),
    IOS((byte) 3, " iOS app"),
    WEBCHAT((byte) 4, "微信"),
    WAP((byte) 5, "H5端"),
    TINY_WECHAT((byte) 6, "小程序");

    private static Map<Byte, Channel> map = new HashMap<>();

    static {
        if (map == null) {
            map = new HashMap<>();
        }
        Arrays.stream(Channel.values()).forEach(channel -> map.put(channel.getCode(), channel));
    }

    private Byte code;
    private String desc;


    Channel(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public static Channel getChannel(Byte type) {
        return map.get(type);
    }
}
