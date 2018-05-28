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

public class AddCategoryActivity extends AppCompatActivity{

    private SharedPreferences sharedPreferences;
    private EditText categoryEditText;
    private AppDatabase database;
    private final int GET_CATEGORIES = 2;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        database = AppDatabase.getInstance(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        categoryEditText = findViewById(R.id.categoryEditText);
        Button addCategoryButton = findViewById(R.id.addCategoryButton);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = "";
                try {
                    result = new InsertCategoryAsync(database).execute().get();
                }catch (Exception e){}
                while (!result.equals("DONE")){

                }
                Intent resultIntent = new Intent();
                setResult(GET_CATEGORIES, resultIntent);
                finish();
            }
        });
    }

    private class InsertCategoryAsync extends AsyncTask<Void, Void, String>{
        private CategoryDao categoryDao;
        private UserDao userDao;

        private InsertCategoryAsync(AppDatabase db){
            categoryDao = db.categoryDao();
            userDao = db.userDao();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String username = sharedPreferences.getString("USERNAME", null);
            int id = userDao.findByUsername(username).getId();
            categoryDao.insert(new Category(categoryEditText.getText().toString(), id));
            return "DONE";
        }
    }
}
