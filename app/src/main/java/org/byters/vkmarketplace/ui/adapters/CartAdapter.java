package org.byters.vkmarketplace.ui.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.Cart;
import org.byters.vkmarketplace.model.dataclasses.CartEntry;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private ControllerMain controllerMain;

    public CartAdapter(ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return controllerMain == null ? 0 : controllerMain.getControllerCart().getCartItemsSize();
    }

    public void updateData() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, DialogInterface.OnClickListener {

        private TextView tvTitle;
        private TextView tvQuantity;
        private ImageView ivItem;
        @Nullable
        private NumberPicker picker;
        private int id;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);

            tvQuantity.setOnClickListener(this);
            itemView.findViewById(R.id.ivRemove).setOnClickListener(this);
        }

        public void setData(int position) {
            CartEntry cartItem = controllerMain.getControllerCart().getCartItem(position);
            MarketplaceItem item = null;
            if (cartItem != null) {
                id = cartItem.getItemId();
                item = controllerMain.getControllerItems().getModel().getItemById(id);
            }

            if (cartItem == null || item == null) {
                tvTitle.setText(R.string.item_cart_error_value);
                tvQuantity.setText(R.string.item_cart_error_value);
                ivItem.setImageDrawable(null);
                return;
            }

            tvTitle.setText(item.getTitle());
            tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
            ImageLoader.getInstance().displayImage(item.getThumb_photo(), ivItem);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvQuantity:
                    CartEntry item = controllerMain.getControllerCart().getCartItemById(id);

                    if (item == null) return;

                    View view_dialog = LayoutInflater.from(itemView.getContext())
                            .inflate(R.layout.view_dialog_number_picker, null);
                    Dialog dialog = new AlertDialog.Builder(itemView.getContext())
                            .setView(view_dialog)
                            .setPositiveButton(R.string.button_save, this)
                            .create();

                    picker = (NumberPicker) view_dialog.findViewById(R.id.numberPicker);
                    picker.setMaxValue(10);
                    picker.setMinValue(1);
                    picker.setValue(item.getQuantity());

                    dialog.show();
                    break;
                case R.id.ivRemove:
                    int pos = controllerMain.getControllerCart().getCartItemPosition(id);
                    if (pos == Cart.NO_VALUE) return;
                    controllerMain.getControllerCart().removeItem(pos);
                    notifyItemRemoved(pos);
                    break;
            }
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (picker == null) return;
            CartEntry item = controllerMain.getControllerCart().getCartItemById(id);
            if (item == null) return;
            item.setQuantity(picker.getValue());

            int pos = controllerMain.getControllerCart().getCartItemPosition(id);
            if (pos == Cart.NO_VALUE) return;
            notifyItemChanged(pos);
        }
    }
}
