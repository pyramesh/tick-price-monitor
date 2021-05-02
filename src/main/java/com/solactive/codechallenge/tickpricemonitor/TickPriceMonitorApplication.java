package com.solactive.codechallenge.tickpricemonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Transactional
public class TickPriceMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(TickPriceMonitorApplication.class, args);
    }

}
