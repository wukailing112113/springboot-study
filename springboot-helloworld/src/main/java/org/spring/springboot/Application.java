package org.spring.springboot;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot 应用启动类
 *
 * Created by bysocket on 16/4/26.
 */
// Spring Boot 应用的标识
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        SpringApplication.run(Application.class,args);
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
