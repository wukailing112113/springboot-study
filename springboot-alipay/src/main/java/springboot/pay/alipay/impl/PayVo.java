package springboot.pay.alipay.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sawyer on 2018/1/9.
 */
public class PayVo {

    /** 用户id */
    private Integer uid;

    /** 支付类型 */
    private Byte payType;

    /** 微信唯一标识 */
    private String openId;

    /** 终端 */
    private Byte channel;

    /** 请求url */
    private String requestUrl;

    /** ip地址 */
    private String ip;

    /** 支付码 */
    private String payNo;

    /** 总价 */
    private Float totalPrice;

    /** 商品描述 */
    private String subject;

    /** 回调地址 */
    private String notifyUrl;

    /** 支付信息 */
    private String result;

    /** 支付密码 */
    private String pwd;

    /** 订单 */
    private Order order;

    /** 订单id */
    private List<Long> subsIds = new ArrayList<>();

    private List<Order> orderList = new ArrayList<>();


    /** 其它参数 */
    private Map<String, String> parms = new HashMap<>();

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Byte getChannel() {
        return channel;
    }

    public void setChannel(Byte channel) {
        this.channel = channel;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }



    public List<Long> getSubsIds() {
        return subsIds;
    }

    public void setSubsIds(List<Long> subsIds) {
        this.subsIds = subsIds;
    }

    public Map<String, String> getParms() {
        return parms;
    }

    public void setParms(Map<String, String> parms) {
        this.parms = parms;
    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
