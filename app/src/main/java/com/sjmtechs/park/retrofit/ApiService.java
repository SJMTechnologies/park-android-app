package com.sjmtechs.park.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("web-services.php?web_method=getCountry")
    Call<String> getCountry();

    @GET("web-services.php?web_method=getState")
    Call<String> getStates(@Query("country_id") String id);

//    @GET("web-services.php?web_method=login")
//    Call<String> doLogin(@Query("email") String email, @Query("password") String password);

    @POST("web-services.php?web_method=signup")
    Call<String> doSignUp(@Query("email") String email, @Query("password") String password,
                          @Query("fname") String fname, @Query("lname") String lname,
                          @Query("business_name") String businessName, @Query("address") String address,
                          @Query("address2") String address2, @Query("city") String city,
                          @Query("state") String state, @Query("zip") String zip,
                          @Query("country") String country, @Query("phone") String phone,
                          @Query("fax") String fax, @Query("subscribe") String subscribe);

}
