package com.example.saurabh.helloworld;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String URL_STRING = "http://dataserver-env.elasticbeanstalk.com/rest/transaction/transfer";
    public  static final String REQUEST_METHOD = "POST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.saurabh", Context.MODE_PRIVATE);
        String username = prefs.getString("username","");
        //Toast.makeText(this,"Hi!!!" + username,Toast.LENGTH_LONG).show();
        if(!username.equals("")) {
            Intent intent = new Intent(this,TransferActivity.class);
            startActivity(intent);
        }

                ((TextView) findViewById(R.id.exp_date)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentDate = Calendar.getInstance();
                        int mYear = mcurrentDate.get(Calendar.YEAR);
                        int mMonth = mcurrentDate.get(Calendar.MONTH);
                        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog mDatePicker = new DatePickerDialog(MainActivity.
                                this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.YEAR, selectedyear);
                                cal.set(Calendar.MONTH, selectedmonth);
                                cal.set(Calendar.DATE, selectedday);
                                Date date = cal.getTime();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                                String dateString = "";
                                dateString = dateFormat.format(date);
                                ((TextView) findViewById(R.id.exp_date)).setText(dateString);
                            }
                        }, mYear, mMonth, mDay);
                        mDatePicker.setTitle("Select date");
                        mDatePicker.show();
                    }
                });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                JSONObject jsonObject = new JSONObject();
                String data;
                SharedPreferences prefs = this.getSharedPreferences(
                        "com.example.saurabh", Context.MODE_PRIVATE);
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                Log.d("Debug", "Username:" + username);
                prefs.edit().putString("username", username).apply();
                Intent intent = new Intent(this,TransferActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
