package com.sjmtechs.park.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sjmtechs.park.ParkApp;
import com.sjmtechs.park.R;
import com.sjmtechs.park.retrofit.ApiService;
import com.sjmtechs.park.retrofit.RetroClient;
import com.sjmtechs.park.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jitesh Dalsaniya on 18-Nov-16.
 */
public class ParkPreferencesActivity extends PreferenceActivity{
    private static final String TAG = "ParkPreferencesActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ParkPreferenceFragment()).commit();
    }

    public static class ParkPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    @Override
    public void onBackPressed() {
        final ProgressDialog pd = new ProgressDialog(ParkPreferencesActivity.this);
        pd.setMessage("Saving Preference...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setIndeterminate(false);

        String authToken = ParkApp.preferences.getAuthToken();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String radius = pref.getString("distance","1000");
        String authenticationType = pref.getString("authenticationType","1");
        Log.e(TAG, "onBackPressed: radius " + radius + " authenticationType " + authenticationType);

        if(InternetConnection.checkConnection(ParkPreferencesActivity.this)){
            pd.show();
            ApiService api = RetroClient.getApiService();

            Call<String> call = api.updatePref(authToken,radius,authenticationType);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e(TAG, "onResponse: " + response.isSuccessful());
                    if(response.isSuccessful()){
                        pd.dismiss();
                        getJson(response.body());

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    pd.dismiss();
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private void getJson(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject j = result.getJSONObject(0);
            String code = j.optString("code");
            if(code.equals("1")){
                super.onBackPressed();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
