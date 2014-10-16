package com.example.books.fragments;

import com.example.books.R;
import com.example.books.customviews.AdapterBooks;
import com.example.books.database.objects.Book;
import com.example.books.datalayer.Data;
import com.example.books.datalayer.OnDataRequestCompleteListener;
import com.example.books.util.StatusCode;
import com.example.books.util.Util;

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

public class MyBooksFragment extends Fragment implements AdapterView.OnItemClickListener, OnDataRequestCompleteListener {

    ListView myBooksList;
    AdapterBooks myBooksListAdapter;
    private int removedBookIndex;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my_books, container, false);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        displayBooks();
    }

    private void displayBooks() {
        myBooksList = (ListView) getActivity().findViewById(
                R.id.my_books_books_list);
        myBooksList.setOnItemClickListener(this);
        myBooksListAdapter = new AdapterBooks(getActivity(), Data.getInstance().getUserBooks());
        myBooksList.setAdapter(myBooksListAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Book book = Data.getInstance().getBookFromUserBooksByIndex(i);
        int bookId = book.getId();
        removedBookIndex = i;
        String title = book.getTitle();
        String author = book.getAuthor().getName();
        builder.setTitle(title + "\n" + author);
        builder.setMessage(getResources().getString(R.string.button_my_books_message));
        builder.setPositiveButton(getResources().getString(R.string.button_my_books_yes_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.button_my_books_no_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        //Data.getInstance().removeBookFromLibrary(bookId, this);
    }

    @Override
    public void onDataRequestComplete(Pair<StatusCode, String> result) {
        switch (result.first)  {
            case SUCCESS:
                //TODO: implement a better variant of this functionality. Currently this is just a hack to see it works.
                Data.getInstance().getUserBooks().remove(removedBookIndex);
                myBooksListAdapter.notifyDataSetChanged();
                break;
            case ERROR:
                break;
            default:
                break;
        }
        Util.displayLongToast(result.second, getActivity());
    }
}
