package com.app.yummitrack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import com.facebook.*;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.widget.LinearLayout;

import java.util.Arrays;

/*
* Activity that handles login process
* */
public class MainActivity extends Activity {
    private boolean isResumed = false;

    String email, GUemail;
    String TAG = "Main";
    // In order to track the session and trigger a session state change listener
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                }
            };

    // ----------- End global variables ----------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_main, null);
        LoginButton authButton = (LoginButton) layout.findViewById(R.id.login_button);
        authButton.setReadPermissions(Arrays.asList("public_profile", "user_likes", "email"));
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible
        if (isResumed) {
            if (state.isOpened()) {
                // If the session state is open:
                // Get user info -> Send this to webservice
                Log.i(TAG, "Logged in...");

                // Get user info
                new Request(
                        session,
                        "/me",
                        null,
                        HttpMethod.GET,
                        new Request.Callback() {
                            public void onCompleted(Response response) {
                                email = response.getGraphObject().getProperty("email").toString();
                                Log.i(TAG, "Email: " + email);
                            }
                        }
                ).executeAsync();

                // Another way to ask for info, don't know which one is better
                Request.newMeRequest(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if(user != null) {
                            GUemail = user.getProperty("email").toString();
                            Log.i(TAG, "Email graphicuser: " + GUemail);
                        }
                    }
                }).executeAsync();

                // -> DashboardActivity
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else if (state.isClosed()) {
                // If is closed, (clear token just in case)
                Session.getActiveSession().closeAndClearTokenInformation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
}
