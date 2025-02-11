package pfe.HumanIQ.HumanIQ.emailConfig;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);

    // Method 2
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details, MultipartFile attachment);

}
