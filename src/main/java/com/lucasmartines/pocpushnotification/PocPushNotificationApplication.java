package com.lucasmartines.pocpushnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PocPushNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(PocPushNotificationApplication.class, args);
    }

}
