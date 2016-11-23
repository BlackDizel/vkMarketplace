package org.byters.vkmarketplace.ui.utils;

import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class StartSnapHelper extends LinearSnapHelper {
    @Override
    public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager lm, View view) {
        return new int[]{view.getLeft() - lm.getPaddingLeft(), 0};
    }
}