package com.example.camille.booklist;

import android.graphics.Bitmap;
import android.media.Image;
import android.provider.ContactsContract;

/**
 * Created by camille on 15/06/2017.
 */

class Book {
    private String[] authors;
    private String title;
    private String yearPublished;
    private String urlLink;
    private Bitmap thumbnail;

    public Book(String title, String[] authors, String yearPublished,
                String urlLink, Bitmap thumbnail){

        this.title = title;
        this.authors = authors;
        this.yearPublished = yearPublished;
        this.urlLink = urlLink;
        this.thumbnail = thumbnail;
    }

    public String[] getAuthors(){
        return this.authors;
    }

    public String getTitle(){
        return this.title;
    }

    public String getYearPublished(){
        return this.yearPublished;
    }

    public String geturlLink(){
        return this.urlLink;
    }

    public Bitmap getThumbnail(){return this.thumbnail;}
}
