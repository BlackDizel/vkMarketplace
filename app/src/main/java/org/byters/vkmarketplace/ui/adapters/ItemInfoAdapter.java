package org.byters.vkmarketplace.ui.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apmem.tools.layouts.FlowLayout;
import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.CommentsBlob;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.activities.ActivityBase;
import org.byters.vkmarketplace.ui.utils.SharingHelper;
import org.byters.vkmarketplace.ui.utils.StartSnapHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemInfoAdapter extends RecyclerView.Adapter<ItemInfoAdapter.ViewHolder> {
    private static final int TYPE_ERROR = -1;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PHOTOS = 1;
    private static final int TYPE_COMMENT = 2;
    private static final int TYPE_COLLECTION = 3;
    private static final int TYPE_SUGGESTIONS = 4;

    private static final double COLLECTIONS_MAX_NUM = 10;

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
        switch (viewType) {
            case TYPE_HEADER:
                return new ViewHolderHeader(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_item_info_list_header, parent, false));
            case TYPE_PHOTOS:
                return new ViewHolderPhotos(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_list_photos_list, parent, false));
            case TYPE_COMMENT:
                return new ViewHolderComment(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_item_info_list_comment, parent, false));
            case TYPE_COLLECTION:
                return new ViewHolderCollections(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_item_info_list_collections, parent, false));
            case TYPE_SUGGESTIONS:
                return new ViewHolderSuggestions(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_item_info_list_suggestions, parent, false));
        }
        //todo return error type viewholder
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        if (isPhotosPosition(position)) return TYPE_PHOTOS;
        if (isCollectionsPosition(position)) return TYPE_COLLECTION;
        if (isSuggestionsPosition(position)) return TYPE_SUGGESTIONS;
        if (isCommentsPosition(position)) return TYPE_COMMENT;
        return TYPE_ERROR;
    }

    private boolean isSuggestionsPosition(int position) {
        return position + 1 == getItemCount();
    }

    private boolean isCollectionsPosition(int position) {
        if (data == null || data.getAlbumIdsSize() == 0) return false;
        return position + 2 == getItemCount();
    }

    private boolean isCommentsPosition(int position) {
        if (data == null) return false;
        if (data.getPhotosSize() > 0 && position >= 2) return true;
        if (data.getPhotosSize() == 0 && position >= 1) return true;
        return false;
    }

    private boolean isPhotosPosition(int position) {
        if (data == null) return false;
        return data.getPhotosSize() > 0 && position == 1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }


    @Override
    public int getItemCount() {
        if (data == null) return 0;
        int count = 1;//add header
        if (data.getPhotosSize() > 0) ++count;//add photos
        if (data.getAlbumIdsSize() > 0) ++count;//add collections
        count += controllerMain.getControllerComments().getSize(data.getId()); //add comments
        ++count; //add suggestions
        return count;
    }

    public void updateData(@NonNull MarketplaceItem item, View rootView) {
        this.data = item;
        this.rootView = rootView;

        notifyDataSetChanged();
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

            controllerMain.openUrl(v.getContext(), v.getContext().getString(R.string.action_view_browser_error), Uri.parse(uri));
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

            CommentsBlob.CommentInfo info = controllerMain.getControllerComments().getCommentsItem(data.getId(), getCommentPosition(position));
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

        private int getCommentPosition(int position) {
            if (data == null) return 0;
            return Math.max(0, position - 1 - (data.getPhotosSize() > 0 ? 1 : 0));
        }
    }

    private class ViewHolderPhotos extends ViewHolder {

        private ItemPhotosAdapter adapter;

        public ViewHolderPhotos(View itemView) {
            super(itemView);
            RecyclerView rvPhotos = (RecyclerView) itemView.findViewById(R.id.rvPhotos);
            rvPhotos.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvPhotos.addItemDecoration(new ItemPhotosDecoration(controllerMain));
            adapter = new ItemPhotosAdapter(controllerMain);
            rvPhotos.setAdapter(adapter);
            SnapHelper snapHelper = new StartSnapHelper();
            snapHelper.attachToRecyclerView(rvPhotos);
        }

        @Override
        public void setData(int position) {
            if (data == null)
                return;
            adapter.updateData(data);
        }

        //region itemDecorator
        private class ItemPhotosDecoration extends RecyclerView.ItemDecoration {

            private int margin;

            public ItemPhotosDecoration(Context context) {
                margin = (int) context.getResources().getDimension(R.dimen.view_photos_list_margin);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                outRect.right = margin;
                outRect.top = margin;
                outRect.bottom = margin;
            }
        }
        //endregion
    }

    private class ViewHolderHeader extends ViewHolder
            implements View.OnClickListener {
        private TextView tvDescription, tvPrice, tvLikes, tvTitle;
        private View llLikes;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            tvDescription = ((TextView) itemView.findViewById(R.id.tvDescription));
            tvPrice = ((TextView) itemView.findViewById(R.id.tvPrice));
            tvLikes = ((TextView) itemView.findViewById(R.id.tvLikes));
            tvTitle = ((TextView) itemView.findViewById(R.id.tvTitle));
            itemView.findViewById(R.id.llShare).setOnClickListener(this);
            llLikes = itemView.findViewById(R.id.llLikes);
            llLikes.setOnClickListener(this);
        }

        @Override
        public void setData(int position) {
            if (data == null) return;
            tvDescription.setText(Html.fromHtml(data.getDescription()));
            tvPrice.setText(data.getPrice().getText().toUpperCase().replace(".", ""));
            tvTitle.setText(data.getTitle());

            setLikes();

            llLikes.setClickable(isLikeEnabled);
        }

        private void setLikes() {
            if (data == null || data.getLikes() == null) return;
            tvLikes.setText(String.format("%d", data.getLikes().getCount()));
        }

        private void addLike() {
            if (data == null || data.getLikes() == null) return;
            tvLikes.setText(String.format("%d", data.getLikes().getCount() + 1));
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

                    addLike();
                    controllerMain.addLike(data.getId(), new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (data == null) return;
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            setLikes();
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

    private class ViewHolderCollections extends ViewHolder
            implements View.OnClickListener {

        FlowLayout layout;

        public ViewHolderCollections(View view) {
            super(view);
            layout = (FlowLayout) view;
        }

        @Override
        public void setData(int position) {
            super.setData(position);

            if (data == null) return;
            layout.removeAllViews();
            int size = (int) Math.min(data.getAlbumIdsSize(), COLLECTIONS_MAX_NUM);
            for (int i = 0; i < size; ++i) {
                String title = data.getCollectionTitle(controllerMain, i);
                if (TextUtils.isEmpty(title)) continue;
                View v = LayoutInflater.from(layout.getContext())
                        .inflate(R.layout.view_item_info_list_collections_item, layout, true);
                TextView tv = ((TextView) v.findViewById(R.id.tvTitle));
                tv.setTag(data.getAlbumId(i));
                tv.setText(title);
                tv.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            Object o = v.getTag();
            int album_id = MarketplaceItem.NO_VALUE;
            if (o instanceof Integer)
                album_id = (int) o;

            if (album_id == MarketplaceItem.NO_VALUE)
                return;

            controllerMain.getControllerItems().setAlbum(album_id);
            if (v.getContext() instanceof ActivityBase)
                ((ActivityBase) v.getContext()).onBackPressed();
        }
    }

    private class ViewHolderSuggestions extends ViewHolder {
        RecyclerView rvSuggestions;
        SuggestionsAdapter adapter;

        public ViewHolderSuggestions(View view) {
            super(view);
            adapter = new SuggestionsAdapter(controllerMain);
            rvSuggestions = (RecyclerView) view.findViewById(R.id.rvSuggestions);
            rvSuggestions.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvSuggestions.setAdapter(adapter);
            SnapHelper snapHelper = new StartSnapHelper();
            snapHelper.attachToRecyclerView(rvSuggestions);
        }

        @Override
        public void setData(int position) {
            super.setData(position);
            if (data != null)
                adapter.updateData(data.getId());
        }
    }
}
