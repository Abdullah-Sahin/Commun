package com.commun.GUI;

import com.commun.AAHELPER.AAFunctions;
import com.commun.MODELS.Post;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PostGUI extends JFrame{

    private Post post;
    private JPanel panelMain;
    private JPanel panelTop;
    private JLabel labelReward;
    private JTextField textFieldReward;
    private JLabel labelDeadline;
    private JTextField textFieldDeadline;
    private JPanel panelBottom;
    private JTextArea textAreaRequest;
    private JButton buttonExit;


    public PostGUI(Post post) {
        this.post = post;
        add(panelMain);
        textFieldReward.setText(String.valueOf(post.getReward()));
        textFieldDeadline.setText(post.getDeadline().toString());
        textAreaRequest.setText(post.getRequest());

        setSize(500,300);
        setResizable(false);
        AAFunctions.setScreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        buttonExit.addActionListener(e -> dispose());
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
