package db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CS360DB {

    private static final String URL = "jdbc:mysql://localhost";
    private static final String DATABASE = "hy360";
    private static final int PORT = 3306;
    private static final String UNAME = "root";
    private static final String PASSWD = "";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String full = URL + ":" + PORT + "/" + DATABASE + "?characterEncoding=UTF-8";
        Logger.getLogger(CS360DB.class.getName()).log(Level.SEVERE, full, full);
        return DriverManager.getConnection(
                URL + ":" + PORT + "/" + DATABASE , UNAME, PASSWD);

    }

    public static String getUserName() {
        return UNAME;
    }

    /**
     * Close db connection
     *
     * @param stmt
     * @param con
     */
    public static void closeDBConnection(Statement stmt, Connection con) {
        // Close connection
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
//                Logger.getLogger(PostDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
//                Logger.getLogger(PostDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
