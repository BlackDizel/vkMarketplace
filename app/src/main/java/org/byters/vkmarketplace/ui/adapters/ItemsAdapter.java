package org.byters.vkmarketplace.ui.adapters;

import android.app.Activity;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
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

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private ControllerMain controllerMain;

    public ItemsAdapter(@NonNull ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM)
            return new ViewHolderItem(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_list_items, parent, false));
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
        return controllerMain.getControllerItems().getModel().getSize() + 1;
    }

    public void updateData() {
        notifyDataSetChanged();
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
            rvHeader.setLayoutManager(new LinearLayoutManager(controllerMain, LinearLayoutManager.HORIZONTAL, false));
            adapter = new NewsAdapter(controllerMain);
            rvHeader.setAdapter(adapter);
            rvHeader.addItemDecoration(new ItemDecoration());
        }

        @Override
        public void setData(int position) {
            adapter.notifyDataSetChanged();
        }

        //region itemDecorator
        private class ItemDecoration extends RecyclerView.ItemDecoration {

            private int margin;

            public ItemDecoration() {
                margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                        , controllerMain.getResources().getDimension(R.dimen.view_item_news_margin)
                        , controllerMain.getResources().getDisplayMetrics());
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                int position = parent.getChildLayoutPosition(view);
                if (position != 0)
                    outRect.left = margin;
            }
        }
        //endregion
    }

    public class ViewHolderItem extends ViewHolder implements View.OnClickListener {
        private static final int NO_VALUE = -1;
        ImageView ivItem;
        TextView tvTitle, tvPrice;
        private int id;

        public ViewHolderItem(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(int position) {
            MarketplaceItem item = controllerMain.getControllerItems().getModel().get(position - 1);
            if (item == null) {
                tvTitle.setText("");
                tvPrice.setText("");
                id = NO_VALUE;
                ivItem.setImageDrawable(null);
            } else {
                tvTitle.setText(item.getTitle());
                if (item.getPrice() != null && !TextUtils.isEmpty(item.getPrice().getText()))
                    tvPrice.setText(item.getPrice().getText().toUpperCase().replace(".", ""));
                else tvPrice.setText("");
                ImageLoader.getInstance().displayImage(item.getThumb_photo(), ivItem);
                id = item.getId();
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getContext() instanceof Activity && id != NO_VALUE)
                ActivityItemInfo.display(v.getContext(), id);
        }
    }
}
