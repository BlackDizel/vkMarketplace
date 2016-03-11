package org.byters.vkmarketplace.ui.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.LikesBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.dialogs.DialogImage;
import org.byters.vkmarketplace.ui.utils.SharingHelper;

import retrofit2.Callback;
import retrofit2.Response;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean isLikeEnabled;
    @Nullable
    private View rootView;
    @Nullable
    private MarketplaceItem data;

    private ControllerMain controllerMain;

    public PhotosAdapter(ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
    }

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

    public void updateData(@NonNull MarketplaceItem item, View rootView) {
        this.data = item;
        this.rootView = rootView;

        notifyDataSetChanged();

        if (data != null)
            controllerMain.isLiked(data.getId(), new Callback<LikesBlob>() {

                @Override
                public void onResponse(Response<LikesBlob> response) {
                    if (response == null) return;
                    LikesBlob item = response.body();
                    if (item == null) return;
                    isLikeEnabled = !item.isLiked();
                    notifyItemChanged(0);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
    }

    private class ViewHolderItem extends ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        @Nullable
        private String uri;

        public ViewHolderItem(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(int position) {
            if (data == null) {
                imageView.setImageDrawable(null);
                return;
            }
            uri = data.getPhotoByPosition(position - 1);
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

    private class ViewHolderHeader extends ViewHolder
            implements View.OnClickListener {
        private TextView tvDescription, tvPrice, tvLikes;
        private View llLikes;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            tvDescription = ((TextView) itemView.findViewById(R.id.tvDescription));
            tvPrice = ((TextView) itemView.findViewById(R.id.tvPrice));
            tvLikes = ((TextView) itemView.findViewById(R.id.tvLikes));
            itemView.findViewById(R.id.llShare).setOnClickListener(this);
            llLikes = itemView.findViewById(R.id.llLikes);
            llLikes.setOnClickListener(this);
        }

        @Override
        public void setData(int position) {
            if (data == null) return;
            tvDescription.setText(Html.fromHtml(data.getDescription()));
            tvPrice.setText(data.getPrice().getText().toUpperCase().replace(".", ""));

            if (data.getLikes() != null)
                tvLikes.setText(String.format("%d", data.getLikes().getCount()));

            llLikes.setClickable(isLikeEnabled);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.llShare:
                    if (data == null) return;
                    SharingHelper.shareItem(v.getContext(), data.getId());
                    break;
                case R.id.llLikes:
                    if (data == null)
                        return;

                    controllerMain.addLike(data.getId(), new Callback() {
                        @Override
                        public void onResponse(Response response) {
                            if (rootView != null) {
                                Snackbar.make(rootView, R.string.action_like_success, Snackbar.LENGTH_SHORT).show();
                                if (data != null)
                                    controllerMain.updateDetailedItemInfo(data.getId(), false);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            if (rootView != null)
                                Snackbar.make(rootView, R.string.action_like_error, Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
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
