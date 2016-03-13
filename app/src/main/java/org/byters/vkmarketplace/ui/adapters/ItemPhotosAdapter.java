package org.byters.vkmarketplace.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.dialogs.DialogImage;

public class ItemPhotosAdapter extends RecyclerView.Adapter<ItemPhotosAdapter.ViewHolder> {

    @Nullable
    private MarketplaceItem data;

    private ControllerMain controllerMain;

    public ItemPhotosAdapter(ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
    }

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

    public void updateData(MarketplaceItem data) {
        this.data = data;
        if (this.data != null)
            notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ImageView imageView;
        @Nullable
        private String uri;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivItem);
            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            if (data == null) {
                imageView.setImageDrawable(null);
                return;
            }
            uri = data.getPhotoByPosition(controllerMain, position);
            if (TextUtils.isEmpty(uri))
                imageView.setImageDrawable(null);
            else
                Picasso.with(imageView.getContext()).load(uri).into(imageView);
        }

        @Override
        public void onClick(View v) {
            if (uri == null) return;
            new DialogImage(v.getContext(), uri).show();
        }
    }
}
