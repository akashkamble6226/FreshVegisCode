package com.example.freshvegis.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshvegis.Interface.ItemClickListner;
import com.example.freshvegis.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtProductName, txtProductPrice, txtProductquantity,txtOrderdDate;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtProductName = (TextView) itemView.findViewById(R.id.in_cart_name);

        txtProductPrice = (TextView)itemView.findViewById(R.id.in_cart_price);

        txtProductquantity = (TextView) itemView.findViewById(R.id.in_cart_qty);

        txtOrderdDate = (TextView) itemView.findViewById(R.id.in_cart_date);


    }

    @Override
    public void onClick(View view)
    {

        itemClickListner.onClick(view,getAdapterPosition(),false);



    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner   = itemClickListner;
    }
}
