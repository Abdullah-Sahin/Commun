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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPosts;
    }

    public static List<Post> getOpenPosts(){
        List<Post> openPosts = new ArrayList<>();
        for(Post post: getAllPosts()){
            if(post.getClaimerId() <= 0){
                openPosts.add(post);
            }
        }
        return openPosts;
    }

    public static boolean existsById(int postId){
        for(Post post:getAllPosts()){
            if(post.getPostId() == postId){
                return true;
            }
        }
        return false;
    }

    public static List<Post> getByPosterId(int posterId){
        List<Post> posts = new ArrayList<>();
        for(Post post:getAllPosts()){
            if(post.getPosterId()==posterId){
                posts.add(post);
            }
        }
        return posts;
    }

    public static List<Post> getByClaimerId(int claimerId){
        List<Post> posts = new ArrayList<>();
        for(Post post:getAllPosts()){
            if(post.getClaimerId()==claimerId){
                posts.add(post);
            }
        }
        return posts;
    }

    public static Post getByPostId(int postId){
        Post post = null;
        for(Post anyPost: getAllPosts()){
            if(anyPost.getPostId() == postId){
                post = anyPost;
            }
        }
        return post;
    }

    public static List<Post> getFilteredPosts(String location, int minPrice, int maxPrice){
        List<Post> filteredposts = new ArrayList<>();
        if(location.equalsIgnoreCase("hepsi") && minPrice == 0 && maxPrice == 0){
            return getOpenPosts();
        }
        else if(location.equalsIgnoreCase("hepsi")){
            for(Post post: getOpenPosts()){
                if(post.getReward() >= minPrice && post.getReward() <= maxPrice){
                    filteredposts.add(post);
                }
            }
        }
        else if(minPrice == 0 && maxPrice == 0){
            for(Post post: getOpenPosts()){
                if(post.getLocation().equalsIgnoreCase(location)){
                    filteredposts.add(post);
                }
            }
        }
        else{
            for(Post post:getOpenPosts()){
                if(post.getLocation().equals(location) && post.getReward() >= minPrice && post.getReward() <= maxPrice){
                    filteredposts.add(post);
                }
            }
        }
        return filteredposts;
    }

    public void delete(){
        if(!existsById(postId)){
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

    public void  updateReward(int newReward){
        if(!existsById(postId)){
            JOptionPane.showMessageDialog(null, "Bu id'ye sahip bir post bulunmamaktadır");
        }
        else{
            String query = "update posts set reward = ? where postid = ?";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setInt(1, newReward);
                preparedStatement.setInt(2, postId);
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateRequest(String newRequest){
        if(!existsById(postId)){
            JOptionPane.showMessageDialog(null, "Bu id'ye sahip bir post bulunmamaktadır");
        }
        else{
            String query = "update posts set request = ? where postid = ?";
            try {
                PreparedStatement preparedStatement = DBConnection.createInstance().prepareStatement(query);
                preparedStatement.setString(1, newRequest);
                preparedStatement.setInt(2, postId);
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateStatus(){
        if(!existsById(postId)){
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
        if (!existsById(postId)) {
            JOptionPane.showMessageDialog(null, "Bu id'ye sahip bir post bulunmamaktadır");
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

    public static void deletePostsByDate(){
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
