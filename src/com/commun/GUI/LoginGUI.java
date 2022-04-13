package com.commun.GUI;

import com.commun.AAHELPER.AAFunctions;
import com.commun.MODELS.User;

import javax.swing.*;

public class LoginGUI extends JFrame{
    private JPanel panelMain;
    private JLabel labelWelcome;
    private JLabel labelNewUserName;
    private JTextField textFieldNewUserName;
    private JLabel labelNewUserPassword;
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

    public LoginGUI() {
        add(panelMain);

        setSize(600, 500);
        setResizable(false);
        AAFunctions.setScreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
            String password = passwordFieldNewUserPassword.getText();
            if(User.addUser(userName, password)){
                textFieldNewUserName.setText(null);
                passwordFieldNewUserPassword.setText(null);
                textFieldUserName.setText(userName);
                passwordFieldUserPassword.setText(password);
            }
        });
    }
}
