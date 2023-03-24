package com.sam.gasapplication.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.model.LoginModel;
import com.sam.gasapplication.model.MyProfileModel;
import com.sam.gasapplication.model.MyProfileModel;
import com.sam.gasapplication.model.data.MyProfileModelData;
import com.sam.gasapplication.utility.Permission;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity {

    private AppCompatImageView manage_profile_back_arrow;
    private CircleImageView user_profile;
    private AppCompatEditText username_edittext,email_edittext,phone_edittext,address_edittext,landmark_edittext;
    private String user_id="";
    AppCompatButton update_profile_button;


    private static final int PICK_IMAGE_R = 178500;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 1457;
    private Uri img;
    private File profileImage;
    private ContentValues values5;
    private Uri imageUri5;
    private Bitmap thumbnail5;
    CircleImageView add_image_layout;

    private String regexEmail = "(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[A-Za-z0-9]:(?:|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        inits();

        SharedPreferences sharedPreferences= MyProfileActivity.this.getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("userId","");


        update_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation()){
                    user_update_api();
                }
            }
        });


        get_user_details_api();

        add_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog addProfileUpdate = new Dialog(MyProfileActivity.this);
                addProfileUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
                addProfileUpdate.setContentView(R.layout.add_profile_update_dialog);
                TextView gallaryDialog = addProfileUpdate.findViewById(R.id.gallaryDialog);
                TextView cameraDialog = addProfileUpdate.findViewById(R.id.cameraDialog);

                addProfileUpdate.show();
                Window window = addProfileUpdate.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                gallaryDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /* openGallery(PICK_IMAGE);*/
                        gallery();
                        addProfileUpdate.dismiss();
                    }
                });

                cameraDialog.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {

                        // profile_camera_open();

                        PackageManager packageManager = getApplicationContext().getPackageManager();

                        boolean readExternal = Permission.checkPermissionReadExternal(getApplicationContext());
                        boolean writeExternal = Permission.checkPermissionReadExternal2(getApplicationContext());
                        boolean camera = Permission.checkPermissionCamera(getApplicationContext());

                        if (camera && writeExternal && readExternal ) {
                            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                                values5 = new ContentValues();
                                values5.put(MediaStore.Images.Media.TITLE, "New Picture");
                                values5.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                                imageUri5 = getApplicationContext().getContentResolver().insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values5);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri5);
                                startActivityForResult(intent, CAMERA_PIC_REQUEST);
                                addProfileUpdate.dismiss();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "camera permission required", Toast.LENGTH_LONG).show();
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                            addProfileUpdate.dismiss();
                        }


                        
                    }
                });
                
                
            }
        });
        
        

        manage_profile_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private boolean validation() {

        if(username_edittext.getText().toString().equals("")){
            Toast.makeText(MyProfileActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
            return false;
        }else if(email_edittext.getText().toString().equals("")){
            Toast.makeText(MyProfileActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!email_edittext.getText().toString().matches(regexEmail)){
            Toast.makeText(MyProfileActivity.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }else if(phone_edittext.getText().toString().equals("")){
            Toast.makeText(MyProfileActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        }else if(address_edittext.getText().toString().equals("")){
            Toast.makeText(MyProfileActivity.this, "Please enter address", Toast.LENGTH_SHORT).show();
            return false;
        }else if(landmark_edittext.getText().toString().equals("")){
            Toast.makeText(MyProfileActivity.this, "Please enter landmark", Toast.LENGTH_SHORT).show();
            return false;
        }

            return true;
    }

    private void gallery() {

        boolean readExternal = Permission.checkPermissionReadExternal(getApplicationContext());
        boolean writeExternal = Permission.checkPermissionReadExternal2(getApplicationContext());
        boolean camera = Permission.checkPermissionCamera(getApplicationContext());
        if (readExternal && camera) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_R);
        }else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }

        }

    }


    public void user_update_api(){

        // show till load api data

        final ProgressDialog pd = new ProgressDialog(MyProfileActivity.this);
        pd.setCancelable(false);
        pd.setMessage("loading...");
        pd.show();

        RequestBody userNameRB = RequestBody.create(MediaType.parse("text/plain"), username_edittext.getText().toString());
        RequestBody userEmailRB = RequestBody.create(MediaType.parse("text/plain"), email_edittext.getText().toString());
        RequestBody userMobileNumberRB = RequestBody.create(MediaType.parse("text/plain"), phone_edittext.getText().toString());
        RequestBody userIDRB = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody addressRB = RequestBody.create(MediaType.parse("text/plain"), address_edittext.getText().toString());
        RequestBody landmarkRB = RequestBody.create(MediaType.parse("text/plain"), landmark_edittext.getText().toString());


        MultipartBody.Part filePart;

//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", profileImage.getName(), RequestBody.create(MediaType.parse("image/*"), profileImage));

        if (profileImage == null) {
            filePart = MultipartBody.Part.createFormData("image", "", RequestBody.create(MediaType.parse("image/*"), ""));
            Log.e("Photo", String.valueOf(profileImage)+"null");

        } else {
            filePart = MultipartBody.Part.createFormData("image", profileImage.getName(), RequestBody.create(MediaType.parse("image/*"), profileImage));
            Log.e("Photo", String.valueOf(profileImage)+"value");
        }

        // profile = MultipartBody.Part.createFormData("profile", profileImage.getName(), RequestBody.create(MediaType.parse("image/*"), profileImage));



        Call<LoginModel> call = API_Client.getClient().UPDATE_PROFILE_MODEL_CALL(userIDRB,userNameRB,userEmailRB,userMobileNumberRB,addressRB,landmarkRB,
                filePart);

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                pd.dismiss();
                try {
                    //if api response is successful ,taking message and success
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = String.valueOf(response.body().getSuccess());


                        if (success.equals("true") || success.equals("True")) {

                            Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
                            profileImage = null;
                            get_user_details_api();

                        } else {
                            Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }

                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getApplicationContext(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getApplicationContext(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getApplicationContext(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getApplicationContext(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getApplicationContext(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getApplicationContext(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getApplicationContext(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Log.e("conversion issue", t.getMessage());

                if (t instanceof IOException) {
                    Toast.makeText(getApplicationContext(), "This is an actual network failure :( inform the user and possibly retry)", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getApplicationContext(), "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }
        });

    }
    
    private void get_user_details_api() {

            final ProgressDialog pd = new ProgressDialog(MyProfileActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<MyProfileModel> call = API_Client.getClient().MY_PROFILE_MODEL_CALL(user_id);

            call.enqueue(new Callback<MyProfileModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<MyProfileModel> call, Response<MyProfileModel> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();


                            if (success.equals("true") || success.equals("True")) {

                                MyProfileModel myProfileModel = response.body();
                                MyProfileModelData myProfileModelData = myProfileModel.getData();

                                try {
                                    username_edittext.setText(myProfileModelData.getName());
                                    email_edittext.setText(myProfileModelData.getEmail());
                                    address_edittext.setText(myProfileModelData.getAddress());
                                    phone_edittext.setText(myProfileModelData.getPhone());
                                    landmark_edittext.setText(myProfileModelData.getLandmark());

                                    Glide.with(MyProfileActivity.this).load(myProfileModelData.getImage())
                                            .placeholder(R.drawable.ic_launcher_background)
                                            .into(user_profile);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }


                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }


                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();

                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(getApplicationContext(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(getApplicationContext(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(getApplicationContext(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(getApplicationContext(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(getApplicationContext(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(getApplicationContext(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(getApplicationContext(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(), "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MyProfileModel> call, Throwable t) {

                    if (t instanceof IOException) {
                        Toast.makeText(getApplicationContext(), "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });


        }

    private void inits() {

        update_profile_button = findViewById(R.id.update_profile_button);
        add_image_layout = findViewById(R.id.add_image_layout);
        user_profile = findViewById(R.id.user_profile);
        landmark_edittext = findViewById(R.id.landmark_edittext);
        address_edittext = findViewById(R.id.address_edittext);
        phone_edittext = findViewById(R.id.phone_edittext);
        email_edittext = findViewById(R.id.email_edittext);
        username_edittext = findViewById(R.id.username_edittext);
        manage_profile_back_arrow = findViewById(R.id.manage_profile_back_arrow);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if (requestCode == PICK_IMAGE_R) {

                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                profileImage = new File(cursor.getString(column_index));
                Log.e("userImage1", String.valueOf(profileImage));
                String selectedImagePath1 = getPath(selectedImageUri);

                Log.v("Deatils_path","***"+selectedImagePath1);
                Glide.with(getApplicationContext()).load(selectedImagePath1).placeholder(R.drawable.ic_launcher_background).into(user_profile);
                Log.e("userImage1", "BB");


            }else if(requestCode==CAMERA_PIC_REQUEST){

                try {
                    thumbnail5 = MediaStore.Images.Media.getBitmap(
                            getApplicationContext().getContentResolver(), imageUri5);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                profileImage = new File(getRealPathFromURIs(imageUri5));

                Glide.with(getApplicationContext())
                        .load(profileImage)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(user_profile);

            }
        }


    }

    public String getRealPathFromURIs(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    public String getPath(Uri uri)
    {
        Cursor cursor=null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor == null) return null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();


            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }
}