package com.example.alex.glucosecoach.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.TokenManager;
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.fragments.HomeFragment;
import com.example.alex.glucosecoach.fragments.LogbookFragment;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;

import static com.example.alex.glucosecoach.R.id.arcMenu;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private Menu menu;
    private ArcMenu fab;
    private FloatingActionButton _bgMenuItem, _insMenuItem, _carbsMenuItem, _exerMenuItem;


    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_LOGBOOK = "logbook";
    private static final String TAG_CHARTS = "charts";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    UserManager _userManager;
    TokenManager _tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _userManager = new UserManager(this);
        _tokenManager = new TokenManager(this);

        if (!isLoggedIn()) {
            startLoginActivity();
        }

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (ArcMenu) findViewById(arcMenu);
        setupFabListener();

        // setup FAB buttons
        _bgMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item1_bg);
        _insMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item2_insulin);
        _carbsMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item3_carbs);
        _exerMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item4_exercise);
        setupFabMenuItemsListeners();

        // navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.txt_header_username);
        txtWebsite = (TextView) navHeader.findViewById(R.id.txt_header_email);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_header_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data
        loadNavHeader();

        // initialize navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleFab();

        if (!isLoggedIn()) {
            startLoginActivity();
        }
    }

    /***
     * Load navigation menu header information
     */
    private void loadNavHeader() {
        txtName.setText(_userManager.getUsername());
        txtWebsite.setText(_userManager.getEmail());
    }

    /***
     * Returns selected fragment from navigation menu
     */
    private void loadHomeFragment() {
        // select appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // close the navigation drawer if fragment selected again
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // load fragment in separate thread
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        mHandler.post(mPendingRunnable);

        // show or hide the fab button
        toggleFab();

        // close drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                LogbookFragment logbookFragment = new LogbookFragment();
                return logbookFragment;
            /*case 2:
                ChartsFragment chartsFragment = new ChartsFragment();
                return chartsFragment;
            case 3:
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;*/
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        // handle item click on navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                // handle nav item click
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_logbook:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_LOGBOOK;
                        break;/*
                    case R.id.nav_charts:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_Charts;
                        break;
                    case R.id.settings:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;*/
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // drawer close
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // drawer open
                super.onDrawerOpened(drawerView);
            }
        };

        // set the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        // calling sync state to update hamburger icon
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // loads home fragment when back key is pressed when user is in other fragment
        if (shouldLoadHomeFragOnBackPress) {
            // check if user is on other navigation menu
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle action bar item clicks
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            _tokenManager.clearToken();
            startLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupFabListener() {
        final FrameLayout _fabFrame = (FrameLayout) findViewById(R.id.frame_fab);

        fab.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                _fabFrame.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.transparentGray));
                _fabFrame.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (fab.isMenuOpened()) {
                            fab.toggleMenu();
                        }

                        return true;
                    }
                });
            }

            @Override
            public void onMenuClosed() {
                _fabFrame.setBackgroundColor(Color.TRANSPARENT);
                _fabFrame.setOnTouchListener(null);
            }
        });
    }

    public void setupFabMenuItemsListeners() {
        _bgMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
                Intent addBGReadingActivity = new Intent(MainActivity.this, AddBGReadingActivity.class);
                startActivity(addBGReadingActivity);
            }
        });

        _insMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
                Intent addInsulinActivity = new Intent(MainActivity.this, AddInsulinActivity.class);
                startActivity(addInsulinActivity);
            }
        });

        _carbsMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
                Intent addCarbsActivity = new Intent(MainActivity.this, AddFoodLogActivity.class);
                startActivity(addCarbsActivity);
            }
        });

        _exerMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
                Intent addExerciseActivity = new Intent(MainActivity.this, AddExerciseLogActivity.class);
                startActivity(addExerciseActivity);
            }
        });
    }

    // show or hide the fab
    private void toggleFab() {
        if (fab.isMenuOpened()) {
            fab.toggleMenu();
        }
    }

    public boolean isLoggedIn() {
        return _tokenManager.hasToken();
    }

    public void startLoginActivity() {
        Intent loginActivity = new Intent(this, LoginActivity.class);
        loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginActivity);
    }
}
