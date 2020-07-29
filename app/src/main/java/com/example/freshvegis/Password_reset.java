package com.example.freshvegis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshvegis.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Password_reset extends AppCompatActivity {

    EditText MobileNumber,NewPassword,ConfirmPassword;
    Button button;
    ProgressBar progressBar;

    private DatabaseReference passResetRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        MobileNumber = (EditText)findViewById(R.id.mobile);
        NewPassword = (EditText)findViewById(R.id.newPassword);
        ConfirmPassword = (EditText)findViewById(R.id.confirmPassword);


        button = (Button)findViewById(R.id.reset);
        progressBar = (ProgressBar)findViewById(R.id.progress);


        String VerifiedPhone = getIntent().getStringExtra("MobileNumber").toString().trim();

        MobileNumber.setText(VerifiedPhone);
        MobileNumber.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final String mobileNumber = MobileNumber.getText().toString().trim();
                final String newPassword = NewPassword.getText().toString().trim();
                String confirmPassword = ConfirmPassword.getText().toString().trim();


                if(mobileNumber.equals(""))
                {
                    Toast.makeText(Password_reset.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(newPassword.equals(""))
                {
                    Toast.makeText(Password_reset.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(confirmPassword.equals(""))
                {
                    Toast.makeText(Password_reset.this, "Please confirm Password", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (newPassword.length()<6)
                {
                    Toast.makeText(Password_reset.this, "Password Too Short", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                if(newPassword.equals(confirmPassword))
                {


                    passResetRef = FirebaseDatabase.getInstance().getReference();

                    passResetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            if(snapshot.child("Users").child(mobileNumber).exists())
                            {
                                User userData = snapshot.child("Users").child(mobileNumber).getValue(User.class);

                                if(userData.Phone.equals(mobileNumber))
                                {
                                    progressBar.setVisibility(View.GONE);
                                    passResetRef.child("Users").child(mobileNumber).child("Password").setValue(newPassword);
                                    Toast.makeText(Password_reset.this, "Password Reset Done.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Password_reset.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                                else
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Password_reset.this, "Incorrect Mobile Number", Toast.LENGTH_SHORT).show();

                                }

                            }
                            else
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Password_reset.this, "User Not Found", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {

                        }
                    });
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Password_reset.this, "Both password should be same", Toast.LENGTH_SHORT).show();
                }


//
//                else {
//
//                    progressBar.setVisibility(View.VISIBLE);
//
//                    firebaseAuth.sendPasswordResetEmail(username).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//
//                            if(task.isSuccessful())
//                            {
//                                progressBar.setVisibility(View.GONE);
//                                Toast.makeText(Password_reset.this, "Password Reset Link has been Sent.", Toast.LENGTH_SHORT).show();
//                                finish();
//                                startActivity(new Intent(Password_reset.this,MainActivity.class));
//                            }else {
//
//                                progressBar.setVisibility(View.GONE);
//                                Toast.makeText(Password_reset.this, "Enter Registerd Email Address", Toast.LENGTH_SHORT).show();
//
//                            }
//
//                        }
//                    });
//                }

            }
        });

    }


    public void GoingBack(View V)
    {
        Intent intent = new Intent (Password_reset.this,MainActivity.class);
        startActivity(intent);
    }




}
