package com.sjmtechs.park.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.sjmtechs.park.R;
import com.sjmtechs.park.adapter.CountryAdapter;
import com.sjmtechs.park.adapter.StateAdapter;
import com.sjmtechs.park.model.Country;
import com.sjmtechs.park.model.Register;
import com.sjmtechs.park.model.State;
import com.sjmtechs.park.register.RegisterPresenterImpl;
import com.sjmtechs.park.register.RegisterView;
import com.sjmtechs.park.retrofit.ApiService;
import com.sjmtechs.park.retrofit.RetroClient;
import com.sjmtechs.park.utils.InternetConnection;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RegisterView {


    private static final String TAG = RegisterActivity.class.getSimpleName();
    BaseAnimatorSet bas_in;
    BaseAnimatorSet bas_out;
    @InjectView(R.id.etFirstName)
    EditText etFirstName;

    @InjectView(R.id.etLastName)
    EditText etLastName;

    @InjectView(R.id.etEmail)
    EditText etEmail;

    @InjectView(R.id.etTelephone)
    EditText etTelephone;

    @InjectView(R.id.etAddressOne)
    EditText etAddressOne;

    @InjectView(R.id.etAddressTwo)
    EditText etAddressTwo;

    @InjectView(R.id.etCity)
    EditText etCity;

    @InjectView(R.id.etPostalCode)
    EditText etPostalCode;

    @InjectView(R.id.etPassword)
    EditText etPassword;

    @InjectView(R.id.etPasswordConfirm)
    EditText etPasswordConfirm;

    @InjectView(R.id.etBusinessName)
    EditText etBusinessName;

    @InjectView(R.id.etFax)
    EditText etFax;

    @InjectView(R.id.rgSubscribe)
    RadioGroup rgSubscribe;

    @InjectView(R.id.rdSubscribeYes)
    RadioButton rdSubscribeYes;

    @InjectView(R.id.rdSubscribeNo)
    RadioButton rdSubscribeNo;

    @InjectView(R.id.chkPrivacyPolicy)
    CheckBox chkPrivacyPolicy;

    @InjectView(R.id.btnContinue)
    Button btnContinue;

    @InjectView(R.id.spCountry)
    Spinner spCountry;

    @InjectView(R.id.spRegionOrState)
    Spinner spRegionOrState;

    @InjectView(R.id.countryProgressBar)
    ProgressBar countryProgressBar;

    @InjectView(R.id.regionOrStateProgressBar)
    ProgressBar regionOrStateProgressBar;

    private List<Country> countryList;
    private List<State> stateList;
    private RegisterPresenterImpl registerPresenter;
    InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.length() > 0) {

                if (!Character.isDigit(source.charAt(0)))
                    return "";
                else {
                    if (dstart == 3) {
                        return source + ") ";
                    } else if (dstart == 0) {
                        return "(" + source;
                    } else if ((dstart == 5) || (dstart == 9))
                        return "-" + source;
                    else if (dstart >= 16)
                        return "";
                }

            }
            return null;
        }
    };

    private String strSubscribe = "0";
    private String strCountry = "";
    private String strRegionOrState = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        registerPresenter = new RegisterPresenterImpl(RegisterActivity.this, this);
