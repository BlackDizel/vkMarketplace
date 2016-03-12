package org.byters.vkmarketplace.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;

public class DialogComment
        implements DialogInterface.OnClickListener {
    private AlertDialog dialog;
    private int itemId;
    private ControllerMain controllerMain;
    private EditText etComment;
    private View rootView;

    public DialogComment(Context context, View rootView, int itemId) {
        this.itemId = itemId;
        controllerMain = (ControllerMain) context.getApplicationContext();
        this.rootView = rootView;

        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_comment, null);
        etComment = (EditText) view.findViewById(R.id.etComment);
        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(R.string.view_dialog_comment_action_send, this)
                .create();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (TextUtils.isEmpty(etComment.getText())) {
            Snackbar.make(rootView, R.string.view_dialog_comment_empty_message_error, Snackbar.LENGTH_SHORT).show();
            return;
        }
        controllerMain.sendComment(itemId, etComment.getText().toString());
    }
}
