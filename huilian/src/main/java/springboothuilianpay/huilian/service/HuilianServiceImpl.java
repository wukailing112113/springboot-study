package springboothuilianpay.huilian.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import springboothuilianpay.huilian.utils.VeryfyUtil;
import springboothuilianpay.huilian.vo.QueryBindCard;

import java.util.Map;

@Service
public class HuilianServiceImpl implements HuilianInt {
    @Override
    public JSONObject queryBindCard(QueryBindCard queryBindCard) {

        queryBindCard.setServiceUri("HK0006");
        queryBindCard.setCharset("UTF-8");
        queryBindCard.setSignType("RSA");

        handleSign(queryBindCard);

        return null;
    }

    // 计算签名
    private void handleSign(QueryBindCard queryBindCard) {
        Map<String, Object> dataMap = JSON.parseObject(JSON.toJSONString(queryBindCard));
        Map<String, Object> filterSignDataMap = VeryfyUtil.paraFilter(dataMap);
        String linkString = VeryfyUtil.createLinkString(filterSignDataMap);
    }

}
