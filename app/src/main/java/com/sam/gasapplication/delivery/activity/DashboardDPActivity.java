package com.sam.gasapplication.delivery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sam.gasapplication.MainActivity;
import com.sam.gasapplication.R;
import com.sam.gasapplication.view.LoginActivity;
import com.sam.gasapplication.view.NotificationActivity;
import com.sam.gasapplication.view.activity.MyProfileActivity;

public class DashboardDPActivity extends AppCompatActivity {

    CardView order_list_dp_layout_CV,Order_history_dp_layout_CV,notification_dp_layout_CV
            ,manage_profile_dp_layout_CV,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_dpactivity);
        intis();

        order_list_dp_layout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardDPActivity.this,OrderListDpActivity.class);
                startActivity(intent);

            }
        });

        Order_history_dp_layout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardDPActivity.this,OrderHistoryDpActivity.class);
                startActivity(intent);
            }
        });

        notification_dp_layout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardDPActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        manage_profile_dp_layout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardDPActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(DashboardDPActivity.this);
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
                       /* SharedPreferences getUserIdData = getSharedPreferences("AUTHENTICATION_FILE_NAME_DP", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = getUserIdData.edit();
                        editor.putString("UserID", "");
                        editor.putString("accessToken", "");
                        editor.putString("refreshToken", "");
                        editor.apply();*/
                        Intent intent = new Intent(DashboardDPActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("finish", true);
                        startActivity(intent);

                        Toast.makeText(DashboardDPActivity.this, "User logout successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });


                noDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });



    }

    private void intis() {
        order_list_dp_layout_CV = findViewById(R.id.order_list_dp_layout_CV);
        Order_history_dp_layout_CV = findViewById(R.id.Order_history_dp_layout_CV);
        notification_dp_layout_CV = findViewById(R.id.notification_dp_layout_CV);
        manage_profile_dp_layout_CV = findViewById(R.id.manage_profile_dp_layout_CV);
        logout = findViewById(R.id.logout);
    }
}