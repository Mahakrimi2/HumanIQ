package pfe.HumanIQ.HumanIQ.emailConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String sendMail(
            @RequestParam("recipient") String recipient,
            @RequestParam("subject") String subject,
            @RequestParam("msgBody") String msgBody,
            @RequestParam("attachment") MultipartFile attachment) {

        EmailDetails details = new EmailDetails();
        details.setRecipient(recipient);
        details.setSubject(subject);
        details.setMsgBody(msgBody);

        return emailService.sendMailWithAttachment(details, attachment); // Pass attachment separately
    }
}