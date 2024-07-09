package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.DTO.EmailDTO;
import com.alkemy.disney_AlkemyChallenge.Service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements IEmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(EmailDTO emailDTO, String pathToAttachment) throws MessagingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setTo(emailDTO.getTo());
            mimeMessageHelper.setSubject(emailDTO.getSubject());
            mimeMessageHelper.setText(emailDTO.getBody(), true);

            // Añadir imagen incrustada
            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            mimeMessageHelper.addInline("disneyImage", file);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
