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
public class Login extends JDialog{
    private JTextField tfName;
    private JPanel loginPanel;
    private JButton btnSignIn;
    private JButton btnCancel;
    private JPasswordField tfPassword;
    public Person user = null;
    private String personType;
    public Login(JFrame parent , String personType) // user or admin
    {
        super(parent);
        this.personType= personType;
        setTitle("User/Admin Sign In");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);

        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Name = tfName.getText();
                String Password = tfPassword.getText();
                if(Name.isEmpty() || Password.isEmpty())
                {
                    JOptionPane.showMessageDialog(Login.this,"Please enter all fields","Try Again",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                user = getAuthentication(Name, Password , personType);
                if(user!= null)
                {
                    System.out.println("Successfully Logged In");
                }
                else
                {
                    JOptionPane.showMessageDialog(Login.this, "Name or Password Invalid" ,
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login.this.dispose();
            }
        });
        setVisible(true);
    }


    private Person getAuthentication(String name , String password ,String personType)
    {
        List<String> names = new ArrayList<>();
        names = sqlGetServerNameDatabaseName();

        String serverName = names.get(0);
        String dbName = names.get(1);

        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        try {
            Connection connection = DriverManager.getConnection(url);
            String sql;
           if(personType.equals("USER"))
               sql = "Select * from [user] where Name= ? AND password = ?";
           else
               sql = "Select * from [Admin] where Name= ? AND password = ?";

            PreparedStatement statment = connection.prepareStatement(sql);
            statment.setString(1,name);
            statment.setString(2,password);
            ResultSet resultSet = statment.executeQuery();
            if(resultSet.next()) {
            user = new Person();
                if(personType.equals("USER"))
                    user.ID = resultSet.getInt("UserID");
                else
                    user.ID = resultSet.getInt("AdminID");
                user.Name = resultSet.getString("Name");
                user.Password = resultSet.getString("Password");
                user.Email = resultSet.getString("Email");
            }
            LoggedInUser loggedInUser ;
            LoggedInAdmin loggedInAdmin;
            if(user!= null)
            {
                this.dispose();
                if(personType.equals("USER"))
                    loggedInUser = new LoggedInUser(user);
                else
                     loggedInAdmin = new LoggedInAdmin(user);




            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    public static void main(String[] args) {
        Login l = new Login(null , "USER");
        Person User = l.user;
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
