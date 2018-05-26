package com.example.alejandro.roomexampleproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alejandro.roomexampleproject.R;
import com.example.alejandro.roomexampleproject.models.Note;
import com.example.alejandro.roomexampleproject.models.User;

import java.util.List;

public class UserInfoFragment extends Fragment{

    private User user;
    private List<Note> userNotes;
    private TextView username, firstName, lastName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_info, container, false);

        username  = v.findViewById(R.id.usernameTextView);
        firstName = v.findViewById(R.id.firstNameTextView);
        lastName = v.findViewById(R.id.lastNameTextView);

        username.setText(user.getUsername());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());

        return v;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserNotes(List<Note> userNotes){
        this.userNotes = userNotes;
    }
}
