package DBO;

import java.sql.*;

public class DBconnect {

    private static final String url="jdbc:mysql://localhost:3306/teste";
    private static final String user="root";
    private static final String password="";
    public static Connection getconnection() throws SQLException {

        return DriverManager.getConnection(url ,user,password);
    }


}
