package com.bank.consumer.config;

import com.bank.consumer.service.TransferProcessingService;
import com.bank.consumer.listener.KafkaMessageListener;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"com.bank.consumer", "com.bank.common"})
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class ConsumerConfig {

    @Bean
    public TransferProcessingService transferProcessingService() {
        return new TransferProcessingService();
    }

    @Bean
    public KafkaMessageListener kafkaMessageListener() {
        return new KafkaMessageListener();
    }
}