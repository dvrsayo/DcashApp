package Main;
import java.util.*;
import java.text.SimpleDateFormat;

public class DataAccount {
    private static final Map<String, String> credentials = new HashMap<>();
    private static final Map<String, Double> balances = new HashMap<>();
    private static final Map<String, List<Transaction>> transactions = new HashMap<>();
    
    public static class Transaction {
        public final String type, phone, date, other;
        public final double amount;
        
        public Transaction(String type, String phone, double amount, String date, String other) {
            this.type = type;
            this.phone = phone;
            this.amount = amount;
            this.date = date;
            this.other = other;
        }
        
        @Override
        public String toString() {
            return String.format("%s,%s,%.2f,%s,%s", type, phone, amount, date, other);
        }
    }
    
    static {
        // Sample data
        String[] users = {"09912345678", "09123456789"};
        String[] passwords = {"admin123", "test123"};
        double[] balances = {5000.0, 8000.0};
        
        for (int i = 0; i < users.length; i++) {
            credentials.put(users[i], passwords[i]);
            DataAccount.balances.put(users[i], balances[i]);
            transactions.put(users[i], new ArrayList<>());
        }
        
        addTransaction("09912345678", new Transaction("SEND", "09412345678", 45.67, "10/02/25", "09412345678"));
        addTransaction("09123456789", new Transaction("RECEIVE", "09722345678", 2500.00, "10/02/25", "09722345678"));
    }
    
    // User methods
    public static boolean addUser(String phone, String password) {
        if (credentials.containsKey(phone)) return false;
        
        credentials.put(phone, password);
        balances.put(phone, 0.0);
        transactions.put(phone, new ArrayList<>());
        return true;
    }
    
    public static boolean verifyUser(String phone, String password) {
        return credentials.get(phone) != null && credentials.get(phone).equals(password);
    }
    
    public static boolean userExists(String phone) {
        return credentials.containsKey(phone);
    }
    
    // Balance methods
    public static double getBalance(String phone) {
        return balances.getOrDefault(phone, 0.0);
    }
    
    public static void updateBalance(String phone, double newBalance) {
        balances.put(phone, newBalance);
    }
    
    // Transaction methods
    public static boolean sendMoney(String from, String to, double amount) {
        if (!userExists(from) || !userExists(to) || getBalance(from) < amount) {
            return false;
        }
        
        updateBalance(from, getBalance(from) - amount);
        updateBalance(to, getBalance(to) + amount);
        
        String date = new SimpleDateFormat("MM/dd/yy").format(new Date());
        addTransaction(from, new Transaction("SEND", from, amount, date, to));
        addTransaction(to, new Transaction("RECEIVE", to, amount, date, from));
        
        return true;
    }
    
    public static boolean cashIn(String phone, double amount, String method) {
        if (!userExists(phone) || amount <= 0) return false;
        
        updateBalance(phone, getBalance(phone) + amount);
        
        String date = new SimpleDateFormat("MM/dd/yy").format(new Date());
        addTransaction(phone, new Transaction("CASH_IN", phone, amount, date, method));
        
        return true;
    }
    
    private static void addTransaction(String phone, Transaction t) {
        transactions.get(phone).add(0, t);
    }
    
    public static List<Transaction> getTransactions(String phone) {
        return transactions.getOrDefault(phone, new ArrayList<>());
    }
}