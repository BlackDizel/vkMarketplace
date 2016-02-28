package org.byters.vkmarketplace.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    @Nullable
    private MarketplaceItem data;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER)
            return new ViewHolderHeader(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_list_photos_header, parent, false));
        else
            return new ViewHolderItem(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_list_photos_item, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return (data == null ? 0 : data.getPhotosSize()) + 1;
    }

    public void updateData(@NonNull MarketplaceItem item) {
        this.data = item;
        notifyDataSetChanged();
    }

    private class ViewHolderItem extends ViewHolder {
        private ImageView imageView;

        public ViewHolderItem(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivItem);

        }

        @Override
        public void setData(int position) {
            if (data == null) {
                imageView.setImageDrawable(null);
                return;
            }
            String uri = data.getPhotoByPosition(position-1);
            if (TextUtils.isEmpty(uri))
                imageView.setImageDrawable(null);
            else ImageLoader.getInstance().displayImage(uri, imageView);
        }
    }

    private class ViewHolderHeader extends ViewHolder {
        private TextView tvDescription, tvPrice;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            tvDescription = ((TextView) itemView.findViewById(R.id.tvDescription));
            tvPrice = ((TextView) itemView.findViewById(R.id.tvPrice));
        }

        @Override
        public void setData(int position) {
            if (data == null) return;
            tvDescription.setText(Html.fromHtml(data.getDescription()));
            tvPrice.setText(data.getPrice().getText());

        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(int position) {
        }
    }
}
