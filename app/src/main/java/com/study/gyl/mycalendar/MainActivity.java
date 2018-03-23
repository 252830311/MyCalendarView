package com.study.gyl.mycalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NewCalendar.NewCalendarListener{
    private NewCalendar newCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newCalendar = (NewCalendar) findViewById(R.id.nc_mycalendar);
        newCalendar.setOnNewCalendarListener(this);
    }

    @Override
    public void onItemLongPress(Date day) {
        System.out.println("------------------"+ SimpleDateFormat.getDateInstance().format(day));
    }
}
