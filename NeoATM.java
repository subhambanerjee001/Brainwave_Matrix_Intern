import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class NeoATM extends JFrame implements ActionListener {
    private JTextField userField, pinField, inputField;
    private JTextArea displayArea;
    private JButton loginBtn, registerBtn, checkBalanceBtn, depositBtn, withdrawBtn, historyBtn, logoutBtn;
    private JPanel loginPanel, atmPanel;
    private String currentUser;
    private Map<String, User> users = new HashMap<>();
    private final String FILE = "users.db";

    public NeoATM() {
        setTitle("NeoATM - Secure Java ATM");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadUsers();
        setupLoginPanel();
        setVisible(true);
    }

    private void setupLoginPanel() {
        loginPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(80, 200, 100, 200));

        userField = new JTextField();
        pinField = new JTextField();
        loginBtn = new JButton("Login");
        registerBtn = new JButton("Register");

        loginBtn.addActionListener(this);
        registerBtn.addActionListener(this);

        loginPanel.add(new JLabel("User ID:"));
        loginPanel.add(userField);
        loginPanel.add(new JLabel("PIN:"));
        loginPanel.add(pinField);
        loginPanel.add(loginBtn);
        loginPanel.add(registerBtn);

        getContentPane().removeAll();
        getContentPane().add(loginPanel);
        revalidate();
        repaint();
    }

    private void showATMPane() {
        atmPanel = new JPanel(new BorderLayout(10, 10));
        atmPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        displayArea = new JTextArea();
        displayArea.setEditable(false);

        inputField = new JTextField();

        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        checkBalanceBtn = new JButton("Check Balance");
        depositBtn = new JButton("Deposit");
        withdrawBtn = new JButton("Withdraw");
        historyBtn = new JButton("History");
        logoutBtn = new JButton("Logout");

        for (JButton b : new JButton[]{checkBalanceBtn, depositBtn, withdrawBtn, historyBtn, logoutBtn}) {
            b.addActionListener(this);
            btnPanel.add(b);
        }

        atmPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        atmPanel.add(inputField, BorderLayout.SOUTH);
        atmPanel.add(btnPanel, BorderLayout.EAST);
        displayArea.setText("Welcome, " + currentUser + "!\nChoose an option.");

        getContentPane().removeAll();
        getContentPane().add(atmPanel);
        revalidate();
        repaint();
    }

    private void loadUsers() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length >= 3) {
                    String[] hist = p[2].split("\\|");
                    User u = new User(p[1], Double.parseDouble(p[0]));
                    u.history.addAll(Arrays.asList(hist));
                    users.put(p[1], u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (Map.Entry<String, User> e : users.entrySet()) {
                User u = e.getValue();
                String histStr = String.join("|", u.history);
                pw.println(u.balance + ";" + e.getKey() + ";" + histStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            if (cmd.equals("Login") || cmd.equals("Register")) {
                String uid = userField.getText().trim();
                String pin = pinField.getText().trim();
                if (uid.isEmpty() || pin.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Fields cannot be empty.");
                    return;
                }

                if (cmd.equals("Login")) {
                    if (!users.containsKey(pin)) {
                        JOptionPane.showMessageDialog(this, "User not found.");
                        return;
                    }
                    currentUser = pin;
                    showATMPane();
                } else { // Register
                    if (users.containsKey(pin)) {
                        JOptionPane.showMessageDialog(this, "User already exists.");
                        return;
                    }
                    users.put(pin, new User(pin, 0));
                    saveUsers();
                    JOptionPane.showMessageDialog(this, "Registered. Please login.");
                }
            } else if (cmd.equals("Check Balance")) {
                displayArea.setText("Balance: ₹" + users.get(currentUser).balance);
            } else if (cmd.equals("Deposit") || cmd.equals("Withdraw")) {
                double amt = Double.parseDouble(inputField.getText().trim());
                User u = users.get(currentUser);
                if (amt <= 0) {
                    displayArea.setText("Invalid amount.");
                    return;
                }
                if (cmd.equals("Withdraw") && amt > u.balance) {
                    displayArea.setText("Insufficient balance.");
                    return;
                }
                if (cmd.equals("Deposit")) {
                    u.balance += amt;
                } else {
                    u.balance -= amt;
                }
                String log = cmd + " ₹" + amt + " @ " + new Date();
                u.history.add(log);
                saveUsers();
                displayArea.setText(log + "\nNew Balance: ₹" + u.balance);
            } else if (cmd.equals("History")) {
                List<String> hist = users.get(currentUser).history;
                displayArea.setText("Transaction History:\n" + String.join("\n", hist));
            } else if (cmd.equals("Logout")) {
                currentUser = null;
                setupLoginPanel(); 
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            displayArea.setText("Error: " + ex.getMessage());
        }
        inputField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NeoATM::new);
    }
}

class User {
    String pin;
    double balance;
    List<String> history = new ArrayList<>();

    User(String pin, double bal) {
        this.pin = pin;
        this.balance = bal;
    }
}
