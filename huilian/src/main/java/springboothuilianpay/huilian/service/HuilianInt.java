package springboothuilianpay.huilian.service;

import com.alibaba.fastjson.JSONObject;
import springboothuilianpay.huilian.vo.QueryBindCard;

public interface HuilianInt {

    public JSONObject queryBindCard(QueryBindCard queryBindCard);
}
