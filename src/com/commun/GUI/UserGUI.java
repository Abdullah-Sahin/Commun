package com.commun.GUI;

import com.commun.AAHELPER.AAFunctions;
import com.commun.MODELS.Post;
import com.commun.MODELS.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Objects;

public class UserGUI extends JFrame{

    private final User user;
    private JPanel panelMain;
    private JPanel panelTop;
    private JLabel labelWelcome;
    private JButton buttonLogout;
    private JTabbedPane tabbedPane1;
    private DefaultTableModel modelOpenPosts;
    private JTable tableOpenPosts;
    private JButton buttonUserSettings;
    private JButton buttonInspect;
    private JComboBox comboBoxLocation;
    private JComboBox comboBoxReward;
    private JButton buttonSubmitFilters;
    private DefaultTableModel modelMyPosts;
    private JTable tableMyPosts;
    private JPanel panelPostManagement;
    private JLabel labelPostId;
    private JTextField textFieldPostId;
    private JComboBox comboBoxToDo;
    private JButton buttonSubmitToDo;
    private JPanel panelMyPosts;
    private JPanel panelMyDuties;
    private JPanel panelTop2;
    private DefaultTableModel modelMyJobs;
    private JTable tableMyJobs;
    private JTextField textFieldChosenPostId;
    private JPanel panelDutyManagement;
    private JTextField textFieldChosenDutyId;
    private JButton buttonCreatePost;
    private JPanel panelOpenPosts;
    private JButton buttonUpdate;
    private JButton buttonClaimDuty;
    private JButton buttonCompleted;
    private JTextField textFieldUserCoin;
    private JLabel labelChosenPostId;
    private JComboBox comboBoxRating;
    private JLabel labelRewards;
    private JLabel labelPlaces;
    private JLabel labelRating;
    private JButton buttonWithdraw;
    private JLabel labelPostID;

