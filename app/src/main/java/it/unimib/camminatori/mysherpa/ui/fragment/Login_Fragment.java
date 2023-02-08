package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.User;

public class Login_Fragment extends Fragment {

    private Button login;
    private ImageButton backButton;
    private MaterialTextView forgotPW;
    private TextInputEditText name, email, password;
    private ProgressBar loading;

    private FirebaseAuth mAuth;

    private View viewNav;

    public Login_Fragment() {
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        email = (TextInputEditText) view.findViewById(R.id.log_email);
        password = (TextInputEditText) view.findViewById(R.id.log_password);

        loading = (ProgressBar) view.findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        viewNav = this.getActivity().findViewById(R.id.nav_host_fragment);

        backButton = (ImageButton) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Navigation.findNavController(viewNav).popBackStack();
            Navigation.findNavController(viewNav).navigate(R.id.fragment_profile);
        });

        login = (Button) view.findViewById(R.id.buttonLogin);

        login.setOnClickListener(view1 -> {

            String emailTrim = email.getText().toString().trim();
            String passwordTrim = password.getText().toString().trim();

            if (emailTrim.isEmpty()) {
                email.setError("Campo obbligatorio!");
                email.requestFocus();
                return;
            }

            if (passwordTrim.isEmpty()) {
                password.setError("Campo obbligatorio!");
                password.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailTrim).matches()) {
                email.setError("Inserire mail valida");
                email.requestFocus();
                return;
            }

            if (passwordTrim.length() < 8) {
                password.setError("Inserire almeno 8 caratteri");
                password.requestFocus();
                return;
            }

            loading.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(emailTrim, passwordTrim).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Navigation.findNavController(viewNav).popBackStack();
                    Navigation.findNavController(viewNav).navigate(R.id.fragment_profile);
                    loading.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), "Errore nel login! Verificare le credenziali", Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);

                }
            });
        });

        /*forgotPW = (MaterialTextView) view.findViewById(R.id.PWdimenticata);
        forgotPW.setOnClickListener(this);*/

    }

}