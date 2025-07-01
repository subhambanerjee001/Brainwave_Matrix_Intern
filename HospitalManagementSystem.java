import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

class Patient implements Serializable {
    String name, gender, ehr;
    int age;
    public Patient(String name, int age, String gender, String ehr) {
        this.name = name; this.age = age; this.gender = gender; this.ehr = ehr;
    }
    public String toString() {
        return name + " (Age: " + age + ", Gender: " + gender + ")\nEHR: " + ehr;
    }
}

class Appointment implements Serializable {
    String patientName, doctorName, dateTime;
    public Appointment(String patientName, String doctorName, String dateTime) {
        this.patientName = patientName; this.doctorName = doctorName; this.dateTime = dateTime;
    }
    public String toString() {
        return "Appointment: " + patientName + " with Dr. " + doctorName + " on " + dateTime;
    }
}

class Bill implements Serializable {
    String patientName, details;
    double amount;
    public Bill(String patientName, String details, double amount) {
        this.patientName = patientName; this.details = details; this.amount = amount;
    }
    public String toString() {
        return "Bill for " + patientName + ": " + details + " - ₹" + amount;
    }
}

class InventoryItem implements Serializable {
    String itemName;
    int quantity;
    public InventoryItem(String itemName, int quantity) {
        this.itemName = itemName; this.quantity = quantity;
    }
    public String toString() {
        return itemName + " - Qty: " + quantity;
    }
}

class Staff implements Serializable {
    String name, role;
    public Staff(String name, String role) {
        this.name = name; this.role = role;
    }
    public String toString() {
        return name + " (" + role + ")";
    }
}

class AppendableObjectOutputStream extends ObjectOutputStream {
    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }
    protected void writeStreamHeader() throws IOException {
        reset();
    }
}

