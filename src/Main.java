import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String serverName = "OMAR-GAAFAR";
        String instanceName = ""; // If connecting to the default instance, leave it empty
        String portNumber = "1433"; // If using the default port, leave it as "1433"
        String dbName = "Omar";
        String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        //String url = "jdbc:sqlserver://" + serverName + "\\" + instanceName + ":" + portNumber + ";integratedSecurity=false;encrypt=false;trustServerCertificate=true;";
        try {
            Connection connection = DriverManager.getConnection(url);
            // Connection successful, you can now use the connection object

            String sql = "SELECT * from scores";
            Statement statment = connection.createStatement();
            ResultSet result = statment.executeQuery(sql);
            int i = 0;
            while(result.next())
            {
                i++;
                    String name = result.getString("Team");
                System.out.println(name);
            }
            System.out.println(i + " rows");
            // Perform your database operations...
            System.out.println("Connected to SQL Server");
            // Remember to close the connection when done
            connection.close();
        } catch (SQLException e) {
            // Handle any errors that occurred during the connection
            e.printStackTrace();
        }
    }
}