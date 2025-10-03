package UI;
import javax.swing.*;
import java.awt.*;
import Main.DataAccount;
import Main.DataAccount.Transaction;
import java.util.List;

public class HomeAccount extends JFrame {
    private String currentAccount;
    private double currentBalance;
    private JLabel balanceLabel;
    private boolean balanceVisible = true;
    private JPanel transactionPanel;
    
    public HomeAccount(String accountNumber) {
        this.currentAccount = accountNumber;
        this.currentBalance = DataAccount.getBalance(accountNumber);
        
        setupFrame();
        setupUI();
    }
    
    private void setupFrame() {
        setTitle("Home Account - " + currentAccount);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(370, 800);
        setLocationRelativeTo(null);
    }
    
    private void setupUI() {
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);
        
        createHeader(mainPanel);
        createBankCard(mainPanel);
        createTransactionButtons(mainPanel);
        createTransactionHistory(mainPanel);
        addLogoutButton(mainPanel);
        
        add(mainPanel);
    }
    
    private void createHeader(JPanel parent) {
        JLabel title = new JLabel("HOME");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.DARK_GRAY);
        title.setBounds(16, 89, 343, 51);
        parent.add(title);
    }
    
    private void createBankCard(JPanel parent) {
        JPanel card = new JPanel(null);
        card.setBounds(37, 140, 288, 179);
        card.setBackground(Color.BLACK);
        
        // Balance display
        balanceLabel = new JLabel("$ " + String.format("%,.2f", currentBalance));
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setBounds(25, 125, 250, 30);
        
        // Toggle visibility button
        JButton toggleBtn = new JButton("👁");
        toggleBtn.setBounds(200, 125, 40, 30);
        toggleBtn.setBackground(Color.BLACK);
        toggleBtn.setForeground(Color.WHITE);
        toggleBtn.setBorder(null);
        toggleBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                toggleBalanceVisibility();
            }
        });
        
        // Account details
        JPanel details = new JPanel(new GridLayout(2, 2, 4, 4));
        details.setBounds(28, 50, 244, 80);
        details.setOpaque(false);
        
        details.add(createDetailLabel("Account Number", 9));
        details.add(createDetailLabel(currentAccount, 10));
        details.add(createDetailLabel("Account Balance", 9));
        
        card.add(balanceLabel);
        card.add(toggleBtn);
        card.add(details);
        parent.add(card);
    }
    
    private JLabel createDetailLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, fontSize));
        label.setForeground(new Color(209, 209, 209));
        return label;
    }
    
    private void toggleBalanceVisibility() {
        if (balanceVisible) {
            balanceLabel.setText("•••••");
        } else {
            balanceLabel.setText("$ " + String.format("%,.2f", currentBalance));
        }
        balanceVisible = !balanceVisible;
    }
    
    private void createTransactionButtons(JPanel parent) {
        // Header
        JLabel header = new JLabel("Transaction");
        header.setBounds(16, 346, 200, 30);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        parent.add(header);
        
        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
        buttons.setBounds(0, 384, 375, 80);
        buttons.setOpaque(false);
        
        buttons.add(createTransactionButton("↑", "Send", new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openSendMoney();
            }
        }));
        buttons.add(createTransactionButton("↓", "Cash In", new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openCashIn();
            }
        }));
        buttons.add(createTransactionButton("≡", "History", new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // TODO: show history or open history view
            }
        }));
        
        parent.add(buttons);
    }
    
    private JPanel createTransactionButton(String icon, String text, java.awt.event.ActionListener action) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JButton btn = new JButton(icon);
        btn.setPreferredSize(new Dimension(48, 48));
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.PLAIN, 18));
        btn.setBorder(null);
        btn.addActionListener(action);
        
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(Color.GRAY);
        
        panel.add(btn, BorderLayout.NORTH);
        panel.add(label, BorderLayout.SOUTH);
        return panel;
    }
    
    private void createTransactionHistory(JPanel parent) {
        // Header
        JLabel header = new JLabel("Transaction History");
        header.setBounds(16, 506, 200, 30);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        parent.add(header);
        
        // Transaction list
        transactionPanel = new JPanel(null);
        transactionPanel.setBounds(12, 540, 330, 222);
        transactionPanel.setBackground(Color.WHITE);
        transactionPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        loadTransactions();
        parent.add(transactionPanel);
    }
    
    private void loadTransactions() {
        transactionPanel.removeAll();
        List<Transaction> transactions = DataAccount.getTransactions(currentAccount);
        
        if (transactions.isEmpty()) {
            JLabel empty = new JLabel("No transactions yet", SwingConstants.CENTER);
            empty.setBounds(0, 80, 330, 30);
            empty.setFont(new Font("Arial", Font.PLAIN, 14));
            empty.setForeground(Color.GRAY);
            transactionPanel.add(empty);
        } else {
            int max = Math.min(transactions.size(), 5);
            for (int i = 0; i < max; i++) {
                addTransactionItem(transactions.get(i), i * 72);
            }
        }
        transactionPanel.revalidate();
        transactionPanel.repaint();
    }
    
    private void addTransactionItem(Transaction t, int y) {
        JPanel item = new JPanel(null);
        item.setBounds(0, y, 330, 72);
        item.setOpaque(false);
        
        // Left side - details
        String displayName = t.type.equals("CASH_IN") ? 
            "Cash In: " + t.other : 
            (t.type.equals("SEND") ? "To: " + t.other : "From: " + t.other);
        
        JLabel title = new JLabel(displayName);
        title.setFont(new Font("Arial", Font.PLAIN, 13));
        title.setBounds(16, 16, 190, 20);
        
        JLabel date = new JLabel(t.date);
        date.setFont(new Font("Arial", Font.PLAIN, 10));
        date.setForeground(Color.GRAY);
        date.setBounds(16, 36, 190, 20);
        
        // Right side - amount
        String amountText = (t.type.equals("SEND") ? "-" : "+") + "$" + String.format("%,.2f", t.amount);
        Color amountColor = t.type.equals("SEND") ? Color.RED : Color.GREEN;
        
        JLabel amount = new JLabel(amountText, SwingConstants.RIGHT);
        amount.setFont(new Font("Arial", Font.PLAIN, 15));
        amount.setForeground(amountColor);
        amount.setBounds(206, 16, 100, 40);
        
        // Divider
        if (y < 288) {
            JSeparator divider = new JSeparator();
            divider.setBounds(16, 71, 298, 1);
            divider.setForeground(Color.LIGHT_GRAY);
            item.add(divider);
        }
        
        item.add(title);
        item.add(date);
        item.add(amount);
        transactionPanel.add(item);
    }
    
    private void addLogoutButton(JPanel parent) {
        JButton logout = new JButton("Logout");
        logout.setBounds(280, 20, 70, 30);
        logout.setBackground(Color.LIGHT_GRAY);
        logout.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                confirmLogout();
            }
        });
        parent.add(logout);
    }
    
    private void confirmLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", "Logout", 
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm().setVisible(true);
        }
    }
    
    private void openSendMoney() {
        dispose();
        new SendMoney(currentAccount).setVisible(true);
    }
    
    private void openCashIn() {
        dispose();
        new CashInMoney(currentAccount).setVisible(true);
    }
    
    public void refreshData() {
        currentBalance = DataAccount.getBalance(currentAccount);
        balanceLabel.setText("$ " + String.format("%,.2f", currentBalance));
        loadTransactions();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HomeAccount("09912345678").setVisible(true);
        });
    }
}