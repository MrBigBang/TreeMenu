package com.hyman.treemenudemo;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.TextAppearanceSpan;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by hyman on 16/5/11.
 */
public class AboutMeFragment extends Fragment {

    public static AboutMeFragment newInstance() {
        AboutMeFragment fragment = new AboutMeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.textColorPrimary, android.R.attr.subtitleTextStyle});
        int textColor = array.getColor(0, 0xff00ddff);
        int textSize = array.getDimensionPixelOffset(1, 16);
        array.recycle();

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        FrameLayout contentView = new FrameLayout(getContext());
        contentView.setLayoutParams(lp);
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout ll = new LinearLayout(getContext());
        lp2.gravity = Gravity.CENTER;
        ll.setLayoutParams(lp2);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tvName = new TextView(getContext());
        tvName.setText("Hyman Lee" + Html.fromHtml("<br/>"));
        tvName.setTextSize(textSize);
        tvName.setTypeface(null, Typeface.BOLD);
        ll.addView(tvName);

        TextView tvEmail = new TextView(getContext());
        tvEmail.setText("Email: hyman.dev@gmail.com" + Html.fromHtml("<br/>"));
        tvEmail.setTypeface(null, Typeface.BOLD);
        Linkify.addLinks(tvEmail, Linkify.EMAIL_ADDRESSES);
        ll.addView(tvEmail);

        TextView tvGithub = new TextView(getContext());
        tvGithub.setText(Html.fromHtml("MrBigBang: " + "<a href=\"https://github.com/MrBigBang\">https://github.com/MrBigBang</a><br/>"));
        tvGithub.setTypeface(null, Typeface.BOLD);
        tvGithub.setMovementMethod(LinkMovementMethod.getInstance());
        ll.addView(tvGithub);

        TextView tvBlog = new TextView(getContext());
        tvBlog.setText(Html.fromHtml("Blog: " + "<a href=\"http://www.cnblogs.com/page-of-Hyman/\">http://www.cnblogs.com/page-of-Hyman/</a><br/>"));
        tvBlog.setMovementMethod(LinkMovementMethod.getInstance());
        tvBlog.setTypeface(null, Typeface.BOLD);
        ll.addView(tvBlog);

        contentView.addView(ll);
        return contentView;
    }
}
