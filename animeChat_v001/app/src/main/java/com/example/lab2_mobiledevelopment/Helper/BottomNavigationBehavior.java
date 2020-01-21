package com.example.lab2_mobiledevelopment.Helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.Gravity;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.logging.Logger;

public class BottomNavigationBehavior<V extends View>  extends CoordinatorLayout.Behavior<V> {

    final private static Logger log = Logger.getLogger(BottomNavigationBehavior.class.getName());

    public BottomNavigationBehavior (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomNavigationBehavior () {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V child, View directTargetChild,
                                       View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll (CoordinatorLayout coordinatorLayout,
                                   V child,
                                   View target,
                                   int dx,
                                   int dy,
                                   int[] consumed,
                                   int type) {

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        child.setTranslationY(Math.max(0f, Math.min(child.getHeight(), child.getTranslationY() + dy)));
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        if(dependency instanceof Snackbar.SnackbarLayout) {
            updateSnackbar(child, (Snackbar.SnackbarLayout)dependency);
        }

        return super.layoutDependsOn(parent, child, dependency);
    }


    private void updateSnackbar(View child, Snackbar.SnackbarLayout snackbarLayout) {
        if(snackbarLayout.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)snackbarLayout.getLayoutParams();

            params.setAnchorId(child.getId());
            params.anchorGravity = Gravity.TOP;
            params.gravity = Gravity.TOP;
            snackbarLayout.setLayoutParams(params);
        }
    }


}