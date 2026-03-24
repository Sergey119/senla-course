package com.bank.consumer;

import com.bank.consumer.config.ConsumerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConsumerApplication {
    private static final Logger logger = LogManager.getLogger(ConsumerApplication.class);
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(ConsumerConfig.class);
        ctx.start();
        logger.info("Consumer application started");
    }
}