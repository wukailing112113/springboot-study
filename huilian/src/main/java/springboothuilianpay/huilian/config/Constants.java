package springboothuilianpay.huilian.config;

/**
 * 常量配置
 */
public class Constants {

    private Constants() {
        // No Constructor.
    }

    /**
     * The constant SUCCESS.
     */
    public static final String SUCCESS = "10000"; // 成功
    /**
     * The constant PAYING.
     */
    public static final String PAYING = "10003";  // 用户支付中
    /**
     * The constant FAILED.
     */
    public static final String FAILED = "40004";  // 失败
    /**
     * The constant ERROR.
     */
    public static final String ERROR = "20000"; // 系统异常

    /**
     * The constant heartbeat_delay.
     */
//交易保障线程第一次调度延迟和调度间隔（秒）
    public static final int heartbeat_delay = 5;
    /**
     * The constant heartbeat_duration.
     */
    public static final int heartbeat_duration = 900;

    /**
     * The constant cancel_duration.
     */
//当面付最大撤销次数和撤销间隔（毫秒）
    public static final int cancel_duration = 2000;
    /**
     * The constant max_cancel_retry.
     */
    public static final int max_cancel_retry = 3;

    /**
     * The constant max_query_retry.
     */
//当面付最大查询次数和查询间隔（毫秒）
    public static final int max_query_retry = 5;
    /**
     * The constant query_duration.
     */
    public static final int query_duration = 5000;


    /**
     * The constant open_api_domain.
     */
    public static final String open_api_domain = "http://39.108.137.8:8099/v1.0/facade/hkRoute";

}
