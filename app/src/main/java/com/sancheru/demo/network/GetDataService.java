package com.sancheru.demo.network;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetDataService {

    //https://koenig-media.raywenderlich.com/uploads/2018/08/RW_Kotlin_Cheatsheet_1_0.pdf
    //https://www.dropbox.com/s/s1dqshs8s4mkxq4/AZipFile.zip?dl=0

    // option 1: a resource relative to your base URL
    @GET("/s/s1dqshs8s4mkxq4/AZipFile.zip")
    Call<ResponseBody> downloadFileWithFixedUrl();

    // option 2: using a dynamic URL
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}