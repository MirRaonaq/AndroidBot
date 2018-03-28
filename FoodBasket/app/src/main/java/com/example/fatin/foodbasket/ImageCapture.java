package com.example.fatin.foodbasket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.Manifest;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Date;


public class ImageCapture extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private Button home;
    ImageView mImageLabel;
    Uri imUrl;
    ProgressDialog progressDialog;
    String TAG ="CurrentUser";

    EditText desc;
    EditText buildName;
    EditText roomNum;
    Button btnShare;
    Context context;
    TextView postDes;
    TextView postBuildN;
    TextView postRoomN;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    private Uri uri_download;

    static final int REQUEST_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.capture_image);
        HideKeyBoard.hideKeyPad(findViewById(R.id.imageView), ImageCapture.this);

        mImageLabel =(ImageView)findViewById(R.id.imageView);
        btnShare=(Button)findViewById(R.id.buttonShare);
        buildName =(EditText)findViewById(R.id.buildingName);
        roomNum = (EditText)findViewById(R.id.roomNumber);
        desc = (EditText)findViewById(R.id.description);
/*
        postBuildN =(TextView) findViewById(R.id.postBuildNum);
        postDes =(TextView)findViewById(R.id.postDesc);
        postRoomN=(TextView)findViewById(R.id.postRoomNum) ;*/

        storageReference = FirebaseStorage.getInstance().getReference();
        //final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("photos");

       /* final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");
        DatabaseReference usersRef = ref.child(userName+"/email");
        usersRef.setValue(userEmail);*/

        if (ContextCompat.checkSelfPermission((Activity)this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    ,REQUEST_PERMISSION);
        }


        progressDialog = new ProgressDialog(this);

       btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Uploading........");
                progressDialog.show();
                if (ValidateFieldInput.fieldsNotEmpty(desc.getText().toString(), roomNum.getText().toString(),desc.getText().toString())) {
                    DatabaseReference mdata = databaseReference.push();
                    mdata.child("description").setValue(desc.getText().toString());
                    mdata.child("roomNum").setValue(roomNum.getText().toString());
                    mdata.child("buildingName").setValue(buildName.getText().toString());
                    mdata.child("image").setValue(uri_download.toString());
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    progressDialog.dismiss();

                    Intent intent = new Intent(ImageCapture.this, PostedImages.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ImageCapture.this,"All input field are required", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_PERMISSION &&grantResults.length>0){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camera_cature:
                launchCamera();
            default:
                break;
        }
        return false;
    }

    private void launchCamera() {
        Toast.makeText(this,"opening camera", Toast.LENGTH_LONG).show();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == this.RESULT_OK) {
            progressDialog.setMessage("Uploading image.....");
            progressDialog.show();
            imUrl = data.getData();
            Bundle bundle= data.getExtras();
            Log.d(TAG, "onActivityResult: "+imUrl.toString());
            // content://media/external/images/media/162500

            Bundle extras = data.getExtras();
            final Bitmap imageBitmap = (Bitmap) extras.get("data");

            Date date = new Date(System.currentTimeMillis());
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("images").child(date.toString());
            if (imUrl != null) {
                ref.putFile(imUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uri_download = taskSnapshot.getDownloadUrl();
                        // Picasso.get().load(uri_download).into(mImageLabel);
                        mImageLabel.setImageBitmap(imageBitmap);


                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();

                    }
                });
            }else {
                Toast.makeText(this,"Unsupported Sdk.Use version Sdk 21", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void main1(){
        Intent intent= new Intent(ImageCapture.this,MainActivity.class);
        startActivity(intent);
    }

}


