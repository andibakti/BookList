package com.example.camille.booklist;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import static com.example.camille.booklist.MainActivity.LOG_TAG;

/**
 * Created by camille on 15/06/2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private String searchRequest;

    //Constructor
    public BookLoader(Context context, String searchRequest){
        super(context);
        this.searchRequest = searchRequest;
    }

    //forceLoad needs to be called to initialize loadInBackground()
    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    //Calls the API query with the input keyword(s)
    @Override
    public List<Book> loadInBackground(){
        List<Book> books;
        if(searchRequest == null){
            return null;
        }

        books = QueryUtils.fetchBooks(searchRequest);
        Log.i(LOG_TAG, "TEST: Books retrieved for the following request: " + searchRequest);

        return books;
    }
}
