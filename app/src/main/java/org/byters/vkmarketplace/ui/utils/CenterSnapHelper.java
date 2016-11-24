package org.byters.vkmarketplace.ui.utils;

import android.graphics.Rect;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CenterSnapHelper extends LinearSnapHelper {
    @Override
    public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager lm, View view) {
        Rect rect = new Rect();
        lm.getDecoratedBoundsWithMargins(view, rect);
        return new int[]{rect.centerX() - lm.getWidth() / 2 - lm.getPaddingLeft(), 0};
    }
}