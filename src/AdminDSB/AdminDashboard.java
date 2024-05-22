package AdminDSB;

import static AdminDSB.pendingAccounts.*;
import Config.*;
import LoginDSB.*;
import RegDSB.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.*;
import net.proteanit.sql.DbUtils;

public class AdminDashboard extends javax.swing.JFrame {

    public File selectedFile;
    public String path2 = null;
    public String destination = "";
    public String oldPath;
    public String path;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern CONTACT_PATTERN = Pattern.compile("^[0-9]{11}$");

    public AdminDashboard() {
        initComponents();
        displayData();
        displayPending();
        pendingPayment();
        recentlyPaid();
        customizeComponents();
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

    private void approvePayment() {
        int rowIndex = pendings.getSelectedRow();
        if (rowIndex < 0) {
            errorMessage("PLEASE SELECT AN INDEX!");
        } else {
            try {
                TableModel tbl = pendings.getModel();
                new DBConnector().updateData("UPDATE transaction SET t_status = 'PAID' WHERE t_id = '" + tbl.getValueAt(rowIndex, 0).toString() + "'");
                successMessage("ACCOUNT APPROVED SUCCESSFULLY!!");
                displayData();
                displayPending();
                pendingPayment();
                recentlyPaid();
            } catch (SQLException er) {
                System.out.println("ERROR: " + er.getMessage());
            }
        }
    }

    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    private boolean validationCheckerForEdit() {

        if (email2.getText().isEmpty() || !EMAIL_PATTERN.matcher(email2.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email address!");
            return false;
        } else if (contact2.getText().isEmpty() || !CONTACT_PATTERN.matcher(contact2.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid contact number! Must be 11 digits.");
            return false;
        } else if (username2.getText().isEmpty() || email2.getText().isEmpty() || contact2.getText().isEmpty()) {
            errorMessage("FILL ALL THE REQUIREMENTS!");
            return false;
        } else if (!contact2.getText().matches("\\d+")) {
            errorMessage("CONTACT MUST CONTAIN ONLY DIGITS!");
            return false;
        } else if (selectedFile == null && jLabel15.getIcon() == null) {
            errorMessage("PLEASE INSERT AN IMAGE FIRST!");
            return false;
        } else {
            return true;
        }
    }

    private String xemail, xusername;

    private boolean updateChecker() throws SQLException {
        ResultSet rs = new DBConnector().getData("select * from bill where (username = '" + username1.getText() + "' or email = '" + email1.getText() + "') and id != '" + id.getText() + "'");
        if (rs.next()) {
            xemail = rs.getString("email");
            if (xemail.equalsIgnoreCase(email1.getText())) {
                errorMessage("EMAIL HAS BEEN USED!");
            }
            xusername = rs.getString("username");
            if (xusername.equalsIgnoreCase(username1.getText())) {
                errorMessage("USERNAME HAS BEEN USERD!");
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean updateCheckerForEdit() throws SQLException {
        ResultSet rs = new DBConnector().getData("select * from bill where (username = '" + username2.getText() + "' or email = '" + email2.getText() + "') and id != '" + id1.getText() + "'");
        if (rs.next()) {
            xemail = rs.getString("email");
            if (xemail.equalsIgnoreCase(email2.getText())) {
                errorMessage("EMAIL HAS BEEN USED!");
            }
            xusername = rs.getString("username");
            if (xusername.equalsIgnoreCase(username2.getText())) {
                errorMessage("USERNAME HAS BEEN USERD!");
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean validationCheckerForMyData() {

        if (email1.getText().isEmpty() || !EMAIL_PATTERN.matcher(email1.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email address!");
            return false;
        } else if (contact1.getText().isEmpty() || !CONTACT_PATTERN.matcher(contact1.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid contact number! Must be 11 digits.");
            return false;
        } else if (username1.getText().isEmpty() || email1.getText().isEmpty() || contact1.getText().isEmpty()) {
            errorMessage("FILL ALL THE REQUIREMENTS!");
            return false;
        } else if (!contact1.getText().matches("\\d+")) {
            errorMessage("CONTACT MUST CONTAIN ONLY DIGITS!");
            return false;
        } else if (selectedFile == null && jLabel6.getIcon() == null) {
            errorMessage("PLEASE INSERT AN IMAGE FIRST!");
            return false;
        } else {
            return true;
        }
    }

    private boolean validationCheckerForAddingAccount() {

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
        } else if (selectedFile == null && jLabel5.getIcon() == null) {
            errorMessage("PLEASE INSERT AN IMAGE FIRST!");
            return false;
        } else {
            return true;
        }
    }

    private boolean validationCheckerForBillStatement() {
        if (unit.getText().isEmpty() || !isInteger(unit.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid unit! Please enter an integer value.");
            return false;
        } else if (totalBill.getText().isEmpty() || !isDouble(totalBill.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid bill! Please enter an integer value.");
            return false;
        } else if (reference.getText().trim().isEmpty() || reference.getText().contains(" ")) {
            JOptionPane.showMessageDialog(this, "Invalid reference!");
            return false;
        } else {
            return true;
        }
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void editAccount() throws SQLException, IOException {
        if (updateCheckerForEdit()) {
        } else if (!validationCheckerForEdit()) {
        } else {
            new DBConnector().updateData("update bill set email = '" + email2.getText() + "',username = '" + username2.getText() + "', "
                    + "contact = '" + contact2.getText() + "', type = '" + type2.getSelectedItem() + "', "
                    + "status = '" + status2.getSelectedItem() + "' , image = '" + destination + "' where id = '" + id1.getText() + "'");

            if (selectedFile != null && path != null) {
                Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            successMessage("ACCOUNT SUCCESSFULLY UPDATED!");
            LoginDashboard ad = new LoginDashboard();
            ad.setVisible(true);
            dispose();
        }
    }

    public void myData() throws SQLException, IOException {
        if (updateChecker()) {
        } else if (!validationCheckerForMyData()) {
        } else {
            new DBConnector().updateData("update bill set email = '" + email1.getText() + "',username = '" + username1.getText() + "', "
                    + "contact = '" + contact1.getText() + "', type = '" + type1.getSelectedItem() + "', "
                    + "status = '" + status1.getSelectedItem() + "' , Image = '" + destination + "' where id = '" + id.getText() + "'");

            if (selectedFile != null) {
                Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            successMessage("ACCOUNT SUCCESSFULLY UPDATED!");
            LoginDashboard ad = new LoginDashboard();
            ad.setVisible(true);
            dispose();
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

    private static void setDefaultFont(Font font) {
        UIManager.put("Button.font", new FontUIResource(font));
        UIManager.put("ToggleButton.font", new FontUIResource(font));
        UIManager.put("RadioButton.font", new FontUIResource(font));
        UIManager.put("CheckBox.font", new FontUIResource(font));
        UIManager.put("ColorChooser.font", new FontUIResource(font));
        UIManager.put("ComboBox.font", new FontUIResource(font));
        UIManager.put("Label.font", new FontUIResource(font));
        UIManager.put("List.font", new FontUIResource(font));
        UIManager.put("MenuBar.font", new FontUIResource(font));
        UIManager.put("MenuItem.font", new FontUIResource(font));
        UIManager.put("RadioButtonMenuItem.font", new FontUIResource(font));
        UIManager.put("CheckBoxMenuItem.font", new FontUIResource(font));
        UIManager.put("Menu.font", new FontUIResource(font));
        UIManager.put("PopupMenu.font", new FontUIResource(font));
        UIManager.put("OptionPane.font", new FontUIResource(font));
        UIManager.put("Panel.font", new FontUIResource(font));
        UIManager.put("ProgressBar.font", new FontUIResource(font));
        UIManager.put("ScrollPane.font", new FontUIResource(font));
        UIManager.put("Viewport.font", new FontUIResource(font));
        UIManager.put("TabbedPane.font", new FontUIResource(font));
        UIManager.put("Table.font", new FontUIResource(font));
        UIManager.put("TableHeader.font", new FontUIResource(font));
        UIManager.put("TextField.font", new FontUIResource(font));
        UIManager.put("PasswordField.font", new FontUIResource(font));
        UIManager.put("TextArea.font", new FontUIResource(font));
        UIManager.put("TextPane.font", new FontUIResource(font));
        UIManager.put("EditorPane.font", new FontUIResource(font));
        UIManager.put("TitledBorder.font", new FontUIResource(font));
        UIManager.put("ToolBar.font", new FontUIResource(font));
        UIManager.put("ToolTip.font", new FontUIResource(font));
        UIManager.put("Tree.font", new FontUIResource(font));
    }

    private void customizeComponents() {
        customizeButton(print);
        customizeButton(create);
        customizeButton(edit);
        customizeButton(pending);
        customizeButton(logout);
        customizeButton(print1);
        customizeButton(print2);
        customizeButton(print3);
        customizeButton(print4);
        customizeButton(print5);
        customizeButton(print6);
        customizeButton(print7);
        customizeButton(print8);
        customizeButton(print9);
        customizeButton(print10);
        customizeButton(print11);
        customizeButton(print12);
        customizeButton(print14);
        customizeButton(print15);
        customizeButton(print16);
        customizeButton(print17);
        customizeButton(print18);
        customizeButton(generateCode);
        customizeButton(print20);
        customizeButton(print21);
        customizeButton(print22);
        customizeButton(logout1);
        customizeButton(logout2);
        customizeButton(logout3);
        customizeButton(logout5);
        customizeButton(logout4);
        customizeButton(logout6);
        customizeButton(print23);
        customizeButton(print24);

        addActionListener(email, "Email");
        addActionListener(username, "Username");
        addActionListener(password, "Password");
        addActionListener(contact, "Contact#");
    }

    private void customizeButton(JButton button) {
        button.setOpaque(false);
        button.setBorder(new RoundedBorder(20));
        button.setForeground(Color.black);
        button.setFocusable(false);
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

    private void displayData() {
        try {
            ResultSet rs = new DBConnector().getData("select * from bill where status in ('active', 'inactive') and id != '" + id.getText() + "'");
            data.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching data: " + e.getMessage());
        }
    }

    private void pendingPayment() {
        try {
            ResultSet rs = new DBConnector().getData("select * from transaction where t_status = 'UNPAID'");
            pendings.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching data: " + e.getMessage());
        }
    }

    private boolean duplicateChecker() throws SQLException {
        ResultSet rs = new DBConnector().getData("select * from bill where email = '" + email.getText() + "' or username = '" + username.getText() + "'");
        if (rs.next()) {
            xemail = rs.getString("email");
            if (xemail.equals(email.getText())) {
                errorMessage("EMAIL HAS BEEN USED!");
            }
            xusername = rs.getString("username");
            if (xusername.equals(username.getText())) {
                errorMessage("USERNAME HAS BEEN USED!");
            }
            return true;
        } else {
            return false;
        }
    }

    private void displayPending() {
        try {
            ResultSet rs = new DBConnector().getData("select id,email,username,type from bill where status = 'PENDING'");
            data1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            System.err.println("An error occurred while fetching data: " + e.getMessage());
        }
    }

    private void recentlyPaid() {
        try {
            ResultSet rs = new DBConnector().getData("select * from transaction where t_status = 'PAID'");
            paid.setModel(DbUtils.resultSetToTableModel(rs));
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
                displayPending();
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
                displayPending();
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel14 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        data = new javax.swing.JTable();
        id = new javax.swing.JTextField();
        print = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        logout5 = new javax.swing.JButton();
        logout = new javax.swing.JButton();
        create = new javax.swing.JButton();
        pending = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        logout1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        data1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        print5 = new javax.swing.JButton();
        print6 = new javax.swing.JButton();
        print7 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        email = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        contact = new javax.swing.JTextField();
        status = new javax.swing.JComboBox<>();
        type = new javax.swing.JComboBox<>();
        showPass = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        print1 = new javax.swing.JButton();
        print2 = new javax.swing.JButton();
        print3 = new javax.swing.JButton();
        print4 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        email1 = new javax.swing.JTextField();
        username1 = new javax.swing.JTextField();
        contact1 = new javax.swing.JTextField();
        status1 = new javax.swing.JComboBox<>();
        type1 = new javax.swing.JComboBox<>();
        showPass1 = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        print8 = new javax.swing.JButton();
        print9 = new javax.swing.JButton();
        print10 = new javax.swing.JButton();
        print11 = new javax.swing.JButton();
        print12 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        email2 = new javax.swing.JTextField();
        username2 = new javax.swing.JTextField();
        password2 = new javax.swing.JPasswordField();
        contact2 = new javax.swing.JTextField();
        status2 = new javax.swing.JComboBox<>();
        type2 = new javax.swing.JComboBox<>();
        showPass2 = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        print14 = new javax.swing.JButton();
        print15 = new javax.swing.JButton();
        print16 = new javax.swing.JButton();
        print17 = new javax.swing.JButton();
        id1 = new javax.swing.JTextField();
        print18 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        month = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        generateCode = new javax.swing.JButton();
        print21 = new javax.swing.JButton();
        meterId = new javax.swing.JTextField();
        reference = new javax.swing.JTextField();
        unit = new javax.swing.JTextField();
        tax = new javax.swing.JTextField();
        totalBill = new javax.swing.JTextField();
        print20 = new javax.swing.JButton();
        print22 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        pendings = new javax.swing.JTable();
        logout2 = new javax.swing.JButton();
        logout3 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        paid = new javax.swing.JTable();
        logout4 = new javax.swing.JButton();
        logout6 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        print23 = new javax.swing.JButton();
        print24 = new javax.swing.JButton();
        showPass3 = new javax.swing.JCheckBox();
        cpassword = new javax.swing.JPasswordField();
        newPassword = new javax.swing.JPasswordField();
        oldPassword = new javax.swing.JPasswordField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1278, 744));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1278, 744));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 1310, 50));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        data.setAutoCreateRowSorter(true);
        data.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(), javax.swing.BorderFactory.createCompoundBorder()));
        data.setFont(new java.awt.Font("Yu Gothic", 1, 11)); // NOI18N
        data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        data.getTableHeader().setReorderingAllowed(false);
        data.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(data);
        data.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, 530, 560));

        id.setEditable(false);
        id.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setText("ID");
        jPanel1.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 530, 30));

        print.setBackground(new java.awt.Color(20, 161, 242));
        print.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print.setForeground(new java.awt.Color(255, 255, 255));
        print.setText("PRINT");
        print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printActionPerformed(evt);
            }
        });
        jPanel1.add(print, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 700, 530, 30));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/admin.png"))); // NOI18N
        jLabel12.setText("Admin[name]");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 120, 680, 80));

        logout5.setBackground(new java.awt.Color(20, 161, 242));
        logout5.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        logout5.setForeground(new java.awt.Color(255, 255, 255));
        logout5.setText("RECENT");
        logout5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout5ActionPerformed(evt);
            }
        });
        jPanel1.add(logout5, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 290, 250, 30));

        logout.setBackground(new java.awt.Color(20, 161, 242));
        logout.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setText("PENDING PAYMENTS");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        jPanel1.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 290, 250, 30));

        create.setBackground(new java.awt.Color(20, 161, 242));
        create.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        create.setForeground(new java.awt.Color(255, 255, 255));
        create.setText("CREATE");
        create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createActionPerformed(evt);
            }
        });
        jPanel1.add(create, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 240, 250, 30));

        pending.setBackground(new java.awt.Color(20, 161, 242));
        pending.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        pending.setForeground(new java.awt.Color(255, 255, 255));
        pending.setText("PENDING ACCOUNT");
        pending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pendingActionPerformed(evt);
            }
        });
        jPanel1.add(pending, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 240, 250, 30));

        edit.setBackground(new java.awt.Color(20, 161, 242));
        edit.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        edit.setForeground(new java.awt.Color(255, 255, 255));
        edit.setText("EDIT");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });
        jPanel1.add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 200, 520, 30));

        logout1.setBackground(new java.awt.Color(20, 161, 242));
        logout1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        logout1.setForeground(new java.awt.Color(255, 255, 255));
        logout1.setText("LOGOUT");
        logout1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout1ActionPerformed(evt);
            }
        });
        jPanel1.add(logout1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 330, 520, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/g.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 380, 520, 350));

        jTabbedPane1.addTab("tab1", jPanel1);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        data1.setAutoCreateRowSorter(true);
        data1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(), javax.swing.BorderFactory.createCompoundBorder()));
        data1.setFont(new java.awt.Font("Yu Gothic", 1, 11)); // NOI18N
        data1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        data1.getTableHeader().setReorderingAllowed(false);
        data1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                data1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(data1);
        data1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 1060, 510));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("PENDING ACCOUNTS");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 110, 230, -1));

        print5.setBackground(new java.awt.Color(20, 161, 242));
        print5.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print5.setForeground(new java.awt.Color(255, 255, 255));
        print5.setText("APPROVE");
        print5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print5ActionPerformed(evt);
            }
        });
        jPanel5.add(print5, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 670, 260, 30));

        print6.setBackground(new java.awt.Color(20, 161, 242));
        print6.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print6.setForeground(new java.awt.Color(255, 255, 255));
        print6.setText("DECLINE");
        print6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print6ActionPerformed(evt);
            }
        });
        jPanel5.add(print6, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 670, 260, 30));

        print7.setBackground(new java.awt.Color(20, 161, 242));
        print7.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print7.setForeground(new java.awt.Color(255, 255, 255));
        print7.setText("BACK");
        print7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print7ActionPerformed(evt);
            }
        });
        jPanel5.add(print7, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 670, 260, 30));

        jTabbedPane1.addTab("tab1", jPanel5);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        email.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email.setText("EMAIL");
        email.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emailMouseClicked(evt);
            }
        });
        jPanel3.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 160, 290, 30));

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
        jPanel3.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 160, 290, 30));

        password.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setText("PASSWORD");
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFocusGained(evt);
            }
        });
        jPanel3.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 210, 290, 30));

        contact.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        contact.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contact.setText("CONTACT#\n");
        contact.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contactFocusGained(evt);
            }
        });
        jPanel3.add(contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 260, 290, 30));

        status.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVE", "INACTIVE", "PENDING" }));
        status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusActionPerformed(evt);
            }
        });
        jPanel3.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 210, 290, 30));

        type.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "CUSTOMER" }));
        type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeActionPerformed(evt);
            }
        });
        jPanel3.add(type, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 260, 290, 30));

        showPass.setBackground(new java.awt.Color(255, 255, 255));
        showPass.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        showPass.setText("SHOW PASSWORD");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });
        jPanel3.add(showPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 300, 130, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("ADD A USER");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 1290, 60));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 480, 350));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 520, 390));

        print1.setBackground(new java.awt.Color(20, 161, 242));
        print1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print1.setForeground(new java.awt.Color(255, 255, 255));
        print1.setText("REMOVE");
        print1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print1ActionPerformed(evt);
            }
        });
        jPanel3.add(print1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 620, 520, 30));

        print2.setBackground(new java.awt.Color(20, 161, 242));
        print2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print2.setForeground(new java.awt.Color(255, 255, 255));
        print2.setText("ADD");
        print2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print2ActionPerformed(evt);
            }
        });
        jPanel3.add(print2, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 380, 290, 30));

        print3.setBackground(new java.awt.Color(20, 161, 242));
        print3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print3.setForeground(new java.awt.Color(255, 255, 255));
        print3.setText("SELECT");
        print3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print3ActionPerformed(evt);
            }
        });
        jPanel3.add(print3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 580, 520, 30));

        print4.setBackground(new java.awt.Color(20, 161, 242));
        print4.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print4.setForeground(new java.awt.Color(255, 255, 255));
        print4.setText("CANCEL");
        print4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print4ActionPerformed(evt);
            }
        });
        jPanel3.add(print4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 380, 290, 30));

        jTabbedPane1.addTab("tab2", jPanel3);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        email1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        email1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email1.setText("EMAIL");
        email1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                email1MouseClicked(evt);
            }
        });
        jPanel6.add(email1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 170, 290, 30));

        username1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        username1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        username1.setText("USERNAME");
        username1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                username1FocusGained(evt);
            }
        });
        username1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                username1MouseClicked(evt);
            }
        });
        jPanel6.add(username1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 170, 290, 30));

        contact1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        contact1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contact1.setText("CONTACT#\n");
        contact1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contact1FocusGained(evt);
            }
        });
        jPanel6.add(contact1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 220, 290, 30));

        status1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        status1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVE", "INACTIVE", "PENDING" }));
        status1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                status1ActionPerformed(evt);
            }
        });
        jPanel6.add(status1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 220, 290, 30));

        type1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        type1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "CUSTOMER" }));
        type1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                type1ActionPerformed(evt);
            }
        });
        jPanel6.add(type1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 270, 290, 30));

        showPass1.setBackground(new java.awt.Color(255, 255, 255));
        showPass1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        showPass1.setText("SHOW PASSWORD");
        showPass1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPass1ActionPerformed(evt);
            }
        });
        jPanel6.add(showPass1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 270, 130, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("MY ACCOUNT");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 1290, 60));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 480, 350));

        jPanel6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 520, 390));

        print8.setBackground(new java.awt.Color(20, 161, 242));
        print8.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print8.setForeground(new java.awt.Color(255, 255, 255));
        print8.setText("CHANGE PASSWORD");
        print8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print8ActionPerformed(evt);
            }
        });
        jPanel6.add(print8, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 430, 290, 30));

        print9.setBackground(new java.awt.Color(20, 161, 242));
        print9.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print9.setForeground(new java.awt.Color(255, 255, 255));
        print9.setText("UPDATE");
        print9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print9ActionPerformed(evt);
            }
        });
        jPanel6.add(print9, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 390, 290, 30));

        print10.setBackground(new java.awt.Color(20, 161, 242));
        print10.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print10.setForeground(new java.awt.Color(255, 255, 255));
        print10.setText("SELECT");
        print10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print10ActionPerformed(evt);
            }
        });
        jPanel6.add(print10, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 590, 520, 30));

        print11.setBackground(new java.awt.Color(20, 161, 242));
        print11.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print11.setForeground(new java.awt.Color(255, 255, 255));
        print11.setText("CANCEL");
        print11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print11ActionPerformed(evt);
            }
        });
        jPanel6.add(print11, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 390, 290, 30));

        print12.setBackground(new java.awt.Color(20, 161, 242));
        print12.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print12.setForeground(new java.awt.Color(255, 255, 255));
        print12.setText("REMOVE");
        print12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print12ActionPerformed(evt);
            }
        });
        jPanel6.add(print12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 630, 520, 30));

        jTabbedPane1.addTab("tab2", jPanel6);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        email2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        email2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email2.setText("EMAIL");
        email2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                email2MouseClicked(evt);
            }
        });
        jPanel8.add(email2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 200, 290, 30));

        username2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        username2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        username2.setText("USERNAME");
        username2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                username2FocusGained(evt);
            }
        });
        username2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                username2MouseClicked(evt);
            }
        });
        jPanel8.add(username2, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 200, 290, 30));

        password2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        password2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password2.setText("PASSWORD");
        password2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                password2FocusGained(evt);
            }
        });
        jPanel8.add(password2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 250, 290, 30));

        contact2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        contact2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contact2.setText("CONTACT#\n");
        contact2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contact2FocusGained(evt);
            }
        });
        jPanel8.add(contact2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 300, 290, 30));

        status2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        status2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVE", "INACTIVE", "PENDING" }));
        status2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                status2ActionPerformed(evt);
            }
        });
        jPanel8.add(status2, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 250, 290, 30));

        type2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        type2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "CUSTOMER" }));
        type2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                type2ActionPerformed(evt);
            }
        });
        jPanel8.add(type2, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 300, 290, 30));

        showPass2.setBackground(new java.awt.Color(255, 255, 255));
        showPass2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        showPass2.setText("SHOW PASSWORD");
        showPass2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPass2ActionPerformed(evt);
            }
        });
        jPanel8.add(showPass2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 340, 130, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("UPDATE ACCOUNT");
        jPanel8.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 1290, 60));

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel9.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 480, 350));

        jPanel8.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 520, 390));

        print14.setBackground(new java.awt.Color(20, 161, 242));
        print14.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print14.setForeground(new java.awt.Color(255, 255, 255));
        print14.setText("UPDATE");
        print14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print14ActionPerformed(evt);
            }
        });
        jPanel8.add(print14, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 420, 290, 30));

        print15.setBackground(new java.awt.Color(20, 161, 242));
        print15.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print15.setForeground(new java.awt.Color(255, 255, 255));
        print15.setText("SELECT");
        jPanel8.add(print15, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 610, 520, 30));

        print16.setBackground(new java.awt.Color(20, 161, 242));
        print16.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print16.setForeground(new java.awt.Color(255, 255, 255));
        print16.setText("CANCEL");
        print16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print16ActionPerformed(evt);
            }
        });
        jPanel8.add(print16, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 420, 290, 30));

        print17.setBackground(new java.awt.Color(20, 161, 242));
        print17.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print17.setForeground(new java.awt.Color(255, 255, 255));
        print17.setText("MANAGE BILLING STATMENT");
        print17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print17ActionPerformed(evt);
            }
        });
        jPanel8.add(print17, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 460, 620, 30));

        id1.setEditable(false);
        id1.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        id1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id1.setText("ID");
        jPanel8.add(id1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 520, 30));

        print18.setBackground(new java.awt.Color(20, 161, 242));
        print18.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print18.setForeground(new java.awt.Color(255, 255, 255));
        print18.setText("REMOVE");
        jPanel8.add(print18, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 650, 520, 30));

        jTabbedPane1.addTab("tab2", jPanel8);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        month.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER" }));
        month.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthActionPerformed(evt);
            }
        });
        jPanel10.add(month, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 180, 260, 30));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("MANAGE BILLING STATEMENT");
        jPanel10.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 1260, 60));

        generateCode.setBackground(new java.awt.Color(20, 161, 242));
        generateCode.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        generateCode.setForeground(new java.awt.Color(255, 255, 255));
        generateCode.setText("GENERATE REFERENCE CODE");
        generateCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateCodeActionPerformed(evt);
            }
        });
        jPanel10.add(generateCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 430, 530, 30));

        print21.setBackground(new java.awt.Color(20, 161, 242));
        print21.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print21.setForeground(new java.awt.Color(255, 255, 255));
        print21.setText("BACK");
        print21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print21ActionPerformed(evt);
            }
        });
        jPanel10.add(print21, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 610, 530, 30));

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
        jPanel10.add(meterId, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 530, 30));

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
        jPanel10.add(reference, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 390, 530, 30));

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
        jPanel10.add(unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 180, 240, 30));

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
        jPanel10.add(tax, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 230, 530, 30));

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
        jPanel10.add(totalBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 280, 530, 30));

        print20.setBackground(new java.awt.Color(20, 161, 242));
        print20.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print20.setForeground(new java.awt.Color(255, 255, 255));
        print20.setText("CALCULATE");
        print20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print20ActionPerformed(evt);
            }
        });
        jPanel10.add(print20, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 320, 530, 30));

        print22.setBackground(new java.awt.Color(20, 161, 242));
        print22.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print22.setForeground(new java.awt.Color(255, 255, 255));
        print22.setText("ADD BILL");
        print22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print22ActionPerformed(evt);
            }
        });
        jPanel10.add(print22, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 570, 530, 30));

        jTabbedPane1.addTab("tab2", jPanel10);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("PENDING PAYMENTS");
        jPanel11.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 1260, 60));

        pendings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(pendings);

        jPanel11.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 970, 510));

        logout2.setBackground(new java.awt.Color(20, 161, 242));
        logout2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        logout2.setForeground(new java.awt.Color(255, 255, 255));
        logout2.setText("BACK");
        logout2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout2ActionPerformed(evt);
            }
        });
        jPanel11.add(logout2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 650, 240, 30));

        logout3.setBackground(new java.awt.Color(20, 161, 242));
        logout3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        logout3.setForeground(new java.awt.Color(255, 255, 255));
        logout3.setText("PAID");
        logout3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout3ActionPerformed(evt);
            }
        });
        jPanel11.add(logout3, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 650, 240, 30));

        jTabbedPane1.addTab("tab2", jPanel11);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("RECENTLY PAID CUSTOMERS");
        jPanel12.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 1260, 60));

        paid.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(paid);

        jPanel12.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 970, 510));

        logout4.setBackground(new java.awt.Color(20, 161, 242));
        logout4.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        logout4.setForeground(new java.awt.Color(255, 255, 255));
        logout4.setText("PRINT");
        logout4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout4ActionPerformed(evt);
            }
        });
        jPanel12.add(logout4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 970, 30));

        logout6.setBackground(new java.awt.Color(20, 161, 242));
        logout6.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        logout6.setForeground(new java.awt.Color(255, 255, 255));
        logout6.setText("BACK");
        logout6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout6ActionPerformed(evt);
            }
        });
        jPanel12.add(logout6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 690, 970, 30));

        jTabbedPane1.addTab("tab2", jPanel12);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        print23.setBackground(new java.awt.Color(20, 161, 242));
        print23.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print23.setForeground(new java.awt.Color(255, 255, 255));
        print23.setText("BACK");
        print23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print23ActionPerformed(evt);
            }
        });
        jPanel13.add(print23, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 610, 530, 30));

        print24.setBackground(new java.awt.Color(20, 161, 242));
        print24.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        print24.setForeground(new java.awt.Color(255, 255, 255));
        print24.setText("CHANGE PASSWORD");
        print24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print24ActionPerformed(evt);
            }
        });
        jPanel13.add(print24, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 570, 530, 30));

        showPass3.setBackground(new java.awt.Color(255, 255, 255));
        showPass3.setFont(new java.awt.Font("Yu Gothic", 0, 11)); // NOI18N
        showPass3.setText("SHOW PASSWORD");
        showPass3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPass3ActionPerformed(evt);
            }
        });
        jPanel13.add(showPass3, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 470, -1, -1));

        cpassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cpassword.setText("CONFIRM PASS");
        jPanel13.add(cpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 430, 460, 30));

        newPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        newPassword.setText("NEW PASSWORD");
        jPanel13.add(newPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 340, 460, 30));

        oldPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        oldPassword.setText("OLD PASSWORD");
        jPanel13.add(oldPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 260, 460, 30));

        jLabel20.setFont(new java.awt.Font("Yu Gothic", 0, 12)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("CONFIRM PASSWORD");
        jPanel13.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 390, 460, 40));

        jLabel21.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("CHANGE PASS");
        jPanel13.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 140, 460, 40));

        jLabel22.setFont(new java.awt.Font("Yu Gothic", 0, 12)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("OLD PASSWORD");
        jPanel13.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 220, 460, 40));

        jLabel23.setFont(new java.awt.Font("Yu Gothic", 0, 12)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("NEW PASSWORD");
        jPanel13.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 300, 460, 40));

        jTabbedPane1.addTab("tab2", jPanel13);

        jPanel2.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, -50, 1340, 900));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 830));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        jTabbedPane1.setSelectedIndex(6);
    }//GEN-LAST:event_logoutActionPerformed

    private void createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_createActionPerformed

    private void dataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataMouseClicked
    }//GEN-LAST:event_dataMouseClicked

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        int rowIndex = data.getSelectedRow();
        if (rowIndex < 0) {
            JOptionPane.showMessageDialog(null, "PLEASE SELECT AN INDEX!");
        } else {
            try {
                TableModel tbl = data.getModel();
                ResultSet rs = new DBConnector().getData("select * from bill where id = '" + tbl.getValueAt(rowIndex, 0) + "'");
                if (rs.next()) {
                    id1.setText("" + rs.getString("id"));
                    email2.setText("" + rs.getString("email"));
                    contact2.setText("" + rs.getString("contact"));
                    username2.setText("" + rs.getString("username"));
                    status2.setSelectedItem("" + rs.getString("status"));
                    type2.setSelectedItem("" + rs.getString("type"));
                    jLabel15.setIcon(ResizeImage(rs.getString("image"), null, jLabel15));
                    oldPath = rs.getString("image");
                    path = rs.getString("image");
                    destination = rs.getString("image");
                    jTabbedPane1.setSelectedIndex(4);
                    if (rs.getString("image") != null) {
                        print15.setEnabled(false);
                        print18.setEnabled(true);
                    } else {
                        print15.setEnabled(true);
                        print18.setEnabled(false);
                    }

                }
            } catch (SQLException er) {
                System.out.println("ERROR: " + er.getMessage());
            }
        }
    }//GEN-LAST:event_editActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        Container cons = Container.getInstance();
