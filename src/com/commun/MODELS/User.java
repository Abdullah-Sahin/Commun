package com.commun.MODELS;

import com.commun.AAHELPER.AAFunctions;
import com.commun.AAHELPER.DBConnection;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

public class User {

    private int userid;
    private String username;
    private String email;
    private int coins;
    private String password;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        else this.coins = coins;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ######################################################################################################################

    public static User getById(int userId){
        String sql = "select * from users where userid = ?";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = new User();
            resultSet.next();
            user.setUserid(resultSet.getInt("userid"));
            user.setUsername(resultSet.getString("username"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setCoins(resultSet.getInt("coins"));
            preparedStatement.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean existsById(int userId){
        return getById(userId) != null;
    }

    public static User getByUserName(String userName){
        String sql = "select * from users where username = ?";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                User user = new User();
                user.setUserid(resultSet.getInt("userid"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setCoins(resultSet.getInt("coins"));
                preparedStatement.close();
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean existsByUsername(String userName){
        return getByUserName(userName) != null;
    }

    public static User getByEmail(String userEmail){
        String sql = "select * from users where email = ?";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
            preparedStatement.setString(1, userEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                User user = new User();
                user.setUserid(resultSet.getInt("userid"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setCoins(resultSet.getInt("coins"));
                preparedStatement.close();
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean existsByEmail(String email){
        return getByEmail(email) != null;
    }

    public static int addUser(String username, String email, String password){
        int flag = 0;
        if(existsByUsername(username)){
            JOptionPane.showMessageDialog(null, "Bu kullanıcı adı ile oluşturulmuş bir hesap bulunmaktadır");
            flag = 1;
        }
        else if(username.trim().length() < 5 ){
            JOptionPane.showMessageDialog(null, "Kullanıcı adı en az 5 karakterden oluşmalıdır");
            flag = 1;
        }
        else if(existsByEmail(email)){
            JOptionPane.showMessageDialog(null,"Bu email'e sahip bir kullanıcı bulunmaktadır");
            flag = 2;
        }
        else if(!email.contains("@") || !email.contains(".com") || email.length() < 9){
            JOptionPane.showMessageDialog(null, "Lütfen geçerli bir mail giriniz");
            flag = 2;
        }
        else if(password.trim().length() < 8){
            JOptionPane.showMessageDialog(null, "Parola en az 8 karakterden oluşmalıdır");
            flag = 3;
        }
        else{
            String query = "insert into users (userid, username, password, coins) Values (null, ?, ?, 200)";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.execute();
                preparedStatement.close();
                JOptionPane.showMessageDialog(null, "Kayıt Başarılı");
                flag = 4;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public boolean delete(){
        String sql1 = "delete from posts where posterid = ?";
        String sql2 = "delete from users where userid = ?";
        try {
            PreparedStatement ps1 = DBConnection.createInstance().prepareStatement(sql1);
            ps1.setInt(1, this.getUserid());
            if(ps1.executeUpdate() != -1){
                    PreparedStatement ps2 = DBConnection.createInstance().prepareStatement(sql2);
                    ps2.setInt(1, this.getUserid());
                    if(ps2.executeUpdate() != -1){
                        JOptionPane.showMessageDialog(null,"Kullanıcı başarıyla silindi");
                        ps2.close();
                        return true;
                    }
                else {
                    JOptionPane.showMessageDialog(null,"Kullanıcı silinemedi");
                }
            }
            else{
                JOptionPane.showMessageDialog(null,"Kullanıcı ilanları silinemedi");
            }
            ps1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateCoins(){
        String query = "Update users set coins = ? where userid = ?";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
            preparedStatement.setInt(1, getCoins());
            preparedStatement.setInt(2, getUserid());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Coin güncellemesinde hata");
            e.printStackTrace();
        }
    }

    public boolean checkPassword(String password){
        return this.getPassword().equals(password);
    }

    //KONTROL ET
    public boolean createPost(String location, String request, LocalDateTime deadline, int reward){
        String query = "insert into posts (postid, posterid, location, request, deadline, reward, claimerid, completed, rating) Values (null,?, ?, ?,?, ?, ?, ?, ?)";
        if(AAFunctions.balanceCheck(this, reward)){
            JOptionPane.showMessageDialog(null, "Yetersiz Bakiye");
        }
        else if(!AAFunctions.timeCheck(deadline)){
            JOptionPane.showMessageDialog(null, "İlan en az 90 dakika önceden oluşturulmalıdır");
        }
        else{
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setInt(1, userid);
                preparedStatement.setString(2, location);
                preparedStatement.setString(3, request);
                preparedStatement.setTimestamp(4, Timestamp.valueOf(deadline));
                preparedStatement.setInt(5, reward);
                preparedStatement.setString(6, "0");
                preparedStatement.setString(7, "false");
                preparedStatement.setString(8, "0");
                preparedStatement.execute();
                preparedStatement.close();
                JOptionPane.showMessageDialog(null,"İlan başarıyla yayınlandı");
                return true;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Post ilanında hata");
                e.printStackTrace();
            }
        }
        return false;
    }

    public void claimPostByPostId(int postId){
        if(Post.getByPostId(postId) == null){
            JOptionPane.showMessageDialog(null, "Bu id'ye sahip bir ilan bulunmamaktadır");
        }
        else{
            Post post = Post.getByPostId(postId);
            if(post.getPosterId()==getUserid()){
                JOptionPane.showMessageDialog(null, "Kendi ilanınızı alamazsınız");
            }
            else if(post.getClaimerId() > 0){
                JOptionPane.showMessageDialog(null, "Bu görev başkası tarafından alınmış");
            }
            else{
                String query = "Update posts set claimerid = ? where postid = ?";
                try {
                    PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                    preparedStatement.setInt(1, this.getUserid());
                    preparedStatement.setInt(2, postId);
                    preparedStatement.execute();
                    preparedStatement.close();
                    JOptionPane.showMessageDialog(null,"Görev başarıyla alındı");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null,"Görev alımı sırasında bir hata oluştu");
                    e.printStackTrace();
                }
            }
        }
    }

    public void withdrawClaimByPostId(int postId){
        if(Post.getByPostId(postId) == null){
            JOptionPane.showMessageDialog(null,"Bu id ile eşleşen bir görev bulunmamaktadır");
        }
        else if(Post.getByPostId(postId).getClaimerId() != getUserid()){
            JOptionPane.showMessageDialog(null, "Sizde olmayan bir görevden vazgeçemezsiniz");
        }
        else{
            String query = "update posts set claimerid = 0 where postid = ?";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setInt(1, postId);
                if(LocalDateTime.now().plusHours(2).isAfter(Post.getByPostId(postId).getDeadline())){
                    JOptionPane.showMessageDialog(null, "Bitişe iki saatten az varsa görevi bırakamazsınız");
                }
                else{
                    if(preparedStatement.executeUpdate() != -1){
                        JOptionPane.showMessageDialog(null, "Görevden başarıyla vazgeçildi");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Görevden vazgeçme sırasında bilinmeyen hata");
                    }
                }
                preparedStatement.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Görevden vazgeçme sırasında SQL hatası");
                e.printStackTrace();
            }
        }
    }

    public boolean updatePasswordOnDB(){
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
            JOptionPane.showMessageDialog(null, "Şifre değiştirilirken SQL hatası");
            e.printStackTrace();
        }
        return false;
    }
}
