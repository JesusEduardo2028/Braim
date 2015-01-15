/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.braim.http;

import com.google.gson.Gson;
import com.unicauca.braim.media.Token;
import com.unicauca.braim.media.Util;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 *
 * @author jesus
 */
public class HttpAuthClient {
    private static final String api_url = Util.server_url+Util.api_port;
    private final HttpClient client;

    public HttpAuthClient() {
        this.client = new HttpClient();
    }
    
    public Token authenticate(String email, String password) throws IOException, Exception{
        Token token = null ;
        Gson gson = new Gson();

            String data = "email="+email+"&password="+password;
            GetMethod method = new GetMethod(api_url+"/api/v1/token?"+data);
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3,false));
            
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
                throw new Exception("The username or password are invalid");
            }
            
            byte[] responseBody = method.getResponseBody();
            String  response = new String(responseBody, "UTF-8");
            token = gson.fromJson(response,Token.class);
            System.out.println("algo");

        return token;
    }

    public void launch_sign_up() {

        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI(Util.server_url+Util.api_port+"/users/sign_up"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
