package com.example.alex.glucosecoach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.alex.glucosecoach.R;
import com.example.alex.glucosecoach.controller.RestManager;
import com.example.alex.glucosecoach.controller.TokenManager;

import com.example.alex.glucosecoach.controller.UserManager;
import com.example.alex.glucosecoach.models.User;
import com.example.alex.glucosecoach.services.UserService;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alex.glucosecoach.R.id.arcMenu;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArcMenu _arcMenu;
    private FloatingActionButton _bgMenuItem, _insMenuItem, _carbsMenuItem, _exerMenuItem;

    RestManager apiService;
    TokenManager tokenManager;
    UserManager userManager = new UserManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tokenManager = new TokenManager();
        userManager = new UserManager();

        super.onCreate(savedInstanceState);
        if (isLoggedIn()) {
            loadContent();
        } else {
            Intent loginActivity = new Intent(this, LoginActivity.class);
            loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginActivity);
        }
    }

    public void loadContent() {
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

        // Setup FAB menu
        _arcMenu = (ArcMenu) findViewById(arcMenu);
        setupFabListener();

        // Setup FAB buttons
        _bgMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item1_bg);
        _insMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item2_insulin);
        _carbsMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item3_carbs);
        _exerMenuItem = (FloatingActionButton) findViewById(R.id.fab_menu_item4_exercise);
        setupFabMenuItemsListeners();

        getUser();
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
        } else if (id == R.id.action_sign_out) {
            tokenManager.clearToken(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_logbook) {

        } else if (id == R.id.nav_charts) {

        } else if (id == R.id.nav_goals) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setupFabListener() {
        _arcMenu.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                //Toast.makeText(getApplicationContext(), "Menu Opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuClosed() {
                //Toast.makeText(getApplicationContext(), "Menu Closed", Toast.LENGTH_SHORT).show();
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
                Intent addCarbsActivity = new Intent(MainActivity.this, AddCarbsActivity.class);
                startActivity(addCarbsActivity);
            }
        });

        _exerMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addExerciseActivity = new Intent(MainActivity.this, AddExerciseActivity.class);
                startActivity(addExerciseActivity);
            }
        });
    }

    public boolean isLoggedIn() {
        if (!tokenManager.hasToken(this)) {
            return false;
        } else {
            return true;
        }
    }

    public void getUser() {
        apiService = new RestManager();
        UserService userService = apiService.getUserService(this);
        Call<User> call = userService.getUser(userManager.getUsername(this));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    setUserLogin(response.body());
                } else {
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void setUserLogin(User user) {
        userManager.setEmail(this, user.getEmail());

        TextView _usernameText = (TextView) findViewById(R.id.txt_username);
        TextView _emailText = (TextView) findViewById(R.id.txt_email);
        _usernameText.setText(user.getUsername());
        _emailText.setText(user.getEmail());
    }
}
