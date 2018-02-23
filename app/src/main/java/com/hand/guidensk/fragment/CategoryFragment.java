package com.hand.guidensk.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hand.guidensk.R;
import com.hand.guidensk.adapter.StartAdapter;

public class CategoryFragment extends Fragment {

    ListView listStart;
    StartAdapter adapter;
    public int category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null);
        listStart = view.findViewById(R.id.list_start);
        adapter = new StartAdapter(getActivity(), category);
        listStart.setAdapter(adapter);
        return view;
    }
}
