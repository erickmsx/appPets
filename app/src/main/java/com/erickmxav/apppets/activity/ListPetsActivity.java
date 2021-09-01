package com.erickmxav.apppets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.erickmxav.apppets.R;
import com.erickmxav.apppets.adapter.AdapterPet;
import com.erickmxav.apppets.config.FirebaseConfig;
import com.erickmxav.apppets.helper.Base64Custom;
import com.erickmxav.apppets.helper.RecyclerItemClickListener;
import com.erickmxav.apppets.model.Pet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListPetsActivity extends AppCompatActivity {

    private FirebaseAuth authentication = FirebaseConfig.getAuthenticationFirebase();
    private DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
    private DatabaseReference userRef;

    private RecyclerView recyclerListPets;
    private AdapterPet adapter;
    private List<Pet> petsList = new ArrayList<>();
    private Pet pet;
    private DatabaseReference petRef;
    private ValueEventListener valueEventListenerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pets);

        recyclerListPets = findViewById(R.id.recyclerListPets);


        //Configurar adapter
        adapter = new AdapterPet (petsList, this);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerListPets.setLayoutManager(layoutManager);
        recyclerListPets.setHasFixedSize(true);
        recyclerListPets.setAdapter(adapter);

        //Config eventclick on recyclerview
        recyclerListPets.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerListPets,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Pet selectedPet = petsList.get( position );
                                Intent i = new Intent(getApplicationContext(), PetsDescriptionActivity.class);
                                i.putExtra("petsDescription", selectedPet);
                                startActivity( i );
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )

        );
    }

    public void recoverPets(){

        String userEmail = authentication.getCurrentUser().getEmail();
        String idUser = Base64Custom.codifyBase64( userEmail );
        petRef = firebaseRef.child("pets")
                .child( idUser );

        valueEventListenerUser = petRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                petsList.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Pet pet = dados.getValue( Pet.class );
                    petsList.add( pet );

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recoverPets();

    }

    @Override
    protected void onStop() {
        super.onStop();
        petRef.removeEventListener( valueEventListenerUser );
    }
}