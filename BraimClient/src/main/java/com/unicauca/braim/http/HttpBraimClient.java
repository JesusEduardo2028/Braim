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
    private static final String api_url = Util.server_url+Util.api_port;
    private static final String socket_url = Util.server_url+Util.socket_port;
    private final HttpClient client;
    private final Token auth_token;

    public HttpBraimClient(Token auth_token) {
        this.client = new HttpClient();
        this.auth_token = auth_token;
    }
    
    public int getConnectionStatus() throws IOException{
        GetMethod method = new GetMethod(socket_url);
        int statusCode = client.executeMethod(method);
        return(statusCode);
    }
    
    
    public String getSongUrl(String song_name) {
        GetMethod method = new GetMethod(api_url+"/song_url?name="+song_name.replace(" ", "%20"));
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
                new DefaultHttpMethodRetryHandler(3,false));
        String song_url = "";
        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }
            byte[] responseBody = method.getResponseBody();
            //System.out.println(new String(responseBody));
            JSONObject response = (JSONObject) JSONValue.parse(new String(responseBody));
            JSONObject file = (JSONObject) response.get("file");
            song_url = file.get("url").toString();
           
        } catch (HttpException e) {
          System.err.println("Fatal protocol violation: " + e.getMessage());
          e.printStackTrace();
        } catch (IOException e) {
          System.err.println("Fatal transport error: " + e.getMessage());
          e.printStackTrace();
        } finally {
          method.releaseConnection();
        }
        
        return song_url;
    }

    public Song[] getTrainingSongList(int page) throws IOException {
        String token = "songbook_token="+auth_token.getAccess_token();
        String data = "page="+page+"&per_page=10";
        GetMethod method = new GetMethod(api_url+"/api/v1/songs?"+token+"&"+data);
        
        Song[] songList = null ;
            Gson gson = new Gson();
            
        try {
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3,false));
            
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }
            
            byte[] responseBody = method.getResponseBody();
            String  response = new String(responseBody, "UTF-8");
            songList = gson.fromJson(response,Song[].class);
            
            } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HttpBraimClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        return songList;
    }
    
    
}
