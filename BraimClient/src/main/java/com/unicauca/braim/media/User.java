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
public class User {

    private String username;
    private String password;
    private Token auth_token;

    public User(String username, String password, Token auth_token) {
        this.username = username;
        this.password = password;
        this.auth_token = auth_token;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Token getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(Token auth_token) {
        this.auth_token = auth_token;
    }
    
    

}
