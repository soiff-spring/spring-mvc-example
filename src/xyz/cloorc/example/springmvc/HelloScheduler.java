package xyz.cloorc.example.springmvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by zhangh on 12/1/2016.
 *
 * @author : wittcnezh@foxmail.com
 * @version : 1.0.0
 * @since : 1.8
 */
@Slf4j
//@Component
public class HelloScheduler {

    @Scheduled(cron = "0 * * * * ?")
    public void hello() {
        log.info("{} : hello world ...", new Date().getTime());
    }
}
