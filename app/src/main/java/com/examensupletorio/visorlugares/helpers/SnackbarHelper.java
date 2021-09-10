package com.examensupletorio.visorlugares.helpers;

import android.view.View;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarHelper {
    public static void showInfiniteMessage(View view, String text, String actionText) {
        Snackbar snackbar = Snackbar.make(view, text, BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setAction(actionText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
