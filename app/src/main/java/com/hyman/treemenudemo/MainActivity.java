package com.hyman.treemenudemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyman.treemenu.DrawerMenuLayout;
import com.hyman.treemenu.MenuView;
import com.hyman.treemenu.pojo.TopMenu;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DrawerMenuLayout.OnMenuClickListener{

    private static final int MENU_TREEMENU_IN_SCROLLVIEW = 1;
    private static final int MENU_INIT_SELECTED_MENU = 2;
    private static final int MENU_ABOUT_ME = 3;


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerMenuLayout drawerMenuLayout;
    private Context context;
    private FragmentManager mFragmentManager;
    private FrameLayout container;
    private Fragment lastFragment;

    private Map<String, Fragment> fragmentMap = new HashMap<>();
    private TextView demoNemeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = MainActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        drawerMenuLayout = (DrawerMenuLayout) findViewById(R.id.drawerMenuLayout);
        initMenuLayout();

        // get container
        container = (FrameLayout) findViewById(R.id.container);
        demoNemeTextView = (TextView)findViewById(R.id.demoNemeTextView);

        initFragments();
    }

    private void initFragments() {
        fragmentMap.put(MenuInScrollViewFragment.class.getSimpleName(), MenuInScrollViewFragment.newInstance());
        fragmentMap.put(InitSelectedMenuFragment.class.getSimpleName(), InitSelectedMenuFragment.newInstance());
        fragmentMap.put(AboutMeFragment.class.getSimpleName(), AboutMeFragment.newInstance());

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : fragmentMap.values()) {
            mTransaction.add(R.id.container, fragment);
            mTransaction.hide(fragment);
        }
        mTransaction.commit();
    }

    private void initMenuLayout() {
        Drawable item1Icon = getResources().getDrawable(R.drawable.item1);
        Drawable profileIcon = getResources().getDrawable(R.drawable.profile);
        Drawable item2Icon = getResources().getDrawable(R.drawable.item2);
        List<TopMenu> topMenuList = new LinkedList<>();
        TopMenu topMenu = new TopMenu("TreeMenu in ScrollView", item1Icon, null);
        topMenu.setTag(MENU_TREEMENU_IN_SCROLLVIEW);
        topMenuList.add(topMenu);

        TopMenu topMenu2 = new TopMenu("Init Selected Menu", item2Icon, null);
        topMenu2.setTag(MENU_INIT_SELECTED_MENU);
        topMenuList.add(topMenu2);

        TopMenu topMenu3 = new TopMenu("About Me", profileIcon, null);
        topMenu3.setTag(MENU_ABOUT_ME);
        topMenuList.add(topMenu3);


        /*List<SecMenu> secMenuList = new LinkedList<>();
        SecMenu secMenu1 = new SecMenu("Sec Title1");
        secMenu1.setTag(MENU_SECOND);
        secMenuList.add(secMenu1);

        *//*List<BaseMenu> baseMenuList = new LinkedList<>();
        BaseMenu baseMenu1 = new BaseMenu("Base Title1");
        baseMenu1.setTag(MENU_THIRD);
        BaseMenu baseMenu2 = new BaseMenu("Base Title2");
        baseMenu2.setTag(MENU_FORTH);
        baseMenuList.add(baseMenu1);
        baseMenuList.add(baseMenu2);
        SecMenu secMenu2 = new SecMenu("Sec Title2", baseMenuList);
        secMenuList.add(secMenu2);*//*

        SecMenu secMenu3 = new SecMenu("Sec Title3");
        secMenu3.setTag(MENU_FIVE);
        secMenuList.add(secMenu3);
        TopMenu topMenu1 = new TopMenu("Top Title2", configIcon, null, secMenuList);
        topMenuList.add(topMenu1);*/

        drawerMenuLayout.createMenus(topMenuList);
        drawerMenuLayout.setOnMenuClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            actionBarDrawerToggle.syncState();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuClick(MenuView menu) {
        int tag = (int)menu.getTag();
        showToast("menu \"" + menu.getTitle() + "\" touched");
        demoNemeTextView.setVisibility(View.GONE);
        switch (tag) {
            case MENU_TREEMENU_IN_SCROLLVIEW:
                // show fragment
                Fragment fragment = fragmentMap.get(MenuInScrollViewFragment.class.getSimpleName());
                showFragment(fragment);
                break;
            case MENU_INIT_SELECTED_MENU:
                showFragment(fragmentMap.get(InitSelectedMenuFragment.class.getSimpleName()));
                break;
            case MENU_ABOUT_ME:
                showFragment(fragmentMap.get(AboutMeFragment.class.getSimpleName()));
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void showFragment(Fragment fragment) {
        if (lastFragment == null || lastFragment != fragment) {
            FragmentTransaction _transaction = mFragmentManager.beginTransaction();
            if (lastFragment != null) {
                _transaction.hide(lastFragment);
            }
            _transaction.show(fragment).commit();
            lastFragment = fragment;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
