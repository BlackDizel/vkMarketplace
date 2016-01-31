package org.byters.vkmarketplace.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvQuantity;
        private ImageView ivItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
        }

        public void setData(int position) {
            CartEntry cartItem = controllerMain.getControllerCart().getCartItem(position);
            MarketplaceItem item = null;
            if (cartItem != null)
                item = controllerMain.getControllerItems().getModel().getItemById(cartItem.getItemId());

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

    }
}
