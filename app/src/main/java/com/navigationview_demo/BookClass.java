package com.navigationview_demo;

import android.net.Uri;

/**
 * Created by aminmekacher on 01.01.19.
 */

public class BookClass {

    public String title;
    public String author;
    public Uri coverUri;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Uri getCoverUri() {
        return coverUri;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCoverUri(Uri coverUri) {
        this.coverUri = coverUri;
    }
}
