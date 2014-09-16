package com.app.yummitrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by leind on 9/15/14.
 * Activity that handles dashboard and its fragments
 */
public class DashboardActivity extends FragmentActivity {
    String email, GUemail;
    String TAG = "Dashboard";

    // Fragmentss
    private static final int FEED = 0;
    private static final int SELECTION = 1;
    private static final int FRAGMENT_COUNT = 2;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    private boolean isResumed = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_main);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        fragments[FEED] = fm.findFragmentById(R.id.feedFragment);
        fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);


        showFragment(FEED, false);
    }

    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    /*
    * Facebook
    * */

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible
        if (state.isClosed()) {
            // If the session state is closed:
            // Show the login fragment
            Session.getActiveSession().closeAndClearTokenInformation();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            //showFragment(SPLASH, false);
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

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();

        if (session != null && session.isOpened()) {
            // if the session is already open,
            // try to show the register fragment
            showFragment(FEED, false);
        } else {
            // otherwise present the splash screen
            // and ask the person to login.
            Session.getActiveSession().closeAndClearTokenInformation();
            Session.getActiveSession().closeAndClearTokenInformation();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
