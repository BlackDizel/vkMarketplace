package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.ControllerAuth;
import org.byters.vkmarketplace.controllers.controllers.ControllerItems;
import org.byters.vkmarketplace.controllers.controllers.ControllerNews;

public class ActivityLogin extends ActivityBase
        implements View.OnClickListener {

    WebView webView;

    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivityLogin.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.tvNavigate).setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.market_address)));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Snackbar.make(findViewById(R.id.rootView), R.string.action_view_browser_error, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void onResult(boolean isOK) {
        if (isOK) {
            ActivityMain.display(this);
            finish();
        } else {
            //todo restart login process
            onBackPressed();
        }
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
            if (TextUtils.isEmpty(query)) onResult(false);
            sanitizer.parseQuery(query);

            String key = sanitizer.getValue(getString(R.string.access_token_key));
            if (TextUtils.isEmpty(key)) onResult(false);

            ControllerAuth.getInstance().setToken(key);
            ControllerItems.getInstance().updateData(ActivityLogin.this);
            ControllerNews.getInstance().updateData(ActivityLogin.this);

            String id = sanitizer.getValue(getString(R.string.user_id_key));
            if (TextUtils.isEmpty(id)) onResult(false);
            ((ControllerMain) getApplicationContext()).setUserID(ActivityLogin.this, id);

            onResult(true);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            showError(null);
        }

    }
}
