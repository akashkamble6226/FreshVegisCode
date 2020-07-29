package com.example.freshvegis;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshvegis.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity
{

    TextView text;

    EditText EnterPhoneNo, EnterPassword;
    ProgressBar progressBar;
    Button login;
    CheckBox forRemebering;

    public String From_New_User_Registration_Phone = "";
    public String From_New_User_Registration_Password = "";

    private FirebaseAuth firebaseAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        text = findViewById(R.id.donthave);

        text.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Getting_Mobile_Number.class);
                i.putExtra("fromMainActivity","fromMainActivity");
                startActivity(i);

            }
        });

        EnterPhoneNo = (EditText) findViewById(R.id.EnterE);
        EnterPassword = (EditText) findViewById(R.id.EnterPass);
        progressBar = (ProgressBar) findViewById(R.id.newprogressbar2);
        login = (Button) findViewById(R.id.login);
        forRemebering = (CheckBox)findViewById(R.id.rememberMe);

        firebaseAuth = FirebaseAuth.getInstance();




        From_New_User_Registration_Phone = getIntent().getStringExtra("UserPhoneNumber");
        From_New_User_Registration_Password = getIntent().getStringExtra("UserPassword");

        if(From_New_User_Registration_Phone != "" && From_New_User_Registration_Password !="")
        {
            EnterPhoneNo.setText(From_New_User_Registration_Phone);
            EnterPassword.setText(From_New_User_Registration_Password);
            forRemebering.setChecked(true);
//            Logging_In_User(From_New_User_Registration_Phone,From_New_User_Registration_Password);

        }

        else
        {
            ;
        }






        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                String phone = EnterPhoneNo.getText().toString().trim();
                String Password = EnterPassword.getText().toString().trim();


                if (TextUtils.isEmpty(phone)) {

                    Toast.makeText(MainActivity.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(Password)) {

                    Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Password.length() < 6) {
                    Toast.makeText(MainActivity.this, "Password Too Short", Toast.LENGTH_SHORT).show();
                    return;
                }

                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    Logging_In_User(phone,Password);

                }



            }
        });






    }

    private void Logging_In_User(final String phone, final String Password)
    {

        if(forRemebering.isChecked())
        {
            Paper.book().write(Prevalent.UserNameKey,phone);
            Paper.book().write(Prevalent.UserPhoneKey,Password);

        }

        DatabaseReference DataRef;
        DataRef = FirebaseDatabase.getInstance().getReference();

        DataRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {



                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    User userData = dataSnapshot.child("Users").child(phone).getValue(User.class);

                    if(userData.Phone.equals(phone))
                    {
                        if(userData.Password.equals(Password))
                        {
                            progressBar.setVisibility(View.GONE);
                            Prevalent.currentOnlineUser = userData;

                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Incorrect Mobile Number", Toast.LENGTH_SHORT).show();

                    }

                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


    public void Reset(View v)
    {
        Intent intent = new Intent(MainActivity.this,Getting_Mobile_Number.class);
        startActivity(intent);

    }

    public void Owner(View v)
    {
        Intent intent = new Intent(MainActivity.this,Owner_Login.class);
        startActivity(intent);
    }


}






