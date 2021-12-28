package com.example.pushcontroller;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private String requestMethod;
    private String requestEndpoint;
    private TextView responseMessage;
    private void returnSuccessMessage(String button_name) {
        int responseCode = 200; // fix
        if (responseCode == 200) {
                if (button_name == "sendMessageRain") {
                    responseMessage.setText("OK");
                } else if (button_name == "sendMessageStatus") {
                    responseMessage.setText("RUNNING");
                }
                responseMessage.setTextColor(getResources().getColor(R.color.success));
            } else {
                responseMessage.setText("FAILURE");
                responseMessage.setTextColor(getResources().getColor(R.color.failure));
            }
            responseMessage.setVisibility(View.VISIBLE);
            responseMessage.postDelayed(() -> responseMessage.setVisibility(View.INVISIBLE), 3000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button sendMessageRain = (Button) findViewById(R.id.button_rain);
        sendMessageRain.setOnClickListener(v -> {
            requestMethod = "POST";
            requestEndpoint = "/run_script?name=controller_rain";
            responseMessage = (TextView)findViewById(R.id.responseMessage);
            MyTask MyTask = new MyTask();
            MyTask.execute();
            returnSuccessMessage("sendMessageRain");

        });

        final Button sendMessageStatus = (Button) findViewById(R.id.button_status);
        sendMessageStatus.setOnClickListener(v -> {
            requestMethod = "GET";
            requestEndpoint = "/";
            responseMessage = (TextView)findViewById(R.id.responseMessage);
            MyTask MyTask = new MyTask();
            MyTask.execute();
            returnSuccessMessage("sendMessageStatus");
        });
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        private int responseCode;

        @Override
        protected Void doInBackground(Void... voids) {
            URL piEndpoint;

            {
                try {
                    piEndpoint = new URL("http://192.168.2.114:4000" + requestEndpoint);
                } catch (MalformedURLException e) {
                    piEndpoint = null;
                    e.printStackTrace();
                }
            }

            HttpURLConnection myConnection;

            {
                try {
                    myConnection = (HttpURLConnection) piEndpoint.openConnection();
                    myConnection.setRequestMethod(requestMethod);
                } catch (IOException e) {
                    myConnection = null;
                    e.printStackTrace();
                }
            }


            {
                try {
                    responseCode = myConnection.getResponseCode();
                    System.out.println(responseCode);
                    if (myConnection.getResponseCode() == 200) {
                        String successMessage = myConnection.getResponseMessage();
                        System.out.println(successMessage);

                    } else {
                        int failureCode = myConnection.getResponseCode();
                        String failureMessage = myConnection.getResponseMessage();
                        System.out.println(failureCode);
                        System.out.println(failureMessage);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

}

