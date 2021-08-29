package com.erickmxav.apppets.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erickmxav.apppets.R;
import com.erickmxav.apppets.config.FirebaseConfig;
import com.erickmxav.apppets.helper.UserFirebase;
import com.erickmxav.apppets.helper.Permission;
import com.erickmxav.apppets.model.Pet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetRegisterActivity extends AppCompatActivity {
    private TextView mTextView; //bottomsheet
    private static final int SELECAO_CAMERA  = 100;
    private static final int SELECAO_GALERIA = 200;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private Button registerPet;
    private EditText fieldName, fieldBirthDate, fieldspecie;
    private CircleImageView chooseProfile;
    private Button chooseCamera;
    private Button chooseGallery;
    private Button changeImageprofile;
    private CircleImageView imageProfile;
    private Pet pet;

    private StorageReference storageReference;
    private FirebaseAuth authentication;
    private String userId;
    private DatabaseReference petRef;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_register);

        //Validation permissions
        Permission.validatePermissions(permissoesNecessarias, this, 1);

        storageReference = FirebaseConfig.getFirebaseStorage();
        authentication = FirebaseConfig.getAuthenticationFirebase();

        userId = UserFirebase.getUserId();
        fieldName = findViewById(R.id.editName);
        fieldBirthDate = findViewById(R.id.editBirthDate);
        fieldspecie = findViewById(R.id.editSpecie);
        chooseCamera = findViewById(R.id.editCamera);
        chooseGallery = findViewById(R.id.editGallery);
        changeImageprofile = findViewById(R.id.changeImageProf);
        imageProfile = findViewById(R.id.imageProf);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastre um pet");
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recover user data
        FirebaseUser user = UserFirebase.getActualUser();
        Uri url = user.getPhotoUrl();

        Button buttonShow = changeImageprofile;
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        PetRegisterActivity.this, R.style.BottomSheetTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_bottom_sheet,
                                (LinearLayout)findViewById(R.id.bottomSheetContainer)
                        );

                bottomSheetView.findViewById(R.id.editCamera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if ( i.resolveActivity(getPackageManager()) != null ) {
                            startActivityForResult(i, SELECAO_CAMERA);

                        }
                    }
                });

                bottomSheetView.findViewById(R.id.editGallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                        if ( i.resolveActivity(getPackageManager()) != null ){
                            startActivityForResult(i, SELECAO_GALERIA );

                        }
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK ){
            Bitmap imagem = null;

            try {

                switch ( requestCode ){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri selectedImage = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage );
                        break;
                }

                if ( imagem != null ){

                    imageProfile.setImageBitmap( imagem );

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos );
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar imagem no firebase
                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("pets")
                            .child( userId )
                            .child(userId + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes( dadosImagem );
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PetRegisterActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(PetRegisterActivity.this,
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    //Register pet
    public void registerPet(View view){

        pet = new Pet();
        pet.setName( fieldName.getText().toString() );
        pet.setBirthDate( fieldBirthDate.getText().toString() );
        pet.setSpecie( fieldspecie.getText().toString() );

        pet.Register();

        Toast.makeText(PetRegisterActivity.this,
                "Sucesso ao cadastrar pet",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.logOut:
                authentication.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    //Permissões galeria/camera

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for ( int resultPermission : grantResults){
            if ( resultPermission == PackageManager.PERMISSION_DENIED ){
                validatePermissionAlert();
            }
        }
    }

    private void validatePermissionAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

