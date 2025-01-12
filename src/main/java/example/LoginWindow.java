package com.ahmethadziaganovic.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.HashMap;

public class LoginWindow extends JFrame {
    private JTextField imeField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private static final Map<String, String> userCredentials = new HashMap<>();
    private static final Map<String, Double> userSalaries = new HashMap<>();

    static {
        // Dodajemo korisnike i njihove lozinke
        userCredentials.put("hamza", "hamza123");
        userCredentials.put("muhamed", "muhamed123");
        userCredentials.put("ahmet", "rizva123"); // admin sa lozinkom rizva123
        userCredentials.put("amir", "amir123");
        userCredentials.put("kenan", "kenan123");
        userCredentials.put("emir", "emir123");
        userCredentials.put("ademir", "ademir123");
        userCredentials.put("eldar", "eldar123");
        userCredentials.put("ismar", "ismar123");
        userCredentials.put("amar", "amar123");
        userCredentials.put("adnan", "adnan123");
        userCredentials.put("ibrahim", "ibrahim123");
        userCredentials.put("isnan", "isnan123");
        userCredentials.put("ernes", "ernes123"); // Ernes kao menadžer
        userCredentials.put("muhamed_nur", "muhamednur123");
        userCredentials.put("galib", "galib123");
        userCredentials.put("huse", "huse123");
        userCredentials.put("tarik", "tarik123");
        userCredentials.put("mustafa", "mustafa123");
        userCredentials.put("ermin", "ermin123");
        userCredentials.put("dzenad", "dzenad123");

        // Dodajemo plate zaposlenika
        userSalaries.put("hamza", 5000.0);
        userSalaries.put("muhamed", 5000.0);
        userSalaries.put("ahmet", 3000.0);
        userSalaries.put("amir", 2000.0);
        userSalaries.put("kenan", 7000.0);
        userSalaries.put("emir", 5000.0);
        userSalaries.put("ademir", 5000.0);
        userSalaries.put("eldar", 2000.0);
        userSalaries.put("ismar", 2200.0);
        userSalaries.put("amar", 700.0);
        userSalaries.put("adnan", 2500.0);
        userSalaries.put("ibrahim", 2450.0);
        userSalaries.put("isnan", 2750.0);
        userSalaries.put("ernes", 5500.0); // Plata Ernesa
        userSalaries.put("muhamed_nur", 3500.0);
        userSalaries.put("galib", 2600.0);
        userSalaries.put("huse", 1500.0);
        userSalaries.put("tarik", 2200.0);
        userSalaries.put("mustafa", 1750.0);
        userSalaries.put("ermin", 2800.0);
        userSalaries.put("dzenad", 2250.0);
    }

    public LoginWindow() {
        setTitle("Prozor za prijavu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Ime:"));
        imeField = new JTextField(20);
        add(imeField);

        add(new JLabel("Lozinka:"));
        passwordField = new JPasswordField(20);
        add(passwordField);

        loginButton = new JButton("Prijavi se");
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(imeField.getText(), new String(passwordField.getPassword()));
            }
        });

        setVisible(true);
        pack();
    }

    private void login(String ime, String password) {
        if (userCredentials.containsKey(ime) && userCredentials.get(ime).equals(password)) {
            if ("ahmet".equals(ime)) {
                new AdminMainWindow(ime);
            } else if ("hamza".equals(ime) || "ernes".equals(ime)) {
                new ManagerMainWindow(ime);  // Za Hamzu i Ernesa otvara ManagerMainWindow
            } else {
                new EmployeeMainWindow(ime, userSalaries.get(ime));  // Za ostale je Employee
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Pogrešni podaci za prijavu!");
        }
    }

    public static Map<String, String> getUserCredentials() {
        return userCredentials;
    }

    public static Map<String, Double> getUserSalaries() {
        return userSalaries;
    }

    public static void main(String[] args) {
        new LoginWindow();
    }
}
