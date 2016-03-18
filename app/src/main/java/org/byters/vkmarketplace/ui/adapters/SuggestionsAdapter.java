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
import org.byters.vkmarketplace.controllers.controllers.ControllerSuggestions;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.ViewHolder> {
    private static final int NO_VALUE = -1;
    ControllerMain controllerMain;
    private int itemId;

    public SuggestionsAdapter(ControllerMain controllerMain) {
        this.controllerMain = controllerMain;
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
        return itemId == NO_VALUE ? 0 : controllerMain.getControllerSuggestions().getSize(itemId);
    }

    public void updateData(int itemId) {
        this.itemId = itemId;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivItem;
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }

        public void setData(int position) {
            MarketplaceItem item = null;
            int id = controllerMain.getControllerSuggestions().getItem(itemId, position);
            if (id != ControllerSuggestions.NO_VALUE)
                item = controllerMain.getControllerItems().getModel().getItemById(id);

            if (id == ControllerSuggestions.NO_VALUE || item == null) {
                ivItem.setImageDrawable(null);
                tvTitle.setText("нет данных");
            } else {
                if (item.getPhotosSize() == 0)
                    ivItem.setImageDrawable(null);
                else
                    Picasso.with(controllerMain)
                            .load(item.getPhotos().get(0).getSrc_big(controllerMain))
                            .into(ivItem);
                tvTitle.setText(item.getTitle());
            }
        }
    }
}
