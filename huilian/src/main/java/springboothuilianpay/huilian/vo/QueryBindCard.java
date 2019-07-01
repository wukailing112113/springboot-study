package springboothuilianpay.huilian.vo;

import lombok.Data;

/**
 * 绑卡查询
 */
@Data
public class QueryBindCard extends Base{

    private String serviceUri;

    private String charset;

    private String signType;

    private String nonceStr;

    private String agentId;

    private String bankCard;

    private String sign;

}
