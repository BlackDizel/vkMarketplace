package org.byters.vkmarketplace.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.byters.vkmarketplace.R;

public class DialogImage {
    @Nullable
    private Dialog dialog;

    public DialogImage(Context context, String uri) {

        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_photo, null);
        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();

        ImageView imgView = (ImageView) view.findViewById(R.id.ivPhoto);
        ImageLoader.getInstance().displayImage(uri, imgView);
    }

    public void show() {
        if (dialog == null) return;
        dialog.show();

    }
}
