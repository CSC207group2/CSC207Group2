import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args){
        JFrame frame = new JFrame("Travel Planener Login");
        frame.setSize(500,200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new LabelTextPanel(new JLabel("Username:"), new JTextField(15)));
        panel.add(new LabelTextPanel(new JLabel("Passwprd:"), new JTextField(15)));
        JPanel buttons = new JPanel();
        buttons.add(new JButton("Login"));
        buttons.add(new JButton("Sign Up"));
        buttons.add(new JButton("Forgot Password"));
        panel.add(buttons);


        frame.getContentPane().add(panel);
        frame.setVisible(true);

    }
}
