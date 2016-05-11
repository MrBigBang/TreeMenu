package com.hyman.treemenu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyman.treemenu.utils.Trace;

/**
 * Created by hyman on 16/5/7.
 * {@link FrameLayout} will pass touch event to it's first child, in order to
 * response to click listener, can get the first child replace FrameLayout, or
 * intercept touch event when there is a click listener registered on this FrameLayout
 */
public abstract class MenuView extends FrameLayout {

    public interface IAnimationExecutor {
        // this anim to control submenu show or hide
        void executeAnim(View target, int h, int animType);

        // a rotation anim for the right icon of an ExpandableMenu
        void executeRightIconAnim(View target, int animType);
    }

    static IAnimationExecutor mDefaultAnimExecutor = new IAnimationExecutor() {
        @Override
        public void executeAnim(final View target, final int h, final int animType) {


            if (animType == ExpandableMenu.STATUS_EXPANDED && target.getVisibility() == VISIBLE)
                return;
            if (animType == ExpandableMenu.STATUS_COLLAPSED && target.getVisibility() == GONE)
                return;

            final float animStartY = animType == ExpandableMenu.STATUS_EXPANDED ? 0 : h;
            final float animEndY = animType == ExpandableMenu.STATUS_COLLAPSED ? h : 0;
            final ViewGroup.LayoutParams lp = target.getLayoutParams();
            ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
            animator.setDuration(200);
            target.setVisibility(VISIBLE);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (animType == ExpandableMenu.STATUS_EXPANDED) {
                        target.setVisibility(VISIBLE);
                    } else {
                        target.setVisibility(GONE);
                    }
                    target.getLayoutParams().height = h;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.setInterpolator(new LinearInterpolator());
            // if set no interpolator, the value get in updateListener will always be zero
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    lp.height = (int) (animation.getAnimatedFraction() * h);
                    target.setLayoutParams(lp);
                    Trace.e("hyman_menu", "menu height: " + target.getHeight());
                    target.requestLayout();
                }
            });

            animator.start();
        }

        @Override
        public void executeRightIconAnim(final View target, int animType) {
            Trace.e("hyman_menu", "executeRightIconAnim executed" + animType);
            int startAngle = animType == ExpandableMenu.STATUS_COLLAPSED ? 90 : 0;
            final int endAngle = animType == ExpandableMenu.STATUS_COLLAPSED ? 0 : 90;
            ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", startAngle, endAngle);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(200);
            animator.start();
        }
    };

    LinearLayout menuContent;
    //    int selectedMenuBgColor;
    int selectedMenuTextColor;

    // views
    TextView tvTitle;
    //    TextView tvSubTitle;
    ImageView ivLeft;
    ImageView ivRight;

    // value
    String title;
    // String subTitle;
    Drawable leftIcon;
    Drawable rightIcon;

    boolean hasLeftIcon = true;
    boolean hasRightIcon = false;
    int defaultTextColor;
    int topMenuSelectedColor;

    // menu height
    protected int menuContentHeight;
    protected boolean isDividerVisible = false;
    public MenuView(Context context) {
        this(context, null);
    }

    public MenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        menuContentHeight = menuContentHeight != 0? menuContentHeight : (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.menu_height),
                getResources().getDisplayMetrics());
        defaultTextColor = getResources().getColor(R.color.title_color);
        topMenuSelectedColor = getResources().getColor(R.color.top_menu_selected_bg);

    }

    protected void addMenuContent() {
        menuContent = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.menu_layout, null);
        ivLeft = (ImageView) menuContent.findViewById(R.id.leftImageView);
        ivRight = (ImageView) menuContent.findViewById(R.id.rightImageView);
        tvTitle = (TextView) menuContent.findViewById(R.id.titleTextView);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, menuContentHeight);
        menuContent.setLayoutParams(lp);
        addView(menuContent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureMenuLayout(widthMeasureSpec, heightMeasureSpec);
    }

    protected abstract void measureMenuLayout(int widthMeasureSpec, int heightMeasureSpec);

    public LinearLayout getMenuContent() {
        return menuContent;
    }

    public IAnimationExecutor getDefaultAnimExecutor() {
        return mDefaultAnimExecutor;
    }

    public void changeColor(boolean selected) {
        if (!selected) {
            tvTitle.setTextColor(defaultTextColor);
        } else {
            tvTitle.setTextColor(Color.WHITE);
        }
        menuContent.setSelected(selected);
        invalidate();
    }

    public View getRightIcon() {
        return ivRight;
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) return;
        this.title = title;
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        invalidate();
    }

    public void setLeftIcon(Drawable leftIcon) {
        if (leftIcon == null) return;
        this.leftIcon = leftIcon;
        if (ivLeft != null) {
            ivLeft.setImageDrawable(leftIcon);
        }
        invalidate();
    }

    public void setRightIcon(Drawable rightIcon) {
        if (rightIcon == null) return;
        this.rightIcon = rightIcon;
        if (ivRight != null) {
            ivRight.setImageDrawable(rightIcon);
        }
        invalidate();
    }

    public String getTitle() {
        return tvTitle == null ? "" : tvTitle.getText().toString();
    }

    public void changeMenuContentHeight(int height) {
        ViewGroup.LayoutParams lp = menuContent.getLayoutParams();
        lp.height = height;
        menuContent.setLayoutParams(lp);
        this.menuContentHeight = height;
        requestLayout();
    }

}
