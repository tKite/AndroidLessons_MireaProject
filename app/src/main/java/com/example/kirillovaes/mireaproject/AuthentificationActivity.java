package com.example.kirillovaes.mireaproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kirillovaes.mireaproject.databinding.ActivityAuthentificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthentificationActivity extends AppCompatActivity {
    ActivityAuthentificationBinding binding;
    private FirebaseAuth mAuth;
    private static final String TAG = AuthentificationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthentificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        goToMain(currentUser);
    }
    // [END on_start_check_user]
    private void goToMain(FirebaseUser user) {
        if (user != null) {
            binding.statusView.setText("Успешно");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            binding.statusView.setText("Попробуйте еще");
        }
    }
    public void onCreateButton(View v) {
        createAccount(binding.emailEdit.getText().toString(), binding.passwordEdit.getText().toString());
    }
    public void onSignInButton(View v) {
        signIn(binding.emailEdit.getText().toString(), binding.passwordEdit.getText().toString());
    }
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goToMain(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure",
                                    task.getException());
                            Toast.makeText(AuthentificationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            goToMain(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goToMain(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthentificationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            goToMain(null);
                        }
                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            binding.statusView.setText(R.string.auth_failed);
                        }
// [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


}