package org.spring.springboot.common;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.springboot.Application;
import org.spring.springboot.controller.CityRestController;
import org.spring.springboot.dao.CityDao;
import org.spring.springboot.domain.City;
import org.spring.springboot.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Sawyer on 2017/9/19.
 *
 *
 *
 * MockMvcRequestBuilders主要API：
 MockHttpServletRequestBuilder get(String urlTemplate, Object... urlVariables)：根据uri模板和uri变量值得到一个GET请求方式的MockHttpServletRequestBuilder；如get("/user/{id}", 1L)；
 MockHttpServletRequestBuilder post(String urlTemplate, Object... urlVariables)：同get类似，但是是POST方法；
 MockHttpServletRequestBuilder put(String urlTemplate, Object... urlVariables)：同get类似，但是是PUT方法；
 MockHttpServletRequestBuilder delete(String urlTemplate, Object... urlVariables) ：同get类似，但是是DELETE方法；
 MockHttpServletRequestBuilder options(String urlTemplate, Object... urlVariables)：同get类似，但是是OPTIONS方法；
 MockHttpServletRequestBuilder request(HttpMethod httpMethod, String urlTemplate, Object... urlVariables)：提供自己的Http请求方法及uri模板和uri
 *
 *
 * MockHttpServletRequestBuilder API：
 MockHttpServletRequestBuilder header(String name, Object... values)/MockHttpServletRequestBuilder headers(HttpHeaders httpHeaders)：添加头信息；
 MockHttpServletRequestBuilder contentType(MediaType mediaType)：指定请求的contentType头信息；
 MockHttpServletRequestBuilder accept(MediaType... mediaTypes)/MockHttpServletRequestBuilder accept(String... mediaTypes)：指定请求的Accept头信息；
 MockHttpServletRequestBuilder content(byte[] content)/MockHttpServletRequestBuilder content(String content)：指定请求Body体内容；
 MockHttpServletRequestBuilder cookie(Cookie... cookies)：指定请求的Cookie；
 MockHttpServletRequestBuilder locale(Locale locale)：指定请求的Locale；
 MockHttpServletRequestBuilder characterEncoding(String encoding)：指定请求字符编码；
 MockHttpServletRequestBuilder requestAttr(String name, Object value) ：设置请求属性数据；
 MockHttpServletRequestBuilder sessionAttr(String name, Object value)/MockHttpServletRequestBuilder sessionAttrs(Map<String, Object> sessionAttributes)：设置请求session属性数据；
 MockHttpServletRequestBuilder flashAttr(String name, Object value)/MockHttpServletRequestBuilder flashAttrs(Map<String, Object> flashAttributes)：指定请求的flash信息，比如重定向后的属性信息；
 MockHttpServletRequestBuilder session(MockHttpSession session) ：指定请求的Session；
 MockHttpServletRequestBuilder principal(Principal principal) ：指定请求的Principal；
 MockHttpServletRequestBuilder contextPath(String contextPath) ：指定请求的上下文路径，必须以“/”开头，且不能以“/”结尾；
 MockHttpServletRequestBuilder pathInfo(String pathInfo) ：请求的路径信息，必须以“/”开头；
 MockHttpServletRequestBuilder secure(boolean secure)：请求是否使用安全通道；
 MockHttpServletRequestBuilder with(RequestPostProcessor postProcessor)：请求的后处理器，用于自定义一些请求处理的扩展点；

 MockMultipartHttpServletRequestBuilder继承自MockHttpServletRequestBuilder，又提供了如下API：
 MockMultipartHttpServletRequestBuilder file(String name, byte[] content)/MockMultipartHttpServletRequestBuilder file(MockMultipartFile file)：指定要上传的文件；
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class JunitTest {

    @Autowired
    private CityService cityService;
    //@Autowired
    private MockMvc mockMvc;

    @Autowired
    private CityDao cityDao;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new CityRestController()).build();
    }


    @Test
    public void test1(){
        City city = cityDao.findById((long) 1);
        System.out.println(city.getCityName());
    }

    @Test
    public void testHttpReq() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/test/1")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("====="+mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testAddCity() throws Exception{
        JSONObject jsonObject = new JSONObject(){{put("id",1);put("cityName","guangxi");}};
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/addcity")
                .header("uuid","1123456")
                .contentType(APPLICATION_JSON)
                .content(jsonObject.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("====="+mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void test() throws Exception{
        JSONObject jsonObject = new JSONObject(){{put("id",1);put("cityName","wkl");}};
        System.out.println(jsonObject.toString());
    }
}