//        db = new Databasehelper(RegisterActivity.this);
        rgSubscribe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.rdSubscribeYes) {
                    strSubscribe = "1";
                } else if (id == R.id.rdSubscribeNo) {
                    strSubscribe = "0";
                }
            }
        });
        bas_in = new FlipVerticalSwingEnter();
        bas_out = new FadeExit();

        countryList = new ArrayList<>();
        stateList = new ArrayList<>();
        loadCountryAndState();
        etTelephone.setFilters(new InputFilter[]{filter});
        etFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (etFirstName.getText().toString().length() > 0) {
                    String upperString = etFirstName.getText().toString().substring(0, 1).toUpperCase() + etFirstName.getText().toString().substring(1);
                    etFirstName.setText(upperString);
                }
            }
        });
        etLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (etLastName.getText().toString().length() > 0) {
                    String upperString = etLastName.getText().toString().substring(0, 1).toUpperCase() + etLastName.getText().toString().substring(1);
                    etLastName.setText(upperString);
                }
            }
        });

        etAddressOne.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (etAddressOne.getText().toString().length() > 0) {
                    String upperString = etAddressOne.getText().toString().substring(0, 1).toUpperCase() + etAddressOne.getText().toString().substring(1);
                    etAddressOne.setText(upperString);
                }
            }
        });

        etAddressTwo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (etAddressTwo.getText().toString().length() > 0) {
                    String upperString = etAddressTwo.getText().toString().substring(0, 1).toUpperCase() + etAddressTwo.getText().toString().substring(1);
                    etAddressTwo.setText(upperString);
                }
            }
        });

        etCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (etCity.getText().toString().length() > 0) {
                    String upperString = etCity.getText().toString().substring(0, 1).toUpperCase() + etCity.getText().toString().substring(1);
                    etCity.setText(upperString);
                }
            }
        });


        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
                if (!charSequence.toString().matches(PASSWORD_PATTERN)) {
                    int colors = Color.RED;
                    String msg = getString(R.string.valid_email);
                    ForegroundColorSpan span = new ForegroundColorSpan(colors);
                    SpannableStringBuilder builder = new SpannableStringBuilder(msg);
                    builder.setSpan(span, 0, msg.length(), 0);
                    etEmail.setError(builder);
                } else {
                    etEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String s = charSequence.toString();
                String msg = "Password and confirm password should be same.";
                if (!s.equals(etPassword.getText().toString())) {
                    int colors = Color.RED;
                    ForegroundColorSpan span = new ForegroundColorSpan(colors);
                    SpannableStringBuilder builder = new SpannableStringBuilder(msg);
                    builder.setSpan(span, 0, msg.length(), 0);
                    etPasswordConfirm.setError(builder);
                } else {
                    etPasswordConfirm.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadCountryAndState() {
        if (InternetConnection.checkConnection(RegisterActivity.this)) {
            countryProgressBar.setVisibility(View.VISIBLE);

            ApiService api = RetroClient.getApiService();

            Call<String> call = api.getCountry();
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    Log.e(TAG, "onResponse: " + response.isSuccessful());
                    if (response.isSuccessful()) {
                        countryProgressBar.setVisibility(View.GONE);
                        String json = response.body();
                        parseJson(json);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "onFailure: ");
                    countryProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject rows = result.getJSONObject("rows");
            for (int i = 0; i < rows.length() - 1; i++) {
                JSONObject j = rows.getJSONObject("" + i);
                Country c = new Country();
                c.setId(j.optString(Country.COUNTRY_ID));
                c.setName(j.optString(Country.COUNTRY_NAME));
                countryList.add(c);
            }

            CountryAdapter adapter = new CountryAdapter(RegisterActivity.this,
                    android.R.layout.simple_spinner_item, countryList);

            Log.e(TAG, "parseJson: countryList " + countryList.size());
            Log.e(TAG, "parseJson: countryList Adapter " + adapter.getCount());
            adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
            spCountry.setAdapter(adapter);
            spCountry.setOnItemSelectedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.btnContinue)
    public void onContinuePressed() {
        registerPresenter.onRegisteredClicked();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

        switch (parent.getId()) {
            case R.id.spCountry:
                Country country = (Country) parent.getItemAtPosition(position);
                String id = country.getId();
                String name = country.getName();
                Log.e(TAG, "onItemSelected: id " + id + " name " + name);
                loadStateFromCountryCode(id);
                strCountry = id;
                break;
            case R.id.spRegionOrState:
                State state = (State) parent.getItemAtPosition(position);
                strRegionOrState = state.getId();
                Log.e(TAG, "onItemSelected: state Id " + strRegionOrState);
                break;
        }
    }

    private void loadStateFromCountryCode(String id) {

        if (InternetConnection.checkConnection(RegisterActivity.this)) {
            regionOrStateProgressBar.setVisibility(View.VISIBLE);
            ApiService api = RetroClient.getApiService();

            Call<String> call = api.getStates(id);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    regionOrStateProgressBar.setVisibility(View.GONE);
                    Log.e(TAG, "onResponse: state " + response.isSuccessful());
                    if (response.isSuccessful()) {
                        Log.e(TAG, "onResponse: states list " + response.body());
                        parseStateJson(response.body());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "onFailure: State");
                    regionOrStateProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void parseStateJson(String json) {
        try {
            stateList.clear();
            JSONObject jsonObject = new JSONObject(json);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject rows = result.getJSONObject("rows");
            for (int i = 0; i < rows.length() - 1; i++) {
                JSONObject j = rows.getJSONObject("" + i);
                State s = new State();
                s.setId(j.optString(State.STATE_ZONE_ID));
                s.setName(j.optString(State.STATE_NAME));
                stateList.add(s);
            }

            StateAdapter adapter = new StateAdapter(RegisterActivity.this,
                    android.R.layout.simple_spinner_item, stateList);

            Log.e(TAG, "parseJson: stateList " + stateList.size());
            Log.e(TAG, "parseJson: stateList Adapter " + adapter.getCount());
            adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
            spRegionOrState.setAdapter(adapter);
            spRegionOrState.setOnItemSelectedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public Register getRegisterData() {
        String strFirstName = etFirstName.getText().toString();
        String strLastName = etLastName.getText().toString();
        String strEmail = etEmail.getText().toString();
        String strTelephone = etTelephone.getText().toString();
        String strAddressOne = etAddressOne.getText().toString();
        String strAddressTwo = etAddressTwo.getText().toString();
        String strCity = etCity.getText().toString();
        String strPostalCode = etPostalCode.getText().toString();
        String strPassword = etPassword.getText().toString();
        String strConfirmPassword = etPasswordConfirm.getText().toString();
        String strBusinessName = etBusinessName.getText().toString();
        String strFax = etFax.getText().toString();

        Register register = new Register();
        register.setFirstName(strFirstName);
        register.setLastName(strLastName);
        register.setEmail(strEmail);
        register.setTelephone(strTelephone);
        register.setAddressOne(strAddressOne);
        register.setAddressTwo(strAddressTwo);
        register.setCity(strCity);
        register.setPostalCode(strPostalCode);
        register.setPassword(strPassword);
        register.setBusinessName(strBusinessName);
        register.setFax(strFax);
        register.setCountry(strCountry);
        register.setRegionOrState(strRegionOrState);
        register.setConfirmPassword(strConfirmPassword);
        register.setSubscribe(strSubscribe);

        return register;
    }

    @Override
    public void showError(int resId) {
        showSnackBar(resId);
    }

    @Override
    public void showError(String msg) {
        showSnackBar(msg);
    }

    @Override
    public void startActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        RegisterActivity.this.finish();
    }

    @Override
    public void showDialog(String msg) {
        showSuccessDialog(msg);
    }

    private void showSnackBar(int resId) {
        Snackbar.make(btnContinue, resId, Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackBar(String resId) {
        Snackbar.make(btnContinue, resId, Snackbar.LENGTH_SHORT).show();
    }

    private void showSuccessDialog(String msg) {

        final MaterialDialog dialog = new MaterialDialog(RegisterActivity.this);
        OnBtnClickL onBtnClickL1 = new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
                startActivity();
            }
        };

        OnBtnClickL onBtnClickL2 = new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
            }
        };


        OnBtnClickL[] clickLs = new OnBtnClickL[]{onBtnClickL2,onBtnClickL1};
        dialog.setOnBtnClickL(clickLs);
        dialog.isTitleShow(false)
                .content(msg)
                .btnText("Okay","")
                .showAnim(bas_in)
                .dismissAnim(bas_out)
                .show();

    }
}
