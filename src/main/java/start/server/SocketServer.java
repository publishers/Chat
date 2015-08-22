package start.server;

import exist.db.user.ExistUser;
import model.Client;
import mySocketClient.ClientListener;
import mySocketClient.Sender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple socket server
 * @author faheem
 *
 */
public class SocketServer {


    public static void main(String[] args) {
        String ADMIN;
        String PASS;
        ADMIN = "admin";
        PASS = "123";
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(10222);
            System.out.println("Work");
        } catch (Exception e) {
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }

        Sender sender = new Sender();
        Thread threadSender = new Thread(sender);
        threadSender.start();

        Socket socket = null;

        while(true) {
            try {
                System.out.println("Wait1 ... ");
                socket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String uName = br.readLine();
                String pass = br.readLine();
                PrintStream ps = new PrintStream(socket.getOutputStream());
                if(ADMIN.equals(uName) && PASS.equals(pass)){
                    ps.println("true");
                }else {
                    if (new ExistUser(uName, pass).connectToDB()) {
                        ps.println("true");
                    } else {
                        ps.println("false");
                        ps.flush();
                        continue;
                    }
                }
                socket.close();
                System.out.println("Wait2 ... ");
                socket = serverSocket.accept();
                System.out.println("After Wait 2 ... " + socket.getInetAddress());
                Client c = new Client(socket, sender);
                sender.addClient(c);
                sender.sendMessageHello();
                ClientListener cListener = new ClientListener(c, sender);
                cListener.start();

            } catch (Exception e) {
                System.err.println("SocketServer 1");
            }
        }

    }
}