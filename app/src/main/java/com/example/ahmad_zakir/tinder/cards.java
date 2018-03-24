package com.example.ahmad_zakir.tinder;

/**
 * Created by ahmad_zakir on 2/23/2018.
 */

public class cards {

    private  String userid;
    private  String name;

    public cards (String userid, String name){
        this.userid = userid;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
