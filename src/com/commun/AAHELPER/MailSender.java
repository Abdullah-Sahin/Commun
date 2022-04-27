package com.commun.AAHELPER;

import com.commun.MODELS.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class MailSender {

    private static final String communMail = "communforcommunity@gmail.com";
    private static final String communPassword = "communba4146";

    private static Properties setProperties(){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }

    private static Session setSession(Properties properties){
        return Session.getDefaultInstance(setProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailSender.communMail, MailSender.communPassword);
            }
        });
    }

    private static Message setMessage(Session session, String receipentMail, String newPassword){
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(communMail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receipentMail));
            message.setSubject("COMMUN PAROLA");
            message.setText("Merhaba "+ Objects.requireNonNull(User.getByEmail(receipentMail)).getUsername()+"\nParolanız:\t" + newPassword);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }


    public static boolean sendMail(User user){
        try {
            String newPassword = User.setNewPassword();
            String sql = "update users set password = ? where email = ?";
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, user.getEmail());
            if(preparedStatement.executeUpdate() != -1){
                Properties properties = setProperties();
                Session session = setSession(properties);
                Message message = setMessage(session, user.getEmail(), newPassword);
                Transport.send(message);
                return true;
            }
        } catch (SQLException | MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendMail(User user, String newPassword){
        try {
            String sql = "update users set password = ? where email = ?";
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, user.getEmail());
            if(preparedStatement.executeUpdate() != -1){
                Properties properties = setProperties();
                Session session = setSession(properties);
                Message message = setMessage(session, user.getEmail(), newPassword);
                Transport.send(message);
                return true;
            }
        } catch (SQLException | MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }

}
