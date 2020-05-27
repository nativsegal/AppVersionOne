package com.example.secondtry;
// A Java program for a Client
import android.app.Application;

import java.net.*;
import java.io.*;

//import static com.example.myapplication.SocketHandler.serverIp;
//import static com.example.myapplication.SocketHandler.serverPort;
class Client implements Runnable {
    Socket clientSocket;
    boolean isConnected=false;
    boolean isOnline=false;
    boolean isMsgRecieved=false;
    String msgRec;
    private PrintWriter out;
    private BufferedReader in;
    Thread msgThread;
    @Override
    public void run() {

        try {
            System.out.println("made it to connect segment");
            clientSocket = new Socket(SocketHandler.getServerIp(), SocketHandler.getServerPort());
            isConnected=true;
//            Thread t1 = new Thread() {
//                public void run() {
//                    String msg="";
//                    try {
//                        SocketHandler.getClient().clientSocket.setSoTimeout(1);
//
//                        SocketHandler.getClient().sendMessage("log in: user name: hi" + " password: 12");
//                        SocketHandler.getClient().clientSocket.setSoTimeout(0);
//                        msg=SocketHandler.getClient().recieveMessage();
//                        System.out.println("cant believe it ! it worked "+msg);
//                    } catch (SocketException e) {
//                        e.printStackTrace();
//                    }
//
////                        while (!SocketHandler.getClient().isMsgRecieved) {
////                            if (SocketHandler.getClient().isOnline) {
////                                Intent moveScreen = new Intent(getApplicationContext(), GameActivity.class);
////                                startActivity(moveScreen);
////                            }
////                        }
//                    System.out.println("out of loop");
//                    if (msg.equals("log in success")){
//                    }
//                    else{
//                        System.out.println("log in failed");
//                    }
//
//                }
//            };
//            t1.start();


        } catch (UnknownHostException u) {
            System.out.println(u);
            isConnected=false;
        } catch (IOException i) {
            System.out.println(i);
            isConnected=false;
        }
    }


    String recieveMessage(){
        String message="";
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            message = in.readLine();
            msgRec=message;
            return message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
    void sendMessage(final String msg) {
        try {
            if(clientSocket!=null) {
                int in =clientSocket.getInputStream().read();

                if (in==-1){
                    System.out.println("client is not connected when button pressed");
                    try {
                        clientSocket = new Socket(SocketHandler.getServerIp(), SocketHandler.getServerPort());
                        out = new PrintWriter(clientSocket.getOutputStream(), true);
                        out.println(msg);
                        System.out.println("client sent message after reconnect");
                        isConnected = true;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.out.println("server is unreachable");
                        isConnected = false;
                    }
                }
            }
        }
        catch (SocketTimeoutException ignored) {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(msg);
                System.out.println("client sent message "+msg);
                isConnected = true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IOException after timeout");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Socket Exception");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        }
        catch (IOException i){
            System.out.println(i);
        }
    }
}