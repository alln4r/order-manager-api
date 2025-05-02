package com.ordermanager.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EmailService {
    private static final Logger logger = LogManager.getLogger(EmailService.class);

    public void sendOrderNotification(String recipientEmail, String userName, Long orderId, String completedPendente) {
        String message = String.format("Simulating email to %s (%s) for order #%d %s.",
                userName, recipientEmail, orderId, completedPendente);

        // Log instead of sending real email
        logger.info(message);
        System.out.println(message);
    }
}

