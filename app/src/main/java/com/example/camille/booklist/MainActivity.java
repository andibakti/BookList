package com.example.camille.booklist;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
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
    private BookArrayAdapter mAdapter;
    private LoaderManager loaderManager;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateUI(new ArrayList<Book>());
        emptyView.setText(R.string.start_search);


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
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_bar);
        progressBar.setVisibility(View.VISIBLE);
        emptyView.setText("");
        return new BookLoader(this, searchRequest);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book>  books){
        //Load UI with the new data, check for null or empty data.
        // Close this UI, send the data to MainActivity
        // Call the Array Adapter there to update UI
        updateUI(Collections.<Book>emptyList());
        Log.i(LOG_TAG, "TEST: Load Finished");

        emptyView.setText(R.string.empty);

        if(books != null && !books.isEmpty()){
            updateUI(books);
            Log.i(LOG_TAG, "TEST: Load Finished; Updated UI");
        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_bar);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader){
        //Clear UI
        //TODO:
        updateUI(Collections.<Book>emptyList());
        Log.i(LOG_TAG, "TEST: Load Reset");
    }



    protected void updateUI(List<Book> books){
        mAdapter = new BookArrayAdapter(this, books);
        ListView bookListView = (ListView) findViewById(R.id.books);

        emptyView = (TextView) findViewById(R.id.emptyElement);

        bookListView.setAdapter(mAdapter);

        if(books == null || books.isEmpty()) {
            mAdapter.clear();
        }

        bookListView.setEmptyView(emptyView);

        Log.i(LOG_TAG, "TEST: Updating UI");
        //listening for click, which redirects to links for the corresponding view
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l){
                Book currentBook = mAdapter.getItem(position);
                assert currentBook != null;
                String url = currentBook.geturlLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


            //Ask user to perform a search

    }

    //SearchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);



        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        final MenuItem searchMenu = menu.findItem(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRequest = query;
                Log.i(LOG_TAG,"TEST: Setting searchRequest to be the query");

                loaderManager = getLoaderManager();
                loaderManager.initLoader(BOOK_LOADER_ID, null,MainActivity.this);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                updateUI(Collections.<Book>emptyList());
                loaderManager = getLoaderManager();
                loaderManager.destroyLoader(BOOK_LOADER_ID);
                emptyView.setText("");
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchMenu,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        updateUI(Collections.<Book>emptyList());
                        loaderManager = getLoaderManager();
                        loaderManager.destroyLoader(BOOK_LOADER_ID);
                        emptyView.setText("");
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // Refresh here with full list.
                        emptyView.setText(R.string.start_search);
                        return true;
                    }
                });
        return true;
    }


}
