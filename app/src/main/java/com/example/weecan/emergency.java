package com.example.weecan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.widget.TextView;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.Manifest;
import android.annotation.SuppressLint;
import java.util.List;
import java.util.Locale;

public class emergency extends AppCompatActivity implements LocationListener {
    MediaPlayer player;
    Button button_location;
    TextView textView_location;
    LocationManager locationManager;
    private User user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        user = (User) getIntent().getSerializableExtra("user");




        textView_location = findViewById(R.id.text_location);
        button_location = findViewById(R.id.button_location);

        if (ContextCompat.checkSelfPermission(emergency.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(emergency.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }



        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create method
                getLocation();
            }
        });



    }
    public void back(View view) {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,emergency.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    String text = "I Need Help  ";
    String text2 = "I Want to Eat  ";
    String text3 = "I Want go to the toilet  ";
    String text1 = " My Location ";

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(emergency.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            textView_location.setText(address);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+user.getPhoneN()+"&text="+text+address));
            startActivity(intent);


        }catch (Exception e){
            e.printStackTrace();

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }




    public void etext(View v) {
        boolean installed = isAppInstalled("com.whatsapp");

        if (installed)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+user.getPhoneN()+"&text="+ text +"&text1="+ text1 ));
            startActivity(intent);
        }
        else
        {
            Toast.makeText(emergency.this, "Whatsapp is not installed!", Toast.LENGTH_SHORT).show();
        }

    }

    public void etext2(View v) {
        boolean installed = isAppInstalled("com.whatsapp");

        if (installed)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+user.getPhoneN()+"&text="+ text2 ));
            startActivity(intent);
        }
        else
        {
            Toast.makeText(emergency.this, "Whatsapp is not installed!", Toast.LENGTH_SHORT).show();
        }

    }

    public void etext3(View v) {
        boolean installed = isAppInstalled("com.whatsapp");

        if (installed)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+user.getPhoneN()+"&text="+ text3 ));
            startActivity(intent);
        }
        else
        {
            Toast.makeText(emergency.this, "Whatsapp is not installed!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isAppInstalled(String s) {
        PackageManager packageManager = getPackageManager();
        boolean is_installed;

        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES);
            is_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed = false;
            e.printStackTrace();
        }
        return is_installed;
    }

    public void icon(View v) {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.emergency_alarm_fire_domestic);
            player.setLooping(true);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }
        player.start();
    }
    public void stop(View v) {
        stopPlayer();
    }
    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(this, "Alaram Of", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

}