package com.ahmethadziaganovic.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.List;
import java.util.ArrayList;

public class MainWindow {

    private static JTextArea textArea;
    private static JButton btnAddEmployee, btnViewEmployees, btnAddProject, btnAddBonus, btnViewSalaries;

    public static void main(String[] args) {
        // Kreiraj JFrame (glavni prozor)
        JFrame frame = new JFrame("Employee Management System");

        // Kreiraj gumbe
        btnAddEmployee = new JButton("Dodaj zaposlenika");
        btnViewEmployees = new JButton("Pregledaj zaposlenike");
        btnAddProject = new JButton("Dodaj projekt zaposleniku");
        btnAddBonus = new JButton("Dodaj bonus zaposleniku");
        btnViewSalaries = new JButton("Pregledaj stare plate");

        // Kreiraj tekstualno područje za prikaz zaposlenika
        textArea = new JTextArea(15, 40);
        textArea.setEditable(false); // Onemogući uređivanje

        // Kreiraj labelu
        JLabel label = new JLabel("Dobrodošli u Employee Management System!");

        // Postavi layout
        frame.setLayout(new FlowLayout());

        // Dodaj komponente u prozor
        frame.add(label);
        frame.add(btnAddEmployee);
        frame.add(btnViewEmployees);
        frame.add(btnAddProject);
        frame.add(btnAddBonus);
        frame.add(btnViewSalaries);
        frame.add(new JScrollPane(textArea)); // Dodaj ScrollPane za textArea

        // Postavi veličinu prozora
        frame.setSize(600, 500);

        // Postavi operaciju zatvaranja
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Akcija za gumb "Dodaj zaposlenika"
        btnAddEmployee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddEmployeeWindow(); // Otvori prozor za unos zaposlenika
            }
        });

        // Akcija za gumb "Pregledaj zaposlenike"
        btnViewEmployees.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayEmployees(); // Prikaz zaposlenika u textArea
            }
        });

        // Akcija za gumb "Dodaj projekt zaposleniku"
        btnAddProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddProjectWindow(); // Otvori prozor za dodavanje projekta
            }
        });

        // Akcija za gumb "Dodaj bonus zaposleniku"
        btnAddBonus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddBonusWindow(); // Otvori prozor za dodavanje bonusa
            }
        });

        // Akcija za gumb "Pregledaj stare plate"
        btnViewSalaries.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayEmployeeSalaries(); // Prikaz starih plata
            }
        });

        // Prikazivanje prozora
        frame.setVisible(true);
    }

    // Funkcija za otvoriti prozor za unos zaposlenika
    private static void openAddEmployeeWindow() {
        new AddEmployeeWindow(); // Otvori novi prozor za unos zaposlenika
    }

    // Funkcija za prikaz zaposlenika u textArea
    private static void displayEmployees() {
        textArea.setText(""); // Očisti prethodni prikaz
        MongoCollection<Document> collection = getDatabase().getCollection("employees");

        // Dohvati sve zaposlenike iz baze i prikaži u textArea
        for (Document doc : collection.find()) {
            textArea.append("Zaposlenik: " + doc.getString("name") + " - Plata: " + doc.getDouble("salary") + "\n");
        }
    }

    // Funkcija za povezivanje s MongoDB bazom
    private static MongoDatabase getDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        return mongoClient.getDatabase("ems_db"); // Naziv tvoje baze podataka
    }

    // Funkcija za otvoriti prozor za dodavanje projekta zaposleniku
    private static void openAddProjectWindow() {
        new AddProjectWindow(); // Otvori novi prozor za dodavanje projekta
    }

    // Funkcija za otvoriti prozor za dodavanje bonusa zaposleniku
    private static void openAddBonusWindow() {
        new AddBonusWindow(); // Otvori novi prozor za dodavanje bonusa
    }

    // Funkcija za prikaz starih plata zaposlenika
    private static void displayEmployeeSalaries() {
        textArea.setText(""); // Očisti prethodni prikaz
        MongoCollection<Document> collection = getDatabase().getCollection("employees");

        // Dohvati sve zaposlenike i njihove stare plate
        for (Document doc : collection.find()) {
            List<Document> salaryHistory = (List<Document>) doc.get("salaryHistory");
            textArea.append("Zaposlenik: " + doc.getString("name") + "\n");
            for (Document salaryRecord : salaryHistory) {
                textArea.append("   Stara plata: " + salaryRecord.getDouble("salary") + " - Datum: " + salaryRecord.getString("date") + "\n");
            }
        }
    }
}

// Drugi prozor za unos zaposlenika
class AddEmployeeWindow extends JFrame {
    private JTextField nameField, salaryField;
    private JButton btnSave;

