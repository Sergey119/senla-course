package com.bank.producer;

import com.bank.producer.config.ProducerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ProducerApplication {
    private static final Logger logger = LogManager.getLogger(ProducerApplication.class);
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(ProducerConfig.class);
        ctx.start();
        logger.info("Producer application started");
    }
}