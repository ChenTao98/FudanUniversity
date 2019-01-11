package com.app.zpvoh.imgcluster.sqlUtils;

import android.util.Log;

import com.app.zpvoh.imgcluster.entities.Comment;
import com.app.zpvoh.imgcluster.entities.Group;
import com.app.zpvoh.imgcluster.entities.Image;
import com.app.zpvoh.imgcluster.entities.User;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.app.zpvoh.imgcluster.sqlUtils.InitEntityUtils.initCommentList;
import static com.app.zpvoh.imgcluster.sqlUtils.InitEntityUtils.initGroupList;
import static com.app.zpvoh.imgcluster.sqlUtils.InitEntityUtils.initImageList;
import static com.app.zpvoh.imgcluster.sqlUtils.InitEntityUtils.initUser;

/**
 * Created by zpvoh on 19-1-7.
 */

public class SqlDao {
    private static QueryRunner queryRunner = new QueryRunner();

    public static boolean signUp(Connection connection, String email, String username, String pass) {
        //TODO:fill up the code
        try {
            int count = Integer.valueOf(queryRunner.query(connection, "select count(*) from user_info where user_mail=?",
                    new ScalarHandler(1), email).toString());
            if (count != 0) {
                return false;
            }
            String passKey = MD5Utils.MD5Encode(pass, "utf8");
            queryRunner.update(connection, "insert into user_info(user_mail,user_name,password) values(?,?,?)", email, username, passKey);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User signIn(Connection connection, String email, String pass) {
        //TODO:fill up the code
        String passKey = MD5Utils.MD5Encode(pass, "utf8");
        try {
            PreparedStatement pst = connection.prepareStatement("select user_id,user_mail,user_name from user_info where user_mail=? and password=?");
            pst.setString(1, email);
            pst.setString(2, passKey);
            ResultSet resultSet = pst.executeQuery();
            User user = initUser(resultSet);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Group> getUserGroups(Connection connection, int uid) {
        //TODO:fill up the code
        SqlDao sqlDao = new SqlDao();
        Callable getUserGroup = sqlDao.new GetUserGroup(connection, uid);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future future = pool.submit(getUserGroup);
        try {
            ArrayList<Group> arrayList = (ArrayList<Group>) future.get();
            Log.i("arraylist", arrayList.size() + " ");
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static ArrayList<Image> getImageByGroup(Connection connection, int group_id) {
        //TODO:fill up the code
        SqlDao sqlDao = new SqlDao();
        Callable getImage = sqlDao.new GetImageByGroup(connection, group_id);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future future = pool.submit(getImage);
        try {
            ArrayList<Image> arrayList = (ArrayList<Image>) future.get();
            Log.i("arraylist", arrayList.size() + " ");
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static ArrayList<Comment> getCommentByImage(Connection connection, int img_id) {
        //TODO:fill up the code
//        Connection connection=jdbcUtils.getConnection();
        SqlDao sqlDao = new SqlDao();
        Callable getComment = sqlDao.new GetCommentByImage(connection, img_id);
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future future = pool.submit(getComment);
        try {
            ArrayList<Comment> arrayList = (ArrayList<Comment>) future.get();
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static Group createGroup(Connection connection, int uid, String name) {
        //TODO:fill up the code
        SqlDao sqlDao = new SqlDao();
        Callable callable = sqlDao.new CreateGroup(connection, uid, name);
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future future = pool.submit(callable);
        try {
            return (Group) future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getValidNumber(Connection connection, int group_id) {
        //TODO:fill up the code
        SqlDao sqlDao = new SqlDao();
        Callable callable = sqlDao.new GetValidNumber(connection, group_id);
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future future = pool.submit(callable);
        try {
            return (String) future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean uploadImage(Connection connection, String imagePath,String name, int groupId) {
        //TODO:fill up the code
        SqlDao sqlDao = new SqlDao();
        Callable upLoadImage=sqlDao.new UpLoadImage(connection, imagePath, name, groupId);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future future = pool.submit(upLoadImage);
        try {
            return (boolean) future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Comment submitComment(Connection connection, int uid, int img_id, String content) {
        //TODO:fill up the code
        SqlDao sqlDao = new SqlDao();
        Callable callable = sqlDao.new SubmitComment(connection, uid, img_id, content);
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future future = pool.submit(callable);
        try {
            return (Comment) future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Group joinGroup(Connection connection, int uid, String validNumber) {
        //TODO:fill up the code
        SqlDao sqlDao = new SqlDao();
        Callable callable = sqlDao.new JoinGroup(connection,uid,validNumber);
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future future = pool.submit(callable);
        try {
            return (Group) future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    class GetUserGroup implements Callable {
        private Connection connection;
        private int uid;

        public GetUserGroup(Connection connection, int uid) {
            this.connection = connection;
            this.uid = uid;
        }

        @Override
        public Object call() throws Exception {
            ArrayList<Group> arrayList;
            try {
                PreparedStatement pst = connection.prepareStatement("select group_id,group_name from user_and_group natural join group_info where user_id=?");
                pst.setInt(1, uid);
                ResultSet resultSet = pst.executeQuery();
                arrayList = initGroupList(resultSet);
                Log.i("getUserGroup", arrayList.size() + " ");
                return arrayList;
            } catch (SQLException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
    }

    class GetImageByGroup implements Callable {
        private Connection connection;
        private int groupId;

        public GetImageByGroup(Connection connection, int groupId) {
            this.connection = connection;
            this.groupId = groupId;
        }

        @Override
        public Object call() throws Exception {
            ArrayList<Group> arrayList;
            ArrayList<Image> images = new ArrayList<>();
            try {
                PreparedStatement pst = connection.prepareStatement("select image_id,group_id,image_name,image_path,up_time from image_info where group_id=?");
                pst.setInt(1, groupId);
                ResultSet resultSet = pst.executeQuery();
                images = initImageList(resultSet);
                return images;
            } catch (SQLException e) {
                e.printStackTrace();
                return images;
            }
        }
    }

    class GetCommentByImage implements Callable {
        private Connection connection;
        private int img_id;

        public GetCommentByImage(Connection connection, int img_id) {
            this.connection = connection;
            this.img_id = img_id;
        }

        @Override
        public Object call() throws Exception {
            ArrayList<Comment> commentArrayList = new ArrayList<>();
            try {
                PreparedStatement pst = connection.prepareStatement("select user_name,content from user_info natural join comment where image_id=?");
                pst.setInt(1, img_id);
                ResultSet resultSet = pst.executeQuery();
                commentArrayList = initCommentList(resultSet);
                return commentArrayList;
            } catch (SQLException e) {
                e.printStackTrace();
                return commentArrayList;
            }
        }
    }

    class CreateGroup implements Callable {
        private Connection connection;
        private int uid;
        private String name;

        public CreateGroup(Connection connection, int uid, String name) {
            this.connection = connection;
            this.uid = uid;
            this.name = name;
        }

        @Override
        public Object call() throws Exception {
            try {
                int count = Integer.valueOf(queryRunner.query(connection, "select count(*) from group_info where group_name=?", new ScalarHandler(1), name).toString());
                if (count != 0) {
                    return null;
                }
                connection.setAutoCommit(false);
                queryRunner.update(connection, "insert into group_info(group_name) values(?)", name);
                int groupId = Integer.valueOf(queryRunner.query(connection, "SELECT LAST_INSERT_ID()", new ScalarHandler(1)).toString());
                queryRunner.update(connection, "insert into user_and_group(user_id,group_id) values(?,?)", uid, groupId);
                connection.commit();
                connection.setAutoCommit(true);
                return new Group(groupId, name);
            } catch (SQLException e) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    return null;
                }
                e.printStackTrace();
                return null;
            }
        }
    }

    class GetValidNumber implements Callable {
        private Connection connection;
        private int group_id;

        public GetValidNumber(Connection connection, int group_id) {
            this.connection = connection;
            this.group_id = group_id;
        }

        @Override
        public Object call() throws Exception {
            Date date = new Date();
            int year = date.getYear() + 1900;
            int month = date.getMonth() + 1;
            int day = date.getDate();
            String dayStr = day < 10 ? "0" + day : day + "";
            String monthStr = month < 10 ? "0" + month : month + "";
            String validNumber = year + monthStr + dayStr + group_id;
            String time = year + "-" + monthStr + "-" + dayStr + " 23:59:59";
            queryRunner.update(connection, "update group_info set valid_code=?,valid_time=? where group_id=?", validNumber, time, group_id);
            return validNumber;
        }
    }

    class SubmitComment implements Callable {
        private Connection connection;
        private int uid;
        private int img_id;
        private String content;

        public SubmitComment(Connection connection, int uid, int img_id, String content) {
            this.connection = connection;
            this.uid = uid;
            this.img_id = img_id;
            this.content = content;
        }

        @Override
        public Object call() throws Exception {
            queryRunner.update(connection, "insert into comment(user_id,image_id,content) values(?,?,?)", uid, img_id, content);
            String name = queryRunner.query(connection, "select user_name from user_info where user_id=?", new ScalarHandler(1), uid).toString();
            return new Comment(name, content);
        }
    }

    class JoinGroup implements Callable {
        private Connection connection;
        private int uid;
        private String validNumber;

        public JoinGroup(Connection connection, int uid, String validNumber) {
            this.connection = connection;
            this.uid = uid;
            this.validNumber = validNumber;
        }

        @Override
        public Object call() throws Exception {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(date);

            PreparedStatement pst = connection.prepareStatement("select group_id,group_name from group_info where valid_code=? and valid_time>=?");
            pst.setString(1, validNumber);
            pst.setString(2, time);
            ResultSet resultSet = pst.executeQuery();
            if (!resultSet.next()) return null;
            int groupId = resultSet.getInt(1);
            String groupName = resultSet.getString(2);
            int count = Integer.valueOf(queryRunner.query(connection, "select count(*) from user_and_group where " +
                    "user_id=? and group_id=?", new ScalarHandler(1), uid, groupId).toString());
            if (count != 0) {
                return new Group(groupId, groupName);
            }
            queryRunner.update(connection, "insert into user_and_group(user_id,group_id) values(?,?)", uid, groupId);
            return new Group(groupId, groupName);
        }
    }
    class UpLoadImage implements Callable{
        private String imagePath;
        private Connection connection;
        private String name;
        private int groupId;

        public UpLoadImage(Connection connection, String imagePath,String name, int groupId) {
            this.imagePath = imagePath;
            this.connection = connection;
            this.name = name;
            this.groupId = groupId;
        }

        @Override
        public Object call() throws Exception {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            SCP scp = SCP.getScpUtilsInstance("root", "82870808Qyy", "139.196.80.102");
            scp.connect();
            String path=imagePath.substring(imagePath.lastIndexOf("/")+1);;
            if (scp.getIsAuthed()) {
                scp.putFile(imagePath, "~/apache-tomcat-9.0.10/webapps/pj-imgs");
            }
            queryRunner.update(connection,"insert into image_info(up_time,image_name,user_id,image_path,group_id) values(?,?,?,?,?)",time,name,1,path,groupId);
            return true;
        }
    }
}