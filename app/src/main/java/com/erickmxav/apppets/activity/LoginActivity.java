package com.erickmxav.apppets.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.erickmxav.apppets.R;
import com.erickmxav.apppets.config.FirebaseConfig;
import com.erickmxav.apppets.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText fieldEmail, fieldPassword;
    private Button buttLogIn;
    private User user;
    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fieldEmail = findViewById(R.id.editEmail);
        fieldPassword = findViewById(R.id.editPassword);
        buttLogIn = findViewById(R.id.buttonLogIn);

        buttLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Validation if the fields have been filled in
                String textEmail = fieldEmail.getText().toString();
                String textPassword = fieldPassword.getText().toString();

                if (!textEmail.isEmpty()) {
                    if (!textPassword.isEmpty()) {
                        user = new User();
                        user.setEmail(textEmail);
                        user.setPassword(textPassword);
                        validateLogIn();

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Preencha a senha",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha o e-mail",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validateLogIn() {
        authentication = FirebaseConfig.getAuthenticationFirebase();
        authentication.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(LoginActivity.this,
                            "Sucesso ao fazer login",
                            Toast.LENGTH_SHORT).show();

                    openHome();

                } else {
                    String exception = "";
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthInvalidUserException e) {
                        exception = "Usuário não está cadastrado";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "E-mail ou senha não correspondem a um usuário cadastrado";

                    } catch (Exception e) {
                        exception = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this,
                            exception,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void verifyUserLogged(){

        authentication = FirebaseConfig.getAuthenticationFirebase();

        if( authentication.getCurrentUser() != null ){

            openHome();

            //authentication.signOut();

        }
    }

    public void openHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyUserLogged();
    }

    public void openHome(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void openUserRegister(View view) {
        startActivity(new Intent(this, UserRegisterActivity.class));
    }
}


