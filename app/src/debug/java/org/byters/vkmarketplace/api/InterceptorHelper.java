package org.byters.vkmarketplace.api;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

class InterceptorHelper {

    static Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
