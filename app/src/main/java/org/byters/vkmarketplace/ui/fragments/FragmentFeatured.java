package org.byters.vkmarketplace.ui.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.byters.vkmarketplace.BuildConfig;
import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerAlbums;
import org.byters.vkmarketplace.controllers.ControllerItems;
import org.byters.vkmarketplace.controllers.ControllerNews;
import org.byters.vkmarketplace.controllers.ControllerUserData;
import org.byters.vkmarketplace.ui.activities.ActivityBase;
import org.byters.vkmarketplace.ui.adapters.ItemsAdapter;
import org.byters.vkmarketplace.ui.adapters.ItemsAdapterCategories;

public class FragmentFeatured extends FragmentBase
        implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView.Adapter adapter;

    public static FragmentFeatured newInstance() {
        return new FragmentFeatured();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_featured, container, false);

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.srlItems);
        refreshLayout.setOnRefreshListener(this);

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rvItems);
        setList(rv);
        return v;
    }

    private void setList(RecyclerView rv) {

        if (BuildConfig.shopListType == BuildConfig.shopListTypeGrid) {
            adapter = new ItemsAdapter();
            int columns = getResources().getInteger(R.integer.items_list_columns);
            GridLayoutManager manager = new GridLayoutManager(rv.getContext(), columns);
            manager.setSpanSizeLookup(new SpanSizeLookupWithHeader());
            rv.setLayoutManager(manager);
            rv.addItemDecoration(new ItemDecoration(rv.getContext()));
        } else {
            adapter = new ItemsAdapterCategories();
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        rv.setAdapter(adapter);
    }

    public void updateData() {
        if (refreshLayout != null) refreshLayout.setRefreshing(false);
        if (adapter != null) adapter.notifyDataSetChanged();
        ActivityBase.setIsOffline((ActivityBase) getActivity(), false);
    }

    public void updateWithError() {
        if (ControllerItems.getInstance().getModel().isEmpty())
            ((ActivityBase) getActivity()).setState(ActivityBase.STATE_ERROR);
        else
            ActivityBase.setIsOffline((ActivityBase) getActivity(), true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        if (getActivity() == null) return;
        ControllerItems.getInstance().updateData(getContext());
        ControllerNews.getInstance().updateData(getContext());

        //todo add interface and move code to activityMain
        ControllerUserData.getInstance().updateUserData(getContext());
        ControllerAlbums.getInstance().updateData(getContext());
    }

    private class SpanSizeLookupWithHeader extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int position) {
            return position == 0 ? 2 : 1;
        }
    }

    //region itemDecorator
    private class ItemDecoration extends RecyclerView.ItemDecoration {

        private int margin;

        ItemDecoration(Context context) {
            margin = (int) context.getResources().getDimension(R.dimen.view_item_list_margin);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildLayoutPosition(view);

            if (position == 0)
                outRect.bottom = 2 * margin;
            else
                outRect.top = 2 * margin;

            if (position == 0) {
            } else if (position % 2 == 0) {
                outRect.right = 2 * margin;
                outRect.left = margin;
            } else {
                outRect.right = margin;
                outRect.left = 2 * margin;

                //margins sum = const
            }

        }
    }
    //endregion

}
