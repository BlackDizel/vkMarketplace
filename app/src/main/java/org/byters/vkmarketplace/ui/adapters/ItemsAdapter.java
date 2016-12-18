package org.byters.vkmarketplace.ui.adapters;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.byters.vkmarketplace.BuildConfig;
import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerFavorites;
import org.byters.vkmarketplace.controllers.ControllerItems;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.activities.ActivityItemInfo;
import org.byters.vkmarketplace.ui.utils.CenterSnapHelper;
import org.byters.vkmarketplace.ui.utils.ShopHelper;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM)
            return new ViewHolderItem(LayoutInflater.from(parent.getContext())
                    .inflate(ShopHelper.getItemLayout(), parent, false));
        else return new ViewHolderHeader(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_items_header, parent, false));
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
        return ControllerItems.getInstance().getModel().getSize() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(int position) {
        }
    }

    public class ViewHolderHeader extends ViewHolder {

        RecyclerView rvHeader;
        NewsAdapter adapter;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            rvHeader = (RecyclerView) itemView.findViewById(R.id.rvHeader);
            rvHeader.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            adapter = new NewsAdapter();
            rvHeader.setAdapter(adapter);
            rvHeader.addItemDecoration(new ItemDecoration());
            SnapHelper snapHelper = new CenterSnapHelper();
            snapHelper.attachToRecyclerView(rvHeader);
        }

        @Override
        public void setData(int position) {
            adapter.notifyDataSetChanged();
        }

        //region itemDecorator
        private class ItemDecoration extends RecyclerView.ItemDecoration {

            private int margin;

            public ItemDecoration() {
                margin = (int) itemView.getContext().getResources().getDimension(R.dimen.view_item_news_margin);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                int position = parent.getChildLayoutPosition(view);
                if (position == 0)
                    outRect.left = margin;
                if (position + 1 == adapter.getItemCount())
                    outRect.right = margin;
            }
        }
        //endregion
    }

    public class ViewHolderItem extends ViewHolder implements View.OnClickListener {
        private static final int NO_VALUE = -1;
        ImageView ivItem, ivFav;
        TextView tvTitle, tvPrice;
        private int id;

        public ViewHolderItem(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvPrice.setVisibility(BuildConfig.isItemPriceOnMainScreen ? View.VISIBLE : View.GONE);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            ivFav = (ImageView) itemView.findViewById(R.id.ivFav);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(int position) {
            MarketplaceItem item = ControllerItems.getInstance().getModel().get(position - 1);
            if (item == null) {
                tvTitle.setText("");
                tvPrice.setText("");
                id = NO_VALUE;
                ivItem.setImageDrawable(null);
                ivFav.setVisibility(View.GONE);
            } else {
                tvTitle.setText(item.getTitle());
                if (item.getPrice() != null && !TextUtils.isEmpty(item.getPrice().getText()))
                    tvPrice.setText(item.getPrice().getText().toUpperCase().replace(".", ""));
                else tvPrice.setText("");
                Picasso.with(itemView.getContext()).load(item.getThumb_photo()).into(ivItem);
                id = item.getId();
                if (ControllerFavorites.getInstance().isFavorite(id))
                    ivFav.setVisibility(View.VISIBLE);
                else
                    ivFav.setVisibility(View.GONE);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = position % 2 == 0 ? Gravity.END : Gravity.START;
                ivFav.setLayoutParams(params);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getContext() instanceof Activity && id != NO_VALUE)
                ActivityItemInfo.display(v.getContext(), id);
        }
    }
}
