package com.example.weecan;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PersonalProfile extends AppCompatActivity{
    private EditText name;
    private EditText UniversityID;
    private EditText PhoneNumber;
    private EditText HousePhone;
    private EditText Age;
    private TextView txt_name;
    private TextView txt_name2;
    private TextView txt_address;
    private TextView txt_more;
    private User user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LANGUAGE = "En";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
        getSupportActionBar().hide();

        user = (User) getIntent().getSerializableExtra("user");
        name = findViewById(R.id.name);
        UniversityID = findViewById(R.id.university_ID);
        PhoneNumber = findViewById(R.id.Phone_Number);
        HousePhone = findViewById(R.id.Phone_NumberH);
        Age = findViewById(R.id.ageEditText);
        txt_name = findViewById(R.id.textView9);
        txt_name2 = findViewById(R.id.textName2);
        txt_address = findViewById(R.id.address);
        txt_more = findViewById(R.id.more);


        if(loadData().equals("En")){
            TextView textView=findViewById(R.id.header);
            /*textView.setText("Wosool\nPersonal Profile");*/
            TextView textView2=findViewById(R.id.EditTextID);
            /*textView2.setText(" University ID/Employee ID");*/
            TextView textView3=findViewById(R.id.EditTextPhone);
            /*textView3.setText(" Phone Number");*/
            Button button=findViewById(R.id.save);
            button.setText("Save");
            button.setAllCaps(false);

        }



        if(user.getName()!=null){
            name.setText(user.getName());
            txt_name.setText(user.getName());
            txt_name2.setText("About "+user.getName());
        }
        if(user.getPhoneN()!=null){
            PhoneNumber.setText(user.getPhoneN());
        }
        if(user.getHousephone()!=null){
            HousePhone.setText(user.getHousephone());
        }
        if(user.getAddress()!=null){
            txt_address.setText(user.getAddress());
        }
        if(user.getMore()!=null){
            txt_more.setText(user.getMore());
        }
        if(user.getEmail()!=null){
            UniversityID.setText(user.getEmail());
        }

    }
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void save(View view){
        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .update("name",name.getText().toString());
        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .update("phoneN",PhoneNumber.getText().toString());
        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .update("Housephone",HousePhone.getText().toString());
        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .update("university_ID",UniversityID.getText().toString());
        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .update("address",txt_address.getText().toString());
        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .update("more",txt_more.getText().toString());
        if(Age.getText().toString().equals("")) {
            db.collection("users").document(FirebaseAuth.getInstance().getUid())
                    .update("age",0);
        }
        else{
            db.collection("users").document(FirebaseAuth.getInstance().getUid())
                    .update("age",Integer.parseInt(Age.getText().toString()));
        }
        if(loadData().equals("En")) {
            Toast.makeText(this,"Saved", Toast.LENGTH_SHORT).show();
        }
    }
    public String loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString(LANGUAGE, "En");
        return currentLanguage;
    }

    public void back(View view) {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
    }
}