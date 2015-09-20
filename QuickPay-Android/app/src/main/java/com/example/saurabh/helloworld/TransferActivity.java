package com.example.saurabh.helloworld;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TransferActivity extends AppCompatActivity {

    public static final String URL_STRING = "http://dataserver-env.elasticbeanstalk.com/rest/transaction/transfer";
    public  static final String REQUEST_METHOD = "POST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.saurabh", Context.MODE_PRIVATE);
        String username = prefs.getString("username","");
        ((TextView)findViewById(R.id.username)).setText("Hi " + username + "!");

        if(username.equals("username1")) {
            ((TextView)findViewById(R.id.balance)).setText("1000");
        } else if(username.equals("username2")) {
            ((TextView)findViewById(R.id.balance)).setText("500");
        } else {
            ((TextView)findViewById(R.id.balance)).setText("500");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transfer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay:
                JSONObject jsonObject = new JSONObject();
                SharedPreferences prefs = this.getSharedPreferences(
                        "com.example.saurabh", Context.MODE_PRIVATE);
                try {
                    jsonObject.put("senderID", prefs.getString("username", "defaultUser"));
                    jsonObject.put("amount", ((EditText) findViewById(R.id.amount)).getText().toString());
                    jsonObject.put("receiverID", ((EditText) findViewById(R.id.recipient)).getText().toString());
                    jsonObject.put("bankName", "Savings");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new sendJSONTask(this,jsonObject).execute();
                break;
            default:
                break;
        }
    }

    private String sendJSON(JSONObject jsonObject) {
        HttpURLConnection httpcon;
        String data = null;
        try{
            data = jsonObject.toString();
            //Connect
            httpcon = (HttpURLConnection) ((new URL(URL_STRING).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod(REQUEST_METHOD);
            httpcon.connect();

            //Write
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();
            os.close();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));
            String result = br.readLine();
            Log.d("Debug", result);
            br.close();
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class sendJSONTask extends AsyncTask<Void, Void, Void> {
        private Context context;
        private JSONObject jsonObject;
        private String result;

        public sendJSONTask(Context context,JSONObject jsonObject) {
            this.context = context;
            this.jsonObject = jsonObject;
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = sendJSON(jsonObject);
            Log.d("Debug",result);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            Log.d("Debug", "Hi");
            int amount = Integer.parseInt(((EditText) findViewById(R.id.amount)).getText().toString());
            int balance = Integer.parseInt(((TextView) findViewById(R.id.balance)).getText().toString());
            Log.d("Debug",(balance - amount) + "");
            ((TextView) findViewById(R.id.balance)).setText((balance - amount)+"");
            ((TextView)findViewById(R.id.ack)).setText("Payment Successful");
        }
    }
}
