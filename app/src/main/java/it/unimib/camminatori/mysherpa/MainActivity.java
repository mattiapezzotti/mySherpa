package it.unimib.camminatori.mysherpa;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent switchActivityIntent = new Intent(this, ExploreActivity.class);
        startActivity(switchActivityIntent);
    }
}