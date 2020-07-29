package com.example.freshvegis;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshvegis.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Register_user extends AppCompatActivity {


    private TextView text;

    private EditText fname,email,phone,pass,conpass;
    private ProgressBar progressBar;
    private Button forword;



    private FirebaseAuth firebaseAuth;

//    private FirebaseDatabase database;
//
//    private DatabaseReference myRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        text = findViewById(R.id.already);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register_user.this, MainActivity.class);


                startActivity(i);

            }
        });




        email = (EditText)findViewById(R.id.E_mail);
        pass = (EditText)findViewById(R.id.Password);
        conpass = (EditText)findViewById(R.id.Con_password);
        fname = (EditText)findViewById(R.id.F_name);
        phone = (EditText)findViewById(R.id.Phone);


        progressBar = (ProgressBar)findViewById(R.id.newprogressbar);
        forword = (Button)findViewById(R.id.Further);


        final String validPhoneNumber = getIntent().getStringExtra("MobileNumber").toString().trim();

        phone.setText(validPhoneNumber);
        phone.setVisibility(View.GONE);


        firebaseAuth = FirebaseAuth.getInstance();





        //database = FirebaseDatabase.getInstance();






        forword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String Fname = fname.getText().toString().trim();
                final String Email = email.getText().toString().trim();
                final String Phone = phone.getText().toString().trim();
                final String Password = pass.getText().toString().trim();
                final String confirmpass = conpass.getText().toString().trim();


                if (TextUtils.isEmpty(Fname)) {

                    Toast.makeText(Register_user.this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(Email)) {

                    Toast.makeText(Register_user.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(Phone)) {

                    Toast.makeText(Register_user.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(Password)) {

                    Toast.makeText(Register_user.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confirmpass)) {

                    Toast.makeText(Register_user.this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Password.length() < 6) {
                    Toast.makeText(Register_user.this, "Password Too Short", Toast.LENGTH_SHORT).show();
                }

                if (Phone.length() < 10) {
                    Toast.makeText(Register_user.this, "Mobile Number must be 10 digits", Toast.LENGTH_SHORT).show();
                }


//                DatabaseReference PhoneRef;
//
//                PhoneRef = FirebaseDatabase.getInstance().getReference().child("Users");
//
//                PhoneRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot)
//                    {
//
//                        if(snapshot.child(Phone).exists())
//                        {
//
//                            status = false;
//                            Toast.makeText(Register_user.this, "The Mobile Number is Already Registerd.", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error)
//                        {
//
//                    }
//                });




                       DatabaseReference phoneRef = FirebaseDatabase.getInstance().getReference().child("Users");
                       phoneRef.orderByChild("Phone").equalTo(Phone).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot)
                           {

                               if(!snapshot.exists())
                               {
                                   LoggingInAdmin(Fname,Email,Phone,Password,confirmpass);
                               }

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });











            }


        });



    }



    private void LoggingInAdmin(final String Fname, final String Email, final String Phone, final String Password, String confirmpass )
    {
        if (Password.equals(confirmpass))//if both the confirm-password and the main password are matches then only we will register user
        {
            progressBar.setVisibility(View.VISIBLE);


            firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(Register_user.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {


                                User user = new User(

                                        Fname,
                                        Email,
                                        Phone,
                                        Password


                                );


                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(Phone)
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            progressBar.setVisibility(View.GONE);
                                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));

                                            Intent intent = new Intent(Register_user.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("UserPhoneNumber", Phone);
                                            intent.putExtra("UserPassword", Password);
                                            startActivity(intent);

                                            Toast.makeText(Register_user.this, "Registration Completed,Just Log In", Toast.LENGTH_SHORT).show();


                                        } else {
                                            progressBar.setVisibility(View.GONE);

                                            Toast.makeText(Register_user.this, "Registration Failed !", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                            } else {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Register_user.this, "Registration Failed here !", Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });


        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(Register_user.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
        }

    }

//    private void LoggingInAdmin()
//    {
//
//        if (Password.equals(confirmpass))//if both the confirm-password and the main password are matches then only we will register user
//        {
//            progressBar.setVisibility(View.VISIBLE);
//
//
//            firebaseAuth.createUserWithEmailAndPassword(Email, Password)
//                    .addOnCompleteListener(Register_user.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//
//
//                            if (task.isSuccessful()) {
//
//
//                                User user = new User(
//
//                                        Fname,
//                                        Email,
//                                        Phone,
//                                        Password
//
//
//                                );
//
//
//                                FirebaseDatabase.getInstance().getReference("Users")
//                                        .child(Phone)
//                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        if (task.isSuccessful()) {
//
//                                            progressBar.setVisibility(View.GONE);
//                                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
//
//                                            Intent intent = new Intent(Register_user.this, MainActivity.class);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                            intent.putExtra("UserPhoneNumber", Phone);
//                                            intent.putExtra("UserPassword", Password);
//                                            startActivity(intent);
//
//                                            Toast.makeText(Register_user.this, "Registration Completed,Just Log In", Toast.LENGTH_SHORT).show();
//
//
//                                        } else {
//                                            progressBar.setVisibility(View.GONE);
//
//                                            Toast.makeText(Register_user.this, "Registration Failed !", Toast.LENGTH_SHORT).show();
//                                        }
//
//                                    }
//                                });
//
//
//                            } else {
//
//                                progressBar.setVisibility(View.GONE);
//                                Toast.makeText(Register_user.this, "Registration Failed here !", Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            // ...
//                        }
//                    });
//
//
//        } else {
//            progressBar.setVisibility(View.GONE);
//            Toast.makeText(Register_user.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
//        }
//    }


    public void GoingBack(View V)
    {
        Intent intent = new Intent (Register_user.this,MainActivity.class);
        startActivity(intent);
    }


//    SavedPhoneNumber,SavedPassword


}













