/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.braim.media;

import java.io.DataInputStream;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

/**
 *
 * @author jesus
 */
public class MusicPlayer implements BasicPlayerListener {
    
    private BasicPlayer player;
    private BasicController audioController;
    private JLabel lbl_status;

    public MusicPlayer(JLabel lbl_progress) {
        this.player = new BasicPlayer();
        this.player.addBasicPlayerListener(this);
        this.lbl_status = lbl_progress;
        audioController= (BasicController) player;
    }
    
    public void OpenFile(String route) throws Exception {
        String full_route = route;
        audioController.open(new URL(route.replace(" ","%20")));
        //player.open(din);
    }
    
    public void Play() {
        try {
            //player.play();
            audioController.play();
        } catch (BasicPlayerException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public void TooglePlay(){
        try {
            int status = player.getStatus();
            if ( status == BasicPlayer.PLAYING)
                player.pause();
            else if(status == BasicPlayer.PAUSED ){
                player.resume();
            }else{
                player.play();
            }
        } catch (BasicPlayerException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Pause() {
        try {
            player.pause();
        } catch (BasicPlayerException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Resume()  {
        try {
            player.resume();
        } catch (BasicPlayerException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Stop() {
        try {
            player.stop();
        } catch (BasicPlayerException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void opened(Object o, Map map) {
        
    }

    @Override
    public void progress(int i, long l, byte[] bytes, Map map) {
        long milisegundos = l/1000;
        lbl_status.setText(String.format("%d min, %d sec", 
                                TimeUnit.MILLISECONDS.toMinutes(milisegundos),
                                TimeUnit.MILLISECONDS.toSeconds(milisegundos) - 
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milisegundos))
                            ));
    }

    @Override
    public void stateUpdated(BasicPlayerEvent bpe) {
        
    }

    @Override
    public void setController(BasicController bc) {
        
    }

    public boolean isPlaying() {
        int status = player.getStatus();
        return status == BasicPlayer.PLAYING;
    }
    
}
