package com.example.weecan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class Login extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LANGUAGE = "En";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        if (mAuth.getCurrentUser() != null) {
            goToHomePage();
            finish();
        }
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        if(loadData().equals("En")){
            emailEditText.setHint("Email");
            //emailEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.person, 0, 0, 0);
            passwordEditText.setHint("Password");
            //passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, 0, 0);
            passwordEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            Button button1=findViewById(R.id.login);
            TextView button2=findViewById(R.id.signUp);
            button1.setAllCaps(false);
            button2.setAllCaps(false);
            button1.setText("Login");
            button2.setText("Sign Up");

        }
    }


    public void LogIn(View view) {
        if(emailEditText.getText().toString().trim().length() == 0){
            if(loadData().equals("En")) {
                Toast.makeText(this,"Email is empty", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this,"Error, Try Again", Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwordEditText.getText().toString().trim().length() <6){
            if(loadData().equals("En")) {
                Toast.makeText(this,"Password is less then 6", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this,"Error, Try Again", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if we can log in the user
        mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(),passwordEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            goToHomePage();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            if(loadData().equals("En")) {
                                Toast.makeText(getApplicationContext(),"Email or password is wrong", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Error, Try Again", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void goToHomePage() {
        // Move to next Activity
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
        finish();
    }

    public String loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString(LANGUAGE, "En");
        return currentLanguage;
    }
}
