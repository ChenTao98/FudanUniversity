package com.app.zpvoh.imgcluster.entities;

import java.io.Serializable;

/**
 * Created by zpvoh on 19-1-7.
 */

public class User implements Serializable {
    public int uid;
    public String username;
    public String email;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public User() {
    }

    public User(int uid, String username, String email){
        this.uid=uid;
        this.username=username;
        this.email=email;
    }
}
