package com.commun;

import com.commun.AAHELPER.AAFunctions;
import com.commun.AAHELPER.MailSender;
import com.commun.GUI.LoginGUI;
import com.commun.MODELS.User;

import javax.swing.*;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        new LoginGUI();

        //System.out.println(User.existsByEmail("abdsahin3@gmail.com"));

    }
}
