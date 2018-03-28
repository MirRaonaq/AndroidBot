package com.example.fatin.foodbasket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    AppLocationServices locationServices;
    double _latitude =0.0;
    double _longitude =0.0;
    public static final int REQUEST_LOCATION=001;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    Button shareBtn =null;
    Button logoutBtn=null;
    Button claimeBtn=null;

    TextView profile;
    FirebaseUser user;


    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        profile =(TextView)findViewById(R.id.profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String currUser = user.getEmail();
        String u = currUser.split("@")[0];
        profile.setText(u);
        //Toast.makeText(this,"current user is "+user,Toast.LENGTH_LONG).show();


        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                  //  startActivity(new Intent(MainActivity.this, MainActivity.class));
                } else if (i == 2) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(MainActivity.this, Main3Activity.class));
<<<<<<< HEAD
                } else if (i ==3){
                    startActivity(new Intent(MainActivity.this, Main7Activity.class));
=======
                }else if (i==3){
                    deactivateAccount();


>>>>>>> kemokhan
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        locationServices = new AppLocationServices(this);

        if (locationServices.getLocationIsEnable()) {
            locationServices.setLocationAvailable(false);

        } else {
            locationServices.displayLocationSetting();
        }

        shareBtn= (Button)findViewById(R.id.shareButton);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main2Activity();
                Log.d("FoodBasket", "Share button pressed");
            }
        });

        claimeBtn = findViewById(R.id.claimButton);
        claimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Main4Activity();
                Log.d("FoodBasket", "Claim button pressed");
            }
        });
    }
    private void deactivateAccount() {

       //final FirebaseUser uid = user;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        if (user !=null){
            progressDialog.setMessage("Deleting account...");
            progressDialog.show();
            try {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            startActivity(new Intent(MainActivity.this, Main3Activity.class));
                            Toast.makeText(MainActivity.this,"Deactivation was successful",Toast.LENGTH_LONG).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();

                    }
                });
            }catch (Exception e){
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(MainActivity.this,"No user is loggin!!",Toast.LENGTH_LONG).show();

        }


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
        Intent intent = new Intent(this,ImageCapture.class);
        startActivity(intent);

    }
    public void Main4Activity(){
        Intent intent4 = new Intent(this,PostedImages.class);
        startActivity(intent4);

    }
}
