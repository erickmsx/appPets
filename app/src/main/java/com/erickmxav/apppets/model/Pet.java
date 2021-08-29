package com.erickmxav.apppets.model;

import com.erickmxav.apppets.config.FirebaseConfig;
import com.erickmxav.apppets.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Pet {

    private String name;
    private String birthDate;
    private String specie;

    public Pet() {
    }

    public void Register(){

        //método pra pegar o email do usuário que está logado e codificar pra ser usado
        //no idUsuario do firebase
        FirebaseAuth authentication = FirebaseConfig.getAuthenticationFirebase();
        String idUser = Base64Custom.codifyBase64( authentication.getCurrentUser().getEmail() );

        DatabaseReference firebase = FirebaseConfig.getFirebaseDatabase();
        firebase.child("pets")
                .child( idUser )
                .push()
                .setValue( this );

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

}
