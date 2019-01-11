package com.app.zpvoh.imgcluster.sqlUtils;

import android.util.Log;

import com.app.zpvoh.imgcluster.entities.Comment;
import com.app.zpvoh.imgcluster.entities.Group;
import com.app.zpvoh.imgcluster.entities.Image;
import com.app.zpvoh.imgcluster.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InitEntityUtils {
    public static User initUser(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) return null;
        Log.i("getUser", "success");
        User user = new User();
        user.setUid(resultSet.getInt(1));
        user.setEmail(resultSet.getString(2));
        user.setUsername(resultSet.getString(3));
        return user;
    }

    public static ArrayList<Group> initGroupList(ResultSet resultSet) throws SQLException {
        ArrayList<Group> arrayList = new ArrayList<>();
        while (resultSet.next()) {
            Group group = new Group();
            group.setGroup_id(resultSet.getInt(1));
            group.setGroup_name(resultSet.getString(2));
            arrayList.add(group);
        }
        return arrayList;
    }

    public static ArrayList<Image> initImageList(ResultSet resultSet) throws SQLException {
        ArrayList<Image> arrayList = new ArrayList<>();
        while (resultSet.next()) {
            Image image = new Image();
            image.setImg_id(resultSet.getInt(1));
            image.setGroup_id(resultSet.getInt(2));
            image.setName(resultSet.getString(3));
            image.setPath("http://139.196.80.102:8080/pj-imgs/"+resultSet.getString(4));
            image.setTime(resultSet.getTimestamp(5).toString());
            arrayList.add(image);
        }
        return arrayList;
    }

    public static ArrayList<Comment> initCommentList(ResultSet resultSet) throws SQLException {
        ArrayList<Comment> arrayList = new ArrayList<>();
        while (resultSet.next()) {
            Comment comment = new Comment();
            comment.setUsername(resultSet.getString(1));
            comment.setContent(resultSet.getString(2));
            arrayList.add(comment);
        }
        return arrayList;
    }
}
