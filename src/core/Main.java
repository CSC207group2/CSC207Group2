package core;

import logs.interface_adapter.FirebaseLogin;
import logs.interface_adapter.LabelTextPanel;

import javax.swing.*;
import java.util.HashMap;

public class Main {
    private static final HashMap<String, String> users = new HashMap<>();
    private static String username;

    public static String getUsername() {
        return username;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Travel Planner Login");
        frame.setSize(500, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");
        JButton forgotPasswordButton = new JButton("Forgot Password");
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new LabelTextPanel(new JLabel("Username:"), usernameField));
        panel.add(new LabelTextPanel(new JLabel("Password:"), passwordField));

        JPanel buttons = new JPanel();
        buttons.add(loginButton);
        buttons.add(signupButton);
        buttons.add(forgotPasswordButton);
        panel.add(buttons);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

        // Login logic
        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            username = user;

            if (FirebaseLogin.login(user, pass)) {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                frame.dispose();
                new HomePage(user).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.");
            }
        });

        // Sign-up logic
        signupButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            HashMap<String, String> currentUsers = FirebaseLogin.loadUsers();
            if (currentUsers.containsKey(user)) {
                JOptionPane.showMessageDialog(frame, "User already exists.");
            } else {
                FirebaseLogin.addUser(user, pass);
                JOptionPane.showMessageDialog(frame, "Sign-up successful! You can now log in.");
            }
        });

        // Forgot password logic
        forgotPasswordButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Please contact support to reset your password.");
        });
    }
    public static void showLoginScreen() {
        main(null);
    }
}
