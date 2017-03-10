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
import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.fragments.HomeFragment;
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
    private FrameLayout _fabFrame;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (ArcMenu) findViewById(arcMenu);
        _fabFrame = (FrameLayout) findViewById(R.id.frame_fab);
        setupFabListener();

        // Setup FAB buttons
        _bgMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item1_bg);
        _insMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item2_insulin);
        _carbsMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item3_carbs);
        _exerMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item4_exercise);
        setupFabMenuItemsListeners();

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.txt_header_username);
        txtWebsite = (TextView) navHeader.findViewById(R.id.txt_header_email);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_header_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        _userManager = new UserManager(this);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        txtName.setText(_userManager.getUsername());
        txtWebsite.setText(_userManager.getEmail());
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, close the navigation drawer
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
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
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
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            /*case 1:
                // photos
                LogbookFragment photosFragment = new PhotosFragment();
                return photosFragment;
            case 2:
                // movies fragment
                ChartsFragment moviesFragment = new MoviesFragment();
                return moviesFragment;
            case 3:
                // settings fragment
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
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;/*
                    case R.id.nav_logbook:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_Logbook;
                        break;
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
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_logbook) {
            Intent logbookActivity = new Intent(MainActivity.this, LogbookActivity.class);
            startActivity(logbookActivity);
        } else if (id == R.id.nav_charts) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent loginActivity = new Intent(this, LoginActivity.class);
            loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginActivity);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupFabListener() {
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
            }
        });
    }

    public void setupFabMenuItemsListeners() {
        _bgMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBGReadingActivity = new Intent(MainActivity.this, AddBGReadingActivity.class);
                startActivity(addBGReadingActivity);
            }
        });

        _insMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addInsulinActivity = new Intent(MainActivity.this, AddInsulinActivity.class);
                startActivity(addInsulinActivity);
            }
        });

        _carbsMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addCarbsActivity = new Intent(MainActivity.this, AddFoodLogActivity.class);
                startActivity(addCarbsActivity);
            }
        });

        _exerMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}
