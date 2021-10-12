package com.demo.webservice;

import com.demo.carDetails.model.CarDetailRequest;
import com.demo.carDetails.model.CarDetailReviewModel;
import com.demo.carDetails.model.CarDetailsSpecificationModel;
import com.demo.carDetails.model.CarPorfomaInvoiceModel;
import com.demo.home.model.AppContentModel;
import com.demo.home.model.AppRequestModel;
import com.demo.home.model.CarDealerResponseModel;
import com.demo.carDetails.model.CarDetailResponse;
import com.demo.home.model.CarFilterResponse;
import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.CarSearchResultModel;
import com.demo.home.model.CategoryResponse;
import com.demo.home.model.MenuResponse;
import com.demo.registrationLogin.model.CommanRequestModel;
import com.demo.registrationLogin.model.LoginResponseModel;
import com.demo.registrationLogin.model.OtpResponseModel;
import com.demo.registrationLogin.model.PINResponseModel;
import com.demo.registrationLogin.model.RegistrationRequestModel;
import com.demo.registrationLogin.model.RegistrationResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {
    @POST("customersingup")
    Call<RegistrationResponse> newSignUpUser(@Body RegistrationRequestModel user);


    @POST("userlogin")
    Call<LoginResponseModel> loginUser(@Body CommanRequestModel user);

    @POST("sendotp")
    Call<OtpResponseModel> sendOTP(@Body CommanRequestModel user);


    @POST("otpvalidation")
    Call<LoginResponseModel> otpValidation(@Body CommanRequestModel pinRequestModel);


    @POST("updatepin")
    Call<PINResponseModel> updatePin(@Body CommanRequestModel pinRequestModel);


    @POST("userloginbypin")
    Call<PINResponseModel> userloginbypin(@Body CommanRequestModel pinRequestModel);

    @Multipart
    @POST("upload/vaccinedoc")
    Call<RegistrationResponse> uploadAttachment(@Part MultipartBody.Part File, @Part("UserID")RequestBody userid);

    @GET("car/filter/price")
    Call<CarFilterResponse> carFilterPrice();

    @GET("car/filter/segment")
    Call<CarFilterResponse> carFilterSegment();

    @GET("car/filter/brand")
    Call<CarFilterResponse> carFilterBrand();


    @GET("car/filter/fueltype")
    Call<CarFilterResponse> fuelFilter();


    @GET("car/drivemydemo/demotype")
    Call<MenuResponse> demotype();

    @GET("car/meetaspecialist/meettype")
    Call<MenuResponse> meettype();

    @GET("car/meetaspecialist/virtualmeettype")
    Call<MenuResponse> virtualmeettype();


    @GET("car/meetaspecialist/calltype")
    Call<MenuResponse> meetspecialist();


    @POST("appcontent")
    Call<AppContentModel> getAppContent(@Body AppRequestModel appRequestModel);

    @POST("car/list")
    Call<CarSearchResultModel> getCarSearchList(@Body CarSearchRequestModel appRequestModel);


    @GET("getproductcategory")
    Call<CategoryResponse> getProductCategoryforSearch();

    @POST("car/topdemodealer")
    Call<CarDealerResponseModel> getCarDealer(@Body CarSearchRequestModel appRequestModel);


    @GET("car/filter/vehicletype")
    Call<CarFilterResponse> getVehicleType();

    @POST("car/details")
    Call<CarDetailResponse> getCarDetail(@Body CarDetailRequest appRequestModel);

    @POST("car/details/reviews")
    Call<CarDetailReviewModel> getCarDetailReviews(@Body CarDetailRequest appRequestModel);

    @POST("car/details/specifications")
    Call<CarDetailsSpecificationModel> getCarDetailSpeciification(@Body CarDetailRequest appRequestModel);

    @POST("car/details/porfomainvoice")
    Call<CarPorfomaInvoiceModel> getCarPorfomainvoice(@Body CarDetailRequest appRequestModel);


}
