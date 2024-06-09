package com.example.easy_event_app.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductoAPICliente {

    //private static final String URL = "http://25.38.254.134:8000/api/";

    private static final String URL = "https://easyevent.api.adsocidm.com/api/";
    //private static final String URL = "http://10.201.194.86:8000/api/";
    //private static final String URL = "http://10.0.2.2:8000/api/";
    private static ProductoAPIService instance;

    public static ProductoAPIService getProductoService() {
        if (instance==null) {

            final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            Retrofit http  = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            instance = http.create(ProductoAPIService.class);

        }
        return instance;
    }

}
