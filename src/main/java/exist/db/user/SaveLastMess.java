package exist.db.user;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by Admin on 12.05.15.
 */
public class SaveLastMess {
    private Connection conn = null;
    private Statement stmt = null;
    private String DB_URL = "jdbc:mysql://localhost/UserChat";
    private String USER = "root";
    private String PASS = "";

    private String uName;
    private String mess;

    public SaveLastMess(String uName, String mess){
        this.uName = uName;
        this.mess = mess;
    }

    public boolean saveMessage(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            GregorianCalendar gcalendar = new GregorianCalendar();
            String time = gcalendar.get(Calendar.DAY_OF_MONTH) + "."
                    + gcalendar.get(Calendar.MONTH) + "."
                    + gcalendar.get(Calendar.YEAR) + " "
                    + gcalendar.get(Calendar.HOUR) + ":"
                    + gcalendar.get(Calendar.MINUTE) + ":"
                    + gcalendar.get(Calendar.SECOND);
            String sql = "insert into `usermessage` (`userName`, `Text`) values (\"" + uName + "\" , \""+ time+": " + mess + "\")";
            stmt.executeUpdate(sql);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (SQLException e) {
            return false;
        }
    }
}