class HospitalSystem extends JFrame {
    public HospitalSystem() {
        setTitle("🏥 Hospital Management System By Subham Banerjee");
        setSize(700, 500);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel title = new JLabel("Welcome to NHSM Hospital Management Dashboard", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBounds(50, 10, 600, 40);
        add(title);

        String[] modules = {
            "Patient Registration", "Appointment Scheduling",
            "Electronic Health Records", "Billing", "Inventory Management", "Staff Management"
        };

        int y = 60;
        for (String module : modules) {
            JButton btn = new JButton(module);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBounds(50, y, 300, 40);
            btn.setBackground(new Color(224, 255, 255));
            btn.setBorder(new LineBorder(new Color(173, 216, 230), 2));
            add(btn);
            y += 50;
            btn.addActionListener(e -> openModule(module));
        }

                JLabel footer = new JLabel("Designed and implemented with Love by Subham Banerjee", JLabel.CENTER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footer.setBounds(50, 370, 600, 30);
        add(footer);

        setVisible(true);
    
    }

    private void openModule(String module) {
        switch (module) {
            case "Patient Registration": patientForm(); break;
            case "Appointment Scheduling": appointmentForm(); break;
            case "Electronic Health Records": ehrViewer(); break;
            case "Billing": billingForm(); break;
            case "Inventory Management": inventoryForm(); break;
            case "Staff Management": staffForm(); break;
        }
    }

    private void patientForm() {
        JFrame frame = new JFrame("Patient Registration");
        frame.setSize(400, 350); frame.setLayout(null); frame.setLocationRelativeTo(null);

        JLabel nameL = new JLabel("Name:"), ageL = new JLabel("Age:"), genderL = new JLabel("Gender:"), ehrL = new JLabel("EHR:");
        JTextField nameF = new JTextField(), ageF = new JTextField(), ehrF = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JButton save = new JButton("Save");

        nameL.setBounds(30, 30, 100, 25); nameF.setBounds(150, 30, 200, 25);
        ageL.setBounds(30, 70, 100, 25); ageF.setBounds(150, 70, 200, 25);
        genderL.setBounds(30, 110, 100, 25); genderBox.setBounds(150, 110, 200, 25);
        ehrL.setBounds(30, 150, 100, 25); ehrF.setBounds(150, 150, 200, 25);
        save.setBounds(150, 200, 100, 30);

        frame.add(nameL); frame.add(nameF); frame.add(ageL); frame.add(ageF);
        frame.add(genderL); frame.add(genderBox); frame.add(ehrL); frame.add(ehrF); frame.add(save);

        save.addActionListener(e -> {
            try (ObjectOutputStream out = new AppendableObjectOutputStream(new FileOutputStream("patients.dat", true))) {
                out.writeObject(new Patient(nameF.getText(), Integer.parseInt(ageF.getText()), genderBox.getSelectedItem().toString(), ehrF.getText()));
                JOptionPane.showMessageDialog(frame, "Patient Registered");
                frame.dispose();
            } catch (Exception ex) { ex.printStackTrace(); }
        });
        frame.setVisible(true);
    }

    private void appointmentForm() {
        JFrame frame = new JFrame("Appointment Scheduling");
        frame.setSize(400, 300); frame.setLayout(null); frame.setLocationRelativeTo(null);

        JLabel patientL = new JLabel("Patient Name:"), doctorL = new JLabel("Doctor:"), dateL = new JLabel("Date & Time:");
        JTextField patientF = new JTextField(), doctorF = new JTextField(), dateF = new JTextField();
        JButton save = new JButton("Schedule");

        patientL.setBounds(30, 30, 100, 25); patientF.setBounds(150, 30, 200, 25);
        doctorL.setBounds(30, 70, 100, 25); doctorF.setBounds(150, 70, 200, 25);
        dateL.setBounds(30, 110, 100, 25); dateF.setBounds(150, 110, 200, 25);
        save.setBounds(150, 160, 100, 30);

        frame.add(patientL); frame.add(patientF); frame.add(doctorL); frame.add(doctorF);
        frame.add(dateL); frame.add(dateF); frame.add(save);

        save.addActionListener(e -> {
            try (ObjectOutputStream out = new AppendableObjectOutputStream(new FileOutputStream("appointments.dat", true))) {
                out.writeObject(new Appointment(patientF.getText(), doctorF.getText(), dateF.getText()));
                JOptionPane.showMessageDialog(frame, "Appointment Scheduled");
                frame.dispose();
            } catch (Exception ex) { ex.printStackTrace(); }
        });
        frame.setVisible(true);
    }

    private void ehrViewer() {
        JFrame frame = new JFrame("Electronic Health Records");
        frame.setSize(500, 400); frame.setLayout(new BorderLayout()); frame.setLocationRelativeTo(null);
        JTextArea area = new JTextArea(); area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("patients.dat"))) {
            while (true) {
                Patient p = (Patient) in.readObject();
                area.append(p.toString() + "\n\n");
            }
        } catch (EOFException ignored) {} catch (Exception ex) { ex.printStackTrace(); }
        frame.add(scroll); frame.setVisible(true);
    }

