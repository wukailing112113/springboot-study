package springboot.thread.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


// 声明这是一个配置类
@Configuration
// 引入springboot.thread.service.async下面的@service,@component,@repository,@controller注册为bean
@ComponentScan("springboot.thread.service.async")
//开启注解，开启异步支持
@EnableAsync

//配置类实现AsyncConfigurer接口并重写AsyncConfigurer方法，并返回一个ThreadPoolTaskExecutor
//这样我们就得到了一个基于线程池的TaskExecutor
public class TaskExecutorConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        // TODO Auto-generated method stub
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //如果池中的实际线程数小于corePoolSize,无论是否其中有空闲的线程，都会给新的任务产生新的线程
        taskExecutor.setCorePoolSize(5);
        //连接池中保留的最大连接数。Default: 15 maxPoolSize
        taskExecutor.setMaxPoolSize(10);
        //queueCapacity 线程池所使用的缓冲队列
        taskExecutor.setQueueCapacity(25);
        taskExecutor.initialize();
        return taskExecutor;

    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}