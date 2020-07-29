package com.example.freshvegis;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshvegis.Model.Cart;
import com.example.freshvegis.Prevalent.Prevalent;
import com.example.freshvegis.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static java.lang.Integer.valueOf;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;

    private Button nextButton;

    private TextView txtTotalAmout,txtMsg1;

//for all price in cart
    private int overallTotalPrice = 0;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);



        nextButton =(Button) findViewById(R.id.next);

        txtTotalAmout =(TextView) findViewById(R.id.total_amt);

        txtMsg1 = (TextView)findViewById(R.id.msg1);





        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {



                if(overallTotalPrice == 0)
                {
                    Toast.makeText(CartActivity.this, "Please Add some items in your cart first !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                    startActivity(intent);

                }
                else {

                    Intent intent = new Intent(CartActivity.this, ConfirmFinalActivity.class);
                    intent.putExtra("Total Price", String.valueOf(overallTotalPrice));
                    startActivity(intent);
                    finish();
                }

            }
        });





    }

    @Override
    protected void onStart()
    {
        super.onStart();


        checkOrderState();



        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("Admin View")
                                .child(Prevalent.currentOnlineUser.Phone).child("Products"), Cart.class)
                        .build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =

                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final Cart model)
                    {



                        holder.txtProductName.setText(model.getProductname());

                        holder.txtProductPrice.setText(model.getPrice());

                        holder.txtProductquantity.setText("Qty:"+model.getQuantity());

                        int oneTypeProductTotalPrice = valueOf(model.getPrice()) * valueOf(model.getQuantity());
                        overallTotalPrice += oneTypeProductTotalPrice;


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {

                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Edit",
                                                "Remove"
                                        };
                            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                            builder.setTitle("Cart Options");

                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {

                                    if(which==0)
                                    {
                                        Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                        intent.putExtra("pid",model.getPid());
                                        startActivity(intent);


                                    }
                                    if(which==1)
                                    {
                                        cartListRef.child("Admin View")
                                                .child(Prevalent.currentOnlineUser.Phone)
                                                .child("Products")
                                                .child(model.getPid())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task)
                                                    {

                                                        Intent intent = new Intent (CartActivity.this,HomeActivity.class);
                                                        startActivity(intent);
                                                        Toast.makeText(CartActivity.this, "Item removed successfully from cart.", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                    }

                                }
                            });

                            builder.show();

                            }
                        });



                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }


                };


//        we can see products to cart just because these two lines are written
        recyclerView.setAdapter(adapter);
        adapter.startListening();






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
                    String shippingCustomerName = datasnapshot.child("name").getValue().toString();
//                    String totalshoppingCost = datasnapshot.child("totalAmount").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {

                        txtTotalAmout.setText("Order Shipped");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);


                    }
                    else if(shippingState.equals("not shipped"))
                    {

                        txtTotalAmout.setText("Under Process");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setText("Dear "+ shippingCustomerName +" your order is under process.");
                        txtMsg1.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item1:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;

            case R.id.item4:
                Intent intent4 = new Intent(this, About_Devoloper.class);
                startActivity(intent4);
                break;

            case R.id.item3:
                Intent intent3 = new Intent(this, SearchProductsActivity.class);
                startActivity(intent3);
                break;

            case R.id.item2:
                Paper.init(this);
                FirebaseAuth.getInstance().signOut();
                Paper.book().destroy();
                Intent intent2 = new Intent(this, MainActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                break;

            default:
                return super.onOptionsItemSelected(item);




        }

        return  true;

    }
}
