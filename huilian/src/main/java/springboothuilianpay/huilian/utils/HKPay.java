package springboothuilianpay.huilian.utils;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import static springboothuilianpay.huilian.test.SignatureData.hexStrToBytes;
import static springboothuilianpay.huilian.utils.RSAUtils.bytesToHexStr;


/**
 * @author: ly
 * @description: ly
 * @date 2018年11月6日 - 下午3:12:13
 */
public class HKPay {

    public static void main(String[] args) throws Exception {
        String agentId="1001228";//机构号
        String prvkey="30820277020100300d06092a864886f70d0101010500048202613082025d02010002818100a025a648753eada169e59001200d4d0fa343eee46acad2e73beeb32b11b6592892101010d8afeea4bf92426403b51463b9b92ca1f7664488612a92b0955134ec2a4a1751495c863aceb4fdd13beb9c72b12947d4597e42430dffeb4dd961ffa3855ed8da2d96a2018c9d9f9f10aab8f318a7a94c0bb43a08389b7e191f1e45cb0203010001028181008922b459a7cc9c72747417f45eb6051f36f79d420c10d2b920212f5bda3703f231e17f606dad79f5a9f65cd3b838ffd4c5dd43d30e3f8cc01c34909895c48c43af3081d9ed049e70941ffb8e48fe057ba383f265ff20440c8ef74d2be748c10b6cad205fd3491b08c21c387399e3bb8f7b1e7aa4cc4f78e092a2cd46d3a97981024100f2528a5f95e0b05e36eeaede807bc8474548f4801359cf50cb00402b748a98d20577bfa084c3781ae30cac78e3124f4890779c0379cd00c5fd27a446343abd69024100a92fb57a1e32527fb4b612032b7ce67b4cee913fd4b15f126f5b5b6b241e1fbf3a16b2fc302c8b12f111fa697f57de2ca2e8f929276ed867cde26cfe606e9f13024046f4705c28d0d013e655e1927960c576490d61ba96eff74a9b0689c3afae5dad31b8acbddde236aaf8ccdf3bce91aff1798bec7d000f4d28ac460d4e194111f102405f4c2b49ad06a5679fafcab794605726eeaf968b40cfe4006b4f27505358d083134243f13f6567b4f4ab2ec50be0bf999eede1a2e7be4d11336c2dfe9eecd09702410087998e9f1d337a34478a9583a5097021f85663a349491f10c436e0b3f1774b88d3ec073641833e74e8c32d5ddf7a7c419eecd7662993afd12d6d3fd31394ceaf";//密钥
        String idcard="112222111";//身份证
        String name="kl";//姓名
        String phone="18278058438";//手机号
        String bankId="309391000011";//联行号
        String bankCard="62284800159892455";//银行卡号  进件默认用借记卡
        String bankCard1="";//绑卡 支付卡号  贷记卡
        String bankName="浦发银行";//银行名称
        String bankNo="CMB";//银行代码
        String rate="30";//费率 对应千3
        String extraFee="50";//手续费 对应每笔6毛
        String merId="";//入网后返回此商户号
        String orderNo="BK"+System.currentTimeMillis()+RandomUtils.generateString(6);//订单号
        String notifyUrl="";//回调地址
        String amount="1000";//交易金额
        System.out.println(report(agentId, idcard, name, phone, bankId, bankCard, bankName, bankNo, rate, extraFee, prvkey));
        //System.out.println(updateMid(agentId,merId,idcard, name, phone, bankId, bankCard, bankName, bankNo, rate, extraFee, prvkey));
        //System.out.println(bindH5(agentId, merId, orderNo, bankCard1, notifyUrl, prvkey));
        //System.out.println(pay(agentId, merId, orderNo, bankCard1, amount, notifyUrl, prvkey));
        //System.out.println(rePay(agentId, merId, orderNo, bankCard1, "947", bankName, prvkey));
        //System.out.println(queryBind(agentId, bankCard1, prvkey));
    }

