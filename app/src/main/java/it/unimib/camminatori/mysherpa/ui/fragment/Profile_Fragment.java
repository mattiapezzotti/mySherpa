package it.unimib.camminatori.mysherpa.ui.fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.User;

public class Profile_Fragment extends Fragment {

    private Button modificaProfilo, logout, register, login, settings, googleLogin;
    private MaterialTextView username;
    private View viewNav;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    private String userID;

    private SignInClient signInClient;


    public Profile_Fragment() {
        // Required empty public constructor
    }

    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> handleSignInResult(result.getData())
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        signInClient = Identity.getSignInClient(requireContext());

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        viewNav = this.getActivity().findViewById(R.id.nav_host_fragment);

        register = view.findViewById(R.id.button_register);
        login = view.findViewById(R.id.button_login);
        modificaProfilo = view.findViewById(R.id.button_profile);
        settings = view.findViewById(R.id.iconButtonSettings);
        googleLogin = view.findViewById(R.id.register_google);
        username = view.findViewById(R.id.nameHolder);
        logout = view.findViewById(R.id.button_logout);

        logout.setOnClickListener(view1 -> {
            mAuth.signOut();

            signInClient.signOut().addOnCompleteListener(requireActivity(),
                    task -> updateUI(null));

            updateUI(null);
        });

        modificaProfilo.setOnClickListener(v -> {
            Navigation.findNavController(viewNav).popBackStack();
            Navigation.findNavController(viewNav).navigate(R.id.editProfile_fragment);
        });

        register.setOnClickListener(v -> {
            Navigation.findNavController(viewNav).popBackStack();
            Navigation.findNavController(viewNav).navigate(R.id.register_fragment);
        });

        login.setOnClickListener(v -> {
            Navigation.findNavController(viewNav).popBackStack();
            Navigation.findNavController(viewNav).navigate(R.id.login_fragment);
        });

        settings.setOnClickListener(v -> {
            Navigation.findNavController(viewNav).popBackStack();
            Navigation.findNavController(viewNav).navigate(R.id.settings_Fragment);
        });

        googleLogin.setOnClickListener(v -> {
            signIn();
        });

        /*
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userProfile = snapshot.getValue(User.class);

                    if (userProfile != null) {
                        String fullName = userProfile.email;
                        username.setText(fullName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_LONG).show();
                }
            });

        }
         */
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            logout.setEnabled(true);
            modificaProfilo.setEnabled(true);
            googleLogin.setEnabled(false);
            register.setEnabled(false);
            login.setEnabled(false);
            username.setText(user.getEmail());
        } else {
            logout.setEnabled(false);
            modificaProfilo.setEnabled(false);
            googleLogin.setEnabled(true);
            register.setEnabled(true);
            login.setEnabled(true);
            username.setText("No current user logged in");
        }
    }

    private void handleSignInResult(Intent data) {
        try {
            SignInCredential credential = signInClient.getSignInCredentialFromIntent(data);
            String idToken = credential.getGoogleIdToken();
            System.out.println(credential.getPassword() + " " + credential.getDisplayName());
            firebaseAuthWithGoogle(idToken);
        } catch (ApiException e) {
            showError(e.getMessage());
            updateUI(null);
        }
    }

    private void signUpWithGoogle(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(email, email);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {

                                    if (task1.isSuccessful()) {
                                        new MaterialAlertDialogBuilder(getActivity())
                                                .setIcon(R.mipmap.ic_launcher)
                                                .setTitle(R.string.app_name)
                                                .setMessage("Utente creato correttamente.")
                                                .setPositiveButton("OK", null)
                                                .show();

                                        Navigation.findNavController(viewNav).navigate(R.id.fragment_profile);

                                    } else {
                                        new MaterialAlertDialogBuilder(getActivity())
                                                .setIcon(R.mipmap.ic_launcher)
                                                .setTitle(R.string.app_name)
                                                .setMessage("Errore nella creazione dell'account.")
                                                .setPositiveButton("OK", null)
                                                .show();
                                    }
                                });
                    } else {
                        new MaterialAlertDialogBuilder(getActivity())
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle(R.string.app_name)
                                .setMessage("Errore nella creazione dell'account.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        showError("Account Inesistente");
                    }
                });
    }

    private void oneTapSignIn() {
        // Configure One Tap UI
        BeginSignInRequest oneTapRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.default_web_client_id))
                                .setFilterByAuthorizedAccounts(true)
                                .build()
                )
                .build();

        // Display the One Tap UI
        signInClient.beginSignIn(oneTapRequest)
                .addOnSuccessListener(beginSignInResult -> launchSignIn(beginSignInResult.getPendingIntent()))
                .addOnFailureListener(e -> {
                    showError(e.getMessage());
                });
    }

    private void signIn() {
        GetSignInIntentRequest signInRequest = GetSignInIntentRequest.builder()
                .setServerClientId(getString(R.string.default_web_client_id))
                .build();

        signInClient.getSignInIntent(signInRequest)
                .addOnSuccessListener(this::launchSignIn)
                .addOnFailureListener(e -> {
                    showError(e.getMessage());
                });
    }

    private void showError(String m) {
        Snackbar.make(requireActivity().findViewById(R.id.container_main_activity), "Errore nel login", Snackbar.LENGTH_LONG)
                .setAction(R.string.ok, sview -> {
                })
                .show();
        System.err.println(m);
    }

    private void launchSignIn(PendingIntent pendingIntent) {
        try {
            IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(pendingIntent)
                    .build();
            signInLauncher.launch(intentSenderRequest);
        } catch (Exception e) {
        }
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

}