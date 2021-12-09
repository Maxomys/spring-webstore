package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.domain.Inquiry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.address}")
    public static final String EMAIL_ADDRESS = "";
    private final JavaMailSender emailSender;
    private final TaskExecutor taskExecutor;

    public EmailService(JavaMailSender emailSender, TaskExecutor taskExecutor) {
        this.emailSender = emailSender;
        this.taskExecutor = taskExecutor;
    }

    public void sendEmail(Inquiry inquiry) {
        taskExecutor.execute(() -> sendEmail1(inquiry));
    }

    //todo xd
    public void sendEmail1(Inquiry inquiry) {
        String userEmail = inquiry.getProduct().getUser().getEmail();
        String subject = inquiry.getProduct().getName();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_ADDRESS);
        message.setTo(userEmail);
        message.setSubject(subject);
        message.setText(inquiry.getMessageBody() + "\n \n" + "Sent from: " + inquiry.getEmail());

        emailSender.send(message);
    }

}
