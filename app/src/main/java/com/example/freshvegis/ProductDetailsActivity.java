package com.example.freshvegis;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.freshvegis.Model.Products;
import com.example.freshvegis.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartButton;

    private ImageView productImage;

    private ElegantNumberButton numberButton;

    private TextView productName, productPrice, productDescription;

    private String productID = "", state = "Normal" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");



        addToCartButton = (Button)findViewById(R.id.btn_cart);

        productImage = (ImageView)findViewById(R.id.detailed_image);

        numberButton = (ElegantNumberButton)findViewById(R.id.quantity);

        productName = (TextView)findViewById(R.id.product_name);

        productPrice = (TextView)findViewById(R.id.product_prc);

        productDescription = (TextView)findViewById(R.id.product_desc);






        getProductDetails(productID);

//        Add to cart button


        addToCartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {



                if(state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "You can't purchase more products until Your previous order is approved by admin !", Toast.LENGTH_LONG).show();

                }
                else
                {
                    adding_product_to_cart_list();
                }
            }
        });



    }

    @Override
    protected void onStart()
    {
        super.onStart();

        checkOrderState();
    }

    private void adding_product_to_cart_list()
    {
        String saveCurrentTime,saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(callForDate.getTime());



        final DatabaseReference cartListRef;
        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("productname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.Phone)
                .child("Products").child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {

                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.Phone)
                                    .child("Products").child(productID).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {

                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });

                        }
                    }
                });













    }


    private void getProductDetails(final String productID)
    {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                    if(dataSnapshot.exists())
                    {
                        Products products = dataSnapshot.getValue(Products.class);

                        productName.setText(products.getProductname());
                        productPrice.setText(products.getPrice());
                        productDescription.setText(products.getDescription());

                        Picasso.get().load(products.getImage()).into(productImage);






                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.our_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.item1:
                Intent intent = new Intent(this,CartActivity.class);
                startActivity(intent);
                break;

            case R.id.item3:
                Intent intent3 = new Intent(this, SearchProductsActivity.class);
                startActivity(intent3);
                break;


            case R.id.item4:
                Intent intent4 = new Intent(this, About_Devoloper.class);
                startActivity(intent4);
                break;

            case R.id.item2:
                Paper.book().destroy();
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(this, FirstScreen.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }




    private void checkOrderState()
    {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.Phone);

        orderRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot)
            {

                if(datasnapshot.exists())
                {
                    String shippingState = datasnapshot.child("state").getValue().toString();


                    if(shippingState.equals("shipped"))
                    {

                        state = "Order Shipped";


                    }
                    else if(shippingState.equals("not shipped"))
                    {

                        state = "Order Placed";


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
