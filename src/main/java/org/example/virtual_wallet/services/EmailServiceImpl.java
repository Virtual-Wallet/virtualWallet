package org.example.virtual_wallet.services;

import org.example.virtual_wallet.services.contracts.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final static String VERIFICATION_URL = "http://localhost:8080/transaction/verify/";

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText("Please confirm your email using the token: " + message);

       this.mailSender.send(simpleMailMessage);
    }

    @Override
    public void sendTransactionEmail(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        String content = "<a href='" + VERIFICATION_URL + "'>Verify</a>";

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText("The token for your large transaction is: " + message
        + "\n Please click on the following link to complete your registration: \n" + content
                        + "\n Please note the token will expire in 5 min!");

        this.mailSender.send(simpleMailMessage);
    }

}
