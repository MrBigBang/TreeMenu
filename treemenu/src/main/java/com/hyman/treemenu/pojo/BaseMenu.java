package com.hyman.treemenu.pojo;

import android.graphics.drawable.Drawable;

/**
 * Created by hyman on 16/5/9.
 */
public class BaseMenu {
    public String title;
    public Drawable leftIcon;
    public Drawable rightIcon;

    protected int tag;

    public BaseMenu() {
    }

    public BaseMenu(String title) {
        this.title = title;
    }

    public BaseMenu(String title, Drawable leftIcon, Drawable rightIcon) {
        this.title = title;
        this.leftIcon = leftIcon;
        this.rightIcon = rightIcon;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }


}
