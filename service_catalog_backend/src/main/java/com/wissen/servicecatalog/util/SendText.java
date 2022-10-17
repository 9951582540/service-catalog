package com.wissen.servicecatalog.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SendText {

    @Autowired
    private JavaMailSender emailSender;

    public void send(String from, String password, String[] to, String sub, Object body) throws MessagingException {
	MimeMessage message = emailSender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	helper.setTo(to);
	helper.setSubject(sub);
	helper.setFrom(from);

	helper.setText(body.toString(), true);

	if (emailSender instanceof JavaMailSenderImpl) {
	    ((JavaMailSenderImpl) emailSender).setPassword(password);
	}
	emailSender.send(message);
    }
}