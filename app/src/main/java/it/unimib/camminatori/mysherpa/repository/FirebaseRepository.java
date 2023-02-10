package it.unimib.camminatori.mysherpa.repository;

import android.content.Context;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.camminatori.mysherpa.model.User;

public class FirebaseRepository {

    private static FirebaseRepository instance = null;

    private final FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private final FirebaseDatabase mDatabase;
    private final String databaseLocation = "https://pdm---pahada-default-rtdb.europe-west1.firebasedatabase.app";

    public static FirebaseRepository getInstance() {
        if (instance == null)
            instance = new FirebaseRepository();
        return instance;
    }

    private FirebaseRepository() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(databaseLocation);
        mReference = mDatabase.getReference("user");
    }

    public SignInClient getSignInClient(Context c) {
        return Identity.getSignInClient(c);
    }

    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
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

    public GetSignInIntentRequest getSignInIntentRequest(String webID) {
        return GetSignInIntentRequest.builder()
                .setServerClientId(webID)
                .build();
    }

    public void writeNewUser(String uid, String email, String name) {
        mReference.child(uid).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                User user = new User(email, name);
                mReference.child(uid).setValue(user);
            }
        });
    }

    public void updateKmTot(Double km) {
        mReference.child(getUser().getUid()).child("kmTot").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Double newKM = task.getResult().getValue(Double.class) + km;
                System.out.println(newKM);
                mReference.child(getUser().getUid()).child("kmTot").setValue(newKM);
            }
        });

    }
}
