package com.commun.MODELS;

import com.commun.AAHELPER.DBConnection;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {

    private int userid;
    private String username;
    private int coins;
    private String password;

    public User() {
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        if(coins > 20000){
            this.coins = 20000;
        }
        else if(coins < 0){
            JOptionPane.showMessageDialog(null, "Coin 0'dan az olamaz");
        }
        else this.coins = Math.max(coins, 0);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        String query = "select * from users";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                User user = new User();
                user.setUserid(resultSet.getInt("userid"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setCoins(resultSet.getInt("coins"));
                users.add(user);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static boolean existsById(int id){
        for(User user: getAllUsers()){
            if(user.getUserid() == id){
                return true;
            }
        }
        return false;
    }

    public static User getById(int id){
        User user = null;
        if(!existsById(id)){
            JOptionPane.showMessageDialog(null,"Bu id'ye sahip bir kullanıcı bulunmamaktadır.");
        }
        else{
            for(User anyUser: getAllUsers()){
                if(anyUser.getUserid() == id){
                    user = anyUser;
                    break;
                }
            }
        }
        return user;
    }

    public static boolean existsByUsername(String userName){
        for(User user: getAllUsers())
            if(user.getUsername().equals(userName)){
                return true;
            }
        return false;
    }

    public static User getByUserName(String userName){
        User user = null;
        if(!existsByUsername(userName)){
            JOptionPane.showMessageDialog(null, "Bu kullanıcı adı sistemde kayıtlı değildir");
        }
        else{
            for(User anyUser:getAllUsers()){
                if(anyUser.getUsername().equals(userName)){
                    user = anyUser;
                    break;
                }
            }
        }
        return user;
    }

    public static boolean addUser(String username, String password){
        if(existsByUsername(username)){
            JOptionPane.showMessageDialog(null, "Bu kullanıcı adı ile oluşturulmuş bir hesap bulunmaktadır");
        }
        else if(username.trim().length() < 5 ){
            JOptionPane.showMessageDialog(null, "Kullanıcı adı en az 5 karakterden oluşmalıdır");
        }
        else if(password.trim().length() < 8){
            JOptionPane.showMessageDialog(null, "Parola en az 8 karakterden oluşmalıdır");
        }
        else{
            String query = "insert into users (userid, username, password, coins, posts) Values (null, ?, ?, 200, '')";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.execute();
                preparedStatement.close();
                JOptionPane.showMessageDialog(null, "Kayıt Başarılı");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean delete(){
        String query = "delete from users where userid = ?";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
            preparedStatement.setInt(1, getUserid());
            List<Post> posts = new ArrayList<>();
            for(Post post: Post.getAllPosts()){
                if(post.getPosterId() == this.getUserid()){
                    posts.add(post);
                }
            }
            if(preparedStatement.execute()){
                for(Post post: posts){
                    post.delete();
                }
            }
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCoins(){
        String query = "Update users set coins = ? where userid = ?";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
            preparedStatement.setInt(1, coins);
            preparedStatement.setInt(2, getUserid());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkPassword(String password){
        return getPassword().equals(password);
    }

    //KONTROL ET
    public boolean createPost(String location, String request, Timestamp deadline, int reward){
        String query = "insert into posts (postid, posterid, location, request, deadline, reward, claimerid, completed, rating) Values (null,?, ?, ?,?, ?, ?, ?, ?)";
        if(deadline.toLocalDateTime().isBefore(ChronoLocalDateTime.from(LocalDateTime.now().plusMinutes(60)))){
            JOptionPane.showMessageDialog(null, "İlan en az bir saat önceden oluşturulmalıdır");
        }
        else if(getCoins() < reward){
            JOptionPane.showMessageDialog(null, "Yetersiz Bakiye");
        }
        else{
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setInt(1, userid);
                preparedStatement.setString(2, location);
                preparedStatement.setString(3, request);
                preparedStatement.setTimestamp(4, deadline);
                preparedStatement.setInt(5, reward);
                preparedStatement.setString(6, "0");
                preparedStatement.setString(7, "false");
                preparedStatement.setString(8, "0");
                preparedStatement.execute();
                preparedStatement.close();
                JOptionPane.showMessageDialog(null,"İlan başarıyla yayınlandı");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void claimPostByPostId(int postId){
        if(!Post.existsById(postId)){
            JOptionPane.showMessageDialog(null, "Bu id'ye sahip bir ilan bulunmamaktadır");
        }
        else{
            Post post = Post.getByPostId(postId);
            if(post.getPosterId()==userid){
                JOptionPane.showMessageDialog(null, "Kendi ilanınızı alamazsınız");
            }
            else{
                String query = "Update posts set claimerid = ? where postid = ?";
                try {
                    PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                    preparedStatement.setInt(1, userid);
                    preparedStatement.setInt(2, postId);
                    preparedStatement.execute();
                    preparedStatement.close();
                    JOptionPane.showMessageDialog(null,"Görev başarıyla alındı");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void withdrawClaimByPostId(int postId){
        if(!Post.existsById(postId)){
            JOptionPane.showMessageDialog(null,"Bu id ile eşleşen bir görev bulunmamaktadır");
        }
        else{
            String query = "update posts set claimerid = 0 where postid = ?";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setInt(1, this.getUserid());
                if(LocalDateTime.now().plusHours(2).isAfter(Post.getByPostId(postId).getDeadline())){
                    JOptionPane.showMessageDialog(null, "Bitişe iki saatten az varsa görevi bırakamazsınız");
                }
                else{
                    if(preparedStatement.executeUpdate() != -1){
                        withdrawClaimByPostId(postId);
                    }
                }
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updatePassword(){
        String query = "update users set password = ? where userid = ?";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
            preparedStatement.setString(1, getPassword());
            preparedStatement.setInt(2, getUserid());
            if(preparedStatement.executeUpdate() != -1){
                JOptionPane.showMessageDialog(null, "Şifre başarıyla güncellendi");
            }
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Şifre değiştirilirken bir hata oluştu");
            e.printStackTrace();
        }
        return false;
    }
}
