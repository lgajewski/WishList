package pl.edu.agh.io.wishlist.android.ui.drawer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import pl.edu.agh.io.wishlist.android.R;
import pl.edu.agh.io.wishlist.android.activity.LoginActivity;
import pl.edu.agh.io.wishlist.android.fragment.FragmentHandler;

import javax.inject.Inject;


@SuppressWarnings("WeakerAccess")
public class Drawer implements NavigationView.OnNavigationItemSelectedListener {

    private final AppCompatActivity activity;
    private final FragmentHandler fragmentHandler;

    private CharSequence activityTitle;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    private DrawerToggle drawerToggle;

    @Inject
    public Drawer(AppCompatActivity activity, FragmentHandler fragmentHandler) {
        this.activity = activity;
        this.fragmentHandler = fragmentHandler;

        // ButterKnife
        ButterKnife.bind(this, activity);

        init();
    }

    private void init() {
        this.activityTitle = activity.getTitle();
        this.activity.setSupportActionBar(toolbar);

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = new DrawerToggle(
                activity,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        drawerLayout.addDrawerListener(drawerToggle);

        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void syncState() {
        // set initial check
        MenuItem menuItem = navigationView.getMenu().getItem(0);
        menuItem.setChecked(true);
        onNavigationItemSelected(menuItem);

        drawerToggle.syncState();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.e("SELECTED", "item: " + item.getTitle());
        // update selected item and activityTitle, then close the drawer
        setDrawerTitle(item.getTitle());

        switch (item.getItemId()) {
            case R.id.nav_logout:
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
                break;
            default:
                fragmentHandler.updateContent(item);
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    /* Toggle for a navigation drawer, triggers activityTitle changes */
    private class DrawerToggle extends ActionBarDrawerToggle {

        public DrawerToggle(Activity activity, DrawerLayout drawerLayout,
                            Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        public void onDrawerClosed(View view) {
            toolbar.setTitle(activity.getTitle());
            activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        public void onDrawerOpened(View drawerView) {
            toolbar.setTitle(activityTitle);
            activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }
    }

    public void setDrawerTitle(CharSequence drawerTitle) {
        this.activity.setTitle(drawerTitle);
        this.toolbar.setTitle(drawerTitle);
    }

}
