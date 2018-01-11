package springboot.pay;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by Sawyer on 2018/1/9.
 */
@SpringBootApplication
public class AlipayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlipayApplication.class,args);
    }


    /**
     * 手动注入HttpClientBuilder RequestConfig 实例，因为他们不属于spring的容器管理范围
     * @return
     */
    @Bean
    public HttpClientBuilder httpClientBuilder(){
        return HttpClientBuilder.create();
    }

    @Bean
    public RequestConfig requestConfig(){
        return RequestConfig.DEFAULT;
    }
}
