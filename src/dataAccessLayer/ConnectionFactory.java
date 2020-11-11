package dataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**Class that establishes the connection between the app and the local database.
 * @author Stefan Ciuprina
 * @version 1.0
 * @since 1.0
 */

public class ConnectionFactory {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/order_management";
    private static final String USER = "root";
    private static final String PASS = "root";

    /**The driver connection is being created when any method of this class is first called.
     */
    private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**The constructor is private since an instance of this class is only created once and in this class
     * (the singleInstance private static variable).
     */

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**Logs in the local database and returns; it is used by the getConnection method.
     * @return a new connection of type Connection.
     */
    private Connection createConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DBURL, USER, PASS);
        } catch(java.sql.SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    /**Creates a connection based on the static ConenctionFactory object previously (automatically) created.
     * @return a new connection of type Connection
     */

    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**Closes the Connection given as parameter.
     */

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch(java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**Closes the Statement given as parameter.
     */

    public static void close(Statement statement) {
        try {
            statement.close();
        } catch(java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**Closes the ResultSet given as parameter.
     */

    public static void close(ResultSet resultSet) {
        try {
            resultSet.close();
        } catch(java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

}
