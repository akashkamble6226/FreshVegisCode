package com.example.freshvegis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Register_OTP_Activity extends AppCompatActivity
{

    private Button verify_btn;
    private EditText Code_Enterd_By_User;
    private ProgressBar progressBar;

    String  verificatinCode;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__otp_);

        verify_btn = (Button)findViewById(R.id.Verify);
        Code_Enterd_By_User = (EditText)findViewById(R.id.Otp);
        progressBar = (ProgressBar)findViewById(R.id.otpProgressBar);


        final String phoneNumber = getIntent().getStringExtra("mobile");




//        first
        Send_Verification_Code_To_User(phoneNumber);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String Enterd_code = Code_Enterd_By_User.getText().toString();

                if(Enterd_code.length()< 6)
                {
                    Toast.makeText(Register_OTP_Activity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Enterd_code.isEmpty())
                {
                    Toast.makeText(Register_OTP_Activity.this, "Please Enter OPT", Toast.LENGTH_SHORT).show();
                    return;
                }


                //don't want to allow user to enter otp from other devices

                //verify_Code(Enterd_code);

                ShowErrorMessege();

            }
        });





    }

    private void ShowErrorMessege()
    {
        Toast.makeText(this, "The mobile number should be yours!", Toast.LENGTH_SHORT).show();
        return;
    }


    private void Send_Verification_Code_To_User(String phoneNumber)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

//        this method is for , if the code is enterd from diffrent devise by the user
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
        {
            super.onCodeSent(s, forceResendingToken);
            verificatinCode = s;
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s)
        {
            super.onCodeAutoRetrievalTimeOut(s);
            //Toast.makeText(Register_OTP_Activity.this, "OTP Validity Expired", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
        {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null)
            {
                progressBar.setVisibility(View.VISIBLE);
                verify_Code(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(Register_OTP_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verify_Code(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificatinCode,code);


        Sign_In_Using_Mobile(credential);




    }

    private void Sign_In_Using_Mobile(PhoneAuthCredential credential)
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                       if (task.isSuccessful())
                       {

                           final String ForRegisterPurpose = getIntent().getStringExtra("ForRegisterPurpose");
                           final String phoneNumber = getIntent().getStringExtra("mobile");

                           if(ForRegisterPurpose == null )
                           {
                               progressBar.setVisibility(View.GONE);
                               Intent intent = new Intent(Register_OTP_Activity.this,Password_reset.class);
                               intent.putExtra("MobileNumber",phoneNumber);
                               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(intent);
                           }
                           else
                           {
                               progressBar.setVisibility(View.GONE);
                               Intent intent = new Intent(Register_OTP_Activity.this,Register_user.class);
                               intent.putExtra("MobileNumber",phoneNumber);
                               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(intent);
                           }


                       }
                       else
                       {
                           progressBar.setVisibility(View.GONE);
                           Toast.makeText(Register_OTP_Activity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();


                       }
                    }
                });
    }


}
