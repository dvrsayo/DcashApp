package UI;
import javax.swing.*;
import Main.DataAccount;
import java.awt.*;

public class SignUpForm extends JFrame {
    private JTextField mobileField;
    private JPasswordField passwordField, confirmPasswordField;
    private JLabel errorLabel;
    private JButton passwordToggle, confirmPasswordToggle;
    private boolean passwordVisible = false, confirmPasswordVisible = false;
    
    public SignUpForm() {
        setupFrame();
        setupUI();
    }
    
    private void setupFrame() {
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(370, 800);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    private void setupUI() {
        JPanel mainPanel = createMainPanel();
        
        // Title
        mainPanel.add(createLabel("Create Account", 24, Font.BOLD, 16, 89, 343, 51));
        
        // Mobile field
        mainPanel.add(createLabel("Mobile Number", 11, Font.PLAIN, 23, 150, 313, 20));
        mainPanel.add(createTextField("Mobile Number", 23, 170, mobileField = new JTextField()));
        
        // Password fields
        mainPanel.add(createLabel("Password", 11, Font.PLAIN, 23, 230, 313, 20));
        JPanel passPanel = createTextField("Password", 23, 250, passwordField = new JPasswordField());
        setupPasswordToggle(passPanel, passwordToggle = new JButton(), true);
        mainPanel.add(passPanel);
        
        mainPanel.add(createLabel("Confirm Password", 11, Font.PLAIN, 23, 310, 313, 20));
        JPanel confirmPanel = createTextField("Confirm Password", 23, 330, confirmPasswordField = new JPasswordField());
        setupPasswordToggle(confirmPanel, confirmPasswordToggle = new JButton(), false);
        mainPanel.add(confirmPanel);
        
        // Error label
        errorLabel = createLabel("", 11, Font.PLAIN, 23, 380, 313, 20);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        mainPanel.add(errorLabel);
        
        // Buttons
        mainPanel.add(createButton("Create Account", 23, 420, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleSignUp();
            }
        }));
        mainPanel.add(createClickableLabel("Already have an account? Sign in", 0, 480, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openLoginForm();
            }
        }));
        
        add(mainPanel);
    }
    
    // Reuse helper methods from LoginForm (would be better in a common utility class)
    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#EEEEEE"));
        panel.setLayout(null);
        return panel;
    }
    
    private JLabel createLabel(String text, int fontSize, int style, int x, int y, int w, int h) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, w, h);
        label.setFont(new Font("Arial", style, fontSize));
        label.setForeground(Color.decode("#333333"));
        return label;
    }
    
    private JPanel createTextField(String placeholder, int x, int y, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.decode("#e7e7e7"));
        panel.setBounds(x, y, 313, 44);
        
        field.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        field.setBackground(Color.decode("#e7e7e7"));
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(field);
        
        return panel;
    }
    
    private void setupPasswordToggle(JPanel panel, JButton toggle, boolean isPassword) {
        toggle.setPreferredSize(new Dimension(40, 40));
        toggle.setBackground(Color.decode("#e7e7e7"));
        toggle.setBorder(BorderFactory.createEmptyBorder());
        toggle.setFocusPainted(false);
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                togglePasswordVisibility(isPassword);
            }
        });
        updateToggleIcon(toggle, isPassword ? passwordVisible : confirmPasswordVisible);
        panel.add(toggle, BorderLayout.EAST);
    }
    
    private JButton createButton(String text, int x, int y, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 313, 50);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);
        return button;
    }
    
    private JLabel createClickableLabel(String text, int x, int y, java.awt.event.ActionListener action) {
        JLabel label = createLabel(text, 11, Font.PLAIN, x, y, 356, 34);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.actionPerformed(new java.awt.event.ActionEvent(this, 0, ""));
            }
        });
        return label;
    }
    
    private void togglePasswordVisibility(boolean isPasswordField) {
        if (isPasswordField) {
            passwordVisible = !passwordVisible;
        } else {
            confirmPasswordVisible = !confirmPasswordVisible;
        }
        
        // Sync both fields for better UX
        boolean show = passwordVisible || confirmPasswordVisible;
        passwordField.setEchoChar(show ? (char) 0 : '•');
        confirmPasswordField.setEchoChar(show ? (char) 0 : '•');
        
        updateToggleIcon(passwordToggle, passwordVisible);
        updateToggleIcon(confirmPasswordToggle, confirmPasswordVisible);
    }
    
    private void updateToggleIcon(JButton toggle, boolean visible) {
        toggle.setText(visible ? "👁️" : "🙈");
    }
    
    private void handleSignUp() {
        String mobile = mobileField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
    
        if (mobile.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields");
        } else if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
        } else if (password.length() < 6) {
            showError("Password must be at least 6 characters long");
        } else if (!isValidMobile(mobile)) {
            showError("Please enter a valid mobile number (09XXXXXXXXX)");
        } else if (DataAccount.userExists(mobile)) {
            showError("Mobile number already registered");
        } else if (DataAccount.addUser(mobile, password)) {
            errorLabel.setVisible(false);
            JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            openLoginForm();
        } else {
            showError("Failed to create account. Please try again.");
        }
    }
    
    private boolean isValidMobile(String mobile) {
        return mobile.matches("09\\d{9}");
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void openLoginForm() {
        dispose();
        new LoginForm().setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUpForm().setVisible(true));
    }
}