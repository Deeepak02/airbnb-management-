package com.airbnb.service;

import com.twilio.rest.api.v2010.account.Message;

import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    public void sendSms(String to, String body) {
       try {
           logger.info("Starting to send SMS - " +new Date());
           Message.creator(
                   new PhoneNumber(to), // Recipient's phone number
                   new PhoneNumber(twilioPhoneNumber), // Twilio phone number
                   body // Message body
           ).create();
           logger.info("sent SMS - "+new Date());


       }catch (Exception e){
           logger.error(e.getMessage());

       }
    }
}
