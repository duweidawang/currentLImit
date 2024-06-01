package duwei;


import com.google.common.util.concurrent.RateLimiter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 这是一个基于RateLimiter组件实现的令牌桶算法限流
 * 具有详细的使用情况
 */
public class CurrentLimit {

    public static void main(String[] args) {
        //线程池
        ExecutorService pool = Executors.newCachedThreadPool();
        //速率是每秒只有3个许可
        final RateLimiter rateLimiter = RateLimiter.create(12);

        for (int i = 0; i < 100; i++) {
            final int no = i;
            pool.execute(()->{
                rateLimiter.acquire();
                System.out.println("Accessing: " + no + ",time:"
                        + new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()));

            });

        }
        //退出线程池
        pool.shutdown();

    }
}
