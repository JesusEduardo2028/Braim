package com.unicauca.braim.braimclient;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.unicauca.braim.emotiv.Edk;
import com.unicauca.braim.emotiv.EdkErrorCode;
import com.unicauca.braim.emotiv.EmoState;
import com.unicauca.braim.http.HttpBraimClient;
import com.unicauca.braim.media.MusicPlayer;
import com.unicauca.braim.media.Song;
import com.unicauca.braim.media.User;
import com.unicauca.braim.media.Util;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.apache.commons.httpclient.HttpStatus;
import org.json.simple.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jesus Muñoz
 */

public class BraimGui extends javax.swing.JFrame {

    /**
     * Creates new form BraimGui
     */
    private EmotivEngineTask emotivTask;
    private final MusicPlayer player;
    private final HttpBraimClient client;
    private Socket socket;
    private final User currentUser;
    private Song actualSong;
    private Song[] actualSongList;
    
    public BraimGui(User user) {
        this.currentUser = user;
        this.client = new HttpBraimClient(user.getAuth_token());
        initComponents();
        //Create the player after initialize the gui otherwise it fails
        this.player = new MusicPlayer(lbl_progress);
        add_mouse_listener_to_list();
        create_socket();
        setLocationRelativeTo(null);
    }
    
    //**** Methods in constructor ****
    private void add_mouse_listener_to_list() {
        
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        actualSong = actualSongList[index];
                        lbl_song_name.setText(actualSong.getName());
                        play_song(actualSong);
                    }
                }
            }
        };
        list_songs.addMouseListener(mouseListener);
    }

    private void create_socket() {
        try {
            socket = IO.socket(Util.server_url+Util.socket_port);
        } catch (URISyntaxException ex) {
           System.out.println("There is an error with node"+ex);
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("Connected to Braim Server");
                lbl_username.setText("New user connected...");
                enable_panels(true);
            }
        });
        socket.on(Socket.EVENT_DISCONNECT,new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                System.out.println("Disconnected of Braim Server..");
                lbl_status.setText("Without Connection...");
                lbl_status.setForeground(Color.BLACK);
                enable_panels(false);
                lbl_username.setText("No user connected");
                menu_item_emo_device.setEnabled(true);
                
                //** Emotiv disconnection task generates a fatal error **
                //emotivTask.cancel(true);
                //emotivTask = null;
            }
        });
        socket.on("user added", new Emitter.Listener() {

            @Override
            public void call(Object... os) {
                lbl_username.setText(os[0].toString());
                menu_item_emo_device.setEnabled(false);
                lbl_status.setText("Connected to Braim Server");
                lbl_status.setForeground(Color.GREEN);
                (emotivTask = new EmotivEngineTask()).execute();
            }
        } );
        
        
        
        try {
            int status = client.getConnectionStatus();
            if(status == HttpStatus.SC_OK){
                socket.connect();
                socket.emit("join", currentUser.getUsername());
            }else{
                throw new IOException();
            }
        } catch (IOException ex) {
            Logger.getLogger(BraimGui.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "There is a problem with the Internet connection!");
            socket.disconnect();
        }
        
    }
    
    //**** Helpers mehtods  ****

    private void enable_panels(boolean value) {
        tab_panel.setEnabled(value);
        tab_panel_data.setEnabled(value);
        tab_panel_recommendation.setEnabled(value);
        tab_panel_training.setEnabled(value);
    }
    
    private void getTrainingSongList(int page) throws IOException{
        
        actualSongList = client.getTrainingSongList(page);
        
        list_songs.setModel(new javax.swing.AbstractListModel() {
            @Override
            public int getSize() { return actualSongList.length; }
            @Override
            public Object getElementAt(int i) { return actualSongList[i].getName(); }
        });
    }
    
    private void play_song(Song song) {
        
        if (song != null) {
            try {
                //(songTask = new SongTask(actualSong)).execute();
                player.OpenFile(song.getUrl());
                player.TooglePlay();
                btn_play.setEnabled(false);
                btn_pause.setEnabled(true);
                emit_action("play");
        
            } catch (Exception ex) {
                
                Logger.getLogger(BraimGui.class.getName()).log(Level.SEVERE, null, ex);
                btn_play.setEnabled(true);
                btn_pause.setEnabled(false);
                player.Stop();
                JOptionPane.showMessageDialog(null, "There was an error playing the song");
            
            }
        }
    
    }
    
    private void emit_action(String play) {
                    
        long lDateTime = System.currentTimeMillis();
        JSONObject json_values = new JSONObject();
        json_values.put("song_id", actualSong.getId());
        json_values.put("action", play);
        json_values.put("timestamp",lDateTime);
        socket.emit("add_player_info", json_values);

    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        lbl_braim_brand = new javax.swing.JLabel();
        pnl_user_settings = new javax.swing.JPanel();
        lbl_user = new javax.swing.JLabel();
        lbl_st = new javax.swing.JLabel();
        lbl_status = new javax.swing.JLabel();
        lbl_username = new javax.swing.JLabel();
        tab_panel = new javax.swing.JTabbedPane();
        tab_panel_data = new javax.swing.JPanel();
        lbl_node1 = new javax.swing.JLabel();
        lbl_node2 = new javax.swing.JLabel();
        lbl_node3 = new javax.swing.JLabel();
        txt_node_1 = new java.awt.TextField();
        txt_node_2 = new java.awt.TextField();
        txt_node_3 = new java.awt.TextField();
        lbl_node4 = new javax.swing.JLabel();
        lbl_node5 = new javax.swing.JLabel();
        lbl_node6 = new javax.swing.JLabel();
        lbl_node7 = new javax.swing.JLabel();
        lbl_node8 = new javax.swing.JLabel();
        lbl_node9 = new javax.swing.JLabel();
        txt_node_4 = new java.awt.TextField();
        txt_node_5 = new java.awt.TextField();
        txt_node_6 = new java.awt.TextField();
        txt_node_7 = new java.awt.TextField();
        txt_node_8 = new java.awt.TextField();
        txt_node_9 = new java.awt.TextField();
        lbl_node10 = new javax.swing.JLabel();
        lbl_node11 = new javax.swing.JLabel();
        lbl_node12 = new javax.swing.JLabel();
        lbl_node13 = new javax.swing.JLabel();
        lbl_node14 = new javax.swing.JLabel();
        txt_node_10 = new java.awt.TextField();
        txt_node_11 = new java.awt.TextField();
        txt_node_12 = new java.awt.TextField();
        txt_node_13 = new java.awt.TextField();
        txt_node_14 = new java.awt.TextField();
        lbl_raw_emotiv = new javax.swing.JLabel();
        lbl_excitement = new javax.swing.JLabel();
        lbl_meditation = new javax.swing.JLabel();
        lbl_frustration = new javax.swing.JLabel();
        lbl_boredom = new javax.swing.JLabel();
        pg_excitement = new javax.swing.JProgressBar();
        pg_meditation = new javax.swing.JProgressBar();
        pg_frustration = new javax.swing.JProgressBar();
        pg_boredom = new javax.swing.JProgressBar();
        tab_panel_training = new javax.swing.JPanel();
        lbl_actual_song = new javax.swing.JLabel();
        lbl_song_name = new javax.swing.JLabel();
        btn_play = new javax.swing.JButton();
        btn_pause = new javax.swing.JButton();
        btn_previous = new javax.swing.JButton();
        btn_next = new javax.swing.JButton();
        scroll_songs = new javax.swing.JScrollPane();
        list_songs = new javax.swing.JList();
        lbl_songs_list = new javax.swing.JLabel();
        lbl_progress = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btn_refresh = new javax.swing.JButton();
        btn_stop = new javax.swing.JButton();
        tab_panel_recommendation = new javax.swing.JPanel();
        menu_braim = new javax.swing.JMenuBar();
        menu_application = new javax.swing.JMenu();
        menu_item_quit = new javax.swing.JMenuItem();
        menu_connect = new javax.swing.JMenu();
        menu_item_emo_device = new javax.swing.JMenuItem();
        menu_item_disconnect = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        lbl_braim_brand.setFont(new java.awt.Font("Ubuntu", 1, 25)); // NOI18N
        lbl_braim_brand.setForeground(new java.awt.Color(0, 162, 255));
        lbl_braim_brand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/unicauca/braim/images/1414623820_Brain-Games.png"))); // NOI18N
        lbl_braim_brand.setText("BraiM");

        pnl_user_settings.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pnl_user_settings.setMaximumSize(new java.awt.Dimension(344, 111));
        pnl_user_settings.setMinimumSize(new java.awt.Dimension(344, 111));

        lbl_user.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_user.setForeground(new java.awt.Color(0, 162, 255));
        lbl_user.setText("User:");

        lbl_st.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_st.setForeground(new java.awt.Color(0, 162, 255));
        lbl_st.setText("Status");

        lbl_status.setText("Without Connection...");

        lbl_username.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_username.setText("No user connected");

        javax.swing.GroupLayout pnl_user_settingsLayout = new javax.swing.GroupLayout(pnl_user_settings);
        pnl_user_settings.setLayout(pnl_user_settingsLayout);
        pnl_user_settingsLayout.setHorizontalGroup(
            pnl_user_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_user_settingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_user_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_username, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnl_user_settingsLayout.createSequentialGroup()
                        .addGroup(pnl_user_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_user)
                            .addGroup(pnl_user_settingsLayout.createSequentialGroup()
                                .addComponent(lbl_st)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_status)))
                        .addGap(0, 185, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnl_user_settingsLayout.setVerticalGroup(
            pnl_user_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_user_settingsLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(pnl_user_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_st)
                    .addComponent(lbl_status))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_user)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_username)
                .addGap(24, 24, 24))
        );

        tab_panel.setBackground(new java.awt.Color(255, 255, 255));
        tab_panel.setEnabled(false);

        tab_panel_data.setBackground(new java.awt.Color(255, 255, 255));
        tab_panel_data.setEnabled(false);

        lbl_node1.setText("Nodo 1");

        lbl_node2.setText("Nodo 2");

        lbl_node3.setText("Nodo 3");

        txt_node_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_node_1ActionPerformed(evt);
            }
        });

        lbl_node4.setText("Nodo 4");

        lbl_node5.setText("Nodo 5");

        lbl_node6.setText("Nodo 6");

        lbl_node7.setText("Nodo 7");

        lbl_node8.setText("Nodo 8");

        lbl_node9.setText("Nodo 9");

        lbl_node10.setText("Nodo 10");

        lbl_node11.setText("Nodo 11");

        lbl_node12.setText("Nodo 12");

        lbl_node13.setText("Nodo 13");

        lbl_node14.setText("Nodo 14");

        lbl_raw_emotiv.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_raw_emotiv.setText("Raw Emotiv Data:");

        lbl_excitement.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_excitement.setText("Excitement:");

        lbl_meditation.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_meditation.setText("Meditation:");

        lbl_frustration.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_frustration.setText("Frustration:");

        lbl_boredom.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_boredom.setText("Boredom:");

        javax.swing.GroupLayout tab_panel_dataLayout = new javax.swing.GroupLayout(tab_panel_data);
        tab_panel_data.setLayout(tab_panel_dataLayout);
        tab_panel_dataLayout.setHorizontalGroup(
            tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_panel_dataLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pg_excitement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_excitement))
                .addGap(18, 18, 18)
                .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(tab_panel_dataLayout.createSequentialGroup()
                        .addComponent(pg_meditation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pg_frustration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tab_panel_dataLayout.createSequentialGroup()
                        .addComponent(lbl_meditation)
                        .addGap(78, 78, 78)
                        .addComponent(lbl_frustration)))
                .addGap(18, 18, 18)
                .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_boredom)
                    .addComponent(pg_boredom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(tab_panel_dataLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_raw_emotiv)
                    .addGroup(tab_panel_dataLayout.createSequentialGroup()
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tab_panel_dataLayout.createSequentialGroup()
                                .addComponent(lbl_node1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_node_1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tab_panel_dataLayout.createSequentialGroup()
                                .addComponent(lbl_node2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_node_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tab_panel_dataLayout.createSequentialGroup()
                                .addComponent(lbl_node5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_node_5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tab_panel_dataLayout.createSequentialGroup()
                                .addComponent(lbl_node6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_node_6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tab_panel_dataLayout.createSequentialGroup()
                                .addComponent(lbl_node7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_node_7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tab_panel_dataLayout.createSequentialGroup()
                                .addComponent(lbl_node4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_node_4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tab_panel_dataLayout.createSequentialGroup()
                                .addComponent(lbl_node3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_node_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(38, 38, 38)
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lbl_node8)
                                .addComponent(lbl_node9))
                            .addComponent(lbl_node11)
                            .addComponent(lbl_node14)
                            .addComponent(lbl_node13)
                            .addComponent(lbl_node12)
                            .addComponent(lbl_node10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_node_12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_node_10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_node_11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_node_13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_node_14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_node_8, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(txt_node_9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tab_panel_dataLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txt_node_1, txt_node_2, txt_node_3, txt_node_4, txt_node_5, txt_node_6, txt_node_7});

        tab_panel_dataLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txt_node_10, txt_node_11, txt_node_12, txt_node_13, txt_node_14, txt_node_8, txt_node_9});

        tab_panel_dataLayout.setVerticalGroup(
            tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab_panel_dataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tab_panel_dataLayout.createSequentialGroup()
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_excitement)
                            .addComponent(lbl_meditation)
                            .addComponent(lbl_frustration)
                            .addComponent(lbl_boredom))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pg_excitement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pg_meditation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pg_frustration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(pg_boredom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lbl_raw_emotiv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab_panel_dataLayout.createSequentialGroup()
                        .addComponent(txt_node_8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_node_9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_node_10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_node_11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(txt_node_12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_node_13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_node_14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tab_panel_dataLayout.createSequentialGroup()
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_node_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_node8)
                            .addComponent(lbl_node1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_node2)
                            .addComponent(txt_node_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_node9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_node3)
                            .addComponent(txt_node_3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_node10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_node4)
                            .addComponent(txt_node_4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_node11))
                        .addGap(12, 12, 12)
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_node_5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_node5)
                            .addComponent(lbl_node12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_node6)
                            .addComponent(txt_node_6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_node13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tab_panel_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_node7)
                            .addComponent(txt_node_7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_node14))))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        tab_panel_dataLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txt_node_1, txt_node_2, txt_node_3, txt_node_4, txt_node_5, txt_node_6, txt_node_7});

        tab_panel.addTab("Emotiv Data", tab_panel_data);

        tab_panel_training.setBackground(new java.awt.Color(255, 255, 255));
        tab_panel_training.setEnabled(false);

        lbl_actual_song.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_actual_song.setText("Actual Song:");

        lbl_song_name.setText("There is any songs");

        btn_play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/unicauca/braim/images/1414642815_audio-video-outline-play-32.png"))); // NOI18N
        btn_play.setText("Play");
        btn_play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_playActionPerformed(evt);
            }
        });

        btn_pause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/unicauca/braim/images/1414642731_audio-video-outline-pause-32.png"))); // NOI18N
        btn_pause.setText("Pause");
        btn_pause.setEnabled(false);
        btn_pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pauseActionPerformed(evt);
            }
        });

        btn_previous.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/unicauca/braim/images/1414642806_audio-video-outline-first-track-32.png"))); // NOI18N
        btn_previous.setText("Previous");
        btn_previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_previousActionPerformed(evt);
            }
        });

        btn_next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/unicauca/braim/images/1414642794_audio-video-outline-last-track-32.png"))); // NOI18N
        btn_next.setText("Next");
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });

        list_songs.setModel(new javax.swing.AbstractListModel() {
            @Override
            public int getSize() { return 0; }
            @Override
            public Object getElementAt(int i) { return null; }
        });
        list_songs.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list_songsValueChanged(evt);
            }
        });
        list_songs.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                list_songsVetoableChange(evt);
            }
        });
        scroll_songs.setViewportView(list_songs);

        lbl_songs_list.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_songs_list.setText("Songs List: ");

        lbl_progress.setText("0:0");

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel1.setText("Position:");

        btn_refresh.setText("Get Training List");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });

        btn_stop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/unicauca/braim/images/1415418604_audio-video-outline-stop-32.png"))); // NOI18N
        btn_stop.setText("Stop");
        btn_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_stopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab_panel_trainingLayout = new javax.swing.GroupLayout(tab_panel_training);
        tab_panel_training.setLayout(tab_panel_trainingLayout);
        tab_panel_trainingLayout.setHorizontalGroup(
            tab_panel_trainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_panel_trainingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab_panel_trainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab_panel_trainingLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_progress)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(tab_panel_trainingLayout.createSequentialGroup()
                        .addGroup(tab_panel_trainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scroll_songs)
                            .addGroup(tab_panel_trainingLayout.createSequentialGroup()
                                .addGroup(tab_panel_trainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(tab_panel_trainingLayout.createSequentialGroup()
                                        .addComponent(lbl_songs_list)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn_refresh))
                                    .addGroup(tab_panel_trainingLayout.createSequentialGroup()
                                        .addComponent(lbl_actual_song)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbl_song_name))
                                    .addGroup(tab_panel_trainingLayout.createSequentialGroup()
                                        .addComponent(btn_play, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_pause)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_stop)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_previous)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_next)))
                                .addGap(0, 120, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        tab_panel_trainingLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_next, btn_pause, btn_play, btn_previous, btn_stop});

        tab_panel_trainingLayout.setVerticalGroup(
            tab_panel_trainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab_panel_trainingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tab_panel_trainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_songs_list)
                    .addComponent(btn_refresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scroll_songs, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_panel_trainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_actual_song)
                    .addComponent(lbl_song_name))
                .addGap(5, 5, 5)
                .addGroup(tab_panel_trainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_progress)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tab_panel_trainingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_play)
                    .addComponent(btn_pause)
                    .addComponent(btn_previous)
                    .addComponent(btn_next)
                    .addComponent(btn_stop))
                .addContainerGap())
        );

        tab_panel.addTab("Training", tab_panel_training);

        tab_panel_recommendation.setBackground(new java.awt.Color(255, 255, 255));
        tab_panel_recommendation.setEnabled(false);

        javax.swing.GroupLayout tab_panel_recommendationLayout = new javax.swing.GroupLayout(tab_panel_recommendation);
        tab_panel_recommendation.setLayout(tab_panel_recommendationLayout);
        tab_panel_recommendationLayout.setHorizontalGroup(
            tab_panel_recommendationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 703, Short.MAX_VALUE)
        );
        tab_panel_recommendationLayout.setVerticalGroup(
            tab_panel_recommendationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 333, Short.MAX_VALUE)
        );

        tab_panel.addTab("Recommendation", tab_panel_recommendation);

        menu_application.setText("Application");

        menu_item_quit.setText("Quit");
        menu_item_quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_quitActionPerformed(evt);
            }
        });
        menu_application.add(menu_item_quit);

        menu_braim.add(menu_application);

        menu_connect.setText("Emotiv Connection");

        menu_item_emo_device.setText("to EmoEngine");
        menu_item_emo_device.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_emo_deviceActionPerformed(evt);
            }
        });
        menu_connect.add(menu_item_emo_device);

        menu_item_disconnect.setText("Disconnect");
        menu_item_disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_item_disconnectActionPerformed(evt);
            }
        });
        menu_connect.add(menu_item_disconnect);

        menu_braim.add(menu_connect);

        setJMenuBar(menu_braim);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tab_panel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_braim_brand, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnl_user_settings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_braim_brand, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnl_user_settings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(tab_panel)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menu_item_quitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_quitActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_menu_item_quitActionPerformed

    private void menu_item_emo_deviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_emo_deviceActionPerformed
        
        create_socket();
    
    }//GEN-LAST:event_menu_item_emo_deviceActionPerformed

    private void btn_playActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_playActionPerformed
       
        // TODO add your handling code here:
        play_song(actualSong);
       
    }//GEN-LAST:event_btn_playActionPerformed

    private void btn_previousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_previousActionPerformed
        // TODO add your handling code here:
        //Emit action before changing the actual song
        emit_action("previous");
        
        int index = list_songs.getSelectedIndex();
        list_songs.setSelectedIndex(index - 1);
        Object selected_song = list_songs.getSelectedValue();
        lbl_song_name.setText(selected_song.toString());
        
        actualSong = actualSongList[list_songs.getSelectedIndex()];
        play_song(actualSong);
        
        
    }//GEN-LAST:event_btn_previousActionPerformed

    private void txt_node_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_node_1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_node_1ActionPerformed

    private void menu_item_disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_item_disconnectActionPerformed
        // TODO add your handling code here:
        
        
        socket.disconnect();
        //enable_panels(false);
    }//GEN-LAST:event_menu_item_disconnectActionPerformed

    private void btn_pauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pauseActionPerformed
            
        // TODO add your handling code here:
        if(player.isPlaying()){
            player.Pause();
            btn_pause.setEnabled(false);
            btn_play.setEnabled(true);
            emit_action("pause");
        }
        
    }//GEN-LAST:event_btn_pauseActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
        try {
            // TODO add your handling code here:
            getTrainingSongList(1);
        } catch (IOException ex) {
            Logger.getLogger(BraimGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void list_songsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list_songsValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_list_songsValueChanged

    private void list_songsVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_list_songsVetoableChange
        // TODO add your handling code here:
    }//GEN-LAST:event_list_songsVetoableChange

    private void btn_stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_stopActionPerformed
        
        // TODO add your handling code here:
        if(player.isPlaying()){
            player.Stop();
            btn_play.setEnabled(true);
            btn_pause.setEnabled(false);
            emit_action("stop");
        }
    
    }//GEN-LAST:event_btn_stopActionPerformed

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nextActionPerformed
        
        // TODO add your handling code here:
        //Emit before update song
        emit_action("next");
        
        int index = list_songs.getSelectedIndex();
        list_songs.setSelectedIndex(index + 1);
        Object selected_song = list_songs.getSelectedValue();
        lbl_song_name.setText(selected_song.toString());
        
        actualSong = actualSongList[list_songs.getSelectedIndex()];
        play_song(actualSong);
    
    }//GEN-LAST:event_btn_nextActionPerformed

    
    public class SongTask extends SwingWorker<Void, Song> {

        private final Song actual_song;

        public SongTask(Song song) {
            this.actual_song = song;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                player.OpenFile(actual_song.getUrl());
                player.Play();
            } catch (Exception e) {
                System.out.println("Ocurrio Un Error");
                System.out.println(e.toString());
            }
            return null;
        }
    }
    private class EmotivEngineTask extends SwingWorker<Void, EmoValues> {

        Pointer eEvent = Edk.INSTANCE.EE_EmoEngineEventCreate();
        Pointer eState = Edk.INSTANCE.EE_EmoStateCreate();
        IntByReference userID = null;
        IntByReference nSamplesTaken = null;
        short composerPort = 1726;
        int option = 1;
        int state = 0;
        float secs = 1;
        boolean readytocollect = false;

        @Override
        protected void done() {
            System.out.println("CERRANDO!!!");
            try {
                Edk.INSTANCE.EE_EngineDisconnect();
                Edk.INSTANCE.EE_EmoStateFree(eState);
                Edk.INSTANCE.EE_EmoEngineEventFree(eEvent);
                System.out.println("Disconnected!");

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        @Override
        protected void process(List<EmoValues> values) {
            EmoValues emoValues = values.get(values.size() - 1);
            
            txt_node_1.setText(emoValues.eegNodes[0] + "");
            txt_node_2.setText(emoValues.eegNodes[1] + "");
            txt_node_3.setText(emoValues.eegNodes[2] + "");
            txt_node_4.setText(emoValues.eegNodes[3] + "");
            txt_node_5.setText(emoValues.eegNodes[4] + "");
            txt_node_6.setText(emoValues.eegNodes[5] + "");
            txt_node_7.setText(emoValues.eegNodes[6] + "");
            txt_node_8.setText(emoValues.eegNodes[7] + "");
            txt_node_9.setText(emoValues.eegNodes[8] + "");
            txt_node_10.setText(emoValues.eegNodes[9] + "");
            txt_node_11.setText(emoValues.eegNodes[10] + "");
            txt_node_12.setText(emoValues.eegNodes[11] + "");
            txt_node_13.setText(emoValues.eegNodes[12] + "");
            txt_node_14.setText(emoValues.eegNodes[13] + "");
            pg_excitement.setValue(Math.round(emoValues.affectivExcitement * 100));
            pg_meditation.setValue(Math.round(emoValues.affectivMeditation * 100));
            pg_frustration.setValue(Math.round(emoValues.affectivFrustration * 100));
            pg_boredom.setValue(Math.round(emoValues.affectivEnagement * 100));
            
            long lDateTime = System.currentTimeMillis();
            JSONObject json_values = new JSONObject();
            json_values.put("excitement", emoValues.affectivExcitement);
            json_values.put("meditation", emoValues.affectivMeditation);
            json_values.put("frustration", emoValues.affectivFrustration);
            json_values.put("engagement", emoValues.affectivEnagement);
            json_values.put("nodes", emoValues.eegNodes);
            json_values.put("timestamp",lDateTime);
            socket.emit("add_emo_info", json_values);
        }

        @Override
        protected Void doInBackground() throws Exception {
            userID = new IntByReference(0);
            nSamplesTaken = new IntByReference(0);

            if (Edk.INSTANCE.EE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
                System.out.println("Emotiv Engine start up failed.");
            }

            Pointer hData = Edk.INSTANCE.EE_DataCreate();
            Edk.INSTANCE.EE_DataSetBufferSizeInSec(secs);
            System.out.print("Buffer size in secs: ");
            System.out.println(secs);
            System.out.println("Start receiving EEG Data!");

            while (true) {
                state = Edk.INSTANCE.EE_EngineGetNextEvent(eEvent);
                double[] nodes = new double[14];
                float aEx = 0;
                float aFr = 0;
                float aMe = 0;
                float aEn = 0;

                // New event needs to be handled
                int eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(eEvent);
                if (state == EdkErrorCode.EDK_OK.ToInt()) {

                    Edk.INSTANCE.EE_EmoEngineEventGetUserId(eEvent, userID);

                    // Log the EmoState if it has been updated
                    if (eventType == Edk.EE_Event_t.EE_UserAdded.ToInt()) {
                        if (userID != null) {
                            System.out.println("User added");
                            Edk.INSTANCE.EE_DataAcquisitionEnable(userID.getValue(), true);
                            readytocollect = true;
                        }
                    }
                } else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
                    System.out.println("Internal error in Emotiv Engine!");
                    break;
                }

                if (readytocollect) {
                    Edk.INSTANCE.EE_DataUpdateHandle(0, hData);
                    Edk.INSTANCE.EE_DataGetNumberOfSample(hData, nSamplesTaken);
                    if (nSamplesTaken != null) {
                        if (nSamplesTaken.getValue() != 0) {
                            double[] data = new double[nSamplesTaken.getValue()];
                            for (int sampleIdx = 0; sampleIdx < nSamplesTaken.getValue(); ++sampleIdx) {
                                
                                for (int i = 0; i < 14; i++) {
                                    Edk.INSTANCE.EE_DataGet(hData, i, data, nSamplesTaken.getValue());
                                    nodes[i] = data[sampleIdx];
                                }

                                if (eventType == Edk.EE_Event_t.EE_EmoStateUpdated.ToInt()) {
                                    Edk.INSTANCE.EE_EmoEngineEventGetEmoState(eEvent, eState);
                                    //float timestamp = EmoState.INSTANCE.ES_GetTimeFromStart(eState);
                                    aEx = EmoState.INSTANCE.ES_AffectivGetExcitementShortTermScore(eState);
                                    aFr = EmoState.INSTANCE.ES_AffectivGetFrustrationScore(eState);
                                    aMe = EmoState.INSTANCE.ES_AffectivGetMeditationScore(eState);
                                    aEn = EmoState.INSTANCE.ES_AffectivGetEngagementBoredomScore(eState);
                                }
                                publish(new EmoValues(nodes, aEx, aFr, aMe, aEn));
                            }
                        }
                    }
                }
            }
            return null;
        }
    }
    private static class EmoValues {

        private final double[] eegNodes;
        private final float affectivExcitement;
        private final float affectivFrustration;
        private final float affectivMeditation;
        private final float affectivEnagement;

        public EmoValues(double[] nodes, float affectivExcitement, float affectivFrustration, float affectivMeditation, float affectivEnagement) {
            this.eegNodes = nodes;
            this.affectivExcitement = affectivExcitement;
            this.affectivFrustration = affectivFrustration;
            this.affectivMeditation = affectivMeditation;
            this.affectivEnagement = affectivEnagement;
        }
    }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_pause;
    private javax.swing.JButton btn_play;
    private javax.swing.JButton btn_previous;
    private javax.swing.JButton btn_refresh;
    private javax.swing.JButton btn_stop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbl_actual_song;
    private javax.swing.JLabel lbl_boredom;
    private javax.swing.JLabel lbl_braim_brand;
    private javax.swing.JLabel lbl_excitement;
    private javax.swing.JLabel lbl_frustration;
    private javax.swing.JLabel lbl_meditation;
    private javax.swing.JLabel lbl_node1;
    private javax.swing.JLabel lbl_node10;
    private javax.swing.JLabel lbl_node11;
    private javax.swing.JLabel lbl_node12;
    private javax.swing.JLabel lbl_node13;
    private javax.swing.JLabel lbl_node14;
    private javax.swing.JLabel lbl_node2;
    private javax.swing.JLabel lbl_node3;
    private javax.swing.JLabel lbl_node4;
    private javax.swing.JLabel lbl_node5;
    private javax.swing.JLabel lbl_node6;
    private javax.swing.JLabel lbl_node7;
    private javax.swing.JLabel lbl_node8;
    private javax.swing.JLabel lbl_node9;
    private javax.swing.JLabel lbl_progress;
    private javax.swing.JLabel lbl_raw_emotiv;
    private javax.swing.JLabel lbl_song_name;
    private javax.swing.JLabel lbl_songs_list;
    private javax.swing.JLabel lbl_st;
    private javax.swing.JLabel lbl_status;
    private javax.swing.JLabel lbl_user;
    private javax.swing.JLabel lbl_username;
    private javax.swing.JList list_songs;
    private javax.swing.JMenu menu_application;
    private javax.swing.JMenuBar menu_braim;
    private javax.swing.JMenu menu_connect;
    private javax.swing.JMenuItem menu_item_disconnect;
    private javax.swing.JMenuItem menu_item_emo_device;
    private javax.swing.JMenuItem menu_item_quit;
    private javax.swing.JProgressBar pg_boredom;
    private javax.swing.JProgressBar pg_excitement;
    private javax.swing.JProgressBar pg_frustration;
    private javax.swing.JProgressBar pg_meditation;
    private javax.swing.JPanel pnl_user_settings;
    private javax.swing.JScrollPane scroll_songs;
    private javax.swing.JTabbedPane tab_panel;
    private javax.swing.JPanel tab_panel_data;
    private javax.swing.JPanel tab_panel_recommendation;
    private javax.swing.JPanel tab_panel_training;
    private java.awt.TextField txt_node_1;
    private java.awt.TextField txt_node_10;
    private java.awt.TextField txt_node_11;
    private java.awt.TextField txt_node_12;
    private java.awt.TextField txt_node_13;
    private java.awt.TextField txt_node_14;
    private java.awt.TextField txt_node_2;
    private java.awt.TextField txt_node_3;
    private java.awt.TextField txt_node_4;
    private java.awt.TextField txt_node_5;
    private java.awt.TextField txt_node_6;
    private java.awt.TextField txt_node_7;
    private java.awt.TextField txt_node_8;
    private java.awt.TextField txt_node_9;
    // End of variables declaration//GEN-END:variables
}
