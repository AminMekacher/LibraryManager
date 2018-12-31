package com.navigationview_demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SONU on 21/03/16.
 */
public class Dummy_Fragment extends Fragment {

    private List<String> quoteList = new ArrayList<String>();
    private TextView quoteView;

    public Dummy_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);

        quoteView = view.findViewById(R.id.quoteView);

        displayRandomQuote();

        return view;
    }

    private void displayRandomQuote() {

        quoteList.add(getResources().getString(R.string.quote1));
        quoteList.add(getResources().getString(R.string.quote2));

        String quote = quoteList.get(new Random().nextInt(quoteList.size()));

        quoteView.setText(quote);

    }
}
