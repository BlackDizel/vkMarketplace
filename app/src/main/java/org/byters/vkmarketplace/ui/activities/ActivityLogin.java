package org.byters.vkmarketplace.ui.activities;

import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;

public class ActivityLogin extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WebView wv = (WebView) findViewById(R.id.wvLogin);
        wv.setWebViewClient(new AuthClient());
        wv.loadUrl(getString(R.string.vk_auth_url));

    }

    private class AuthClient extends WebViewClient {

        @Nullable
        private String getQuery(@NonNull String url) {
            int queryIndex = url.indexOf('#');
            String query = null;
            if (queryIndex >= 0) {
                query = url.substring(queryIndex + 1);
            }
            return query;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (TextUtils.isEmpty(url) || !url.contains(getString(R.string.access_token_key)))
                return;

            UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
            sanitizer.registerParameter(getString(R.string.access_token_key), UrlQuerySanitizer.getAllButNulLegal());

            String query = getQuery(url);
            if (TextUtils.isEmpty(query)) finish();
            sanitizer.parseQuery(query);

            String key = sanitizer.getValue(getString(R.string.access_token_key));

            if (TextUtils.isEmpty(key)) finish();

            ((ControllerMain) getApplicationContext()).setToken(key);

            finish();
        }

        private void finish() {
            onBackPressed();
        }
    }
}
