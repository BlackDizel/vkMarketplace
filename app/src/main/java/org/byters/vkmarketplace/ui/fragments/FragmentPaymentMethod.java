package org.byters.vkmarketplace.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.ui.adapters.AdapterPaymentMethod;

public class FragmentPaymentMethod extends FragmentBase {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment_method, container, false);
        setList(v);
        return v;
    }

    private void setList(View v) {
        RecyclerView rvList = (RecyclerView) v.findViewById(R.id.rvItems);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvList.setAdapter(new AdapterPaymentMethod());
    }
}
