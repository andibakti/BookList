package com.example.camille.booklist;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.example.camille.booklist.MainActivity.LOG_TAG;


/**
 * Created by camille on 14/06/2017.
 */

public class QueryUtils {
    /*This class will:
     *      - Create URL object from string
     *      - Make HTTP request
     *      - Read the stream (convert bytes to UTF-8)
     *      - Extract the JSON (return a list of books)
      */

    //Test URL "https://www.googleapis.com/books/v1/volumes?q=quilting"

    //This method creates a URL object from a search request
    //// TODO: Check for symbols in string

    private static URL createURL(String searchRequest){
        URL urlObject = null;
        searchRequest = searchRequest.replaceAll(" ", "+");
        try {
            urlObject = new URL("https://www.googleapis.com/books/v1/volumes?q="+ searchRequest + "&printType=books");
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error fetching the following request: " + searchRequest);
        }
        return urlObject;
    }

    public static List<Book> fetchBooks(String searchRequest){
        String jsonResponse = null;
        List<Book> books;

        URL Url = createURL(searchRequest);

        try{
            jsonResponse = makeHTTPRequest(Url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Error connecting to URL: " + Url.toString());
        }

        books = extractBooks(jsonResponse);
        return books;

    }

    private static List<Book> extractBooks(String jsonResponse) {
        List<Book> books = new LinkedList<>();

        if(jsonResponse == null){
            return null;
        }
        try {
            JSONObject root = new JSONObject(jsonResponse);

            JSONArray jsonArray = root.getJSONArray("items");

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject aBook = jsonArray.getJSONObject(i);
                JSONObject volumeInfo = aBook.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");
                Log.i(LOG_TAG, "TEST: Title created");

                String urlLink = volumeInfo.getString("infoLink");
                Log.i(LOG_TAG, "TEST: Link created");

                /////////////
                String date = volumeInfo.getString("publishedDate");
                String[] split = date.split("-");
                date = split[0];            //this only retrieves the year
                Log.i(LOG_TAG, "TEST: Date created");

                /////////////
                String authorsList = volumeInfo.get("authors").toString();
                Log.i(LOG_TAG, "TEST: authors array created");
                String[]authors = new String[1];
                authors[0] = authorsList;
                String[] newAuthors = authors[0].split(",");
                Log.i(LOG_TAG, "TEST: Authors created");


                //Creating book object
                Book book = new Book(title, newAuthors,date,urlLink);
                Log.i(LOG_TAG, "TEST: Book object created");

                books.add(book);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error creating book objects from JSON");
        }

        return books;
    }


    //This method make the HTTP request and returns the raw json from the stream
    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream input = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                input = urlConnection.getInputStream();
                jsonResponse = readStream(input);
            }else{
                jsonResponse = "Bad response";
                Log.e(LOG_TAG, "Error during HTTP request: " + urlConnection.getResponseCode());
            }

        }catch (IOException e){
            Log.e(LOG_TAG, "Error reading HTTP response @" + url);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }

            if(input != null){
                input.close();
            }

        }
        return jsonResponse;
    }


    //This method reads the input stream and return the raw JSON response
    private static String readStream(InputStream input)throws IOException {
        StringBuilder builder = new StringBuilder();

        if(input != null){
            InputStreamReader inReader = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader bf = new BufferedReader(inReader);
            String line = bf.readLine();

            while (line != null){
                builder.append(line);
                line = bf.readLine();
            }
        }
        return builder.toString();
    }
}
