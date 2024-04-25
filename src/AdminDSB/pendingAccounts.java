package AdminDSB;

import static AdminDSB.AdminDashboard.*;
import Config.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import net.proteanit.sql.DbUtils;

public class pendingAccounts extends javax.swing.JFrame {

    public pendingAccounts() {
        initComponents();
        displayData();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        declineBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        acceptBtn = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pendings = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(749, 572));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(749, 572));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        declineBtn.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        declineBtn.setText("DECLINE");
        declineBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                declineBtnActionPerformed(evt);
            }
        });
        jPanel1.add(declineBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 520, 120, -1));

        jButton2.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton2.setText("BACK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 520, 150, -1));

        acceptBtn.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        acceptBtn.setText("ACCEPT");
        acceptBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptBtnActionPerformed(evt);
            }
        });
        jPanel1.add(acceptBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 520, 120, 30));

        jLabel17.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel17.setText("PENDING ACCOUNTS");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, 210, 70));

        pendings.setFont(new java.awt.Font("Yu Gothic", 1, 11)); // NOI18N
        pendings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        pendings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pendingsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(pendings);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 620, 440));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pendingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingsMouseClicked
    }//GEN-LAST:event_pendingsMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new AdminDashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void acceptBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptBtnActionPerformed
        acceptAccount();
    }//GEN-LAST:event_acceptBtnActionPerformed

    private void declineBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineBtnActionPerformed
        declineAccount();
    }//GEN-LAST:event_declineBtnActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pendingAccounts().setVisible(true);
            }
        });
    }

    private void displayData() {
        try {
            ResultSet rs = new DBConnector().getData("select id,email,user,type from bill where status = 'PENDING'");
            pendings.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching data: " + e.getMessage());
        }
    }

    private void acceptAccount() {
        int rowIndex = pendings.getSelectedRow();
        if (rowIndex < 0) {
            errorMessage("PLEASE SELECT AN INDEX!");
        } else {
            try {
                TableModel tbl = pendings.getModel();
                new DBConnector().updateData("UPDATE bill SET status = 'ACTIVE' WHERE id = '" + tbl.getValueAt(rowIndex, 0).toString() + "'");
                successMessage("ACCOUNT APPROVED SUCCESSFULLY!!");
                displayData();
            } catch (SQLException er) {
                System.out.println("ERROR: " + er.getMessage());
            }
        }
    }

    private void declineAccount() {
        int rowIndex = pendings.getSelectedRow();
        if (rowIndex < 0) {
            errorMessage("PLEASE SELECT AN INDEX!");
        } else {
            try {
                TableModel tbl = pendings.getModel();
                new DBConnector().updateData("UPDATE bill SET status = 'DECLINED' WHERE id = '" + tbl.getValueAt(rowIndex, 0).toString() + "'");
                successMessage("ACCOUNT HAS BEEN DISAPPROVED!");
                displayData();
            } catch (SQLException er) {
                System.out.println("ERROR: " + er.getMessage());
            }
        }
    }

    private void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "ERROR!", JOptionPane.ERROR_MESSAGE);
    }

    private void successMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptBtn;
    private javax.swing.JButton declineBtn;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable pendings;
    // End of variables declaration//GEN-END:variables
}
