package it.unimib.camminatori.mysherpa.ui.activity;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.model.User;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    private Button login, register;
    private MaterialTextView forgotPW;
    private TextInputEditText name, email, password;
    private ProgressBar loading;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        login = (Button) findViewById(R.id.buttonLogin);
        login.setOnClickListener(this);

        register = (Button) findViewById(R.id.buttonRegister);
        register.setOnClickListener(this);

        forgotPW = (MaterialTextView) findViewById(R.id.PWdimenticata);
        forgotPW.setOnClickListener(this);

        email = (TextInputEditText) findViewById(R.id.log_email);
        password = (TextInputEditText) findViewById(R.id.log_password);
        name = (TextInputEditText) findViewById(R.id.log_name);

        loading = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.buttonRegister:
                registerUser();
                break;

            case R.id.buttonLogin:
                loginUser();
                break;
        }
    }

    private void registerUser(){
        String emailTrim = email.getText().toString().trim();
        String passwordTrim = password.getText().toString().trim();
        String nameTrim = name.getText().toString().trim();

        if(emailTrim.isEmpty()){
            email.setError("Campo obbligatorio!");
            email.requestFocus();
            return;
        }

        if(passwordTrim.isEmpty()){
            password.setError("Campo obbligatorio!");
            password.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailTrim).matches()){
            email.setError("Inserire mail valida");
            email.requestFocus();
            return;
        }

        if(passwordTrim.length() < 8){
            password.setError("Lunghezza minima: 8 caratteri");
            password.requestFocus();
            return;
        }

        if(nameTrim.isEmpty()){
            name.setError("Campo obbligatorio!");
            name.requestFocus();
            return;
        }

        if(nameTrim.length() < 4){
            name.setError("Inserire un nome di almeno 4 caratteri");
            name.requestFocus();
            return;
        }

        loading.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailTrim, passwordTrim)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(emailTrim, nameTrim);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()) {
                                                Toast.makeText(StartActivity.this, "Utente registrato correttamente!", Toast.LENGTH_LONG).show();
                                                loading.setVisibility(View.GONE);

                                                startActivity(new Intent(StartActivity.this, MainActivity.class));

                                            }else{
                                                Toast.makeText(StartActivity.this, "Errore nella registrazione!", Toast.LENGTH_LONG).show();
                                                loading.setVisibility(View.GONE);

                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(StartActivity.this, "Errore nella registrazione!", Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void loginUser(){
        String emailTrim = email.getText().toString().trim();
        String passwordTrim = password.getText().toString().trim();

        if(emailTrim.isEmpty()){
            email.setError("Campo obbligatorio!");
            email.requestFocus();
            return;
        }

        if(passwordTrim.isEmpty()){
            password.setError("Campo obbligatorio!");
            password.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailTrim).matches()){
            email.setError("Inserire mail valida");
            email.requestFocus();
            return;
        }

        if(passwordTrim.length() < 8){
            password.setError("Inserire almeno 8 caratteri");
            password.requestFocus();
            return;
        }

        loading.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emailTrim, passwordTrim).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    loading.setVisibility(View.GONE);

                }else{
                    Toast.makeText(StartActivity.this, "Errore nel login! Verificare le credenziali", Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);

                }
            }
        });
    }

}
