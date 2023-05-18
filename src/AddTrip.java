import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddTrip extends JFrame {
    private JTextField tfTrainId ; // Limit the field to 2 characters


    private JButton addTripButton;
    private JButton cancelButton;
    private JPanel tripPanel;
    private JTextField tfDate1;
    private JTextField tfDate2;
    private JTextField tfDate3;
    private JTextField tfSource;
    private JTextField tfDestination;
    private JTextField tfArriv1;
    private JTextField tfArriv2;
    private JTextField tfArriv3;
    private JTextField tfDepa1;
    private JTextField tfDepa2;
    private JTextField tfDepa3;

    public AddTrip()
    {
        setTitle("Adding new Trip");
        setContentPane(tripPanel);
        setMinimumSize(new Dimension(1050,674));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        addTripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTrip();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddTrip.this.dispose();
            }
        });
    }


    private void createTrip()
    {
        String trainID = tfTrainId.getText();
        String Date1 = tfDate1.getText();
        String Date2 = tfDate2.getText();
        String Date3 = tfDate3.getText();
        String Source = tfSource.getText();
        String Destination = tfDestination.getText();
        String Arriv1 = tfArriv1.getText();
        String Arriv2 = tfArriv2.getText();
        String Arriv3 = tfArriv3.getText();
        String Depar1 = tfDepa1.getText();
        String Depar2 = tfDepa2.getText();
        String Depar3 = tfDepa3.getText();
        String checking = Date1+Date2+Date3+Arriv1+Arriv2+Arriv3+Depar1+Depar2+Depar3+trainID;
        boolean checkDigits = checking.matches("\\d+");

        if(trainID.length() == 0 ||Date1.length()!= 4 ||Date2.length() != 2 || Date3.length()!=2 || Arriv1.length()!=2
          || Arriv2.length() != 2 || Arriv3.length()!= 2 || Depar1.length()!=2 || Depar2.length()!=2 ||
            Depar3.length()!=2 || !checkDigits)
        {
            JOptionPane.showMessageDialog(this,"Date or Time Entered is invalid, Make sure that all field are 2 digits");
            return;
        }
        if(checkTrainID(Integer.parseInt(trainID)))
        {
            List<String> names = new ArrayList<>();
            names = sqlGetServerNameDatabaseName();
            String serverName = names.get(0);
            String dbName = names.get(1);
            String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
            try {
                Connection connection = DriverManager.getConnection(url);

                String sql = "INSERT INTO [Trip] (TripID,TrainID, Source,Destination , ArrivalTime,DepartureTime, Date)" +
                        "VALUES (?,?, ?, ?,?,?,?)";
                int ID = getNextTripID();
                PreparedStatement statment_1 = connection.prepareStatement(sql);
                statment_1.setInt(1,ID);
                statment_1.setInt(2, Integer.parseInt(trainID));
                statment_1.setString(3,Source);
                statment_1.setString(4,Destination);
                statment_1.setString(5,Arriv1+":"+Arriv2+":"+Arriv3);
                statment_1.setString(6,Depar1+":"+Depar2+":"+Depar3);
                statment_1.setString(7,Date1+"-"+Date2+"-"+Date3);
                statment_1.executeUpdate();
                connection.close();
                JOptionPane.showMessageDialog(this, "Successfully added Trip");
                this.dispose();

            } catch (SQLException e) {
                // Handle any errors that occurred during the connection

                e.printStackTrace();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this,"There is no train with the following ID");
        }

    }

    private boolean checkTrainID(int trainID)
    {
        int rowCount=0;
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);
        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);

            String sql = "SELECT COUNT(*) FROM Train WHERE TrainID = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, trainID);

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
    public static void main(String[] args) {
        AddTrip addTrip = new AddTrip();
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

    private int getNextTripID() {
        String filename = "src/Generators/TripID";
        int nextTripID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            nextTripID = Integer.parseInt(line);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file: " + filename);
        }

        nextTripID++;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(String.valueOf(nextTripID));
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
        }

        return nextTripID;
    }
}
