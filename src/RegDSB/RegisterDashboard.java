package RegDSB;

import AdminDSB.*;
import Config.*;
import LoginDSB.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.*;
import java.security.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.*;
import static javax.swing.JOptionPane.*;

public class RegisterDashboard extends javax.swing.JFrame {

    public File selectedFile;
    public String path2 = null;
    public String destination = "";
    public String oldPath;
    public String path;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern CONTACT_PATTERN = Pattern.compile("^[0-9]{11}$");

    public RegisterDashboard() {
        initComponents();
    }

    private void customizeComponents() {
        customizeButton(jButton1);
        customizeButton(jButton2);
        customizeButton(jButton3);
        customizeButton(jButton4);

        addActionListener(email, "Email");
        addActionListener(username, "Username");
        addActionListener(password, "Password");
        addActionListener(contact, "Contact#");
    }

    private void addActionListener(JTextField field, String placeholder) {
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                }
            }
        });
    }

    private void customizeButton(JButton button) {
        button.setOpaque(false);
        button.setBorder(new RoundedBorders(20));
        button.setForeground(Color.black);
        button.setFocusable(false);
    }

    public static int getHeightFromWidth(String imagePath, int desiredWidth) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();

            int newHeight = (int) ((double) desiredWidth / originalWidth * originalHeight);

            return newHeight;
        } catch (IOException ex) {
            System.out.println("No image found!");
        }

        return -1;
    }

    private ImageIcon ResizeImage(String ImagePath, byte[] pic, JLabel label) {
        ImageIcon MyImage = null;
        if (ImagePath != null) {
            MyImage = new ImageIcon(ImagePath);
        } else {
            MyImage = new ImageIcon(pic);
        }

        int newHeight = getHeightFromWidth(ImagePath, label.getWidth());

        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(label.getWidth(), newHeight, Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }

    private int FileExistenceChecker(String path) {
        File file = new File(path);
        String fileName = file.getName();

        Path filePath = Paths.get("src/ImageDB", fileName);
        boolean fileExists = Files.exists(filePath);

        if (fileExists) {
            return 1;
        } else {
            return 0;
        }

    }

    private String xemail, xusername;

    private boolean duplicateChecker() throws SQLException {
        ResultSet rs = new DBConnector().getData("select * from bill where email = '" + email.getText() + "' or user = '" + username.getText() + "'");

        if (rs.next()) {
            xemail = rs.getString("email");
            if (xemail.equals(email.getText())) {
                JOptionPane.showMessageDialog(this, "EMAIL HAS BEEN USED!", "OH NO!", ERROR_MESSAGE);
            }

            xusername = rs.getString("user");
            if (xusername.equals(username.getText())) {
                JOptionPane.showMessageDialog(this, "USERNAME HAS BEEN USED!", "OH NO!", ERROR_MESSAGE);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean validationChecker() {
        if (username.getText().isEmpty() || password.getText().isEmpty()
                || email.getText().isEmpty() || contact.getText().isEmpty()) {
            errorMessage("FILL ALL THE REQUIREMENTS!");
            return false;
        } else if (password.getText().length() < 8) {
            errorMessage("PASSWORD MUST BE AT LEAST 8 CHARACTERS!");
            return false;
        } else if (!contact.getText().matches("\\d+")) {
            errorMessage("CONTACT MUST CONTAIN ONLY DIGITS!");
            return false;
        } else {
            return true;
        }
    }

    private void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "ERROR!", JOptionPane.ERROR_MESSAGE);
    }

    private void successMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "SUCCESS!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void createAccount() throws NoSuchAlgorithmException {
        String emailText = email.getText().trim();
        String contactText = contact.getText().trim();
        String usernameText = username.getText().trim();
        String passwordText = password.getText().trim();
        String typeText = (String) type.getSelectedItem();

        if (emailText.isEmpty() || !EMAIL_PATTERN.matcher(emailText).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email address!");
            return;
        }

        if (contactText.isEmpty() || !CONTACT_PATTERN.matcher(contactText).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid contact number! Must be 11 digits.");
            return;
        }

        if (usernameText.isEmpty() || usernameText.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters long!");
            return;
        }

        if (passwordText.isEmpty() || passwordText.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long!");
            return;
        }

        if (destination == null || selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Image file is required!");
            return;
        }

        if (usernameText.isEmpty() || passwordText.isEmpty() || emailText.isEmpty() || contactText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "FILL ALL THE REQUIREMENTS!");
            return;
        }

        if (passwordText.length() < 8) {
            JOptionPane.showMessageDialog(this, "PASSWORD MUST BE AT LEAST 8 CHARACTERS!");
            return;
        }

        if (!contactText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "CONTACT MUST CONTAIN ONLY DIGITS!");
            return;
        }

        try {
            ResultSet rs = new DBConnector().getData("select * from bill where email = '" + emailText + "' or username = '" + usernameText + "'");
            if (rs.next()) {
                String xemail = rs.getString("email");
                if (xemail.equals(emailText)) {
                    JOptionPane.showMessageDialog(this, "EMAIL HAS BEEN USED!", "OH NO!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String xusername = rs.getString("username");
                if (xusername.equals(usernameText)) {
                    JOptionPane.showMessageDialog(this, "USERNAME HAS BEEN USED!", "OH NO!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error checking for duplicates!");
            System.out.println(ex.getMessage());
            return;
        }

        try {
            String pass = passwordHashing.hashPassword(password.getText());

            DBConnector cn = new DBConnector();
            cn.insertData("insert into bill (email,contact,username,password,type,status,image) "
                    + "values ('" + emailText + "', '" + contactText + "', "
                    + "'" + usernameText + "', '" + pass + "', '" + typeText + "', 'PENDING', '" + destination + "')");

            Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);

            JOptionPane.showMessageDialog(this, "ACCOUNT CREATED SUCCESSFULLY!");

            LoginDashboard ld = new LoginDashboard();
            ld.setVisible(true);
            this.dispose();

        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error creating account!");
            System.out.println(ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        type = new javax.swing.JComboBox<>();
        showPass = new javax.swing.JCheckBox();
        contact = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        username = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        icon1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(742, 541));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(742, 541));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(394, 541));
        jPanel1.setPreferredSize(new java.awt.Dimension(394, 541));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton1.setText("BACK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 462, 110, -1));

        type.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "USER" }));
        type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeActionPerformed(evt);
            }
        });
        jPanel1.add(type, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 270, -1));

        showPass.setBackground(new java.awt.Color(255, 255, 255));
        showPass.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        showPass.setText("SHOW PASSWORD");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });
        jPanel1.add(showPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 280, -1, -1));

        contact.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        contact.setForeground(new java.awt.Color(102, 102, 102));
        contact.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contact.setText("CONTACT# ");
        contact.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contactFocusGained(evt);
            }
        });
        contact.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contactMouseClicked(evt);
            }
        });
        jPanel1.add(contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 270, 30));

        password.setForeground(new java.awt.Color(102, 102, 102));
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setText("PASSWORD");
        password.setMinimumSize(new java.awt.Dimension(43, 25));
        password.setPreferredSize(new java.awt.Dimension(43, 25));
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
        });
        jPanel1.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 270, 30));

        username.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        username.setForeground(new java.awt.Color(102, 102, 102));
        username.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        username.setText("USERNAME");
        username.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usernameMouseClicked(evt);
            }
        });
        jPanel1.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 270, 30));

        email.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        email.setForeground(new java.awt.Color(102, 102, 102));
        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email.setText("EMAIL ");
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
        jPanel1.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 270, 30));

        jButton2.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        jButton2.setText("REGISTER");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 462, 110, -1));

        jLabel1.setFont(new java.awt.Font("Yu Gothic", 1, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("REGISTRATION FORM");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 270, 40));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(icon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 320, 350));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, 360, 390));

        jButton3.setText("SELECT");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 460, 100, 30));

        jButton4.setText("REMOVE");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 460, 100, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, 750, 540));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            createAccount();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RegisterDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void emailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailMouseClicked
        email.setText("");
    }//GEN-LAST:event_emailMouseClicked

    private void usernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usernameMouseClicked
        username.setText("");
    }//GEN-LAST:event_usernameMouseClicked

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        password.setText("");
    }//GEN-LAST:event_passwordFocusGained

    private void contactMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactMouseClicked
        contact.setText("");
    }//GEN-LAST:event_contactMouseClicked

    private void contactFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactFocusGained

    }//GEN-LAST:event_contactFocusGained

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
        char echoChar = showPass.isSelected() ? (char) 0 : '*';
        password.setEchoChar(echoChar);
    }//GEN-LAST:event_showPassActionPerformed

    private void typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeActionPerformed

    }//GEN-LAST:event_typeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new LoginDashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                selectedFile = fileChooser.getSelectedFile();
                destination = "src/ImageDB/" + selectedFile.getName();
                path = selectedFile.getAbsolutePath();

                if (FileExistenceChecker(path) == 1) {
                    JOptionPane.showMessageDialog(null, "File Already Exist, Rename or Choose another!");
                    destination = "";
                    path = "";
                } else {
                    icon1.setIcon(ResizeImage(path, null, icon1));
                    jButton4.setEnabled(true);
                    jButton3.setEnabled(false);
                }
            } catch (Exception ex) {
                System.out.println("File Error!");
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        icon1.setIcon(null);
        path = "";
        destination = "";
        jButton4.setEnabled(false);
        jButton3.setEnabled(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegisterDashboard().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField contact;
    private javax.swing.JTextField email;
    private javax.swing.JLabel icon1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField password;
    private javax.swing.JCheckBox showPass;
    private javax.swing.JComboBox<String> type;
    public javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
