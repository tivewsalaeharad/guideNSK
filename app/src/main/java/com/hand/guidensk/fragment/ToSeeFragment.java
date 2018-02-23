package com.hand.guidensk.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hand.guidensk.R;

public class ToSeeFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_to_see, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dinner:

                break;
            case R.id.hotel:

                break;
            case R.id.to_see:

                break;
        }
    }
}
