package springboot.pay.alipay;

import springboot.pay.alipay.impl.PayVo;

/**
 * Created by Sawyer on 2018/1/9.
 */
public interface ILauchPayPreProcessor {
    PayVo process(PayVo vo);
}
