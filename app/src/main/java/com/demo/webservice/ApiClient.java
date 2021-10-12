package com.demo.webservice;

import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ApiClient {

    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    /**
     * The constant MULTIPART_IMAGE_FORM_DATA.
     */
    private static final String MULTIPART_IMAGE_FORM_DATA = "image/*";

    /**
     * Create request body request body.
     *
     * @param file the file
     * @return the request body
     */
    public static RequestBody createImageRequestBody(@NonNull File file) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_IMAGE_FORM_DATA), file);
    }

    /**
     * Create request body request body.
     *
     * @param file the file
     * @return the request body
     */
    public static RequestBody createFormDataRequestBody(@NonNull File file) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), file);
    }

    /**
     * Create request body request body.
     *
     * @param s the s
     * @return the request body
     */
    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse("text/plain"), (s == null) ? "" : s);
    }


    /**
     * Create request body request body.
     *
     * @param f the s
     * @return the request body
     */
    public static RequestBody createRequestBodyFromNonString(@NonNull Float f) {
        return RequestBody.create(
                MediaType.parse("text/plain"), f.toString());
    }

    public static RequestBody createRequestBodyFromNonString(@NonNull int f) {
        return RequestBody.create(
                MediaType.parse("text/plain"), "" + f);
    }

    public static RequestBody createRequestBodyFromNonString(@NonNull Double f) {
        return RequestBody.create(
                MediaType.parse("text/plain"), f.toString());
    }

    public static RequestBody createRequestBodyFromLong(long l) {
        return RequestBody.create(
                MediaType.parse("text/plain"), String.valueOf(l));
    }

    public static RequestBody createRequestBody(boolean s) {
        return RequestBody.create(
                MediaType.parse("text/plain"), s ? "1" : "0");
    }




}