    /**
     * 入网
     * @param agentId
     * @param idCard
     * @param name
     * @param phone
     * @param bankId
     * @param bankCard
     * @param bankName
     * @param bankNo
     * @param rate
     * @param extraFee
     * @param prvkey
     * @return
     */
    protected static Map<String,String> report(String agentId,String idCard,String name,String phone,String bankId,String bankCard,String bankName,String bankNo,String rate,String extraFee,String prvkey) throws Exception{
        Map<String,String> map=new HashMap<>();
        try {
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put("version", KFTPayConfig.VERSION);//版本号
            paramMap.put("charset", KFTPayConfig.CHARSET);//编码格式
            paramMap.put("signType", KFTPayConfig.SIGNTYPE);//签名方式，固定RSA
            paramMap.put("serviceUri", "HK0001");//交易代码
            paramMap.put("nonceStr",RandomUtils.generateString(6));
            paramMap.put("agentId",agentId);//受理方预分配的渠道代理商标识
            paramMap.put("isCompay","0");//对公对私标识0为对私，1为对公
            paramMap.put("idcardType", KFTPayConfig.IDCARDTYPE);//证件类型 暂只支持 01 身份证
            paramMap.put("idcard", idCard);//证件号码
            paramMap.put("name",name);//姓名
            paramMap.put("phone",phone);//手机号
            paramMap.put("bankId",bankId);//联行号
            paramMap.put("bankCard",bankCard); //银行卡号
            paramMap.put("bankName",bankName);
            paramMap.put("bankNo",bankNo);
            paramMap.put("rate", rate);
            paramMap.put("extraFee",extraFee);

            System.out.println("加签前："+paramMap);

            //String sign=NewParam.signParam(paramMap,prvkey);

            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(hexStrToBytes(prvkey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey myprikey = keyf.generatePrivate(priPKCS8);



            Map<String, Object> filterSignDataMap = VeryfyUtil.paraFilter(paramMap);
            String signInfo = VeryfyUtil.createLinkString(filterSignDataMap);
            System.out.println("RSA加签参数：" + signInfo);

            // 用私钥对信息生成数字签名
            java.security.Signature signet = java.security.Signature.getInstance("MD5withRSA");
            signet.initSign(myprikey);
            signet.update(signInfo.getBytes("ISO-8859-1"));
            byte[] signed = signet.sign(); // 对信息的数字签名

            System.out.println("signed(签名内容)原值=" + bytesToHexStr(signed));
            System.out.println("info（原值）=" + signInfo);

            paramMap.put("sign", bytesToHexStr(signed));//签名数据
            System.out.println("加签后："+paramMap);
            String resString = doPost("http://39.108.137.8:8099/v1.0/facade/hkRoute", paramMap);
            System.out.println("返回报文"+resString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 修改费率
     * @param agentId
     * @param merId
     * @param idCard
     * @param name
     * @param phone
     * @param bankId
     * @param bankCard
     * @param bankName
     * @param bankNo
     * @param rate
     * @param extraFee
     * @param prvkey
     * @return
     */
    protected static Map<String,String> updateMid(String agentId,String merId,String idCard,String name,String phone,String bankId,String bankCard,String bankName,String bankNo,String rate,String extraFee,String prvkey) {
        Map<String,String> map=new HashMap<String, String>();
        try {
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put("version", KFTPayConfig.VERSION);//版本号
            paramMap.put("charset", KFTPayConfig.CHARSET);//编码格式
            paramMap.put("signType", KFTPayConfig.SIGNTYPE);//签名方式，固定RSA
            paramMap.put("serviceUri", "HK0002");//交易代码
            paramMap.put("nonceStr",RandomUtils.generateString(6));
            paramMap.put("agentId",agentId);//受理方预分配的渠道代理商标识
            paramMap.put("merId",merId);//
            paramMap.put("phone",phone);//手机号
            paramMap.put("bankId",bankId);//联行号
            paramMap.put("bankName",bankName);
            paramMap.put("bankCard",bankCard); //银行卡号
            paramMap.put("bankNo",bankNo);
            paramMap.put("rate", rate);
            paramMap.put("extraFee",extraFee);

            System.out.println("加签前："+paramMap);

            String sign=NewParam.signParam(paramMap,prvkey);
            paramMap.put("sign", sign);//签名数据
            System.out.println("加签后："+paramMap);
            String resString = doPost("http://39.108.137.8:8099/v1.0/facade/hkRoute", paramMap);
            System.out.println("返回报文"+resString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 绑卡
     * @param agentId
     * @param merId
     * @param orderNo
     * @param bankCard
     * @param notifyUrl
     * @param prvkey
     * @return
     */
    protected static Map<String,String> bindH5(String agentId,String merId,String orderNo,String bankCard,String notifyUrl,String prvkey) {
        Map<String,String> map=new HashMap<String, String>();
        try {
            Map<String,Object> paramMap=new HashMap<String, Object>();
            paramMap.put("version", KFTPayConfig.VERSION);//版本号
            paramMap.put("charset", KFTPayConfig.CHARSET);//编码格式
            paramMap.put("signType", KFTPayConfig.SIGNTYPE);//签名方式，固定RSA
            paramMap.put("serviceUri", "HK0003");//交易代码
            paramMap.put("nonceStr",RandomUtils.generateString(6));
            paramMap.put("agentId",agentId);//受理方预分配的渠道代理商标识
            paramMap.put("merId",merId);//
            paramMap.put("orderNo",orderNo);
            paramMap.put("bankCard",bankCard);
            paramMap.put("notifyUrl",notifyUrl);

            System.out.println("加签前："+paramMap);

            String sign=NewParam.signParam(paramMap,prvkey);
            paramMap.put("sign", sign);//签名数据
            System.out.println("加签后："+paramMap);
            String resString = doPost("http://39.108.137.8:8099/v1.0/facade/hkRoute", paramMap);
            System.out.println("返回报文"+resString.replaceAll("\\\\" , ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 支付
     * @param agentId
     * @param merId
     * @param orderNo
     * @param bankCard
     * @param amount
     * @param notifyUrl
     * @param prvkey
     * @return
     */
    protected static Map<String,String> pay(String agentId,String merId,String orderNo,String bankCard,String amount,String notifyUrl,String prvkey) {
        Map<String,String> map=new HashMap<String, String>();
        try {
            Map<String,Object> paramMap=new HashMap<String, Object>();
            paramMap.put("version", KFTPayConfig.VERSION);//版本号
            paramMap.put("charset", KFTPayConfig.CHARSET);//编码格式
            paramMap.put("signType", KFTPayConfig.SIGNTYPE);//签名方式，固定RSA
            paramMap.put("serviceUri", "HK0004");//交易代码
            paramMap.put("nonceStr",RandomUtils.generateString(6));
            paramMap.put("agentId",agentId);//受理方预分配的渠道代理商标识
            paramMap.put("merId",merId);//
            paramMap.put("orderNo",orderNo);
            paramMap.put("bankCard",bankCard);
            paramMap.put("amount",amount);
            paramMap.put("notifyUrl",notifyUrl);

            System.out.println("加签前："+paramMap);

            String sign=NewParam.signParam(paramMap,prvkey);
            paramMap.put("sign", sign);//签名数据
            System.out.println("加签后："+paramMap);
            String resString = doPost("http://39.108.137.8:8099/v1.0/facade/hkRoute", paramMap);
            System.out.println("返回报文"+resString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 代付
     * @param agentId
     * @param merId
     * @param orderNo
     * @param bankCard
     * @param amount
     * @param bankName
     * @param prvkey
     * @return
     */
    protected static Map<String,String> rePay(String agentId,String merId,String orderNo,String bankCard,String amount,String bankName,String prvkey) {
        Map<String,String> map=new HashMap<String, String>();
        try {
            Map<String,Object> paramMap=new HashMap<String, Object>();
            paramMap.put("version", KFTPayConfig.VERSION);//版本号
            paramMap.put("charset", KFTPayConfig.CHARSET);//编码格式
            paramMap.put("signType", KFTPayConfig.SIGNTYPE);//签名方式，固定RSA
            paramMap.put("serviceUri", "HK0005");//交易代码
            paramMap.put("nonceStr",RandomUtils.generateString(6));
            paramMap.put("agentId",agentId);//受理方预分配的渠道代理商标识
            paramMap.put("merId",merId);//
            paramMap.put("orderNo",orderNo);
            paramMap.put("bankCard",bankCard);
            paramMap.put("bankName",bankName);
            paramMap.put("amount",amount);

            System.out.println("加签前："+paramMap);

            String sign=NewParam.signParam(paramMap,prvkey);
            paramMap.put("sign", sign);//签名数据
            System.out.println("加签后："+paramMap);
            String resString = doPost("http://39.108.137.8:8099/v1.0/facade/hkRoute", paramMap);
            System.out.println("返回报文"+resString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 查询是否绑卡
     * @param agentId
     * @param bankCard
     * @param prvkey
     * @return
     */
    protected static Map<String,String> queryBind(String agentId,String bankCard,String prvkey) {
        Map<String,String> map=new HashMap<String, String>();
        try {
            Map<String,Object> paramMap=new HashMap<String, Object>();
            paramMap.put("version", KFTPayConfig.VERSION);//版本号
            paramMap.put("charset", KFTPayConfig.CHARSET);//编码格式
            paramMap.put("signType", KFTPayConfig.SIGNTYPE);//签名方式，固定RSA
            paramMap.put("serviceUri", "HK0006");//交易代码
            paramMap.put("nonceStr",RandomUtils.generateString(6));
            paramMap.put("agentId",agentId);//受理方预分配的渠道代理商标识
            paramMap.put("bankCard",bankCard);

            System.out.println("加签前："+paramMap);

            String sign=NewParam.signParam(paramMap,prvkey);
            paramMap.put("sign", sign);//签名数据
            System.out.println("加签后："+paramMap);
            String resString = doPost("http://39.108.137.8:8099/v1.0/facade/hkRoute", paramMap);
            System.out.println("返回报文"+resString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 发送POST请求
     *
     */
    private static String doPost(String url, Map<String, Object> param) throws IOException {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        try {
            if (param != null) {
                // 设置2个post参数
                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                for (String key : param.keySet()) {
                    parameters.add(new BasicNameValuePair(key, (String) param.get(key)));
                }
                // 构造一个form表单式的实体
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
                // 将请求实体设置到httpPost对象中
                httpPost.setEntity(formEntity);
            }

            response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
        return result;
    }
}
