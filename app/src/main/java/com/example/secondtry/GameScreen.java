package com.example.secondtry;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.SocketException;

public class GameScreen extends AppCompatActivity {
    int totalAttempts = 0;
    int totalMakes=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        Button swish=(Button) findViewById(R.id.madeBasketButton);
        Button missed=(Button) findViewById(R.id.missedBasketButton);
        final TextView gameResult =(TextView) findViewById(R.id.resultTextView);
        final TextView shots =(TextView) findViewById(R.id.shotsLeftTextView);

        swish.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (totalAttempts<10) {
                    Thread t1 = new Thread() {
                        @SuppressLint("SetTextI18n")
                        public void run() {
                            String msg = "";
                            try {
                                SocketHandler.getClient().clientSocket.setSoTimeout(1);
                                SocketHandler.getClient().sendMessage("made basket");
                                SocketHandler.getClient().clientSocket.setSoTimeout(0);
                                msg = SocketHandler.getClient().recieveMessage();
                                if (msg == null) {
                                    System.out.println("failed the made basket process from server");
                                } else {
                                    totalAttempts++;
                                    totalMakes++;
                                }
                                if (totalAttempts >= 10) {
                                    SocketHandler.getClient().clientSocket.setSoTimeout(1);
                                    SocketHandler.getClient().sendMessage("finished game" + totalMakes);
                                    SocketHandler.getClient().clientSocket.setSoTimeout(0);
                                    msg = SocketHandler.getClient().recieveMessage();
                                    System.out.println("after finish " + msg);
                                    if (SocketHandler.getClient().msgRec.contains("wait")) {
                                        msg = SocketHandler.getClient().recieveMessage();
                                        System.out.println("final result " + msg);
                                    }
                                }

                            } catch (SocketException e) {
                                e.printStackTrace();
                            }
                            System.out.println("out of loop");

                        }
                    };
                    t1.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (SocketHandler.getClient().msgRec.contains("you")) {
                        gameResult.setText(SocketHandler.getClient().msgRec.toString());
                    }
                    shots.setText(Integer.toString(10 - totalAttempts));
                }
            }
        });
        missed.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (totalAttempts<10) {
                    Thread t1 = new Thread() {
                        @SuppressLint("SetTextI18n")
                        public void run() {
                            String msg = "";
                            try {
                                SocketHandler.getClient().clientSocket.setSoTimeout(1);
                                SocketHandler.getClient().sendMessage("missed basket");
                                SocketHandler.getClient().clientSocket.setSoTimeout(0);
                                msg = SocketHandler.getClient().recieveMessage();
                                if (msg == null) {
                                    System.out.println("failed the made basket process from server");
                                } else {
                                    totalAttempts++;
                                }
                                if (totalAttempts >= 10) {
                                    SocketHandler.getClient().clientSocket.setSoTimeout(1);
                                    SocketHandler.getClient().sendMessage("finished game" + totalMakes);
                                    SocketHandler.getClient().clientSocket.setSoTimeout(0);
                                    msg = SocketHandler.getClient().recieveMessage();
                                    System.out.println("after finish " + msg);
                                    if (SocketHandler.getClient().msgRec.contains("wait")) {
                                        msg = SocketHandler.getClient().recieveMessage();
                                        System.out.println("final result " + msg);
                                    }
                                }

                            } catch (SocketException e) {
                                e.printStackTrace();
                            }
                            System.out.println("out of loop");

                        }
                    };
                    t1.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (SocketHandler.getClient().msgRec.contains("you")) {
                        gameResult.setText(SocketHandler.getClient().msgRec.toString());
                    }
                    shots.setText(Integer.toString(10 - totalAttempts));
                }
            }
        });
    }
}
