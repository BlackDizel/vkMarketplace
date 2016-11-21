package org.byters.vkmarketplace.ui.activities;

import android.os.Bundle;
import android.os.Handler;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;

public class ActivitySplash extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!((ControllerMain) getApplicationContext()).isAuth())
                    ActivityLogin.display(ActivitySplash.this);
                else
                    ActivityMain.display(ActivitySplash.this);
            }
        }, getResources().getInteger(R.integer.splash_time));
    }
}