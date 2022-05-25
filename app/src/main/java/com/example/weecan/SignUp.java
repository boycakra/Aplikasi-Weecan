package com.example.weecan;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import java.util.Date;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText emailEditText;
    private EditText name;
    private EditText passwordEditText;
    private EditText phoneN;
    LoadingDialog loadingDialog;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LANGUAGE = "En";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        emailEditText = findViewById(R.id.emailEditText);
        loadingDialog=new LoadingDialog(SignUp.this);
        name = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneN = findViewById(R.id.name2);

        if(loadData().equals("En")){
            emailEditText.setHint("Email");
            //emailEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.person, 0, 0, 0);
            passwordEditText.setHint("Password");
           // passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, 0, 0);
            name.setHint("Name");
            //name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.id, 0, 0, 0);
            phoneN.setHint("Nurse Contact +62");
           //phoneN.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phone, 0, 0, 0);
            Button button1=findViewById(R.id.signUpBtn);
            button1.setText("Sign Up");
            button1.setAllCaps(false);
            passwordEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            phoneN.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }
    }

    public void signUp(View view){
        if(emailEditText.getText().toString().trim().length() == 0) {
            if(loadData().equals("En")) {
                Toast.makeText(this,"Email is empty", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Error, Try Again", Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwordEditText.getText().toString().trim().length() <6) {
            if(loadData().equals("En")) {
                Toast.makeText(this,"Password less then 6", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Error, Try Again", Toast.LENGTH_SHORT).show();
            return;
        }
        if(name.getText().toString().trim().length() ==0) {
            if(loadData().equals("En")) {
                Toast.makeText(this,"Name is empty", Toast.LENGTH_SHORT).show();
            }
            else
                    Toast.makeText(this, "Error, Try Again", Toast.LENGTH_SHORT).show();
            return;
        }
        if(phoneN.getText().toString().trim().length() ==0) {
            if(loadData().equals("En")) {
                Toast.makeText(this,"Phone number is empty", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Error, Try Again", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.startLoadingDialog();
        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            User user=new User(name.getText().toString(),emailEditText.getText().toString(),phoneN.getText().toString());
                            user.setPassword(passwordEditText.getText().toString());
                            db.collection("users").document(mAuth.getUid()).set(user);

                            if(loadData().equals("En")) {
                                Toast.makeText(SignUp.this,"Signed up", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(SignUp.this, "Success", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                            Intent intent = new Intent(SignUp.this, Homepage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            loadingDialog.dismissDialog();
                            if(loadData().equals("En")) {
                                Toast.makeText(SignUp.this,"Email is wrong or used", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(SignUp.this,"Error, Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public String loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString(LANGUAGE, "En");
        return currentLanguage;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        finish();
    }
}