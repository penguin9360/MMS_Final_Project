package com.project.plantappui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.project.plantappui.databinding.ActivityLibraryListBinding;

public class LibraryListActivity extends AppCompatActivity {

    private ActivityLibraryListBinding binding_lib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding_lib = ActivityLibraryListBinding.inflate(getLayoutInflater());
        setContentView(binding_lib.getRoot());

        Toolbar toolbar = binding_lib.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding_lib.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

    }
}