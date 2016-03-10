package org.byters.vkmarketplace.ui.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.byters.vkmarketplace.R;

public abstract class ActivityBase extends AppCompatActivity {
    public static final int STATE_ERROR = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_OFFLINE = 2;
    public static final int STATE_OK = 3;

    private View vError;
    private View vOffline;
    private View vLoading;
    private TextView tvErrorDescription;
    private ViewGroup contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        contentView = (ViewGroup) findViewById(R.id.vContent);

        vError = findViewById(R.id.vError);
        vLoading = findViewById(R.id.vLoading);
        vOffline = findViewById(R.id.tvOffline);
        tvErrorDescription = (TextView) findViewById(R.id.tvErrorDescription);

        View vReload = findViewById(R.id.tvReload);
        if (vReload != null) vReload.setOnClickListener(new ErrorClickListener());
    }

    @Override
    public void setContentView(int layoutResId) {
        getLayoutInflater().inflate(layoutResId, contentView);
    }

    protected void setState(int state) {

        if (vError != null)
            vError.setVisibility(View.GONE);

        if (vOffline != null)
            vOffline.setVisibility(View.GONE);

        if (vLoading != null)
            vLoading.setVisibility(View.GONE);

        if (tvErrorDescription != null)
            tvErrorDescription.setVisibility(View.GONE);

        switch (state) {
            case STATE_ERROR:
                if (vError != null)
                    vError.setVisibility(View.VISIBLE);
                break;
            case STATE_LOADING:
                if (vLoading != null)
                    vLoading.setVisibility(View.VISIBLE);
                break;
            case STATE_OFFLINE:
                if (vOffline != null)
                    vOffline.setVisibility(View.VISIBLE);
                break;
            case STATE_OK:
                break;
        }
    }

    protected final void showError(String errorText) {
        setState(STATE_ERROR);
        if (tvErrorDescription != null && !TextUtils.isEmpty(errorText)) {
            tvErrorDescription.setText(errorText);
            tvErrorDescription.setVisibility(View.VISIBLE);
        }
    }

    protected void reloadData() {
        setState(STATE_OK);
    }

    private class ErrorClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            reloadData();
        }
    }
}
