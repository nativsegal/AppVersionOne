package com.example.secondtry;

import androidx.appcompat.app.AppCompatActivity;
import java.net.*;
import java.io.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_screen);

        Button createAcc= (Button) findViewById(R.id.ButtonCreateAccount) ;
        Button toLogin =(Button) findViewById(R.id.buttonMoveToLogIn) ;

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveScreen = new Intent(getApplicationContext(),MainActivity.class);
                moveScreen.putExtra("com.example.myapplication.MoveLogIn","dsgs");
                startActivity(moveScreen);
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText userName = (EditText) findViewById(R.id.EditTextNameInput);

                final EditText password = (EditText) findViewById(R.id.EditTextPassword);
                EditText passwordConfirm = (EditText) findViewById(R.id.EditTextPasswordConfirm);

                if (password.getText().toString().equals(passwordConfirm.getText().toString())) {

                    Thread t1 = new Thread() {
                        public void run() {
                            String msg="";
                            try {
                                SocketHandler.getClient().clientSocket.setSoTimeout(1);
                                SocketHandler.getClient().sendMessage("sign up: username: " + userName.getText().toString() + " password: " + password.getText().toString());
                                SocketHandler.getClient().clientSocket.setSoTimeout(0);
                                msg =SocketHandler.getClient().recieveMessage();

                            } catch (SocketException e) {
                                e.printStackTrace();
                            }

                        }
                    };

                    t1.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (SocketHandler.getClient().msgRec=="sign in success") {
                        Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_LONG).show();
                        Intent moveScreen = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(moveScreen);
                    } else{
                        Toast.makeText(getApplicationContext(), SocketHandler.getClient().msgRec, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "user name or password confirm is not the same", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
