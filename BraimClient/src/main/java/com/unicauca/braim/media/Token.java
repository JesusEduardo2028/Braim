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
public class Token {
    private String access_token;
    private String expires_in;
    private String user_logged;
    
    public Token(String access_token, String expires_in, String user_logged) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.user_logged = user_logged;
    }

    public String getUser_logged() {
        return user_logged;
    }

    public void setUser_logged(String user_logged) {
        this.user_logged = user_logged;
    }
    
    
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }
    
    
    
}
