package com.softserve.actent.notification;

public interface EmailNotification {
    Boolean sendEmail(String email, String subject, String content);
}
