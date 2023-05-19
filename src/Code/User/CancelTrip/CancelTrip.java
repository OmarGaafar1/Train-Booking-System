package Code.User.CancelTrip;

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

public class CancelTrip extends JFrame {


    private JPanel cancelPanel;
    private JButton cancelButton;
    private JButton cancelTripButton;
    private JTextField tfBookingID;
    private int userID;
    public CancelTrip(int UserID)
    {
        userID= UserID;
        setTitle("Cancel A Trip");
        setContentPane(cancelPanel);
        setMinimumSize(new Dimension(450,254));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CancelTrip.this.dispose();

            }
        });
        cancelTripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strBookingID = tfBookingID.getText();
                boolean isDigitsOnly = strBookingID.matches("\\d+");
                if(!isDigitsOnly)
                {
                    JOptionPane.showMessageDialog(CancelTrip.this,"Invalid Input" , "Input is Invalid" , JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int intBookingID = Integer.parseInt(strBookingID);
                if(checkBooking(intBookingID))
                    cancelBooking(intBookingID);
                else
                    JOptionPane.showMessageDialog(CancelTrip.this,"This User has no Trip with this BookingID-- Check list of user trips");
            }


        });
    }
    private boolean checkBooking(int bookingID) {
        int rowCount=0;
        List<String> names = new ArrayList<>();

        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);
        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);

            String sql = "SELECT COUNT(*) FROM BOOKING WHERE UserID = ? and BOOKINGID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userID);
            statement.setInt(2, bookingID);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            rowCount = resultSet.getInt(1); // Get the count of rows
            connection.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "This information is already registered");
            // Handle any errors that occurred during the connection

            e.printStackTrace();
        }
        if(rowCount > 0)
            return true;
        else
            return false;

    }

    private void cancelBooking(int bookingID)
    {
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);
        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "Delete from BOOKING where BOOKINGID = ? ";
            PreparedStatement statment = connection.prepareStatement(sql);
            statment.setInt(1,bookingID);
            statment.executeUpdate();
            JOptionPane.showMessageDialog(this,"Trip was successfully canceled");
            this.dispose();
            connection.close();
        } catch (SQLException e) {
            // Handle any errors that occurred during the connection
            e.printStackTrace();
        }
    }
    public List<String> sqlGetServerNameDatabaseName()
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
        CancelTrip c = new CancelTrip(1);
    }
}
