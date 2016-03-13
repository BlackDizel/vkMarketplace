package org.byters.vkmarketplace.ui.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
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
import org.byters.vkmarketplace.model.dataclasses.CommentsBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.utils.SharingHelper;

import retrofit2.Callback;
import retrofit2.Response;

public class ItemInfoAdapter extends RecyclerView.Adapter<ItemInfoAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PHOTOS = 1;
    private static final int TYPE_COMMENT = 2;

    private boolean isLikeEnabled;
    @Nullable
    private View rootView;
    @Nullable
    private MarketplaceItem data;

    private ControllerMain controllerMain;

    public ItemInfoAdapter(ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER)
            return new ViewHolderHeader(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_info_list_header, parent, false));
        else if (viewType == TYPE_PHOTOS)
            return new ViewHolderPhotos(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_list_photos_list, parent, false));
        else
            return new ViewHolderComment(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_info_list_comment, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0
                ? TYPE_HEADER
                : (position < getHeaderWithItemsSize()
                ? TYPE_PHOTOS
                : TYPE_COMMENT);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    private int getHeaderWithItemsSize() {
        return data == null
                ? 0
                : ((data.getPhotosSize() > 0 ? 1 : 0) + 1);
    }

    @Override
    public int getItemCount() {
        return (data == null ? 0
                : (getHeaderWithItemsSize()
                + controllerMain.getControllerComments().getSize(data.getId())));
    }

    public void updateData(@NonNull MarketplaceItem item, View rootView) {
        this.data = item;
        this.rootView = rootView;

        notifyDataSetChanged();
    }

    public boolean isComment(int position) {
        return position >= getHeaderWithItemsSize();
    }

    public void updateDataHeader(boolean isLikeAvailable) {
        this.isLikeEnabled = isLikeAvailable;
        if (getItemCount() > 0)
            notifyItemChanged(0);
    }

    private class ViewHolderComment extends ViewHolder implements View.OnClickListener {
        private static final int NO_VALUE = 0;
        private ImageView ivUser;
        private TextView tvUser, tvText;
        private int id;

        public ViewHolderComment(View itemView) {
            super(itemView);
            ivUser = (ImageView) itemView.findViewById(R.id.ivUser);
            tvUser = (TextView) itemView.findViewById(R.id.tvName);
            tvText = (TextView) itemView.findViewById(R.id.tvComment);
            ivUser.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (id == NO_VALUE) return;

            String uri = "";
            if (id < 0)
                uri = String.format(controllerMain.getString(R.string.address_group_format), Math.abs(id));
            else
                uri = String.format(controllerMain.getString(R.string.address_user_format), id);

            controllerMain.openUrl(
                    v.getContext()
                    , rootView
                    , v.getContext().getString(R.string.action_view_browser_error)
                    , Uri.parse(uri));
        }

        @Override
        public void setData(int position) {
            super.setData(position);

            if (data == null) {
                ivUser.setImageDrawable(null);
                tvUser.setText(R.string.comment_no_title);
                tvText.setText(R.string.comment_no_text);
                id = NO_VALUE;
                return;
            }

            CommentsBlob.CommentInfo info = controllerMain.getControllerComments().getCommentsItem(data.getId(), position - getHeaderWithItemsSize());
            if (info == null) {
                ivUser.setImageDrawable(null);
                tvUser.setText(R.string.comment_no_title);
                tvText.setText(R.string.comment_no_text);
                id = NO_VALUE;
                return;
            }

            id = info.getFrom_id();
            String url = info.getImageUrl();
            if (TextUtils.isEmpty(url))
                ivUser.setImageDrawable(null);
            else
                Picasso.with(controllerMain).load(url).into(ivUser);
            tvUser.setText(info.getTitle());
            tvText.setText(info.getText());
        }
    }

    private class ViewHolderPhotos extends ViewHolder {

        private ItemPhotosAdapter adapter;

        public ViewHolderPhotos(View itemView) {
            super(itemView);
            RecyclerView rvPhotos = (RecyclerView) itemView.findViewById(R.id.rvPhotos);
            int columns = controllerMain.getResources().getInteger(R.integer.photos_columns);
            rvPhotos.setLayoutManager(new GridLayoutManager(itemView.getContext(), columns));
            rvPhotos.addItemDecoration(new ItemPhotosDecoration(controllerMain, columns));
            adapter = new ItemPhotosAdapter(controllerMain);
            rvPhotos.setAdapter(adapter);
        }

        @Override
        public void setData(int position) {
            if (data == null)
                return;
            adapter.updateData(data);
        }

        //region itemDecorator
        private class ItemPhotosDecoration extends RecyclerView.ItemDecoration {

            private int columns;
            private int margin;

            public ItemPhotosDecoration(Context context, int columns) {
                margin = (int) context.getResources().getDimension(R.dimen.view_photos_list_margin);
                this.columns = columns;
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                int position = parent.getChildLayoutPosition(view);

                if (position >= columns)
                    outRect.top = 2 * margin;
                if (position % 2 == 0) { //items
                    outRect.right = margin;
                } else {
                    outRect.left = margin;
                    //margins sum = const
                }
            }
        }
        //endregion
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