    public AddEmployeeWindow() {
        setTitle("Unos zaposlenika");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // Unos imena zaposlenika
        add(new JLabel("Ime:"));
        nameField = new JTextField(20);
        add(nameField);

        // Unos plate zaposlenika
        add(new JLabel("Plata:"));
        salaryField = new JTextField(20);
        add(salaryField);

        // Spremi dugme
        btnSave = new JButton("Spremi");
        add(btnSave);

        // Akcija kad klikneš na "Spremi"
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEmployee();
            }
        });

        setVisible(true); // Postavi prozor kao vidljiv
    }

    // Funkcija za provjeru valjanosti unosa
    private boolean isValidInput(String name, String salary) {
        // Provjeri da ime nije prazno
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ime zaposlenika ne smije biti prazno.");
            return false;
        }

        // Provjeri da plata nije prazna i da je broj
        try {
            Double.parseDouble(salary);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Unesite ispravnu vrijednost za platu.");
            return false;
        }

        // Provjeri da plata nije negativna
        double salaryValue = Double.parseDouble(salary);
        if (salaryValue < 0) {
            JOptionPane.showMessageDialog(this, "Plata ne može biti negativna.");
            return false;
        }

        return true;
    }

    // Spremanje zaposlenika u MongoDB
    private void saveEmployee() {
        String name = nameField.getText();
        String salary = salaryField.getText();

        // Provjeri valjanost unosa
        if (!isValidInput(name, salary)) {
            return;
        }

        double salaryValue = Double.parseDouble(salary);

        // Spremanje zaposlenika u MongoDB
        MongoDatabase db = getDatabase();
        MongoCollection<Document> collection = db.getCollection("employees");

        Document employee = new Document("name", name)
                .append("salary", salaryValue)
                .append("projects", new ArrayList<>()) // Dodano polje za projekte
                .append("bonus", 0.0) // Dodano polje za bonus
                .append("salaryHistory", new ArrayList<>()); // Dodano polje za povijest plata

        // Dodaj zaposlenika u kolekciju
        try {
            collection.insertOne(employee);
            JOptionPane.showMessageDialog(this, "Zaposlenik je uspješno dodan!");
            dispose(); // Zatvori ovaj prozor nakon spremanja
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom spremanja zaposlenika.");
        }
    }

    // Povezivanje s MongoDB
    private MongoDatabase getDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        return mongoClient.getDatabase("ems_db"); // Spajanje na tvoju bazu
    }
}

// Prozor za dodavanje projekta zaposleniku
class AddProjectWindow extends JFrame {
    private JTextField employeeNameField, projectNameField;
    private JButton btnSave;

    public AddProjectWindow() {
        setTitle("Dodaj projekt zaposleniku");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // Unos imena zaposlenika
        add(new JLabel("Ime zaposlenika:"));
        employeeNameField = new JTextField(20);
        add(employeeNameField);

        // Unos naziva projekta
        add(new JLabel("Naziv projekta:"));
        projectNameField = new JTextField(20);
        add(projectNameField);

        // Spremi dugme
        btnSave = new JButton("Spremi");
        add(btnSave);

        // Akcija kad klikneš na "Spremi"
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveProject();
            }
        });

        setVisible(true); // Postavi prozor kao vidljiv
    }

    private void saveProject() {
        String employeeName = employeeNameField.getText();
        String projectName = projectNameField.getText();

        // Provjeri valjanost unosa
        if (employeeName == null || employeeName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ime zaposlenika ne smije biti prazno.");
            return;
        }

        // Povezivanje s bazom
        MongoDatabase db = getDatabase();
        MongoCollection<Document> collection = db.getCollection("employees");

        // Pronadji zaposlenika u bazi
        Document employee = collection.find(new Document("name", employeeName)).first();

        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Zaposlenik s tim imenom nije pronađen.");
            return;
        }

        // Dodaj projekt zaposleniku
        List<String> projects = (List<String>) employee.get("projects");
        projects.add(projectName);

        // Ažuriraj zaposlenika u bazi
        collection.updateOne(new Document("name", employeeName),
                new Document("$set", new Document("projects", projects)));

        JOptionPane.showMessageDialog(this, "Projekt je uspješno dodan zaposleniku!");
        dispose(); // Zatvori prozor
    }

    // Povezivanje s MongoDB
    private MongoDatabase getDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        return mongoClient.getDatabase("ems_db");
    }
}

// Prozor za dodavanje bonusa zaposleniku
class AddBonusWindow extends JFrame {
    private JTextField employeeNameField, bonusField;
    private JButton btnSave;

    public AddBonusWindow() {
        setTitle("Dodaj bonus zaposleniku");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // Unos imena zaposlenika
        add(new JLabel("Ime zaposlenika:"));
        employeeNameField = new JTextField(20);
        add(employeeNameField);

        // Unos bonusa
        add(new JLabel("Bonus:"));
        bonusField = new JTextField(20);
        add(bonusField);

        // Spremi dugme
        btnSave = new JButton("Spremi");
        add(btnSave);

        // Akcija kad klikneš na "Spremi"
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveBonus();
            }
        });

        setVisible(true); // Postavi prozor kao vidljiv
    }

    private void saveBonus() {
        String employeeName = employeeNameField.getText();
        String bonusText = bonusField.getText();

        // Provjera unosa bonusa
        if (employeeName == null || employeeName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ime zaposlenika ne smije biti prazno.");
            return;
        }

        if (bonusText == null || bonusText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bonus ne smije biti prazan.");
            return;
        }

        double bonus = Double.parseDouble(bonusText);

        // Povezivanje s bazom
        MongoDatabase db = getDatabase();
        MongoCollection<Document> collection = db.getCollection("employees");

        // Pronadji zaposlenika
        Document employee = collection.find(new Document("name", employeeName)).first();

        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Zaposlenik s tim imenom nije pronađen.");
            return;
        }

        // Ažuriraj bonus zaposlenika
        double currentSalary = employee.getDouble("salary");
        double newSalary = currentSalary + bonus;

        // Spremi novi bonus i ažuriraj platu
        List<Document> salaryHistory = (List<Document>) employee.get("salaryHistory");
        salaryHistory.add(new Document("salary", currentSalary).append("date", java.time.LocalDate.now().toString()));

        collection.updateOne(new Document("name", employeeName),
                new Document("$set", new Document("salary", newSalary)
                        .append("bonus", bonus)
                        .append("salaryHistory", salaryHistory)));

        JOptionPane.showMessageDialog(this, "Bonus je uspješno dodan zaposleniku!");
        dispose(); // Zatvori prozor
    }

    // Povezivanje s MongoDB
    private MongoDatabase getDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        return mongoClient.getDatabase("ems_db");
    }
}
