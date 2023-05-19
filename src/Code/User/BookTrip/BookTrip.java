package Code.User.BookTrip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookTrip extends JFrame {
    private JTextField tfTripId;
    private JButton btnBook;
    private JButton btnCancel;
    private JPanel bookPanel;

    private int userID;
    public BookTrip (int ID)
    {
        userID = ID;

        setTitle("BOOK NOW");
        setContentPane(bookPanel);
        setMinimumSize(new Dimension(450,374));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookTrip.this.dispose();
            }
        });
        btnBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strTripID = tfTripId.getText();
                boolean isDigitsOnly = strTripID.matches("\\d+");
                if(!isDigitsOnly)
                {
                    JOptionPane.showMessageDialog(BookTrip.this,"Invalid Input" , "Input is Invalid" , JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int intTripID = Integer.parseInt(strTripID);
                if(checkTrip(intTripID))
                    createTicket(intTripID);
                else
                    JOptionPane.showMessageDialog(BookTrip.this,"Trip was not Found--Check the trip list");

            }
        });
    }

    private boolean checkTrip(int TripID){
        int rowCount=0;
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);


        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);

            String sql = "SELECT COUNT(*) FROM Trip WHERE tripID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, TripID);

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
    private void createTicket(int TripID)
    {

        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);


        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);
            int generateID = getNextBookingID();
            System.out.println(generateID);
            String sql = "insert into  BOOKING (USERID,TRIPID,BOOKINGID) values(?,?,?)";
            PreparedStatement statment = connection.prepareStatement(sql);
            statment.setInt(1,this.userID);
            statment.setInt(2,TripID);
            statment.setInt(3,generateID);
            statment.executeUpdate();
            JOptionPane.showMessageDialog(this,"Successfully Booked");
            this.dispose();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "This information is already registered");
            // Handle any errors that occurred during the connection

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BookTrip b = new BookTrip(1);
    }
    private int getNextBookingID()
    {
        String filename = "src/Generators/BookingsID";
        int nextBookingID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            nextBookingID = Integer.parseInt(line);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file: " + filename);
        }

        nextBookingID++;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(String.valueOf(nextBookingID));
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
        }

        return nextBookingID;
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
