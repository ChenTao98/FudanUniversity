package com.app.zpvoh.imgcluster.entities;

import java.io.Serializable;

/**
 * Created by zpvoh on 19-1-7.
 */

public class Comment implements Serializable{
    public String username;
    public String content;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment() {

    }

    public Comment(String username, String content){
        this.username=username;
        this.content=content;
    }
}
