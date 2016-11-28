package org.byters.vkmarketplace.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerCart;

public class DialogPayment
        implements DialogInterface.OnClickListener {
    private AlertDialog dialog;
    private Context context;
    private EditText etComment;
    @Nullable
    private View rootView;

    public DialogPayment(Context context, @Nullable View rootView) {

        this.rootView = rootView;
        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_buy, null);

        etComment = (EditText) view.findViewById(R.id.etComment);
        String comment = ControllerCart.getInstance().getComment();
        if (!TextUtils.isEmpty(comment))
            etComment.setText(comment);

        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(R.string.alert_buy_title)
                .setPositiveButton(R.string.alert_buy_positive_button, this)
                .create();
        dialog.setCanceledOnTouchOutside(false);

    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (etComment != null)
            ControllerCart.getInstance().setComment(etComment.getText().toString());

        ControllerCart.getInstance().trySendBuyRequest(context, rootView);
    }
}
