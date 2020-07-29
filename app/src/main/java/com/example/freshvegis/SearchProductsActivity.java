package com.example.freshvegis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.freshvegis.Model.Products;
import com.example.freshvegis.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class SearchProductsActivity extends AppCompatActivity
{

    private Button searchButton;
    private EditText inputText;
    RecyclerView searchList;
    private String anyInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        inputText = (EditText) findViewById(R.id.search);
        searchButton =(Button) findViewById(R.id.Search_button);
        searchList = (RecyclerView) findViewById(R.id.Search_list);

        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                anyInput = inputText.getText().toString();

                onStart();

            }
        });

    }


    @Override
    protected void onStart()
    {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");


        FirebaseRecyclerOptions<Products> options =

                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("productname").startAt(anyInput),Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {


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
                                Intent intent = new Intent (SearchProductsActivity.this,ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);

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
        searchList.setAdapter(adapter);
        adapter.startListening();


    }




}
