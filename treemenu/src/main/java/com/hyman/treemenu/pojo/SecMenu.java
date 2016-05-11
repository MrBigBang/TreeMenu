package com.hyman.treemenu.pojo;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by hyman on 16/5/9.
 */
public class SecMenu extends BaseMenu {
    public List<BaseMenu> subMenus;

    public SecMenu() {
    }

    public SecMenu(String title) {
        super(title);
    }

    public SecMenu(String title, List<BaseMenu> subMenus) {
        super(title);
        this.subMenus = subMenus;
    }

    public SecMenu(String title, Drawable leftIcon, Drawable rightIcon) {
        this(title, leftIcon, rightIcon, null);
    }

    public SecMenu(String title, Drawable leftIcon, Drawable rightIcon, List<BaseMenu> subMenus) {
        super(title, leftIcon, rightIcon);
        this.subMenus = subMenus;
    }
}
