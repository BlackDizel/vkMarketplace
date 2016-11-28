package org.byters.vkmarketplace.api;

import com.google.gson.JsonObject;

import org.byters.vkmarketplace.model.dataclasses.AccountInfoBlob;
import org.byters.vkmarketplace.model.dataclasses.AlbumBlob;
import org.byters.vkmarketplace.model.dataclasses.CommentsBlob;
import org.byters.vkmarketplace.model.dataclasses.LikesBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceBlob;
import org.byters.vkmarketplace.model.dataclasses.NewsBlob;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VkApi {

    String BASE_URL = "https://api.vk.com/method/";
    String USER_INFO_FIELDS = "photo_max_orig";

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

    @GET("market.search")
    Call<MarketplaceBlob> searchMarketItems(@Query("owner_id") int owner_id
            , @Query("q") String query
            , @Query("extended") int isExtended
            , @Query("access_token") String token
            , @Query("v") String v);

    @GET("market.getComments")
    Call<CommentsBlob> getMarketItemsComments(
            @Query("owner_id") int group_id
            , @Query("item_id") int id
            , @Query("need_likes") int isNeedLikes
            , @Query("sort") String sort
            , @Query("extended") int isExtended
            , @Query("fields") String fields
            , @Query("access_token") String token
            , @Query("v") String v);

    @GET("market.getById")
    Call<MarketplaceBlob> getMarketItemsById(@Query("item_ids") String ids
            , @Query("extended") int isExtended
            , @Query("access_token") String token
            , @Query("v") String v);


    @GET("market.getAlbums")
    Call<AlbumBlob> getAlbums(@Query("owner_id") int owner_id
            , @Query("access_token") String token
            , @Query("v") String v);

    @GET("market.createComment")
    Call<JsonObject> createComment(
            @Query("owner_id") int owner_id
            , @Query("item_id") int item_id
            , @Query("message") String message
            , @Query("access_token") String token
            , @Query("v") String v);

    @GET("wall.get")
    Call<NewsBlob> getNews(@Query("owner_id") int owner_id
            , @Query("filter") String filter
            , @Query("count") int count
            , @Query("v") String v);

    @GET("users.get")
    Call<AccountInfoBlob> getAccountInfo(
            @Query("user_ids") String ids
            , @Query("fields") String fields
            , @Query("v") String v);

    @GET("likes.add")
    Call<JsonObject> addLike(
            @Query("type") String type
            , @Query("owner_id") int owner_id
            , @Query("item_id") int item_id
            , @Query("access_token") String token
            , @Query("v") String v);

    @GET("likes.isLiked")
    Call<LikesBlob> isLiked(
            @Query("type") String type
            , @Query("owner_id") int owner_id
            , @Query("item_id") int item_id
            , @Query("access_token") String token
            , @Query("v") String v);

    @GET("messages.send")
    Call<JsonObject> sendMessage(
            @Query("peer_id") int group_id
            , @Query("message") String message
            , @Query("attachment") String attach
            , @Query("access_token") String token
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
