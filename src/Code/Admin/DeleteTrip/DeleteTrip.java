package Code.Admin.DeleteTrip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteTrip extends JFrame{
    private JTextField tfTripID;
    private JButton cancelButton;
    private JButton deleteTripButton;
    private JPanel DeleteTripPanel;

    public DeleteTrip ()
    {
        setTitle("Deleting Trip");
        setContentPane(DeleteTripPanel);
        setMinimumSize(new Dimension(350,274));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteTrip.this.dispose();
            }
        };
        cancelButton.addActionListener(listener);
        deleteTripButton.addActionListener(listener);
        deleteTripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tripID = tfTripID.getText();
                if(tripID.matches("\\d+"))
                {
                    deleteTrain(Integer.parseInt(tripID));
                }
                else
                {
                    JOptionPane.showMessageDialog(DeleteTrip.this,"TripID must be a number");
                }
            }


        });
    }

    private void deleteTrain(int tripID) {
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);
        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "Delete from Trip where TripID = ? ";
            PreparedStatement statment = connection.prepareStatement(sql);
            statment.setInt(1,tripID);
            statment.executeUpdate();
            JOptionPane.showMessageDialog(this,"Trip was successfully Deleted");
            this.dispose();
            connection.close();
        } catch (SQLException e) {
            // Handle any errors that occurred during the connection
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        DeleteTrip deleteTrip = new DeleteTrip();
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
