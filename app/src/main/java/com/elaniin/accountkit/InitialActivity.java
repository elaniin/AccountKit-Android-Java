package com.elaniin.accountkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;

public class InitialActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 3301;
    public static String APP_TAG = "AccountKit";
    private LinearLayout mLytLoading;
    private Button mBtnEmailLogin;
    private Button mBtnPhoneLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        getObjects();
        setProperties();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String responseMessage;
            if (loginResult.getError() != null) {
                responseMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                responseMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    responseMessage = "Success: " + loginResult.getAccessToken().getAccountId();
                } else {
                    responseMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                goToMyLoggedInActivity();
            }
            log(responseMessage);
        }
    }

    private void getObjects() {
        mBtnEmailLogin = findViewById(R.id.btnEmailLogin);
        mBtnPhoneLogin = findViewById(R.id.btnPhoneLogin);
        mLytLoading = findViewById(R.id.lytLoading);
    }

    private void setProperties() {
        AccountKit.initialize(getApplicationContext(), new AccountKit.InitializeCallback() {
            @Override
            public void onInitialized() {
                mLytLoading.setVisibility(View.GONE);
            }
        });

        mBtnEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin(false);
            }
        });

        mBtnPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin(true);
            }
        });

        if (AccountKit.getCurrentAccessToken() != null) {
            goToMyLoggedInActivity();
        }
    }

    public void goToLogin(boolean isSMSLogin) {
        LoginType loginType = isSMSLogin ? LoginType.PHONE : LoginType.EMAIL;

        Intent intent = new Intent(getApplicationContext(), AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );

        configurationBuilder.setUIManager(new SkinManager(
                        SkinManager.Skin.CONTEMPORARY,
                        getResources().getColor(R.color.colorBackground),
                        R.drawable.bg,
                        SkinManager.Tint.BLACK,
                        0.10
                )
        );

        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    private void goToMyLoggedInActivity() {
        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        startActivity(intent);
        finish();
    }

    private void log(String msj) {
        Log.println(Log.DEBUG, APP_TAG, msj);
    }
}
