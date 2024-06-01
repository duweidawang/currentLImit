package duwei;

import duwei.controller.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CurrentLimitApplication {
    public static void main(String[] args) {

        SpringApplication.run(CurrentLimitApplication.class,args);




    }
}
