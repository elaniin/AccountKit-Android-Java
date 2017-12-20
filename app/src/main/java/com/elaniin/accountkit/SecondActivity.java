package com.elaniin.accountkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;

public class SecondActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTxtAccountKitID;
    private TextView mTxtUserLoginData;
    private TextView mTxtUserLoginMode;
    private Button mBtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getObjects();
        setProperties();
    }

    private void getObjects(){
        mToolbar = findViewById(R.id.toolbar);
        mTxtAccountKitID = findViewById(R.id.txtAccountKitID);
        mTxtUserLoginMode = findViewById(R.id.txtUserLoginMode);
        mTxtUserLoginData = findViewById(R.id.txtUserLoginData);
        mBtnLogout = findViewById(R.id.btnLogout);
    }

    private void setProperties(){
        setSupportActionBar(mToolbar);

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        setUserInformation();
    }

    public void setUserInformation(){
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();
                log("ID: " + accountKitId);

                boolean SMSLoginMode = false;

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                String phoneNumberString = "";
                if (phoneNumber != null) {
                    phoneNumberString = phoneNumber.toString();
                    log("Phone: " + phoneNumberString);
                    SMSLoginMode = true;
                }

                // Get email
                String email = account.getEmail();
                log("Email: " + email);

                mTxtAccountKitID.setText(accountKitId);
                mTxtUserLoginMode.setText(SMSLoginMode ? "Phone:" : "Email:");
                if (SMSLoginMode) {
                    mTxtUserLoginData.setText(phoneNumberString);
                } else {
                    mTxtUserLoginData.setText(email);
                }

            }

            @Override
            public void onError(final AccountKitError error) {
                log("Error: " + error.toString());
            }
        });
    }

    public void logout(){
        AccountKit.logOut();
        Intent initialActivity = new Intent(this, InitialActivity.class);
        startActivity(initialActivity);
        finish();
    }

    private void log(String msj) {
        Log.println(Log.DEBUG, InitialActivity.APP_TAG, msj);
    }

}
