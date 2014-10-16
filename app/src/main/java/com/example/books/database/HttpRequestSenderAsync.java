package com.example.books.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.example.books.util.Constants;
import com.example.books.util.Util;

public class HttpRequestSenderAsync {
	private String url;
	private List<NameValuePair> postParams;

	public HttpRequestSenderAsync(String url) {
		this.url = url;
	}

	public HttpRequestSenderAsync(String url, List<NameValuePair> postParams) {
		this(url);
		this.postParams = postParams;
	}

	private JSONObject sendRequest() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(this.url);
		HttpResponse httpResponse = null;
		JSONObject jsonObject = null;
		try {
			if (postParams != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(postParams));
			}
			httpResponse = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			try {
				return errorReadingDatabase();
			} catch (JSONException ex) {
				ex.printStackTrace();
			}

		} catch (IOException e) {
			try {
				return errorReadingDatabase();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}

		if (httpResponse != null) {
			BufferedReader reader;
			String jsonString = null;
			try {
				// TODO: extend to get full string (comment inherited from
				// Boris' HttpRequestSender)
				reader = new BufferedReader(new InputStreamReader(httpResponse
						.getEntity().getContent(), "UTF-8"));
				jsonString = Util.readBuffer(reader);
				jsonObject = new JSONObject(jsonString);
			} catch (UnsupportedEncodingException e) {
			} catch (IllegalStateException e) {
			} catch (IOException e) {
			} catch (JSONException e) {
			}
		}
		return jsonObject;
	}

	private JSONObject errorReadingDatabase() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate(Constants.JSON_SUCCESS_KEY, false);
		jsonObject.accumulate(Constants.JSON_MESSAGE_KEY,
				"Error connecting to database.");
		return jsonObject;
	}

	public void sendPostRequest(final OnRequestCompleteListener listener) {
		new AsyncTask<Void, String, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				return sendRequest();
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				listener.onRequestComplete(result);
				super.onPostExecute(result);
			}
		}.execute();
	}
}
