package com.example.freshvegis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.freshvegis.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.zip.Inflater;

import static android.view.View.GONE;

public class AdminNewOrdersActivity extends AppCompatActivity
{

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList  = findViewById(R.id.NewOrders);



        ordersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =

                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter =

                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull final AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model)
                    {

                        holder.userName.setText("Name : "+ model.getName());
                        holder.userPhone.setText("Phone : "+ model.getPhone());
                        holder.userTotalPrice.setText("Worth : "+ model.getTotalAmount());
                        holder.userShippingAddress.setText("Address : "+ model.getAddress()+","+model.getCity());

                        holder.userDateTime.setText("Orderd at : "+ model.getDate());



                        holder.showOrderdProducts.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                String uid = getRef(position).getKey();
                                Intent intent = new Intent(AdminNewOrdersActivity.this,AdminUserOrderdProductsActivity.class);
                                intent.putExtra("uid",uid);
                                startActivity(intent);

                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                CharSequence options[] = new CharSequence[]
                                {
                                        "Order Ready",
                                        "Delete Order"
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Have you prepared this order ?");

                                builder.setItems(options, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                        if (which == 0)
                                        {
                                           holder.isReady.setVisibility(View.VISIBLE);

                                           holder.itemView.setOnClickListener(new View.OnClickListener()
                                           {
                                               @Override
                                               public void onClick(View v)
                                               {
                                                   CharSequence options2[] = new CharSequence[]
                                                           {
                                                                   "Not Ready",
                                                                   "Delete Order"
                                                           };
                                                   AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                                   builder.setTitle("Do you want to make it Not Ready ?");


                                                   builder.setItems(options2, new DialogInterface.OnClickListener()
                                                   {
                                                       @Override
                                                       public void onClick(DialogInterface dialog, int which)
                                                       {

                                                           if (which == 0)
                                                           {
                                                               holder.isReady.setVisibility(View.GONE);
                                                               Intent intent = new Intent(AdminNewOrdersActivity.this,admin_category.class);
                                                               startActivity(intent);


                                                           }


                                                           else
                                                           {
                                                               String uid = getRef(position).getKey();
                                                               RemoveOrder(uid);
                                                           }

                                                       }
                                                   });

                                                   builder.show();

                                               }
                                           });

                                        }
                                        else
                                        {
                                            String uid = getRef(position).getKey();
                                            RemoveOrder(uid);
                                        }

                                    }
                                });

                                builder.show(); 

                            }
                        });







                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new AdminOrdersViewHolder(view);


                    }
                };

        ordersList.setAdapter(adapter);
        adapter.startListening();

    }



    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName, userPhone,userTotalPrice,userDateTime, userShippingAddress,isReady;
        public Button showOrderdProducts;

        public AdminOrdersViewHolder(@NonNull View itemView)
        {


            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhone = itemView.findViewById(R.id.order_phone_no);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            showOrderdProducts = itemView.findViewById(R.id.show_orderd_products);
            isReady = itemView.findViewById(R.id.ReadyOrNot);





        }
    }

    private void RemoveOrder(String uid)
    {
        ordersRef.child(uid).removeValue();

//        Now i am making users cart empty

        DatabaseReference CartRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("Admin View");

        CartRef.child(uid).removeValue();
    }
}
