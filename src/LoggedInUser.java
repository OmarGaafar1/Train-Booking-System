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

public class LoggedInUser extends JFrame {
    public Person user;
    private JPanel LoggedPanel;
    private JLabel lID;
    private JLabel lName;
    private JLabel lEmail;
    private JButton updateProfileButton;
    private JLabel lChangeName;
    private JTextField tfChangeName;
    private JTextField tfChangeEmail;
    private JTextField tfChangePassword;
    private JLabel lChangeEmail;
    private JLabel lChangePassword;
    private JButton btnUpdate;
    private JTextArea taTrips;
    private JButton bookButton;
    private JButton bookATripButton;
    private JButton showMyBooksButton;
    private JButton btnHide;
    private JButton cancelTripsButton;

    public LoggedInUser(Person U)
    {
        this.user = new Person();
        this.user.Name = U.Name;
        this.user.Email = U.Email;
        this.user.Password = U.Password;
        this.user.ID = U.ID;

        setTitle("Hello " + user.Name);
        setContentPane(LoggedPanel);
        setMinimumSize(new Dimension(1050,474));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        lName.setText(user.Name);
        lID.setText(""+user.ID);
        lEmail.setText(user.Email);
        setUpdateToInvisible();

        updateProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUpdateToVisible();
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfile();
            }


        });
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTrips();
            }


        });
        bookATripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookTrip b = new BookTrip(user.ID);
            }


        });

        showMyBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBooks();
            }


        });
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUpdateToInvisible();
            }
        };
        btnUpdate.addActionListener(listener);
        btnHide.addActionListener(listener);
        updateProfileButton.addActionListener(listener);
        bookButton.addActionListener(listener);
        bookATripButton.addActionListener(listener);
        showMyBooksButton.addActionListener(listener);
        cancelTripsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CancelTrip cancelTrip = new CancelTrip(user.ID);
            }
        });
    }
    private void showBooks() {
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);

        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);
            StringBuilder tripsText = new StringBuilder();
            String sql = "select count(*) \n" +
                    "from BOOKING as B ,Trip as T\n" +
                    "where USERID = ? and B.TRIPID = T.TripID";
            PreparedStatement statment = connection.prepareStatement(sql);
            statment.setInt(1,user.ID);
            ResultSet resultSet = statment.executeQuery();
            int rowCount = 0;
            if (resultSet.next()) {
                rowCount = resultSet.getInt(1);
            } // this will return no of rows
            if(rowCount> 0)
            {
                sql = "select * \n" +
                        "from BOOKING as B ,Trip as T\n" +
                        "where USERID = ? And B.TRIPID = T.TripID";
                statment = connection.prepareStatement(sql);
                statment.setInt(1,user.ID);
                resultSet = statment.executeQuery();
                tripsText.append("\t\t\t ----Your Trips---\n").append("BookingID   ");
                tripsText.append("TripID").append("\t")
                        .append("TrainID").append("\t")
                        .append("Date").append("\t")
                        .append("Source").append("\t")
                        .append("Destination").append("\t")
                        .append("arrivalTime").append("\t\t")
                        .append("departureTime").append("\n");

                while(resultSet.next())
                {
                    String BookingID = resultSet.getString("BookingID");
                    String TripID = resultSet.getString("TRIPID");
                    String TrainID = resultSet.getString("TRAINID");
                    String Date = resultSet.getString("Date");
                    String Source = resultSet.getString("Source");
                    String Destination = resultSet.getString("Destination");
                    String arrivalTime = resultSet.getString("arrivalTime");
                    String departureTime = resultSet.getString("departureTime");
                    tripsText.append(BookingID).append("\t")
                            .append(TripID).append("\t")
                            .append(TrainID).append("\t")
                            .append(Date).append("\t")
                            .append(Source).append("\t")
                            .append(Destination).append("\t")
                            .append(arrivalTime).append("\t")
                            .append(departureTime).append("\n");
                }
            }
           else
            {
                tripsText.append("You have not got any bookings yet :(");
            }
            taTrips.setText(tripsText.toString());

            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "This information is already registered");
            // Handle any errors that occurred during the connection

            e.printStackTrace();
        }
    }

    private void showTrips() {
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
            taTrips.setText(tripsText.toString());
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "This information is already registered");
            // Handle any errors that occurred during the connection

            e.printStackTrace();
        }
    }
    private void updateProfile() {
        String Email = tfChangeEmail.getText();
        String Name= tfChangeName.getText();
        String Password = tfChangePassword.getText();
        if(!Email.isEmpty() || !Name.isEmpty() || !Password.isEmpty())
        {
            if(Email.isEmpty())
                Email = user.Email;
            if(Name.isEmpty())
                Name = user.Name;
            if(Password.isEmpty())
                Password= user.Password;
            List<String> names = new ArrayList<>();
            names = sqlGetServerNameDatabaseName();

            String serverName = names.get(0);
            String dbName = names.get(1);
            String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
            try {
                Connection connection = DriverManager.getConnection(url);

                String sql = "update [user] set Name=? , Email=? , Password=? where userID=?";
                PreparedStatement statment_1 = connection.prepareStatement(sql);
                statment_1.setString(1,Name);
                statment_1.setString(2,Email);
                statment_1.setString(3,Password);
                statment_1.setInt(4,user.ID);
                statment_1.executeUpdate();
                connection.close();
                JOptionPane.showMessageDialog(this, "Successfully Updated, Please Login  Again");
                this.dispose();
                Login l = new Login(null, "USER");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "This information is already registered");
                // Handle any errors that occurred during the connection

                e.printStackTrace();
            }
        }
        else
        {
           JOptionPane.showMessageDialog(LoggedInUser.this,
                   "Please Enter at least one field to update"
                        , "Fields are empty" , JOptionPane.ERROR_MESSAGE);
           return;
        }
    }
    public static void main(String[] args) {
        Person user = new Person();
        user.Name = "Omar";
        user.ID = 1;
        user.Email= "omar_gaafar@gmail.com";
        user.Password = "omar";
        LoggedInUser l = new LoggedInUser(user);
        System.out.println(12);

    }
    private void setUpdateToInvisible()
    {
        tfChangeEmail.setVisible(false);
        tfChangeName.setVisible(false);
        tfChangePassword.setVisible(false);
        lChangeName.setVisible(false);
        lChangePassword.setVisible(false);
        lChangeEmail.setVisible(false);
        btnUpdate.setVisible(false);
        btnHide.setVisible(false);
    }

    private void setUpdateToVisible()
    {
        tfChangeEmail.setVisible(true);
        tfChangeName.setVisible(true);
        tfChangePassword.setVisible(true);
        lChangeName.setVisible(true);
        lChangePassword.setVisible(true);
        lChangeEmail.setVisible(true);
        btnUpdate.setVisible(true);
        btnHide.setVisible(true);
    }
    private static List<String> sqlGetServerNameDatabaseName()
    {
        List<String> s = new ArrayList<>();
        String filename = "Database";

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
