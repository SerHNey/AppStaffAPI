package com.example.example;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitApi {

    @PUT("GamesModels")
    Call<Game> updateData(@Query("ID")int id, @Body Game game);

    @POST("GamesModels")
    Call<Game> postData(@Body Game game);

    @DELETE("GamesModels")
    Call<Game> deleteData(@Query("ID") int id);
}
