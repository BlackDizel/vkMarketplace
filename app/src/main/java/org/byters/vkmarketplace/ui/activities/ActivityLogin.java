package org.byters.vkmarketplace.ui.activities;

import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;

public class ActivityLogin extends ActivityBase {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        webView = (WebView) findViewById(R.id.wvLogin);
        webView.setWebViewClient(new AuthClient());
        loadUrl();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        loadUrl();
    }

    private void loadUrl() {
        webView.loadUrl(getString(R.string.vk_auth_url));
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
            sanitizer.registerParameter(getString(R.string.user_id_key), UrlQuerySanitizer.getAllButNulLegal());

            String query = getQuery(url);
            if (TextUtils.isEmpty(query)) finish();
            sanitizer.parseQuery(query);

            String key = sanitizer.getValue(getString(R.string.access_token_key));
            if (TextUtils.isEmpty(key)) finish();
            ((ControllerMain) getApplicationContext()).setToken(key);

            String id = sanitizer.getValue(getString(R.string.user_id_key));
            if (TextUtils.isEmpty(id)) finish();
            ((ControllerMain) getApplicationContext()).setUserID(id);

            finish();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            showError(null);
        }

        private void finish() {
            onBackPressed();
        }
    }
}
