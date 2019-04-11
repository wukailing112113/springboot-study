package springboot.aop.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 过滤器的配置
 */
@Component
@WebFilter(urlPatterns = "/**",filterName = "filterTest")
public class FilterTest implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("init test");//项目启动的时候执行，之后请求不执行
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException{
        System.out.println("doFilter test");
        filterChain.doFilter(servletRequest, servletResponse);  // 传递给下一个Filter进行处理
    }

    @Override
    public void destroy() {
        System.out.println("destroy test");
    }
}
