package com.sam.gasapplication.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sam.gasapplication.MainActivity;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.model.CheckUserAccountModel;
import com.sam.gasapplication.model.CommonSuccessMessageModel;
import com.sam.gasapplication.view.LoginActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivityActivity extends AppCompatActivity {

    AppCompatImageView back_arrow_forgot_password;
    private Context context;
    private AppCompatButton reset_password_button_layout;
    AppCompatEditText user_phoennumber_edittext,new_password_edittext,confirm_new_password_edittext;

    private static final String TAG = "ForgotPasswordActivityActivity";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private String phoneVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String userId;
    private AppCompatImageView hiden_password_image, c_hiden_password_image, visibale_password_image, c_visibale_password_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_activity);

        FirebaseApp.initializeApp(ForgotPasswordActivityActivity.this);
        context = ForgotPasswordActivityActivity.this;
        FirebaseApp.getApps(context);
        mAuth = FirebaseAuth.getInstance();


        hiden_password_image = findViewById(R.id.hiden_password_image);
        c_hiden_password_image = findViewById(R.id.c_hiden_password_image);
        visibale_password_image = findViewById(R.id.visibale_password_image);
        c_visibale_password_image = findViewById(R.id.c_visibale_password_image);
        back_arrow_forgot_password = findViewById(R.id.back_arrow_forgot_password);
        reset_password_button_layout = findViewById(R.id.reset_password_button_layout);
        user_phoennumber_edittext = findViewById(R.id.user_phoennumber_edittext);
        new_password_edittext = findViewById(R.id.new_password_edittext);
        confirm_new_password_edittext = findViewById(R.id.confirm_new_password_edittext);



        hiden_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibale_password_image.setVisibility(View.VISIBLE);
                hiden_password_image.setVisibility(View.GONE);
                new_password_edittext.setTransformationMethod(null);
                new_password_edittext.setSelection(new_password_edittext.getText().length());

            }
        });

        visibale_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiden_password_image.setVisibility(View.VISIBLE);
                visibale_password_image.setVisibility(View.GONE);

                new_password_edittext.setTransformationMethod(new PasswordTransformationMethod());
                new_password_edittext.setSelection(new_password_edittext.getText().length());
            }
        });


        c_hiden_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c_visibale_password_image.setVisibility(View.VISIBLE);
                c_hiden_password_image.setVisibility(View.GONE);
                confirm_new_password_edittext.setTransformationMethod(null);
                confirm_new_password_edittext.setSelection(confirm_new_password_edittext.getText().length());

            }
        });

        c_visibale_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c_hiden_password_image.setVisibility(View.VISIBLE);
                c_visibale_password_image.setVisibility(View.GONE);

                confirm_new_password_edittext.setTransformationMethod(new PasswordTransformationMethod());
                confirm_new_password_edittext.setSelection(confirm_new_password_edittext.getText().length());
            }
        });




        reset_password_button_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validation()){
                    check_user_acount_api();
                }

            }
        });


        back_arrow_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void check_user_acount_api() {


            final ProgressDialog pd = new ProgressDialog(ForgotPasswordActivityActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<CheckUserAccountModel> call = API_Client.getClient().CHECK_USER_ACCOUNT_MODEL_CALL(user_phoennumber_edittext.getText().toString());

            call.enqueue(new Callback<CheckUserAccountModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<CheckUserAccountModel> call, Response<CheckUserAccountModel> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();


                            if (success.equals("true") || success.equals("True")) {
                                // calling firebase OTP verification .....
                                CheckUserAccountModel checkUserAccountModel = response.body();

                                userId = checkUserAccountModel.getUser_id();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();


                                String mobileNumberData = remove_zero(user_phoennumber_edittext.getText().toString());

                               // startPhoneNumberVerification("+91"+user_phoennumber_edittext.getText().toString());
                                startPhoneNumberVerification("+27"+mobileNumberData);

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
                public void onFailure(Call<CheckUserAccountModel> call, Throwable t) {
                    Log.e("bhgyrrrthbh", String.valueOf(t));
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

    private String remove_zero(String str) {

        StringBuffer sb = new StringBuffer(str);
        while (sb.length()>1 && sb.charAt(0) == '0')
            sb.deleteCharAt(0);
        return sb.toString();  // return in String
    }

    private boolean validation() {

        if(user_phoennumber_edittext.getText().toString().equals("")){
            Toast.makeText(ForgotPasswordActivityActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        }else  if(new_password_edittext.getText().toString().equals("")){
            Toast.makeText(ForgotPasswordActivityActivity.this, "Please enter new password", Toast.LENGTH_SHORT).show();
            return false;
        }else  if(confirm_new_password_edittext.getText().toString().equals("")){
            Toast.makeText(ForgotPasswordActivityActivity.this, "Please enter confirm new password", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!new_password_edittext.getText().toString().equals(confirm_new_password_edittext.getText().toString())){
            Toast.makeText(ForgotPasswordActivityActivity.this, "Passsword did not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
           /* Intent intent=new Intent(getApplicationContext(),Verification_Activity.class);
            startActivity(intent);*/
            phoneVerificationId = s;
            //viewFlipper.setDisplayedChild(1);

            popup();


        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            signInWithPhoneAuthCredential(phoneAuthCredential);
            // checking if the code
            // is null or not.

            if (code != null) {
                //   Forget_Login();
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
//                edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
//                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.

         /*   Intent intent = new Intent(RegisterActivity.this, NameEmailActivity.class);
            startActivity(intent);*/
            Toast.makeText(ForgotPasswordActivityActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(ForgotPasswordActivityActivity.this,"Please enter valid number", Toast.LENGTH_LONG).show();
        }
    };



    private void startPhoneNumberVerification(String s) {

        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                s, // first parameter is user's mobile number
                60, // second parameter is time limit for OTP
                // verification which is 60 seconds in our case.
                TimeUnit.SECONDS, // third parameter is for initializing units
                // for time period which is in seconds in our case.
                TaskExecutors.MAIN_THREAD, // this task will be excuted on Main thread.
                mCallBack // we are calling callback method when we recieve OTP for
                // auto verification of user.
        );
    }



    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI

                            Toast.makeText(ForgotPasswordActivityActivity.this, "OTP Verification Successful...", Toast.LENGTH_SHORT).show();

                            // calling  reset password API...

                            reset_password_api();


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void reset_password_api() {



            final ProgressDialog pd = new ProgressDialog(ForgotPasswordActivityActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<CommonSuccessMessageModel> call = API_Client.getClient().RESET_PASSWORD_COMMON_SUCCESS_MESSAGE_MODEL_CALL(userId,new_password_edittext.getText().toString(),confirm_new_password_edittext.getText().toString());

            call.enqueue(new Callback<CommonSuccessMessageModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<CommonSuccessMessageModel> call, Response<CommonSuccessMessageModel> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();


                            if (success.equals("true") || success.equals("True")) {
                                // calling firebase OTP verification .....

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(ForgotPasswordActivityActivity.this, LoginActivity.class);
                                startActivity(intent);



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
                public void onFailure(Call<CommonSuccessMessageModel> call, Throwable t) {
                    Log.e("bhgyrrrthbh", String.valueOf(t));
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
    // [END sign_in_with_phone]



    private void popup() {

        AlertDialog dialogs;


        final LayoutInflater inflater = ForgotPasswordActivityActivity.this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.test_dialog_xml_otp, null);
        final AppCompatButton continue_button = alertLayout.findViewById(R.id.continue_button);
        final OtpTextView otpText = alertLayout.findViewById(R.id.otpText);



        final AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPasswordActivityActivity.this);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        dialogs = alert.create();
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogs.show();
        dialogs.setCanceledOnTouchOutside(false);


        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialogs.dismiss();
                // call varification function here......


                if( otpText.getOTP().equals("")){
                    Toast.makeText(ForgotPasswordActivityActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                }else if( otpText.getOTP().length()!=6){
                    Toast.makeText(ForgotPasswordActivityActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();

                } else{
                    verifyCode(otpText.getOTP());
                }
            }
        });
    }

    private void verifyCode(String code) {

       // String code = otpText.getOTP();
        if ((!code.equals("")) && (code.length() == 6)) {

            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(phoneVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else if (code.length() != 6) {
            Toast.makeText(this, "Please enter six digit valid otp", Toast.LENGTH_SHORT).show();

        }
    }

}