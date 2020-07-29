package com.example.freshvegis;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshvegis.Model.Products;
import com.example.freshvegis.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");


        recyclerView = (RecyclerView)findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Intent intentNew = getIntent();
        Bundle bundle = intentNew.getExtras();



        if(bundle != null)
        {
            type = getIntent().getExtras().get("Admins").toString();

        }


    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =

                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productRef,Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =

        new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
            {

//for displaying


                        holder.txtProductName.setText(model.getProductname());

                        holder.txtProductDesc.setText(model.getDescription());

                        holder.txtProductPrice.setText("Rs."+model.getPrice()+"/-");


                        Picasso.get().load(model.getImage()).into(holder.txtProductImage);


//                setting click listner for each item on page , for further process



                        holder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {

                                if(type.equals("Admins"))
                                {
                                    Intent intent = new Intent (HomeActivity.this,AdminEditProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent (HomeActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                                }

                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();





    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(!type.equals("Admins"))
        {
            MenuInflater inflate = getMenuInflater();
            inflate.inflate(R.menu.our_menu,menu);


        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {

        if(!type.equals("Admins"))
        {
            switch (item.getItemId())
            {
                case R.id.item1:
                    Intent intent = new Intent(this, CartActivity.class);
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
                    Paper.init(this);
                    FirebaseAuth.getInstance().signOut();
                    Paper.book().destroy();
                    Intent intent2 = new Intent(this, FirstScreen.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent2);
                    break;

                default:
                    return super.onOptionsItemSelected(item);
        }





        }

        return  true;

    }


}
