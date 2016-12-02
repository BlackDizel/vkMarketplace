package org.byters.vkmarketplace.controllers;

import android.content.Context;

import org.byters.vkmarketplace.model.ResponsePayment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerPaymentBank extends ControllerBase {
    private static ControllerPaymentBank instance;
    private RequestListener listenerRequest;

    private ControllerPaymentBank() {
        listenerRequest = new RequestListener();
    }

    public static ControllerPaymentBank getInstance() {
        if (instance == null) instance = new ControllerPaymentBank();
        return instance;
    }

    public void requestPayment(Context context, String stripeToken) {
        Call<ResponsePayment> request = ControllerCart.getInstance().getRequestBankPayment(context, stripeToken);
        if (request == null) {
            notifyListeners(false);
            return;
        }
        request.enqueue(listenerRequest);
    }

    private class RequestListener implements Callback<ResponsePayment> {
        @Override
        public void onResponse(Call<ResponsePayment> call, Response<ResponsePayment> response) {
            if (!response.isSuccessful()) {
                notifyListeners(false);
                return;
            }
            notifyListeners(true);
        }

        @Override
        public void onFailure(Call<ResponsePayment> call, Throwable t) {
            notifyListeners(false);
        }
    }
}
