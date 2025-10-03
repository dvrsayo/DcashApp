package UI;
import javax.swing.*;
import Main.DataAccount;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JButton eyeButton;
    private boolean passwordVisible = false;
    
    public LoginForm() {
        setupFrame();
        setupUI();
    }
    
    private void setupFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(370, 800);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    private void setupUI() {
        JPanel mainPanel = createMainPanel();
        
        // Title
        mainPanel.add(createLabel("Login", 24, Font.BOLD, 16, 89, 343, 51));
        
        // Username field
        mainPanel.add(createLabel("Mobile Number", 11, Font.PLAIN, 23, 180, 313, 20));
        mainPanel.add(createTextField("Mobile Number", 23, 200, usernameField = new JTextField()));
        
        // Password field
        mainPanel.add(createLabel("Password", 11, Font.PLAIN, 23, 260, 313, 20));
        JPanel passPanel = createTextField("Password", 23, 280, passwordField = new JPasswordField());
        setupPasswordToggle(passPanel);
        mainPanel.add(passPanel);
        
        // Error label
        errorLabel = createLabel("", 11, Font.PLAIN, 30, 330, 300, 20);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        mainPanel.add(errorLabel);
        
        // Additional components
        mainPanel.add(createLabel("Forgot your password?", 10, Font.PLAIN, 194, 345, 131, 34));
        mainPanel.add(createButton("Sign in", 23, 400, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleLogin();
            }
        }));
        mainPanel.add(createClickableLabel("Don't have an account? Sign up", 0, 455, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openSignUpForm();
            }
        }));
        
        add(mainPanel);
    }
    
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
    
    private void setupPasswordToggle(JPanel passPanel) {
        eyeButton = new JButton();
        eyeButton.setPreferredSize(new Dimension(40, 40));
        eyeButton.setBackground(Color.decode("#e7e7e7"));
        eyeButton.setBorder(BorderFactory.createEmptyBorder());
        eyeButton.setFocusPainted(false);
        eyeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eyeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                togglePasswordVisibility();
            }
        });
        updateEyeButtonIcon();
        passPanel.add(eyeButton, BorderLayout.EAST);
    }
    
    private JButton createButton(String text, int x, int y, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 313, 50);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
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
    
    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        passwordField.setEchoChar(passwordVisible ? (char) 0 : '•');
        updateEyeButtonIcon();
        passwordField.repaint();
    }
    
    private void updateEyeButtonIcon() {
        eyeButton.setText(passwordVisible ? "👁️" : "🙈");
        eyeButton.setToolTipText(passwordVisible ? "Hide password" : "Show password");
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
    
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both mobile number and password");
        } else if (!DataAccount.verifyUser(username, password)) {
            showError("Invalid mobile number or password");
        } else {
            errorLabel.setVisible(false);
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new HomeAccount(username).setVisible(true);
            dispose();
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void openSignUpForm() {
        dispose();
        new SignUpForm().setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SplashScene().showSplash());
    }   
}