package com.alkemy.disney_AlkemyChallenge.Service;

import com.alkemy.disney_AlkemyChallenge.DTO.EmailDTO;
import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendEmail(EmailDTO emailDTO, String pathToAttachment) throws MessagingException;
}
