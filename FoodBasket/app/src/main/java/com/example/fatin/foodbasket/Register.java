package com.example.fatin.foodbasket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private static class User {

        public String email;
        public String user_name;

        public User(String use_name, String email) {
        }

    }
    private Button register_btn;
    private EditText user_name;
    private EditText user_password;
    private EditText user_pass_reEntered;
    private EditText user_email;

    FirebaseAuth firebaseAuth =null;
    static String TAG ="HELL0";
    boolean userIScreated =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        HideKeyBoard.hideKeyPad(findViewById(R.id.home_register), Register.this);

        register_btn =(Button) findViewById(R.id.registerBtn);
        user_email =(EditText)findViewById(R.id.email);
        user_password=(EditText)findViewById(R.id.password);
        firebaseAuth=FirebaseAuth.getInstance();
        register_btn.setOnClickListener(this);
        user_name =(EditText)findViewById(R.id.username);
       // user_pass_reEntered =(EditText) findViewById(R.id.reEnterPassword);

        //final String pass = user_pass_reEntered.getText().toString().trim();
        final String iniPass =user_password.getText().toString().trim();

    /*    user_pass_reEntered.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                    if (!pass.equals(iniPass)){
                        user_pass_reEntered.setBackgroundResource(R.drawable.edit_text_password);
                    }else {
                        user_pass_reEntered.setBackgroundResource(R.drawable.edit_text_border);
                    }

                }else {
                    if (pass.equals(iniPass)){
                        user_pass_reEntered.setBackgroundResource(R.drawable.edit_text_border);

                    }
                }
            }

        });*/
    }

    @Override
    public void onClick(View view) {
        String _user_email = user_email.getText().toString();
        String _password = user_password.getText().toString();
        String _username = user_name.getText().toString();


        registerUser(_user_email,_password,_username);
    }

    private void registerUser(final String userEmail,final String userPassword, final String userName) {

        if(ValidateFieldInput.fieldsNotEmpty(userEmail,userPassword,userName)){
            firebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        userIScreated=true;
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference ref = database.getReference("users");
                        DatabaseReference usersRef = ref.child(userName+"/email");
                        usersRef.setValue(userEmail);
                        comfirmEnteredData(firebaseAuth.getCurrentUser(), userName);
                        Intent intent = new Intent(Register.this, Main3Activity.class);

                    }else {
                        user_email.setText("");
                        user_password.setText("");
                        Toast.makeText(Register.this,user_name+", there was an error in your account creation.", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }else {
           Toast.makeText(Register.this,"Input field cannot be empty.", Toast.LENGTH_LONG).show();

       }
    }

    private boolean checkIfAcountAlreadyExist(final String userEmail, final String userName) {
        boolean crated =false;
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("users").child(userEmail);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                   // created=true;

                }catch (Exception e){
                    DatabaseReference username_1 = user.child(userName);
                    DatabaseReference ename =username_1.child("email");
                    ename.setValue(userEmail);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }

    private void comfirmEnteredData(FirebaseUser currentUser, String userName) {

        //String userName = currentUser.getDisplayName();
        String userEmail =currentUser.getEmail();

        findViewById(R.id.password).setVisibility(View.GONE);
        findViewById(R.id.username).setVisibility(View.GONE);
        findViewById(R.id.registerBtn).setVisibility(View.GONE);
        findViewById(R.id.email).setVisibility(View.GONE);




        Toast.makeText(this,"Account was created successfully",Toast.LENGTH_LONG).show();



    }
}
