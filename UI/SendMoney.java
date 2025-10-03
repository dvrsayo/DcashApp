package UI;
import javax.swing.*;
import Main.DataAccount;
import java.awt.*;

public class SendMoney extends JFrame {
    private JTextField phoneNumberField, amountField;
    private JLabel errorLabel;
    private String currentAccountNumber;
    
    public SendMoney(String accountNumber) {
        this.currentAccountNumber = accountNumber;
        setupFrame();
        setupUI();
    }
    
    private void setupFrame() {
        setTitle("Send Money");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(370, 800);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    private void setupUI() {
        JPanel mainPanel = createMainPanel();
        
        // Title
        mainPanel.add(createLabel("Send Money", 24, Font.BOLD, 16, 89, 343, 51));
        
        // Phone field
        mainPanel.add(createLabel("Mobile Number", 11, Font.PLAIN, 23, 180, 313, 20));
        mainPanel.add(createTextField("Mobile Number", 23, 200, phoneNumberField = new JTextField()));
        
        // Amount field
        mainPanel.add(createLabel("Amount", 11, Font.PLAIN, 23, 260, 313, 20));
        mainPanel.add(createTextField("Amount", 23, 280, amountField = new JTextField()));
        
        // Error label
        errorLabel = createLabel("", 11, Font.PLAIN, 30, 330, 300, 20);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        mainPanel.add(errorLabel);
        
        // Buttons
        mainPanel.add(createButton("Send", 23, 400, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleSendMoney();
            }
        }));
        mainPanel.add(createClickableLabel("Back to Home", 0, 455, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                returnToHome();
            }
        }));
        
        add(mainPanel);
    }
    
    // Reuse helper methods (would be better in a common utility class)
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
    
    private void handleSendMoney() {
        String phoneNumber = phoneNumberField.getText().trim();
        String amount = amountField.getText().trim();
    
        if (phoneNumber.isEmpty() || amount.isEmpty()) {
            showError("Please enter both mobile number and amount");
        } else if (!isValidPhoneNumber(phoneNumber)) {
            showError("Please enter a valid mobile number");
        } else if (!isValidAmount(amount)) {
            showError("Please enter a valid amount");
        } else if (phoneNumber.equals(currentAccountNumber)) {
            showError("Cannot send money to yourself");
        } else if (!DataAccount.userExists(phoneNumber)) {
            showError("Recipient account not found");
        } else {
            double amountValue = Double.parseDouble(amount);
            
            if (DataAccount.sendMoney(currentAccountNumber, phoneNumber, amountValue)) {
                errorLabel.setVisible(false);
                JOptionPane.showMessageDialog(this, 
                    "Successfully sent $" + String.format("%,.2f", amountValue) + " to " + phoneNumber, 
                    "Transaction Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                returnToHome();
            } else {
                showError("Transaction failed. Check your balance.");
            }
        }
    }
    
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10,15}");
    }
    
    private boolean isValidAmount(String amount) {
        try {
            return Double.parseDouble(amount) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void returnToHome() {
        dispose();
        new HomeAccount(currentAccountNumber).setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SendMoney("09912345678").setVisible(true));
    }   
}