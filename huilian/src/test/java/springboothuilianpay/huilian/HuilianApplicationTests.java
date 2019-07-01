package springboothuilianpay.huilian;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboothuilianpay.huilian.config.Constants;
import springboothuilianpay.huilian.utils.HttpHelper;
import springboothuilianpay.huilian.vo.QueryBindCard;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HuilianApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() throws Exception{
        System.out.println("111");
        QueryBindCard queryBindCard = new QueryBindCard();
        queryBindCard.setAgentId("111");
        JSONObject jsonObject = HttpHelper.httpPost(Constants.open_api_domain, queryBindCard);
        System.out.println(jsonObject.toJSONString());
    }

}
