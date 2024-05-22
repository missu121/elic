package LoginDSB;

import AdminDSB.*;
import Config.*;
import RegDSB.*;
import UserDSB.*;
import java.awt.Color;
import java.security.*;
import java.security.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class LoginDashboard extends javax.swing.JFrame {

    public LoginDashboard() {
        initComponents();
        showPass.setOpaque(false);
    }

//    FOR ID RESET 
//    SET @num := 1000 or 0;
//
//    UPDATE accounts_table SET account_id = @num := (@num);
//
//    ALTER TABLE accounts_table AUTO_INCREMENT = 1000 or 1;
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        a = new javax.swing.JButton();
        showPass = new javax.swing.JCheckBox();
        password = new javax.swing.JPasswordField();
        b = new javax.swing.JButton();
        c = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(889, 529));
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(635, 423));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ELECTRIC BILLING SYSTEM");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 360, 160));

        email.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        email.setForeground(new java.awt.Color(102, 102, 102));
        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email.setText("EMAIL");
        email.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                emailFocusGained(evt);
            }
        });
        email.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emailMouseClicked(evt);
            }
        });
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        jPanel1.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 410, 29));

        a.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        a.setText("EXIT");
        a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aActionPerformed(evt);
            }
        });
        jPanel1.add(a, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 350, 200, -1));

        showPass.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        showPass.setText("SHOW PASSWORD");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });
        jPanel1.add(showPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 280, -1, -1));

        password.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setText("PASSWORD ");
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
        });
        jPanel1.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 230, 410, 30));

        b.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        b.setText("LOGIN");
        b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bActionPerformed(evt);
            }
        });
        jPanel1.add(b, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 350, 190, -1));

        c.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        c.setText("REGISTER");
        c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cActionPerformed(evt);
            }
        });
        jPanel1.add(c, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 390, 420, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/ggggggggggg.jpg"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, -1, 550));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 889, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aActionPerformed
        System.exit(0);
    }//GEN-LAST:event_aActionPerformed

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        password.setText("");
    }//GEN-LAST:event_passwordFocusGained

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
        password.setEchoChar(showPass.isSelected() ? (char) 0 : '*');
    }//GEN-LAST:event_showPassActionPerformed

    private void emailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailMouseClicked
        email.setText("");
    }//GEN-LAST:event_emailMouseClicked

    private void emailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailFocusGained
    }//GEN-LAST:event_emailFocusGained

    private void bActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bActionPerformed
        try {
            String hashedPass = passwordHashing.hashPassword(password.getText());
            if (loginDB(email.getText(), hashedPass)) {

                if (xstatus.equalsIgnoreCase("pending")) {
                    errorMessage("WAIT FOR ADMIN APPROVAL!");
                } else if (xstatus.equalsIgnoreCase("declined")) {
                    errorMessage("YOUR ACCOUNT HAS BEEN DECLINED!");
                } else if (xstatus.equalsIgnoreCase("inactive")) {
                    errorMessage("YOUR ACCOUNT IS IN-ACTIVE!");
                } else if (!xstatus.equalsIgnoreCase("active")) {
                    errorMessage("INVALID TYPE!");
                } else {
                    if (xtype.equalsIgnoreCase("customer")) {
                        successMessage("LOGIN SUCCESSFULLY!");
                        new UserDashboard().setVisible(true);
                        dispose();
                    } else if (xtype.equalsIgnoreCase("admin")) {
                        successMessage("LOGIN SUCCESSFULLY!");
                        new AdminDashboard().setVisible(true);
                        dispose();
                    } else {
                        errorMessage("ACCOUNT TYPE INVALID!");
                    }
                }
            } else {
                errorMessage("ACCOUNT NOT FOUND!");
            }
        } catch (SQLException | NoSuchAlgorithmException er) {
            System.out.println("ERROR: " + er.getMessage());
        }
    }//GEN-LAST:event_bActionPerformed

    private void cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cActionPerformed
        new RegisterDashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_cActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginDashboard().setVisible(true);
            }
        });
    }

    private static String xstatus, xtype;

    private boolean loginDB(String email, String pass) throws SQLException {
        ResultSet rs = new DBConnector().getData("select * from bill where email = '" + email + "' and password = '" + pass + "'");
        if (rs.next()) {
            xstatus = rs.getString("status");
            xtype = rs.getString("type");
            Container cons = Container.getInstance();
            cons.setId(rs.getString("id"));
            cons.setEmail(rs.getString("email"));
            cons.setUsername(rs.getString("username"));
            cons.setPassword(rs.getString("password"));
            cons.setContact(rs.getString("contact"));
            cons.setType(rs.getString("type"));
            cons.setStatus(rs.getString("status"));
            return true;
        } else {
            return false;
        }
    }

    private void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "ERROR!", JOptionPane.ERROR_MESSAGE);
    }

    private void successMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton a;
    private javax.swing.JButton b;
    private javax.swing.JButton c;
    public javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField password;
    private javax.swing.JCheckBox showPass;
    // End of variables declaration//GEN-END:variables
}
