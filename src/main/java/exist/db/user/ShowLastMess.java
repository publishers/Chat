package exist.db.user;

import java.sql.*;

/**
 * Created by Admin on 12.05.15.
 */
public class ShowLastMess {
    private Connection conn = null;
    private Statement stmt = null;
    private String DB_URL = "jdbc:mysql://localhost/UserChat";
    private String USER = "root";
    private String PASS = "";

    private String uName;
    private String mess;

    public ShowLastMess(){

    }
    ResultSet reset;
    public void showMessage(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "SELECT COUNT(*) FROM `usermessage`";
            reset = stmt.executeQuery(sql);
            reset.next();
            sql = "select * from `usermessage` limit "+ (Integer.parseInt(reset.getString(1)) - 5 < 0 ? 0 :(Integer.parseInt(reset.getString(1)) - 5))
                    +", "+ reset.getString(1);
            reset = stmt.executeQuery(sql);
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
        }
    }

    public String getMess(int index) {
        try {
          return reset.getString(index);
        } catch (SQLException e) {
        }
        return null;
    }

    public boolean next(){
        try {
            if(reset.next()){
                return true;
            }else return false;
        } catch (SQLException e) {
            return false;
        }
    }
}
