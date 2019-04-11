package springboot.thread.service.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    //异步声明,如果在方法处表示是异步方法，如果在类处表示异步类（所有的方法都是异步方法）。
    // 如果去掉注解则变成同步执行
    //这里的方法自动被注入使用ThreadPoolTaskExecutor作为TaskExecutor（线程池）
    @Async
    public void executorAsyncTask(Integer i) {
        try {
            Thread.sleep(3000);
            System.out.println("执行异步：" + i);
            System.out.println(10/i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
