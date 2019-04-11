package springboot.aop.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 拦截器
 * 实现一个interceptor拦截器类后，需要在配置中配置使它生效：实现 WebMvcConfigurerAdapter并重写 addInterceptors，同时在这个方法里设置要过滤的URL。
 */
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    private static final Log log = LogFactory.getLog(LogInterceptor.class);

    /**
     * preHandle方法将在请求处理之前调用，SpringMVC中的Interceptor是链式调用的，
     * 每个Interceptor的调用都根据它的声明顺序依次执行，且最先执行其preHandle方法，
     * 所以可以在该方法中进行一些前置初始化操作或是预处理。该方法的返回值是布尔类型，
     * 如果返回false，表示请求结束，后续的Interceptor和Controller都不会再执行了，
     * 如果返回true就执行下一个拦截器的preHandle方法，一直到最后一个拦截器preHandle方法执行完成后调用当前请求的Controller方法。
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        request.setAttribute("REQUEST_START_TIME",new Date());
        return true;
    }

    /**
     * postHandle方法是在当前请求进行处理之后，也就是Controller方法调用结束之后执行，
     * 但是它会在DispatcherServlet进行视图渲染之前被调用，所以可以在这个方法中可以对Controller处理之后的ModelAndView对象进行操作。
     * postHandle方法被调用的方向跟preHandle是相反的，也就是说先声明的Interceptor的postHandle方法反而后执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * afterCompletion方法需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。
     * 该方法会在整个请求结束之后，也就是在DispatcherServlet渲染了对应的视图之后执行，这个方法的主要作用是用于资源清理工作。
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Date start = (Date) request.getAttribute("REQUEST_START_TIME");
        Date end = new Date();

        log.info("本次请求耗时：" + (end.getTime() - start.getTime()) + "毫秒；");

    }
}
