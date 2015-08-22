package exist.db.user;

import java.sql.*;

/**
 * Created by Admin on 05.03.15.
 */
public class ExistUser {

    private Connection conn = null;
    private Statement stmt = null;
    private String DB_URL = "jdbc:mysql://localhost/UserChat";
    private String USER = "root";
    private String PASS = "";

    private String uName;
    private String pass;

    public ExistUser(String uName, String pass){
        this.uName = uName;
        this.pass = pass;
    }

    public boolean connectToDB(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "select * from `registrationusers` where `userName` = \"" + uName + "\" and `pass` = \"" + pass + "\"";

            ResultSet reset = stmt.executeQuery(sql);

            return reset.next() ? true : false;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
