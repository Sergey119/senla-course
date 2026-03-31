package com.bank.producer.config;

import com.bank.producer.service.AccountService;
import com.bank.producer.service.MessageProducerService;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"com.bank.producer", "com.bank.common"})
@EnableScheduling
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class ProducerConfig {

    @Bean
    public AccountService accountService() {
        return new AccountService();
    }

    @Bean
    public MessageProducerService messageProducerService() {
        return new MessageProducerService();
    }

    @Bean
    public ApplicationStartupListener applicationStartupListener() {
        return new ApplicationStartupListener();
    }
}