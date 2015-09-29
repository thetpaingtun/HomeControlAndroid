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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    TextView label1;
    TextView label2;
    TextView label3;
    TextView temp;

    Switch switch1;
    Switch switch2;
    Switch switch3;


    Button button1;
    Button button2;

    private final int UPDATE_ALL = 1;
    private final String SWITCH_ONE_ON = "http://homecontrol.eu5.org/index.php/api/changestate?device_id=1&state=1";
    private final String SWITCH_ONE_OFF = "http://homecontrol.eu5.org/index.php/api/changestate?device_id=1&state=0";
    private final String SWITCH_TWO_ON = "http://homecontrol.eu5.org/index.php/api/changestate?device_id=2&state=1";
    private final String SWITCH_TWO_OFF = "http://homecontrol.eu5.org/index.php/api/changestate?device_id=2&state=0";
    private final String SWITCH_THREE_ON = "http://homecontrol.eu5.org/index.php/api/changestate?device_id=3&state=1";
    private final String SWITCH_THREE_OFF = "http://homecontrol.eu5.org/index.php/api/changestate?device_id=3&state=0";

    private final String OPEN_ALL = "http://homecontrol.eu5.org/index.php/api/openall";
    private final String CLOSE_ALL = "http://homecontrol.eu5.org/index.php/api/closeall";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label1 = (TextView) findViewById(R.id.label1);
        label2 = (TextView) findViewById(R.id.label2);
        label3 = (TextView) findViewById(R.id.label3);

        temp = (TextView) findViewById(R.id.temp_value);

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

        updateData();
    }

    private void updateData() {
        new DataSync().execute();

    }

    //attach and detach listener to switch
    private void setLinstenerForSwitch(CompoundButton.OnCheckedChangeListener listener) {
        switch1.setOnCheckedChangeListener(listener);
        switch2.setOnCheckedChangeListener(listener);
        switch3.setOnCheckedChangeListener(listener);
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
        if (switch1.getId() == buttonView.getId()) {
            if (isChecked) {
                new DataSender().execute(SWITCH_ONE_ON);
                label1.setText("LED1 is ON");
            } else {
                new DataSender().execute(SWITCH_ONE_OFF);
                label1.setText("LED1 is OFF");
            }


        } else if (switch2.getId() == buttonView.getId()) {
            if (isChecked) {
                new DataSender().execute(SWITCH_TWO_ON);
                label2.setText("LED2 is ON");
            } else {
                new DataSender().execute(SWITCH_TWO_OFF);
                label2.setText("LED2 is OFF");
            }
        } else {
            if (isChecked) {
                new DataSender().execute(SWITCH_THREE_ON);
                label3.setText("LED3 is ON");
            } else {
                new DataSender().execute(SWITCH_THREE_OFF);
                label3.setText("LED3 is OFF");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == button1.getId()) {

            //just want to change the state of switch without triggering the events
            setLinstenerForSwitch(null);

            new DataSender().execute(OPEN_ALL);

            switch1.setChecked(true);
            switch2.setChecked(true);
            switch3.setChecked(true);

            //attach the listener back
            setLinstenerForSwitch(MainActivity.this);
        } else if (v.getId() == button2.getId()) {
            new DataSender().execute(CLOSE_ALL);

            //just want to change the state of switch without triggering the events
            setLinstenerForSwitch(null);

            switch1.setChecked(false);
            switch2.setChecked(false);
            switch3.setChecked(false);

            setLinstenerForSwitch(MainActivity.this);
        }
    }

    public class DataSender extends AsyncTask<String, Void, Void> {

        URL url;

        @Override
        protected Void doInBackground(String... params) {


            String strUrl = params[0];

            try {
                URL url = new URL(strUrl);
                Log.d("HOME_CONTROL", "url is " + strUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();


                connection.getInputStream();


                Log.d("HOME_CONTROL", "just finish sending get request");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



            return null;
        }
    }

    public class DataSync extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            String urlTemp = "http://homecontrol.eu5.org/index.php/api/gettemp";
            String urlState = "http://homecontrol.eu5.org/index.php/api/getstate";

            String[] data = new String[4];

            String strTemp = returnJsonStringFromWebservice(urlTemp);
            String strState = returnJsonStringFromWebservice(urlState);

            try {
                JSONArray jsonTemp = new JSONArray(strTemp);
                JSONArray jsonState = new JSONArray(strState);


                for (int i = 0; i < 3; i++) {
                    String value = jsonState.getJSONObject(i).getString("state");
                    data[i] = value;
                }

                String value = jsonTemp.getJSONObject(0).getString("value");
                data[3] = value;

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return data;
        }

        @Override
        protected void onPostExecute(String[] data) {

            //detach listener before sync data with server
            setLinstenerForSwitch(null);

            //update data according to data sent from servers
            switch1.setChecked(data[0].equals("1"));
            switch2.setChecked(data[1].equals("1"));
            switch3.setChecked(data[2].equals("1"));

            //attach listener back to the switch after syncing with server
            setLinstenerForSwitch(MainActivity.this);

            temp.setText(data[3]+" C");


        }

        private String returnJsonStringFromWebservice(String urlString) {
            String ans = "";

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                StringBuilder stringBuilder = new StringBuilder();
                String lines;

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((lines = bufferedReader.readLine()) != null)
                    stringBuilder.append(lines + "\n");

                ans = stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ans;
        }
    }


}
