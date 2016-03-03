package org.byters.vkmarketplace.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.byters.vkmarketplace.R;

public class FragmentGoods extends FragmentBase {


    public static FragmentGoods newInstance() {
        return new FragmentGoods();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goods, container, false);

        return v;
    }

}
