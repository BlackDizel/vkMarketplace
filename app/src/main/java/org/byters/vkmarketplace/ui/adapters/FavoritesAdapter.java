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
import org.byters.vkmarketplace.controllers.controllers.ControllerFavorites;
import org.byters.vkmarketplace.model.dataclasses.FavoriteItem;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.activities.ActivityFavorites;
import org.byters.vkmarketplace.ui.activities.ActivityItemInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private ControllerMain controllerMain;

    public FavoritesAdapter(ControllerMain context) {
        controllerMain = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_fav, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }


    @Override
    public int getItemCount() {
        return controllerMain.getControllerFavorites().getSize();
    }

    public void updateData() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private static final int NO_VALUE = -1;
        private TextView tvTitle, tvDate;
        private ImageView ivItem;
        private int id;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);

            itemView.findViewById(R.id.ivRemove).setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            FavoriteItem favItem = controllerMain.getControllerFavorites().getItem(position);
            if (favItem == null) {
                tvTitle.setText(R.string.item_cart_error_value);
                tvDate.setText(R.string.item_cart_error_value);
                ivItem.setImageDrawable(null);
                id = NO_VALUE;
                return;
            }

            id = favItem.getId();
            MarketplaceItem item = controllerMain.getControllerItems().getModel().getItemById(id);

            if (item == null) {
                tvTitle.setText(R.string.item_cart_error_value);
                tvDate.setText(R.string.item_cart_error_value);
                ivItem.setImageDrawable(null);
                return;
            }

            tvTitle.setText(item.getTitle());
            tvDate.setText(new SimpleDateFormat(controllerMain.getString(R.string.fav_date_format)).format(new Date(favItem.getTime_added())));
            Picasso.with(controllerMain).load(item.getThumb_photo()).into(ivItem);
        }

        @Override
        public void onClick(View v) {
            if (id == NO_VALUE) return;
            if (v.getId() == R.id.ivRemove) {
                int pos = controllerMain.getControllerFavorites().getItemPosition(id);
                if (pos == ControllerFavorites.NO_VALUE) return;
                controllerMain.getControllerFavorites().removeItem(pos);
                notifyItemRemoved(pos);
                if (itemView.getContext() instanceof ActivityFavorites)
                    ((ActivityFavorites) itemView.getContext()).checkState();
            }
            if (v == itemView) {
                ActivityItemInfo.display(itemView.getContext(), id);
            }
        }
    }
}
