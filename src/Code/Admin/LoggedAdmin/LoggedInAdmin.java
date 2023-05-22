package Code.Admin.LoggedAdmin;

import Code.Admin.AddTrain.AddTrain;
import Code.Admin.AddTrip.AddTrip;
import Code.Admin.DeleteTrain.DeleteTrain;
import Code.Admin.DeleteTrip.DeleteTrip;
import Code.PublicInterface.Person;
import Code.PublicInterface.WelcomScreen.Welcome;

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
    private JButton showReportButton;
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
        showReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }


        });
    }
    private void generateReport() {
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);
        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);
            StringBuilder Report = new StringBuilder();
            Report.append("\t----Generating Reports about users----\nWe have found the following Statistics\n");
            String sql = "SELECT\n" +
                    "  (SELECT COUNT(*) FROM [USER]) AS UserCount,\n" +
                    "  (SELECT COUNT(*) FROM BOOKING) AS BookedTripsCount,\n" +
                    "  (SELECT COUNT(*) FROM TRAIN) AS TrainCount,\n" +
                    "  (SELECT COUNT(*) FROM SEATs) AS SeatCount,\n" +
                    "  (select count(*) from Trip) as TripsCount";
            Report.append("UserCount").append("\t")
                    .append("BookedTripsCount").append("\t")
                    .append("TripsCount").append('\t')
                    .append("TrainCount").append("\t")
                    .append("SeatCount").append("\t").append("\n");
            PreparedStatement statment = connection.prepareStatement(sql);
            ResultSet resultSet = statment.executeQuery();

            while(resultSet.next())
            {
                String UserCount = resultSet.getString("UserCount");
                String BookedTripsCount = resultSet.getString("BookedTripsCount");
                String TrainCount = resultSet.getString("TrainCount");
                String SeatCount = resultSet.getString("SeatCount");
                String TripsCount = resultSet.getString("TripsCount");
                Report.append(UserCount).append("\t")
                        .append(BookedTripsCount).append("\t\t   ")
                        .append(TripsCount).append('\t')
                        .append(TrainCount).append("\t")
                        .append(SeatCount).append("\t").append("\n");
            }




            Report.append("UserID").append("\t")
                    .append("Name").append("\t")
                    .append("Seats").append('\t')
                    .append("BookingID").append("\t")
                    .append("TripIDs").append("\t").append('\n');


            sql = "SELECT\n" +
                    "  U.UserID,\n" +
                    "  U.NAME,\n" +
                    "  COUNT(B.BookingID) AS Seats,\n" +
                    "  CASE\n" +
                    "    WHEN COUNT(B.BookingID) > 0 THEN STRING_AGG(CAST(B.BookingID AS VARCHAR), ', ')\n" +
                    "    ELSE 'null'\n" +
                    "  END AS BookingID,\n" +
                    "  CASE\n" +
                    "    WHEN B.BookingID > 0 THEN STRING_AGG(CAST(B.TripID AS VARCHAR), ', ')\n" +
                    "    ELSE 'null'\n" +
                    "  END AS TripID\n" +
                    "FROM\n" +
                    "  [USER] U\n" +
                    "  LEFT JOIN BOOKING B ON U.UserID = B.UserID\n" +
                    "GROUP BY\n" +
                    "  U.UserID, U.NAME, B.BookingID\n" +
                    "ORDER BY\n" +
                    "  U.UserID;\n";
            statment = connection.prepareStatement(sql);
            resultSet = statment.executeQuery();
            while(resultSet.next())
            {
                String UserID = resultSet.getString("UserID");
                String Name = resultSet.getString("Name");
                String Seats = resultSet.getString("Seats");
                String BookingID = resultSet.getString("BookingID");
                String TripID = resultSet.getString("TripID");
                Report.append(UserID).append("\t")
                        .append(Name).append("\t")
                        .append(Seats).append('\t')
                        .append(BookingID).append("\t")
                        .append(TripID).append("\t").append("\n");
            }
            connection.close();
            taDisplayer.setText(Report.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "This information is already registered");
            // Handle any errors that occurred during the connection

            e.printStackTrace();
        }
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
