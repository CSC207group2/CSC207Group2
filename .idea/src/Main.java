import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args){
        JFrame frame = new JFrame("Travel Planener Login");
        frame.setSize(300,200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Username"));
        panel.add(new JLabel("Password"));
        panel.add(new JButton("Login"));

        frame.getContentPane().add(panel);

        frame.setVisible(true);

    }
}
