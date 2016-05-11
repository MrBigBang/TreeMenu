package com.hyman.treemenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hyman.treemenu.pojo.BaseMenu;
import com.hyman.treemenu.pojo.SecMenu;
import com.hyman.treemenu.pojo.TopMenu;
import com.hyman.treemenu.utils.Trace;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by hyman on 16/5/6.
 */
public class DrawerMenuLayout extends LinearLayout implements ExpandableMenu.OnSubMenuClickListener {

    public interface OnMenuClickListener {
        void onMenuClick(MenuView menu);
    }

    OnMenuClickListener mOnMenuClickListener;

    private final Context context;
    // menu height
    private int menuHeight = 0;
    // is divider visible between two menu
    private boolean isDividerVisible = false;
    // is there a gap between each close menu
    private boolean hasGap = false;
    // the height of gap
    private int gapHeight = 2;
    // paddings
    private int mPaddingLeft = 0;
    private int mPaddingTop = 16;
    private int mPaddingRight = 0;
    private int mPaddingBottom = 0;
    // the indent of sub menu in EspandableMenu
    private int indent = 10;

    private Set<MenuView> menuSet = new HashSet<>();
    private SparseArray<WeakReference<View>> dividers = new SparseArray<>();
    private int dividerNum = 0;
    private int dividerColor;
    private int paddingLeft;

    private MenuView selectedMenu;
    private MenuView selectedSubMenu;
    private MenuView selectedThirdMenu;

    public DrawerMenuLayout(Context context) {
        this(context, null);
    }

    public DrawerMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = getContext();
        setOrientation(VERTICAL);

        menuHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.menu_height),
                getResources().getDisplayMetrics());

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DrawerMenuLayout, defStyleAttr, 0);
            try {
                menuHeight = a.getDimensionPixelSize(R.styleable.DrawerMenuLayout_menuHeight, menuHeight);
                isDividerVisible = a.getBoolean(R.styleable.DrawerMenuLayout_isDividerVisible, isDividerVisible);
                hasGap = a.getBoolean(R.styleable.DrawerMenuLayout_hasGap, hasGap);
                gapHeight = a.getDimensionPixelSize(R.styleable.DrawerMenuLayout_gapHeight, gapHeight);
                mPaddingLeft = a.getDimensionPixelSize(R.styleable.DrawerMenuLayout_android_paddingLeft, mPaddingLeft);
                mPaddingTop = a.getDimensionPixelSize(R.styleable.DrawerMenuLayout_android_paddingTop, (int) dpTopx(context, mPaddingTop));
                mPaddingRight = a.getDimensionPixelSize(R.styleable.DrawerMenuLayout_android_paddingRight, mPaddingRight);
                mPaddingBottom = a.getDimensionPixelSize(R.styleable.DrawerMenuLayout_android_paddingBottom, mPaddingBottom);
                indent = a.getDimensionPixelSize(R.styleable.DrawerMenuLayout_indent, indent);

            } finally {
                a.recycle();
            }
        }

        paddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.menu_padding_left),
                getResources().getDisplayMetrics());
        dividerColor = getResources().getColor(R.color.menu_divider);
    }

    @Override
    public void onSubMenuClick(MenuView menu) {
        // just deliver unclickable menu to user
        Trace.e("hyman_menu", "enter onSubMenuClick");
        if (menu instanceof UnExpandableMenu) {
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener.onMenuClick(menu);
            }
        }
        Trace.e("hyman_menu", "third menu clicked before");
        if (selectedSubMenu != null &&
                selectedSubMenu instanceof ExpandableMenu &&
                ((ExpandableMenu) selectedSubMenu).containsMenu(menu)) {
            // add third menu
            Trace.e("hyman_menu", "third menu clicked");
            if (selectedThirdMenu == null || selectedThirdMenu != menu) {
                if (selectedThirdMenu != null) {
                    selectedThirdMenu.changeColor(false);
                    // third menu cannot be ExpandableMenu
                    /*if (selectedThirdMenu instanceof ExpandableMenu) {
                        ((ExpandableMenu) selectedThirdMenu).collapse(MenuView.mDefaultAnimExecutor);
                    }*/
                }
                selectedThirdMenu = menu;
                selectedThirdMenu.changeColor(true);
            }
            return;
        }

        Trace.e("hyman_menu", "third menu clicked after");

        // 1. second menu is unclickable
        // 2. second menu is clickable
        if (selectedSubMenu == null || selectedSubMenu != menu) {
            if (selectedSubMenu != null) {
                selectedSubMenu.changeColor(false);
                if (selectedSubMenu instanceof ExpandableMenu) {
                    ((ExpandableMenu) selectedSubMenu).collapse(MenuView.mDefaultAnimExecutor);
                }
            }
            selectedSubMenu = menu;
            selectedSubMenu.changeColor(true);
            if (selectedThirdMenu != null) {
                selectedThirdMenu.changeColor(false);
                selectedThirdMenu = null;
            }
        }

    }


    public void addMenu(final MenuView menu) {
        if (menu == null) return;
        if (menuSet == null) {
            menuSet = new HashSet<>();
        }

        if (menuSet.add(menu)) {
            addView(menu);
            if (menu instanceof ExpandableMenu) {
                ((ExpandableMenu) menu).setOnSubMenuClickListener(DrawerMenuLayout.this);
            }
            if (menu.getChildCount() == 0) return;

            View menuContent = menu.getMenuContent();
            menuContent.setClickable(true);
            menuContent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (selectedMenu == null || selectedMenu != menu) {
                        if (selectedMenu != null) {
                            selectedMenu.changeColor(false);
                            if (selectedMenu instanceof ExpandableMenu) {
                                ((ExpandableMenu) selectedMenu).collapse(MenuView.mDefaultAnimExecutor);
                            }
                        }
                        selectedMenu = menu;
                        selectedMenu.changeColor(true);
                        // reset selectedSubMenu and selectedThirdMenu
                        if (selectedSubMenu != null) {
                            selectedSubMenu.changeColor(false);
                            selectedSubMenu = null;
                        }
                        if (selectedThirdMenu != null) {
                            selectedThirdMenu.changeColor(false);
                            selectedThirdMenu = null;
                        }

                    }

                    if (v.getParent() instanceof UnExpandableMenu) {
                        if (mOnMenuClickListener != null) {
                            mOnMenuClickListener.onMenuClick(menu);
                        }
                    } else {
                        ExpandableMenu expandableMenu = (ExpandableMenu) v.getParent();
                        if (expandableMenu.isExpand()) {
                            Log.e("hyman_menu", "expandableMenu collpase");
                            expandableMenu.collapse(expandableMenu.getDefaultAnimExecutor());
                        } else {
                            expandableMenu.expand(expandableMenu.getDefaultAnimExecutor());
                        }
                    }
                }
            });
            // add divider
            if (getChildCount() != 0) {
                addDivider();
            }
            requestLayout();
        }
    }

    private void addDivider() {
        View dividerLine = new View(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
        layoutParams.leftMargin = paddingLeft;
        dividerLine.setLayoutParams(layoutParams);
        dividerLine.setBackgroundColor(dividerColor);
        if (!isDividerVisible) {
            dividerLine.setVisibility(GONE);
        }
        addView(dividerLine);
        dividers.put(dividerNum++, new WeakReference<View>(dividerLine));
    }

    public void addMenus(MenuView... menus) {
        if (menus == null) return;
        for (int i = 0; i < menus.length; i++) {
            addMenu(menus[i]);
        }
    }

    public void setOnMenuClickListener(OnMenuClickListener mOnMenuClickListener) {
        this.mOnMenuClickListener = mOnMenuClickListener;
    }

    public void setAnimationExecutor(MenuView.IAnimationExecutor animationExecutor) {
        MenuView.mDefaultAnimExecutor = animationExecutor;
    }

    public void setHasDivider(boolean hasDivider) {
        if (!(hasDivider && isDividerVisible)) {
            if (dividers != null) {
                for (int i = 0; i < dividerNum; i++) {
                    View view = dividers.valueAt(i).get();
                    if (view != null) {
                        view.setVisibility(VISIBLE);
                        if (!hasDivider) {
                            view.setVisibility(GONE);
                        }
                    }
                }
            }
            isDividerVisible = hasDivider;
            invalidate();
        }

        Iterator<MenuView> iterator = menuSet.iterator();
        while (iterator.hasNext()) {
            MenuView menu = iterator.next();
            if (menu instanceof ExpandableMenu) {
                ((ExpandableMenu) menu).changeDividerVisibility(hasDivider);
            }
        }
    }

    public void setMenuHeigth(int height) {
        if (menuSet != null && menuSet.size() > 0) {
            for (MenuView menu : menuSet) {
                if (menu != null) {
                    menu.changeMenuContentHeight(height);
                }
            }
        }
    }

    private float dpTopx(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * method for create Menus in DrawerMenuLayout
     *
     * @param menuBeans <p>
     *                  A List of {@link TopMenu}, in each TopMenu contains a List of
     *                  {@link SecMenu} as sub menu of each topmenu
     *                  and each SecMenu contains a List of {@link BaseMenu} as sub menus,
     *                  the BaseMenu has no sub menu!
     *                  </p>
     */
    public void createMenus(List<TopMenu> menuBeans) {
        if (menuBeans != null && menuBeans.size() != 0) {
            // create top menu
            for (TopMenu topMenu : menuBeans) {
                MenuView menuView = null;
                if (topMenu.subMenus != null && topMenu.subMenus.size() != 0) {
                    menuView = new ExpandableMenu(context);
                    // create sec menu
                    for (SecMenu secMenu : topMenu.subMenus) {
                        MenuView secMenuView = null;
                        if (secMenu.subMenus != null && secMenu.subMenus.size() != 0) {
                            secMenuView = new ExpandableMenu(context);
                            // create third menu
                            for (BaseMenu baseMenu : secMenu.subMenus) {
                                UnExpandableMenu baseMenuView = new UnExpandableMenu(context);
                                baseMenuView.setTitle(baseMenu.title);
                                baseMenuView.setLeftIcon(baseMenu.leftIcon);
                                baseMenuView.setRightIcon(baseMenu.rightIcon);
                                // set tag
                                baseMenuView.setTag(baseMenu.getTag());
                                ((ExpandableMenu) secMenuView).addSubMenus(secMenu.subMenus.size(), baseMenuView);
                            }
                        } else {
                            secMenuView = new UnExpandableMenu(context);
                        }
                        secMenuView.setTitle(secMenu.title);
                        secMenuView.setLeftIcon(secMenu.leftIcon);
                        secMenuView.setRightIcon(secMenu.rightIcon);
                        secMenuView.setTag(secMenu.getTag());
                        ((ExpandableMenu) menuView).addSubMenus(topMenu.subMenus.size(), secMenuView);
                    }
                } else {
                    menuView = new UnExpandableMenu(context);
                }
                menuView.setTitle(topMenu.title);
                menuView.setLeftIcon(topMenu.leftIcon);
                menuView.setRightIcon(topMenu.rightIcon);
                menuView.setTag(topMenu.getTag());
                addMenus(menuView);
            }

            setHasDivider(isDividerVisible);
            setMenuHeigth(menuHeight);
            requestLayout();
        }
    }

    /**
     * set the initial selected menu
     * @param menu
     */
    public void setInitialSelectedMenu(MenuView menu) {
        if (menu != null &&
                menuSet.contains(menu) &&
                menu instanceof UnExpandableMenu) {
            selectedMenu = menu;
            selectedMenu.changeColor(true);
        }
    }


}
