package org.byters.vkmarketplace.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    @Nullable
    private MarketplaceItem data;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_photos_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.getPhotosSize();
    }

    public void updateData(@NonNull MarketplaceItem item) {
        this.data = item;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivItem);
        }

        public void setData(int position) {
            if (data == null) {
                imageView.setImageDrawable(null);
                return;
            }
            String uri = data.getPhotoByPosition(position);
            if (TextUtils.isEmpty(uri))
                imageView.setImageDrawable(null);
            else ImageLoader.getInstance().displayImage(uri, imageView);
        }
    }
}
