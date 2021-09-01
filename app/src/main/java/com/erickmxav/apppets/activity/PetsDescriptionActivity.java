package com.erickmxav.apppets.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.erickmxav.apppets.R;
import com.erickmxav.apppets.model.Pet;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetsDescriptionActivity extends AppCompatActivity {

    private TextView textNameDesc;
    private TextView textSpecieDesc;
    private TextView textBDateDesc;
    private CircleImageView imageProfDesc;
    private Pet petDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets_description);

        textNameDesc = findViewById(R.id.textNameDesc);
        textSpecieDesc = findViewById(R.id.textSpecieDesc);
        textBDateDesc = findViewById(R.id.textBDateDesc);
        imageProfDesc = findViewById(R.id.imageProfDesc);

        //Recover data pets
        Bundle bundle = getIntent().getExtras();
        if ( bundle != null ){

            petDesc = ( Pet )bundle.getSerializable("petsDescription");
            textNameDesc.setText( petDesc.getName() );
            textSpecieDesc.setText( petDesc.getSpecie() );
            textBDateDesc.setText( petDesc.getBirthDate() );

            String photo = petDesc.getPhoto();
            if( photo != null ){

                Uri url = Uri.parse( petDesc.getPhoto() );
                Glide.with(PetsDescriptionActivity.this)
                        .load( url )
                        .into( imageProfDesc );

            }else {
                imageProfDesc.setImageResource(R.drawable.perfil);
            }

        }


    }
}