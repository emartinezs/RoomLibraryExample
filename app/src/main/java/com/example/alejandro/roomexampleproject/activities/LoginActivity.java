package com.example.alejandro.roomexampleproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alejandro.roomexampleproject.R;
import com.example.alejandro.roomexampleproject.database.AppDatabase;
import com.example.alejandro.roomexampleproject.database.daos.CategoryDao;
import com.example.alejandro.roomexampleproject.database.daos.UserDao;
import com.example.alejandro.roomexampleproject.models.Category;
import com.example.alejandro.roomexampleproject.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private AppDatabase database;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = AppDatabase.getInstance(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = sharedPreferences.getString("USERNAME", null);

        if (username != null) {
            openMainActivity();
            finish();
        }

        Button loginButton = findViewById(R.id.loginButton);
        usernameEditText = findViewById(R.id.loginUsernameEditText);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!usernameEditText.getText().toString().equals("")) {
                    new ValidateUserAsync(database).execute();

                    openMainActivity();
                    finish();
                }
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private class InsertUserAsync extends AsyncTask<Void, Void, Void> {
        private final UserDao userDao;
        private final CategoryDao categoryDao;

        private InsertUserAsync(AppDatabase db) {
            this.userDao = db.userDao();
            this.categoryDao = db.categoryDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new User(usernameEditText.getText().toString(), "", "", ""));
            int id = userDao.findByUsername(usernameEditText.getText().toString()).getId();
            categoryDao.insert(new Category("All", id));
            return null;
        }
    }

    private class ValidateUserAsync extends AsyncTask<Void, Void, Void>{

        private final UserDao userDao;
        private boolean userExists = false;

        private ValidateUserAsync(AppDatabase db) {
            this.userDao = db.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String username = usernameEditText.getText().toString();
            if (userDao.findByUsername(username) != null){
                userExists = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            String username = usernameEditText.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("USERNAME", username);
            editor.apply();
            if (!userExists) {
                new InsertUserAsync(database).execute();
            }
        }
    }
}
