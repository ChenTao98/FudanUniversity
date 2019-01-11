package com.app.zpvoh.imgcluster.entities;

import java.io.Serializable;

/**
 * Created by zpvoh on 19-1-7.
 */

public class Image implements Serializable {
    public int img_id;
    public int group_id;
    public String time;
    public String name;

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Image() {

    }

    public String path;

    public Image(int img_id, int group_id, String time, String name, String path){
        this.img_id=img_id;
        this.group_id=group_id;
        this.time=time;
        this.name=name;
        this.path=path;
    }
}
