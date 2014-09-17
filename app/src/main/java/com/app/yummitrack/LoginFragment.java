package com.app.yummitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

/**
 * Created by leind on 9/16/14.
 * Login UI, now its a fragment hosted by MainActivity due to facebook login shit
 */
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    List<String> PERMISSIONS = Arrays.asList("public_profile", "email");
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private UiLifecycleHelper uiHelper;

    String email, GUemail;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_main, container, false);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.login_button);
        authButton.setFragment(this);
        authButton.setReadPermissions(PERMISSIONS);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            if (!session.getPermissions().containsAll(PERMISSIONS))
            {
                //authButton.setReadPermissions(Arrays.asList("public_profile", "user_likes", "email"));
                //Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
                //session.requestNewReadPermissions(newPermissionsRequest);
                Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
                        this, PERMISSIONS);
                session.requestNewReadPermissions(newPermissionsRequest);
            }
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
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            // If is closed, (clear token just in case)
            Session.getActiveSession().closeAndClearTokenInformation();
        }
    }

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
}
