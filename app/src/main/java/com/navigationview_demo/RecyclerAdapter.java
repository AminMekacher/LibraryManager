package com.navigationview_demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by aminmekacher on 01.01.19.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHoder> {

    List<BookClass> bookList = new ArrayList<>();
    List<BookClass> bookListCopy = new ArrayList<>();

    Context context;

    public RecyclerAdapter(List<BookClass> bookList, Context context) {
        this.bookList = bookList;
        this.bookListCopy = bookList;
        this.context = context;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        MyHoder myHoder = new MyHoder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(MyHoder holder, int position) {
        BookClass myList = bookList.get(position);
        holder.author.setText(myList.getAuthor());
        holder.title.setText(myList.getTitle());
        Picasso.get().load(myList.getCoverUri())
                .resize(220, 350)
                .into(holder.coverUri);
    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try{
            if (bookList.size() == 0) {
                arr = 0;
            } else {
                arr = bookList.size();
            }
        } catch (Exception e) {

        }

        return arr;
    }

    public void filter (String queryText) {

        bookList.clear();

        if (queryText.isEmpty()) {
            bookList.addAll(bookListCopy);
        } else {
            queryText = queryText.toLowerCase();
            for (BookClass bookClass : bookListCopy) {
                if (bookClass.getTitle().toLowerCase().contains(queryText)) {
                    bookList.add(bookClass);
                }
            }
        }

    }

    class MyHoder extends RecyclerView.ViewHolder {

        TextView author, title;
        ImageView coverUri;

        public MyHoder(View itemView) {

            super(itemView);
            author = itemView.findViewById(R.id.book_author);
            title = itemView.findViewById(R.id.book_title);
            coverUri = itemView.findViewById(R.id.book_cover);
        }
    }
}
