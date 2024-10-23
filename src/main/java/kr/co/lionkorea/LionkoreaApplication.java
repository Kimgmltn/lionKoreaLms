package kr.co.lionkorea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LionkoreaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LionkoreaApplication.class, args);
    }

}
