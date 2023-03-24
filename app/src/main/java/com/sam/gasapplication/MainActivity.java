package com.sam.gasapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import android.app.Dialog;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.delivery.activity.DashboardDPActivity;
import com.sam.gasapplication.model.CartItemNumberModel;
import com.sam.gasapplication.model.CartItemNumberModel;
import com.sam.gasapplication.view.LoginActivity;
import com.sam.gasapplication.view.NotificationActivity;
import com.sam.gasapplication.view.activity.ChangePasswordActivity;
import com.sam.gasapplication.view.activity.MyProfileActivity;
import com.sam.gasapplication.view.activity.SupportActivity;
import com.sam.gasapplication.view.fragment.CartListFragment;
import com.sam.gasapplication.view.fragment.DashboardFragment;
import com.sam.gasapplication.view.fragment.ItemListPlaceOrderFragment;

import com.sam.gasapplication.view.fragment.OrderHistoryFragment;
import com.sam.gasapplication.view.fragment.OrderStatusFragment;
import com.sam.gasapplication.view.fragment.CheckPriceListFragment;

import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends SlidingFragmentActivity implements View.OnClickListener  {

    RelativeLayout dashboard_layout,support_layout,check_price_layout,place_order_layout,order_status_layout,
            changhe_password_layout,order_history_layout,setting_layout,logout_layout,notification_layout;
    AppCompatImageView btnMenu,shoping_cart_icon;
    public static AppCompatTextView title;
    ShapeableImageView userImageLayout;
    ConstraintLayout container;
    private String user_id;
    AppCompatTextView cart_number;
    private boolean shouldLoadHomeFragOnBackPress = true;
    boolean doubleBackToExitPressedOnce = false;
    private int navItemIndex;
    private static final String TAG_DASH_BOARD = "dashboard";
    public static String CURRENT_TAG = TAG_DASH_BOARD;
    private static final String OrderHistory = "OrderHistory";
    private static final String OrderStatus = "OrderStatus";
    private static final String ItemListPlaceOrder = "ItemListPlaceOrder";
    private static final String CartList = "CartList";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.menu);

        intis();

        SharedPreferences sharedPreferences= getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("userId","");

        logout_layout.setOnClickListener(this::onClick);
        notification_layout.setOnClickListener(this::onClick);
        btnMenu.setOnClickListener(this::onClick);
        support_layout.setOnClickListener(this::onClick);
        dashboard_layout.setOnClickListener(this::onClick);
        check_price_layout.setOnClickListener(this::onClick);
        place_order_layout.setOnClickListener(this::onClick);
        order_status_layout.setOnClickListener(this::onClick);
        changhe_password_layout.setOnClickListener(this::onClick);
        setting_layout.setOnClickListener(this::onClick);
        order_history_layout.setOnClickListener(this::onClick);
        shoping_cart_icon.setOnClickListener(this::onClick);

        my_sliding_window();
        cart_item_number_api();

         /*  // default fragment
        */


        shoping_cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);*/
                navItemIndex=45;
                CURRENT_TAG = CartList;
                CartListFragment cartListFragment = new CartListFragment();
                androidx.fragment.app.FragmentManager fragmentManager_two = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction212 = fragmentManager_two.beginTransaction();
                fragmentTransaction212.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction212.replace(R.id.container, cartListFragment);
                fragmentTransaction212.commit();

                title.setText("Cart List");

            }
        });

    }

    public void cart_item_number_api() {

        // show till load api data
        Log.e("test","Calling.. API Success");
        Log.e("testdklfjskalf","Calling.. API Success");

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("loading...");
        pd.show();

        //Toast.makeText(MainActivity.this, "###########", Toast.LENGTH_SHORT).show();

            Call<CartItemNumberModel> call = API_Client.getClient().CART_ITEM_NUMBER_MODEL_CALL(user_id);

            call.enqueue(new Callback<CartItemNumberModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<CartItemNumberModel> call, Response<CartItemNumberModel> response) {

                    pd.dismiss();

                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();
                            String totalAmount = String.valueOf(response.body().getCartCount());

                            if (success.equals("true") || success.equals("True")) {

                                CartItemNumberModel cartItemNumberModel = response.body();


                                cart_number.setText(String.valueOf(cartItemNumberModel.getCartCount()));

                                Log.e("testdklfjskalf","Cart value is: "+String.valueOf(cartItemNumberModel.getCartCount()));
                                pd.dismiss();

                            } else {

                                pd.dismiss();
                            }


                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(MainActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();

                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(MainActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(MainActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(MainActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(MainActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(MainActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(MainActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(MainActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(MainActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CartItemNumberModel> call, Throwable t) {
                    Log.e("bhgyrrrthbh", String.valueOf(t));
                    if (t instanceof IOException) {
                        Toast.makeText(MainActivity.this, "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(MainActivity.this, "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });


        }

    public void my_sliding_window() {
        try {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

            final Display display = getWindowManager().getDefaultDisplay();
            int screenWidth = display.getWidth();
            //int screenWidth = getScreenWidthInPixel();
            final int slidingmenuWidth = (int) (screenWidth - (screenWidth / 3.7) + 20);
            final int offset = Math.max(0, screenWidth - slidingmenuWidth);
            getSlidingMenu().setBehindOffset(offset);
            getSlidingMenu().toggle();
            getSlidingMenu().isVerticalFadingEdgeEnabled();
            getSlidingMenu().isHorizontalFadingEdgeEnabled();
            getSlidingMenu().setMode(SlidingMenu.LEFT);
            getSlidingMenu().setFadeEnabled(true);
            getSlidingMenu().setFadeDegree(0.5f);
            getSlidingMenu().setFadingEdgeLength(10);
            getSlidingMenu().setEnabled(false);

            MainActivity mainActivity = MainActivity.this;

        } catch (Exception e) {
            e.printStackTrace();
        }
     /*   DashboardFragment dashboardFragment = new DashboardFragment();
        FragmentManager fragmentManager_two = getSupportFragmentManager();
        ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
        FragmentTransaction fragmentTransaction2 = fragmentManager_two.beginTransaction();
        fragmentTransaction2.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
        fragmentTransaction2.replace(R.id.container, dashboardFragment);
        fragmentTransaction2.commit();
        getSlidingMenu().toggle();*/

        CURRENT_TAG = ItemListPlaceOrder;

        DashboardFragment dashboardFragment112 = new DashboardFragment();
        androidx.fragment.app.FragmentManager fragmentManager12312121212 = getSupportFragmentManager();
        ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
        androidx.fragment.app.FragmentTransaction fragmentTransaction1231212121212 = fragmentManager12312121212.beginTransaction();
        fragmentTransaction1231212121212.add(R.id.container, dashboardFragment112, CURRENT_TAG);
        fragmentTransaction1231212121212.commit();
        getSlidingMenu().toggle();

    }

    private void intis() {
        cart_number = (AppCompatTextView) findViewById(R.id.cart_number);
        notification_layout = (RelativeLayout) findViewById(R.id.notification_layout);
        btnMenu = (AppCompatImageView) findViewById(R.id.btnMenu);
        btnMenu= (AppCompatImageView) findViewById(R.id.btnMenu);
        title= (AppCompatTextView) findViewById(R.id.title);
        shoping_cart_icon= (AppCompatImageView) findViewById(R.id.shoping_cart_icon);
        dashboard_layout= (RelativeLayout) findViewById(R.id.dashboard_layout);
        container= (ConstraintLayout) findViewById(R.id.container);
        support_layout= (RelativeLayout) findViewById(R.id.support_layout);
        check_price_layout= (RelativeLayout) findViewById(R.id.check_price_layout);
        place_order_layout= (RelativeLayout) findViewById(R.id.place_order_layout);
        order_status_layout= (RelativeLayout) findViewById(R.id.order_status_layout);
        changhe_password_layout= (RelativeLayout) findViewById(R.id.changhe_password_layout);
        dashboard_layout= (RelativeLayout) findViewById(R.id.dashboard_layout);
        setting_layout= (RelativeLayout) findViewById(R.id.setting_layout);
        order_history_layout= (RelativeLayout) findViewById(R.id.order_history_layout);
        logout_layout= (RelativeLayout) findViewById(R.id.logout_layout);
        userImageLayout = (ShapeableImageView) findViewById(R.id.userImageLayout);
    }
/*    public static void load_home_fragment(Fragment homeFragment, FragmentManager fragmentManager)
    {

        *//*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();*//*

        FragmentTransaction fragmentTransaction12312121212 = fragmentManager.beginTransaction();
        fragmentTransaction12312121212.add(R.id.container, homeFragment);
        fragmentTransaction12312121212.commit();



    }*/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnMenu:
                showMenu();
                break;



            case R.id.dashboard_layout:
                title.setText("Home");
                navItemIndex=0;
              /*  DashboardFragment dashboardFragment = new DashboardFragment();
                FragmentManager fragmentManager_two = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction2 = fragmentManager_two.beginTransaction();
                fragmentTransaction2.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction2.replace(R.id.container, dashboardFragment);
                fragmentTransaction2.commit();
                getSlidingMenu().toggle();*/


                CURRENT_TAG = ItemListPlaceOrder;
                DashboardFragment dashboardFragment1 = new DashboardFragment();
                androidx.fragment.app.FragmentManager fragmentManager12312121212 = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction12312121212 = fragmentManager12312121212.beginTransaction();
                fragmentTransaction12312121212.replace(R.id.container, dashboardFragment1, CURRENT_TAG);
                fragmentTransaction12312121212.commit();
                getSlidingMenu().toggle();
                break;

            case R.id.notification_layout:
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                break;

            case R.id.check_price_layout:
                title.setText("Check Price");
                navItemIndex=1;
              /*  CheckPriceListFragment checkPriceListFragment = new CheckPriceListFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction.replace(R.id.container, checkPriceListFragment);
                fragmentTransaction.commit();
                getSlidingMenu().toggle();*/



                CURRENT_TAG = ItemListPlaceOrder;
                CheckPriceListFragment checkPriceListFragment1 = new CheckPriceListFragment();
                androidx.fragment.app.FragmentManager fragmentManager123121212 = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction123121212 = fragmentManager123121212.beginTransaction();
                fragmentTransaction123121212.add(R.id.container, checkPriceListFragment1, CURRENT_TAG);
                fragmentTransaction123121212.commit();
                getSlidingMenu().toggle();

                break;

            case R.id.place_order_layout:
                title.setText("Place Order");
               /* CheckPriceFragment checkPriceFragment = new CheckPriceFragment();
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction3 = fragmentManager2.beginTransaction();
                fragmentTransaction3.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction3.add(R.id.container, checkPriceFragment);
                fragmentTransaction3.commit();
                getSlidingMenu().toggle();*/
                navItemIndex=2;
               /* ItemListPlaceOrderFragment checkPriceFragment = new ItemListPlaceOrderFragment();
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction3 = fragmentManager2.beginTransaction();
                fragmentTransaction3.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction3.replace(R.id.container, checkPriceFragment);
                fragmentTransaction3.commit();
                getSlidingMenu().toggle();*/

                CURRENT_TAG = ItemListPlaceOrder;
                ItemListPlaceOrderFragment itemListPlaceOrderFragment = new ItemListPlaceOrderFragment();
                androidx.fragment.app.FragmentManager fragmentManager1231212 = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction1231212 = fragmentManager1231212.beginTransaction();
                fragmentTransaction1231212.add(R.id.container, itemListPlaceOrderFragment, CURRENT_TAG);
                fragmentTransaction1231212.commit();
                getSlidingMenu().toggle();


                break;


            case R.id.order_status_layout:
                title.setText("Order Status");
                navItemIndex=3;
               /* OrderStatusFragment orderStatusFragment = new OrderStatusFragment();
                FragmentManager fragmentManager4 = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                fragmentTransaction4.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction4.replace(R.id.container, orderStatusFragment);
                fragmentTransaction4.commit();
                getSlidingMenu().toggle();*/

                CURRENT_TAG = OrderStatus;
                OrderStatusFragment orderStatusFragment = new OrderStatusFragment();
                androidx.fragment.app.FragmentManager fragmentManager12312 = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction12312 = fragmentManager12312.beginTransaction();
                fragmentTransaction12312.add(R.id.container, orderStatusFragment, CURRENT_TAG);
                fragmentTransaction12312.commit();
                getSlidingMenu().toggle();

                break;

            case R.id.order_history_layout:
                title.setText("Order History");
                navItemIndex=4;
             /*   OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
                FragmentManager fragmentManager5 = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction5 = fragmentManager5.beginTransaction();
                fragmentTransaction5.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction5.replace(R.id.container, orderHistoryFragment);
                fragmentTransaction5.commit();
                getSlidingMenu().toggle();*/


                CURRENT_TAG = OrderHistory;
                OrderHistoryFragment contact_us_fragment = new OrderHistoryFragment();
                androidx.fragment.app.FragmentManager fragmentManager123 = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                androidx.fragment.app.FragmentTransaction fragmentTransaction123 = fragmentManager123.beginTransaction();
                fragmentTransaction123.add(R.id.container, contact_us_fragment, CURRENT_TAG);
                fragmentTransaction123.commit();
                getSlidingMenu().toggle();




                break;

            case R.id.setting_layout:

                Intent intent25 = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(intent25);
                break;
            case R.id.changhe_password_layout:
                Intent intent2 = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(intent2);
                break;
            case R.id.support_layout:
                Intent intent3 = new Intent(MainActivity.this, SupportActivity.class);
                startActivity(intent3);
                break;

            case R.id.logout_layout:


                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.logout_dialog);
                LinearLayout noDialogLogout = dialog.findViewById(R.id.noDialogLogout);
                LinearLayout yesDialogLogout = dialog.findViewById(R.id.yesDialogLogout);


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                yesDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences getUserIdData = getSharedPreferences("AUTHENTICATION_FILE_NAME_DP", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = getUserIdData.edit();
                        editor.putString("UserID", "");
                        editor.putString("accessToken", "");
                        editor.putString("refreshToken", "");
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("finish", true);
                        startActivity(intent);

                        Toast.makeText(MainActivity.this, "User logout successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });


                noDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                break;

        }

    }

    @Override
    public void onBackPressed() {

        Log.e("frg_test","Calling on back pressed...");

        if (shouldLoadHomeFragOnBackPress) {

            if (navItemIndex != 0) {
                Log.e("frg_test","Calling navIndex 0");
                navItemIndex = 0;
                CURRENT_TAG = TAG_DASH_BOARD;
                loadHomeFragment();
            } else {
                Log.e("frg_test","Calling navIndex not 0");
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new android.os.Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

                return;
            }
        }

    }

    public static void set_head(String value){

        if(value.equals("1")){
            title.setText("Check Price");
        }else  if(value.equals("2")){
            title.setText("Place Order");
        }else  if(value.equals("3")){
            title.setText("Order Status");
        }else  if(value.equals("4")){
            title.setText("Order History");
        }


    }

    private void loadHomeFragment() {
        /*DashboardFragment fragment123123 = new DashboardFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment123123, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();*/

        DashboardFragment dasboardFragment = new DashboardFragment();
        androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
        ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
        androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, dasboardFragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();

    /*    Log.e("frg_test","Calling home fragment after back pressed");
        DashboardFragment dashboardFragment = new DashboardFragment();
        FragmentManager fragmentManager_two = getSupportFragmentManager();
        ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
        FragmentTransaction fragmentTransaction2 = fragmentManager_two.beginTransaction();
        fragmentTransaction2.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
        fragmentTransaction2.replace(R.id.container, dashboardFragment);
        fragmentTransaction2.commitAllowingStateLoss();*/
    }
}