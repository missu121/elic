package UserDSB;

import AdminDSB.AdminDashboard;
import static AdminDSB.AdminDashboard.data;
import Config.DBConnector;
import LoginDSB.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.*;

public class UserDashboard extends javax.swing.JFrame {

    DefaultTableModel adminTable;

    public UserDashboard() {
        initComponents();
        displayData();
        retrieveData();
    }

    public UserDashboard(String usersName) {
        initComponents();
        adminName.setText(usersName);
        displayData();
        retrieveData();
    }

    private void displayData() {
        try {
            ResultSet rs = new DBConnector().getData("SELECT * FROM accounts_table WHERE "
                    + "account_status IN ('ACTIVE', 'INACTIVE')");

            DefaultTableModel tblModel = (DefaultTableModel) userTB.getModel();
            tblModel.setRowCount(0);

            while (rs.next()) {
                String[] rowData = {
                    String.valueOf(rs.getInt("account_id")),
                    rs.getString("account_email"),
                    rs.getString("account_contact"),
                    rs.getString("account_password"),
                    rs.getString("account_type"),
                    rs.getString("account_status"),
                    rs.getString("account_username"),
                    rs.getString("account_gender"),};
                tblModel.addRow(rowData);
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching data: " + e.getMessage());
        }
    }

    private void retrieveData() {
        DefaultTableModel tableModel = (DefaultTableModel) userTB.getModel();

        if (tableModel.getRowCount() > 0) {
            int selectedModelIndex = 0;

            String[] rowData = {
                tableModel.getValueAt(selectedModelIndex, 0).toString(),
                tableModel.getValueAt(selectedModelIndex, 1).toString(),
                tableModel.getValueAt(selectedModelIndex, 2).toString(),
                tableModel.getValueAt(selectedModelIndex, 3).toString(),
                tableModel.getValueAt(selectedModelIndex, 4).toString(),
                tableModel.getValueAt(selectedModelIndex, 5).toString(),
                tableModel.getValueAt(selectedModelIndex, 6).toString(),
                tableModel.getValueAt(selectedModelIndex, 7).toString(),};

            id.setText(rowData[0]);
            email.setText(rowData[1]);
            password.setText(rowData[2]);
            username.setText(rowData[3]);
            contact.setText(rowData[4]);
            gender.setSelectedItem(rowData[5]);
            type.setSelectedItem(rowData[6]);
            status.setSelectedItem(rowData[7]);
        }
    }

    private void updateAccountData() {
        try (Connection connection = new DBConnector().getConnection()) {
            DefaultTableModel tblModel = (DefaultTableModel) userTB.getModel();

            String xid = id.getText(), xemail = email.getText(), xusername = username.getText(),
                    xcontact = contact.getText(), xgender = (String) gender.getSelectedItem(),
                    xpassword = password.getText(), xtype = (String) type.getSelectedItem(),
                    xstatus = (String) status.getSelectedItem();

            if (xemail.equals(tblModel.getValueAt(userTB.getSelectedRow(), 1))
                    && xusername.equals(tblModel.getValueAt(userTB.getSelectedRow(), 2))
                    && xcontact.equals(tblModel.getValueAt(userTB.getSelectedRow(), 3))
                    && xpassword.equals(tblModel.getValueAt(userTB.getSelectedRow(), 4))
                    && xgender.equals(tblModel.getValueAt(userTB.getSelectedRow(), 5))
                    && xtype.equals(tblModel.getValueAt(userTB.getSelectedRow(), 6))
                    && xstatus.equals(tblModel.getValueAt(userTB.getSelectedRow(), 7))) {
                JOptionPane.showMessageDialog(null, "PLEASE MAKE CHANGES BEFORE UPDATING!", "NO CHANGES MADE!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            PreparedStatement update = connection.prepareStatement("UPDATE accounts_table SET "
                    + "account_email = ?, account_username = ?, account_contact = ?, account_password = ?,"
                    + "account_gender = ?, account_type = ?, account_status = ? WHERE account_id = ?");

            update.setString(1, xemail);
            update.setString(2, xusername);
            update.setString(3, xcontact);
            update.setString(4, xpassword);
            update.setString(5, xgender);
            update.setString(6, xtype);
            update.setString(7, xstatus);
            update.setString(8, xid);

            int rowsAffected = update.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "ACCOUNT SUCCESSFULLY UPDATED!", "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
                int option = JOptionPane.showOptionDialog(null, "SOME CHANGES MAY APPLY AFTER RESTARTING YOUR CLIENT!", "WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null,
                        new String[]{"RESTART NOW", "RESTART LATER"}, "RESTART NOW");
                if (option == JOptionPane.YES_OPTION) {
                    new LoginDashboard().setVisible(true);
                    dispose();
                }
                ((DefaultTableModel) userTB.getModel()).setRowCount(0);
                displayData();
            } else {
                JOptionPane.showMessageDialog(null, "FAILED TO UPDATE!", "OH NO!", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        adminName = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTB = new javax.swing.JTable();
        id = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        contact = new javax.swing.JTextField();
        password = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        type = new javax.swing.JComboBox<>();
        gender = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        status = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        adminName.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        adminName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/user.png"))); // NOI18N
        adminName.setText("USERS NAME");
        jPanel1.add(adminName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel4.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel4.setText("USERS DASHBOARD");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 100, -1, 20));

        jLabel2.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel2.setText("HOSPITAL MANAGEMENT SYSTEMv.3");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, -1, -1));

        userTB.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "EMAIL", "PASSWORD", "USERNAME", "CONTACT", "GENDER", "TYPE", "STATUS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(userTB);
        if (userTB.getColumnModel().getColumnCount() > 0) {
            userTB.getColumnModel().getColumn(0).setResizable(false);
            userTB.getColumnModel().getColumn(1).setResizable(false);
            userTB.getColumnModel().getColumn(2).setResizable(false);
            userTB.getColumnModel().getColumn(3).setResizable(false);
            userTB.getColumnModel().getColumn(4).setResizable(false);
            userTB.getColumnModel().getColumn(5).setResizable(false);
            userTB.getColumnModel().getColumn(6).setResizable(false);
            userTB.getColumnModel().getColumn(7).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 670, 480));

        id.setEditable(false);
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setText("ID");
        id.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jPanel1.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 230, 60, 30));

        email.setForeground(new java.awt.Color(102, 102, 102));
        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email.setText("EMAIL");
        jPanel1.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 280, 170, 30));

        contact.setForeground(new java.awt.Color(102, 102, 102));
        contact.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contact.setText("CONTACT");
        jPanel1.add(contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 280, 170, 30));

        password.setForeground(new java.awt.Color(102, 102, 102));
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setText("PASSWORD");
        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });
        jPanel1.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 330, 170, 30));

        username.setForeground(new java.awt.Color(102, 102, 102));
        username.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        username.setText("USERNAME");
        jPanel1.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 330, 170, 30));

        jLabel15.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("TYPE");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 390, -1, -1));

        type.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "USER" }));
        type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeActionPerformed(evt);
            }
        });
        jPanel1.add(type, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 420, 110, 30));

        gender.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        gender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MALE", "FEMALE", "OTHERS" }));
        gender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genderActionPerformed(evt);
            }
        });
        jPanel1.add(gender, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 420, 110, 30));

        jLabel13.setFont(new java.awt.Font("Yu Gothic", 1, 19)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("GENDER");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 390, -1, -1));

        jLabel14.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("STATUS");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 390, -1, -1));

        status.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVE", "INACTIVE" }));
        status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusActionPerformed(evt);
            }
        });
        jPanel1.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 420, 110, 30));

        jButton6.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton6.setText("CLEAR");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 580, 110, -1));

        jButton3.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton3.setText("LOGOUT");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 580, 110, -1));

        jButton5.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton5.setText("UPDATE");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 580, 110, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 660));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeActionPerformed

    }//GEN-LAST:event_typeActionPerformed

    private void genderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genderActionPerformed

    }//GEN-LAST:event_genderActionPerformed

    private void statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusActionPerformed

    }//GEN-LAST:event_statusActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        new LoginDashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        updateAccountData();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adminName;
    private javax.swing.JTextField contact;
    private javax.swing.JTextField email;
    private javax.swing.JComboBox<String> gender;
    private javax.swing.JTextField id;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField password;
    private javax.swing.JComboBox<String> status;
    private javax.swing.JComboBox<String> type;
    public javax.swing.JTable userTB;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
