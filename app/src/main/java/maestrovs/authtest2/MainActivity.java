package maestrovs.authtest2;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.BrowserClientRequestUrl;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.wuman.android.auth.AuthorizationFlow;
import com.wuman.android.auth.AuthorizationUIController;
import com.wuman.android.auth.DialogFragmentController;
import com.wuman.android.auth.OAuthManager;
import com.wuman.android.auth.oauth2.implicit.ImplicitResponseUrl;
import com.wuman.android.auth.oauth2.store.SharedPreferencesCredentialStore;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity  {


    AuthorizationFlow flow;
    AuthorizationUIController controller;

    SharedPreferencesCredentialStore credentialStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         controller =
                new DialogFragmentController(getFragmentManager()) {

                    @Override
                    public String getRedirectUri() throws IOException {
                        return "https://api.equalibra.org/redirectToApp.json";
                    }

                    @Override
                    public boolean isJavascriptEnabledForWebView() {
                        return true;
                    }

                    @Override
                    public boolean disableWebViewCache() {
                        return false;
                    }

                    @Override
                    public boolean removePreviousCookie() {
                        return false;
                    }
                };


         credentialStore =  new SharedPreferencesCredentialStore(MainActivity.this, //???????????????????????????
                        "",
                 new JacksonFactory());



        AuthorizationFlow.Builder builder = new AuthorizationFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                new GenericUrl("https://api.equalibra.org/oauth/token"),
                new ClientParametersAuthentication("ios-app", "Wduj4atYYBhAhHKMwk6Swgn9Wp987nhA"),
                "ios-app",
                "https://api.equalibra.org/oauth/authorize?state=facebook&response_type=code");
        builder.setCredentialStore(credentialStore);

        //builder.setScopes(Arrays.asList("state", "facebook"));


        flow = builder.build();

        OathManagerThread oathManagerThread = new OathManagerThread();
        oathManagerThread.start();


    }

    OAuthManager oauth;
    Credential credential;

    Handler handler = new Handler();


    class OathManagerThread extends Thread
    {
        @Override
        public void run() {
            oauth = new OAuthManager(flow, controller);
            try
            {
                 //credential = oauth.authorizeImplicitly("ios-app", null, null).getResult();

                credential = oauth.authorizeExplicitly("ios-app", null, null).getResult();


             /*   OAuthManager.OAuthCallback<Credential> callback = new OAuthManager.OAuthCallback<Credential>() {
                    @Override public void run(OAuthManager.OAuthFuture<Credential> future) {
                        try {
                            Credential credential = future.getResult();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // make API queries with credential.getAccessToken()
                    }
                };*/
               // credential = (Credential) oauth.authorizeImplicitly("userId", callback, handler);

              // // credential = (Credential)
              //  oauth.authorizeExplicitly("ios-app", callback,handler);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
