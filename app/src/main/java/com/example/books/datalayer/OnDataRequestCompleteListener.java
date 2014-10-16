package com.example.books.datalayer;

import android.util.Pair;

import com.example.books.util.StatusCode;

import org.json.JSONObject;

/**
 * Created by My on 07.10.2014 г..
 */
public interface OnDataRequestCompleteListener {
    public void onDataRequestComplete(Pair<StatusCode, String> result);
}
