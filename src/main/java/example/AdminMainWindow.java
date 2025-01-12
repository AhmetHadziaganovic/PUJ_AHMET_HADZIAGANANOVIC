package com.ahmethadziaganovic.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class AdminMainWindow extends JFrame {
    private String adminName;
    private JTextArea employeesListArea;
    private JTextField employeeNameField;
    private JButton updateSalaryButton;
    private JButton addEmployeeButton;  // Dugme za dodavanje zaposlenika
    private JButton promoteToManagerButton;
    private JButton addBonusButton;
    private JButton logoutButton; // Dugme za odjavu
    private static final Map<String, Double> userSalaries = LoginWindow.getUserSalaries();
    private static final Map<String, String> userCredentials = LoginWindow.getUserCredentials();

    public AdminMainWindow(String adminName) {
        this.adminName = adminName;

        setTitle("Prozor admina");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Dugme za pregled zaposlenika
        JButton viewEmployeesButton = new JButton("Pogledaj zaposlenike");
        add(viewEmployeesButton);

        employeesListArea = new JTextArea(15, 40);
        employeesListArea.setEditable(false);
        add(new JScrollPane(employeesListArea));

        // Unos za ime zaposlenika za promenu plate ili druge akcije
        add(new JLabel("Ime zaposlenika:"));
        employeeNameField = new JTextField(20);
        add(employeeNameField);

        // Dugme za promenu plate
        updateSalaryButton = new JButton("Promijeni platu");
        add(updateSalaryButton);

        // Dugme za dodavanje zaposlenika
        addEmployeeButton = new JButton("Dodaj zaposlenika");
        add(addEmployeeButton);

        // Dugme za promociju zaposlenika u menadžera
        promoteToManagerButton = new JButton("Promoviši u menadžera");
        add(promoteToManagerButton);

        // Dugme za dodavanje/oduzimanje bonusa
        addBonusButton = new JButton("Dodaj/Skini Bonus");
        add(addBonusButton);

        // Dugme za odjavu
        logoutButton = new JButton("Odjavi se");
        add(logoutButton);

        // Akcije dugmadi
        viewEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEmployeeList();
            }
        });

        updateSalaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSalary();
            }
        });

        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        promoteToManagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promoteToManager();
            }
        });

        addBonusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrRemoveBonus();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        setVisible(true);
        pack();
    }

    private void showEmployeeList() {
        StringBuilder employeeList = new StringBuilder();
        for (Map.Entry<String, Double> entry : userSalaries.entrySet()) {
            employeeList.append("Zaposlenik: " + entry.getKey() + " - Plata: " + entry.getValue() + "\n");
        }
        employeesListArea.setText(employeeList.toString());
    }

    private void updateSalary() {
        String employeeName = employeeNameField.getText().trim();
        String newSalaryText = JOptionPane.showInputDialog(this, "Unesite novu platu:");

        if (employeeName.isEmpty() || newSalaryText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Molimo unesite ime zaposlenika i novu platu.");
            return;
        }

        try {
            double newSalary = Double.parseDouble(newSalaryText);
            if (userSalaries.containsKey(employeeName)) {
                userSalaries.put(employeeName, newSalary);
                JOptionPane.showMessageDialog(this, "Plata za " + employeeName + " uspešno promenjena.");
                showEmployeeList();
            } else {
                JOptionPane.showMessageDialog(this, "Zaposlenik sa tim imenom nije pronađen.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Molimo unesite validnu vrednost za platu.");
        }
    }

    private void addEmployee() {
        String employeeName = JOptionPane.showInputDialog(this, "Unesite ime i prezime zaposlenika:");
        if (employeeName == null || employeeName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ime zaposlenika nije uneseno.");
            return;
        }

        String salaryText = JOptionPane.showInputDialog(this, "Unesite platu za zaposlenika " + employeeName + ":");
        if (salaryText == null || salaryText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plata zaposlenika nije unesena.");
            return;
        }

        try {
            double salary = Double.parseDouble(salaryText);
            if (salary > 0) {
                userSalaries.put(employeeName, salary);
                userCredentials.put(employeeName, "employee123");
                JOptionPane.showMessageDialog(this, "Zaposlenik " + employeeName + " je uspešno dodat sa platom " + salary);
                showEmployeeList();
            } else {
                JOptionPane.showMessageDialog(this, "Plata mora biti veća od 0.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Molimo unesite validnu platu.");
        }
    }

    private void promoteToManager() {
        String employeeName = employeeNameField.getText().trim();

        if (employeeName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Molimo unesite ime zaposlenika.");
            return;
        }

        if (userCredentials.containsKey(employeeName) && !employeeName.equals(adminName)) {
            userCredentials.put(employeeName, "manager123");
            JOptionPane.showMessageDialog(this, "Zaposlenik " + employeeName + " je promovisano u menadžera.");
        } else {
            JOptionPane.showMessageDialog(this, "Zaposlenik sa tim imenom nije pronađen ili je već menadžer.");
        }
    }

    private void addOrRemoveBonus() {
        String employeeName = employeeNameField.getText().trim();
        String bonusText = JOptionPane.showInputDialog(this, "Unesite bonus (pozitivan ili negativan broj):");

        if (employeeName.isEmpty() || bonusText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Molimo unesite ime zaposlenika i bonus.");
            return;
        }

        try {
            double bonus = Double.parseDouble(bonusText);

            if (userSalaries.containsKey(employeeName)) {
                double currentSalary = userSalaries.get(employeeName);
                double newSalary = currentSalary + bonus;
                userSalaries.put(employeeName, newSalary);
                JOptionPane.showMessageDialog(this, "Plata za " + employeeName + " je uspešno promenjena.");
                showEmployeeList();
            } else {
                JOptionPane.showMessageDialog(this, "Zaposlenik sa tim imenom nije pronađen.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Molimo unesite validan broj za bonus.");
        }
    }

    private void logout() {
        // Zatvori prozor admina i vrati se na login
        dispose();  // Zatvara prozor admina
        new LoginWindow(); // Otvara prozor za prijavu
    }

    public static void main(String[] args) {
        new AdminMainWindow("ahmet"); // Pokreće aplikaciju sa admin korisnikom "ahmet"
    }
}
