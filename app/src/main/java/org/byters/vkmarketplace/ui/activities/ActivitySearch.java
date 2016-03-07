package org.byters.vkmarketplace.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.controllers.controllers.utils.DataUpdateListener;
import org.byters.vkmarketplace.ui.adapters.SearchResultAdapter;

public class ActivitySearch extends ActivityBase
        implements SearchView.OnQueryTextListener
        , DataUpdateListener {

    private SearchResultAdapter adapter;

    public static void display(Context context) {
        context.startActivity(new Intent(context, ActivitySearch.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new GridLayoutManager(this, 2));
        rvItems.addItemDecoration(new ItemDecoration(this));
        adapter = new SearchResultAdapter(this);
        rvItems.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((ControllerMain) getApplicationContext()).getControllerSearchResult().setListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ControllerMain) getApplicationContext()).getControllerSearchResult().removeListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_menu, menu);

        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ((ControllerMain) getApplicationContext()).searchItems(newText);
        return false;
    }

    @Override
    public void onUpdated(int type) {
        if (type == TYPE_SEARCH)
            adapter.updateData();
    }

    @Override
    public void onError(int type) {
        //todo if no cached data, show error
        //todo else show offline mode and search in cache
        if (type == TYPE_SEARCH)
            adapter.updateData();
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

            if (position % 2 == 1) {
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
