package bg.duosoft.uniapplicationdemo.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);

    void sendEmailAsync(String to, String subject, String body);
}
