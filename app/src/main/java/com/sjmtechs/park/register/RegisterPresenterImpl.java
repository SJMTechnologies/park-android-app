package com.sjmtechs.park.register;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.sjmtechs.park.R;
import com.sjmtechs.park.model.Register;
import com.sjmtechs.park.retrofit.ApiService;
import com.sjmtechs.park.retrofit.RetroClient;
import com.sjmtechs.park.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenterImpl implements RegisterPresenter {

    private static final String TAG = "RegisterPresenterImpl";
    private RegisterView registerView;
    private Context context;

    public RegisterPresenterImpl(Context context, RegisterView registerView) {
        this.registerView = registerView;
        this.context = context;
    }

    @Override
    public void onRegisteredClicked() {

        Register register = registerView.getRegisterData();
        if (TextUtils.isEmpty(register.getFirstName())) {
            registerView.showError(R.string.please_enter_first_name);
            return;
        }

        if (TextUtils.isEmpty(register.getLastName())) {
            registerView.showError(R.string.please_enter_last_name);
            return;
        }

        if (TextUtils.isEmpty(register.getEmail())) {
            registerView.showError(R.string.please_email);
        } else {
            Log.e(TAG, "onRegisteredClicked: Email Validation IN " + Patterns.EMAIL_ADDRESS.matcher(register.getEmail()).matches());
            if(!Patterns.EMAIL_ADDRESS.matcher(register.getEmail()).matches()){
                registerView.showError(R.string.valid_email);
                return;
            }
        }

        if (TextUtils.isEmpty(register.getTelephone())) {
            registerView.showError(R.string.please_enter_mobile);
            return;
        }

        if (TextUtils.isEmpty(register.getAddressOne())) {
            registerView.showError(R.string.please_enter_address);
            return;
        }

        if (TextUtils.isEmpty(register.getCity())) {
            registerView.showError(R.string.please_enter_city);
            return;
        }

        if (TextUtils.isEmpty(register.getPostalCode())) {
            registerView.showError(R.string.please_enter_postal_code);
            return;
        }

        if (TextUtils.isEmpty(register.getPassword())) {
            registerView.showError(R.string.please_enter_password);
            return;
        } else {
            String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
            if (!register.getPassword().matches(PASSWORD_PATTERN)) {
                registerView.showError(R.string.password_message);
                return;
            }
        }

        if (TextUtils.isEmpty(register.getConfirmPassword())) {
            registerView.showError(R.string.please_enter_confirm_password);
            return;
        }

        if (!TextUtils.isEmpty(register.getPassword()) && !TextUtils.isEmpty(register.getConfirmPassword()) &&
                !register.getPassword().equals(register.getConfirmPassword())) {
            registerView.showError(R.string.password_same);
            return;
        }

        if (InternetConnection.checkConnection(context)) {
            ApiService api = RetroClient.getApiService();
            String phone = register.getTelephone();
            String p = phone.replace("(","").replace(" ", "").replace(")", "").replace("-", "");
            Log.e(TAG, "onRegisteredClicked: phone " + p);
            Call<String> call = api.doSignUp(register.getEmail().trim(), register.getPassword(), encode(register.getFirstName()),
                    encode(register.getLastName()), encode(register.getBusinessName()), encode(register.getAddressOne()),
                    encode(register.getAddressTwo()), encode(register.getCity()), encode(register.getRegionOrState()),
                    encode(register.getPostalCode()), encode(register.getCountry()), encode(p),
                    encode(register.getFax()), encode(register.getSubscribe()));

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e(TAG, "onResponse: Error " + response.message());
                    Log.e(TAG, "onResponse: body " + response.body());
                    parseJson(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "onFailure: ");
                }
            });
        } else {
            registerView.showError(R.string.connect_internet);
        }
    }

    private void parseJson(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("result");
                JSONObject result = array.getJSONObject(0);
                String code = result.optString("code");
                String msg = result.optString("message");
                Log.e(TAG, "parseJson: response " + jsonObject.toString());
                if (code.equals("1")) {
                    registerView.showDialog(msg);
                } else {
                    registerView.showError(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "parseJson: response Null");
        }
    }

    private String encode(String s){
        try {
            return URLEncoder.encode(s,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
