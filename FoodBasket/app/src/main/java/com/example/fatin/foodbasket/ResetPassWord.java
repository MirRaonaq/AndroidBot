package com.example.fatin.foodbasket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassWord extends AppCompatActivity{

    EditText passwordReset;
    Button resetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_pass_word);

        HideKeyBoard.hideKeyPad(findViewById(R.id.home_register), ResetPassWord.this);

        passwordReset =(EditText)findViewById(R.id.resetPassword);
        resetButton =(Button)findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String email =passwordReset.getText().toString().trim();
                final ProgressDialog progressDialog = new ProgressDialog(ResetPassWord.this);
                progressDialog.setMessage("Sending..");
                progressDialog.show();
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            sendAlert();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(ResetPassWord.this,"Email not send Sucessfully", Toast.LENGTH_LONG).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    private void sendAlert() {

        findViewById(R.id.resetButton).setVisibility(View.GONE);
        findViewById(R.id.resetPassword).setVisibility(View.GONE);
        Toast.makeText(ResetPassWord.this,"Password reset email was send successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Main3Activity.class);


    }

}
