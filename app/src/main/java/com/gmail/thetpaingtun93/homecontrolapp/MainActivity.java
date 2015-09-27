package com.gmail.thetpaingtun93.homecontrolapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener{

    TextView label1;
    TextView label2;
    TextView label3;

    Switch switch1;
    Switch switch2;
    Switch switch3;


    Button button1;
    Button button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label1 = (TextView) findViewById(R.id.label1);
        label2 = (TextView) findViewById(R.id.label2);
        label3 = (TextView) findViewById(R.id.label3);

        switch1 = (Switch) findViewById(R.id.switch1);
        switch2 = (Switch) findViewById(R.id.switch2);
        switch3 = (Switch) findViewById(R.id.switch3);

        switch1.setOnCheckedChangeListener(this);
        switch2.setOnCheckedChangeListener(this);
        switch3.setOnCheckedChangeListener(this);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String url = "http://192.168.1.177/";
        if (switch1.getId() == buttonView.getId()) {
            if (isChecked) {
                new DataSender().execute(url+"?LED1=ON");
                label1.setText("LED1 is ON");
            } else {
                new DataSender().execute(url+"?LED1=OFF");
                label1.setText("LED1 is OFF");
            }


        } else if (switch2.getId() == buttonView.getId()) {
            if (isChecked) {
                new DataSender().execute(url+"?LED2=ON");
                label2.setText("LED2 is ON");
            } else {
                new DataSender().execute(url+"?LED2=OFF");
                label2.setText("LED2 is OFF");
            }
        }
        else{
            if (isChecked) {
                new DataSender().execute(url+"?LED3=ON");
                label3.setText("LED3 is ON");
            } else {
                new DataSender().execute(url+"?LED3=OFF");
                label3.setText("LED3 is OFF");
            }
        }
    }

    @Override
    public void onClick(View v) {
        String url = "http://192.168.1.177/";
        if(v.getId()==button1.getId()){
            new DataSender().execute(url+"?LED=OPEN_ALL");
        }
        else if(v.getId()==button2.getId()){
            new DataSender().execute(url+"?LED=CLOSE_ALL");
        }
    }

    public class DataSender extends AsyncTask<String,Void,Void>{

        URL url;
        @Override
        protected Void doInBackground(String... params) {
            String urlString = params[0];
//

            Log.d("HOME","url is "+urlString);
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();
                StringBuilder stringBuilder = new StringBuilder();
                String lines;

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while ((lines = bufferedReader.readLine()) != null) {
                    stringBuilder.append(lines + "\n");
                }
                Log.d("HOME", stringBuilder.toString());

                Log.d("HOME", "Just finish connection");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
