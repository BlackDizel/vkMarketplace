package org.byters.vkmarketplace.api;

import org.byters.vkmarketplace.model.ResponsePayment;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiMarketplace {
    String BASE_URL = "http://192.168.0.95:8000/";

    @FormUrlEncoded
    @POST("payment/")
    Call<ResponsePayment> requestPayment(
            @Field("token") String token,
            @Field("cost") int cost,
            @Field("order") String orderText,
            @Field("comment") String comment);
}
