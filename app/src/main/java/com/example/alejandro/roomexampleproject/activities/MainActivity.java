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
import android.view.MenuItem;

import com.example.alejandro.roomexampleproject.R;
import com.example.alejandro.roomexampleproject.database.AppDatabase;
import com.example.alejandro.roomexampleproject.database.daos.CategoryDao;
import com.example.alejandro.roomexampleproject.database.daos.NoteDao;
import com.example.alejandro.roomexampleproject.database.daos.UserDao;
import com.example.alejandro.roomexampleproject.fragments.CategoryListFragment;
import com.example.alejandro.roomexampleproject.fragments.NoteListFragment;
import com.example.alejandro.roomexampleproject.fragments.UserInfoFragment;
import com.example.alejandro.roomexampleproject.models.Category;
import com.example.alejandro.roomexampleproject.models.Note;
import com.example.alejandro.roomexampleproject.models.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private AppDatabase database;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //database
        database = AppDatabase.getInstance(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
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

                switch (item.getItemId()) {
                    case R.id.profileItem:
                        new GetUserAsync(database).execute();
                        break;

                    case R.id.notesItem:
                        new GetNotesAsync(database).execute();
                        break;

                    case R.id.categoriesItem:
                        new GetCategoriesAsync(database).execute();
                        break;

                    case R.id.changeUserItem:
                        logout();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                new GetNotesAsync(database).execute();
                break;
            case 2:
                new GetCategoriesAsync(database).execute();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERNAME", null);
        editor.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private class GetUserAsync extends AsyncTask<Void, User, User> {
        private final UserDao userDao;
        private final NoteDao noteDao;
        private List<Note> notes;

        private GetUserAsync(AppDatabase db) {
            this.userDao = db.userDao();
            this.noteDao = db.noteDao();
        }

        @Override
        protected User doInBackground(Void... voids) {
            String username = sharedPreferences.getString("USERNAME", null);
            int id = userDao.findByUsername(username).getId();
            notes = noteDao.getAllNotesByUser(id);
            return userDao.findByUsername(username);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            UserInfoFragment fragment = new UserInfoFragment();
            fragment.setUser(user);
            fragment.setUserNotes(notes);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentFrame, fragment);
            fragmentTransaction.commit();
        }
    }

    private class GetNotesAsync extends AsyncTask<Void, Void, List<Note>> {
        private final NoteDao noteDao;
        private final UserDao userDao;

        private GetNotesAsync(AppDatabase db) {
            this.noteDao = db.noteDao();
            this.userDao = db.userDao();
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            String username = sharedPreferences.getString("USERNAME", null);
            int id = userDao.findByUsername(username).getId();
            return noteDao.getAllNotesByUser(id);
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            super.onPostExecute(notes);
            NoteListFragment fragment = new NoteListFragment();
            fragment.setUserNotes(notes);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentFrame, fragment);
            fragmentTransaction.commit();
        }
    }

    private class GetCategoriesAsync extends AsyncTask<Void, Void, List<Category>> {
        private final CategoryDao categoryDao;
        private final UserDao userDao;

        private GetCategoriesAsync(AppDatabase db) {
            this.categoryDao = db.categoryDao();
            this.userDao = db.userDao();
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            String username = sharedPreferences.getString("USERNAME", null);
            int id = userDao.findByUsername(username).getId();
            return categoryDao.getAllCategoriesByUser(id);
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            super.onPostExecute(categories);
            CategoryListFragment fragment = new CategoryListFragment();
            fragment.setUserCategories(categories);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentFrame, fragment);
            fragmentTransaction.commit();
        }
    }
}
