package com.example.books.customviews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.books.R;
import com.example.books.database.objects.Book;

import java.util.List;

/**
 * Created by My on 06.10.2014 г..
 */
public class AdapterBooks extends BaseAdapter {

    private final Context context;
    private final List<Book> bookList;

    public AdapterBooks(Context context, List<Book> bookList)  {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int i) {
        return bookList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        Holder holder = new Holder();
        if(convertView == null)  {
            view = inflater.inflate(R.layout.books_list_row, null);
            holder.tvTitle = (TextView) view.findViewById(R.id.book_title);
            holder.tvAuthor = (TextView) view.findViewById(R.id.book_author);
            holder.tvGenre = (TextView) view.findViewById(R.id.book_genre);
            holder.tvIsbn = (TextView) view.findViewById(R.id.book_isbn);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (Holder) view.getTag();
        }

        Book book = bookList.get(position);

        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor().getName());
        holder.tvGenre.setText(book.getGenre().getName());
        holder.tvIsbn.setText(book.getIsbn());

        return view;
    }

    public class Holder  {
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvGenre;
        public TextView tvIsbn;
    }
}
