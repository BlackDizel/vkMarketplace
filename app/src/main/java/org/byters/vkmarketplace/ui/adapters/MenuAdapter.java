package org.byters.vkmarketplace.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.MarketInfo;
import org.byters.vkmarketplace.ui.activities.ActivitySearchMarkets;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    ControllerMain controllerMain;
    Context context;

    public MenuAdapter(Context context) {
        this.context = context;
        controllerMain = ((ControllerMain) context.getApplicationContext());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return controllerMain.getControllerMarkets().getSize() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;
        private ImageView ivItem;
        private String market_uri;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.tvTitle);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
        }

        public void setData(int position) {
            if (position + 1 == getItemCount()) {
                market_uri = null;
                textView.setText(R.string.menu_add_market);
                //todo add image
            } else {
                MarketInfo item = controllerMain.getControllerMarkets().getItem(position);
                if (item == null) {
                    textView.setText(R.string.menu_item_text_error);
                    //todo add image
                } else {
                    market_uri = item.getAddress();
                    textView.setText(item.getTitle());
                    //todo add image
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(market_uri)) {
                ActivitySearchMarkets.display(context);
            } else {
                //todo navigate to selected market
            }
        }
    }

}
