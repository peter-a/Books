package com.example.books.database;

import com.example.books.datalayer.OnDataRequestCompleteListener;

import org.json.JSONObject;

public interface OnRequestCompleteListener {
	public void onRequestComplete(JSONObject json);
}
