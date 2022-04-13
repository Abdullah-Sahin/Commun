package com.commun.GUI;

import com.commun.AAHELPER.AAFunctions;
import com.commun.MODELS.User;

import javax.swing.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class CreatePostGUI extends JFrame{

    private UserGUI userGUI;
    private JPanel panelMain;
    private JLabel labelLocation;
    private JComboBox comboBoxLocation;
    private JButton buttonCreatePost;
    private JButton buttonLogout;
    private JComboBox comboBoxYear;
    private JComboBox comboBoxMonth;
    private JComboBox comboBoxDay;
    private JComboBox comboBoxHour;
    private JTextArea textAreaRequest;
    private JComboBox comboBoxReward;


    public CreatePostGUI(UserGUI userGUI,User user){
        add(panelMain);
        this.userGUI = userGUI;

        setDeadlineSelectors();


        setSize(600,600);
        setResizable(false);
        AAFunctions.setScreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        buttonCreatePost.addActionListener(e -> {
            if(textAreaRequest.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(null, "Tüm alanları doldurduğunuza emin olun");
            }
            else{
                String location = Objects.requireNonNull(comboBoxLocation.getSelectedItem()).toString();
                int year = Integer.parseInt(Objects.requireNonNull(comboBoxYear.getSelectedItem()).toString());
                int month = Integer.parseInt(Objects.requireNonNull(comboBoxMonth.getSelectedItem()).toString());
                int day = Integer.parseInt(Objects.requireNonNull(comboBoxDay.getSelectedItem()).toString());
                int hour = Integer.parseInt(Objects.requireNonNull(comboBoxHour.getSelectedItem()).toString());
                String request = textAreaRequest.getText();
                int reward = Integer.parseInt(Objects.requireNonNull(comboBoxReward.getSelectedItem()).toString());
                LocalDateTime deadline = LocalDateTime.of(year,month,day,hour,0);
                try{
                    if(user.createPost(location, request, Timestamp.valueOf(deadline), reward)){
                        userGUI.setModelOpenPosts();
                        userGUI.setModelMyPosts();
                        dispose();
                    }
                }catch (Exception exception){
                    System.out.println(exception.getMessage());
                }
            }
        });
        buttonLogout.addActionListener(e -> dispose());
    }

    public void setDeadlineSelectors(){
        LocalDateTime now = LocalDateTime.now();
        if(Integer.parseInt(Objects.requireNonNull(comboBoxYear.getItemAt(0)).toString()) < now.getYear()){
            //Set index of year
            for(int indexOfYear = 0; indexOfYear < comboBoxYear.getItemCount(); indexOfYear++){
                if(Integer.parseInt(comboBoxYear.getSelectedItem().toString()) < now.getYear()){
                    comboBoxYear.setSelectedIndex(indexOfYear + 1);
                }
            }
        }
        else{
            if(Integer.parseInt(Objects.requireNonNull(comboBoxMonth.getItemAt(0)).toString()) < now.getMonthValue()){
                //Set index of month
                for(int indexOfMonth = 0; indexOfMonth < comboBoxMonth.getItemCount(); indexOfMonth++){
                    if(Integer.parseInt(comboBoxMonth.getSelectedItem().toString()) < now.getMonthValue()){
                        comboBoxMonth.setSelectedIndex(indexOfMonth + 1);
                    }
                }
                if(Integer.parseInt(Objects.requireNonNull(comboBoxDay.getItemAt(0)).toString()) < now.getDayOfMonth()){
                    //Set index of day
                    for(int indexOfDay = 0; indexOfDay < comboBoxDay.getItemCount(); indexOfDay++){
                        if(Integer.parseInt(comboBoxDay.getItemAt(indexOfDay).toString()) < now.getDayOfMonth()){
                            comboBoxDay.setSelectedIndex(indexOfDay + 1);
                        }
                    }
                    if(Integer.parseInt(comboBoxHour.getItemAt(0).toString()) < now.getHour()){
                        //Set indexx of Hour
                        for(int indexOfHour = 0; indexOfHour < comboBoxHour.getItemCount(); indexOfHour++){
                            if(Integer.parseInt(comboBoxHour.getItemAt(indexOfHour).toString()) < now.getHour()){
                                comboBoxHour.setSelectedIndex(indexOfHour + 1);
                            }
                        }
                    }
                }
            }
        }
    }
}
