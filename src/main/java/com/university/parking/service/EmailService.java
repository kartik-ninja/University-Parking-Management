package com.university.parking.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
