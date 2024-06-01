package duwei.controller;

import duwei.config.MyRedisLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Test {

    @GetMapping("find")
    @MyRedisLimiter(key = "find",limitCount = 10,period = 1)
    public String find(){
        return "okokok";
    }

}
