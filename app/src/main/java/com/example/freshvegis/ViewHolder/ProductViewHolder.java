package com.example.freshvegis.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshvegis.Interface.ItemClickListner;
import com.example.freshvegis.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

{

    public TextView txtProductName,txtProductDesc, txtProductPrice;
    public ImageView  txtProductImage;

//    accessing ItemClick Listner
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductImage = (ImageView) itemView.findViewById(R.id.product_image);

        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDesc = (TextView) itemView.findViewById(R.id.product_description);

        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);


    }

    public void setItemClickListner(ItemClickListner listner)
    {

//       first listner is comming from above
        this.listner = listner;

    }

    @Override
    public void onClick(View v)
    {

        listner.onClick(v,getAdapterPosition(),false);


    }
}
