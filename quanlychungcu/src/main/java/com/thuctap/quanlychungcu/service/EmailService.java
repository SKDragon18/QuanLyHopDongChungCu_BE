package com.thuctap.quanlychungcu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendSimpleEmail(String to, String subject, String text) throws MessagingException{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    // public void sendHtmlEmail(String to, String subject, String text) throws MessagingException{
    //     MimeMessage message = mailSender.createMimeMessage();
    //     MimeMessageHelper helper = new MimeMessageHelper(message,true);
    //     helper.setTo(to);
    //     helper.setSubject(subject);
    //     helper.setText(text,true);
    //     mailSender.send(message);
    // }
}
