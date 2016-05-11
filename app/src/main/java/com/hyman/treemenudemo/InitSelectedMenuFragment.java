package com.hyman.treemenudemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyman.treemenu.DrawerMenuLayout;
import com.hyman.treemenu.ExpandableMenu;
import com.hyman.treemenu.MenuView;
import com.hyman.treemenu.UnExpandableMenu;

/**
 * Created by hyman on 16/5/11.
 */
public class InitSelectedMenuFragment extends Fragment implements DrawerMenuLayout.OnMenuClickListener {

    private static final int MENU_ONE= 1;
    private static final int MENU_TWO = 2;
    private static final int MENU_THREE = 3;
    private static final int MENU_FOUR = 4;
    private static final int MENU_FIVE = 5;
    private static final int MENU_SIX = 6;
    private static final int MENU_SEVEN = 7;

    public static InitSelectedMenuFragment newInstance() {
        InitSelectedMenuFragment fragment = new InitSelectedMenuFragment();
        //
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // create menu layout in this way u can easily set the initially selected menu,
        // but witch can only be a TopMenu and have no sub menus(UnExpandableMenu)
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout contentView = new LinearLayout(getContext());
        contentView.setLayoutParams(lp);
        contentView.setOrientation(LinearLayout.VERTICAL);

        // create MenuViews
        UnExpandableMenu topMenu1 = new UnExpandableMenu(getContext());
        // by this way set tag to each MenuView
        topMenu1.setTag(MENU_ONE);
        UnExpandableMenu topMenu2 = new UnExpandableMenu(getContext());
        topMenu1.setTag(MENU_TWO);

        ExpandableMenu topMenu3 = new ExpandableMenu(getContext());
        UnExpandableMenu secMenu1 = new UnExpandableMenu(getContext());
        secMenu1.setTag(MENU_THREE);
        UnExpandableMenu secMenu2 = new UnExpandableMenu(getContext());
        secMenu2.setTag(MENU_FOUR);
        ExpandableMenu secMenu3 = new ExpandableMenu(getContext());
        UnExpandableMenu thirdMenu1 = new UnExpandableMenu(getContext());
        thirdMenu1.setTag(MENU_FIVE);
        UnExpandableMenu thirdMenu2 = new UnExpandableMenu(getContext());
        thirdMenu2.setTag(MENU_SIX);
        secMenu3.addSubMenus(2, thirdMenu1, thirdMenu2);
        topMenu3.addSubMenus(3, secMenu1, secMenu2, secMenu3);

        UnExpandableMenu topMenu4 = new UnExpandableMenu(getContext());
        topMenu4.setTag(MENU_SEVEN);
        DrawerMenuLayout drawerMenuLayout = new DrawerMenuLayout(getContext());
        drawerMenuLayout.setOnMenuClickListener(this);
        drawerMenuLayout.addMenus(topMenu1, topMenu2, topMenu3, topMenu4);
        // init selected menu
        drawerMenuLayout.setInitialSelectedMenu(topMenu1);
//        drawerMenuLayout.setHasDivider(true);

        contentView.addView(drawerMenuLayout);
        return contentView;
    }


    @Override
    public void onMenuClick(MenuView menu) {
        Toast.makeText(getContext(), "menu \"" + menu.getTitle() + "\" clicked!", Toast.LENGTH_SHORT).show();
    }
}
