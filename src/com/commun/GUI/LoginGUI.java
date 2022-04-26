package com.commun.GUI;

import com.commun.AAHELPER.AAFunctions;
import com.commun.AAHELPER.MailSender;
import com.commun.MODELS.User;

import javax.swing.*;
import java.util.Objects;

public class LoginGUI extends JFrame{
    private JPanel panelMain;
    private JLabel labelWelcome;
    private JLabel labelNewUserName;
    private JTextField textFieldNewUserName;
    private JLabel labelNewUserEmail;
    private JPasswordField passwordFieldNewUserPassword;
    private JButton buttonSignUp;
    private JPanel panelTop;
    private JPanel panelBottom;
    private JLabel labelUserName;
    private JTextField textFieldUserName;
    private JLabel labelUserPassword;
    private JPasswordField passwordFieldUserPassword;
    private JLabel labelRegister;
    private JButton buttonLogin;
    private JPanel panelMid;
    private JTextField textFieldForgottenPasswordEmail;
    private JButton buttonRequestNewPassword;
    private JLabel labelForgotPassword;
    private JLabel labelAskEmail;
    private JLabel labelPasswordSending;
    private JTextField textFieldNewUserEmail;

    public LoginGUI() {
        add(panelMain);

        setSize(600, 500);
        setResizable(false);
        AAFunctions.setScreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        AAFunctions.setIcon(this);
        setVisible(true);


        buttonLogin.addActionListener(e -> {
            String userName = textFieldUserName.getText();
            String password = passwordFieldUserPassword.getText();
            if(User.existsByUsername(userName)){
                User user = User.getByUserName(userName);
                if(user.checkPassword(password)){
                    new UserGUI(user);
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(null,"Şifre Yanlış!");
                    passwordFieldUserPassword.setText(null);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Geçersiz kullanıcı adı!");
                textFieldUserName.setText(null);
                passwordFieldUserPassword.setText(null);
            }
        });


        buttonSignUp.addActionListener(e -> {
            String userName = textFieldNewUserName.getText();
            String email = textFieldNewUserEmail.getText();
            String password = User.setNewPassword();
            int success = User.addUser(userName, email, password);
            if(success == 0){
                JOptionPane.showMessageDialog(null,"Bilinmeyen bir hata oluştu");
            }
            else if(success == 1){
                textFieldNewUserName.setText(null);
            }
            else if(success == 2){
                textFieldNewUserEmail.setText(null);
            }
            else if(success == 3){
                passwordFieldNewUserPassword.setText(null);
            }
            else{
                if(MailSender.sendMail(Objects.requireNonNull(User.getByUserName(userName)), password)){
                    JOptionPane.showMessageDialog(null, "Şifreniz mail adresinize gönderildi");
                }
                else{
                    JOptionPane.showMessageDialog(null, "mail gönderilemedi");
                }
                textFieldUserName.setText(userName);
                passwordFieldUserPassword.setText(password);
                textFieldNewUserName.setText(null);
                textFieldNewUserEmail.setText(null);
            }
        });


        buttonRequestNewPassword.addActionListener(e -> {
            if(User.existsByEmail(textFieldForgottenPasswordEmail.getText())){
                if(MailSender.sendMail(Objects.requireNonNull(User.getByEmail(textFieldForgottenPasswordEmail.getText())))){
                    JOptionPane.showMessageDialog(null, "Yeni şifreniz mail adresinize gönderilmiştir");
                }
                else{
                    JOptionPane.showMessageDialog(null,"Bir hata oluştu\n" +
                            "Lütfen customer@commun.com adresine mail gönderiniz");
                }
            }
            else {
                JOptionPane.showMessageDialog(null,"Bu mail adresine sahip bir kullanıcı bulunmamaktadır");
            }

        });
    }
}
