package com.example.freshvegis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Getting_Mobile_Number extends AppCompatActivity
{
    EditText GivenMobileNumber;
    ProgressBar progressBar;
    Button getOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting__mobile__number);

        GivenMobileNumber = (EditText)findViewById(R.id.GiveMobileNumber);
        progressBar = (ProgressBar)findViewById(R.id.GetPhoneProgressBar);
        getOtp = (Button)findViewById(R.id.GetOTP);

        Intent newIntent = getIntent();
        final String FromFirstScreen = newIntent.getStringExtra("fromFirstScreen");

        Intent newIntent2 = getIntent();
        final String fromMainActivity= newIntent2.getStringExtra("fromMainActivity");








        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {




                    String EnterdMobileNumber = GivenMobileNumber.getText().toString().trim();

                    if (EnterdMobileNumber.isEmpty()) {
                        Toast.makeText(Getting_Mobile_Number.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (EnterdMobileNumber.length() < 10) {
                        Toast.makeText(Getting_Mobile_Number.this, "Mobile Number must be 10 digits", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);

//                        if(FromFirstScreen.length()==0 )
//                        {
//                           not comming from firstScreen i.e from after clicking Register
//                        SendMobileNumberToCheckOtpForForgotPassword(EnterdMobileNumber);
//                        }
//                        if(fromMainActivity.length()==0)
//                        {
//                            SendMobileNumberToCheckOtpForForgotPassword(EnterdMobileNumber);
//
//                        }
//                        else
//                        {
//                            SendMobileNumberToCheckOtpForRegisterNewUser(EnterdMobileNumber);
//                        }



                        if(FromFirstScreen != null || fromMainActivity != null )
                        {
                            SendMobileNumberToCheckOtpForRegisterNewUser(EnterdMobileNumber);

                        }
                        else
                        {
                            SendMobileNumberToCheckOtpForForgotPassword(EnterdMobileNumber);
                        }

                }


            }
        });

    }

    private void SendMobileNumberToCheckOtpForRegisterNewUser(String enterdMobileNumber)
    {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Code is being sent", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Getting_Mobile_Number.this,Register_OTP_Activity.class);
        intent.putExtra("ForRegisterPurpose","ForRegisterPurpose");
        intent.putExtra("mobile",enterdMobileNumber);
        startActivity(intent);
    }

    private void SendMobileNumberToCheckOtpForForgotPassword(String enterdMobileNumber)
    {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Code is being sent", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Getting_Mobile_Number.this,Register_OTP_Activity.class);
        intent.putExtra("mobile",enterdMobileNumber);
        startActivity(intent);

    }



}
