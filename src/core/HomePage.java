package core;

import CountryInfo_p.data_access.RESTCountriesAPI;
import CountryInfo_p.interface_adapter.country_info.CountryInfoController;
import CountryInfo_p.interface_adapter.country_info.CountryInfoPresenter;
import CountryInfo_p.use_case.country_info.CountryInfoInteractor;
import CountryInfo_p.view.CountryInfoView;
import Flights.FlightSearch;
import Flights.FlightSearchPanel;
import weather.ui.WeatherView;

import javax.swing.*;
import java.awt.*;

import TravelTips.TravelTipsPage;
import core.*;

public class HomePage extends JFrame {
    public HomePage(String username) {
        super("Home - Travel Planner");

        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------------- Top Bar ----------------
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0, 102, 204));
        topBar.setPreferredSize(new Dimension(800, 50));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logoLabel = new JLabel("✈ Travel Planner");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        topBar.add(logoLabel, BorderLayout.WEST);

        // Right-side controls (Logs + Logout)
        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightControls.setOpaque(false);

        // “My Recent Logs” opens the window for THIS user
        JButton logsButton = new JButton("My Recent Logs");
        logsButton.setFocusPainted(false);
        logsButton.addActionListener(e -> {
            FirebaseInitializer.initialize();       // safe to call multiple times
            new RecentLogsWindow(username);
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(new Color(0, 102, 204));
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutButton.addActionListener(e -> {
            dispose();
            Main.showLoginScreen();
        });

        rightControls.add(logsButton);
        rightControls.add(logoutButton);
        topBar.add(rightControls, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // ---------------- Main Panel ----------------
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(135, 206, 250),
                        0, getHeight(), new Color(173, 216, 230));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel welcome = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcome.setForeground(Color.DARK_GRAY);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(welcome);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        buttonPanel.setOpaque(false);

        JButton planTripButton = styledButton("Plan a Trip");
        JButton viewTripsButton = styledButton("Check Weather");
        JButton exploreButton = styledButton("Explore Destinations");

        buttonPanel.add(planTripButton);
        buttonPanel.add(viewTripsButton);
        buttonPanel.add(exploreButton);

        mainPanel.add(buttonPanel);
        add(mainPanel, BorderLayout.CENTER);

        // ---------------- Explore Destinations (Country Info) ----------------
        exploreButton.addActionListener(e -> {
            CountryInfoView countryInfoView = new CountryInfoView();
            CountryInfoPresenter presenter = new CountryInfoPresenter(countryInfoView);
            RESTCountriesAPI dataAccess = new RESTCountriesAPI();
            CountryInfoInteractor interactor = new CountryInfoInteractor(dataAccess, presenter);
            CountryInfoController controller = new CountryInfoController(interactor);
            countryInfoView.setController(controller);
            countryInfoView.setVisible(true);
        });
        viewTripsButton.addActionListener(e -> {
                WeatherView weatherView = new WeatherView();
                weatherView.launch(username);
                dispose();
                }

        );
        // ---------------- Travel Tips % Warnings Button Creation ----------------
        JButton travelTipsButton = new JButton("Travel Tips & Warnings");
        travelTipsButton.setPreferredSize(new Dimension(460, 150)); // Wider width
        travelTipsButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        travelTipsButton.setFocusPainted(false);
        travelTipsButton.setBackground(new Color(204, 229, 255));
        travelTipsButton.setForeground(new Color(0, 102, 204));
        travelTipsButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerWrapper.setOpaque(false);
        centerWrapper.add(travelTipsButton);

        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(new Color(173, 216, 230));
        bottomButtonPanel.setLayout(new BoxLayout(bottomButtonPanel, BoxLayout.Y_AXIS));
        bottomButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        bottomButtonPanel.add(centerWrapper);

        add(bottomButtonPanel, BorderLayout.SOUTH);

        // ---------------- Travel Tips % Warnings Button Functionality  ----------------
        travelTipsButton.addActionListener(e -> {
            dispose();
            TravelTipsPage.showPanel();
        });

        // ---------------- Plan a Trip (Flights) ----------------
        planTripButton.addActionListener(e -> {
            dispose();
            // Make username available to the flights feature before showing UI
            FlightSearch.setUsername(username);
            FlightSearchPanel.showPanel();
        });
    }

    // creates a clean blue-outline button
    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0, 102, 204));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }
}
