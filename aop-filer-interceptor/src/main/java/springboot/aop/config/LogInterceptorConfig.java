package springboot.aop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springboot.aop.interceptor.LogInterceptor;

@Configuration
public class LogInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LogInterceptor logInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/prod/*")//对所有请求都拦截，但是排除了/swagger-ui/**,//swagger-resources,
                .excludePathPatterns("/swagger-ui/**", "/swagger-resources", "/doc.html");
    }
}
