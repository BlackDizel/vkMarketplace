package org.byters.vkmarketplace.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.byters.vkmarketplace.BuildConfig;
import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.MenuEnum;
import org.byters.vkmarketplace.model.dataclasses.AccountInfo;
import org.byters.vkmarketplace.model.dataclasses.AlbumBlob;
import org.byters.vkmarketplace.ui.activities.ActivityBonus;
import org.byters.vkmarketplace.ui.activities.ActivityChat;
import org.byters.vkmarketplace.ui.activities.ActivityFavorites;
import org.byters.vkmarketplace.ui.activities.ActivityMain;
import org.byters.vkmarketplace.ui.activities.ActivitySettings;
import org.byters.vkmarketplace.ui.utils.PluralName;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM_CATEGORY = 1;
    private static final int TYPE_MENU_ITEM = 2;

    private ControllerMain controllerMain;

    public MenuAdapter(Context context) {
        controllerMain = ((ControllerMain) context.getApplicationContext());
    }

    public void updateData() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        if (position < getMenuCategoryItemsCount())
            return TYPE_ITEM_CATEGORY;
        return TYPE_MENU_ITEM;
    }

    private int getMenuCategoryItemsCount() {
        return 1 + (BuildConfig.isCategoryListOnSideMenu ? controllerMain.getControllerAlbums().getSize() : 0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM_CATEGORY)
            return new ViewHolderItemCategory(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_menu_item_category, parent, false));
        else if (viewType == TYPE_HEADER)
            return new ViewHolderHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_menu_header, parent, false));
        else
            return new ViewHolderMenuItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return getMenuCategoryItemsCount() + MenuEnum.values().length;
    }

    public class ViewHolderMenuItem extends ViewHolder
            implements View.OnClickListener {

        ImageView ivItem;
        TextView tvTitle;
        MenuEnum item;

        public ViewHolderMenuItem(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(int position) {
            super.setData(position);

            int itemPos = position - getMenuCategoryItemsCount();
            item = itemPos < 0 || itemPos >= MenuEnum.values().length ? null : MenuEnum.values()[itemPos];
            if (item == null) return;

            tvTitle.setText(item.getMenuResTitle());
            ivItem.setImageResource(item.getMenuResDrawable());
        }

        @Override
        public void onClick(View view) {
            if (item == null) return;

            switch (item) {
                case FAVORITES:
                    ActivityFavorites.display(itemView.getContext());
                    break;
                case BONUSES:
                    ActivityBonus.display(itemView.getContext());
                    break;
                case SETTINGS:
                    ActivitySettings.display(itemView.getContext());
                    break;
                case CHAT:
                    ActivityChat.display(itemView.getContext());
                    break;
                case FEEDBACK:
                    Intent intentSend = controllerMain.getIntentSendEmail(itemView.getContext()
                            , R.string.feedback_message_title
                            , R.string.feedback_message_body);

                    if (intentSend.resolveActivity(itemView.getContext().getPackageManager()) == null) {
                        Toast.makeText(itemView.getContext(), R.string.email_app_error_no_found, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    itemView.getContext().startActivity(intentSend);
                    break;
                case WEBSITE:
                    controllerMain.openUrl(itemView.getContext()
                            , itemView.getContext().getString(R.string.action_view_browser_error)
                            , Uri.parse(itemView.getContext().getString(R.string.market_address)));
                    break;
                case PHONE:
                    controllerMain.call(itemView.getContext()
                            , R.string.calling_error
                            , R.string.market_phone);
                    break;
                case RATE:
                    //todo implement
                    break;
            }
        }
    }

    public class ViewHolderItemCategory extends ViewHolder
            implements View.OnClickListener {
        private static final int NO_VALUE = -1;
        private TextView tvTitle, tvSubtitle;
        private ImageView ivItem;
        private int album_id;

        public ViewHolderItemCategory(View itemView) {
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
