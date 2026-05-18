package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.Service.Interface.IEmailService;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
   public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("HEDI - Secure Password Reset");
            
            String htmlContent = "<!DOCTYPE html>"
                    + "<html><head><style>"
                    + "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f0fdf4; margin: 0; padding: 40px 20px; color: #374151; }"
                    + ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 24px; box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05); overflow: hidden; border: 1px solid #e5e7eb; }"
                    + ".header { background-color: #4f9d69; padding: 35px 20px; text-align: center; }"
                    + ".header h1 { color: #ffffff; margin: 0; font-size: 32px; letter-spacing: 2px; font-weight: 900; }"
                    + ".content { padding: 40px 35px; }"
                    + ".content h2 { color: #1f2937; margin-top: 0; font-size: 22px; font-weight: bold; }"
                    + ".content p { font-size: 16px; line-height: 1.6; color: #4b5563; }"
                    + ".button-container { text-align: center; margin: 40px 0; }"
                    + ".button { background-color: #4f9d69; color: #ffffff !important; padding: 16px 32px; text-decoration: none; border-radius: 14px; font-weight: bold; display: inline-block; font-size: 16px; box-shadow: 0 4px 6px -1px rgba(79, 157, 105, 0.3); }"
                    + ".link-box { font-size: 13px; word-break: break-all; color: #4f9d69; background-color: #e6fbf0; padding: 16px; border-radius: 12px; margin-top: 10px; border: 1px solid #bcffdb; }"
                    + ".footer { background-color: #f9fafb; padding: 24px; text-align: center; font-size: 13px; color: #9ca3af; border-top: 1px solid #f3f4f6; }"
                    + "</style></head><body>"
                    + "<div class='container'>"
                    + "  <div class='header'>"
                    + "    <h1>HEDI</h1>"
                    + "  </div>"
                    + "  <div class='content'>"
                    + "    <h2>Password Reset Request</h2>"
                    + "    <p>Hello,</p>"
                    + "    <p>We received a request to reset the password for your HEDI health tracking account. If you initiated this request, please click the button below to securely set a new password.</p>"
                    + "    <div class='button-container'>"
                    + "      <a href='" + resetLink + "' class='button'>Reset My Password</a>"
                    + "    </div>"  
                    + "    <p>This link is only valid for <strong>24 hours</strong>. If you did not request a password reset, you can safely ignore this email. Your account remains secure.</p>"
                    + "    <p style='margin-top: 30px;'>Stay healthy,<br><strong style='color: #4f9d69;'>The MinhDZ</strong></p>"
                    + "  </div>"
                    + "  <div class='footer'>"
                    + "    &copy; 2026 HEDI. Aight reversed."
                    + "  </div>"
                    + "</div>"
                    + "</body></html>";
            
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            System.out.println("Branded HTML Reset email successfully sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send HTML email to " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Failed to send email. Please try again later.");
        }
    }
}