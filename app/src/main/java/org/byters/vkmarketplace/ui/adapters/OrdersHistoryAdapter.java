package org.byters.vkmarketplace.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerOrdersHistory;
import org.byters.vkmarketplace.model.dataclasses.OrderHistoryInfo;

public class OrdersHistoryAdapter extends RecyclerView.Adapter<OrdersHistoryAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_orders_history, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return ControllerOrdersHistory.getInstance().getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvItem, tvSum;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
            tvSum = (TextView) itemView.findViewById(R.id.tvSum);
        }

        public void setData(int position) {
            OrderHistoryInfo item = ControllerOrdersHistory.getInstance().getItem(position);
            if (item == null) {
                tvDate.setText("");
                tvItem.setText("");
                tvSum.setText("");
            } else {
                tvDate.setText(String.format(itemView.getContext().getString(R.string.order_history_date_format), item.getDate()));
                tvItem.setText(item.getInfo(itemView.getContext()));
                tvSum.setText(String.format(itemView.getContext().getString(R.string.order_history_sum_format), String.valueOf(item.getSum())));
            }
        }
    }
}
