package com.example.freshvegis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class admin_add_new_product extends AppCompatActivity {

    private ImageView Input_product_image;

    private EditText Input_product_name, Input_product_desc, Input_product_price;

    private Button Add_new_product;

    private static final int GalleryPick = 1 ;

    private Uri ImageUri;


    private String categoryName, Description, price, pname;

    private String saveCurrentDate,saveCurrentTime,productRandomeKey,downloadImageUrl;

    private StorageReference productImageRef;

    private DatabaseReference ProductInfoRef;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);


//        //catch intent info

        categoryName = getIntent().getExtras().get("Category").toString();


        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();


        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        ProductInfoRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Input_product_image = (ImageView) findViewById(R.id.select_vegetable_image);

        Input_product_name = (EditText)findViewById(R.id.vegetable_name);
        Input_product_desc = (EditText)findViewById(R.id.vegetable_description);
        Input_product_price = (EditText)findViewById(R.id.vegetable_Price);

        Add_new_product = (Button)findViewById(R.id.add_product);


        progressBar = (ProgressBar)findViewById(R.id.newprogressbar);



        Input_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                OpenGallery();
            }
        });


        Add_new_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidateProductData();
            }
        });

    }

    private void ValidateProductData()
    {

        Description = Input_product_desc.getText().toString();
        price = Input_product_price.getText().toString();
        pname = Input_product_name.getText().toString();

//Image should be selected
        if(ImageUri == null)
        {
            Toast.makeText(this, "Image Is Required", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(Description))
        {

            Toast.makeText(this, "Please Enter the Description", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pname))
        {
            Toast.makeText(this, "Please Enter the Product name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price))
        {

            Toast.makeText(this, "Please Enter Price", Toast.LENGTH_SHORT).show();
        }

        else
        {

//            storing data to firebase database
            progressBar.setVisibility(View.VISIBLE);
            storeProductInfo();

        }




    }

    private void storeProductInfo()
    {

        Calendar calender = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate =  currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime =  currentTime.format(calender.getTime());

        productRandomeKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = productImageRef.child(ImageUri.getLastPathSegment() + productRandomeKey + ".jpg" );

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                
                String ErrorMsg = e.toString();

                //Toast.makeText(admin_add_new_product.this, "Error :" + ErrorMsg, Toast.LENGTH_SHORT).show();
                
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
               // Toast.makeText(admin_add_new_product.this, "Vegetable Image Uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                       if(!task.isSuccessful())
                       {
                           throw task.getException();
                       }

                       downloadImageUrl = filePath.getDownloadUrl().toString();

                       return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {

                        downloadImageUrl = task.getResult().toString();

                        //Toast.makeText(admin_add_new_product.this, "Got Vegetable image Url Successfully", Toast.LENGTH_SHORT).show();


                        saveProductDataTODatabase();

                    }
                });
            }
        });







    }



    private void saveProductDataTODatabase()
    {
        HashMap<String,Object> productMap  =  new HashMap<>();



        productMap.put("pid",productRandomeKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("price",price);
        productMap.put("productname",pname);


        ProductInfoRef.child(productRandomeKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(admin_add_new_product.this, "Vegetable added Successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(admin_add_new_product.this,admin_category.class);
                            startActivity(intent);
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            String ErrorMess = task.getException().toString();

                            Toast.makeText(admin_add_new_product.this, "Error:"+ ErrorMess, Toast.LENGTH_SHORT).show();


                        }
                    }

                });



    }

    private void OpenGallery()
    {

        Intent GalleryIntent = new Intent();
        GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent,GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            Input_product_image.setImageURI(ImageUri);
        }
    }
}
