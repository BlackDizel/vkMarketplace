package org.byters.vkmarketplace.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class VkService {
    private static VkApi api;

    public static synchronized VkApi getApi() {
        if (api == null) {
            api = initializeApi();
        }
        return api;
    }

    private static VkApi initializeApi() {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                /*.addInterceptor(new okhttp3.Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        //this will closed request so Gson will not parse response
                        String s = new String(chain.proceed(chain.request()).body().bytes());
                        android.util.Log.v("some",""+s);
                        return  chain.proceed(chain.request());
                    }
                })*/
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(double.class, new JsonDeserializer<Double>() {
                    @Override
                    public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        try {
                            return Double.parseDouble(json.getAsString().replace(',', '.'));
                        } catch (NumberFormatException e) {
                            throw new JsonParseException(e);
                        }
                    }
                })
                .registerTypeAdapter(MarketplaceItem.class, new JsonDeserializer<MarketplaceItem>() {
                    @Override
                    public MarketplaceItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        try {
                            //todo check. may generate overhead
                            return new GsonBuilder().create().fromJson(json, MarketplaceItem.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })

                .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        return new Retrofit.Builder()
                .baseUrl(VkApi.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(VkApi.class);
    }
}
