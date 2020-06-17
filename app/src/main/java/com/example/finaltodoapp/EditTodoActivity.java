package com.example.finaltodoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class EditTodoActivity extends AppCompatActivity {

    Fragment mFragment;
    FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        mFragment = new EditTodoFragment();
        mFragmentManager=getSupportFragmentManager();

        mFragmentManager.beginTransaction()
                .add(R.id.main_container,mFragment)
                .commit();
    }
}
