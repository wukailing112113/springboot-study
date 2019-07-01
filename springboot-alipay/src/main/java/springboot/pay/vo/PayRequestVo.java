package springboot.pay.vo;

import lombok.Data;
import springboot.pay.utils.JsonUtil;

/**
 * 支付宝手机网页支付请求参数
 */
@Data
public class PayRequestVo {

    /**业务参数*/
    private String out_trade_no  ;
    private String total_amount  ;
    private String subject       ;
    private String seller_id     ;
    private String product_code  ;
    private String body;

    @Override
    public String toString() {
        return JsonUtil.format(this);
    }
}
