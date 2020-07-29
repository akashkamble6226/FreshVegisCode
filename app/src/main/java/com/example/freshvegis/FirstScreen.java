package com.example.freshvegis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshvegis.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class FirstScreen extends AppCompatActivity {

    Button LoginButton,RegisterButton;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        LoginButton = (Button)findViewById(R.id.loginButton);
        RegisterButton = (Button) findViewById(R.id.registerButton);

        progressBar = (ProgressBar)findViewById(R.id.firstScreenProgressBar);


        Paper.init(this);


        String SavedPhoneNUmber = Paper.book().read(Prevalent.UserNameKey);
        String SavedPassword = Paper.book().read(Prevalent.UserPhoneKey);
        String SavedStatus = Paper.book().read(Prevalent.ForAdmin);





        if(SavedPhoneNUmber != "" && SavedPassword != "")
        {
            if(!TextUtils.isEmpty(SavedPhoneNUmber) && !TextUtils.isEmpty(SavedPassword))
            {
                LoginButton.setVisibility(View.GONE);
                RegisterButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                Allow_Access_To_Already_Logged_In_User(SavedPhoneNUmber,SavedPassword);


            }
        }

        if(SavedPhoneNUmber != "" && SavedPassword != "" && SavedStatus!="")
        {
            if(!TextUtils.isEmpty(SavedPhoneNUmber) && !TextUtils.isEmpty(SavedPassword)&& !TextUtils.isEmpty(SavedStatus))
            {
                if(SavedStatus.equals("Yes"))
                {
                    LoginButton.setVisibility(View.GONE);
                    RegisterButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    Allow_Access_To_Already_Logged_In_Admin(SavedPhoneNUmber, SavedPassword);
                }

            }
        }







        LoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (FirstScreen.this,MainActivity.class);
                startActivity(intent);
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (FirstScreen.this,Getting_Mobile_Number.class);
                startActivity(intent);
            }
        });


    }

    private void Allow_Access_To_Already_Logged_In_Admin(final String savedPhoneNUmber, final String savedPassword)
    {
        DatabaseReference DataRef;
        DataRef = FirebaseDatabase.getInstance().getReference();

       DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot)
           {

               if(snapshot.child("Users").child(savedPhoneNUmber).exists())
               {
                   User userData = snapshot.child("Users").child(savedPhoneNUmber).getValue(User.class);

                   if(userData.Phone.equals(savedPhoneNUmber))
                   {
                       if(userData.Password.equals(savedPassword))
                       {
                           progressBar.setVisibility(View.GONE);
                           Prevalent.currentOnlineUser = userData;
                           startActivity(new Intent(getApplicationContext(), admin_category.class));
                           Toast.makeText(FirstScreen.this, "Already Logged In, Welcome Back Admin!", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           progressBar.setVisibility(View.GONE);
                           Toast.makeText(FirstScreen.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                       }
                   }
                   else
                   {
                       progressBar.setVisibility(View.GONE);
                       Toast.makeText(FirstScreen.this, "Incorrect Username", Toast.LENGTH_SHORT).show();


                   }

               }
               else
               {
                   progressBar.setVisibility(View.GONE);
                   Toast.makeText(FirstScreen.this, "User Not Found", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(getApplicationContext(), MainActivity.class));


               }


           }


           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }




    private void Allow_Access_To_Already_Logged_In_User(final String SavedPhoneNUmber, final String SavedPassword)
    {
        DatabaseReference DataRef;
        DataRef = FirebaseDatabase.getInstance().getReference();

        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {


                if(dataSnapshot.child("Users").child(SavedPhoneNUmber).exists())
                {
                    User userData = dataSnapshot.child("Users").child(SavedPhoneNUmber).getValue(User.class);

                    if(userData.Phone.equals(SavedPhoneNUmber))
                    {
                        if(userData.Password.equals(SavedPassword))
                        {

                            progressBar.setVisibility(View.GONE);
                            Prevalent.currentOnlineUser = userData;
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            Toast.makeText(FirstScreen.this, "Already Logged In, Welcome Back", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(FirstScreen.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FirstScreen.this, "Incorrect Username", Toast.LENGTH_SHORT).show();


                    }

                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FirstScreen.this, "User Not Found", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }









}
