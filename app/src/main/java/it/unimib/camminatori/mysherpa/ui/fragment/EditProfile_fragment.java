package it.unimib.camminatori.mysherpa.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.camminatori.mysherpa.R;

public class EditProfile_fragment extends Fragment {

    private ImageButton backArrow;
    private Button modImage, modProfile;
    private TextInputEditText name, mail, password;

    public EditProfile_fragment(){
        //required public empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        name = (TextInputEditText) view.findViewById(R.id.nameNew);
        mail = (TextInputEditText) view.findViewById(R.id.mailNew);
        password = (TextInputEditText) view.findViewById(R.id.pwNew);

        backArrow = (ImageButton) view.findViewById(R.id.backButton);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO action verso Profile_Fragment
            }
        });

        modImage = (Button) view.findViewById(R.id.buttonModPicture);
        modImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO metodo cambio immagine
            }
        });

        modProfile = (Button) view.findViewById(R.id.buttonModUser);
        modProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetUser();
            }
        });

    }

    private void resetUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mailTrim = mail.getText().toString().trim();
        String passwordTrim = password.getText().toString().trim();
        String nameTrim = name.getText().toString().trim();

        user.updatePassword(passwordTrim)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "User password updated", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        user.updateEmail(mailTrim)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "User email address updated", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

}
