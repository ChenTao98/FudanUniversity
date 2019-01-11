package com.app.zpvoh.imgcluster.entities;

import java.io.Serializable;

/**
 * Created by zpvoh on 19-1-7.
 */

public class Group implements Serializable {
    public int group_id;
    public String group_name;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public Group() {

    }

    public Group(int group_id, String group_name){
        this.group_id=group_id;
        this.group_name=group_name;
    }
}
