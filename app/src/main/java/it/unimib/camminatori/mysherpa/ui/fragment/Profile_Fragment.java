package it.unimib.camminatori.mysherpa.ui.fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.User;
import it.unimib.camminatori.mysherpa.viewmodel.Firebase_ViewModel;

public class Profile_Fragment extends Fragment {

    private Button logout, register, login, settings, googleLogin;
    private MaterialTextView username, kmText;
    private View viewNav;

    private String userID;
    private ImageView propic;

    private Firebase_ViewModel firebase_viewModel;

    private SignInClient signInClient;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;

    private NavController navController;

    public Profile_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        firebase_viewModel = new Firebase_ViewModel();

        mAuth = firebase_viewModel.getmAuth();
        mDatabase = firebase_viewModel.getmDatabase();
        mReference = firebase_viewModel.getmReference();
        signInClient = firebase_viewModel.getSignInClient(getContext());

        if (mAuth.getCurrentUser() != null) {
            user = mAuth.getCurrentUser();
            userID = user.getUid();
        }

        viewNav = this.getActivity().findViewById(R.id.nav_host_fragment);

        register = view.findViewById(R.id.button_register);
        login = view.findViewById(R.id.button_login);
        settings = view.findViewById(R.id.iconButtonSettings);
        googleLogin = view.findViewById(R.id.register_google);
        username = view.findViewById(R.id.nameHolder);
        kmText = view.findViewById(R.id.kmText);
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

    @Override
    public void onResume() {
        super.onResume();
        updateUI(mAuth.getCurrentUser());
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {

            mReference.child(currentUser.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User user = task.getResult().getValue(User.class);
                    kmText.setText(user.getKmTot() + " km");
                    username.setText(user.getName());
                }
            });

            logout.setEnabled(true);
            googleLogin.setEnabled(false);
            register.setEnabled(false);
            login.setEnabled(false);
            Picasso.get().load(currentUser.getPhotoUrl()).into(propic);
            kmText.setVisibility(View.VISIBLE);
        } else {
            logout.setEnabled(false);
            googleLogin.setEnabled(true);
            register.setEnabled(true);
            login.setEnabled(true);
            username.setText("No current user logged in");
            propic.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_account_circle_24));
            kmText.setVisibility(View.INVISIBLE);
        }
    }

    private void signOut() {
        mAuth.signOut();

        signInClient.signOut().addOnCompleteListener(requireActivity(),
                task -> updateUI(null));
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        firebase_viewModel.writeNewUser(
                                user.getUid(), user.getEmail(), user.getEmail().split("@")[0]
                        );
                        updateUI(user);
                    }
                });
    }

    private void signIn() {
        GetSignInIntentRequest signInRequest = firebase_viewModel.getSignInIntentRequest(getString(R.string.default_web_client_id));

        signInClient.getSignInIntent(signInRequest)
                .addOnSuccessListener(this::launchSignIn);
    }

    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> handleSignInResult(result.getData())
    );

    private void launchSignIn(PendingIntent pendingIntent) {
        IntentSenderRequest intentSenderRequest =
                new IntentSenderRequest.Builder(pendingIntent).build();
        signInLauncher.launch(intentSenderRequest);
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

    private void showError(String m) {
        Snackbar.make(requireActivity().findViewById(R.id.container_main_activity), "Errore nel login", Snackbar.LENGTH_LONG)
                .setAction(R.string.ok, sview -> {
                })
                .show();
        System.err.println(m);
    }

}