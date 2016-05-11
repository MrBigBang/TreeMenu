package com.hyman.treemenu;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hyman.treemenu.utils.Trace;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by hyman on 16/5/6.
 */
public class ExpandableMenu extends MenuView {

    private final int subMenuIndent;
    private final int paddingLeft;
    private int containerHeight;

    private int subMenuDividerColor;

    public interface OnSubMenuClickListener {
        void onSubMenuClick(MenuView menu);
    }

    OnSubMenuClickListener mOnSubMenuClickListener;

    /* two status */
    public static final int STATUS_COLLAPSED = 0;
    public static final int STATUS_EXPANDED = 1;

    private final Context context;
    private int currentStatus = STATUS_COLLAPSED;

    private LinearLayout subMenuContainer;
    private Set<MenuView> subMenus = new HashSet<>();
    private SparseArray<WeakReference<View>> dividers = new SparseArray<>();
    private int dividerNum = 0;

    /*  touch handling  */
    private float mDownY;
    private boolean mHeaderOwnsTouch;
    private float mTouchSlop;

    public ExpandableMenu(Context context) {
        this(context, null);
    }

    public ExpandableMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = getContext();

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        subMenuIndent = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.subtitle_indent),
                getResources().getDisplayMetrics());
        paddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.menu_padding_left),
                getResources().getDisplayMetrics());

        subMenuDividerColor = getResources().getColor(R.color.menu_divider);

        initializeView(context);
    }

    @Override
    protected void measureMenuLayout(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
        int parentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int parentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(menuContentHeight, MeasureSpec.EXACTLY);
        measureChild(menuContent, parentWidthMeasureSpec, parentHeightMeasureSpec);
        int parentHeightMeasureSpec1 = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        measureChild(subMenuContainer, parentWidthMeasureSpec, parentHeightMeasureSpec1);

        int height = menuContentHeight;
        if (isExpand()) {
            height += containerHeight;
        }
        setMeasuredDimension(width, height);
    }

    private void initializeView(Context context) {
        subMenuContainer = new LinearLayout(context);
        subMenuContainer.setOrientation(LinearLayout.VERTICAL);
        subMenuContainer.setBackgroundColor(Color.WHITE);
        addView(subMenuContainer);
        subMenuContainer.setPadding(subMenuIndent, menuContentHeight, 0, 0);
        subMenuContainer.setVisibility(View.GONE);

        addMenuContent();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Trace.e("hyman_menu", "dispatchTouchEvent" + subMenuContainer.getChildAt(0).getHeight());
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN) {
            mDownY = ev.getY();
            mHeaderOwnsTouch = mDownY >= 0 && mDownY <= menuContentHeight;
        }
        boolean handled = true;
        if (mHeaderOwnsTouch) {
            if (menuContent != null && Math.abs(mDownY - ev.getY()) <= mTouchSlop) {
                handled = menuContent.dispatchTouchEvent(ev);
            } else {
                if (menuContent != null) {
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    menuContent.dispatchTouchEvent(cancelEvent);
                    cancelEvent.recycle();
                }
            }
        } else {
            handled = subMenuContainer.dispatchTouchEvent(ev);
        }
        return handled;
    }

    public void addSubMenus(int size, MenuView... menus) {
        if (subMenus == null) {
            subMenus = new HashSet<>();
        }
        for (int i = 0; i < menus.length; i++) {
            if (subMenus.add(menus[i])) {
                addToContainer(menus[i]);
                View dividerLine = new View(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
                layoutParams.leftMargin = paddingLeft;
                dividerLine.setLayoutParams(layoutParams);
                dividerLine.setBackgroundColor(subMenuDividerColor);
                if (!isDividerVisible) {
                    dividerLine.setVisibility(GONE);
                }
                subMenuContainer.addView(dividerLine);
                dividers.put(dividerNum++, new WeakReference<View>(dividerLine));
            }
        }
        if (subMenus.size() == size) {
            View lastDividerLine = subMenuContainer.getChildAt(subMenuContainer.getChildCount() - 1);
            subMenuContainer.removeView(lastDividerLine);
            dividers.remove(dividerNum--);
        }

        updateContainerHeight();
        requestLayout();
    }

    // update container height
    public void updateContainerHeight() {
        containerHeight = 0;
        Iterator<MenuView> _iterator = subMenus.iterator();
        while (_iterator.hasNext()) {
            MenuView view = _iterator.next();
            if (view != null) {
//                containerHeight += subMenuHeight;
                containerHeight += menuContentHeight;
                if (isDividerVisible) {
                    containerHeight += 2;
                }
                if ((view instanceof ExpandableMenu) && ((ExpandableMenu) view).isExpand()) {
                    int size = ((ExpandableMenu) view).getSubMenuNum();
//                    containerHeight += size * subMenuHeight;
                    containerHeight += size * menuContentHeight;
                    if (isDividerVisible) {
                        containerHeight += (size - 1) * 2;
                    }
                }
            }
        }
    }

    private void addToContainer(final MenuView menu) {
        // framelayout cannot response to onclick listener,so add to it's child(menuContent)
        subMenuContainer.addView(menu);
        // here onSubMenuClickListener would be null,because its top menu may haven't been add into DrawerMenuLayout
        /*if (menu instanceof ExpandableMenu) {
            ((ExpandableMenu) menu).setOnSubMenuClickListener(this.mOnSubMenuClickListener);
        }*/
        View childView = menu.getMenuContent();
        childView.setClickable(true);
        childView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Trace.e("hyman_menu", "expandable's sub menu clicked:   " + v.toString());
                if (mOnSubMenuClickListener != null) {
                    mOnSubMenuClickListener.onSubMenuClick(menu);
                }
                if (v.getParent() instanceof ExpandableMenu) {
                    ExpandableMenu expandableMenu = (ExpandableMenu) v.getParent();
                    if (expandableMenu.isExpand()) {
                        expandableMenu.collapse(mDefaultAnimExecutor);
                    } else {
                        expandableMenu.expand(mDefaultAnimExecutor);
                    }
                    // update height
                }
                updateContainerHeight();
                ExpandableMenu.this.requestLayout();
            }
        });
        if (isExpand()) {
            menu.setVisibility(VISIBLE);
        } else {
            menu.setVisibility(GONE);
        }
    }

    public void expand(IAnimationExecutor animExecutor) {
        animExecutor.executeRightIconAnim(ivRight, STATUS_EXPANDED);
        subMenuContainer.setVisibility(VISIBLE);
        for (MenuView menu : subMenus) {
            animExecutor.executeAnim(menu, menuContentHeight, STATUS_EXPANDED);
        }
        currentStatus = STATUS_EXPANDED;
        updateContainerHeight();
        requestLayout();
    }

    public void collapse(IAnimationExecutor animExecutor) {
        // collapse sub ExpandableMenu first
        for (MenuView menu : subMenus) {
            if (menu instanceof ExpandableMenu) {
                ((ExpandableMenu) menu).collapse(MenuView.mDefaultAnimExecutor);
            }
        }
        animExecutor.executeRightIconAnim(ivRight, STATUS_COLLAPSED);
        for (MenuView menu : subMenus) {
            animExecutor.executeAnim(menu, menuContentHeight, STATUS_COLLAPSED);
            if (menu instanceof ExpandableMenu) {
                animExecutor.executeRightIconAnim(menu.getRightIcon(), STATUS_COLLAPSED);
            }
        }
        currentStatus = STATUS_COLLAPSED;
        subMenuContainer.setVisibility(GONE);
        updateContainerHeight();
        requestLayout();
    }

    public boolean isExpand() {
        return currentStatus == STATUS_EXPANDED;
    }

    public void setOnSubMenuClickListener(OnSubMenuClickListener mOnSubMenuClickListener) {
        this.mOnSubMenuClickListener = mOnSubMenuClickListener;
        if (subMenus != null) {
            for (MenuView menu : subMenus) {
                if (menu instanceof ExpandableMenu) {
                    ((ExpandableMenu) menu).setOnSubMenuClickListener(mOnSubMenuClickListener);
                }
            }
        }
    }

    public void changeDividerVisibility(boolean visibility) {
        if (!(isDividerVisible && visibility)) {
            if (dividers != null) {
                Trace.e("hyman_menu", "changeDividerVisibility " + dividerNum);
                for (int i = 0; i < dividerNum; i++) {
                    View view = dividers.valueAt(i).get();
                    if (view != null) {
                        view.setVisibility(VISIBLE);
                        if (!visibility) {
                            view.setVisibility(GONE);
                        }
                    }
                }
            }
            isDividerVisible = visibility;
            invalidate();
        }

        // notify its sub menu
        if (subMenus != null && subMenus.size() != 0) {
            for (MenuView subMenu : subMenus) {
                if (subMenu instanceof ExpandableMenu) {
                    ((ExpandableMenu) subMenu).changeDividerVisibility(visibility);
                }
            }
        }
    }

    public int getSubMenuNum() {
        return subMenus != null ? subMenus.size() : 0;
    }

    /**
     * to check if the menu is a sub menu of this ExpandableMenu
     *
     * @param menu
     * @return
     */
    public boolean containsMenu(MenuView menu) {
        return subMenus.contains(menu);
    }

    @Override
    public void changeMenuContentHeight(int height) {
        super.changeMenuContentHeight(height);
        changeSubMenusHeight(height);
    }

    public void changeSubMenusHeight(int height) {
        if (subMenus != null && subMenus.size() > 0) {
            for (MenuView menu : subMenus) {
                menu.changeMenuContentHeight(height);
                if (menu instanceof ExpandableMenu) {
                    ((ExpandableMenu) menu).changeSubMenusHeight(height);
                }
            }
        }
    }

}
