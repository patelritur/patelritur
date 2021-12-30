package com.demo.webservice;

import com.demo.carDetails.model.CarDetailRequest;
import com.demo.carDetails.model.CarDetailReviewModel;
import com.demo.carDetails.model.CarDetailsSpecificationModel;
import com.demo.carDetails.model.CarPorfomaInvoiceModel;
import com.demo.faq.model.FAQRequestModel;
import com.demo.faq.model.FAQResponseModel;
import com.demo.home.booking.model.LocationResponse;
import com.demo.home.booking.model.ReviewRequestModel;
import com.demo.home.booking.model.BookingAcceptModel;
import com.demo.home.booking.model.BookingRequestModel;
import com.demo.home.booking.model.BookingResponseModel;
import com.demo.home.booking.model.CancelBookingModel;
import com.demo.home.booking.model.CurrentStatuSModel;
import com.demo.home.booking.model.LaterOptionModel;
import com.demo.home.booking.model.MapLocationResponseModel;
import com.demo.home.booking.model.StatusRequestModel;
import com.demo.home.model.AppContentModel;
import com.demo.home.model.AppRequestModel;
import com.demo.home.model.CarDealerResponseModel;
import com.demo.carDetails.model.CarDetailResponse;
import com.demo.home.model.CarFilterResponse;
import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.CarSearchResultModel;
import com.demo.home.model.CarSectionFilterResponse;
import com.demo.home.model.CategoryResponse;
import com.demo.home.model.MenuResponse;
import com.demo.home.profile.model.DemoTripResponseModel;
import com.demo.home.profile.model.FavoritespecialistModel;
import com.demo.news.NewsResponseModel;
import com.demo.notifications.model.NotificationResponseModel;
import com.demo.registrationLogin.model.AddTokenRequestModel;
import com.demo.registrationLogin.model.CommanRequestModel;
import com.demo.registrationLogin.model.LoginResponseModel;
import com.demo.registrationLogin.model.OtpResponseModel;
import com.demo.registrationLogin.model.PINResponseModel;
import com.demo.registrationLogin.model.RegistrationRequestModel;
import com.demo.registrationLogin.model.RegistrationResponse;

import com.demo.launch.model.LaunchRequestModel;
import com.demo.launch.model.LaunchResponseModel;
import com.demo.rewards.s.model.RewardsResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @Multipart
    @POST("upload/bookingvoicemessage")
    Call<RegistrationResponse> uploadVoiceMessage(@Part MultipartBody.Part File, @Part("UserID")RequestBody userid,@Part("BookingID")RequestBody bookingid);

    @GET("car/filter/price")
    Call<CarFilterResponse> carFilterPrice();

    @GET("car/filter/segment")
    Call<CarFilterResponse> carFilterSegment();

    @GET("car/filter/brand")
    Call<CarFilterResponse> carFilterBrand();
    @GET("car/filter/section")
    Call<CarSectionFilterResponse> carFilterSection();


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

    @POST("car/booking")
    Call<BookingResponseModel> carbooking(@Body BookingRequestModel appRequestModel);

    @POST("car/meet/booking")
    Call<BookingResponseModel> meetingBooking(@Body BookingRequestModel appRequestModel);

    @POST("car/allmaplocation")
    Call<MapLocationResponseModel> allmaplocation(@Body CarDetailRequest appRequestModel);

    @POST("devicetokenforpushnotification")
    Call<RegistrationResponse> addNotificationToken(@Body AddTokenRequestModel addTokenRequestModel);

    @POST("car/booking/later/option")
    Call<LaterOptionModel> getLaterTimeOptions(@Body CarDetailRequest addTokenRequestModel);

    @POST("car/meet/later/option")
    Call<LaterOptionModel> getMeetingLaterTimeOptions(@Body CarDetailRequest addTokenRequestModel);


    @POST("car/booking/currentStatus")
    Call<CurrentStatuSModel> getCurrentStatus(@Body StatusRequestModel addTokenRequestModel);

    @POST("car/meeting/currentstatus")
    Call<CurrentStatuSModel> getMeetingCurrentStatus(@Body StatusRequestModel addTokenRequestModel);



    @POST("getnotification")
    Call<NotificationResponseModel> getNotifications(@Body CarDetailRequest addTokenRequestModel);

    @POST("car/booking/details")
    Call<BookingAcceptModel> getBookingDetails(@Body StatusRequestModel statusRequestModel);

    @POST("car/meeting/details")
    Call<BookingAcceptModel> getMeetingDetails(@Body StatusRequestModel statusRequestModel);


    @POST("car/booking/cancel")
    Call<BookingResponseModel> cancelBooking(@Body CancelBookingModel statusRequestModel);

    @POST("car/meeting/cancel")
    Call<BookingResponseModel> cancelMeeting(@Body CancelBookingModel statusRequestModel);

    @POST("faqs")
    Call<FAQResponseModel> getFaqs(@Body FAQRequestModel statusRequestModel);

    @POST("mydemotrips")
    Call<DemoTripResponseModel> getMyDemoTrips(@Body FAQRequestModel statusRequestModel);
    @POST("favoritespecialist")
    Call<FavoritespecialistModel> getFavouriteSpecialist(@Body FAQRequestModel userId);

    @POST("latestlaunch")
    Call<LaunchResponseModel> getLaunch(@Body LaunchRequestModel userId);

    @POST("news")
    Call<NewsResponseModel> getNews(@Body FAQRequestModel userId);

    @POST("rewardsandoffers")
    Call<RewardsResponseModel> getRewardsOffer(@Body FAQRequestModel userId);

    @POST("car/booking/review")
    Call<RegistrationResponse> insertDemoBookingReview(@Body ReviewRequestModel userId);

    @POST("car/meet/review")
    Call<RegistrationResponse> addMeetingBookingReview(@Body ReviewRequestModel userId);

    @POST("car/specialist/feedback")
    Call<RegistrationResponse> addSpecialistFeedback(@Body ReviewRequestModel userId);


    @GET("specialistlocation/{specialist_id}")
    Call<LocationResponse> getSpecialistLocation(@Path("specialist_id") String specialistId);

}
