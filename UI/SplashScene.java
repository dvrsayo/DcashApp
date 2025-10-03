package UI;
import javax.swing.*;
import java.awt.*;

public class SplashScene extends JFrame {
    private Timer timer;

    public SplashScene() {
        setupFrame();
        setupUI();
        setupTimer();
    }
    
    private void setupFrame() {
        setTitle("Splash Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(370, 800);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    private void setupUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.decode("#343434"));
        mainPanel.setLayout(null);
        
        JPanel titlePanel = createTitlePanel();
        mainPanel.add(titlePanel);
        add(mainPanel);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBounds(90, 333, 172, 150);
        panel.setBackground(Color.decode("#343434"));
        
        JLabel heading = createLabel("Dcash", Font.BOLD, 53);
        JLabel caption = createLabel("  Banking make simple", Font.PLAIN, 14);
        
        panel.add(heading);
        panel.add(Box.createVerticalStrut(5));
        panel.add(caption);
        
        return panel;
    }
    
    private JLabel createLabel(String text, int style, int size) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", style, size));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private void setupTimer() {
        timer = new Timer(3000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                transitionToLogin();
            }
        });
        timer.setRepeats(false);
    }
    
    public void showSplash() {
        setVisible(true);
        timer.start();
    }
    
    private void transitionToLogin() {
        dispose();
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SplashScene().showSplash());
    }
}