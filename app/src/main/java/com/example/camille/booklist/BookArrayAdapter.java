package com.example.camille.booklist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by camille on 14/06/2017.
 */

public class BookArrayAdapter extends ArrayAdapter<Book>{

    //Constructor
    public BookArrayAdapter(Activity context, List<Book> books){
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Book book = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_book, parent, false);
        }

        TextView date = (TextView) convertView.findViewById(R.id.published_date);
        date.setText(book.getYearPublished());

        //TODO: Use switch to determine the Oxford coma

        TextView authors = (TextView) convertView.findViewById(R.id.authors);
        String[] newAuthors = Arrays.deepToString(book.getAuthors()).split("\"");
        authors.setText(newAuthors[1]);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(book.getTitle());

        ImageView bookCover = (ImageView) convertView.findViewById(R.id.book_cover);
        bookCover.setImageBitmap(book.getThumbnail());

        return convertView;
    }
}
