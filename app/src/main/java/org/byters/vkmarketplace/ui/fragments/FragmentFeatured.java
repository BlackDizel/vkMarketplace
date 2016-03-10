package org.byters.vkmarketplace.ui.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.ui.activities.ActivityBase;
import org.byters.vkmarketplace.ui.adapters.ItemsAdapter;

public class FragmentFeatured extends FragmentBase
        implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private ItemsAdapter adapter;

    public static FragmentFeatured newInstance() {
        return new FragmentFeatured();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_featured, container, false);

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.srlItems);
        refreshLayout.setOnRefreshListener(this);

        adapter = new ItemsAdapter((ControllerMain) v.getContext().getApplicationContext());

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rvItems);
        int columns = getResources().getInteger(R.integer.items_list_columns);
        GridLayoutManager manager = new GridLayoutManager(v.getContext(), columns);
        manager.setSpanSizeLookup(new SpanSizeLookupWithHeader());
        rv.setLayoutManager(manager);
        rv.addItemDecoration(new ItemDecoration(v.getContext()));
        rv.setAdapter(adapter);

        return v;
    }

    public void updateData() {
        if (refreshLayout != null) refreshLayout.setRefreshing(false);
        if (adapter != null) adapter.updateData();
        ((ActivityBase) getActivity()).setIsOffline((ActivityBase) getActivity(), false);

    }

    public void updateWithError() {
        if (((ControllerMain) getContext().getApplicationContext()).getControllerItems().getModel().isEmpty())
            ((ActivityBase) getActivity()).setState(ActivityBase.STATE_ERROR);
        else
            ((ActivityBase) getActivity()).setIsOffline((ActivityBase) getActivity(), true);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        if (getActivity() != null) {
            ((ControllerMain) getContext().getApplicationContext()).updateMarketList();
            ((ControllerMain) getContext().getApplicationContext()).updateNews();
        }
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

        public ItemDecoration(Context context) {
            margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                    , context.getResources().getDimension(R.dimen.view_item_list_margin)
                    , context.getResources().getDisplayMetrics());

        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildLayoutPosition(view);

            outRect.top = 2 * margin;

            if (position == 0) {
                outRect.right = 2 * margin;
                outRect.left = 2 * margin;
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