//        if (cons.getUsername() == null) {
//            errorMessage("PLEASE GO TO LOGIN DASHBOARD FIRST!");
//            new LoginDashboard().setVisible(true);
//            dispose();
//        } else {
        id.setText("" + cons.getId());
        jLabel12.setText("" + cons.getUsername());
        displayData();
//        }
    }//GEN-LAST:event_formWindowActivated

    private void data1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_data1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_data1MouseClicked

    private void print5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print5ActionPerformed
        acceptAccount();
    }//GEN-LAST:event_print5ActionPerformed

    private void print6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print6ActionPerformed
        declineAccount();
    }//GEN-LAST:event_print6ActionPerformed

    private void print7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print7ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_print7ActionPerformed

    private void pendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pendingActionPerformed
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_pendingActionPerformed

    private void email1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_email1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_email1MouseClicked

    private void username1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_username1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_username1FocusGained

    private void username1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_username1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_username1MouseClicked

    private void contact1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contact1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_contact1FocusGained

    private void status1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_status1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_status1ActionPerformed

    private void type1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_type1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_type1ActionPerformed

    private void showPass1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPass1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_showPass1ActionPerformed

    private void print9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print9ActionPerformed
        try {
            myData();
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_print9ActionPerformed

    private void print11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print11ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_print11ActionPerformed

    private void print4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print4ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_print4ActionPerformed

    private void print2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print2ActionPerformed
        try {
            if (duplicateChecker()) {
            } else if (!validationCheckerForAddingAccount()) {
            } else {
                String pass = passwordHashing.hashPassword(password.getText());
                new DBConnector().insertData("insert into bill (email,username,password,contact,type,status,image) "
                        + "values ('" + email.getText() + "','" + username.getText() + "', '" + pass + "'"
                        + ",'" + contact.getText() + "','" + type.getSelectedItem() + "', '" + status.getSelectedItem() + "', '" + destination + "')");

                if (destination != null) {
                    Files.copy(selectedFile.toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                JOptionPane.showMessageDialog(this, "REGISTRATION SUCCESSFULL!", "SUCCESS", INFORMATION_MESSAGE);

                new AdminDashboard().setVisible(true);
                dispose();
            }
        } catch (SQLException | IOException er) {
            System.out.println("Eror: " + er.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_print2ActionPerformed

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
        char echoChar = showPass.isSelected() ? (char) 0 : '*';
        password.setEchoChar(echoChar);
    }//GEN-LAST:event_showPassActionPerformed

    private void typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeActionPerformed
    }//GEN-LAST:event_typeActionPerformed

    private void statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusActionPerformed

    }//GEN-LAST:event_statusActionPerformed

    private void contactFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactFocusGained
        contact.setText("");
    }//GEN-LAST:event_contactFocusGained

    private void passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordFocusGained
        password.setText("");
    }//GEN-LAST:event_passwordFocusGained

    private void usernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usernameMouseClicked

    }//GEN-LAST:event_usernameMouseClicked

    private void usernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameFocusGained
        username.setText("");
    }//GEN-LAST:event_usernameFocusGained

    private void emailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailMouseClicked
        email.setText("");
    }//GEN-LAST:event_emailMouseClicked

    private void print8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print8ActionPerformed
        jTabbedPane1.setSelectedIndex(8);
    }//GEN-LAST:event_print8ActionPerformed

    private void email2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_email2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_email2MouseClicked

    private void username2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_username2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_username2FocusGained

    private void username2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_username2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_username2MouseClicked

    private void password2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_password2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_password2FocusGained

    private void contact2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contact2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_contact2FocusGained

    private void status2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_status2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_status2ActionPerformed

    private void type2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_type2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_type2ActionPerformed

    private void showPass2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPass2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_showPass2ActionPerformed

    private void print14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print14ActionPerformed
        try {
            editAccount();
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_print14ActionPerformed

    private void print16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print16ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_print16ActionPerformed

    private void print21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print21ActionPerformed
        jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_print21ActionPerformed

    private void generateCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateCodeActionPerformed
        String generatedCode = generateRandomCode(15);
        reference.setText(generatedCode);
    }//GEN-LAST:event_generateCodeActionPerformed

    private void monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_monthActionPerformed

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        try {
            Container cs = Container.getInstance();
            if (cs == null || cs.getId() == null) {
                JOptionPane.showMessageDialog(null, "Please Login First!");
                LoginDashboard ld = new LoginDashboard();
                ld.setVisible(true);
                dispose();
            }

            String query = "SELECT * FROM bill WHERE id = ?";
            try (PreparedStatement pstmt = new DBConnector().getConnection().prepareStatement(query)) {
                pstmt.setString(1, cs.getId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        email1.setText(rs.getString("email"));
                        username1.setText(rs.getString("username"));
                        contact1.setText(rs.getString("contact"));
                        status1.setSelectedItem(rs.getString("status"));
                        type1.setSelectedItem(rs.getString("type"));
                        String imagePath = rs.getString("image");

                        SwingUtilities.invokeLater(() -> {
                            jTabbedPane1.setSelectedIndex(3);
                        });

                        if (imagePath != null && !imagePath.isEmpty()) {
                            jLabel6.setIcon(ResizeImage(imagePath, null, jLabel6));
                            oldPath = imagePath;
                            path = imagePath;
                            destination = imagePath;
                            print10.setEnabled(false);
                            print12.setEnabled(true);
                        } else {
                            print10.setEnabled(true);
                            print12.setEnabled(false);
                        }
                    } else {
                        System.out.println("No data found for id: " + cs.getId());
                    }
                }
            }
        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected ERROR: " + e.getMessage());
        }
    }//GEN-LAST:event_jLabel12MouseClicked

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

    private void print17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print17ActionPerformed
        try {
            String query = "SELECT * FROM transaction WHERE t_id = ?";
            try (PreparedStatement pstmt = new DBConnector().getConnection().prepareStatement(query)) {
                pstmt.setString(1, id1.getText());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        meterId.setText(rs.getString("t_id"));
                        unit.setText(rs.getString("t_unit"));
                        month.setSelectedItem(rs.getString("t_month"));
                        totalBill.setText(rs.getString("t_total"));
                        reference.setText(rs.getString("t_reference"));

                        SwingUtilities.invokeLater(() -> {
                            jTabbedPane1.setSelectedIndex(5);
                        });
                    } else {
                        jTabbedPane1.setSelectedIndex(5);
                        meterId.setText(rs.getString("t_id"));
                        unit.setText("UNIT = ₱100 PER UNIT");
                        totalBill.setText("TOTAL BILL");
                        reference.setText("REFERENCE CODE");
                    }
                }
            }
        } catch (SQLException er) {
            System.out.println("ERROR: " + er.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected ERROR: " + e.getMessage());
        }

    }//GEN-LAST:event_print17ActionPerformed

    private void print20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print20ActionPerformed
        int uns = Integer.parseInt(unit.getText());
        double totalVat = 0.20;
        double subTotal = uns + (uns * totalVat);
        totalBill.setText(String.valueOf(subTotal));
    }//GEN-LAST:event_print20ActionPerformed

    private void print3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print3ActionPerformed
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
                    jLabel5.setIcon(ResizeImage(path, null, jLabel5));
                    print1.setEnabled(true);
                    print3.setEnabled(false);
                }
            } catch (Exception ex) {
                System.out.println("File Error!");
            }
        }
    }//GEN-LAST:event_print3ActionPerformed

    private void print1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print1ActionPerformed
        jLabel5.setIcon(null);
        path = "";
        destination = "";
        print1.setEnabled(false);
        print3.setEnabled(true);
    }//GEN-LAST:event_print1ActionPerformed

    private void print12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print12ActionPerformed
        destination = "";
        jLabel6.setIcon(null);
        path = "";
        print10.setEnabled(true);
        print12.setEnabled(false);
    }//GEN-LAST:event_print12ActionPerformed

    private void print10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print10ActionPerformed
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
                    jLabel6.setIcon(ResizeImage(path, null, jLabel6));
                    print10.setEnabled(false);
                    print12.setEnabled(true);
                }
            } catch (Exception ex) {
                System.out.println("File Error!");
            }
        }
    }//GEN-LAST:event_print10ActionPerformed

    private void print22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print22ActionPerformed
        try {
            if (!validationCheckerForBillStatement()) {
            } else {
                new DBConnector().insertData("insert into transaction (t_month, t_tax, t_unit, t_total, t_reference, t_id, t_status) "
                        + "values ('" + month.getSelectedItem() + "', '" + tax.getText() + "', "
                        + "'" + unit.getText() + "', '" + totalBill.getText() + "', '" + reference.getText() + "', '" + id1.getText() + "', 'UNPAID')");

                DBConnector db = new DBConnector();
                ResultSet rs = db.getData("select * from bill where id = '" + id1.getText() + "'");

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "BILL UPDATED SUCCESSFULLY!", "SUCCESS", INFORMATION_MESSAGE);

                    new AdminDashboard().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "BILL NOT FOUND!", "ERROR", ERROR_MESSAGE);
                }
            }
        } catch (SQLException er) {
            System.out.println("Error: " + er.getMessage());
        }
    }//GEN-LAST:event_print22ActionPerformed

    private void logout1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout1ActionPerformed
        new LoginDashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_logout1ActionPerformed

    private void logout2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout2ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_logout2ActionPerformed

    private void logout3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout3ActionPerformed
        approvePayment();
    }//GEN-LAST:event_logout3ActionPerformed

    private void logout4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout4ActionPerformed
        MessageFormat header = new MessageFormat("Recently Paid Customers Reports");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            paid.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException er) {
            System.out.println("" + er.getMessage());
        }
    }//GEN-LAST:event_logout4ActionPerformed

    private void logout6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout6ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_logout6ActionPerformed

    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed
        MessageFormat header = new MessageFormat("Total Registered Accounts Reports");
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            data.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException er) {
            System.out.println("" + er.getMessage());
        }
    }//GEN-LAST:event_printActionPerformed

    private void logout5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout5ActionPerformed
        jTabbedPane1.setSelectedIndex(7);
    }//GEN-LAST:event_logout5ActionPerformed

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

    private void showPass3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPass3ActionPerformed
        char echoChar = showPass.isSelected() ? (char) 0 : '*';
        oldPassword.setEchoChar(echoChar);
        newPassword.setEchoChar(echoChar);
        cpassword.setEchoChar(echoChar);
    }//GEN-LAST:event_showPass3ActionPerformed

    public static void main(String args[]) {
        setDefaultFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField contact;
    public javax.swing.JTextField contact1;
    public javax.swing.JTextField contact2;
    private javax.swing.JPasswordField cpassword;
    private javax.swing.JButton create;
    public static javax.swing.JTable data;
    public static javax.swing.JTable data1;
    private javax.swing.JButton edit;
    public javax.swing.JTextField email;
    public javax.swing.JTextField email1;
    public javax.swing.JTextField email2;
    private javax.swing.JButton generateCode;
    private javax.swing.JTextField id;
    private javax.swing.JTextField id1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton logout;
    private javax.swing.JButton logout1;
    private javax.swing.JButton logout2;
    private javax.swing.JButton logout3;
    private javax.swing.JButton logout4;
    private javax.swing.JButton logout5;
    private javax.swing.JButton logout6;
    public javax.swing.JTextField meterId;
    public javax.swing.JComboBox<String> month;
    private javax.swing.JPasswordField newPassword;
    private javax.swing.JPasswordField oldPassword;
    private javax.swing.JTable paid;
    public javax.swing.JPasswordField password;
    public javax.swing.JPasswordField password2;
    private javax.swing.JButton pending;
    private javax.swing.JTable pendings;
    private javax.swing.JButton print;
    private javax.swing.JButton print1;
    private javax.swing.JButton print10;
    private javax.swing.JButton print11;
    private javax.swing.JButton print12;
    private javax.swing.JButton print14;
    private javax.swing.JButton print15;
    private javax.swing.JButton print16;
    private javax.swing.JButton print17;
    private javax.swing.JButton print18;
    private javax.swing.JButton print2;
    private javax.swing.JButton print20;
    private javax.swing.JButton print21;
    private javax.swing.JButton print22;
    private javax.swing.JButton print23;
    private javax.swing.JButton print24;
    private javax.swing.JButton print3;
    private javax.swing.JButton print4;
    private javax.swing.JButton print5;
    private javax.swing.JButton print6;
    private javax.swing.JButton print7;
    private javax.swing.JButton print8;
    private javax.swing.JButton print9;
    public javax.swing.JTextField reference;
    private javax.swing.JCheckBox showPass;
    private javax.swing.JCheckBox showPass1;
    private javax.swing.JCheckBox showPass2;
    private javax.swing.JCheckBox showPass3;
    public javax.swing.JComboBox<String> status;
    public javax.swing.JComboBox<String> status1;
    public javax.swing.JComboBox<String> status2;
    public javax.swing.JTextField tax;
    public javax.swing.JTextField totalBill;
    public javax.swing.JComboBox<String> type;
    public javax.swing.JComboBox<String> type1;
    public javax.swing.JComboBox<String> type2;
    public javax.swing.JTextField unit;
    public javax.swing.JTextField username;
    public javax.swing.JTextField username1;
    public javax.swing.JTextField username2;
    // End of variables declaration//GEN-END:variables
}
