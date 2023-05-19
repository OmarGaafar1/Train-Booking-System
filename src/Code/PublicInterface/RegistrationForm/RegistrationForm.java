package Code.PublicInterface.RegistrationForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationForm extends JDialog{
    private JLabel label1;
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPassword;
    private JButton registerButton;
    private JButton cancelButton;
    private JPanel registerPanel;
    private JTextField tfPasswordConfirm;
    private JButton registerAsAdminButton;

    public RegistrationForm(JFrame parent)
    {
        super(parent);
        setTitle("Create a new Account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(650,474));
        setModal(true);
        setLocationRelativeTo(parent);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        registerAsAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerAdmin();
            }
        });
        setVisible(true);
    }
    private void registerUser(){
        String Name = tfName.getText();
        String Email = tfEmail.getText();
        String Password = tfPassword.getText();
        String passwordConfirm  = tfPasswordConfirm.getText();
        if(Name.isEmpty() || Email.isEmpty() || Password.isEmpty() || passwordConfirm.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Please enter all fields","Try Again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(!Password.equals(passwordConfirm))
        {
            JOptionPane.showMessageDialog(this,"Password did't match","Try Again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        else
        {
            List<String> names = new ArrayList<>();
            names = sqlGetServerNameDatabaseName();
            String serverName = names.get(0);
            String dbName = names.get(1);
            String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
            try {
                Connection connection = DriverManager.getConnection(url);

                String sql = "INSERT INTO [USER] (userID,Name, email, Password) VALUES (?,?, ?, ?)";
                int ID = getNextUserID();
                System.out.println(ID);
                PreparedStatement statment_1 = connection.prepareStatement(sql);
                statment_1.setInt(1,ID);
                statment_1.setString(2,Name);
                statment_1.setString(3,Email);
                statment_1.setString(4,Password);
                statment_1.executeUpdate();
                connection.close();
                JOptionPane.showMessageDialog(this, "Successfully Registered");
                this.dispose();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "This information is already registered");
                // Handle any errors that occurred during the connection

                e.printStackTrace();
            }
        }

    }

    private void registerAdmin()
    {
        String Name = tfName.getText();
        String Email = tfEmail.getText();
        String Password = tfPassword.getText();
        String passwordConfirm  = tfPasswordConfirm.getText();
        if(Name.isEmpty() || Email.isEmpty() || Password.isEmpty() || passwordConfirm.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Please enter all fields","Try Again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(!Password.equals(passwordConfirm))
        {
            JOptionPane.showMessageDialog(this,"Password did't match","Try Again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        else
        {
            List<String> names = new ArrayList<>();
            names = sqlGetServerNameDatabaseName();

            String serverName = names.get(0);
            String dbName = names.get(1);
            String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
            try {
                Connection connection = DriverManager.getConnection(url);

                String sql = "INSERT INTO [admin] (AdminID,Name, email, Password) VALUES (?,?, ?, ?)";
                int ID = getNextAdminID();
                PreparedStatement statment_1 = connection.prepareStatement(sql);
                statment_1.setInt(1,ID);
                statment_1.setString(2,Name);
                statment_1.setString(3,Email);
                statment_1.setString(4,Password);
                statment_1.executeUpdate();
                connection.close();
                JOptionPane.showMessageDialog(this, "Successfully Registered");
                this.dispose();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "This information is already registered");
                // Handle any errors that occurred during the connection

                e.printStackTrace();
            }
        }

    }
    private int getNextUserID() {
        String filename = "src/Generators/userID";
        int nextUserID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            nextUserID = Integer.parseInt(line);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file: " + filename);
        }

        nextUserID++;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(String.valueOf(nextUserID));
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
        }

        return nextUserID;
    }
    private int getNextAdminID()
    {
        String filename = "src/Generators/adminID";
        int nextUserID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            nextUserID = Integer.parseInt(line);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file: " + filename);
        }

        nextUserID++;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(String.valueOf(nextUserID));
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
        }

        return nextUserID;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
    private static List<String> sqlGetServerNameDatabaseName()
    {
        List<String> s = new ArrayList<>();
        String filename = "Database.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            String line2 = br.readLine();
            s.add(line);
            s.add(line2);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file: " + filename);
        }

        return s;
    }
    public static void main(String[] args) {
        RegistrationForm myFrom =  new RegistrationForm(null);
    }



//    private String getServerName()
//    {
//
//    }


}


