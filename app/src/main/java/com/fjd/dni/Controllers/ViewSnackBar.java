package com.fjd.dni.Controllers;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.fjd.dni.R;
import com.google.android.material.snackbar.Snackbar;

public interface ViewSnackBar {

     static void SnackBarDanger(View layout, String srt, Context context){
         Snackbar snack =Snackbar.make(layout, srt, Snackbar.LENGTH_LONG).setBackgroundTint(context.getColor(R.color.danger));
         View view = snack.getView();
         FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
         params.gravity = Gravity.TOP;
         view.setLayoutParams(params);
         snack.show();
    }
    static void SnackBarSuccess(View layout, String srt, Context context){
        Snackbar snack =Snackbar.make(layout, srt, Snackbar.LENGTH_LONG).setBackgroundTint(context.getColor(R.color.success));
        View view = snack.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }
    static void SnackBarWarring(View layout, String srt, Context context){
        Snackbar snack =Snackbar.make(layout, srt, Snackbar.LENGTH_LONG).setBackgroundTint(context.getColor(R.color.warning));
        View view = snack.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }
}
