import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteTrain extends JFrame {
    private JButton deleteTrainButton;
    private JTextField tfTrainId;
    private JButton cancelButton;
    private JPanel deleteTrainPanel;

    public DeleteTrain() {
        setTitle("Deleting Train");
        setContentPane(deleteTrainPanel);
        setMinimumSize(new Dimension(370,274));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteTrain.this.dispose();
            }
        });
        deleteTrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String TrainID = tfTrainId.getText();
                if(TrainID.matches("\\d+"))
                {
                    deleteTrain(Integer.parseInt(TrainID));
                }
                else
                {
                    JOptionPane.showMessageDialog(DeleteTrain.this,"TrainID must be a number");
                }
            }


        });
    }
    private void deleteTrain(int trainID) {
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);
        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql = "Delete from Train where TrainID = ? ";
            PreparedStatement statment = connection.prepareStatement(sql);
            statment.setInt(1,trainID);
            statment.executeUpdate();
            JOptionPane.showMessageDialog(this,"Train was successfully Deleted");
            this.dispose();
            connection.close();
        } catch (SQLException e) {
            // Handle any errors that occurred during the connection
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        DeleteTrain a = new DeleteTrain();
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
