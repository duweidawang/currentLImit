package duwei.config;


import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MyRedisLimiter {
    //缓存使用的key
    String key();
    //key的前缀
    String prefix() default "limiter: ";
    //过期时间
    int period() default 1;
    //限流大小
    int limitCount();

}


