package com.quotify.www.quotify.services;

import com.quotify.www.quotify.models.Question;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by cbono on 11/13/16.
 * Service for issuing HTTP requests to server
 */
public class QuotifyService {
    private static QuotifyService ourInstance = new QuotifyService();

    public static QuotifyService getInstance() {
        return ourInstance;
    }

    public static final String QUESTIONS_API = "/questions/";

    private static final String BASE_URL = "localhost:1323/";

    private RetrofitService service;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client = new OkHttpClient();

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private QuotifyService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        service = retrofit.create(RetrofitService.class);
    }

    private interface RetrofitService {
        @GET("questions/{id}")
        Call<Question> getQuestion(@Path("id") int id);
    }
}
