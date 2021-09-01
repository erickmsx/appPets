package com.erickmxav.apppets.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.erickmxav.apppets.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCadastrarPet(View view) {
        startActivity(new Intent(this, PetRegisterActivity.class));
    }

    public void openListPets(View view) {
        startActivity(new Intent(this, ListPetsActivity.class));
    }
}