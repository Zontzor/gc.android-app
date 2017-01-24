package com.example.alex.glucosecoach.main;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.services.APIConnection;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;

import static com.example.alex.glucosecoach.R.id.arcMenu;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button connectButton;
    private ArcMenu floatingActionMenu;
    private FloatingActionButton bgFabMenuItem, insFabMenuItem, carbsFabMenuItem, exerFabMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup connect button
        connectButton = (Button) findViewById(R.id.btn_connect);
        setupConnListener();

        // Setup FAB menu
        floatingActionMenu = (ArcMenu) findViewById(arcMenu);
        setupFabListener();

        // Setup FAB buttons
        bgFabMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item1_bg);
        insFabMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item2_insulin);
        carbsFabMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item3_carbs);
        exerFabMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item4_exercise);
        setupFabMenuItemsListeners();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setupConnListener() {
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Gets the URL from the UI's text field.
                String stringUrl = "http://192.168.1.101:5000/glucose_coach/api/v1.0/users";

                // Check to see if a network connection is available
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    new APIConnection().execute(stringUrl);
                } else {
                    Toast.makeText(getApplicationContext(),"No network connection available",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setupFabListener() {
        floatingActionMenu.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                Toast.makeText(getApplicationContext(), "Menu Opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuClosed() {
                Toast.makeText(getApplicationContext(), "Menu Closed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupFabMenuItemsListeners() {
        bgFabMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBGReadingActivity = new Intent(MainActivity.this, AddBGReadingActivity.class);
                startActivity(addBGReadingActivity);
            }
        });

        insFabMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        carbsFabMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        exerFabMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
