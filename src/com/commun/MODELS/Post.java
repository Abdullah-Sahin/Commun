package com.commun.MODELS;

import com.commun.AAHELPER.DBConnection;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {

    private int postId;
    private int posterId;
    private String location;
    private String request;
    private LocalDateTime deadline;
    private int reward;
    private int claimerId;
    private boolean completed;
    private int rating;

    public Post() {
    }

    public Post(int posterId, String location, String request, LocalDateTime deadline, int reward) {
        this.posterId = posterId;
        this.location = location;
        this.request = request;
        this.deadline = deadline;
        this.reward = reward;
        this.claimerId = -1;
        this.completed = false;
        this.rating = 0;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getClaimerId() {
        return claimerId;
    }

    public void setClaimerId(int claimerId) {
        this.claimerId = claimerId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    //####################################################################################################3
    public static List<Post> getAllPosts(){
        List<Post> allPosts = new ArrayList<>();
        String query = "select * from posts order by deadline asc";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Post post = new Post();
                post.setPostId(resultSet.getInt("postid"));
                post.setPosterId(resultSet.getInt("posterid"));
                post.setRequest(resultSet.getString("request"));
                post.setLocation(resultSet.getString("location"));
                post.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
                post.setReward(resultSet.getInt("reward"));
                post.setClaimerId(resultSet.getInt("claimerid"));
                post.setCompleted(resultSet.getString("completed").equals("true"));
                allPosts.add(post);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPosts;
    }

    public static List<Post> getOpenPosts(){
        List<Post> openPosts = new ArrayList<>();
        String query = "select * from posts where claimerid = 0";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Post post = new Post();
                post.setPostId(resultSet.getInt("postid"));
                post.setPosterId(resultSet.getInt("posterid"));
                post.setRequest(resultSet.getString("request"));
                post.setLocation(resultSet.getString("location"));
                post.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
                post.setReward(resultSet.getInt("reward"));
                post.setClaimerId(resultSet.getInt("claimerid"));
                post.setCompleted(resultSet.getString("completed").equals("true"));
                post.setRating(resultSet.getInt("rating"));
                openPosts.add(post);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return openPosts;
    }

    public static Post getByPostId(int postId){
        String sql = "select * from posts where postid = ?";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Post post = new Post();
            post.setPostId(resultSet.getInt("postid"));
            post.setPosterId(resultSet.getInt("posterid"));
            post.setLocation(resultSet.getString("location"));
            post.setRequest(resultSet.getString("request"));
            post.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
            post.setReward(resultSet.getInt("reward"));
            post.setClaimerId(resultSet.getInt("claimerid"));
            post.setCompleted(Boolean.getBoolean(resultSet.getString("completed")));
            post.setRating(resultSet.getInt("rating"));
            preparedStatement.close();
            return post;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean existsByPostId(int postId){
        return getByPostId(postId) != null;
    }

    public static List<Post> getOpenPostsByPosterId(int userid){
        List<Post> openPosts = new ArrayList<>();
        String sql = "select * from posts where (posterid = ? and completed = ?)";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
            preparedStatement.setInt(1, userid);
            preparedStatement.setString(2, "false");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Post post = new Post();
                post.setPostId(resultSet.getInt("postid"));
                post.setPosterId(resultSet.getInt("posterid"));
                post.setLocation(resultSet.getString("location"));
                post.setRequest(resultSet.getString("request"));
                post.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
                post.setReward(resultSet.getInt("reward"));
                post.setClaimerId(resultSet.getInt("claimerid"));
                post.setCompleted(Boolean.getBoolean(resultSet.getString("completed")));
                post.setRating(resultSet.getInt("rating"));
                openPosts.add(post);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Kullanıcı açık ilanlarında SQL hatası");
            e.printStackTrace();
        }
        return openPosts;
    }

    public static List<Post> getByPosterId(int posterId){
        List<Post> posts = new ArrayList<>();
        String sql = "select * from posts where (posterid = ?)";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
            preparedStatement.setInt(1, posterId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Post post = new Post();
                post.setPostId(resultSet.getInt("postid"));
                post.setPosterId(resultSet.getInt("posterid"));
                post.setLocation(resultSet.getString("location"));
                post.setRequest(resultSet.getString("request"));
                post.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
                post.setReward(resultSet.getInt("reward"));
                post.setClaimerId(resultSet.getInt("claimerid"));
                post.setCompleted(Boolean.getBoolean(resultSet.getString("completed")));
                post.setRating(resultSet.getInt("rating"));
                posts.add(post);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Kullanıcı açık ilanlarında SQL hatası");
            e.printStackTrace();
        }
        return posts;
    }

    public static List<Post> getByClaimerId(int claimerId){
        List<Post> posts = new ArrayList<>();
        String sql = "select * from posts where (claimerid= ? and completed = ?)";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
            preparedStatement.setInt(1, claimerId);
            preparedStatement.setString(2, "false");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Post post = new Post();
                post.setPostId(resultSet.getInt("postid"));
                post.setPosterId(resultSet.getInt("posterid"));
                post.setLocation(resultSet.getString("location"));
                post.setRequest(resultSet.getString("request"));
                post.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
                post.setReward(resultSet.getInt("reward"));
                post.setClaimerId(resultSet.getInt("claimerid"));
                post.setCompleted(Boolean.getBoolean(resultSet.getString("completed")));
                post.setRating(resultSet.getInt("rating"));
                posts.add(post);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Kullanıcı açık ilanlarında SQL hatası");
            e.printStackTrace();
        }
        return posts;
    }

    public static List<Post> getOpenPostsByLocation(String location){
        List<Post> posts = new ArrayList<>();
        String sql = "select * from posts where (location = ? and completed = ?)";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
            preparedStatement.setString(1, location);
            preparedStatement.setString(2, "false");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Post post = new Post();
                post.setPostId(resultSet.getInt("postid"));
                post.setPosterId(resultSet.getInt("posterid"));
                post.setLocation(resultSet.getString("location"));
                post.setRequest(resultSet.getString("request"));
                post.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
                post.setReward(resultSet.getInt("reward"));
                post.setClaimerId(resultSet.getInt("claimerid"));
                post.setCompleted(Boolean.getBoolean(resultSet.getString("completed")));
                post.setRating(resultSet.getInt("rating"));
                posts.add(post);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Kullanıcı açık ilanlarında SQL hatası");
            e.printStackTrace();
        }
        return posts;
    }

    public static List<Post> getOpenFilteredPosts(String location, int minPrice, int maxPrice){
        List<Post> filteredposts = new ArrayList<>();
        if(location.equalsIgnoreCase("hepsi") && minPrice == 0 && maxPrice == 0){
            return getOpenPosts();
        }
        else if(minPrice == 0 && maxPrice == 0){
            return getOpenPostsByLocation(location);
        }
        else if(location.equalsIgnoreCase("hepsi")){
            String sql = "select * posts where (reward >= ? and reward <= ?)";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
                preparedStatement.setInt(1, minPrice);
                preparedStatement.setInt(2, maxPrice);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    Post post = new Post();
                    post.setPostId(resultSet.getInt("postid"));
                    post.setPosterId(resultSet.getInt("posterid"));
                    post.setLocation(resultSet.getString("location"));
                    post.setRequest(resultSet.getString("request"));
                    post.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
                    post.setReward(resultSet.getInt("reward"));
                    post.setClaimerId(resultSet.getInt("claimerid"));
                    post.setCompleted(Boolean.getBoolean(resultSet.getString("completed")));
                    post.setRating(resultSet.getInt("rating"));
                    filteredposts.add(post);
                }
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            String sql = "select * from posts where location = ? and reward >= ? and reward <= ?";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(sql);
                preparedStatement.setString(1, location);
                preparedStatement.setInt(2, minPrice);
                preparedStatement.setInt(3, maxPrice);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    Post post = new Post();
                    post.setPostId(resultSet.getInt("postid"));
                    post.setPosterId(resultSet.getInt("posterid"));
                    post.setLocation(resultSet.getString("location"));
                    post.setRequest(resultSet.getString("request"));
                    post.setDeadline(resultSet.getTimestamp("deadline").toLocalDateTime());
                    post.setReward(resultSet.getInt("reward"));
                    post.setClaimerId(resultSet.getInt("claimerid"));
                    post.setCompleted(Boolean.getBoolean(resultSet.getString("completed")));
                    filteredposts.add(post);
                }
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return filteredposts;
    }

    public void updateDeadline(LocalDateTime newDeadline){
        if(!existsByPostId(getPostId())){
            JOptionPane.showMessageDialog(null, "Bu id'ye sahip bir post bulunmamaktadır");
        }
        else{
            String query = "update posts set deadline = ? where postid = ?";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setTimestamp(1, Timestamp.valueOf(newDeadline));
                preparedStatement.setInt(2, getPostId());
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void  updateReward(int newReward){
        if(!existsByPostId(getPostId())){
            JOptionPane.showMessageDialog(null, "Bu id'ye sahip bir post bulunmamaktadır");
        }
        else{
            String query = "update posts set reward = ? where postid = ?";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setInt(1, newReward);
                preparedStatement.setInt(2, getPostId());
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateRequest(String newRequest){
        if(!existsByPostId(getPostId())){
            JOptionPane.showMessageDialog(null, "Bu id'ye sahip bir post bulunmamaktadır");
        }
        else{
            String query = "update posts set request = ? where postid = ?";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setString(1, newRequest);
                preparedStatement.setInt(2, getPostId());
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateStatus(){
        if(!existsByPostId(postId)){
            JOptionPane.showMessageDialog(null,"Bu ID'ye sahip bir ilan bulunmamaktadır");
        }
        else{
            String query = "update posts set completed = true where postid = ?";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setInt(1, postId);
                preparedStatement.execute();
                preparedStatement.close();
                JOptionPane.showMessageDialog(null, "İlan statüsü güncellendi");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateRating() {
        if (!existsByPostId(postId)) {
            JOptionPane.showMessageDialog(null, "Bu id'ye sahip bir ilan bulunmamaktadır");
        } else {
            String query = "update posts set rating = ? where postid = ?";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setString(1, String.valueOf(rating));
                preparedStatement.setInt(2, postId);
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(){
        if(!existsByPostId(postId)){
            JOptionPane.showMessageDialog(null, "Bu id'ye sahip bir post bulunmamaktadır.");
        }
        else{
            String query = "delete from posts where postid = ?";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setInt(1, postId);
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deletePastDuePosts(){
        String query = "delete from posts where deadline < ?";
        try {
            PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(Timestamp.valueOf(LocalDateTime.now())));
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
