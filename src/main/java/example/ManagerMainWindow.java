package com.ahmethadziaganovic.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class ManagerMainWindow extends JFrame {
    private JButton changeSalaryButton;
    private JButton listEmployeesButton;
    private JButton logoutButton;  // Dugme za odjavu
    private JTextArea employeeListTextArea;

    private String managerName;

    public ManagerMainWindow(String managerName) {
        this.managerName = managerName;

        // Postavljanje izgleda prozora
        setTitle("Menadžer - " + managerName);
        setSize(400, 350);  // Povećajte veličinu prozora
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // Dugme za promjenu plate
        changeSalaryButton = new JButton("Promijeni platu");
        add(changeSalaryButton);

        // Dugme za pregled liste zaposlenika
        listEmployeesButton = new JButton("Pogledaj zaposlenike");
        add(listEmployeesButton);

        // Dugme za odjavu
        logoutButton = new JButton("Odjavi se");
        add(logoutButton);

        // TextArea za prikaz zaposlenika
        employeeListTextArea = new JTextArea(10, 30);
        employeeListTextArea.setEditable(false);
        add(new JScrollPane(employeeListTextArea));

        // Akcija za promjenu plate
        changeSalaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Uzimanje imena zaposlenika i nove plate
                String employeeName = JOptionPane.showInputDialog("Unesite ime zaposlenika:");
                String salaryInput = JOptionPane.showInputDialog("Unesite novu platu:");
                try {
                    double newSalary = Double.parseDouble(salaryInput);
                    // Promena plate zaposlenika
                    if (LoginWindow.getUserSalaries().containsKey(employeeName)) {
                        LoginWindow.getUserSalaries().put(employeeName, newSalary);
                        JOptionPane.showMessageDialog(ManagerMainWindow.this, "Plata za " + employeeName + " je uspješno promijenjena.");
                    } else {
                        JOptionPane.showMessageDialog(ManagerMainWindow.this, "Zaposlenik ne postoji.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ManagerMainWindow.this, "Unesite validnu platu.");
                }
            }
        });

        // Akcija za prikaz liste zaposlenika
        listEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder employeeList = new StringBuilder();
                Map<String, Double> salaries = LoginWindow.getUserSalaries();
                for (Map.Entry<String, Double> entry : salaries.entrySet()) {
                    employeeList.append(entry.getKey()).append(" - Plata: ").append(entry.getValue()).append("\n");
                }
                employeeListTextArea.setText(employeeList.toString());
            }
        });

        // Akcija za odjavu
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Zatvara prozor menadžera
                new LoginWindow();  // Otvara prozor za prijavu
            }
        });

        // Postavljanje prozora vidljivim
        setVisible(true);
        pack();
    }
}
