package kr.co.lionkorea.service;

public interface EmailService {

    void sendUpdatePasswordEmail(String to, String subject, String text);
}
