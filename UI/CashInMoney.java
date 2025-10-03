package UI;
import javax.swing.*;
import java.awt.*;
import Main.DataAccount;

public class CashInMoney extends JFrame {
    private static final long serialVersionUID = 1L;
    private String currentAccount;
    private JTextField amountField;
    private JLabel errorLabel;
    private JComboBox<String> bankCombo;
    
    public CashInMoney(String currentAccount) {
        this.currentAccount = currentAccount;
        setupFrame();
        setupUI();
    }
    
    private void setupFrame() {
        setTitle("Cash In Money");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(370, 800);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void setupUI() {
        JPanel main = new JPanel(null);
        main.setBackground(Color.LIGHT_GRAY);
        
        // Title
        JLabel title = new JLabel("Cash In Money");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(16, 89, 343, 51);
        main.add(title);
        
        // Payment method
        main.add(createLabel("Payment Method", 23, 180));
        bankCombo = new JComboBox<>(new String[]{"ZALDCO BANK", "MARTIN BANK", "FC BANK"});
        main.add(createStyledPanel(bankCombo, 23, 200));
        
        // Phone number (read-only)
        main.add(createLabel("Mobile Number", 23, 260));
        JTextField phoneField = new JTextField(currentAccount);
        phoneField.setEditable(false);
        main.add(createStyledPanel(phoneField, 23, 280));
        
        // Amount
        main.add(createLabel("Amount", 23, 340));
        amountField = new JTextField();
        main.add(createStyledPanel(amountField, 23, 360));
        
        // Error message
        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(30, 410, 300, 20);
        errorLabel.setVisible(false);
        main.add(errorLabel);
        
        // Buttons
        JButton cashInBtn = createButton("Cash In", 23, 450);
        cashInBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleCashIn();
            }
        });
        main.add(cashInBtn);
        
        JLabel backLabel = createLinkLabel("Back to Home", 0, 505);
        backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnToHome();
            }
        });
        main.add(backLabel);
        
        add(main);
    }
    
    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 313, 20);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        return label;
    }
    
    private JPanel createStyledPanel(JComponent comp, int x, int y) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(231, 231, 231));
        panel.setBounds(x, y, 313, 44);
        
        if (comp instanceof JTextField) {
            comp.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        }
        comp.setBackground(new Color(231, 231, 231));
        comp.setFont(new Font("Arial", Font.PLAIN, 15));
        
        panel.add(comp, BorderLayout.CENTER);
        return panel;
    }
    
    private JButton createButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 313, 50);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setBorder(null);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private JLabel createLinkLabel(String text, int x, int y) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setBounds(x, y, 356, 34);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return label;
    }
    
    private void handleCashIn() {
        String amountText = amountField.getText().trim();
        
        if (amountText.isEmpty()) {
            showError("Please enter amount");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Please enter a valid amount");
                return;
            }
            
            String bank = (String) bankCombo.getSelectedItem();
            boolean success = DataAccount.cashIn(currentAccount, amount, bank);
            
            if (success) {
                errorLabel.setVisible(false);
                JOptionPane.showMessageDialog(this, 
                    "Successfully cashed in $" + String.format("%,.2f", amount) + 
                    " to " + currentAccount + " via " + bank, 
                    "Transaction Successful", JOptionPane.INFORMATION_MESSAGE);
                
                returnToHome();
            } else {
                showError("Cash in failed. Please try again.");
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid amount");
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void returnToHome() {
        dispose();
        new HomeAccount(currentAccount).setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CashInMoney("09912345678").setVisible(true);
        });
    }
}