/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.braim.media;

/**
 *
 * @author jesus
 */
public class Session {
    
    private String id;
    private Float start_at;
    private String user_id;

    public Session(String id, Float start_at, String user_id) {
        this.id = id;
        this.start_at = start_at;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Float getStart_at() {
        return start_at;
    }

    public void setStart_at(Float start_at) {
        this.start_at = start_at;
    }
    
    
    
    
}
