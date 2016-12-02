package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.byters.vkmarketplace.R;

public class ActivityBankPayment extends ActivityBase {
    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivityBankPayment.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_payment);
        //todo set data
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
