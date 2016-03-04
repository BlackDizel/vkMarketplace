package org.byters.vkmarketplace.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.activities.ActivityItemInfo;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private ControllerMain controllerMain;

    public SearchResultAdapter(Context context) {
        this.controllerMain = (ControllerMain) context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return controllerMain.getControllerSearchResult().getSize();
    }

    public void updateData() {
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private static final int NO_VALUE = -1;
        ImageView ivItem;
        TextView tvTitle, tvPrice;
        private int id;


        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            itemView.findViewById(R.id.ivFav).setVisibility(View.GONE);

            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            MarketplaceItem item = controllerMain.getControllerSearchResult().getItem(position);
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
                Picasso.with(controllerMain).load(item.getThumb_photo()).into(ivItem);
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
