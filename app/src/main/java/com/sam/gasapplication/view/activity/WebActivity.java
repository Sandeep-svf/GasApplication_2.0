package com.sam.gasapplication.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sam.gasapplication.MainActivity;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;

public class WebActivity extends AppCompatActivity {
    WebView web;
    private String orderNumber, totalAmount,userName,email,address,cartCount,userId;
    AppCompatImageView place_order_home_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        web = findViewById(R.id.web);
        place_order_home_button = findViewById(R.id.place_order_home_button);

        place_order_home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WebActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("finish", true);
                startActivity(intent);
            }
        });


        try {
            orderNumber = getIntent().getStringExtra("orderNumber");
            totalAmount = getIntent().getStringExtra("totalAmount");
            userName = getIntent().getStringExtra("userName");
            email = getIntent().getStringExtra("email");
            address = getIntent().getStringExtra("address");
            cartCount = getIntent().getStringExtra("cartCount");
            userId = getIntent().getStringExtra("userId");


            Log.e("placeOrderData","orderNumber :"+orderNumber);
            Log.e("placeOrderData","totalAmount :"+totalAmount);
            Log.e("placeOrderData","userName :"+userName);
            Log.e("placeOrderData","email :"+email);
            Log.e("placeOrderData","address :"+address);
            Log.e("placeOrderData","cartCount :"+cartCount);
            Log.e("placeOrderData","userId :"+userId);

        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(WebActivity.this, "Something went wrong while loading order ID.", Toast.LENGTH_SHORT).show();
        }


        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setAllowContentAccess(true);
        web.getSettings().setPluginState(WebSettings.PluginState.ON);
        web.getSettings().setMediaPlaybackRequiresUserGesture(false);
        web.setLayerType(View.LAYER_TYPE_NONE, null);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl(API_Client.PAYMENT_GETWAY+userId+"&order_number="+orderNumber);
        //https://itdevelopmentservices.com/thegaskiosk/admin/payment?user_id=&order_number=
        // +"&total_amount="+totalAmount+"&user_name="+userName+"&user_email="+email+"&user_address="+address+"&cartCount="+cartCount
    }
}