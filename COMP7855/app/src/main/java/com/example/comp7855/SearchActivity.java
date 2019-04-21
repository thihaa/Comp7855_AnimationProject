package com.example.comp7855;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {

    private int currentPhotoIndex = 0;
    private EditText fromDate;
    private EditText toDate;
    private EditText keyWord;
    private EditText latitude;
    private EditText longitude;
    private EditText distance;
    private Context context;
    private int duration;
    private String toast_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        currentPhotoIndex = intent.getIntExtra("photoIndex", 0);
        fromDate = findViewById(R.id.editFrom);
        toDate   = findViewById(R.id.editTo);
        keyWord = findViewById(R.id.editKeyword);
        latitude = findViewById(R.id.editLat);
        longitude = findViewById(R.id.editLong);
        distance = findViewById(R.id.editDist);
        context = getApplicationContext();
        duration = Toast.LENGTH_SHORT;
    }

    public void cancel (View v){
        Intent i = new Intent();
        i.putExtra("OLDPICTURE",currentPhotoIndex);
        setResult(Activity.RESULT_CANCELED, i);
        finish();
    }

    public void go(View v) {
        boolean send = true;
        Toast toast;
        String from = this.fromDate.getText().toString();
        String to = this.toDate.getText().toString();
        String key = this.keyWord.getText().toString();
        String lat = this.latitude.getText().toString();
        String lon = this.longitude.getText().toString();
        String dist = this.distance.getText().toString();
        float latFloat, longFloat, distFloat;

        if (TextUtils.isEmpty(lat))
            latFloat = 0;
        else
            latFloat = Float.parseFloat(lat);

        if (TextUtils.isEmpty(lon))
            longFloat = 0;
        else
            longFloat = Float.parseFloat(lon);

        if (TextUtils.isEmpty(dist))
            distFloat = 0;
        else
            distFloat = Float.parseFloat(dist);

        if (!from.equals("") && from.length() != 14){
            send = false;
            toast_text = "Improper From Date";
        }

        if (!to.equals("") && to.length() != 14){
            send = false;
            toast_text = "Improper To Date";
        }

        if (!dist.equals("") && (distFloat > Float.MAX_VALUE || distFloat < 0)){
            send = false;
            toast_text = "Improper Distance";
        }

        if (!lat.equals("") && (latFloat < -90 || latFloat > 90)){
            send = false;
            toast_text = "Improper Latitude";
        }

        if (!lon.equals("") && (longFloat < -180 || longFloat > 180)){
            send = false;
            toast_text = "Improper Longitude";
        }

        if (send) {
            Intent i = new Intent();
            i.putExtra("STARTDATE", from);
            i.putExtra("ENDDATE", to);
            i.putExtra("NAME", key);
            i.putExtra("LAT", lat);
            i.putExtra("LONG", lon);
            i.putExtra("DIST", dist);
            i.putExtra("OLDPICTURE",currentPhotoIndex);
            setResult(Activity.RESULT_OK, i);
            finish();
        }
        else {
            toast = Toast.makeText(context, toast_text + ", cancel or try again", duration);
            toast.show();
        }

    }

}
