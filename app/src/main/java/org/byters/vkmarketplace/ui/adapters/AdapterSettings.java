package org.byters.vkmarketplace.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerNotifications;

public class AdapterSettings extends RecyclerView.Adapter<AdapterSettings.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_settings, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return ControllerNotifications.getInstance().getNotificationTypesNum();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener {
        private CheckBox cbItem;

        public ViewHolder(View itemView) {
            super(itemView);
            cbItem = (CheckBox) itemView.findViewById(R.id.cbItem);
            cbItem.setOnCheckedChangeListener(this);
        }

        void setData(int position) {
            cbItem.setText(ControllerNotifications.getInstance().getNotificationTypeTitleResource(position));
            cbItem.setChecked(ControllerNotifications.getInstance().isNotificationTypeEnabled(position));
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ControllerNotifications.getInstance().setNotificationTypeEnabled(getAdapterPosition(), isChecked);
        }
    }
}
