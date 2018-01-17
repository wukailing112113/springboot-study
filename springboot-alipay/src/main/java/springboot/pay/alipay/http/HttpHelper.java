package springboot.pay.alipay.http;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.util.StringUtil;
import springboot.pay.alipay.impl.Channel;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2017/3/30.
 */
public class HttpHelper {
    /**
     * 获取请求消息头，如果ReqEntity里面没有，则从RequestHeader里面取。
     *
     * @param request
     * @param reqEntity
     * @return
     */
    public static ReqHeader getReqHeader(HttpServletRequest request, ReqEntity reqEntity) {

        ReqHeader reqHead;

        if (null != reqEntity && null != reqEntity.getReqHeader()) {
            reqHead = reqEntity.getReqHeader();
        } else {
            reqHead = new ReqHeader();
        }
        String tmpStr;
        if (Objects.isNull(reqHead.getChannel())) { // 从HttpHeader里面获取
            tmpStr = request.getHeader(ReqHeader.CHANNEL);
            if (StringUtils.isEmpty(tmpStr)) {
                reqHead.setChannel(Channel.PC.getCode());
            } else {
                reqHead.setChannel(Byte.parseByte(tmpStr));
            }
        }
        if (Objects.isNull(reqHead.getPlatform())) {
            tmpStr = request.getHeader(ReqHeader.PLATFORM);
            if (StringUtils.isEmpty(tmpStr)) {
                reqHead.setPlatform((byte)1);
            } else {
                reqHead.setPlatform(Byte.parseByte(tmpStr));
            }
        }
        if (Objects.isNull(reqHead.getIndustry())) {
            tmpStr = request.getHeader(ReqHeader.INDUSTRY);
            if (StringUtils.isEmpty(tmpStr)) {
                reqHead.setIndustry(-1);
            } else {
                reqHead.setIndustry(Integer.parseInt(tmpStr));
            }
        }
        if (Objects.isNull(reqHead.getTrackId())) {
            tmpStr = request.getHeader(ReqHeader.TACKID);
            if (!StringUtils.isEmpty(tmpStr)) {
            } else {
                reqHead.setTrackId(tmpStr);
            }
        }

        return reqHead;
    }

    /**
     * 获取请求头行业
     */
    public static Integer getIndustFromRequest(HttpServletRequest request, ReqEntity reqEntity) {
        ReqHeader reqHeader = getReqHeader(request, reqEntity);
        return Objects.isNull(reqHeader.getIndustry()) ? -1 : reqHeader.getIndustry();
    }

    /**
     * 获取请求来源终端和平台
     */
    public static Byte getChannelFromRequest(HttpServletRequest request, ReqEntity reqEntity) {
        ReqHeader reqHeader = HttpHelper.getReqHeader(request, reqEntity);
        return reqHeader.getChannel();
    }

    /**
     * 获取访问者IP
     * <p>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    public static String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (null != ip && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (null != ip && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    /**
     * 判断是否ajax请求
     *
     * @param httpRequest
     * @return
     */
    public static boolean isAjax(HttpServletRequest httpRequest) {
        return httpRequest.getHeader("accept").contains("application/json") ||
                (httpRequest.getHeader("X-Requested-With") != null &&
                        httpRequest.getHeader("X-Requested-With").contains("XMLHttpRequest"));
    }

    /**
     * 输出文字到前台浏览器
     * @param httpResponse
     * @throws IOException
     */
    public static void sendHttpReponseString(HttpServletResponse httpResponse, String retString, String contentType) throws IOException {
        httpResponse.setContentType(contentType == null? "application/json":contentType);
        PrintWriter out = httpResponse.getWriter();
        out.write(retString);
        out.close();
    }

    /**
     * 从trackId中获取uid
     * @param trackId
     * @return
     */
//    public static Integer getUidFromTrackId(String trackId) throws UnsupportedEncodingException {
//        if (trackId == null || trackId.length() == 36) { //如果是UUID加密出来的信息，
//            return null;
//        }
//        byte[] aesKeyByte;
//        AesCipherService aesCipherService = new AesCipherService();
//        aesCipherService.setKeySize(64); //设置key长度
//        aesKeyByte = Base64.decode("PPkZYjJ4vIP4W0K7rt/RZw==");
//        byte[] sessionDecode = Base64.decode(URLDecoder.decode(trackId, "utf-8"));
//        String encodeTrackId = new String(aesCipherService.decrypt(sessionDecode, aesKeyByte).getBytes());
//        String[] trackIds = org.apache.commons.lang3.StringUtils.split(encodeTrackId, "-");
//        if (trackIds == null || trackIds.length < 4) {
//            return null;
//        }
//        return Integer.valueOf(trackIds[0]);
//    }

    /**
     * 获取请求参数中所有的信息
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> getAllRequestParam(final HttpServletRequest request, String encoding) throws UnsupportedEncodingException {
        request.setCharacterEncoding(encoding);
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String key = (String) temp.nextElement();
                String value = request.getParameter(key);
                if (null != value){
                    res.put(key, value);
                }
            }
        }
        return res;
    }

    /**
     * 获取知道key-value的cookie 设置path /
     * @param key
     * @param value
     * @return
     */
    public static Cookie getCookie(String key, Object value) {
        Cookie cookie = new Cookie(key,Objects.isNull(value) ? "" : value.toString());
        cookie.setPath("/");
        return cookie;
    }

    /**
     * 将json对象发送到前台
     * @param httpResponse
     * @param jsonData
     * @throws IOException
     */
    public static void sendJson(HttpServletResponse httpResponse, JSONObject jsonData) throws IOException {
        httpResponse.setContentType("application/json");
//		String data = WebUtil.formatResponseParams(UserCodeDefine.USER_NOT_EXIST, jsonData, null);
        PrintWriter out = httpResponse.getWriter();
        out.write(jsonData.toJSONString());
        out.close();
    }
}
