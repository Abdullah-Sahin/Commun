package com.commun.GUI;

import com.commun.AAHELPER.AAFunctions;
import com.commun.MODELS.User;

import javax.swing.*;

public class UserSettingsGUI extends JFrame{

    private UserGUI userGUI;
    private User user;
    private JPanel panelMain;
    private JPanel panelTop;
    private JPanel panelBottom;
    private JButton buttonLogout;
    private JLabel labelUserName;
    private JTextField textFieldUserName;
    private JLabel labelUserPassword;
    private JTextField textFieldUserPassword;
    private JButton buttonSubmitChanges;
    private JButton buttonDeleteUser;

    public UserSettingsGUI(UserGUI userGUI, User user){
        this.user = user;
        this.userGUI = userGUI;
        add(panelMain);

        textFieldUserName.setText(user.getUsername());
        setTextFieldUserPassword();


        setSize(600,500);
        setResizable(false);
        AAFunctions.setScreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        AAFunctions.setIcon(this);
        setVisible(true);



        // Listeners start from here

        buttonLogout.addActionListener(e -> dispose());
        buttonSubmitChanges.addActionListener(e -> {
            if(textFieldUserPassword.getText().trim().length() < 8){
                JOptionPane.showMessageDialog(null, "Parola en az 8 karakterden oluşmalıdır");
            }
            else{
                user.setPassword(textFieldUserPassword.getText().trim());
                setTextFieldUserPassword();
                if(user.updatePasswordOnDB()){
                    dispose();
                }
            }
        });
        buttonDeleteUser.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(null,"Hesabınızı silmek istediğinize emin misiniz?") == 0){
                if(user.delete()){
                    new LoginGUI();
                    dispose();
                    userGUI.dispose();
                }
            }
        });
    }

    public void setTextFieldUserPassword(){
        textFieldUserPassword.setText(user.getPassword());
    }
}
