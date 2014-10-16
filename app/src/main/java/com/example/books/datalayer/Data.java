package com.example.books.datalayer;

import android.util.Pair;

import com.example.books.database.HttpRequestSenderAsync;
import com.example.books.database.OnRequestCompleteListener;
import com.example.books.database.objects.Author;
import com.example.books.database.objects.Book;
import com.example.books.database.objects.Genre;
import com.example.books.util.Constants;
import com.example.books.util.StatusCode;
import com.example.books.util.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 06.10.2014 г..
 */
public class Data {
    private int userId;
    private String email;
    private String fullname;
    private List<Book> allBooks;
    private List<Book> userBooks;
    private static Data instance;

    private Data() {
    }

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Book> getAllBooks() {
        if (allBooks == null) {
            BooksHandler populator = new BooksHandler(BooksHandler.ALL_BOOKS_REQUEST);
            HttpRequestSenderAsync sender = new HttpRequestSenderAsync(Constants.URL_ALL_BOOKS);
            sender.sendPostRequest(populator);
        }
        return allBooks;
    }

    public List<Book> getUserBooks() {
        if (userBooks == null) {
            fetchUserBooksFromDB();
        }
        return userBooks;
    }


    public Book getBookFromAllBooksByIndex(int arrayIndex) {
        return allBooks.get(arrayIndex);
    }

    //TODO: at least use binary search
    public Book getBookFromAllBooksById(int bookId) {
        for (Book next : allBooks) {
            if (next.getId() == bookId) {
                return next;
            }
        }
        return null;
    }
    public Book getBookFromUserBooksByIndex(int arrayIndex)  {
        return userBooks.get(arrayIndex);
    }

    //TODO: at least use binary search
    public Book getBookFromUserBooksById(int bookId)  {
        for (Book next : userBooks) {
            if (next.getId() == bookId) {
                return next;
            }
        }
        return null;
    }

    //TODO: at least use binary search
    private int getBookIndex(int bookId, List<Book> books)  {
        for(int i = 0; i < books.size(); i++)  {
            if(books.get(i).getId() == bookId)  {
                return i;
            }
        }
        return -1;
    }

