package com.example.secondtry;
import java.net.*;
import java.io.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button LogInButton = (Button) findViewById(R.id.LogInButton);
        Button createAccount=(Button) findViewById(R.id.ButtonCreateAccount) ;

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveScreen = new Intent(getApplicationContext(),CreateAccountScreen.class);
                moveScreen.putExtra("com.example.myapplication.MoveCreateAccount","dsgs");
                startActivity(moveScreen);


            }
        });


        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText name= (EditText) findViewById(R.id.EditTextNameInput);
                final EditText password =(EditText) findViewById(R.id.EditTextConfirmUserName);

                Thread t1 = new Thread() {
                    public void run() {
                        String msg="";
                        try {
                            SocketHandler.getClient().clientSocket.setSoTimeout(1);

                            SocketHandler.getClient().sendMessage("log in: username: " + name.getText().toString() + " password: " + password.getText().toString());
                            SocketHandler.getClient().clientSocket.setSoTimeout(0);
                            msg=SocketHandler.getClient().recieveMessage();
                            if ("log in success".equals(msg)){
                                System.out.println("IT WORKED!!!!");
                                Intent moveScreen = new Intent(getApplicationContext(), GameActivity.class);
                                startActivity(moveScreen);
                            }
                            else{
                                System.out.println("log in failed msg rec: "+msg);
                            }


                        } catch (SocketException e) {
                            e.printStackTrace();
                        }
                        System.out.println("out of loop");

                    }
                };
                t1.start();

//                entered.setText("name "+name.getText().toString()+" password "+password.getText().toString());

            }
        });
    }
}
