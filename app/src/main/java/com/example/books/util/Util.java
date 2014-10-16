package com.example.books.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;
import android.widget.Toast;

public class Util {
	public static String readBuffer(BufferedReader reader) {
		StringBuffer result = new StringBuffer();
		if (reader != null) {
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				reader.close();
			} catch (IOException e) {
			}
		}
		return result.toString();
	}

	public static void displayShortToast(String str, Context context) {
		Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void displayLongToast(String str, Context context) {
		Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
		toast.show();
	}

	public static boolean isValidEmail(String email) {
        if (email == null || "".equals(email))  {
            return false;
        } else {
            Pattern pattern = Pattern.compile(Constants.REGEX_EMAIL_PATTERN);
            Matcher m = pattern.matcher(email);
            return m.matches();
        }
	}

	public static boolean isNotEmptyString(String fullname) {
		if (fullname == null || fullname.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean arePasswordsValid(String password, String repassword) {
		return areBothPasswordsFilled(password, repassword)
				&& arePasswordsEqual(password, repassword);
	}

	public static boolean areBothPasswordsFilled(String password,
                                                 String repassword) {
		return isNotEmptyString(password) && isNotEmptyString(repassword);
	}

	public static boolean arePasswordsEqual(String password, String repassword) {
		if (password == null || !password.equals(repassword)) {
			return false;
		} else {
			return true;
		}
	}
    public static String readEditText(EditText et)  {
        return et.getText().toString();
    }

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
}
