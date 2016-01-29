package org.byters.vkmarketplace.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.activities.ActivityItemInfo;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private ControllerMain controllerMain;

    public ItemsAdapter(@NonNull ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return controllerMain.getControllerItems().getModel().getSize();
    }

    public void updateData() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivItem;
        TextView tvTitle, tvPrice;
        private int id;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            MarketplaceItem item = controllerMain.getControllerItems().getModel().get(position);
            if (item == null) return; //maybe need to throw error here, hm?
            tvTitle.setText(item.getTitle());
            if (item.getPrice() != null)
                tvPrice.setText(item.getPrice().getText());
            ImageLoader.getInstance().displayImage(item.getThumb_photo(), ivItem);
            id = item.getId();
        }

        @Override
        public void onClick(View v) {
            if (v.getContext() instanceof Activity)
                ActivityItemInfo.display(v.getContext(), id);
        }
    }
}
