package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.byters.vkmarketplace.R;

public class ActivitySearchMarkets extends AppCompatActivity {

    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivitySearchMarkets.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_markets);
    }
}
