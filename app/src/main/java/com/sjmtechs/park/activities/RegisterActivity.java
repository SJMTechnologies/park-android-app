package com.sjmtechs.park.activities;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.sjmtechs.park.R;
import com.sjmtechs.park.validation.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
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

            } else {

            }
            return null;
        }
    };

    //    private Databasehelper db;
    private String strFirstName = "", strLastName = "", strEmail = "", strTelephone = "",
            strAddressOne = "", strAddressTwo = "", strCity = "", strPostalCode = "", strPassword = "",
            strConfirmPassword = "", strSubscribe = "No", strCountry = "", strRegionOrState = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

//        db = new Databasehelper(RegisterActivity.this);
        rgSubscribe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.rdSubscribeYes) {
                    strSubscribe = "Yes";
                } else if (id == R.id.rdSubscribeNo) {
                    strSubscribe = "No";
                }
            }
        });

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

        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                CharSequence charSequence = editable.toString()

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


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

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "((?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @OnClick(R.id.btnContinue)
    public void onContinuePressed() {
        boolean isAllDataEntered = false;
        strFirstName = etFirstName.getText().toString();
        strLastName = etLastName.getText().toString();
        strEmail = etEmail.getText().toString();
        strTelephone = etTelephone.getText().toString();
        strAddressOne = etAddressOne.getText().toString();
        strAddressTwo = etAddressTwo.getText().toString();
        strCity = etCity.getText().toString();
        strPostalCode = etPostalCode.getText().toString();
        strPassword = etPassword.getText().toString();
        strConfirmPassword = etPasswordConfirm.getText().toString();
        strCountry = spCountry.getSelectedItem().toString();
        strRegionOrState = spRegionOrState.getSelectedItem().toString();

        if (strFirstName != null && strFirstName.length() == 0) {
            Snackbar.make(btnContinue, "Please Enter First Name", Snackbar.LENGTH_LONG).show();
        } else if (strLastName != null && strLastName.length() == 0) {
            Snackbar.make(btnContinue, "Please Enter Last Name", Snackbar.LENGTH_LONG).show();
        } else if (strEmail != null && strEmail.length() == 0) {
            Snackbar.make(btnContinue, "Please Enter Email", Snackbar.LENGTH_LONG).show();
        } else if (strTelephone != null && strTelephone.length() == 0) {
            Snackbar.make(btnContinue, "Please Enter Telephone or Mobile number", Snackbar.LENGTH_LONG).show();
        } else if (strAddressOne != null && strAddressOne.length() == 0) {
            Snackbar.make(btnContinue, "Please Enter Address", Snackbar.LENGTH_LONG).show();
        } else if (strCity != null && strCity.length() == 0) {
            Snackbar.make(btnContinue, "Please Enter City", Snackbar.LENGTH_LONG).show();
        } else if (strPostalCode != null && strPostalCode.length() == 0) {
            Snackbar.make(btnContinue, "Please Enter Postal Code", Snackbar.LENGTH_LONG).show();
        } else if (strPassword != null && strPassword.length() == 0) {
            Snackbar.make(btnContinue, "Please Enter Password", Snackbar.LENGTH_LONG).show();
        } else if (strConfirmPassword != null && strConfirmPassword.length() == 0) {
            Snackbar.make(btnContinue, "Please Enter Confirm Password", Snackbar.LENGTH_LONG).show();
        } else if (strPassword != null && strConfirmPassword != null && !strPassword.equals(strConfirmPassword)) {
            Snackbar.make(btnContinue, "Password and confirm password should be same.", Snackbar.LENGTH_LONG).show();
        } else if (strCountry != null && strCountry.length() == 0) {
            Snackbar.make(btnContinue, "Please Select Country", Snackbar.LENGTH_LONG).show();
        } else if (strRegionOrState != null && strRegionOrState.length() == 0) {
            Snackbar.make(btnContinue, "Please Select State", Snackbar.LENGTH_LONG).show();
        } else if (strPassword != null && strPassword.length() < 8) {
            String msg = "Password should be contain one lowercase letter, one uppercase letter and one number.";
            Log.e(TAG, "onTextChanged: " + isValidPassword(strPassword));
            if (isValidPassword(strPassword)) {
                Log.e(TAG, "onTextChanged: inside if");
                etPassword.setError(null);
            } else {
                int colors = Color.RED;
                ForegroundColorSpan span = new ForegroundColorSpan(colors);
                SpannableStringBuilder builder = new SpannableStringBuilder(msg);
                builder.setSpan(span, 0, msg.length(), 0);
                etPassword.setError(builder);
            }
            Snackbar.make(btnContinue, msg, Snackbar.LENGTH_LONG).show();
        } else if (!chkPrivacyPolicy.isChecked()) {
            Snackbar.make(btnContinue, "Please Agree terms and condition.", Snackbar.LENGTH_LONG).show();
        } else {
            isAllDataEntered = true;
        }

        boolean isValidEmail = Validation.setEmailError(etEmail, "Please enter valid email address.");
//        Log.e(TAG, "onContinuePressed: isValidEmail " + isValidEmail + " isAllDataEntered " + isAllDataEntered);
//        if (isAllDataEntered && isValidEmail) {
//            boolean userExist = db.checkUser(strEmail);
//            Log.e(TAG, "onContinuePressed: userExist " + userExist);
//            if (!userExist) {
//                Register reg = new Register();
//                strFirstName = etFirstName.getText().toString();
//                strLastName = etLastName.getText().toString();
//                strEmail = etEmail.getText().toString();
//                strTelephone = etTelephone.getText().toString();
//                strAddressOne = etAddressOne.getText().toString();
//                strAddressTwo = etAddressTwo.getText().toString();
//                strCity = etCity.getText().toString();
//                strPostalCode = etPostalCode.getText().toString();
//                strPassword = etPassword.getText().toString();
//                strConfirmPassword = etPasswordConfirm.getText().toString();
//                strCountry = spCountry.getSelectedItem().toString();
//                strRegionOrState = spRegionOrState.getSelectedItem().toString();
//                reg.setFirstName(strFirstName);
//                reg.setLastName(strLastName);
//                reg.setTelephone(strTelephone);
//                reg.setEmail(strEmail);
//                reg.setAddressOne(strAddressOne);
//                reg.setAddressTwo(strAddressTwo);
//                reg.setCity(strCity);
//                reg.setPostalCode(strPostalCode);
//                reg.setPassword(strPassword);
//                reg.setConfirmPassword(strConfirmPassword);
//                reg.setCountry(strCountry);
//                reg.setRegionOrState(strRegionOrState);
//                reg.setSubscribe(strSubscribe);
//                db.insert(reg);
//                Snackbar.make(btnContinue, "Successfully registered!", Snackbar.LENGTH_LONG).show();
//                clearAll();
//            } else {
//                Snackbar.make(btnContinue, "Username with current email id is already exists", Snackbar.LENGTH_SHORT).show();
//            }
//        }
    }

    private void clearAll() {
        etFirstName.setText("");
        etLastName.setText("");
        etEmail.setText("");
        etTelephone.setText("");
        etAddressOne.setText("");
        etAddressTwo.setText("");
        etPostalCode.setText("");
        etCity.setText("");
        etPassword.setText("");
        etPasswordConfirm.setText("");
        etPassword.setError(null);
        etPasswordConfirm.setError(null);
        spCountry.setSelection(0);
        spRegionOrState.setSelection(0);
        rdSubscribeNo.setChecked(true);
        chkPrivacyPolicy.setChecked(false);
        RegisterActivity.this.finish();
    }
}
