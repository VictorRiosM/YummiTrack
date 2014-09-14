package com.app.yummitrack;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

/**
 * Created by leind on 9/14/14.
 */
public class SplashFragment extends Fragment {
    private static final String TAG = "SplashFragment";
    private UiLifecycleHelper uiHelper;

    String email, GUemail;

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash, container, false);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.login_button);
        //authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("public_profile", "user_likes", "email"));

        return view;
    }

    /*
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");

            // forma fresa de obtener la informaciondel usuario
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

            // Otra forma, no se cual es mas cool
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
            public void onCompleted(GraphUser user, Response response) {
                    if(user != null) {
                        GUemail = user.getProperty("email").toString();
                        Log.i(TAG, "Email graphicuser: " + GUemail);
                    }
                }
            }).executeAsync();

            Log.i(TAG, "Logged in...");

        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            Session.getActiveSession().closeAndClearTokenInformation();
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    */
}
