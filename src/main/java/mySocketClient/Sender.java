package mySocketClient;

import exist.db.user.SaveLastMess;
import exist.db.user.ShowLastMess;
import model.Client;
import status.StatusClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 15.01.15.
 */
public class Sender implements Runnable{
    private Vector<Client> arraySocketClient;
    private PrintStream out ;
    private int countUsers = 0;

    public Sender(){
        arraySocketClient = new Vector<Client>();
    }

    public synchronized void addClient(Client socketClient){
        arraySocketClient.add(socketClient);
    }

    public synchronized void sendMessageHello() {
        try {
            out = new PrintStream(arraySocketClient.get(arraySocketClient.size()-1).getSocketClient().getOutputStream(), true, "UTF-8");
            out.println("Hello from Clients.java class. Your port = " + arraySocketClient.get(arraySocketClient.size() - 1).getPort());
            out.flush();
            showLastMessage();
        } catch (IOException e) {
            System.err.println("Sender 1");
        }
    }

    private synchronized void showLastMessage(){
//        File file = new File("MessageFile.txt");
//        try {
//            if(!file.exists()){
//                file.createNewFile();
//            }
//            Scanner sc = new Scanner(file, "utf-8");
//            while(sc.hasNext()) {
//                try {
//                    TimeUnit.MILLISECONDS.sleep(90);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                out.println(sc.nextLine());
//                out.flush();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


            ShowLastMess resultSet = new ShowLastMess();
            resultSet.showMessage();
            String mess;
            while(resultSet.next()){
                mess = resultSet.getMess(2) + ": " + resultSet.getMess(3);
                try {
                    TimeUnit.MILLISECONDS.sleep(90);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                out.println(mess);
                out.flush();
            }

    }

    public void saveLastMessage(String uName, String message) throws IOException {
        SaveLastMess saveLastMess = new SaveLastMess(uName, message);
        if(saveLastMess.saveMessage()){
            System.out.println("It's ok!");
        }else  System.out.println("It's bad!");
    }

    public synchronized void sendMessToAllClient(String mess){
        if( !mess.equals("null") || !mess.equals("") || !mess.equals(null) ) {
            try {
                //saveLastMessage(mess);
                for (Client s : arraySocketClient) {
                        out = new PrintStream(s.getSocketClient().getOutputStream(), true, "UTF-8");
                        out.println(mess);
                        out.flush();
                }
            } catch (IOException e) {
                System.err.println("Sender 3");
            }
        }
    }

    public synchronized void disconnectUser(Client client){
        client.setStatusClient(StatusClient.DISCONNECT);
        arraySocketClient.remove(client);
    }

    public synchronized void connectUpdateClient() {
        arraySocketClient.forEach((s) -> {
            try {
                out = new PrintStream(s.getSocketClient().getOutputStream(), true, "UTF-8");
                out.println("/new");

                arraySocketClient.forEach((client) -> {
                    String line = "/";
                    line += client.getUserName();
                    out.println(line);
                    try {
                        TimeUnit.MILLISECONDS.sleep(70);
                    } catch (InterruptedException e) {
                    }
                });
                out.flush();
            } catch (IOException e) {
                System.err.println("Sender 3");
            }
        });
    }

    public void close(){
        try {
            for (Client s : arraySocketClient){
                s.getSocketClient().close();
            }
        } catch (IOException e) {
            System.err.println("Sender 2");
        }
    }
    @Override
    public void run() {
        try {
            while (true) {
                if (countUsers != arraySocketClient.size()) {
                    countUsers = arraySocketClient.size();
                    connectUpdateClient();
                }
            }
        }catch(Exception e){
            close();
        }
    }

}
