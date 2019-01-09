package com.usermanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.usermanager_demo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SONU on 21/03/16.
 */
public class Dummy_Fragment extends Fragment {

    private List<String> quoteList = new ArrayList<String>();
    private TextView quoteView;

    private Button libraryButton;
    private Button checkBooksButton;

    public Dummy_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);

        quoteView = view.findViewById(R.id.quoteView);

        displayRandomQuote();

        libraryButton = view.findViewById(R.id.browseLibrary);
        libraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LibraryActivity.class);
                getActivity().startActivity(intent);
            }
        });

        checkBooksButton = view.findViewById(R.id.checkBooks);
        checkBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserBooksActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    private void displayRandomQuote() {

        quoteList.add(getResources().getString(R.string.quote1));
        quoteList.add(getResources().getString(R.string.quote2));

        String quote = quoteList.get(new Random().nextInt(quoteList.size()));

        quoteView.setText(quote);

    }
}
