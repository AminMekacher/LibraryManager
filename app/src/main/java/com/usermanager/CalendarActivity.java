package com.usermanager;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.roomorama.caldroid.CaldroidFragment;
import com.usermanager_demo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    ArrayList<String> dueDateString = new ArrayList<>();
    ArrayList<Date> dueDate = new ArrayList<>();

    ColorDrawable colorDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();

        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.redCalendar));

        dueDateString = getIntent().getExtras().getStringArrayList("DueDates");

        // Transform the strings into dates

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (String dateString : dueDateString) {
            try {
                dueDate.add(dateFormat.parse(dateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (Date date : dueDate) {
            caldroidFragment.setBackgroundDrawableForDate(colorDrawable, date);
        }

    }
}