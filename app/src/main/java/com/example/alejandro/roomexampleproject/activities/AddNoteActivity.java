package com.example.alejandro.roomexampleproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.alejandro.roomexampleproject.R;
import com.example.alejandro.roomexampleproject.database.AppDatabase;
import com.example.alejandro.roomexampleproject.database.daos.CategoryDao;
import com.example.alejandro.roomexampleproject.database.daos.NoteDao;
import com.example.alejandro.roomexampleproject.database.daos.UserDao;
import com.example.alejandro.roomexampleproject.models.Category;
import com.example.alejandro.roomexampleproject.models.Note;

import java.util.ArrayList;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity{

    private SharedPreferences sharedPreferences;
    private EditText noteEditText;
    private AppDatabase database;
    private Spinner categorySpinner;
    private int selectedCategory;
    private List<Category> categoryList = new ArrayList<>();
    private List<Integer> categoryIdList = new ArrayList<>();
    private final int GET_NOTES = 1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        database = AppDatabase.getInstance(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        noteEditText = findViewById(R.id.noteEditText);
        Button addNoteButton = findViewById(R.id.addNoteButton);

        categorySpinner = findViewById(R.id.categorySpinner);
        new GetCategoriesAsync(database).execute();

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = "";
                try {
                    result = new InsertNoteAsync(database).execute().get();
                }catch (Exception e){}
                while (!result.equals("DONE")){

                }
                Intent resultIntent = new Intent();
                setResult(GET_NOTES, resultIntent);
                finish();
            }
        });
    }

    private class InsertNoteAsync extends AsyncTask<Void, Void, String>{
        private NoteDao noteDao;
        private UserDao userDao;

        private InsertNoteAsync(AppDatabase db){
            noteDao = db.noteDao();
            userDao = db.userDao();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String username = sharedPreferences.getString("USERNAME", null);
            int id = userDao.findByUsername(username).getId();
            noteDao.insert(new Note(noteEditText.getText().toString(), id, selectedCategory));
            return "DONE";
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
            categoryList = categories;
            List<String> categoryNameList = new ArrayList<>();
            for (Category c:categoryList){
                categoryNameList.add(c.getName());
                categoryIdList.add(c.getId());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryNameList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
            categorySpinner.setSelection(0);
            categorySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedCategory = categoryIdList.get(i);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }
}
