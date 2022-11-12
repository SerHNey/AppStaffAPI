package com.example.appforstaff;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetrofitAPI {
    @POST("Staffs")
    Call<Staff> createPost(@Body Staff staff);

    @PUT("Staffs")
    Call<Staff> updateData(@Query("id") int Id, @Body Staff staff);

    @DELETE("Staffs")
    Call<Staff> deleteData(@Query("id") int Id);
}

