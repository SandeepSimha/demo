package com.sancheru.demo.network;

import java.io.File;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("/s/s1dqshs8s4mkxq4/AZipFile.zip?dl=0")
    Call<File> getZipFile();
}