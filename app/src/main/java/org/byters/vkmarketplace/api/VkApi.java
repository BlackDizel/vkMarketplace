package org.byters.vkmarketplace.api;

import org.byters.vkmarketplace.model.dataclasses.AlbumBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceBlob;
import org.byters.vkmarketplace.model.dataclasses.NewsBlob;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VkApi {

    String BASE_URL = "https://api.vk.com/method/";

    @GET("market.get")
    Call<MarketplaceBlob> getMarketItems(@Query("owner_id") int owner_id
            , @Query("offset") int offset
            , @Query("access_token") String token
            , @Query("extended") int isExtended
            , @Query("v") String v);

    @GET("market.get")
    Call<MarketplaceBlob> getMarketItems(@Query("owner_id") int owner_id
            , @Query("album_id") int album_id
            , @Query("offset") int offset
            , @Query("access_token") String token
            , @Query("extended") int isExtended
            , @Query("v") String v);

    @GET("market.searcg")
    Call<MarketplaceBlob> searchMarketItems(@Query("owner_id") int owner_id
            , @Query("q") int offset
            , @Query("extended") int isExtended
            , @Query("v") String v);

    @GET("market.getById")
    Call<MarketplaceBlob> getMarketItemsById(@Query("item_ids") String ids
            , @Query("extended") int isExtended
            , @Query("access_token") String token
            , @Query("v") String v);

    @GET("wall.get")
    Call<NewsBlob> getNews(@Query("owner_id") int owner_id
            , @Query("filter") String filter
            , @Query("count") int count
            , @Query("v") String v);

    @GET("market.getAlbums")
    Call<AlbumBlob> getAlbums(@Query("owner_id") int owner_id
            , @Query("v") String v);

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
