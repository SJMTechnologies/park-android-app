package com.sjmtechs.park.validation;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jitesh Dalsaniya on 5/29/13.
 */

public class Validation {

	static String pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	static String password_pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=\\\\S+$).{8,}$";

	public static boolean setEmailError(EditText editText, String msg) {

		String email = editText.getText().toString();
		if (email.trim().length() > 0) {
			if(email.matches(pattern)){
				editText.setError(null);
				return true;
			}

			int colors = Color.RED;
			ForegroundColorSpan span = new ForegroundColorSpan(colors);
			SpannableStringBuilder builder = new SpannableStringBuilder(msg);
			builder.setSpan(span, 0, msg.length(), 0);
			editText.setError(builder);
		}
		return false;
	}

	public static boolean isValidPassword(final String password){
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(password_pattern);
		matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public static boolean isValidEmail(EditText editText) {

		String email = editText.getText().toString().trim();
		if (email.matches(pattern)) {
			return true;
		} else {
			return false;
		}
	}

	public static void zeroLengthCheck(EditText editText, String msg) {

		String email = editText.getText().toString();
		// onClick of button perform this simplest code.
		if (email.length() > 0) {
			editText.setError(null);
		} else {
			int colors = Color.RED;
			ForegroundColorSpan span = new ForegroundColorSpan(colors);
			SpannableStringBuilder builder = new SpannableStringBuilder(msg);
			builder.setSpan(span, 0, msg.length(), 0);
			editText.setError(builder);
		}
	}
	
	public static void lengthCheck(EditText editText, String msg, int Minlength, int Maxlength) {

		String cvv = editText.getText().toString();
		// onClick of button perform this simplest code.
		if (cvv.length() > Minlength && cvv.length() < Maxlength) {
			editText.setError(null);
		} else {
			int colors = Color.RED;
			ForegroundColorSpan span = new ForegroundColorSpan(colors);
			SpannableStringBuilder builder = new SpannableStringBuilder(msg);
			builder.setSpan(span, 0, msg.length(), 0);
			editText.setError(builder);
		}
	}

	public static boolean isValidMobile(EditText txtPhone) {
		boolean check;
		String phone = txtPhone.getText().toString();
		if (phone.length() < 10) {
			check = false;
		} else {
			check = true;
			txtPhone.setError(null);
		}
		return check;
	}

}
