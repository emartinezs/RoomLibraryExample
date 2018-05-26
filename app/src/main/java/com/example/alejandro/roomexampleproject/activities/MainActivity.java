package com.example.alejandro.roomexampleproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.alejandro.roomexampleproject.R;
import com.example.alejandro.roomexampleproject.database.AppDatabase;
import com.example.alejandro.roomexampleproject.database.daos.UserDao;
import com.example.alejandro.roomexampleproject.fragments.UserInfoFragment;
import com.example.alejandro.roomexampleproject.models.User;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private AppDatabase database;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SharedPreferences sharedPreferences;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //database
        database = AppDatabase.getInstance(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //setting up the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //setting up drawerlayout
        drawerLayout = findViewById(R.id.drawerLayout);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                switch (item.getItemId()){
                    case R.id.profileItem:
                        new GetUserAsync(database).execute();
                        break;

                    case R.id.notesItem:
                        Log.d("DrawerLayout", "Notes not implemented yet");
                        break;

                    case R.id.changeUserItem:
                        logout();
                        break;
                }
                return true;
            }
        });
    }

    private void logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERNAME", null);
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private class GetUserAsync extends AsyncTask<Void, User, User>{
        private final UserDao userdao;

        private GetUserAsync(AppDatabase db) {
            this.userdao = db.userDao();
        }

        @Override
        protected User doInBackground(Void... voids) {
            String username = sharedPreferences.getString("USERNAME", null);
            return userdao.findByUsername(username);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            UserInfoFragment fragment = new UserInfoFragment();
            fragment.setUser(user);

            fragmentTransaction.replace(R.id.contentFrame, fragment);
            fragmentTransaction.commit();
        }
    }
 }
