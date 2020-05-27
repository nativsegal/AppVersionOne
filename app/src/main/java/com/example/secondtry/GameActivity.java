package com.example.secondtry;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.secondtry.R;

import java.net.SocketException;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ImageButton openCamera=(ImageButton) findViewById(R.id.imageButtonOpenCamera) ;
        Button searchGame=(Button) findViewById(R.id.searchGameButton);
        final TextView scoreText=(TextView) findViewById(R.id.scoreTextView);
        Thread t5 = new Thread() {
            @SuppressLint("SetTextI18n")
            public void run() {
                String msg="";
                try {
                    SocketHandler.getClient().clientSocket.setSoTimeout(1);
                    SocketHandler.getClient().sendMessage("score req");
                    SocketHandler.getClient().clientSocket.setSoTimeout(0);
                    msg=SocketHandler.getClient().recieveMessage();
                    if (msg!=null){
                        System.out.println("msg received "+ msg);
                        scoreText.setText("Your score: "+ msg.toString());
                    }
                    else{
                        System.out.println("failed to receive player score from server");
                    }


                } catch (SocketException e) {
                    e.printStackTrace();
                }
                System.out.println("out of loop");

            }
        };
        t5.start();

        final TextView searching =(TextView) findViewById(R.id.searchingTextView);
        searchGame.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                searching.setText("Searching Game...");

                Thread t3 = new Thread() {
                    public void run() {
                        String msg="";
                        try {
                            SocketHandler.getClient().clientSocket.setSoTimeout(1);
                            SocketHandler.getClient().sendMessage("search game");
                            SocketHandler.getClient().clientSocket.setSoTimeout(0);
                            msg=SocketHandler.getClient().recieveMessage();
                            System.out.println("msg received "+ msg);
                            if (msg.equals("found match")){
                                Intent moveScreen = new Intent(getApplicationContext(), GameScreen.class);
                                startActivity(moveScreen);
                            }
                            else{
                                System.out.println("failed to find a match");
                            }


                        } catch (SocketException e) {
                            e.printStackTrace();
                        }
                        System.out.println("out of loop");

                    }
                };
                t3.start();
            }
        });

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });
    }
}
