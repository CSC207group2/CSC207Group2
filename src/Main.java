import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {


    public static void main(String[] args){


        JFrame frame = new JFrame("Travel Planner Login");
        frame.setSize(300,200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "login method");
            }
        });

        JPanel usernameTextEntry = new JPanel();
        usernameTextEntry.add(new JLabel("Username"));
        usernameTextEntry.add(new JTextField(20));

        JPanel passwordTextEntry = new JPanel();
        passwordTextEntry.add(new JLabel("Password"));
        passwordTextEntry.add(new JTextField(20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(new JButton("Sign Up"));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(usernameTextEntry);
        panel.add(passwordTextEntry);
        panel.add(buttonPanel);
        frame.setContentPane(panel);

        JPanel main = new JPanel();


        //frame.getContentPane().add(panel);

        frame.setVisible(true);

    }
}
