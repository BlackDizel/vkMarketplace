package org.byters.vkmarketplace.ui.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.NewsItem;
import org.byters.vkmarketplace.ui.activities.ActivityMain;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private ControllerMain controllerMain;

    public NewsAdapter(ControllerMain context) {
        controllerMain = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return controllerMain.getControllerNews().getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final int NO_VALUE = -1;
        private TextView textView;
        private ImageView ivItem;
        private int newsId;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvTitle);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);

            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            NewsItem item = controllerMain.getControllerNews().getItem(position);
            if (item == null) {
                newsId = NO_VALUE;
                textView.setVisibility(View.GONE);
                ivItem.setImageDrawable(null);
            } else {
                newsId = item.getId();
                if (TextUtils.isEmpty(item.getText()))
                    textView.setVisibility(View.GONE);
                else textView.setVisibility(View.VISIBLE);

                textView.setText(item.getText());
                String url = item.getPhotoUri();
                if (!TextUtils.isEmpty(url))
                    ImageLoader.getInstance().displayImage(url, ivItem);
            }
        }

        @Override
        public void onClick(View v) {
            if (newsId == NO_VALUE) return;

            Intent intent = new Intent(Intent.ACTION_VIEW);
            String uri = String.format("%s?w=wall%d_%d"
                    , controllerMain.getString(R.string.market_address)
                    , controllerMain.getResources().getInteger(R.integer.market)
                    , newsId);

            android.util.Log.v("some", uri);
            intent.setData(Uri.parse(uri));

            if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                v.getContext().startActivity(intent);
            } else {
                if (v.getContext() instanceof ActivityMain) {
                    Snackbar.make(((ActivityMain) v.getContext()).findViewById(R.id.rootView)
                            , R.string.action_view_browser_error
                            , Snackbar.LENGTH_SHORT).show();
                }
            }

        }
    }
}
