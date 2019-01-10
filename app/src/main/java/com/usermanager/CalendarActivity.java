package com.usermanager;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.usermanager_demo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    ArrayList<String> dueDateString = new ArrayList<>();
    ArrayList<Date> dueDate = new ArrayList<>();

    ArrayList<String> bookTitleList = new ArrayList<>();

    ColorDrawable colorDrawable;

    TextView dueBookText;
    String bookListView;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dueBookText = findViewById(R.id.dueBookText);

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();

        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.greenCalendar));

        dueDateString = getIntent().getExtras().getStringArrayList("DueDates");
        bookTitleList = getIntent().getExtras().getStringArrayList("BookList");

        // Transform the strings into dates

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                ArrayList<String> titleDate = new ArrayList<>();

                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                String dateSelected = dateFormat1.format(date);

                if (dueDate.contains(date)) {
                    for (String dateString : dueDateString) {
                        try {
                            Date testDate = dateFormat.parse(dateString);
                            if (testDate.equals(date)) {
                                index = dueDateString.indexOf(dateString);
                                titleDate.add(bookTitleList.get(index));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    if (titleDate != null) {
                        bookListView = android.text.TextUtils.join(",", titleDate);
                        dueBookText.setText("Books due for the " + dateSelected + " : \n" + bookListView);
                        dueBookText.setVisibility(View.VISIBLE);
                    }
                } else {
                    dueBookText.setVisibility(View.INVISIBLE);
                }
            }
        };

        caldroidFragment.setCaldroidListener(listener);

    }


}