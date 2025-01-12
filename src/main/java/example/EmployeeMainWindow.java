package com.ahmethadziaganovic.example;

import javax.swing.*;
import java.awt.*;

public class EmployeeMainWindow extends JFrame {
    private JLabel nameLabel;
    private JLabel salaryLabel;
    private JButton logoutButton;

    public EmployeeMainWindow(String username, Double salary) {
        // Postavljanje izgleda prozora za zaposlenika
        setTitle("Glavni prozor Zaposlenika");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Ispis korisničkog imena i plate
        nameLabel = new JLabel("Zaposlenik: " + username);
        salaryLabel = new JLabel("Plata: " + salary);
        add(nameLabel);
        add(salaryLabel);

        // Dugme za izlaz
        logoutButton = new JButton("Odjava");
        add(logoutButton);

        // Akcija za dugme odjava
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginWindow();  // Otvara prozor za prijavu
        });

        // Postavljanje prozora vidljivim
        setVisible(true);
    }

    public static void main(String[] args) {
        new EmployeeMainWindow("employee", 2000.0);
    }
}
