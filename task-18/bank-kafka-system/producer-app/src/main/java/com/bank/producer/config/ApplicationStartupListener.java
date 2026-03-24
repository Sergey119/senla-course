package com.bank.producer.config;

import com.bank.producer.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener {
    private static final Logger logger = LogManager.getLogger(ApplicationStartupListener.class);

    @Autowired
    private AccountService accountService;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        logger.info("Application context refreshed, initializing accounts...");
        try {
            accountService.initializeAccounts();
            logger.info("Accounts initialization completed successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize accounts", e);
            throw new RuntimeException("Failed to initialize accounts", e);
        }
    }
}