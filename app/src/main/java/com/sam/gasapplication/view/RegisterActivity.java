package com.sam.gasapplication.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sam.gasapplication.MainActivity;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.model.CommonSuccessMessageModel;
import com.sam.gasapplication.view.activity.ForgotPasswordActivityActivity;


import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.aabhasjindal.otptextview.OtpTextView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.logo_register_page)
    AppCompatImageView logo_register_page;

    @BindView(R.id.username_edittext)
    AppCompatEditText username_edittext;

    @BindView(R.id.email_edittext)
    AppCompatEditText email_edittext;

    @BindView(R.id.address_edittext)
    AppCompatEditText address_edittext;

    @BindView(R.id.phone_edittext)
    AppCompatEditText phone_edittext;

    @BindView(R.id.password_edittext)
    AppCompatEditText password_edittext;

    @BindView(R.id.c_password_edittext)
    AppCompatEditText c_password_edittext;

    @BindView(R.id.landmark_edittext)
    AppCompatEditText landmark_edittext;

    @BindView(R.id.login_layout)
    AppCompatTextView login_layout;

    @BindView(R.id.register_button)
    AppCompatButton register_button;


    private String regexEmail = "(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[A-Za-z0-9]:(?:|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private String phoneVerificationId;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);


        FirebaseApp.initializeApp(RegisterActivity.this);
        context = RegisterActivity.this;
        FirebaseApp.getApps(context);
        mAuth = FirebaseAuth.getInstance();
        
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation()){

                    // remove zero from start...
                    String mobileNumberData = remove_zero(phone_edittext.getText().toString());
                    Log.e("test_sam_mobile_number",mobileNumberData+"ok");


                    //startPhoneNumberVerification("+91"+phone_edittext.getText().toString());
                    startPhoneNumberVerification("+27"+mobileNumberData);

                }
            }
        });

        login_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private String remove_zero(String str) {

            StringBuffer sb = new StringBuffer(str);
            while (sb.length()>1 && sb.charAt(0) == '0')
                sb.deleteCharAt(0);
            return sb.toString();  // return in String
        }

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
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            //Toast.makeText(RegisterActivity.this,"Please enter valid number", Toast.LENGTH_LONG).show();
        }
    };

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI

                            Toast.makeText(RegisterActivity.this, "OTP Verification Successful...", Toast.LENGTH_SHORT).show();

                            // calling  register password API...

                            register_api();


                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(RegisterActivity.this, "Something went wrong.. Please verify your pone number again.", Toast.LENGTH_SHORT).show();

                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void popup() {

        AlertDialog dialogs;


        final LayoutInflater inflater = RegisterActivity.this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.test_dialog_xml_otp, null);
        final AppCompatButton continue_button = alertLayout.findViewById(R.id.continue_button);
        final OtpTextView otpText = alertLayout.findViewById(R.id.otpText);



        final AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);

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
                    Toast.makeText(RegisterActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                }else if( otpText.getOTP().length()!=6){
                    Toast.makeText(RegisterActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();

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

    private boolean validation() {
        if(username_edittext.getText().toString().equals("")){
            Toast.makeText(RegisterActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
            return false;
        }else if(email_edittext.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
            return false;

        }else if(!email_edittext.getText().toString().matches(regexEmail)) {
            Toast.makeText(RegisterActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return false;

        }else if(phone_edittext.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;

        }else if(address_edittext.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Please enter address", Toast.LENGTH_SHORT).show();
            return false;

        }else if(landmark_edittext.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Please enter landmark", Toast.LENGTH_SHORT).show();
            return false;

        }else if(password_edittext.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;

        }else if(c_password_edittext.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
            return false;

        }else if(!password_edittext.getText().toString().equals(c_password_edittext.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Password did not match", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;
    }

    private void register_api() {
        // show till load api data
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("loading...");
        pd.show();

        Call<CommonSuccessMessageModel> call = API_Client.getClient().RegisterCommonSuccessMessageModelCall(
                username_edittext.getText().toString(),
                email_edittext.getText().toString(),
                phone_edittext.getText().toString(),
                address_edittext.getText().toString(),
                landmark_edittext.getText().toString(),
                password_edittext.getText().toString(),
                password_edittext.getText().toString());

        call.enqueue(new Callback<CommonSuccessMessageModel>() {
            @Override
            public void onResponse(Call<CommonSuccessMessageModel> call, Response<CommonSuccessMessageModel> response) {
                pd.dismiss();
                try {
                    //if api response is successful ,taking message and success
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();

                        if (success.equals("true") || success.equals("True")) {
                            Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), message +  "\n" + "Please Try Again", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());

                            Log.e("json",jObjError.toString());
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
                            Log.e("conversion issue", e.getMessage());
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (
                        Exception e) {
                    Log.e("conversion issue", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessMessageModel> call, Throwable t) {
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
}