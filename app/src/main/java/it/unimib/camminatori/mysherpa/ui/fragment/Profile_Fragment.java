package it.unimib.camminatori.mysherpa.ui.fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

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
import com.squareup.picasso.Picasso;

import it.unimib.camminatori.mysherpa.R;

public class Profile_Fragment extends Fragment {

    private Button logout, register, login, settings, googleLogin;
    private MaterialTextView username;
    private View viewNav;

    private SignInClient signInClient;
    private String userID;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ImageView propic;

    private NavController navController;

    public Profile_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null) {
            user = mAuth.getCurrentUser();
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

        viewNav = this.getActivity().findViewById(R.id.nav_host_fragment);

        signInClient = Identity.getSignInClient(requireContext());

        register = view.findViewById(R.id.button_register);
        login = view.findViewById(R.id.button_login);
        settings = view.findViewById(R.id.iconButtonSettings);
        googleLogin = view.findViewById(R.id.register_google);
        username = view.findViewById(R.id.nameHolder);
        logout = view.findViewById(R.id.button_logout);
        propic = view.findViewById(R.id.profilePicture);

        logout.setOnClickListener(view1 -> {
            new MaterialAlertDialogBuilder(getActivity())
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle("Logout")
                    .setPositiveButton("Si", (dialog, which) -> signOut())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .setMessage("Sicuro di voler effettuare il logut?")
                    .show();
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
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            logout.setEnabled(true);
            googleLogin.setEnabled(false);
            register.setEnabled(false);
            login.setEnabled(false);
            username.setText(currentUser.getEmail());
            Picasso.get().load(currentUser.getPhotoUrl()).into(propic);
        } else {
            logout.setEnabled(false);
            googleLogin.setEnabled(true);
            register.setEnabled(true);
            login.setEnabled(true);
            username.setText("No current user logged in");
            propic.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_account_circle_24));
        }
    }

    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        updateUI(user);
    }

    private void signOut() {
        mAuth.signOut();

        signInClient.signOut().addOnCompleteListener(requireActivity(),
                task -> updateUI(null));
    }

    private void showError(String m) {
        Snackbar.make(requireActivity().findViewById(R.id.container_main_activity), "Errore nel login", Snackbar.LENGTH_LONG)
                .setAction(R.string.ok, sview -> {
                })
                .show();
        System.err.println(m);
    }

    private void handleSignInResult(Intent data) {
        try {
            SignInCredential credential = signInClient.getSignInCredentialFromIntent(data);
            String idToken = credential.getGoogleIdToken();
            firebaseAuthWithGoogle(idToken);
        } catch (ApiException e) {
            showError(e.getMessage());
        }
    }

    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> handleSignInResult(result.getData())
    );

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

    private void signIn(){
        GetSignInIntentRequest signInRequest = GetSignInIntentRequest.builder()
                .setServerClientId(getString(R.string.default_web_client_id))
                .build();

        signInClient.getSignInIntent(signInRequest)
                .addOnSuccessListener(this::launchSignIn)
                .addOnFailureListener(e -> {
                });
    }

    private void launchSignIn(PendingIntent pendingIntent) {
        try {
            IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(pendingIntent)
                    .build();
            signInLauncher.launch(intentSenderRequest);
        } catch (Exception e) {
        }
    }

}