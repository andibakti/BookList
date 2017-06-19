package com.example.camille.booklist;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import java.util.List;

/**
 * Created by camille on 15/06/2017.
 */

public class SearchResultActivity extends Activity {

    private String searchRequest;
    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting the query as the searchRequest
        handleIntent(getIntent());
        //if there is internet

        Log.i(LOG_TAG,"TEST: SearchResultActivity created");
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //TODO: Use the query to search your data somehow
            //      (ie, call the BookLoader)
            searchRequest = query;

        }
    }
}
