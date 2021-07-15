package com.preethampm.mychatapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.preethampm.mychatapplication.model_class.Users;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    EditText name, emailId, password, confirmPassword;
    TextView signIn, signUp;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    CircleImageView profileImage;
    Uri imageUri;
    String imageUri1;
    ProgressDialog progressDialog;


    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        profileImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.name_of_registration);
        emailId = findViewById(R.id.email_id_of_registration);
        password = findViewById(R.id.password_of_registration);
        confirmPassword = findViewById(R.id.confirm_password_of_registration);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        signIn = findViewById(R.id.sign_in_of_registration);
        signUp = findViewById(R.id.sign_up_of_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String email = emailId.getText().toString();
                String password1 = password.getText().toString();
                String confirmPassword1 = confirmPassword.getText().toString();
                String status = "Hey there";


                if (userName.isEmpty()) {
                    name.setError("Name is empty");
                    Toast.makeText(getApplicationContext(), "Name is empty", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    emailId.setError("Email is empty");
                    Toast.makeText(getApplicationContext(), "Email is empty", Toast.LENGTH_SHORT).show();
                } else if (password1.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password is empty", Toast.LENGTH_SHORT).show();
                } else if (confirmPassword1.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Confirm Password is empty", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    emailId.setError("Invalid Email");
                    Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                } else if (password1.length() < 8) {
                    password.setError("Password must have 8 Characters");
                    confirmPassword.setError("Password must have 8 Characters");
                    Toast.makeText(getApplicationContext(), "Password must have 8 Characters", Toast.LENGTH_SHORT).show();
                } else if (!password1.equals(confirmPassword1)) {
                    password.setError("Password is not matching");
                    confirmPassword.setError("Password is not matching");
                    Toast.makeText(getApplicationContext(), "Password and Confirm Password are different", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("User").child(firebaseAuth.getUid());
                                StorageReference storageReference = firebaseStorage.getReference().child("upload").child(firebaseAuth.getUid());

                                /*
                                If image not equal to null
                                 */
                                if (imageUri != null) {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageUri1 = uri.toString();
                                                        Users users = new Users( firebaseAuth.getUid(), userName, email, imageUri1, status);
                                                        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    progressDialog.dismiss();
                                                                    Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else{
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(getApplicationContext(), "Error in creating new user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });

                                }
                                /*
                                Image equal to null
                                 */
                                else{

                                    imageUri1 = "https://firebasestorage.googleapis.com/v0/b/my-chat-application-7432a.appspot.com/o/default_image.png?alt=media&token=67fd2736-4087-49c5-b5b3-bd2663a16607";
                                    Users users = new Users(firebaseAuth.getUid(), userName, email, imageUri1, status);
                                    databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.show();
                                                Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Error in creating new user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


        /*
        Profile Image
         */
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("images/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivity(Intent.createChooser(intent, "Select Picture"));

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,10);
            }
        });



        /*
        On Clicking sign in move to login screen and finish this activity
         */
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (data != null) {
                imageUri = data.getData();
                profileImage.setImageURI(imageUri);
            }
        }
    }
}