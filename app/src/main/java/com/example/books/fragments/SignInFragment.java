package com.example.books.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.books.R;
import com.example.books.activities.BooksActivity;
import com.example.books.database.HttpRequestSenderAsync;
import com.example.books.database.OnRequestCompleteListener;
import com.example.books.datalayer.Data;
import com.example.books.datalayer.OnDataRequestCompleteListener;
import com.example.books.util.Constants;
import com.example.books.util.StatusCode;
import com.example.books.util.Util;

public class SignInFragment extends Fragment implements OnClickListener,
        OnDataRequestCompleteListener{

	private Button buttonSignIn;
	private Button buttonCancel;
	private EditText editTextEmail;
	private EditText editTextPassword;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sign_in, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		buttonSignIn = (Button) getActivity().findViewById(
				R.id.sign_in_button_sign_in);
		buttonCancel = (Button) getActivity().findViewById(
				R.id.sign_in_button_cancel);
		buttonSignIn.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);

		editTextEmail = (EditText) getActivity().findViewById(
				R.id.sign_in_email);
		editTextPassword = (EditText) getActivity().findViewById(
				R.id.sign_in_password);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.sign_in_button_sign_in:
            Data.getInstance().signIn(Util.readEditText(editTextEmail), Util.readEditText(editTextPassword), this);
			break;
		case R.id.sign_in_button_cancel:
			clearTextFields();
			break;
		default:
			break;
		}
	}

    @Override
    public void onDataRequestComplete(Pair<StatusCode, String> result) {
        Activity activity = getActivity();
        switch(result.first)  {
            case ERROR:
                Util.displayLongToast(result.second, activity);
                break;
            case SUCCESS:
                Intent intent = new Intent(getActivity(), BooksActivity.class);
                startActivity(intent);
                break;
            case SIGN_IN_INVALID_EMAIL:
                Util.displayLongToast(getResources().getString(R.string.message_invalid_email), activity);
                break;
            case SIGN_IN_EMPTY_PASSWORD:
                Util.displayLongToast(getResources().getString(R.string.message_sign_in_password_empty), activity);
                break;
            default:
                Util.displayLongToast(getResources().getString(R.string.message_fatal_error), activity);
                break;
        }
    }

	private void clearTextFields() {
		editTextEmail.setText("");
		editTextPassword.setText("");
	}
}
