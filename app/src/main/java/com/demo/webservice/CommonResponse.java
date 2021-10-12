package com.demo.webservice;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Common response.
 */
@Keep
public class CommonResponse<T> {

    @SerializedName(value = "Message", alternate = "message")
    @Expose
    private String Message;
    @SerializedName("Version")
    @Expose
    private String Version;
    @SerializedName("StatusCode")
    @Expose
    private int StatusCode;
    @SerializedName("Result")
    @Expose
    private String Result;
    @SerializedName("Data")
    @Expose
    private T Data;

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return this.Message == null ? "" : Message;
    }

    /**
     * Sets message.
     *
     * @param Message the message
     */
    public void setMessage(String Message) {
        this.Message = Message;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return this.Version == null ? "" : Version;
    }

    /**
     * Sets version.
     *
     * @param Version the version
     */
    public void setVersion(String Version) {
        this.Version = Version;
    }

    /**
     * Gets status code.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return this.StatusCode;
    }

    /**
     * Sets status code.
     *
     * @param StatusCode the status code
     */
    public void setStatusCode(int StatusCode) {
        this.StatusCode = StatusCode;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public String getResult() {
        return this.Result == null ? "" : Result;
    }

    /**
     * Sets result.
     *
     * @param Result the result
     */
    public void setResult(String Result) {
        this.Result = Result;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}