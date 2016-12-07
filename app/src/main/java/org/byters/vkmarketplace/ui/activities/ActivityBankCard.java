package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerPaymentBank;
import org.byters.vkmarketplace.controllers.utils.OnResultBase;

public class ActivityBankCard extends ActivityBase
        implements View.OnClickListener {

    StripeTokenListener listenerStripe;
    private BankPaymentListener listenerBankPayment;

    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivityBankCard.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);

        listenerStripe = new StripeTokenListener();
        listenerBankPayment = new BankPaymentListener();

        findViewById(R.id.tvPayment).setOnClickListener(this);
    }

    private void pay() {

        String cardNumber = ((TextView) findViewById(R.id.etBankCardNumber)).getText().toString();
        String cvv = ((TextView) findViewById(R.id.etCVV)).getText().toString();
        int month, year;
        try {
            month = Integer.valueOf(((TextView) findViewById(R.id.etMonth)).getText().toString());
            year = Integer.valueOf(((TextView) findViewById(R.id.etYear)).getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_bank_card_date, Toast.LENGTH_SHORT).show();
            return;
        }

        Card card = new Card(cardNumber, month, year, cvv);
        if (!card.validateCard()) {
            Toast.makeText(this, R.string.error_bank_card, Toast.LENGTH_SHORT).show();
            return;
        }
        Stripe stripe;
        try {
            stripe = new Stripe(getString(R.string.com_stripe_publishable_key));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.error_bank_card_payment_provider, Toast.LENGTH_SHORT).show();
            return;
        }
        stripe.createToken(card, listenerStripe);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ControllerPaymentBank.getInstance().addListener(listenerBankPayment);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ControllerPaymentBank.getInstance().removeListener(listenerBankPayment);
    }

    @Override
    public void onClick(View view) {
        pay();
    }

    private class StripeTokenListener extends TokenCallback {
        @Override
        public void onError(Exception error) {
            error.printStackTrace();
            Toast.makeText(ActivityBankCard.this, R.string.error_bank_card_payment_provider, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(Token token) {
            ControllerPaymentBank.getInstance().requestPayment(ActivityBankCard.this, token.getId());
        }
    }

    private class BankPaymentListener implements OnResultBase {
        @Override
        public void onResult(boolean isSuccess) {
            if (isSuccess) {
                Toast.makeText(ActivityBankCard.this, R.string.payment_successful, Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else
                Toast.makeText(ActivityBankCard.this, R.string.error_bank_card_payment_provider, Toast.LENGTH_SHORT).show();
        }
    }
}
