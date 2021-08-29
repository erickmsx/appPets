package com.erickmxav.apppets.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.erickmxav.apppets.config.FirebaseConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserFirebase {

    public static String getUserId() {

        FirebaseAuth user = FirebaseConfig.getAuthenticationFirebase();
        String email = user.getCurrentUser().getEmail();
        String userId = Base64Custom.codifyBase64(email);

        return userId;
    }

    public static FirebaseUser getActualUser() {
        FirebaseAuth user = FirebaseConfig.getAuthenticationFirebase();
        return user.getCurrentUser();
    }
}
