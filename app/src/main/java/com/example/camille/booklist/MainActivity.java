package com.example.camille.booklist;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.AbsListView;
import android.widget.ListView;


import java.util.Collections;
import java.util.List;

import static com.example.camille.booklist.R.id.search;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    /*TODO:
     *      - Call loader on create
     *      - Implement LoaderManager interface to interact with loader
     *      - Define the abstract inherited loader methods(create, onFinish, on Reset)
     *      - updateUI method (Call the ViewAdapter)
     */

    public static final String LOG_TAG = MainActivity.class.getName();
    protected String searchRequest = null;
    private static final int BOOK_LOADER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //List<Book> books = QueryUtils.fetchBooks("quilting");
    }

    public boolean setSearchRequest(String request){
        boolean wasSet = false;
        if(!request.isEmpty()){
            searchRequest = request;
            wasSet = true;
        }
        return wasSet;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle bundle){
        //Calling the BookLoader constructor, which calls QueryUtils and performs the search
        return new BookLoader(this, searchRequest);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book>  books){
        //Load UI with the new data, check for null or empty data.
        // Close this UI, send the data to MainActivity
        // Call the Array Adapter there to update UI
        updateUI(Collections.<Book>emptyList());
        Log.i(LOG_TAG, "TEST: Load Finished");
        Log.i(LOG_TAG, "TEST: books isEmpty?: " + books.isEmpty());


        if(books != null && !books.isEmpty()){
            updateUI(books);
            Log.i(LOG_TAG, "TEST: Load Finished; Updated UI");
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader){
        //Clear UI
        updateUI(Collections.<Book>emptyList());
        Log.i(LOG_TAG, "TEST: Load Reset");
    }



    protected void updateUI(List<Book> books){

        if(books != null && !books.isEmpty()) {

            BookArrayAdapter adapter = new BookArrayAdapter(this, books);
            final ListView bookListView = (ListView) findViewById(R.id.books);

            bookListView.setAdapter(adapter);
            Log.i(LOG_TAG, "TEST: Updating UI" + books.get(0).getAuthors().toString());
        }
    }


    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        Log.i(LOG_TAG,"TEST: Options Menu closed, calling loader");

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null,this);     //force load is already called
    }

    //SearchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRequest = query;
                Log.i(LOG_TAG,"TEST: Setting searchRequest to be the query");
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(BOOK_LOADER_ID, null,MainActivity.this);     //force load is already called
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


}
