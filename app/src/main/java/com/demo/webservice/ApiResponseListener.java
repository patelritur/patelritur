package com.demo.webservice;

import retrofit2.Call;


public interface ApiResponseListener {
    /**
     * Call on success API response with request code
     *
     * @param call     the call
     * @param response the response
     * @param reqCode  the req code
     * @throws Exception the exception
     */
    void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception;

    /**
     * On api error.
     *
     * @param call     the call
     * @param response the response
     * @param reqCode  the req code
     * @throws Exception the exception
     */
    void onApiError(Call<Object> call, Object response, int reqCode) throws Exception;
    /*Call on error of API with request code */
//    void onApiError(Call<Object> call, Object object, int reqCode);

}