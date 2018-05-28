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
import com.example.alejandro.roomexampleproject.database.daos.UserDao;
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
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USERNAME", usernameEditText.getText().toString());
                    editor.apply();

                    new InsertUserAsync(database).execute();

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
        private final UserDao userdao;

        private InsertUserAsync(AppDatabase db) {
            this.userdao = db.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userdao.insert(new User(usernameEditText.getText().toString(), "", "", ""));
            return null;
        }
    }
}