    private void fetchUserBooksFromDB() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        BasicNameValuePair pair = new BasicNameValuePair(Constants.PARAM_ID_USER, Integer.toString(userId));
        params.add(pair);
        HttpRequestSenderAsync sender = new HttpRequestSenderAsync(Constants.URL_MY_BOOKS, params);
        sender.sendPostRequest(new BooksHandler(BooksHandler.MY_BOOKS_REQUEST));
    }

    public void addBookToMyBooks(int bookId, OnDataRequestCompleteListener listener) {
        HttpRequestSenderAsync sender = new HttpRequestSenderAsync(Constants.URL_ADD_BOOK, populateBookAndUserParams(bookId, userId));
        sender.sendPostRequest(new AddBookHandler(listener, bookId));
    }

    public void removeBookFromLibrary(int bookId, OnDataRequestCompleteListener listener) {
        HttpRequestSenderAsync sender = new HttpRequestSenderAsync(Constants.URL_REMOVE_BOOK, populateBookAndUserParams(bookId, userId));
        sender.sendPostRequest(new RemoveBookHandler(listener, bookId));
    }

    public void signIn(String email, String password, OnDataRequestCompleteListener listener) {
        Pair<StatusCode, String> result = null;
        if (!Util.isValidEmail(email)) {
            result = new Pair<StatusCode, String>(StatusCode.SIGN_IN_INVALID_EMAIL, null);
        } else if (!Util.isNotEmptyString(password)) {
            result = new Pair<StatusCode, String>(StatusCode.SIGN_IN_EMPTY_PASSWORD, null);
        }
        if (result != null) {
            listener.onDataRequestComplete(result);
            return;
        } else {
            HttpRequestSenderAsync sender = new HttpRequestSenderAsync(Constants.URL_SIGN_IN, populateSignInParams(email, password));
            sender.sendPostRequest(new SingInHandler(listener));
        }
    }

    public void signUp(String email, String fullname, String password, String repassword, OnDataRequestCompleteListener listener) {
        Pair<StatusCode, String> result = null;
        if (!Util.isValidEmail(email)) {
            result = new Pair<StatusCode, String>(StatusCode.SIGN_UP_INVALID_EMAIL, null);
        } else if (!Util.isNotEmptyString(fullname)) {
            result = new Pair<StatusCode, String>(StatusCode.SIGN_UP_INVALID_FULLNAME, null);
        } else if (!Util.areBothPasswordsFilled(password, repassword)) {
            result = new Pair<StatusCode, String>(StatusCode.SIGN_UP_ARE_NOT_BOTH_PASSWORDS_FILLED, null);
        } else if (!Util.arePasswordsEqual(password, repassword)) {
            result = new Pair<StatusCode, String>(StatusCode.SIGN_UP_ARE_NOT_PASSWORDS_EQUAL, null);
        }
        if (result != null) {
            listener.onDataRequestComplete(result);
            return;
        } else {
            HttpRequestSenderAsync sender = new HttpRequestSenderAsync(Constants.URL_SIGN_UP, populateSignUpParams(email, fullname, password));
            sender.sendPostRequest(new SignUpHandler(listener));
        }
    }

    private List<NameValuePair> populateBookAndUserParams(int bookId, int userId) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(Constants.PARAM_ID_BOOK, Integer.toString(bookId)));
        params.add(new BasicNameValuePair(Constants.PARAM_ID_USER, Integer.toString(userId)));
        return params;
    }

    private List<NameValuePair> populateSignInParams(String email, String password) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(Constants.PARAM_EMAIL, email));
        params.add(new BasicNameValuePair(Constants.PARAM_PASSWORD, password));
        return params;
    }

    private List<NameValuePair> populateSignUpParams(String email, String fullname, String password) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(Constants.PARAM_EMAIL, email));
        params.add(new BasicNameValuePair(Constants.PARAM_FULL_NAME, fullname));
        params.add(new BasicNameValuePair(Constants.PARAM_PASSWORD, password));
        return params;
    }

    private class BooksHandler implements OnRequestCompleteListener {
        public static final String ALL_BOOKS_REQUEST = "all_books";
        public static final String MY_BOOKS_REQUEST = "my_books";
        private String requestType;

        public BooksHandler(String requestType) {
            this.requestType = requestType;
        }

        @Override
        public void onRequestComplete(JSONObject json) {
            try {
                String books = json.getString(Constants.JSON_MESSAGE_KEY);
                JSONArray booksArray = new JSONArray(books);
                List<Book> bookList = new ArrayList<Book>(booksArray.length());
                for (int i = 0; i < booksArray.length(); i++) {
                    JSONObject nextBook = booksArray.getJSONObject(i);
                    Author author = new Author(
                            nextBook.getString(Constants.JSON_AUTHOR_KEY));
                    Genre genre = new Genre(
                            nextBook.getString(Constants.JSON_GENRE_KEY));
                    int bookId = nextBook.getInt(Constants.JSON_ID_KEY);
                    String bookTitle = nextBook.getString(Constants.JSON_TITLE_KEY);
                    String bookISBN = nextBook.getString(Constants.JSON_ISBN_KEY);
                    bookList.add(new Book(bookId, bookISBN, bookTitle, author, genre));
                }
                if (requestType.equals(ALL_BOOKS_REQUEST)) {
                    allBooks = bookList;
                } else if (requestType.equals(MY_BOOKS_REQUEST)) {
                    userBooks = bookList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class SingInHandler implements OnRequestCompleteListener {

        private OnDataRequestCompleteListener listener;

        public SingInHandler(OnDataRequestCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        public void onRequestComplete(JSONObject json) {
            Pair<StatusCode, String> result;
            try {
                if (json.getBoolean(Constants.JSON_SUCCESS_KEY)) {
                    userId = json.getInt(Constants.JSON_MESSAGE_KEY);
                    getUserBooks();
                    result = new Pair<StatusCode, String>(StatusCode.SUCCESS, null);
                } else {
                    result = new Pair<StatusCode, String>(StatusCode.ERROR, json.getString(Constants.JSON_MESSAGE_KEY));
                }
                listener.onDataRequestComplete(result);
            } catch (JSONException e) {
                e.printStackTrace();
                result = new Pair<StatusCode, String>(StatusCode.FATAL_ERROR, null);
                listener.onDataRequestComplete(result);
            }
        }
    }

    private class SignUpHandler implements OnRequestCompleteListener {
        private OnDataRequestCompleteListener listener;

        public SignUpHandler(OnDataRequestCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        public void onRequestComplete(JSONObject json) {
            Pair<StatusCode, String> result = null;
            try {
                if (json.getBoolean(Constants.JSON_SUCCESS_KEY)) {
                    result = new Pair<StatusCode, String>(StatusCode.SUCCESS, null);
                } else {
                    result = new Pair<StatusCode, String>(StatusCode.ERROR, json.getString(Constants.JSON_MESSAGE_KEY));
                }
                listener.onDataRequestComplete(result);
            } catch (JSONException e) {
                e.printStackTrace();
                result = new Pair<StatusCode, String>(StatusCode.FATAL_ERROR, null);
                listener.onDataRequestComplete(result);
            }
        }
    }

    private class AddBookHandler implements OnRequestCompleteListener {
        private OnDataRequestCompleteListener listener;
        private int bookId;

        public AddBookHandler(OnDataRequestCompleteListener listener, int bookId) {
            this.listener = listener;
            this.bookId = bookId;
        }

        @Override
        public void onRequestComplete(JSONObject json) {
            Pair<StatusCode, String> result = null;
            try {
                //TODO: optimize or at least use binary search
                if (json.getBoolean(Constants.JSON_SUCCESS_KEY)) {
                    for (int i = 0; i < allBooks.size(); i++) {
                        if (allBooks.get(i).getId() == bookId) {
                            userBooks.add(allBooks.get(i));
                        }
                    }
                    result = new Pair<StatusCode, String>(StatusCode.SUCCESS, json.getString(Constants.JSON_MESSAGE_KEY));
                } else {
                    result = new Pair<StatusCode, String>(StatusCode.ERROR, json.getString(Constants.JSON_MESSAGE_KEY));
                }
                listener.onDataRequestComplete(result);
            } catch (JSONException e) {
                e.printStackTrace();
                result = new Pair<StatusCode, String>(StatusCode.FATAL_ERROR, null);
                listener.onDataRequestComplete(result);
            }
        }
    }

    private class RemoveBookHandler implements OnRequestCompleteListener {
        private OnDataRequestCompleteListener listener;
        private int bookId;

        public RemoveBookHandler(OnDataRequestCompleteListener listener, int bookId)  {
            this.listener = listener;
            this.bookId = bookId;
        }
        @Override
        public void onRequestComplete(JSONObject json) {
            Pair<StatusCode, String> result = null;
            StatusCode code;
            String message;
            try {
                if (json.getBoolean(Constants.JSON_SUCCESS_KEY)) {
                    code = StatusCode.SUCCESS;
                } else {
                    code = StatusCode.ERROR;
                }
                message = json.getString(Constants.JSON_MESSAGE_KEY);
                result = new Pair<StatusCode, String>(code, message);
                listener.onDataRequestComplete(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
