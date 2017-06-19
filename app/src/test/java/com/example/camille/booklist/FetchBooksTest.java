package com.example.camille.booklist;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by camille on 15/06/2017.
 */

public class FetchBooksTest {
    @Test
    public void fetch_is_correct() throws Exception{
        List<Book> books = Collections.EMPTY_LIST;
        books = QueryUtils.fetchBooks("https://www.googleapis.com/books/v1/volumes?q=quilting&maxResults=1");
        Book book = books.get(0);

        assertEquals(book.getTitle(), "Quilting");
        assertEquals(book.getYearPublished(), "1991");
        assertEquals(book.geturlLink(), "http://books.google.co.id/books?id=BdoSnRE9b8wC&dq=quilting&hl=&source=gbs_api");
        //assertEquals(book.getRating(), 0.0, 0.1);

    }

    @Test
    public void httpRequest_isCorrect() throws Exception{
        URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=quilting&maxResults=1");
        String jsonResponse = QueryUtils.makeHTTPRequest(url);

        assertEquals(jsonResponse, "Bad response");
    }
}
