package com.usermanager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usermanager_demo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserBooksActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    static List<BookClass> bookClassList;

    private RecyclerView recycle;
    private RecyclerAdapter recyclerAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String mUsername;

    private boolean notificationToSend;

    private static Date currentTime = Calendar.getInstance().getTime();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static FirebaseNotification firebaseNotification = new FirebaseNotification();

    ArrayList<String> dueDateList;
    ArrayList<String> bookTitleList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mUsername = firebaseUser.getDisplayName();

        recycle = findViewById(R.id.recycleLibrary);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        dueDateList = new ArrayList<>();
        bookTitleList = new ArrayList<>();

        myRef.child("Books").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Value", "onDataChange called");
                bookClassList = new ArrayList<BookClass>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    BookClass value = dataSnapshot1.getValue(BookClass.class);
                    BookClass fire = new BookClass();

                    String author = value.getAuthor();
                    String title = value.getTitle();
                    String borrowedState = value.getBorrowed();
                    String dueDate = value.getDueDate();
                    long barcode = value.getBarcode();

                    String coverURL = dataSnapshot1.child("cover").getValue(String.class);

                    fire.setAuthor(author);
                    fire.setTitle(title);
                    fire.setCoverUri(Uri.parse(coverURL));
                    fire.setBorrowed(borrowedState);
                    fire.setDueDate(dueDate);
                    fire.setBarcode(barcode);

                    // Add to the database only if the book is borrowed by the user

                    if (borrowedState.equals(mUsername)) {
                        bookClassList.add(fire);
                        dueDateList.add(dueDate);
                        bookTitleList.add(title);

                        try {
                            Date bookDate = dateFormat.parse(dueDate);
                            if (bookDate.before(currentTime)) {
                                notificationToSend = true;
                                //sendNotification();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }

                setRecyclerAdapter();

                if (notificationToSend == true) {
                    sendNotification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.w("Library", "Failed to read database" + databaseError.toException());

            }
        });
    }

    private void sendNotification() {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("DueDates", dueDateList);
        intent.putExtra("BookList", bookTitleList);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.agenda);
        mBuilder.setContentTitle("User Manager");
        mBuilder.setContentText("Don't forget to bring some books back to the library!");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }

    public void setRecyclerAdapter() {

        recyclerAdapter = new RecyclerAdapter(bookClassList, UserBooksActivity.this);
        RecyclerView.LayoutManager recyclerManager = new GridLayoutManager(UserBooksActivity.this, 1);
        recycle.setLayoutManager(recyclerManager);
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setAdapter(recyclerAdapter);
    }

    public void displayCalendar(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("DueDates", dueDateList);
        intent.putExtra("BookList", bookTitleList);
        startActivity(intent);
    }
}
