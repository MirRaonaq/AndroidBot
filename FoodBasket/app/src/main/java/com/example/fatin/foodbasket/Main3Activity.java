package com.example.fatin.foodbasket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.jar.JarEntry;

public class Main3Activity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Login;
    private Button Register;
    private TextView Info;
    private int counter = 5;
    FirebaseDatabase fBase;
    DatabaseReference dataRef;
    String TAG ="MAIN_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Name = (EditText) findViewById(R.id.etName);
        Password = (EditText) findViewById(R.id.etPassword);
        Login = (Button) findViewById(R.id.btnLogin);
        Register = (Button) findViewById(R.id.btnReg);


        Info.setText("No of attempts remaining: 5");

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main5();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



    private void validate(String userName, String userPassword){
        final  FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference user = firebaseDatabase.getReference("users_name");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String,String> user_data=dataSnapshot.getValue(Map.class);
                //Log.d(TAG, "validate: "+dataSnapshot.getValue());
                Log.d(TAG, "validate: "+user_data.get("kemokhan"));
                //Toast.makeText(Main3Activity.this,dataSnapshot.getValue().toString() , Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //String string =user.getKey();

       // String val = user.getDatabase().toString();

        /* DatabaseReference username_1 = user.child("kemokhan");
        DatabaseReference last_name =username_1.child("Last Name");
        last_name.setValue("Khan");*/

        //DatabaseReference password =username_1.child("Password");
        //first_name.setValue(userPassword);

        DatabaseReference username_2 = user.getRef().child("FatinNazat");

        //first name
       // DatabaseReference first_name =username_1.push().child("First Name");
       //first_name.setValue(user);
//        //last name
//        DatabaseReference last_name =username_1.child("Last Name").push();
//        last_name.setValue("Raymond");
//        //password
//        DatabaseReference password =username_1.child("Password").push();
//        password.setValue(userPassword);


        // user.child("Password").setValue(userPassword);
        if ((userName.equals("Mir")) && (userPassword.equals("1234"))){
            Intent intent = new Intent(Main3Activity.this,MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alert =  new AlertDialog.Builder(this);
            alert.setTitle("Invalid Credentials.\nPlease, Enter Password Again. Attemps remain:"+ String.valueOf(counter));
            alert.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            /**
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });**/
            alert.show();
            counter --;
            Info.setText("Invalid Credentials.\nPlease, Enter Password Again. Attemps remain: "+ String.valueOf(counter));
            if (counter==0){
                Info.setText("No of attemps has reached the limit: ");
            }
            if(counter == 0){
                Login.setEnabled(false);
            }
        }
    }

    public void main5(){
        Intent intent= new Intent(Main3Activity.this,Main5Activity.class);
        startActivity(intent);
    }

}
