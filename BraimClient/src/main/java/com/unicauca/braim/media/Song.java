/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.braim.media;

import java.util.List;

/**
 *
 * @author jesus
 */
public class Song {
    private String id;
    private String name;
    private String url;
    private String author;
    private String duration;
    //private static final String main_url = Util.server_url;

    private static final String main_url = Util.server_url + Util.api_port;

    public Song() {
    }

    public Song(String id,String name, String url, String author,String duration) {
        this.name = name;
        this.url = url;
        this.author = author;
        this.duration = duration;
        this.id = id;
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return main_url+url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean has_valid_name() {
        return this.name != null;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    
    
}
