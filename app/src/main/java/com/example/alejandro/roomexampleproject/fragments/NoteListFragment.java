
package com.example.alejandro.roomexampleproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.alejandro.roomexampleproject.R;
import com.example.alejandro.roomexampleproject.activities.AddNoteActivity;
import com.example.alejandro.roomexampleproject.adapters.NoteAdapter;
import com.example.alejandro.roomexampleproject.models.Category;
import com.example.alejandro.roomexampleproject.models.Note;
import com.example.alejandro.roomexampleproject.models.User;

import java.util.List;

public class NoteListFragment extends Fragment{

    private User user;
    private List<Note> userNotes;
    private List<Category> userCategories;
    private NoteAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.notesListRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NoteAdapter(container.getContext(), userNotes);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getContext(), AddNoteActivity.class);
                startActivityForResult(intent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        adapter.notifyItemInserted(userNotes.size()+1);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserNotes(List<Note> userNotes){
        this.userNotes = userNotes;
    }

    public void setUserCategories(List<Category> userCategories){
        this.userCategories = userCategories;
    }
}
