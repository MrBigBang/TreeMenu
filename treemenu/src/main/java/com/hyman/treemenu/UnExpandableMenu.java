package com.hyman.treemenu;

import android.content.Context;

/**
 * Created by hyman on 16/5/6.
 */
public class UnExpandableMenu extends MenuView {

    public UnExpandableMenu(Context context) {
        super(context);
        addMenuContent();
    }

    @Override
    protected void measureMenuLayout(int widthMeasureSpec, int heightMeasureSpec) {
        // call this method will cause stack overflow here
//        super.measure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int parentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int parentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(menuContentHeight, MeasureSpec.EXACTLY);
        measureChild(menuContent, parentWidthMeasureSpec, parentHeightMeasureSpec);

        setMeasuredDimension(width, menuContent.getMeasuredHeight());
    }
}
