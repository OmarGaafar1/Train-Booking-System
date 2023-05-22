package Code.User.Seats;

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

public class Seats extends JFrame{
    private JTextField tfSeatID;
    private JButton cancelButton;
    private JButton bookButton;
    private JTextArea taSeats;
    private JPanel SeatsPanel;
    private int TripID;
    private int BookingID;
    public Seats (int TripID , int BookingID){
        this.TripID = TripID;
        this.BookingID = BookingID;
        setTitle("Choose Your Seat");
        setContentPane(SeatsPanel);
        setMinimumSize(new Dimension(450,374));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showAvaliableSeats(TripID);
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookSeat(BookingID);
                Seats.this.dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelBooking(BookingID);
                Seats.this.dispose();
            }
        });
    }

    private void bookSeat(int BookingID)
    {
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);


        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);
            String SeatID = tfSeatID.getText();

            String sql = "UPDATE SEATS\n" +
                    "SET BookingID = ?\n" +
                    "WHERE SEATID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, BookingID);
            statement.setInt(2, Integer.parseInt(SeatID));
            statement.executeUpdate();
            connection.close();
            JOptionPane.showMessageDialog(this,"Successfully Booked (:");



        } catch (SQLException h) {
            JOptionPane.showMessageDialog(Seats.this, "Wrong ID entered");
            // Handle any errors that occurred during the connection
            h.printStackTrace();
        }
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
            String sql2 = "update Seats " +
                    "set BOOKINGID = null\n" +
                    "where BOOKINGID=?";
            PreparedStatement statment = connection.prepareStatement(sql);
            PreparedStatement statment2 = connection.prepareStatement(sql2);
            statment.setInt(1,bookingID);
            statment2.setInt(1,bookingID);
            statment2.executeUpdate();
            statment.executeUpdate();

            JOptionPane.showMessageDialog(this,"Trip was canceled");
            this.dispose();
            connection.close();
        } catch (SQLException e) {
            // Handle any errors that occurred during the connection
            e.printStackTrace();
        }
    }
    private void showAvaliableSeats(int tripID)
    {
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);


        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);
            StringBuilder availableSeats = new StringBuilder();
            availableSeats.append("SeatID").append('\t')
                    .append("SeatNumber").append('\t').append('\n');
            String sql = "select seats.SEATID , Seats.SEATNO \n" +
                    "from SEATS , trip \n" +
                    "where seats.TrainID = trip.TrainID and trip.tripID=? and BookingID is null ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, TripID);

            ResultSet resultSet = statement.executeQuery();
            int i = 0 ;
            while (resultSet.next())
            {
                i++;
                String ID = resultSet.getString("SeatID");
                String number = resultSet.getString("SeatNo");
                availableSeats.append(ID).append('\t')
                        .append(number).append('\t').append('\n');
            }
            if(i == 0)
                taSeats.setText("\tNo Seats Avaliable For this Trip :( ");
            else
                taSeats.setText(availableSeats.toString());
            connection.close();


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Something went wrong, contact the  Admin ");
            // Handle any errors that occurred during the connection

            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Seats seats = new Seats(4,14);
    }


    private static java.util.List<String> sqlGetServerNameDatabaseName()
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
