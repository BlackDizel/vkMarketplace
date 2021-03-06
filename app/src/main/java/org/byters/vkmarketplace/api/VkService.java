package org.byters.vkmarketplace.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VkService {
    private static VkApi api;

    public static synchronized VkApi getApi() {
        if (api == null) {
            api = initializeApi();
        }
        return api;
    }

    private static VkApi initializeApi() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES);
        Interceptor interceptor = InterceptorHelper.getLoggingInterceptor();
        if (interceptor != null) builder.addInterceptor(interceptor);
        OkHttpClient client = builder.build();

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
