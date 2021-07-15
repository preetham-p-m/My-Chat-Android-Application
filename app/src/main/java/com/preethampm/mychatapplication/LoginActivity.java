package com.preethampm.mychatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView signIn, signUp;
    EditText emailId, password;
    FirebaseAuth firebaseAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        signIn = findViewById(R.id.sign_in_of_login);
        signUp = findViewById(R.id.sign_up_of_login);
        emailId = findViewById(R.id.email_id_of_login);
        password = findViewById(R.id.password_of_login);

        firebaseAuth = FirebaseAuth.getInstance();



        /*
        On clicking sign in button check with firebase and login
         */
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String password1 = password.getText().toString();

                /*
                Email and Password authentication
                 */
                if(email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Email is empty", Toast.LENGTH_SHORT).show();
                }else if(password1.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Password is empty", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)){
                    emailId.setError("Invalid Email");
                    Toast.makeText(getApplicationContext(), "Email is Incorrect", Toast.LENGTH_SHORT).show();
                }else if(password1.length() <8){
                    password.setError("Password must have 8 Characters");
                    Toast.makeText(getApplicationContext(), "Password must have 8 Characters", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Email or Password is Incorrect",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });



        /*
        On Clicking sign up move to registration activity
         */
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}