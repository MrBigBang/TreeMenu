package com.hyman.treemenudemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.hyman.treemenu.DrawerMenuLayout;
import com.hyman.treemenu.MenuView;
import com.hyman.treemenu.pojo.BaseMenu;
import com.hyman.treemenu.pojo.SecMenu;
import com.hyman.treemenu.pojo.TopMenu;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hyman on 16/5/11.
 */
public class MenuInScrollViewFragment extends Fragment implements DrawerMenuLayout.OnMenuClickListener {

    private static final int MENU_FIRST = 1;
    private static final int MENU_SECOND = 2;
    private static final int MENU_THIRD = 3;
    private static final int MENU_FORTH = 4;
    private static final int MENU_FIVE = 5;
    private static final int MENU_SIX = 6;
    private static final int MENU_SEVEN = 7;
    private static final int MENU_EIGHT = 8;
    private static final int MENU_NINE = 9;
    private static final int MENU_TEN = 10;
    private static final int MENU_ELEVEN = 11;
    private static final int MENU_TWELVE = 12;
    private static final int MENU_THIRTEEN = 13;
    private static final int MENU_FOURTEEN = 14;
    private static final int MENU_FIFTEEN = 15;

    public static MenuInScrollViewFragment newInstance() {
        MenuInScrollViewFragment fragment = new MenuInScrollViewFragment();
        // set bundle args
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get bundle values
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout contentView = new LinearLayout(getContext());
        contentView.setLayoutParams(lp);
        contentView.setOrientation(LinearLayout.VERTICAL);
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        scrollView.isSmoothScrollingEnabled();
        DrawerMenuLayout drawerMenuLayout = new DrawerMenuLayout(getContext());
        drawerMenuLayout.setOnMenuClickListener(this);
        drawerMenuLayout.createMenus(initMenuList());
        // default dividers is unvisible
//        drawerMenuLayout.setHasDivider(true);
        scrollView.addView(drawerMenuLayout);
        contentView.addView(scrollView);
        return contentView;
    }

    private List<TopMenu> initMenuList() {
        List<TopMenu> topMenuList = new LinkedList<>();
        // add top menu1
        TopMenu topMenu = new TopMenu("Top Title1");
        topMenu.setTag(MENU_FIRST);
        topMenuList.add(topMenu);

        //add top menu2
        // create sec submenus
        List<SecMenu> secMenuList = new LinkedList<>();
        SecMenu secMenu1 = new SecMenu("Sec Title1");
        secMenu1.setTag(MENU_SECOND);
        secMenuList.add(secMenu1);

        // create third submenus
        List<BaseMenu> baseMenuList = new LinkedList<>();
        BaseMenu baseMenu1 = new BaseMenu("Base Title1");
        baseMenu1.setTag(MENU_THIRD);
        BaseMenu baseMenu2 = new BaseMenu("Base Title2");
        baseMenu2.setTag(MENU_FORTH);
        baseMenuList.add(baseMenu1);
        baseMenuList.add(baseMenu2);
        SecMenu secMenu2 = new SecMenu("Sec Title2", baseMenuList);
        secMenuList.add(secMenu2);

        SecMenu secMenu3 = new SecMenu("Sec Title3");
        secMenu3.setTag(MENU_FIVE);
        secMenuList.add(secMenu3);
        TopMenu topMenu1 = new TopMenu("Top Title2", secMenuList);
        topMenuList.add(topMenu1);


        // top menu3
        List<SecMenu> secMenuList1 = new LinkedList<>();
        SecMenu secMenu4 = new SecMenu("Sec Title4");
        secMenu4.setTag(MENU_SIX);
        secMenuList1.add(secMenu4);
        SecMenu secMenu5 = new SecMenu("Sec Title5");
        secMenu4.setTag(MENU_SEVEN);
        secMenuList1.add(secMenu5);
        SecMenu secMenu6 = new SecMenu("Sec Title4");
        secMenu4.setTag(MENU_EIGHT);
        secMenuList1.add(secMenu6);
        TopMenu topMenu2 = new TopMenu("Top Title3", secMenuList);
        topMenuList.add(topMenu2);


        // top menu4
        TopMenu topMenu3 = new TopMenu("Top Title4");
        topMenu3.setTag(MENU_NINE);
        topMenuList.add(topMenu3);
        // top menu5
        TopMenu topMenu4 = new TopMenu("Top Title5");
        topMenu4.setTag(MENU_TEN);
        topMenuList.add(topMenu4);
        // top menu6
        TopMenu topMenu5 = new TopMenu("Top Title6");
        topMenu5.setTag(MENU_ELEVEN);
        topMenuList.add(topMenu5);
        // top menu7
        TopMenu topMenu6 = new TopMenu("Top Title7");
        topMenu6.setTag(MENU_TWELVE);
        topMenuList.add(topMenu6);
        // top menu8
        TopMenu topMenu7 = new TopMenu("Top Title8");
        topMenu7.setTag(MENU_THIRTEEN);
        topMenuList.add(topMenu7);
        // top menu9
        TopMenu topMenu8 = new TopMenu("Top Title9");
        topMenu8.setTag(MENU_FOURTEEN);
        topMenuList.add(topMenu8);
        // top menu10
        TopMenu topMenu9 = new TopMenu("Top Title10");
        topMenu9.setTag(MENU_FIFTEEN);
        topMenuList.add(topMenu9);

        return topMenuList;
    }

    @Override
    public void onMenuClick(MenuView menu) {
        showToast("menu \"" + menu.getTitle() + "\" clicked!");
        // do custom things
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
