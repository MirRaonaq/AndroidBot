package com.example.fatin.foodbasket;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppLocationServices locationServices;
    double _latitude =0.0;
    double _longitude =0.0;
    public static final int REQUEST_LOCATION=001;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationServices = new AppLocationServices(this);

        if (locationServices.getLocationIsEnable()) {
            locationServices.setLocationAvailable(false);

        } else {
            locationServices.displayLocationSetting();
          //  enableLocation();
        }

        Button button = findViewById(R.id.shareButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                main2Activity();
                //Log.d is to see if the button is actually working or not
                Log.d("FoodBasket", "Share button pressed");
            }
        });

        Button button1 = findViewById(R.id.claimButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Main4Activity();
                //Log.d is to see if the button is actually working or not
                Log.d("FoodBasket", "Claim button pressed");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(locationServices.getLocationIsEnable()){
            finish();
            startActivity(getIntent());
            locationServices.setLocationAvailable(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (locationServices.getLocationIsEnable()){
            finish();
            startActivity(getIntent());
            locationServices.setLocationAvailable(false);

        }
    }

    public void main2Activity(){

        //Open the user input page or second acitivity
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);

    }
    public void Main4Activity(){
//        Log.d("FoodBasket", "Share button pressed");
        Intent intent4 = new Intent(this,Main4Activity.class);
        startActivity(intent4);

    }
}
