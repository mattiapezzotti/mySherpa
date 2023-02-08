package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import it.unimib.camminatori.mysherpa.R;

public class Register_Fragment extends Fragment {

    private Button register;
    private ImageButton backButton;
    private TextInputEditText name, email, password, passwordRepeat;
    private ProgressBar loading;

    private FirebaseAuth mAuth;

    private View viewNav;

    public Register_Fragment() {
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        email = (TextInputEditText) view.findViewById(R.id.log_email);
        password = (TextInputEditText) view.findViewById(R.id.log_password);
        name = (TextInputEditText) view.findViewById(R.id.log_name);
        passwordRepeat = (TextInputEditText) view.findViewById(R.id.log_passwordRepeat);

        loading = (ProgressBar) view.findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        viewNav = this.getActivity().findViewById(R.id.nav_host_fragment);

        backButton = (ImageButton) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Navigation.findNavController(viewNav).popBackStack();
            Navigation.findNavController(this.getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.fragment_profile);
        });

        register = (Button) view.findViewById(R.id.buttonLogin);

        register.setOnClickListener(v -> {

            String emailTrim = email.getText().toString().trim();
            String passwordTrim = password.getText().toString().trim();
            String nameTrim = name.getText().toString().trim();
            String passwordRepeatTrim = passwordRepeat.getText().toString().trim();

            if (emailTrim.isEmpty()) {
                email.setError("Campo obbligatorio");
                email.requestFocus();
                return;
            }

            if (passwordTrim.isEmpty()) {
                password.setError("Campo obbligatorio");
                password.requestFocus();
                return;
            }

            if (passwordRepeatTrim.isEmpty()) {
                passwordRepeat.setError("Campo obbligatorio");
                passwordRepeat.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailTrim).matches()) {
                email.setError("Inserire mail valida");
                email.requestFocus();
                return;
            }

            if (passwordTrim.length() < 8) {
                password.setError("Lunghezza minima: 8 caratteri");
                password.requestFocus();
                return;
            }

            if (!passwordTrim.equals(passwordRepeatTrim)) {
                passwordRepeat.setError("Le password devono coincidere");
                passwordRepeat.requestFocus();
                return;
            }

            if (nameTrim.isEmpty()) {
                name.setError("Campo obbligatorio");
                name.requestFocus();
                return;
            }

            if (nameTrim.length() < 4) {
                name.setError("Inserire un nome di almeno 4 caratteri");
                name.requestFocus();
                return;
            }

            loading.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(emailTrim, passwordTrim)
                    .addOnCompleteListener(task -> {
                        loading.setVisibility(View.GONE);
                        showDialog(task.isSuccessful());
                    });
        });
    }

        private void showDialog(boolean status){
            MaterialAlertDialogBuilder m =
                    new MaterialAlertDialogBuilder(getActivity())
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle(R.string.app_name)
                            .setPositiveButton("OK", null);

            if(status) {
                m.setMessage("Registrazione effettuata correttamente");
                m.setOnDismissListener(d -> {
                    Navigation.findNavController(viewNav).popBackStack();
                    Navigation.findNavController(viewNav).navigate(R.id.fragment_profile);
                });
            }
            else {
                m.setMessage("Errore nella creazione dell'account");
            }
            m.show();
        }

}
