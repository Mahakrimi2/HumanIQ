package pfe.HumanIQ.HumanIQ.emailConfig;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final String sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String sender) {
        this.javaMailSender = javaMailSender;
        this.sender = sender;
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details, MultipartFile attachment) {
        if (details.getRecipient() == null || details.getMsgBody() == null || details.getSubject() == null || attachment == null) {
            throw new IllegalArgumentException("Error: Recipient, message body, subject, and attachment cannot be null.");
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            mimeMessageHelper.addAttachment(attachment.getOriginalFilename(), attachment);

            javaMailSender.send(mimeMessage);
            return "Mail Sent Successfully...";
        } catch (MessagingException e) {
            return "Error while Sending Mail with Attachment: " + e.getMessage();
        }
    }

    @Override
    public String sendSimpleMail(EmailDetails details) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendSimpleMail'");
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMailWithAttachment'");
    }
}