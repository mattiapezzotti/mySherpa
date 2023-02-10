package it.unimib.camminatori.mysherpa.viewmodel;

import android.content.Context;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.camminatori.mysherpa.repository.FirebaseRepository;

public class Firebase_ViewModel {

    public Firebase_ViewModel() {
    }

    public SignInClient getSignInClient(Context c) {
        return FirebaseRepository.getInstance().getSignInClient(c);
    }

    public FirebaseUser getUser() {
        return FirebaseRepository.getInstance().getUser();
    }

    public FirebaseAuth getmAuth() {
        return FirebaseRepository.getInstance().getmAuth();
    }

    public DatabaseReference getmReference() {
        return FirebaseRepository.getInstance().getmReference();
    }

    public FirebaseDatabase getmDatabase() {
        return FirebaseRepository.getInstance().getmDatabase();
    }

    public GetSignInIntentRequest getSignInIntentRequest(String webID) {
        return FirebaseRepository.getInstance().getSignInIntentRequest(webID);
    }

    public void writeNewUser(String uid, String name, String email){
        FirebaseRepository.getInstance().writeNewUser(uid, name, email);
    }
}
