package com.ahmethadziaganovic.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.*;
import org.bson.Document;

public class UpdateSalaryWindow extends JFrame {
    private JTextField employeeNameField, newSalaryField;
    private JButton btnSave;

    public UpdateSalaryWindow() {
        setTitle("Update Employee Salary");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // Polje za unos imena zaposlenika
        add(new JLabel("Employee Name:"));
        employeeNameField = new JTextField(20);
        add(employeeNameField);

        // Polje za unos nove plate
        add(new JLabel("New Salary:"));
        newSalaryField = new JTextField(20);
        add(newSalaryField);

        // Dugme za spremanje promjena
        btnSave = new JButton("Save");
        add(btnSave);

        // Dodavanje akcije na dugme
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSalary();
            }
        });

        // Postavljanje prozora kao vidljivog
        setVisible(true);
    }

    // Funkcija za ažuriranje plate zaposlenika
    private void updateSalary() {
        String employeeName = employeeNameField.getText();
        String newSalary = newSalaryField.getText();

        // Provjera ispravnosti unosa
        if (employeeName.isEmpty() || newSalary.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }

        // Spremanje nove plate zaposlenika u bazu
        MongoDatabase db = getDatabase();
        MongoCollection<Document> collection = db.getCollection("employees");

        double salaryValue = Double.parseDouble(newSalary);
        Document employee = collection.find(new Document("name", employeeName)).first();

        if (employee != null) {
            // Ako zaposleni postoji, ažuriraj njegovu platu
            collection.updateOne(new Document("name", employeeName),
                    new Document("$set", new Document("salary", salaryValue)));
            JOptionPane.showMessageDialog(this, "Salary updated successfully!");
        } else {
            // Ako zaposleni nije pronađen
            JOptionPane.showMessageDialog(this, "Employee not found!");
        }

        dispose();  // Zatvori prozor nakon ažuriranja
    }

    // Funkcija za povezivanje sa bazom podataka
    private MongoDatabase getDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        return mongoClient.getDatabase("ems_db");  // Tvoje ime baze podataka
    }
}
