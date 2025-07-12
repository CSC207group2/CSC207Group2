import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args){
        JFrame frame = new JFrame("Travel Planner Login");
        frame.setSize(300,200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel usernameTextEntry = new JPanel();
        usernameTextEntry.add(new JLabel("Username"));
        usernameTextEntry.add(new JTextField(20));

        JPanel passwordTextEntry = new JPanel();
        passwordTextEntry.add(new JLabel("Password"));
        passwordTextEntry.add(new JTextField(20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton("Login"));
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
