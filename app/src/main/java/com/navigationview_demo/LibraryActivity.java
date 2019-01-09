package com.navigationview_demo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navigationview_demo.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    static List<BookClass> bookClassList;
    static List<BookClass> bookClassListCopy;
    RecyclerView recycle;

    RecyclerAdapter recyclerAdapter;

    SearchView searchLibrary;
    ImageButton scanBarcode;
    boolean barcodeFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        recycle = findViewById(R.id.recycleLibrary);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

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

                    bookClassList.add(fire);
                }

                bookClassListCopy = new ArrayList<>(bookClassList);

                if (getIntent().getBooleanExtra("ScannerDone", false) != true) {
                    setRecyclerAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.w("Library", "Failed to read database" + databaseError.toException());

            }
        });

        searchLibrary = findViewById(R.id.librarySearch);
        searchLibrary.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int i = bookClassList.size();
                int j = bookClassListCopy.size();

                bookClassList = new ArrayList<>(bookClassListCopy);
                setRecyclerAdapter();
                barcodeFilter = false;
                recyclerAdapter.filter(query, barcodeFilter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //recyclerAdapter.filter(newText);
                return true;
            }
        });

        scanBarcode = findViewById(R.id.scanBarcode);
        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeScannerActivity.class);
                startActivity(intent);
            }
        });

        Intent barcodeIntent = getIntent();
        String barcodeSearch = barcodeIntent.getStringExtra("Barcode");

        if (barcodeSearch != null) {
            bookClassList = new ArrayList<>(bookClassListCopy);
            setRecyclerAdapter();
            barcodeFilter = true;
            recyclerAdapter.filter(barcodeSearch, barcodeFilter);
        }

    }

    public void setRecyclerAdapter() {

        recyclerAdapter = new RecyclerAdapter(bookClassList, LibraryActivity.this);
        RecyclerView.LayoutManager recyclerManager = new GridLayoutManager(LibraryActivity.this, 1);
        recycle.setLayoutManager(recyclerManager);
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setAdapter(recyclerAdapter);
    }
}
