package com.commun.AAHELPER;

import javax.swing.*;
import java.awt.*;

public class AAFunctions {

    public static void setScreen(JFrame frame){

        int x= Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth() / 2;
        int y= Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight() / 2;
        frame.setLocation(x,y);
    }
}
