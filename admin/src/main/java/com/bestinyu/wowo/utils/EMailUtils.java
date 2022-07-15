package com.bestinyu.wowo.utils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class EMailUtils {

    public static void send(String host, String port, String transportProtocol, String sender, String sendPwd,
                            String[] recipients, String[] coptTo, String title,
                            String filePath, String fileName, String content) throws MessagingException, UnsupportedEncodingException {
        // 防止长文件名分割
        System.setProperty("mail.mime.splitlongparameters", "false");

        Properties properties = new Properties();
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.host", host);
        properties.setProperty("mail.smtp.port", port);

        properties.setProperty("mail.smtp.ssl.enable", "true");
        // properties.setProperty("mail.smtp.ssl.trust", "*");

        properties.setProperty("mail.transport.protocol", transportProtocol);
        properties.setProperty("mail.mime.splitlongparameters", "false");
        properties.setProperty("mail.mime.chartset", "UTF-8");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, sendPwd);
            }
        });
        session.setDebug(true);

        Message message = new MimeMessage(session);

        message.setSubject(title);
        message.setSentDate(new Date());
        message.setFrom(new InternetAddress(sender));

        message.addRecipients(Message.RecipientType.TO, createInternetAddress(recipients));
        message.addRecipients(Message.RecipientType.CC, createInternetAddress(coptTo));

        Multipart multipart = new MimeMultipart();
        if (content != null && !"".equals(content)) {
            BodyPart part = new MimeBodyPart();
            part.setContent(content, "text/html;chartset=UTF-8");
            multipart.addBodyPart(part);
        }

        if (filePath != null && !"".equals(filePath)) {
            BodyPart part = new MimeBodyPart();
            FileDataSource dataSource = new FileDataSource(filePath);
            part.setDataHandler(new DataHandler(dataSource));

            part.setFileName(MimeUtility.encodeText(fileName));
            multipart.addBodyPart(part);
        }

        message.setContent(multipart);

        Transport.send(message, message.getAllRecipients());
    }

    private static InternetAddress[] createInternetAddress(String[] addrs) throws UnsupportedEncodingException {
        if (addrs == null || addrs.length == 0) {
            return new InternetAddress[0];
        }

        InternetAddress[] addresses = new InternetAddress[addrs.length];
        for (int i = 0; i < addrs.length; i++) {
            addresses[i] = new InternetAddress(addrs[i], addrs[i]);
        }
        return addresses;
    }
}
