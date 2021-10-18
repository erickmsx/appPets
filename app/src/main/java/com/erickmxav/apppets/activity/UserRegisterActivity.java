package com.erickmxav.apppets.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.erickmxav.apppets.R;
import com.erickmxav.apppets.config.FirebaseConfig;
import com.erickmxav.apppets.helper.Base64Custom;
import com.erickmxav.apppets.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class UserRegisterActivity extends AppCompatActivity {

    private EditText fieldName, fieldEmail, fieldPassword;
    private Button buttRegister;
    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        fieldName = findViewById(R.id.editName);
        fieldEmail = findViewById(R.id.editEmail);
        fieldPassword = findViewById(R.id.editPassword);
        buttRegister = findViewById(R.id.buttonRegister);

        buttRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textName = fieldName.getText().toString();
                String textEmail = fieldEmail.getText().toString();
                String textPassword = fieldPassword.getText().toString();

                //Validation if the fields have been filled in
                if ( !textName.isEmpty() ) {
                    if ( !textEmail.isEmpty() ){
                        if ( !textPassword.isEmpty() ){

                            User user = new User();
                            user.setName( textName );
                            user.setEmail( textEmail );
                            user.setPassword( textPassword );
                            registerUser( user );

                        }else {
                            Toast.makeText(UserRegisterActivity.this,
                                    "Preencha a senha",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(UserRegisterActivity.this,
                                "Preencha o email",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UserRegisterActivity.this,
                            "Preencha o nome",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registerUser(final User user){

        authentication = FirebaseConfig.getAuthenticationFirebase();
        authentication.createUserWithEmailAndPassword(
                user.getEmail(), user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful() ) {

                    try{
                        String idUser = Base64Custom.codifyBase64( user.getEmail() );
                        user.setIdUser( idUser);
                        user.save();

                        Toast.makeText(UserRegisterActivity.this,
                                "Cadastro efetuado com sucesso!",
                                Toast.LENGTH_SHORT).show();

                        finish();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    }else {
                        String exception = "";

                        try {
                            throw task.getException();

                        }catch ( FirebaseAuthWeakPasswordException e) {
                            exception = "Por favor, digite uma senha mais forte!";

                        }catch ( FirebaseAuthInvalidCredentialsException e) {
                            exception = "Por favor, digite um e-mail válido";

                        }catch (FirebaseAuthUserCollisionException e) {
                            exception = "Esta conta já foi cadastrada";

                        }catch (Exception e){
                            exception = "Erro ao cadastrar usuário: " + e.getMessage();
                            e.printStackTrace();
                        }

                        Toast.makeText(UserRegisterActivity.this,
                                exception,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }