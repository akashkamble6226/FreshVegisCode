package com.example.freshvegis;

import android.content.Intent;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshvegis.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalActivity extends AppCompatActivity
{

    private TextView CartValue;
    private EditText  nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmButton;


    private String total_amount = "";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final);


        CartValue = (TextView)findViewById(R.id.CartValue);
        nameEditText = (EditText)findViewById(R.id.shipment_name);
        phoneEditText = (EditText)findViewById(R.id.shipment_phone_no);
        addressEditText = (EditText)findViewById(R.id.shipment_address);
        cityEditText = (EditText)findViewById(R.id.shipment_City);


        confirmButton = (Button)findViewById(R.id.shipment_confirm_button);

        total_amount = getIntent().getStringExtra("Total Price");

        CartValue.setText("Your Cart Value is Rs : "+total_amount+"/-");



        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                check();


            }
        });




    }

    private void check()
    {

        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please enter your name ", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Please enter your phone number ", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please enter your address ", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please enter your city ", Toast.LENGTH_SHORT).show();

        }
        else
        {
            confirmOrder();

        }



    }

    private void confirmOrder()
    {

        final String saveCurrentTime,saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(callForDate.getTime());

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.Phone);

        HashMap<String,Object>orderMap  = new HashMap<>();


        orderMap.put("totalAmount",total_amount);
        orderMap.put("name",nameEditText.getText().toString());
        orderMap.put("phone",phoneEditText.getText().toString());
        orderMap.put("address",addressEditText.getText().toString());
        orderMap.put("city",cityEditText.getText().toString());
        orderMap.put("Date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("state","not shipped");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.Phone)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)

                                {
                                    if (task.isSuccessful())
                                    {

                                        Intent intent = new Intent(ConfirmFinalActivity.this,OrderPlaced.class);
//Avoiding to come back to this activity(Important)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();


                                    }
                                }
                            });
                }

            }
        });



    }
}
