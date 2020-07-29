package com.example.freshvegis;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshvegis.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Owner_Login extends AppCompatActivity {


    EditText AdminPhone,Adminpass,SecredCode;
    Button logi;

    ProgressBar progressBar;
    CheckBox forRemebering;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__login);

        AdminPhone = (EditText)findViewById(R.id.EnterMobileNumber);
        Adminpass = (EditText)findViewById(R.id.EnterPass);
        SecredCode = (EditText)findViewById(R.id.EnterSecretCode);


        logi = (Button)findViewById(R.id.login);

        progressBar = (ProgressBar)findViewById(R.id.newprogressbar);
        forRemebering = (CheckBox)findViewById(R.id.Owner_rememberMe);



        logi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                final String EnterdPhoneNumber = AdminPhone.getText().toString().trim();
                final String EnterdPassword = Adminpass .getText().toString().trim();
                final String EnterdSecretCode = SecredCode.getText().toString().trim();




                if(TextUtils.isEmpty(EnterdPhoneNumber))
                {

                    Toast.makeText(Owner_Login.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty( EnterdPassword))
                {

                    Toast.makeText(Owner_Login.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (EnterdPassword.length() < 6) {
                    Toast.makeText(Owner_Login.this, "Password Too Short", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(EnterdSecretCode))
                {

                    Toast.makeText(Owner_Login.this, "Please Enter Secret Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (EnterdSecretCode.length() < 6) {
                    Toast.makeText(Owner_Login.this, "Secret Code Is Too Short", Toast.LENGTH_SHORT).show();
                    return;
                }


                else
                {
                    CheckSecretCode(EnterdPhoneNumber,EnterdPassword, EnterdSecretCode);



                        //progressBar.setVisibility(View.VISIBLE);



                }

            }
        });

    }

    private void Login_Admin(final String enterdPhoneNumber, final String enterdPassword)
    {
        if(forRemebering.isChecked())
        {
            Paper.book().write(Prevalent.UserNameKey,enterdPhoneNumber);
            Paper.book().write(Prevalent.UserPhoneKey,enterdPassword);
            Paper.book().write(Prevalent.ForAdmin,"Yes");

        }

        DatabaseReference DataRef;
        DataRef = FirebaseDatabase.getInstance().getReference();

        progressBar.setVisibility(View.VISIBLE);

        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {


                if(snapshot.child("Users").child(enterdPhoneNumber).exists())
                {
                    User userData = snapshot.child("Users").child(enterdPhoneNumber).getValue(User.class);

                    if(userData.Phone.equals(enterdPhoneNumber))
                    {
                        if(userData.Password.equals(enterdPassword))
                        {
                            progressBar.setVisibility(View.GONE);
                            Prevalent.currentOnlineUser = userData;

                            Intent intent = new Intent(Owner_Login.this,admin_category.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(Owner_Login.this, "Welcome admin! ", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Owner_Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Owner_Login.this, "Incorrect Mobile Number", Toast.LENGTH_SHORT).show();

                    }

                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Owner_Login.this, "User Not Found", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void CheckSecretCode(final String EnterdPhoneNumber,final String EnterdPassword,final String  EnterdSecretCode)
    {
        DatabaseReference CheckCodeRef;
        CheckCodeRef = FirebaseDatabase.getInstance().getReference().child("Codes");

        CheckCodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String CodeFromDatabase = snapshot.child("Code").getValue().toString();

                if(CodeFromDatabase.equals( EnterdSecretCode))
                    {

                        Login_Admin(EnterdPhoneNumber,EnterdPassword);

                    }
                else
                    {

                        Toast.makeText(Owner_Login.this, "Invalid Secret Code", Toast.LENGTH_SHORT).show();
                        return;
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


    }


    public void GoingBack(View V)
    {
        Intent intent = new Intent (Owner_Login.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



}
