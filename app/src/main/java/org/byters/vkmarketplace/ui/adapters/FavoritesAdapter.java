package org.byters.vkmarketplace.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.ControllerFavorites;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;
import org.byters.vkmarketplace.ui.activities.ActivityItemInfo;

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

        private TextView tvTitle;
        private ImageView ivItem;
        private int id;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);

            itemView.findViewById(R.id.ivRemove).setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            id = controllerMain.getControllerFavorites().getItem(position);
            MarketplaceItem item = controllerMain.getControllerItems().getModel().getItemById(id);

            if (item == null) {
                tvTitle.setText(R.string.item_cart_error_value);
                ivItem.setImageDrawable(null);
                return;
            }

            tvTitle.setText(item.getTitle());
            ImageLoader.getInstance().displayImage(item.getThumb_photo(), ivItem);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ivRemove) {
                int pos = controllerMain.getControllerFavorites().getItemPosition(id);
                if (pos == ControllerFavorites.NO_VALUE) return;
                controllerMain.getControllerFavorites().removeItem(pos);
                notifyItemRemoved(pos);
            }
            if (v == itemView) {
                ActivityItemInfo.display(itemView.getContext(), id);
            }
        }
    }
}
