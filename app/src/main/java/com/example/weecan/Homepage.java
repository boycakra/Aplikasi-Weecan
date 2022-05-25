package com.example.weecan;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Homepage extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User user;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LANGUAGE = "En";
    private long pressedTime;
    private TextView txt_name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        getSupportActionBar().hide();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        txt_name = findViewById(R.id.textView);
        if(loadData().equals("En")) {
            ImageView imageView=findViewById(R.id.imageView2);
            imageView.setContentDescription("Sign out");
        }

        getProfil();
    }

    public void signOut(View view) {
        mAuth.signOut();
        finish();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }

    public void personalProfile(View view) {
        DocumentReference userRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                Intent intent = new Intent(getApplicationContext(), PersonalProfile.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    public void getProfil() {
        DocumentReference userRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                if(user.getName() != null){
                    txt_name.setText("Welcome "+user.getName());
                }
            }
        });
    }


    public void communicate(View view) {
        Intent intent = new Intent(this, Conversation.class);
        startActivity(intent);
    }

    public void assistanceVideoCall(View view) {
        DocumentReference userRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                Intent intent = new Intent(getApplicationContext(), emergency.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }


    void getlocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            askLocationPermission();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //We have a location
                    Log.d("TAG", "onSuccess: " + location.toString());
                    Log.d("TAG", "onSuccess: " + location.getLatitude());
                    Log.d("TAG", "onSuccess: " + location.getLongitude());
                    String googleMap="http://www.google.com/maps/place/"+location.getLatitude()+","+location.getLongitude();
                    Map<String, String> data = new HashMap<>();
                    data.put("Last Location", googleMap);
                    db.collection("users").document(FirebaseAuth.getInstance().getUid()).set(data, SetOptions.merge());
                } else  {
                    Log.d("TAG", "onSuccess: Location was null...");
                }
            }
        });

        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: " + e.getLocalizedMessage() );
            }
        });
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d("TAG", "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getLastLocation();
            } else {
                //Permission not granted
            }
        }
    }

    public String loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString(LANGUAGE, "En");
        return currentLanguage;
    }
}
