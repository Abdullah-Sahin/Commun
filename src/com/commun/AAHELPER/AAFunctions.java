package com.commun.AAHELPER;

import com.commun.MODELS.Post;
import com.commun.MODELS.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class AAFunctions {

    public static void setScreen(JFrame frame){
        int x= (Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth()) / 2;
        int y= (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2;
        frame.setLocation(x,y);
    }

    public static void setIcon(JFrame frame){
        try{
            frame.setIconImage(new ImageIcon("C:\\Users\\abdsa\\Desktop\\Projects\\Commun\\src\\com\\commun\\commun logo.png").getImage());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean timeCheck(LocalDateTime localDateTime){
        return localDateTime.isAfter(LocalDateTime.now().plusMinutes(90));
    }

    public static boolean balanceCheck(User user, int Reward){
        int previousAmount = 0;
        for(Post post: Post.getOpenPostsByPosterId(user.getUserid())){
            previousAmount += post.getReward();
        }
        return user.getCoins() - previousAmount - Reward >= 0;
    }

    public static boolean balanceCheck(User user, int newReward, int postId){
        int previousAmount = - Objects.requireNonNull(Post.getByPostId(postId)).getReward();
        for(Post post: Post.getOpenPostsByPosterId(user.getUserid())){
            previousAmount += post.getReward();
        }
        return user.getCoins() - previousAmount - newReward >= 0;
    }

}
