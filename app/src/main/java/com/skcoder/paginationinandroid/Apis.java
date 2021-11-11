package com.skcoder.paginationinandroid;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Apis {

    @GET("api/users")
    Call<ProfileModel> getData(@Query("page") Integer id);
}
