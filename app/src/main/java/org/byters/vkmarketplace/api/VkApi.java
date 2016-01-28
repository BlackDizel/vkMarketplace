package org.byters.vkmarketplace.api;

import org.byters.vkmarketplace.model.MarketplaceBlob;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VkApi {

    String BASE_URL = "https://api.vk.com/method/";

    @GET("market.get")
    Call<MarketplaceBlob> getMarketItems(@Query("owner_id") int owner_id, @Query("offset") int offset, @Query("access_token") String token);

    /*
    @GET("users/{username}")
    Call<UserInfo> getUserInfo(@Path("username") String username);

    @GET("users")
    Call<ArrayList<UserInfo>> getData(@Query("since") int id);

    @GET("users")
    Call<ArrayList<UserInfo>> getData();

    @GET("search/users")
    Call<SearchInfo> searchUsers(@Query("q") String query);

    @GET("search/users")
    Call<SearchInfo> searchUsers(@Query("q") String query, @Query("page") int page);*/
}
