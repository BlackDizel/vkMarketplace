package org.byters.vkmarketplace.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerListPaymentMethod;
import org.byters.vkmarketplace.model.PaymentMethodEnum;

public class AdapterPaymentMethod extends RecyclerView.Adapter<AdapterPaymentMethod.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_payment_method, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return ControllerListPaymentMethod.getInstance().getItemsCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView ivItem;
        TextView tvItem;
        PaymentMethodEnum currentPaymentMethod;

        public ViewHolder(View itemView) {
            super(itemView);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            currentPaymentMethod = ControllerListPaymentMethod.getInstance().getItem(position);
            if (currentPaymentMethod == null) return;
            ivItem.setImageResource(currentPaymentMethod.getImgRes());
            tvItem.setText(currentPaymentMethod.getTitleRes());
        }

        @Override
        public void onClick(View view) {
            //todo implement
        }
    }
}
