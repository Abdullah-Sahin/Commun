package com.commun.GUI;

import com.commun.AAHELPER.AAFunctions;
import com.commun.MODELS.Post;

import javax.swing.*;
import java.time.LocalDateTime;
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
    private JPanel panelBottom;
    private JPanel panelDate;
    private JComboBox comboBoxYear;
    private JComboBox comboBoxMonth;
    private JComboBox comboBoxDay;
    private JComboBox comboBoxHour;
    private JLabel labelYear;
    private JLabel labelMonth;
    private JLabel labelDay;
    private JLabel labelHour;

    public UpdatePostGUI(UserGUI userGUI, Post post) {
        this.post = post;
        this.userGUI = userGUI;
        add(panelMain);
        setDeadlineSelectors();
        setRewardIndex();
        textAreaRequest.setText(post.getRequest());

        setSize(400,300);
        setResizable(false);
        AAFunctions.setScreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        AAFunctions.setIcon(this);
        setVisible(true);


        buttonUpdate.addActionListener(e -> {

            String request = textAreaRequest.getText();
            int reward = Integer.parseInt(Objects.requireNonNull(comboBoxReward.getSelectedItem()).toString());
            int year = Integer.parseInt(Objects.requireNonNull(comboBoxYear.getSelectedItem()).toString());
            int month = Integer.parseInt(Objects.requireNonNull(comboBoxMonth.getSelectedItem()).toString());
            int day = Integer.parseInt(Objects.requireNonNull(comboBoxDay.getSelectedItem()).toString());
            int hour = Integer.parseInt(Objects.requireNonNull(comboBoxHour.getSelectedItem()).toString());
            LocalDateTime deadline = LocalDateTime.of(year,month,day,hour,0);
            if(AAFunctions.timeCheck(deadline) && AAFunctions.balanceCheck(userGUI.getUser(), reward, post.getPostId())){
                post.updateDeadlineOnDB(deadline);
                post.updateRequestOnDB(textAreaRequest.getText());
                post.updateRewardOnDB(Integer.parseInt(Objects.requireNonNull(comboBoxReward.getSelectedItem()).toString()));
                userGUI.setModelMyPosts();
                userGUI.setModelOpenPosts();
                dispose();
                JOptionPane.showMessageDialog(null,"İlan başarıyla güncellendi");
            }
        });
        buttonWithdraw.addActionListener(e -> dispose());
    }

    public void setRewardIndex(){
        for(int indexOfReward = 0; indexOfReward < comboBoxReward.getItemCount(); indexOfReward++){
            if(comboBoxReward.getItemAt(indexOfReward).toString().equals(String.valueOf(post.getReward()))){
                comboBoxReward.setSelectedIndex(indexOfReward);
                break;
            }
        }
    }

    public void setDeadlineSelectors(){
        for(int indexOfYear = 0; indexOfYear < comboBoxYear.getItemCount(); indexOfYear++){
            if(comboBoxYear.getItemAt(indexOfYear).toString().equals(String.valueOf(post.getDeadline().getYear()))){
                comboBoxYear.setSelectedIndex(indexOfYear);
                break;
            }
        }
        for(int indexOfMonth = 0; indexOfMonth < comboBoxMonth.getItemCount(); indexOfMonth++){
            if(comboBoxMonth.getItemAt(indexOfMonth).toString().equals(String.valueOf(post.getDeadline().getYear()))){
                comboBoxMonth.setSelectedIndex(indexOfMonth);
                break;
            }
        }
        for(int indexOfDay = 0; indexOfDay < comboBoxDay.getItemCount(); indexOfDay++){
            if(comboBoxDay.getItemAt(indexOfDay).toString().equals(String.valueOf(post.getDeadline().getYear()))){
                comboBoxDay.setSelectedIndex(indexOfDay);
                break;
            }
        }
        for(int indexOfHour = 0; indexOfHour < comboBoxHour.getItemCount(); indexOfHour++){
            if(comboBoxHour.getItemAt(indexOfHour).toString().equals(String.valueOf(post.getDeadline().getYear()))){
                comboBoxHour.setSelectedIndex(indexOfHour);
                break;
            }
        }
    }
}
