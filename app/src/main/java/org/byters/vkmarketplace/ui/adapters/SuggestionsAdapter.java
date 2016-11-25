package org.byters.vkmarketplace.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.ControllerItems;
import org.byters.vkmarketplace.controllers.controllers.ControllerSuggestions;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.activities.ActivityItemInfo;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.ViewHolder> {
    private static final int NO_VALUE = -1;
    private int itemId;

    public SuggestionsAdapter() {
        itemId = NO_VALUE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_list_suggestions_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return itemId == NO_VALUE ? 0 : ControllerSuggestions.getInstance().getSize(itemId);
    }

    public void updateData(int itemId) {
        this.itemId = itemId;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivItem;
        private TextView tvTitle;
        private int id;

        public ViewHolder(View itemView) {
            super(itemView);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            MarketplaceItem item = null;
            id = ControllerSuggestions.getInstance().getItem(itemId, position);
            if (id != ControllerSuggestions.NO_VALUE)
                item = ControllerItems.getInstance().getModel().getItemById(id);

            if (id == ControllerSuggestions.NO_VALUE || item == null) {
                ivItem.setImageDrawable(null);
                tvTitle.setText(R.string.suggestions_list_item_error_text);
                return;
            }

            tvTitle.setText(item.getTitle());
            if (item.getPhotosSize() == 0)
                ivItem.setImageDrawable(null);
            else
                Picasso.with(itemView.getContext())
                        .load(item.getPhotos().get(0).getSrc_big(itemView.getContext()))
                        .into(ivItem);
        }

        @Override
        public void onClick(View v) {
            if (id == ControllerSuggestions.NO_VALUE) return;
            ActivityItemInfo.display(v.getContext(), id);
        }
    }
}
