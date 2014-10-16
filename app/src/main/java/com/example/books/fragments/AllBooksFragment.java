package com.example.books.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.books.R;
import com.example.books.customviews.AdapterBooks;
import com.example.books.database.objects.Book;
import com.example.books.datalayer.Data;
import com.example.books.datalayer.OnDataRequestCompleteListener;
import com.example.books.util.StatusCode;
import com.example.books.util.Util;

public class AllBooksFragment extends Fragment implements AdapterView.OnItemClickListener, OnDataRequestCompleteListener {

	private ListView booksList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_all_books, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		displayBooks();
	}

    private void displayBooks() {
        booksList = (ListView) getActivity().findViewById(
                R.id.all_books_books_list);
        booksList.setOnItemClickListener(this);
        AdapterBooks adapterBooks = new AdapterBooks(getActivity(), Data.getInstance().getAllBooks());
        booksList.setAdapter(adapterBooks);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Book book = Data.getInstance().getBookFromAllBooksByIndex(i);
        int bookId = book.getId();
        String title = book.getTitle();
        String author = book.getAuthor().getName();

        builder.setTitle(title + "\n" + author);
        builder.setMessage(getResources().getString(R.string.button_all_books_dialog_message));
        builder.setPositiveButton(getResources().getText(R.string.button_all_books_dialog_yes_label), new AddToMyBooksHandler(this, bookId));
        builder.setNegativeButton(getResources().getText(R.string.button_all_books_dialog_no_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Util.displayLongToast(Integer.toString(i), getActivity());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDataRequestComplete(Pair<StatusCode, String> result) {
        Util.displayLongToast(result.second, getActivity());
        switch (result.first)  {
            case SUCCESS:
                break;
            case ERROR:
                break;
            case FATAL_ERROR:
                break;
            default:
                break;
        }
    }

    private class AddToMyBooksHandler implements DialogInterface.OnClickListener  {
        private int bookId;
        private OnDataRequestCompleteListener listener;
        public AddToMyBooksHandler(OnDataRequestCompleteListener listener, int bookId)  {
            this.listener = listener;
            this.bookId = bookId;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Data.getInstance().addBookToMyBooks(bookId, listener);
        }
    }
}

