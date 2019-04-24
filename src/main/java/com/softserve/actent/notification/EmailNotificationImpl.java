package com.softserve.actent.notification;

import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.verification.service.SendEmail;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Log4j2
@Service
public class EmailNotificationImpl implements EmailNotification {
    @Override
    public Boolean sendEmail(String email, String subject, String content) {

        MimeMessage mimeMessage = getMimeMessage(email, subject, content);
        try {
            Transport.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            log.error("Cant sand message");
            return false;
        }
    }

    private static MimeMessage getMimeMessage(String to, String subject, String text) {

        MimeMessage message = new MimeMessage(getSession());

        try {
            InternetAddress[] address = InternetAddress.parse(to, true);
            message.setRecipients(Message.RecipientType.TO, address);
            message.setFrom();
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(text);

        } catch (MessagingException mex) {
            log.error("Cant create message");
        }

        return message;
    }

    private static Session getSession() {

        final Session session = Session.getInstance(getBaseProperties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(StringConstants.EMAIL_LOGIN, StringConstants.EMAIL_PASSWORD);
                    }
                });

        return session;
    }

    private static Properties getBaseProperties() {

        final Properties properties = new Properties();

        try {
            properties.load(SendEmail.class.getClassLoader().getResourceAsStream("mail.properties"));
        } catch (IOException e) {
            log.error("Cant download main.properties file");
        }

        return properties;
    }
}
