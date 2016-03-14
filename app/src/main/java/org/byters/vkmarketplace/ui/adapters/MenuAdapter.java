package org.byters.vkmarketplace.ui.adapters;

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
import org.byters.vkmarketplace.model.dataclasses.AccountInfo;
import org.byters.vkmarketplace.model.dataclasses.AlbumBlob;
import org.byters.vkmarketplace.ui.activities.ActivityMain;
import org.byters.vkmarketplace.ui.utils.PluralName;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ControllerMain controllerMain;
    private Context context;

    public MenuAdapter(Context context) {
        this.context = context;
        controllerMain = ((ControllerMain) context.getApplicationContext());
    }

    public void updateData() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM)
            return new ViewHolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_menu_item, parent, false));
        else
            return new ViewHolderHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_menu_header, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return controllerMain.getControllerAlbums().getSize() + 1;
    }

    public class ViewHolderItem extends ViewHolder
            implements View.OnClickListener {
        private static final int NO_VALUE = -1;
        private TextView tvTitle, tvSubtitle;
        private ImageView ivItem;
        private int album_id;

        public ViewHolderItem(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSubtitle = (TextView) itemView.findViewById(R.id.tvSubtitle);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            album_id = NO_VALUE;
        }

        @Override
        public void onClick(View v) {
            if (album_id != NO_VALUE) {
                controllerMain.getControllerItems().setAlbum(album_id);

                if (v.getContext() instanceof ActivityMain)
                    ((ActivityMain) v.getContext()).invalidateData();
            }
        }

        @Override
        public void setData(int position) {
            AlbumBlob.AlbumItem item = controllerMain.getControllerAlbums().getItem(position - 1);
            if (item == null) {
                tvTitle.setText(R.string.menu_item_text_error);
                tvSubtitle.setText("");
                ivItem.setImageDrawable(null);
                album_id = NO_VALUE;
            } else {
                tvTitle.setText(item.getTitle());
                tvSubtitle.setText(String.format("%s %s"
                        , String.valueOf(item.getCount())
                        , PluralName.ITEM.toString(controllerMain, item.getCount())));
                if (item.getPhoto() != null && !TextUtils.isEmpty(item.getPhoto().getLittlePhoto()))
                    Picasso.with(controllerMain).load(item.getPhoto().getLittlePhoto()).into(ivItem);
                album_id = item.getId();
            }
        }
    }

    public class ViewHolderHeader extends ViewHolder {

        private ImageView imgView;
        private TextView tvTitle;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.ivHeader);
            tvTitle = ((TextView) itemView.findViewById(R.id.tvTitle));
        }

        @Override
        public void setData(int position) {
            AccountInfo info = controllerMain.getControllerUserData().getData();
            if (info == null) {
                imgView.setImageDrawable(null);
                tvTitle.setText("");
            } else {
                Picasso.with(itemView.getContext()).load(info.getPhoto_max_orig()).into(imgView);
                tvTitle.setText(String.format("%s %s", info.getFirst_name(), info.getLast_name()));
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(int position) {
        }
    }

}
