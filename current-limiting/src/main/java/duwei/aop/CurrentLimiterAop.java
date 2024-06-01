package duwei.aop;

import duwei.config.MyRedisLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class CurrentLimiterAop {
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    private static DefaultRedisScript<Number> redisScript;

    static{
        String script = script ="local count" +
                "\ncount = redis.call('get',KEYS[1])" +
                // 不超过最大值，则直接返回
                "\nif count and tonumber(count) > tonumber(ARGV[1]) then" +
                "\nreturn count;" +
                "\nend" +
                // 执行计算器自加
                "\ncount = redis.call('incr',KEYS[1])" +
                "\nif tonumber(count) == 1 then" +
                // 从第一次调用开始限流，设置对应键值的过期
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn count;";

        redisScript = new DefaultRedisScript<Number>();
        redisScript.setResultType(Number.class);
        redisScript.setScriptText(script);


    }

    /**
     * 切面
     * @return
     */
    @Around("@annotation(duwei.config.MyRedisLimiter)")
    public Object limit(ProceedingJoinPoint joinPoint){

        System.out.println(1);
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MyRedisLimiter annotation = method.getAnnotation(MyRedisLimiter.class);

        String key = annotation.key();
        int limitCount = annotation.limitCount();
        String prefix = annotation.prefix();
        int period = annotation.period();

        String key1 = prefix + key;
        List<String> list = new ArrayList<>();
        list.add(key1);

        //执行lua脚本语言
        try {
            Number execute = redisTemplate.execute(redisScript, list, limitCount+"", period+"");

            if(execute !=null && execute.intValue()<=limitCount){

               return joinPoint.proceed();

            }else{
                throw new RuntimeException("执行出错");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;


    }



}
