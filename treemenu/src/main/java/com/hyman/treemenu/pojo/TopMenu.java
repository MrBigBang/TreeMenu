package com.hyman.treemenu.pojo;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by hyman on 16/5/9.
 */
public class TopMenu extends BaseMenu{
    public List<SecMenu> subMenus;

    public TopMenu() {
    }

    public TopMenu(String title) {
        super(title);
    }

    public TopMenu(String title, List<SecMenu> subMenus) {
        super(title);
        this.subMenus = subMenus;
    }

    public TopMenu(String title, Drawable leftIcon, Drawable rightIcon) {
        this(title, leftIcon, rightIcon, null);
    }

    public TopMenu(String title, Drawable leftIcon, Drawable rightIcon, List<SecMenu> subMenus) {
        super(title, leftIcon, rightIcon);
        this.subMenus = subMenus;
    }
}
