package com.hand.guidensk.dialog;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hand.guidensk.R;

public class PermissionDialog extends DialogFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Window window = getDialog().getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        View v = inflater.inflate(R.layout.dialog_permission, null);
        v.findViewById(R.id.btn_ok).setOnClickListener(this);
        v.findViewById(R.id.btn_cancel).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        GetPermissionListener listener = (GetPermissionListener) getActivity();
        listener.onGetPermissionFromDialog(v.getId() == R.id.btn_ok);
        dismiss();
    }

    public interface GetPermissionListener { void onGetPermissionFromDialog(boolean confirmed); }

}