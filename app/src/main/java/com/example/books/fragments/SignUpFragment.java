package com.example.books.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.books.R;
import com.example.books.database.HttpRequestSenderAsync;
import com.example.books.database.OnRequestCompleteListener;
import com.example.books.datalayer.Data;
import com.example.books.datalayer.OnDataRequestCompleteListener;
import com.example.books.util.Constants;
import com.example.books.util.StatusCode;
import com.example.books.util.Util;

public class SignUpFragment extends Fragment implements OnClickListener,
        OnDataRequestCompleteListener  {

	private Button buttonSignUp;
	private Button buttonCancel;
	private EditText editTextEmail;
	private EditText editTextFullname;
	private EditText editTextPassowrd;
	private EditText editTextReEnterPassword;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sing_up, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		buttonSignUp = (Button) getActivity().findViewById(
				R.id.sign_up_button_sign_up);
		buttonCancel = (Button) getActivity().findViewById(
				R.id.sign_up_button_cancel);
		buttonSignUp.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);
		editTextEmail = (EditText) getActivity().findViewById(
				R.id.sign_up_email);
		editTextFullname = (EditText) getActivity().findViewById(
				R.id.sign_up_fullname);
		editTextPassowrd = (EditText) getActivity().findViewById(
				R.id.sign_up_password);
		editTextReEnterPassword = (EditText) getActivity().findViewById(
				R.id.sign_up_reenter_password);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.sign_up_button_sign_up:
            Data.getInstance().signUp(Util.readEditText(editTextEmail), Util.readEditText(editTextFullname), Util.readEditText(editTextPassowrd), Util.readEditText(editTextReEnterPassword), this);
			break;
		case R.id.sign_up_button_cancel:
			clearTextFields();
			break;
		default:
			break;
		}
	}

    @Override
    public void onDataRequestComplete(Pair<StatusCode, String> result) {
        Activity activity = getActivity();
        switch (result.first)  {
            case SUCCESS:
                Util.displayLongToast(getResources().getString(R.string.message_registration_successful),activity);
                break;
            case ERROR:
                Util.displayLongToast(result.second, activity);
                break;
            case SIGN_UP_INVALID_EMAIL:
                Util.displayLongToast(getResources().getString(R.string.message_invalid_email), activity);
                break;
            case SIGN_UP_INVALID_FULLNAME:
                Util.displayLongToast(getResources().getString(R.string.message_invalid_fullname),activity);
                break;
            case SIGN_UP_ARE_NOT_BOTH_PASSWORDS_FILLED:
                Util.displayLongToast(getResources().getString(R.string.message_empty_sign_up_password),activity);
                break;
            case SIGN_UP_ARE_NOT_PASSWORDS_EQUAL:
                Util.displayLongToast(getResources().getString(R.string.message_passwords_dont_match),activity);
                break;
            default:
                Util.displayLongToast(getResources().getString(R.string.message_fatal_error), activity);
                break;
        }
    }

	private void clearTextFields() {
		editTextEmail.setText("");
		editTextFullname.setText("");
		editTextPassowrd.setText("");
		editTextReEnterPassword.setText("");
	}


}
