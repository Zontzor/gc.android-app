package com.example.alex.glucosecoach.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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

import com.example.alex.glucosecoach.R;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;

import static com.example.alex.glucosecoach.R.id.arcMenu;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArcMenu _arcMenu;
    private FloatingActionButton _bgMenuItem, _insMenuItem, _carbsMenuItem, _exerMenuItem;
    private FrameLayout _fabFrame;

    final Context _context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _arcMenu = (ArcMenu) findViewById(arcMenu);
        setupFabListener();

        // Setup FAB buttons
        _bgMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item1_bg);
        _insMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item2_insulin);
        _carbsMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item3_carbs);
        _exerMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item4_exercise);
        setupFabMenuItemsListeners();

        _fabFrame = (FrameLayout) findViewById(R.id.frame_fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        } else if (id == R.id.nav_goals) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_signout) {
            Intent intent = new Intent(_context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setupFabListener() {
        _arcMenu.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                _fabFrame.setBackgroundColor(ContextCompat.getColor(_context, R.color.transparentGray));
                _fabFrame.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (_arcMenu.isMenuOpened()) {
                            _arcMenu.toggleMenu();
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
                if (_arcMenu.isMenuOpened()) {
                    _arcMenu.toggleMenu();
                }
                Intent addBGReadingActivity = new Intent(MainActivity.this, AddBGReadingActivity.class);
                startActivity(addBGReadingActivity);
            }
        });

        _insMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_arcMenu.isMenuOpened()) {
                    _arcMenu.toggleMenu();
                }
                Intent addInsulinActivity = new Intent(MainActivity.this, AddInsulinActivity.class);
                startActivity(addInsulinActivity);
            }
        });

        _carbsMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_arcMenu.isMenuOpened()) {
                    _arcMenu.toggleMenu();
                }
                Intent addCarbsActivity = new Intent(MainActivity.this, AddFoodLogActivity.class);
                startActivity(addCarbsActivity);
            }
        });

        _exerMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_arcMenu.isMenuOpened()) {
                    _arcMenu.toggleMenu();
                }
                Intent addExerciseActivity = new Intent(MainActivity.this, AddExerciseLogActivity.class);
                startActivity(addExerciseActivity);
            }
        });
    }
}
