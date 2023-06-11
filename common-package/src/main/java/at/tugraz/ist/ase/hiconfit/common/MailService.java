/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class MailService {
    private final String username;
    private final String password;

    private final Properties props;

    public MailService(String host, String port, String username, String password) {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        this.username = username;
        this.password = password;
    }

    public MailService(String username, String password) {
        this("smtp.gmail.com", "587", username, password);
    }

    public void sendMail(String from, String to, String subject, String body) {
        //create the Session object
        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            log.info("{}Sent an email successfully [from={}, to={}]", LoggerUtils.tab(), from, to);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
