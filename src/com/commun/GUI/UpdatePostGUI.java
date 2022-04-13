package com.commun.GUI;

import com.commun.AAHELPER.AAFunctions;
import com.commun.MODELS.Post;

import javax.swing.*;
import java.util.Objects;

public class UpdatePostGUI extends JFrame{

    private UserGUI userGUI;
    private final Post post;
    private JPanel panelMain;
    private JPanel panelTop;
    private JLabel labelUpdateReward;
    private JComboBox comboBoxReward;
    private JButton buttonUpdate;
    private JTextArea textAreaRequest;
    private JButton buttonWithdraw;

    public UpdatePostGUI(UserGUI userGUI, Post post) {
        this.post = post;
        this.userGUI = userGUI;
        add(panelMain);
        setRewardIndex();


        textAreaRequest.setText(post.getRequest());
        setSize(400,300);
        setResizable(false);
        AAFunctions.setScreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        buttonUpdate.addActionListener(e -> {
            post.updateRequest(textAreaRequest.getText());
            post.updateReward(Integer.parseInt(Objects.requireNonNull(comboBoxReward.getSelectedItem()).toString()));
            userGUI.setModelMyPosts();
            userGUI.setModelOpenPosts();
            setRewardIndex();
            dispose();
            JOptionPane.showMessageDialog(null,"İlan başarıyla güncellendi");
        });
        buttonWithdraw.addActionListener(e -> dispose());
    }

    public void setRewardIndex(){
        for(int indexOfReward = 0; indexOfReward < comboBoxReward.getItemCount(); indexOfReward++){
            if(comboBoxReward.getItemAt(indexOfReward).toString().equals(String.valueOf(post.getReward()))){
                comboBoxReward.setSelectedIndex(indexOfReward);
            }
        }
    }
}
