import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class LoggedInAdmin extends JFrame {
    private JPanel adminPanel;
    private JButton deleteTripButton;
    private JTextArea taDisplayer;
    private JButton addTripButton;
    private JButton addTrainButton;
    private JButton deleteTrainButton;
    private JLabel tfID;
    private JLabel tfName;
    private JLabel tfEmail;
    private JButton showTripsButton;
    private JButton showTrainsButton;
    private JButton signOutButton;
    private Person admin;
    public LoggedInAdmin(Person Admin)
    {
        admin = new Person(Admin);

        tfEmail.setText(admin.Email);
        tfName.setText(admin.Name);
        tfID.setText("" +admin.ID);
        setTitle("Hello admin " + Admin.Name);
        setContentPane(adminPanel);
        setMinimumSize(new Dimension(1050,474));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        showTripsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTrips();
            }
        });
        showTrainsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTrains();
            }


        });
        addTripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddTrip addTrip = new AddTrip();
            }
        });
        addTrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddTrain addTrain = new AddTrain();
            }
        });
        deleteTrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteTrain deleteTrain= new DeleteTrain();
            }
        });
        deleteTripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteTrip deleteTrip = new DeleteTrip();
            }
        });
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggedInAdmin.this.dispose();
                Welcome l = new Welcome();
            }
        });
    }

    private void showTrains() {
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);
        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);
            StringBuilder trainsText = new StringBuilder();
            trainsText.append("TrainID").append("\t")
                    .append("Name").append("\t")
                    .append("Capacity").append("\n");

            String sql = "select * from Train";
            PreparedStatement statment = connection.prepareStatement(sql);
            ResultSet resultSet = statment.executeQuery();
            while(resultSet.next())
            {
                String TrainID = resultSet.getString("TRAINID");
                String Name = resultSet.getString("Name");
                String Capacity = resultSet.getString("Capacity");
                trainsText.append(TrainID).append("\t")
                        .append(Name).append("\t")
                        .append(Capacity).append("\n");
            }
            taDisplayer.setText(trainsText.toString());
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "This information is already registered");
            // Handle any errors that occurred during the connection

            e.printStackTrace();
        }
    }
    private void showTrips(){
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);
        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);
            StringBuilder tripsText = new StringBuilder();
            tripsText.append("TripID").append("\t")
                    .append("TrainID").append("\t")
                    .append("Date").append("\t")
                    .append("Source").append("\t")
                    .append("Destination").append("\t")
                    .append("arrivalTime").append("\t\t")
                    .append("departureTime").append("\n");

            String sql = "select * from Trip";
            PreparedStatement statment = connection.prepareStatement(sql);
            ResultSet resultSet = statment.executeQuery();
            while(resultSet.next())
            {
                String TripID = resultSet.getString("TRIPID");
                String TrainID = resultSet.getString("TRAINID");
                String Date = resultSet.getString("Date");
                String Source = resultSet.getString("Source");
                String Destination = resultSet.getString("Destination");
                String arrivalTime = resultSet.getString("arrivalTime");
                String departureTime = resultSet.getString("departureTime");
                tripsText.append(TripID).append("\t")
                        .append(TrainID).append("\t")
                        .append(Date).append("\t")
                        .append(Source).append("\t")
                        .append(Destination).append("\t")
                        .append(arrivalTime).append("\t")
                        .append(departureTime).append("\n");
            }
            taDisplayer.setText(tripsText.toString());
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "This information is already registered");
            // Handle any errors that occurred during the connection

            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        Person admin = new Person();
        admin.Name = "Omar";
        admin.ID = 1;
        admin.Email= "omar_gaafar@gmail.com";
        admin.Password = "omar";
        LoggedInAdmin l = new LoggedInAdmin(admin);
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
}
