package com.elaniin.accountkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;

public class SecondActivity extends AppCompatActivity {

    TextView txtAccountKitID, txtUserLoginMode, txtUserLoginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        txtAccountKitID = (TextView) findViewById(R.id.txtAccountKitID);
        txtUserLoginMode = (TextView) findViewById(R.id.txtUserLoginMode);
        txtUserLoginData = (TextView) findViewById(R.id.txtUserLoginData);

        this.setUserInformation();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void setUserInformation(){
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();
                Log.println(Log.ASSERT, "AccountKit", "ID: " + accountKitId);

                boolean SMSLoginMode = false;

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                String phoneNumberString = "";
                if (phoneNumber != null) {
                    phoneNumberString = phoneNumber.toString();
                    Log.println(Log.ASSERT, "AccountKit", "Phone: " + phoneNumberString);
                    SMSLoginMode = true;
                }

                // Get email
                String email = account.getEmail();
                Log.println(Log.ASSERT, "AccountKit", "Email: " + email);

                txtAccountKitID.setText(accountKitId);
                txtUserLoginMode.setText(SMSLoginMode ? "Phone:" : "Email:");
                if (SMSLoginMode) {
                    txtUserLoginData.setText(phoneNumberString);
                } else {
                    txtUserLoginData.setText(email);
                }

            }

            @Override
            public void onError(final AccountKitError error) {
                Log.println(Log.ASSERT, "AccountKit", "Error: " + error.toString());
            }
        });
    }

    public void LogOut(View v){
        AccountKit.logOut();
        Intent initialActivity = new Intent(this, InitialActivity.class);
        this.startActivity(initialActivity);
        this.finish();
    }

}
