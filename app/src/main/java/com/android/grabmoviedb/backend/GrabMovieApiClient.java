package com.android.grabmoviedb.backend;

import retrofit.RestAdapter;

/**
 * Created by hp pc on 03-03-2016.
 */
public class GrabMovieApiClient {
    private final static String API_URL = "https://api.themoviedb.org/3/";
    private static GrabMovieApiInterface grabMovieApiInterface;

    private GrabMovieApiClient(){}

    /**
     * Create a Rest adapter for api.
     *
     * @return
     */
    private static RestAdapter getRestAdapter() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return restAdapter;
    }

    /**
     * Reteruns rest interface of image search api.
     * @return
     */
    public static GrabMovieApiInterface getGrabMovieApi() {
        if (grabMovieApiInterface == null)
            grabMovieApiInterface = getRestAdapter().create(GrabMovieApiInterface.class);

        return grabMovieApiInterface;
    }

}
