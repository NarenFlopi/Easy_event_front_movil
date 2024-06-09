package com.example.easy_event_app.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginAPICliente {

    //private static final String URL = "http://25.38.254.134:8000/api/";

    private static final String URL = "https://easyevent.api.adsocidm.com/api/";
    //private static final String URL = "http://10.201.194.86:8000/api/";
    //private static final String URL = "http://10.0.2.2:8000/api/";
    private static LoginAPIService instance;

    public static LoginAPIService getLoginService() {
        if (instance==null) {
            Retrofit http  = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            instance = http.create(LoginAPIService.class);

        }
        return instance;
    }

}
