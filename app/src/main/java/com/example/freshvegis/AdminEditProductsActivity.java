package com.example.freshvegis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminEditProductsActivity extends AppCompatActivity
{

    EditText editName, editPrice,editDesc;
    ImageView uploadImage;
    Button updateButton,deleteButton;

    private String productID = "";

    private DatabaseReference editProductRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_products);

        productID = getIntent().getStringExtra("pid");

        editName = (EditText)findViewById(R.id.edit_product_name);
        editPrice = (EditText)findViewById(R.id.edit_product_price);
        editDesc = (EditText)findViewById(R.id.edit_product_description);
        uploadImage = (ImageView)findViewById(R.id.edit_product_image);


        updateButton = (Button)findViewById(R.id.edit_product_btn);

        deleteButton =(Button)findViewById(R.id.delete_product_btn);

        editProductRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        displaySpecificProductInfo();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                applyChanges();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               deleteProduct();
            }
        });
    }

    private void deleteProduct()
    {
        editProductRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(AdminEditProductsActivity.this, "The product is  deleted successfully.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminEditProductsActivity.this,admin_category.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void applyChanges()
    {
        String productNewName = editName.getText().toString();
        String productNewPrice = editPrice.getText().toString();
        String productNewDescription = editDesc.getText().toString();

        if(productNewName.equals(""))
        {
            Toast.makeText(AdminEditProductsActivity.this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
        }
        else if(productNewPrice.equals(""))
        {
            Toast.makeText(AdminEditProductsActivity.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
        }
        else if(productNewDescription.equals(""))
        {
            Toast.makeText(AdminEditProductsActivity.this, "Please Enter Description", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,Object> productMap  =  new HashMap<>();



            productMap.put("pid",productID);
            productMap.put("description",productNewDescription);
            productMap.put("price",productNewPrice);
            productMap.put("productname",productNewName);


            editProductRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminEditProductsActivity.this, "Product details applied successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminEditProductsActivity.this,admin_category.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });


        }
    }

    private void displaySpecificProductInfo()
    {

        editProductRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String Pname = snapshot.child("productname").getValue().toString();
                    String Pprice = snapshot.child("price").getValue().toString();
                    String Pdescription = snapshot.child("description").getValue().toString();

                    String Pimage = snapshot.child("image").getValue().toString();

                    editName.setText(Pname);
                    editPrice.setText(Pprice);
                    editDesc.setText(Pdescription);

                    Picasso.get().load(Pimage).into(uploadImage);






                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }


        });

    }
}
