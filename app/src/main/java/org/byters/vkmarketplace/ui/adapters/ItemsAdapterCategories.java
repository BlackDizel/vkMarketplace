package org.byters.vkmarketplace.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerAlbums;
import org.byters.vkmarketplace.model.dataclasses.AlbumBlob;

public class ItemsAdapterCategories extends RecyclerView.Adapter<ItemsAdapterCategories.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_shop_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return ControllerAlbums.getInstance().getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterShopCategory adapter;
        private TextView tvTitle;
        private RecyclerView rvItems;
        private AlbumBlob.AlbumItem item;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            rvItems = (RecyclerView) itemView.findViewById(R.id.rvItems);
            adapter = new AdapterShopCategory();
            rvItems.setAdapter(adapter);
        }

        public void setData(int position) {
            item = ControllerAlbums.getInstance().getItem(position);
            String title = item == null ? null : item.getTitle();
            tvTitle.setText(TextUtils.isEmpty(title) ? "" : title);
            adapter.setData(item == null ? ControllerAlbums.NO_VALUE : item.getId());
        }
    }
}
