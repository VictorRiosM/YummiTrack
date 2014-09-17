package com.app.yummitrack;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/*
* Activity that handles login process
* */
public class MainActivity extends FragmentActivity {
    private LoginFragment loginFragment;

    // ----------- End global variables ----------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            loginFragment = new LoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, loginFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            loginFragment = (LoginFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }

    }
}
