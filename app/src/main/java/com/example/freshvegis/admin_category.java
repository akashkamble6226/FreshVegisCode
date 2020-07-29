package com.example.freshvegis;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

import io.paperdb.Paper;

public class admin_category extends AppCompatActivity {



    private Button adminViewOrderBut, adminLogout,editProductInfo,bhajipala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        bhajipala = (Button)findViewById(R.id.bhaji);


        adminViewOrderBut = (Button)findViewById(R.id.AllOrders);
        adminLogout =  (Button)findViewById(R.id.adminLogout);

        editProductInfo = (Button)findViewById(R.id.Edit_products_info);

        editProductInfo.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (admin_category.this,HomeActivity.class);
                intent.putExtra("Admins","Admins");
                startActivity(intent);

            }
        });


        adminViewOrderBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent  = new Intent(admin_category.this,AdminNewOrdersActivity.class);
                startActivity(intent);

            }
        });


        bhajipala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(admin_category.this,admin_add_new_product.class);
                intent.putExtra("Category","भाजीपाला");
                startActivity(intent);

            }
        });



        adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                logout();

            }
        });


    }

    private void logout()
    {
        Paper.init(this);
        Intent intent = new Intent (admin_category.this,FirstScreen.class);
        Paper.book().destroy();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
