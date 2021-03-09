package com.cinaf.android_tv.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("last")
    @Expose
    private String last;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("values")
    @Expose
    private List<ApiValue> values = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ApiValue> getValues() {
        return values;
    }

    public void setValues(List<ApiValue> values) {
        this.values = values;
    }

    public String getLast() {return last;}

    public void setLast(String last) {
        this.last = last;
    }
}
