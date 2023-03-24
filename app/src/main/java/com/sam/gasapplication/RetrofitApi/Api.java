package com.sam.gasapplication.RetrofitApi;

import com.sam.gasapplication.delivery.model.OrderDetailsModel;
import com.sam.gasapplication.delivery.model.OrderListModelDP;
import com.sam.gasapplication.model.AddRemoveitemtoCartModel;
import com.sam.gasapplication.model.CartItemNumberModel;
import com.sam.gasapplication.model.CartListModel;
import com.sam.gasapplication.model.ChangeQuantityModel;
import com.sam.gasapplication.model.CheckPriceListModel;
import com.sam.gasapplication.model.CheckUserAccountModel;
import com.sam.gasapplication.model.CommonSuccessMessageModel;
import com.sam.gasapplication.model.IdQuntityListModel;
import com.sam.gasapplication.model.ItemListModel;
import com.sam.gasapplication.model.LoginModel;
import com.sam.gasapplication.model.MyProfileModel;
import com.sam.gasapplication.model.NotificationModel;
import com.sam.gasapplication.model.OrderHistoryModel;
import com.sam.gasapplication.model.OrderHistoryModelNew;
import com.sam.gasapplication.model.OrderStatusModle;
import com.sam.gasapplication.model.PlaceOrderModel;
import com.sam.gasapplication.model.TotalAmountModel;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface Api {


    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> LOGIN_MODEL_CALL(
            @Field("email") String email,
            @Field("password") String password);


    @FormUrlEncoded
    @POST("registration")
    Call<CommonSuccessMessageModel> RegisterCommonSuccessMessageModelCall(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobileNo") String mobileNo,
            @Field("address") String address,
            @Field("landmark") String landmark,
            @Field("password") String password,
            @Field("enable_password") String enable_password);

    @FormUrlEncoded
    @POST("forget_password")
    Call<CheckUserAccountModel> CHECK_USER_ACCOUNT_MODEL_CALL(@Field("mobileNo") String mobileNo);

    @FormUrlEncoded
    @POST("reset_password")
    Call<CommonSuccessMessageModel> RESET_PASSWORD_COMMON_SUCCESS_MESSAGE_MODEL_CALL(@Field("user_id") String user_id,
                                                                                     @Field("new_password") String new_password,
                                                                                     @Field("confirm_password") String confirm_password);

    @FormUrlEncoded
    @POST("change_password")
    Call<CommonSuccessMessageModel> CHANGE_PASSWORD_COMMON_SUCCESS_MESSAGE_MODEL_CALL(@Field("user_id") String user_id,
                                                                                      @Field("old_password") String old_password,
                                                                                      @Field("new_password") String new_password,
                                                                                      @Field("confirm_password") String confirm_password);

    @Multipart
    @POST("update_profile")
    Call<CommonSuccessMessageModel> UPDATE_PROFILE_COMMON_SUCCESS_MESSAGE_MODEL_CALL(
            @Part("user_id") RequestBody user_id,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("mobileNo") RequestBody mobileNo,
            @Part("address") RequestBody address,
            @Part("landmark") RequestBody landmark,
            @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("product_list")
    Call<CheckPriceListModel> CHECK_PRICE_LIST_MODEL_CALL(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("create_support")
    Call<CommonSuccessMessageModel> SUPPORT_COMMON_SUCCESS_MESSAGE_MODEL_CALL(
            @Field("user_id") String user_id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("message") String message);

    @FormUrlEncoded
    @POST("order_history")
    Call<OrderHistoryModel> ORDER_HISTORY_MODEL_CALL(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("view_profile")
    Call<MyProfileModel> MY_PROFILE_MODEL_CALL(@Field("user_id") String user_id);

    @Multipart
    @POST("update_profile")
    Call<LoginModel> UPDATE_PROFILE_MODEL_CALL(@Part ("user_id") RequestBody user_id,
                                               @Part ("name") RequestBody name,
                                               @Part ("email") RequestBody email,
                                               @Part ("mobileNo") RequestBody mobileNo,
                                               @Part ("address") RequestBody address,
                                               @Part ("landmark") RequestBody landmark,
                                               @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("change_password")
    Call<CommonSuccessMessageModel> CHANGE_PASSWORD_COMMON_SUCCESS_MESSAGE_MODEL_CALL(
                                                @Field("user_id") String user_id,
                                                @Field("old_password") String old_password,
                                                @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST("notification")
    Call<NotificationModel> NOTIFICATION_MODEL_CALL (@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("product_list")
    Call<ItemListModel> ITEM_LIST_MODEL_CALL (@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("cartValue")

    Call<AddRemoveitemtoCartModel> ADD_REMOVEITEMTO_CART_MODEL_CALL (@Field("user_id") String user_id,
                                                                     @Field("product_id") String product_id);


    @FormUrlEncoded
    @POST("cartlist")
    Call<CartListModel> CART_LIST_MODEL_CALL (@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("change_quantity")
    Call<ChangeQuantityModel> CHANGE_QUANTITY_MODEL_CALL(@Field("user_id") String user_id,
                                                         @Field("product_id") String product_id,
                                                         @Field("quantity") String quantity);

    @FormUrlEncoded
    @POST("total_amount")
    Call<TotalAmountModel> TOTAL_AMOUNT_MODEL_CALL (@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("cart_count")
    Call<CartItemNumberModel> CART_ITEM_NUMBER_MODEL_CALL (@Field("user_id") String user_id);


    @Headers({"Content-Type:application/json"})
    @POST("place_order")
    Call<PlaceOrderModel> PLACE_ORDER_MODEL_CALL (@Body String body);


    @FormUrlEncoded
    @POST("order_status")
    Call<OrderStatusModle> ORDER_STATUS_MODLE_CALL (@Field("user_id") String user_id);



  /*  @FormUrlEncoded
    @POST("order_history")
    Call<OrderHistoryModelNew> ORDER_HISTORY_MODEL_NEW_CALL (@Field("user_id") String user_id);*/



    @FormUrlEncoded
    @POST("delete_item")
    Call<CommonSuccessMessageModel> DELETE_CART_ITEM_COMMON_SUCCESS_MESSAGE_MODEL_CALL (@Field("user_id") String user_id,
                                                                              @Field("product_id") String product_id);


    @FormUrlEncoded
    @POST("delete_notification")
    Call<CommonSuccessMessageModel> DELETE_NOTIFICATION_ITEM_COMMON_SUCCESS_MESSAGE_MODEL_CALL (@Field("user_id") String user_id,
                                                                                        @Field("notification_id") String notification_id);

    @FormUrlEncoded
    @POST("delete_notifications")
    Call<CommonSuccessMessageModel> DELETE_ALL_NOTIFICATION_COMMON_SUCCESS_MESSAGE_MODEL_CALL (@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("cancel_order")
    Call<CommonSuccessMessageModel> CANCEL_ORDER_COMMON_SUCCESS_MESSAGE_MODEL_CALL (@Field("user_id") String user_id,
                                                                                    @Field("order_number") String order_number,
                                                                                    @Field("cancel_reason") String cancel_reason);


    @FormUrlEncoded
    @POST("order_list")
    Call<OrderListModelDP> ORDER_LIST_MODEL_DP_CALL (@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("order_details")
    Call<OrderDetailsModel> ORDER_DETAILS_MODEL_CALL (@Field("user_id") String user_id,
                                                      @Field("order_number") String order_number);



    @FormUrlEncoded
    @POST("ontheway_order")
    Call<CommonSuccessMessageModel> ON_THE_WAY_COMMON_SUCCESS_MESSAGE_MODEL_CALL (@Field("order_number") String order_number);


    @FormUrlEncoded
    @POST("delivered_order")
    Call<CommonSuccessMessageModel> DELIVERD_COMMON_SUCCESS_MESSAGE_MODEL_CALL (@Field("order_number") String order_number);



}