package com.fisken_astet.fikenastet.base;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.fisken_astet.fikenastet.R;

public class ProgressDialogAvl {
    Dialog dialog;

    public ProgressDialogAvl(Context context) {
        View view = View.inflate(context, R.layout.dialog_progress, null);
        dialog = new Dialog(context, R.style.CustomDialogProgress);
        dialog.setContentView(view);
        dialog.setCancelable(false);
    }

    public void isLoading(boolean isLoading) {
        if (isLoading) {
            if (!dialog.isShowing()) {
                try {
                    System.gc();
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                System.gc();
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
