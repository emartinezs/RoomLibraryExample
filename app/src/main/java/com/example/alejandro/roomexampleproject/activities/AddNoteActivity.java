package com.example.alejandro.roomexampleproject.activities;

import android.app.Activity;
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
import com.example.alejandro.roomexampleproject.database.daos.NoteDao;
import com.example.alejandro.roomexampleproject.database.daos.UserDao;
import com.example.alejandro.roomexampleproject.models.Note;

public class AddNoteActivity extends AppCompatActivity{

    private SharedPreferences sharedPreferences;
    private EditText noteEditText;
    private AppDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        database = AppDatabase.getInstance(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        noteEditText = findViewById(R.id.noteEditText);
        Button addNoteButton = findViewById(R.id.addNoteButton);

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
                setResult(Activity.RESULT_OK, resultIntent);
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
            noteDao.insert(new Note(noteEditText.getText().toString(), id, 0));
            return "DONE";
        }
    }
}
