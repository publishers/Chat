package start.server;

import exist.db.user.ExistUser;
import mySocketClient.Sender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Admin on 07.03.15.
 */
public class SocketServerConnectWithDB {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8088);
            System.out.println("Work");
        } catch (Exception e) {
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }

        Sender sender = new Sender();
        Thread threadSender = new Thread(sender);
        threadSender.start();

        Socket socket = null;
        while (true) {
            try {
                socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String uName = br.readLine();
                String pass = br.readLine();
                PrintStream ps = new PrintStream(socket.getOutputStream());
                if (new ExistUser(uName, pass).connectToDB()) {
                    ps.println("true");
                } else {
                    ps.println("false");
                }
            } catch (Exception e) {
                System.err.println("SocketServerConnectWithDB 1");
            }
        }
    }
}
