package springboot.pay.alipay.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 支付类型
 */
public enum PayType {
    ALIPAY((byte) 2, "支付宝"), //旧版支付宝支付
    WXPAY((byte) 4, "微信"),
    UNIONPAY((byte) 5, "银联"),
    APPLEPAY((byte) 7, "苹果支付"),
    NEWALIPAY((byte) 10, "新版支付宝"), // 已实现
    TINY_WECHATPAY((byte) 12, "小程序微信支付");

    private static Map<Byte, PayType> map = new HashMap<>();

    static {
        if (Objects.isNull(map)) {
            map = new HashMap<>();
        }
        Arrays.stream(PayType.values()).forEach(pt -> map.put(pt.getCode(), pt));
    }

    public static void main(String[] args) {
        System.out.println(PayType.map.get(10));
    }

    private Byte code;
    private String desc;

    PayType(byte code, String desc) {
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

    public static PayType getPayType(Byte type) {
        return map.get(type);
    }
}
