package it.unimib.camminatori.mysherpa.repository;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.User;

public class FirebaseRepository {

    private static FirebaseRepository instance = null;

    private SignInClient signInClient;
    private FirebaseUser user;
    private final FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private final FirebaseDatabase mDatabase;
    private final String databaseLocation = "https://pdm---pahada-default-rtdb.europe-west1.firebasedatabase.app";

    public static FirebaseRepository getInstance() {
        if (instance == null)
            instance = new FirebaseRepository();
        return instance;
    }

    private FirebaseRepository(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(databaseLocation);
        mReference = mDatabase.getReference("user");
    }

    public SignInClient getSignInClient(Context c) {
        return Identity.getSignInClient(c);
    }

    public FirebaseUser getUser() {
        return user;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public DatabaseReference getmReference() {
        return mReference;
    }

    public FirebaseDatabase getmDatabase() {
        return mDatabase;
    }

    public GetSignInIntentRequest getSignInIntentRequest(String webID){
        return GetSignInIntentRequest.builder()
                .setServerClientId(webID)
                .build();
    }

    public void writeNewUser(String uid, String email, String name) {
        User user = new User(email, name);
        mReference.child(uid).setValue(user);
    }

    private void updateKmTot(Double km) {
        mReference.child("users").child(mAuth.getCurrentUser().getUid()).child("km").setValue(km);
    }
}