    private void billingForm() {
        JFrame frame = new JFrame("Billing");
        frame.setSize(400, 350); frame.setLayout(null); frame.setLocationRelativeTo(null);

        JLabel patientL = new JLabel("Patient Name:"), detailsL = new JLabel("Details:"), amountL = new JLabel("Amount:");
        JTextField patientF = new JTextField(), detailsF = new JTextField(), amountF = new JTextField();
        JButton save = new JButton("Generate Bill"), view = new JButton("View Bills");

        patientL.setBounds(30, 30, 100, 25); patientF.setBounds(150, 30, 200, 25);
        detailsL.setBounds(30, 70, 100, 25); detailsF.setBounds(150, 70, 200, 25);
        amountL.setBounds(30, 110, 100, 25); amountF.setBounds(150, 110, 200, 25);
        save.setBounds(150, 150, 120, 30); view.setBounds(150, 190, 120, 30);

        frame.add(patientL); frame.add(patientF); frame.add(detailsL); frame.add(detailsF);
        frame.add(amountL); frame.add(amountF); frame.add(save); frame.add(view);

        save.addActionListener(e -> {
            try (ObjectOutputStream out = new AppendableObjectOutputStream(new FileOutputStream("bills.dat", true))) {
                out.writeObject(new Bill(patientF.getText(), detailsF.getText(), Double.parseDouble(amountF.getText())));
                JOptionPane.showMessageDialog(frame, "Bill Generated");
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        view.addActionListener(e -> {
            JTextArea area = new JTextArea();
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("bills.dat"))) {
                while (true) {
                    Bill bill = (Bill) in.readObject();
                    area.append(bill.toString() + "\n\n");
                }
            } catch (EOFException ignored) {} catch (Exception ex) { ex.printStackTrace(); }
            JOptionPane.showMessageDialog(frame, new JScrollPane(area), "All Bills", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setVisible(true);
    }

    private void inventoryForm() {
        JFrame frame = new JFrame("Inventory Management");
        frame.setSize(400, 300); frame.setLayout(null); frame.setLocationRelativeTo(null);

        JLabel itemL = new JLabel("Item Name:"), qtyL = new JLabel("Quantity:");
        JTextField itemF = new JTextField(), qtyF = new JTextField();
        JButton save = new JButton("Add Item");

        itemL.setBounds(30, 50, 100, 25); itemF.setBounds(150, 50, 200, 25);
        qtyL.setBounds(30, 90, 100, 25); qtyF.setBounds(150, 90, 200, 25);
        save.setBounds(150, 140, 100, 30);

        frame.add(itemL); frame.add(itemF); frame.add(qtyL); frame.add(qtyF); frame.add(save);

        save.addActionListener(e -> {
            try (ObjectOutputStream out = new AppendableObjectOutputStream(new FileOutputStream("inventory.dat", true))) {
                out.writeObject(new InventoryItem(itemF.getText(), Integer.parseInt(qtyF.getText())));
                JOptionPane.showMessageDialog(frame, "Item Added");
                frame.dispose();
            } catch (Exception ex) { ex.printStackTrace(); }
        });
        frame.setVisible(true);
    }

    private void staffForm() {
        JFrame frame = new JFrame("Staff Management");
        frame.setSize(400, 350); frame.setLayout(null); frame.setLocationRelativeTo(null);

        JLabel nameL = new JLabel("Name:"), roleL = new JLabel("Role:");
        JTextField nameF = new JTextField(), roleF = new JTextField();
        JButton save = new JButton("Add Staff"), view = new JButton("View Staff");

        nameL.setBounds(30, 50, 100, 25); nameF.setBounds(150, 50, 200, 25);
        roleL.setBounds(30, 90, 100, 25); roleF.setBounds(150, 90, 200, 25);
        save.setBounds(150, 140, 100, 30); view.setBounds(150, 180, 100, 30);

        frame.add(nameL); frame.add(nameF); frame.add(roleL); frame.add(roleF); frame.add(save); frame.add(view);

        save.addActionListener(e -> {
            try (ObjectOutputStream out = new AppendableObjectOutputStream(new FileOutputStream("staff.dat", true))) {
                out.writeObject(new Staff(nameF.getText(), roleF.getText()));
                JOptionPane.showMessageDialog(frame, "Staff Added");
                frame.dispose();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        view.addActionListener(e -> {
            JTextArea area = new JTextArea();
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("staff.dat"))) {
                while (true) {
                    Staff s = (Staff) in.readObject();
                    area.append(s.toString() + "\n");
                }
            } catch (EOFException ignored) {} catch (Exception ex) { ex.printStackTrace(); }
            JOptionPane.showMessageDialog(frame, new JScrollPane(area), "All Staff", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setVisible(true);
    } 
  
}

public class HospitalManagementSystem {
    public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> new HospitalSystem());
      
    }
}

