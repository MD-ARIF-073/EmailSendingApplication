package com.example.userservice.service.impl;

import com.example.userservice.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.example.userservice.utils.EmailUtils.getEmailMessage;
import static com.example.userservice.utils.EmailUtils.getVerificationUrl;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
 //   public static final String UTF_8_ENCODING = "UTF_8";
    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final String EMAILTEMPLATE = "emailtemplate";
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;


    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {           // token is for generating url to send with specific email so that the user can just click on it and they can verify it
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getEmailMessage(name, host, token));
            emailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8.name());
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getEmailMessage(name, host, token));

            // Add Atachments
            // Adding the files inside of the mail as attachments and sent that mail

            Resource amiyakhumResource = new UrlResource("file:///home/khenshin_arif/Pictures/amiyakhum.jpg");
            Resource bogalakeResource = new UrlResource("file:///home/khenshin_arif/Pictures/Bogalake.jpg");
            helper.addAttachment(amiyakhumResource.getFilename(), amiyakhumResource);
            helper.addAttachment(bogalakeResource.getFilename(), bogalakeResource);

            emailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

    @Override
    @Async
    public void sendMimeMessageWithEmbeddedImages(String name, String to, String token) {

        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8.name());
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getEmailMessage(name, host, token));

            // Add Atachments
            // Adding the files inside of the mail as attachments and sent that mail

            Resource amiyakhumResource = new UrlResource("file:///home/khenshin_arif/Pictures/amiyakhum.jpg");
            Resource bogalakeResource = new UrlResource("file:///home/khenshin_arif/Pictures/Bogalake.jpg");
            helper.addInline(getContentId(amiyakhumResource.getFilename()), amiyakhumResource);
            helper.addInline(getContentId(bogalakeResource.getFilename()), bogalakeResource);

            emailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

    }

    @Override
    @Async
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {

        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8.name());
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getEmailMessage(name, host, token));

            // Add Atachments
            // Adding the files inside of the mail as attachments and sent that mail

            Resource amiyakhumResource = new UrlResource("file:///home/khenshin_arif/Pictures/amiyakhum.jpg");
            Resource bogalakeResource = new UrlResource("file:///home/khenshin_arif/Pictures/Bogalake.jpg");
            helper.addInline(getContentId(amiyakhumResource.getFilename()), amiyakhumResource);
            helper.addInline(getContentId(bogalakeResource.getFilename()), bogalakeResource);

            emailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

    }

    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {

        try {
            Context context = new Context();
//            context.setVariable("name", name);
//            context.setVariable("url", getVerificationUrl(host, token));
            context.setVariables(Map.of("name", name, "url", getVerificationUrl(host, token)));
            String text = templateEngine.process(EMAILTEMPLATE, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8.name());
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);  // HTML text
        //  Add attachments
            Resource amiyakhumResource = new UrlResource("file:///home/khenshin_arif/Pictures/amiyakhum.jpg");
            Resource bogalakeResource = new UrlResource("file:///home/khenshin_arif/Pictures/Bogalake.jpg");
            helper.addAttachment(amiyakhumResource.getFilename(), amiyakhumResource);
            helper.addAttachment(bogalakeResource.getFilename(), bogalakeResource);

            emailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

    }


    private String getContentId(String filename) {
        return "<" + filename + ">";
    }

}
