/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.braim.braimclient;

import com.unicauca.braim.http.HttpAuthClient;
import com.unicauca.braim.media.Token;
import com.unicauca.braim.media.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author jesus
 */
public class NewSessionGui extends javax.swing.JFrame {
    private final HttpAuthClient client;

    /**
     * Creates new form NewSessionGui
     */
    public NewSessionGui() {
        initComponents();
        client = new HttpAuthClient();
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_braim_brand = new javax.swing.JLabel();
        et_username = new javax.swing.JTextField();
        bt_sign_in = new javax.swing.JButton();
        et_password = new javax.swing.JPasswordField();
        lbl_username = new javax.swing.JLabel();
        lbl_password = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbl_braim_brand.setFont(new java.awt.Font("Ubuntu", 1, 25)); // NOI18N
        lbl_braim_brand.setForeground(new java.awt.Color(0, 162, 255));
        lbl_braim_brand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/unicauca/braim/images/1414623820_Brain-Games.png"))); // NOI18N
        lbl_braim_brand.setText("BraiM");

        et_username.setToolTipText("Username");

        bt_sign_in.setText("Sign in");
        bt_sign_in.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_sign_inActionPerformed(evt);
            }
        });

        et_password.setToolTipText("Password");

        lbl_username.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_username.setText("Email:");

        lbl_password.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_password.setText("Password:");

        jLabel1.setText("Don't have an account?");

        jButton1.setText("Sign up");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbl_braim_brand, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jLabel1)))
                        .addGap(0, 48, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(et_password, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(et_username, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bt_sign_in, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_username)
                                    .addComponent(lbl_password))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_braim_brand, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbl_username)
                .addGap(5, 5, 5)
                .addComponent(et_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_password)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(et_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_sign_in)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_sign_inActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_sign_inActionPerformed
        // TODO add your handling code here:
        String username = et_username.getText();
        String password = new String(et_password.getPassword());
        
        try {
            Token token = client.authenticate(username, password);
            if (token.getAccess_token() !=null){
                final User newUser = new User(username, password, token);
                setVisible(false);
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new BraimGui(newUser).setVisible(true);
                    }
                });
                
            }
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "There was an error with the connection");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
    }//GEN-LAST:event_bt_sign_inActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        client.launch_sign_up();

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewSessionGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_sign_in;
    private javax.swing.JPasswordField et_password;
    private javax.swing.JTextField et_username;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lbl_braim_brand;
    private javax.swing.JLabel lbl_password;
    private javax.swing.JLabel lbl_username;
    // End of variables declaration//GEN-END:variables
}
