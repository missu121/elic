package UserDSB;

import AdminDSB.AdminDashboard;
import static AdminDSB.AdminDashboard.data;
import AdminDSB.RoundedBorder;
import Config.Container;
import Config.DBConnector;
import Config.passwordHashing;
import LoginDSB.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import javax.swing.SwingUtilities;
import javax.swing.table.*;
import net.proteanit.sql.DbUtils;

public class UserDashboard extends javax.swing.JFrame {

    public File selectedFile;
    public String path2 = null;
    public String destination = "";
    public String oldPath;
    public String path;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern CONTACT_PATTERN = Pattern.compile("^[0-9]{11}$");

    public UserDashboard() {
        initComponents();
        customizeComponents();
        try {
            Container cons = Container.getInstance();
            String query = "SELECT * FROM bill WHERE id = ?";
            try (PreparedStatement pstmt = new DBConnector().getConnection().prepareStatement(query)) {
                pstmt.setString(1, cons.getId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        id.setText(rs.getString("id"));
                        username.setText(rs.getString("username"));
                        contact.setText(rs.getString("contact"));
                        email.setText(rs.getString("email"));
                        icon.setIcon(ResizeImage(rs.getString("image"), null, icon));
                        oldPath = rs.getString("image");
                        path = rs.getString("image");
                        destination = rs.getString("image");
                        jTabbedPane1.setSelectedIndex(4);
                        System.out.println("Selected File: " + selectedFile);
                        System.out.println("Icon: " + icon.getIcon());
                        if (rs.getString("image") != null) {
                            select.setEnabled(false);
                            remove.setEnabled(true);
                        } else {
                            select.setEnabled(true);
                            remove.setEnabled(false);
                        }

                    } else {
                        System.out.println("No data found for id: " + cons.getId());
                    }
                }
            }
        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected ERROR: " + e.getMessage());
        }
    }

    private boolean validationChecker() {
        if (newPassword.getText().isEmpty() || oldPassword.getText().isEmpty() || cpassword.getText().isEmpty()) {
            errorMessage("FILL ALL THE REQUIREMENTS!");
            return false;
        } else if (newPassword.getText().length() < 8) {
            errorMessage("PASSWORD MUST ATLEAST 8 CHARACTERS!");
            return false;
        } else if (!newPassword.getText().equals(cpassword.getText())) {
            errorMessage("PASSWORD DO NOT MATCH!");
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

    private void customizeComponents() {
        customizeButton(select);
        customizeButton(remove);
        customizeButton(print13);
        customizeButton(print9);
        customizeButton(print14);
        customizeButton(print8);
        customizeButton(print22);
        customizeButton(print21);
        customizeButton(print24);
        customizeButton(print23);
    }

    private void customizeButton(JButton button) {
        button.setOpaque(false);
        button.setBorder(new RoundedBorderss(20));
        button.setForeground(Color.black);
        button.setFocusable(false);
    }

    public void myData() throws SQLException, IOException {
        if (updateChecker()) {
        } else if (!validationCheckerForMyData()) {
        } else {
            new DBConnector().updateData("update bill set email = '" + email.getText() + "',username = '" + username.getText() + "', "
                    + "contact = '" + contact.getText() + "', image = '" + destination + "' where id = '" + id.getText() + "'");

            if (selectedFile != null && path != null) {
                Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            successMessage("ACCOUNT SUCCESSFULLY UPDATED!");
            LoginDashboard ad = new LoginDashboard();
            ad.setVisible(true);
            dispose();
        }
    }

    private boolean validationCheckerForMyData() {

        if (email.getText().isEmpty() || !EMAIL_PATTERN.matcher(email.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email address!");
            return false;
        } else if (contact.getText().isEmpty() || !CONTACT_PATTERN.matcher(contact.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid contact number! Must be 11 digits.");
            return false;
        } else if (username.getText().isEmpty() || email.getText().isEmpty() || contact.getText().isEmpty()) {
            errorMessage("FILL ALL THE REQUIREMENTS!");
            return false;
        } else if (!contact.getText().matches("\\d+")) {
            errorMessage("CONTACT MUST CONTAIN ONLY DIGITS!");
            return false;
        } else if (selectedFile == null && icon.getIcon() == null) {
            errorMessage("PLEASE INSERT AN IMAGE FIRST!");
            return false;
        } else {
            return true;
        }
    }

    private String xemail, xusername;

    private boolean updateChecker() throws SQLException {
        ResultSet rs = new DBConnector().getData("select * from bill where (username = '" + username.getText() + "' or email = '" + email.getText() + "') and id != '" + id.getText() + "'");
        if (rs.next()) {
            xemail = rs.getString("email");
            if (xemail.equalsIgnoreCase(email.getText())) {
                errorMessage("EMAIL HAS BEEN USED!");
            }
            xusername = rs.getString("username");
            if (xusername.equalsIgnoreCase(username.getText())) {
                errorMessage("USERNAME HAS BEEN USERD!");
            }
            return true;
        } else {
            return false;
        }
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        icon = new javax.swing.JLabel();
        select = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        id = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        contact = new javax.swing.JTextField();
        print9 = new javax.swing.JButton();
        print8 = new javax.swing.JButton();
        email = new javax.swing.JTextField();
        print13 = new javax.swing.JButton();
        print14 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        payment = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        print21 = new javax.swing.JButton();
        meterId = new javax.swing.JTextField();
        reference = new javax.swing.JTextField();
        unit = new javax.swing.JTextField();
        tax = new javax.swing.JTextField();
        totalBill = new javax.swing.JTextField();
        print22 = new javax.swing.JButton();
        month = new javax.swing.JTextField();
        status = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        print23 = new javax.swing.JButton();
        print24 = new javax.swing.JButton();
        showPass = new javax.swing.JCheckBox();
        cpassword = new javax.swing.JPasswordField();
        newPassword = new javax.swing.JPasswordField();
        oldPassword = new javax.swing.JPasswordField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -20, 1340, 80));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 480, 350));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 520, 390));

        select.setBackground(new java.awt.Color(20, 161, 242));
        select.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        select.setForeground(new java.awt.Color(255, 255, 255));
        select.setText("SELECT");
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });
        jPanel2.add(select, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 540, 520, 30));

        remove.setBackground(new java.awt.Color(20, 161, 242));
        remove.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        remove.setForeground(new java.awt.Color(255, 255, 255));
        remove.setText("REMOVE");
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });
        jPanel2.add(remove, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 580, 520, 30));

        id.setEditable(false);
        id.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setText("ID");
        id.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                idMouseClicked(evt);
            }
        });
        jPanel2.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 520, 30));

        username.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        username.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        username.setText("USERNAME");
        username.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameFocusGained(evt);
            }
        });
        username.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usernameMouseClicked(evt);
            }
        });
        jPanel2.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 230, 600, 30));

        contact.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        contact.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contact.setText("CONTACT#\n");
        contact.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contactFocusGained(evt);
            }
        });
        jPanel2.add(contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 280, 290, 30));

        print9.setBackground(new java.awt.Color(20, 161, 242));
        print9.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print9.setForeground(new java.awt.Color(255, 255, 255));
        print9.setText("UPDATE");
        print9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print9ActionPerformed(evt);
            }
        });
        jPanel2.add(print9, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 370, 290, 30));

        print8.setBackground(new java.awt.Color(20, 161, 242));
        print8.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print8.setForeground(new java.awt.Color(255, 255, 255));
        print8.setText("CHANGE PASSWORD");
        print8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print8ActionPerformed(evt);
            }
        });
        jPanel2.add(print8, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 410, 290, 30));

        email.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email.setText("EMAIL");
        email.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emailMouseClicked(evt);
            }
        });
        jPanel2.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 280, 290, 30));

        print13.setBackground(new java.awt.Color(20, 161, 242));
        print13.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print13.setForeground(new java.awt.Color(255, 255, 255));
        print13.setText("PAY BILL");
        print13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print13ActionPerformed(evt);
            }
        });
        jPanel2.add(print13, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 370, 290, 30));

        print14.setBackground(new java.awt.Color(20, 161, 242));
        print14.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print14.setForeground(new java.awt.Color(255, 255, 255));
        print14.setText("LOGOUT");
        print14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print14ActionPerformed(evt);
            }
        });
        jPanel2.add(print14, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 410, 290, 30));

        jTabbedPane1.addTab("tab1", jPanel2);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        payment.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        payment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GCASH", "UNION BANK", "PAYMAYA", "LOAD WALLET" }));
        payment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentActionPerformed(evt);
            }
        });
        jPanel10.add(payment, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 280, 530, 30));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("MANAGE BILLING STATEMENT");
        jPanel10.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 1260, 60));

        print21.setBackground(new java.awt.Color(20, 161, 242));
        print21.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print21.setForeground(new java.awt.Color(255, 255, 255));
        print21.setText("BACK");
        print21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print21ActionPerformed(evt);
            }
        });
        jPanel10.add(print21, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 610, 530, 30));

        meterId.setEditable(false);
        meterId.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        meterId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        meterId.setText("METER NUMBER / ID NUMBER");
        meterId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                meterIdFocusGained(evt);
            }
        });
        meterId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                meterIdMouseClicked(evt);
            }
        });
        jPanel10.add(meterId, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 130, 530, 30));

        reference.setEditable(false);
        reference.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        reference.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        reference.setText("REFERENCE CODE");
        reference.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                referenceFocusGained(evt);
            }
        });
        reference.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                referenceMouseClicked(evt);
            }
        });
        jPanel10.add(reference, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 390, 530, 30));

        unit.setEditable(false);
        unit.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        unit.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        unit.setText("UNIT = ₱100 PER UNIT");
        unit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                unitFocusGained(evt);
            }
        });
        unit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                unitMouseClicked(evt);
            }
        });
        jPanel10.add(unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 180, 240, 30));

        tax.setEditable(false);
        tax.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        tax.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tax.setText("VAT (VALUE ADDED TAX)  RATE  = 20%");
        tax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                taxFocusGained(evt);
            }
        });
        tax.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                taxMouseClicked(evt);
            }
        });
        jPanel10.add(tax, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 230, 530, 30));

        totalBill.setEditable(false);
        totalBill.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        totalBill.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        totalBill.setText("TOTAL BILL");
        totalBill.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                totalBillFocusGained(evt);
            }
        });
        totalBill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                totalBillMouseClicked(evt);
            }
        });
        jPanel10.add(totalBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 350, 530, 30));

        print22.setBackground(new java.awt.Color(20, 161, 242));
        print22.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print22.setForeground(new java.awt.Color(255, 255, 255));
        print22.setText("PAY");
        print22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print22ActionPerformed(evt);
            }
        });
        jPanel10.add(print22, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 570, 530, 30));

        month.setEditable(false);
        month.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        month.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        month.setText("MONTH");
        month.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                monthFocusGained(evt);
            }
        });
        month.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                monthMouseClicked(evt);
            }
        });
        jPanel10.add(month, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 180, 240, 30));

        status.setEditable(false);
        status.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        status.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        status.setText("STATUS");
        status.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                statusFocusGained(evt);
            }
        });
        status.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                statusMouseClicked(evt);
            }
        });
        jPanel10.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 530, 530, 30));

        jTabbedPane1.addTab("tab2", jPanel10);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        print23.setBackground(new java.awt.Color(20, 161, 242));
        print23.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print23.setForeground(new java.awt.Color(255, 255, 255));
        print23.setText("BACK");
        print23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print23ActionPerformed(evt);
            }
        });
        jPanel11.add(print23, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 610, 530, 30));

        print24.setBackground(new java.awt.Color(20, 161, 242));
        print24.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print24.setForeground(new java.awt.Color(255, 255, 255));
        print24.setText("CHANGE PASSWORD");
        print24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print24ActionPerformed(evt);
            }
        });
        jPanel11.add(print24, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 570, 530, 30));

        showPass.setBackground(new java.awt.Color(255, 255, 255));
        showPass.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        showPass.setText("SHOW PASSWORD");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });
        jPanel11.add(showPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 410, -1, -1));

        cpassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cpassword.setText("CONFIRM PASS");
        jPanel11.add(cpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 370, 460, 30));

        newPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        newPassword.setText("NEW PASSWORD");
        jPanel11.add(newPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 280, 460, 30));

        oldPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        oldPassword.setText("OLD PASSWORD");
        jPanel11.add(oldPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 160, 460, 30));

        jLabel18.setFont(new java.awt.Font("Yu Gothic", 0, 12)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("CONFIRM PASSWORD");
        jPanel11.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 330, 460, 40));

        jLabel19.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("CHANGE PASS");
        jPanel11.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 40, 460, 40));

        jLabel20.setFont(new java.awt.Font("Yu Gothic", 0, 12)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("OLD PASSWORD");
        jPanel11.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 460, 40));

        jLabel21.setFont(new java.awt.Font("Yu Gothic", 0, 12)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("NEW PASSWORD");
        jPanel11.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 240, 460, 40));

        jTabbedPane1.addTab("tab2", jPanel11);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 0, 1370, 760));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1340, 750));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
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
                    icon.setIcon(ResizeImage(path, null, icon));
                    select.setEnabled(false);
                    remove.setEnabled(true);
                }
            } catch (Exception ex) {
                System.out.println("File Error!");
            }
        }
    }//GEN-LAST:event_selectActionPerformed

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        destination = "";
        icon.setIcon(null);
        path = "";
        select.setEnabled(true);
        remove.setEnabled(false);
    }//GEN-LAST:event_removeActionPerformed

    private void idMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_idMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_idMouseClicked

    private void usernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameFocusGained

    private void usernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usernameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameMouseClicked

    private void contactFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_contactFocusGained

    private void print9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print9ActionPerformed
        try {
            myData();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(UserDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_print9ActionPerformed

    private void print8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print8ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_print8ActionPerformed

    private void emailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_emailMouseClicked

    private void print13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print13ActionPerformed
        try {
            Container cons = Container.getInstance();
            String query = "SELECT * FROM transaction WHERE u_id = ?";
            try (PreparedStatement pstmt = new DBConnector().getConnection().prepareStatement(query)) {
                pstmt.setString(1, cons.getId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        meterId.setText(rs.getString("u_id"));
                        unit.setText(rs.getString("t_unit"));
                        month.setText(rs.getString("t_month"));
                        tax.setText(rs.getString("t_tax"));
                        totalBill.setText(rs.getString("t_total"));
                        reference.setText(rs.getString("t_reference"));
                        status.setText(rs.getString("t_status"));

                        SwingUtilities.invokeLater(() -> {
                            jTabbedPane1.setSelectedIndex(1);
                        });

                    } else {
                        jTabbedPane1.setSelectedIndex(1);
                        meterId.setText("NO DATA");
                        unit.setText("NO DATA");
                        tax.setText("NO DATA");
                        totalBill.setText("NO DATA");
                        reference.setText("NO DATA");
                        status.setText("NO DATA");
                        print22.setEnabled(false);
                    }
                }
            }
        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected ERROR: " + e.getMessage());
        }
    }//GEN-LAST:event_print13ActionPerformed

    private void print14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print14ActionPerformed
        LoginDashboard ld = new LoginDashboard();
        ld.setVisible(true);
        dispose();
    }//GEN-LAST:event_print14ActionPerformed

    private void paymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentActionPerformed

    private void print21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print21ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_print21ActionPerformed

    private void meterIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_meterIdFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_meterIdFocusGained

    private void meterIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_meterIdMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_meterIdMouseClicked

    private void referenceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_referenceFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_referenceFocusGained

    private void referenceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_referenceMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_referenceMouseClicked

    private void unitFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_unitFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_unitFocusGained

    private void unitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_unitMouseClicked
        unit.setText("");
    }//GEN-LAST:event_unitMouseClicked

    private void taxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_taxFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_taxFocusGained

    private void taxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taxMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_taxMouseClicked

    private void totalBillFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_totalBillFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_totalBillFocusGained

    private void totalBillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_totalBillMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_totalBillMouseClicked

    private void print22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print22ActionPerformed
        if (status.getText().equals("PAID")) {
            JOptionPane.showMessageDialog(null, "YOU ALREADY PAID THIS MONTH!");
        } else {
            try {
                new DBConnector().insertData("update transaction set t_month = '" + month.getText() + "', t_tax = '" + tax.getText() + "', "
                        + "t_unit = '" + unit.getText() + "', t_total = '" + totalBill.getText() + "', t_reference = '" + reference.getText() + "', "
                        + "t_payment = '" + payment.getSelectedItem() + "', t_status = 'PAID' where u_id = '" + meterId.getText() + "'");

                JOptionPane.showMessageDialog(this, "PAYMENT HAS BEEN PROCESSED!", "SUCCESS", INFORMATION_MESSAGE);

                new UserDashboard().setVisible(true);
                dispose();
            } catch (SQLException er) {
                System.out.println("Eror: " + er.getMessage());
            }
        }
    }//GEN-LAST:event_print22ActionPerformed

    private void monthFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_monthFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_monthFocusGained

    private void monthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_monthMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_monthMouseClicked

    private void statusFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_statusFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_statusFocusGained

    private void statusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statusMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_statusMouseClicked

    private void print23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print23ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_print23ActionPerformed

    private void print24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print24ActionPerformed
        try {
            if (!validationChecker()) {
            } else {
                Container sess = Container.getInstance();
                ResultSet rs = new DBConnector().getData("select * from bill where id = '" + sess.getId() + "'");
                if (rs.next()) {
                    String oldPass = rs.getString("password");
                    String oldHash = passwordHashing.hashPassword(oldPassword.getText());

                    if (oldPass.equals(oldHash)) {
                        String newPass = passwordHashing.hashPassword(newPassword.getText());
                        new DBConnector().updateData("update bill set password = '" + newPass + "' where id = '" + sess.getId() + "'");
                        successMessage("ACCOUNT SUCCESSFULLY UPDATED!");
                        new LoginDashboard().setVisible(true);
                        dispose();
                    } else {
                        errorMessage("OLD PASSWORD IS INCORRECT!");
                    }
                } else {
                    errorMessage("NO ACCOUNT FOUND!");
                }
            }
        } catch (SQLException | NoSuchAlgorithmException er) {
            System.out.println("Error: " + er.getMessage());
        }
    }//GEN-LAST:event_print24ActionPerformed

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
        char echoChar = showPass.isSelected() ? (char) 0 : '*';
        oldPassword.setEchoChar(echoChar);
        newPassword.setEchoChar(echoChar);
        cpassword.setEchoChar(echoChar);
    }//GEN-LAST:event_showPassActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField contact;
    private javax.swing.JPasswordField cpassword;
    public javax.swing.JTextField email;
    private javax.swing.JLabel icon;
    public javax.swing.JTextField id;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTextField meterId;
    public javax.swing.JTextField month;
    private javax.swing.JPasswordField newPassword;
    private javax.swing.JPasswordField oldPassword;
    public javax.swing.JComboBox<String> payment;
    private javax.swing.JButton print13;
    private javax.swing.JButton print14;
    private javax.swing.JButton print21;
    private javax.swing.JButton print22;
    private javax.swing.JButton print23;
    private javax.swing.JButton print24;
    private javax.swing.JButton print8;
    private javax.swing.JButton print9;
    public javax.swing.JTextField reference;
    private javax.swing.JButton remove;
    private javax.swing.JButton select;
    private javax.swing.JCheckBox showPass;
    public javax.swing.JTextField status;
    public javax.swing.JTextField tax;
    public javax.swing.JTextField totalBill;
    public javax.swing.JTextField unit;
    public javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
