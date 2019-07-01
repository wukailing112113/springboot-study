package springboothuilianpay.huilian.utils;

import java.util.Map;
import java.util.TreeMap;
import org.springframework.util.StringUtils;


public class NewParam {
	public static void main(String[] args) {
	}
	
	/**
	 * 加签
	 */
	public static String signParam(Map<String, Object> map, String key) {
		TreeMap<String, Object> param = new TreeMap<String, Object>(map);
		String signInfo = "";
		for (String pkey : param.keySet()) {
			if(StringUtils.isEmpty(param.get(pkey)) || "sign".equals(pkey)) {
				continue;
			}
			signInfo += pkey+"="+param.get(pkey)+"&";
		}
		signInfo = signInfo.substring(0, signInfo.length() - 1);
		System.out.println("RSA加签参数：" + signInfo);
		String sign = null;
		try {
			byte[] data = RSAUtils.encryptByPrivateKey(signInfo.getBytes(), key);
			sign = Base64.encode(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}
}