    public UserGUI(User user){
        this.user = user;
        add(panelMain);
        Post.deletePastDuePosts();
        labelWelcome.setText("Commun'a Hoşgeldin " + user.getUsername() + ". Kullanıcı ID: " + user.getUserid());
        setTextFieldUserCoin();


        modelOpenPosts = new DefaultTableModel();
        modelOpenPosts.setColumnIdentifiers(new Object[]{"Post Id","Poster Id","Location","Request", "Deadline", "Reward"});
        setModelOpenPosts();
        tableOpenPosts.setModel(modelOpenPosts);
        tableOpenPosts.getTableHeader().setReorderingAllowed(false);
        tableOpenPosts.getColumn("Post Id").setMaxWidth(60);
        tableOpenPosts.getColumn("Poster Id").setMaxWidth(60);
        tableOpenPosts.getColumn("Reward").setMaxWidth(60);

        tableOpenPosts.getSelectionModel().addListSelectionListener(e -> {
            try{
                textFieldChosenPostId.setText(tableOpenPosts.getValueAt(tableOpenPosts.getSelectedRow(),0).toString());
            }catch (Exception ignored){

            }
        });

        modelMyPosts = new DefaultTableModel();
        modelMyPosts.setColumnIdentifiers(new Object[]{"Post Id","Location","Request", "Deadline" ,"Reward","Claimer Id"});
        setModelMyPosts();
        tableMyPosts.setModel(modelMyPosts);
        tableMyPosts.getTableHeader().setReorderingAllowed(false);
        tableMyPosts.getColumn("Post Id").setMaxWidth(60);
        tableMyPosts.getColumn("Reward").setMaxWidth(60);
        tableMyPosts.getColumn("Claimer Id").setMaxWidth(60);

        tableMyPosts.getSelectionModel().addListSelectionListener(e -> {
            try{
                textFieldPostId.setText(tableMyPosts.getValueAt(tableMyPosts.getSelectedRow(),0).toString());
            }catch (Exception ignored){

            }
        });

        modelMyJobs = new DefaultTableModel();
        modelMyJobs.setColumnIdentifiers(new Object[]{"Post Id","Poster Id","Location","Request", "Deadline", "Reward"});
        setModelMyJobs();
        tableMyJobs.setModel(modelMyJobs);
        tableMyJobs.getTableHeader().setReorderingAllowed(false);
        tableMyJobs.getColumn("Post Id").setMaxWidth(60);
        tableMyJobs.getColumn("Poster Id").setMaxWidth(60);
        tableMyJobs.getColumn("Reward").setMaxWidth(60);

        tableMyJobs.getSelectionModel().addListSelectionListener(e -> {
            try{
                textFieldChosenDutyId.setText(tableMyJobs.getValueAt(tableMyJobs.getSelectedRow(),0).toString());
            }catch (Exception ignored){

            }
        });


        setSize(1000,750);
        setResizable(false);
        AAFunctions.setScreen(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        AAFunctions.setIcon(this);
        setVisible(true);




        //Listeners start from here
        buttonLogout.addActionListener(e -> {
            new LoginGUI();
            dispose();
        });
        buttonUserSettings.addActionListener(e -> new UserSettingsGUI(this, user));
        buttonCreatePost.addActionListener(e -> new CreatePostGUI(this, user));
        buttonUpdate.addActionListener(e -> {
            Post.deletePastDuePosts();
            setModelOpenPosts();
            setModelMyPosts();
            setModelMyJobs();
        });
        buttonSubmitFilters.addActionListener(e -> {
            int minPrice = 0, maxPrice = 0;
            String location = Objects.requireNonNull(comboBoxLocation.getSelectedItem()).toString();
            try{
                String[] values = Objects.requireNonNull(comboBoxReward.getSelectedItem()).toString().split("-");
                minPrice = Integer.parseInt(values[0]); maxPrice = Integer.parseInt(values[values.length-1]);
            }catch (Exception ignore){

            }
            setModelOpenPosts(Post.getOpenFilteredPosts(location, minPrice, maxPrice));
        });
        buttonClaimDuty.addActionListener(e -> {
            Post post = Post.getByPostId(Integer.parseInt(textFieldChosenPostId.getText()));
            user.claimPostByPostId(Objects.requireNonNull(post).getPostId());
            setModelOpenPosts();
            setModelMyJobs();
        });
        buttonInspect.addActionListener(e -> {
            Post post = Post.getByPostId(Integer.parseInt(textFieldChosenPostId.getText()));
            new PostGUI(Objects.requireNonNull(post));
        });
        buttonCompleted.addActionListener(e -> {
            try{
                Post post = Post.getByPostId(Integer.parseInt(textFieldPostId.getText()));
                Objects.requireNonNull(post).setRating(Integer.parseInt(Objects.requireNonNull(comboBoxRating.getSelectedItem()).toString()));
                post.updateRating();
                post.updateStatus();
                int coins = (post.getReward() * (post.getRating())) / 5;
                User claimer = User.getByUserId(post.getPosterId());
                Objects.requireNonNull(claimer).setCoins(claimer.getCoins() + coins);
                claimer.updateCoins();
                user.setCoins(user.getCoins() - coins);
                setTextFieldUserCoin();
            }catch (Exception ignore){

            }

        });

        buttonSubmitToDo.addActionListener(e -> {
            try{
                Post post = Post.getByPostId(Integer.parseInt(textFieldPostId.getText()));
                if(Objects.requireNonNull(comboBoxToDo.getSelectedItem()).toString().equalsIgnoreCase("ilanı güncelle")){
                    new UpdatePostGUI(this, Objects.requireNonNull(post));
                }
                else{
                    Objects.requireNonNull(post).delete();
                    setModelMyPosts();
                    setModelOpenPosts();
                }
            }catch (Exception ignore){

            }

        });
        buttonWithdraw.addActionListener(e -> {
            user.withdrawClaimByPostId(Integer.parseInt(textFieldChosenDutyId.getText()));
            setModelOpenPosts();
            setModelMyJobs();
        });
    }


    public void setModelOpenPosts(){
        DefaultTableModel model = modelOpenPosts;
        model.setRowCount(0);
        Object[] row = new Object[6];
        for(Post post: Post.getOpenPosts()){
            row[0] = post.getPostId();
            row[1] = post.getPosterId();
            row[2] = post.getLocation();
            row[3] = post.getRequest();
            String[] s = post.getDeadline().toString().split("T");
            row[4] = s[0] + " " + s[1];
            row[5] = post.getReward();
            modelOpenPosts.addRow(row);
        }
    }

    public void setModelOpenPosts(List<Post> filteredPosts){
        DefaultTableModel model = modelOpenPosts;
        model.setRowCount(0);
        Object[] row = new Object[6];
        for(Post post: filteredPosts){
            row[0] = post.getPostId();
            row[1] = post.getPosterId();
            row[2] = post.getLocation();
            row[3] = post.getRequest();
            String[] s = post.getDeadline().toString().split("T");
            row[4] = s[0] + " " + s[1];
            row[5] = post.getReward();
            modelOpenPosts.addRow(row);
        }
    }

    public void setModelMyPosts(){
        DefaultTableModel model = modelMyPosts;
        model.setRowCount(0);
        Object[] row = new Object[6];
        for(Post post: Post.getByPosterId(user.getUserid())){
            if(!post.isCompleted()){
                row[0] = post.getPostId();
                row[1] = post.getLocation();
                row[2] = post.getRequest();
                String[] s = post.getDeadline().toString().split("T");
                row[3] = s[0] + " " + s[1];
                row[4] = post.getReward();
                row[5] = post.getClaimerId();
                modelMyPosts.addRow(row);}
            }
    }

    public void setModelMyJobs(){
        DefaultTableModel model = modelMyJobs;
        model.setRowCount(0);
        Object[] row = new Object[6];
        for(Post post: Post.getByClaimerId(user.getUserid())){
            if(!post.isCompleted()){
                row[0] = post.getPostId();
                row[1] = post.getPosterId();
                row[2] = post.getLocation();
                row[3] = post.getRequest();
                String[] s = post.getDeadline().toString().split("T");
                row[4] = s[0] + " " + s[1];
                row[5] = post.getReward();
                modelMyJobs.addRow(row);
            }
        }
    }

    public void setTextFieldUserCoin(){
        textFieldUserCoin.setText("Mevcut coin " + user.getCoins());
    }

    public User getUser() {
        return user;
    }
}
