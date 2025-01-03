package kr.co.lionkorea.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.lionkorea.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;
    private static final String PASSWORD_CHANGE_FORMAT = "/templates/email/password-change-format.html";

    @Override
    public void sendUpdatePasswordEmail(String to, String subject, String resetLink) {
        try {
            // html 양식 찾기
            Resource resource = resourceLoader.getResource("classpath:" + PASSWORD_CHANGE_FORMAT);
            String html = Files.readString(resource.getFile().toPath());
            // html 링크 변경
            html.replace("${resetLink}", resetLink);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(mimeMessage);
            log.info("{}로 메일 전송", to);
        } catch (MailParseException e){
            e.printStackTrace();

            log.error("MailParseException 에러 발생 : {}", e.getMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("MessagingException 에러 발생 : {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("I/O 에러 발생 : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
