package com.thinkmobiles.easyerp.data.services;

import com.thinkmobiles.easyerp.presentation.utils.Constants;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Lynx on 1/13/2017.
 */

public interface LoginService {

    @FormUrlEncoded
    @POST(Constants.POST_LOGIN)
    Observable<Void> login(@Field("login") String login, @Field("pass") String pass, @Field("dbId") String dbId, @Field("rememberMe") boolean isRememberMe);

    @FormUrlEncoded
    @POST(Constants.POST_FORGOT_PASSWORD)
    Observable<Void> forgotPassword(@Field("login") String login, @Field("dbId") String dbId);
}
