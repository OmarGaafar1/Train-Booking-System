package Code.Admin.AddTrain;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AddTrain extends JFrame{
    private JPanel TrainPanel;
    private JTextField tfCapacity;
    private JTextField tfName;
    private JTextField tfTrainID;
    private JButton cancelButton;
    private JButton addTrainButton;

    public AddTrain()
    {
        setTitle("Adding new Trip");
        setContentPane(TrainPanel);
        setMinimumSize(new Dimension(650,474));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddTrain.this.dispose();
            }
        });
        addTrainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String trainID = tfTrainID.getText();
                String Name = tfName.getText();
                String Capacity = tfCapacity.getText();
                String checking = trainID+Capacity;
                if(!checking.matches("\\d+") || trainID.length() == 0 || Name.length() == 0 || Capacity.length() == 0)
                {
                    JOptionPane.showMessageDialog(AddTrain.this,"Make sure  that the capacity and Train ID are numeric values");
                    return;
                }
                int ID = Integer.parseInt(trainID);
                if(checkTrainExistince(ID))
                {
                    insertTrain(ID, Name , Capacity);
                }
                else
                {
                    JOptionPane.showMessageDialog(AddTrain.this,"Train ID is already existed");
                    return;
                }

            }
        });
    }

    private void insertTrain(int trainID , String Name , String Capacity)
    {

            List<String> names = new ArrayList<>();
            names = sqlGetServerNameDatabaseName();
            String serverName = names.get(0);
            String dbName = names.get(1);
            String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
            try {
                Connection connection = DriverManager.getConnection(url);

                String sql = "INSERT INTO [Train] (TrainID, Name,Capacity)" +
                        "VALUES (?,?, ?)";
                PreparedStatement statment = connection.prepareStatement(sql);
                statment.setInt(1,trainID);
                statment.setString(2, Name);
                statment.setInt(3, Integer.parseInt(Capacity));
                statment.executeUpdate();
                connection.close();
                JOptionPane.showMessageDialog(this, "Successfully added Train");
                this.dispose();

            } catch (SQLException e) {
                // Handle any errors that occurred during the connection

                e.printStackTrace();
            }
        }

    private boolean checkTrainExistince(int trainID)
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
            return false;
        else
            return true;
    }

    public static void main(String[] args) {
        AddTrain add = new AddTrain();
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
}
