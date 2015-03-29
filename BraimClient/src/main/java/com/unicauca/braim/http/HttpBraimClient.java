/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.braim.http;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.Constants;
import com.unicauca.braim.media.Song;
import com.unicauca.braim.media.Token;
import com.unicauca.braim.media.User;
import com.unicauca.braim.media.Util;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author jesus
 */
public class HttpBraimClient {

    private static final String api_url = Util.server_url + Util.api_port;
    private static final String socket_url = Util.server_url + Util.socket_port;
    private final HttpClient client;

    public HttpBraimClient() {
        this.client = new HttpClient();
    }

    public int getConnectionStatus() throws IOException {
        GetMethod method = new GetMethod(socket_url);
        int statusCode = client.executeMethod(method);
        return (statusCode);
    }

    public Token GET_Token(String email_or_username, String password) throws IOException, Exception {
        Token token = null;
        Gson gson = new Gson();

        String data = "data=" + email_or_username + "&password=" + password;
        GetMethod method = new GetMethod(api_url + "/api/v1/token?" + data);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + method.getStatusLine());
            throw new Exception("The username or password are invalid");
        }

        byte[] responseBody = method.getResponseBody();
        String response = new String(responseBody, "UTF-8");
        token = gson.fromJson(response, Token.class);
        System.out.println("algo");

        return token;
    }

    public Song[] GET_Songs(int page, String access_token) throws IOException {
        String token = "braim_token=" + access_token;
        String data = "page=" + page + "&per_page=10";
        GetMethod method = new GetMethod(api_url + "/api/v1/songs?" + token + "&" + data);

        Song[] songList = null;
        Gson gson = new Gson();

        try {
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));

            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            byte[] responseBody = method.getResponseBody();
            String response = new String(responseBody, "UTF-8");
            songList = gson.fromJson(response, Song[].class);

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HttpBraimClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return songList;
    }

    public User GET_User(String username, String access_token) throws IOException, Exception {
        User user = null;
        Gson gson = new Gson();

        String token_data = "?braim_token=" + access_token;

        GetMethod method = new GetMethod(api_url + "/api/v1/users/" + username + token_data);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + method.getStatusLine());
            throw new Exception("The username or password are invalid");
        }

        byte[] responseBody = method.getResponseBody();
        String response = new String(responseBody, "UTF-8");
        user = gson.fromJson(response, User.class);
        System.out.println(user.getEmail() + "Has been retrieved");

        return user;
    }
}
