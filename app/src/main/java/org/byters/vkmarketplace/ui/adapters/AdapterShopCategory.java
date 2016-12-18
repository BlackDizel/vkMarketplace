package org.byters.vkmarketplace.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerAlbums;
import org.byters.vkmarketplace.model.dataclasses.AlbumBlob;
import org.byters.vkmarketplace.ui.activities.ActivityItemInfo;

class AdapterShopCategory extends RecyclerView.Adapter<AdapterShopCategory.ViewHolder> {
    private int id;

    AdapterShopCategory() {
        this.id = ControllerAlbums.NO_VALUE;
    }

    public void setData(int id) {
        this.id = id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_category, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return id == ControllerAlbums.NO_VALUE ? 0 : ControllerAlbums.getInstance().getItemsSizeById(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView ivItem;
        TextView tvTitle;
        private AlbumBlob.AlbumItem item;

        public ViewHolder(View itemView) {
            super(itemView);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            item = null; //todo implement

            if (item != null && item.getPhoto() != null && !TextUtils.isEmpty(item.getPhoto().getLittlePhoto()))
                Picasso.with(itemView.getContext())
                        .load(item.getPhoto().getLittlePhoto())
                        .into(ivItem);
            else ivItem.setImageDrawable(null);

            String title = item == null ? null : item.getTitle();
            tvTitle.setText(TextUtils.isEmpty(title) ? "" : title);
        }

        @Override
        public void onClick(View v) {
            if (item == null) return;
            ActivityItemInfo.display(v.getContext(), item.getId());
        }
    }
}
