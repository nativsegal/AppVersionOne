package com.example.secondtry;
import android.app.Application;

public class SocketHandler extends Application {
    private static Client client;
    private static final int serverPort=2355;
    private static final String serverIp="10.100.102.8";//"10.0.2.2";

    @Override
    public void onCreate() {
        super.onCreate();
        client = new Client();
        Thread thread = new Thread(client);
        thread.start();
    }
    static synchronized Client getClient(){
        return client;
    }
    static synchronized int getServerPort(){
        return serverPort;
    }
    static synchronized String getServerIp(){
        return serverIp;
    }

    static synchronized void setClient(Client socket){
        SocketHandler.client = socket;
    }
    public static synchronized boolean tryConnection(){
        Client clientThread;
        Thread thread;
        if (client!=null){
            client.sendMessage("isConnected");
            if (!client.isConnected) {
                //try to create clientThread to connect
                clientThread = new Client();
                thread = new Thread(clientThread);
                thread.start();
                SocketHandler.setClient(clientThread);
            }
            return true;
        }else{
            //try to create clientThread to connect
            clientThread = new Client();
            thread = new Thread(clientThread);
            thread.start();
            SocketHandler.setClient(clientThread);
            client.sendMessage("isConnected");
        }
        return client.isConnected;
    }
}