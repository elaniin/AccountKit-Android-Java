package com.elaniin.accountkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;

public class InitialActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccountKit.initialize(getApplicationContext());
        setContentView(R.layout.activity_initial);

        AccessToken accessToken = AccountKit.getCurrentAccessToken();

        if(accessToken != null){
            goToMyLoggedInActivity();
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String responseMessage;
            if (loginResult.getError() != null) {
                responseMessage = loginResult.getError().getErrorType().getMessage();
                logAssert(loginResult.getError() + " - " + responseMessage);
            } else if (loginResult.wasCancelled()) {
                responseMessage = "Login Cancelled";
                logAssert(responseMessage);
            } else {
                if (loginResult.getAccessToken() != null) {
                    responseMessage = "Success: " + loginResult.getAccessToken().getAccountId();
                    logAssert(responseMessage);
                } else {
                    responseMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                    logAssert(responseMessage);
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                goToMyLoggedInActivity();
            }
        }
    }

    public void goToLogin(boolean isSMSLogin) {

        LoginType loginType = isSMSLogin ? LoginType.PHONE : LoginType.EMAIL;

        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN);

        UIManager uiManager = new SkinManager(
                SkinManager.Skin.CONTEMPORARY,
                getResources().getColor(R.color.colorBackground),
                R.drawable.bg,
                SkinManager.Tint.BLACK,
                0.10);

        configurationBuilder.setUIManager(uiManager);

        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        this.startActivityForResult(intent, APP_REQUEST_CODE);
    }

    public void smsLogin(View v){
        goToLogin(true);
    }

    public void emailLogin(View v){
        goToLogin(false);
    }

    private void goToMyLoggedInActivity(){
        final Intent intent = new Intent(this, SecondActivity.class);
        this.startActivity(intent);
    }

    private void logAssert(String error) {
        Log.println(Log.ASSERT, "AccountKit", error);
    }
}
