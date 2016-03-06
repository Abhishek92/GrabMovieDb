package com.android.grabmoviedb.backend;

import com.android.grabmoviedb.model.NowPlayingMovieList;
import com.android.grabmoviedb.model.SimilarMovieList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by hp pc on 03-03-2016.
 */
public interface GrabMovieApiInterface {

    @GET("/movie/now_playing")
    void getListOfMovies(@Query("api_key") String apiKey, @Query("page") int pageNo, Callback<NowPlayingMovieList> nowPlayingMovieListCallback);

    @GET("/movie/{movieId}/similar")
    void getListOfSimilarMovies(@Path("movieId") String movieId, @Query("api_key") String apiKey, @Query("page") int pageNo, Callback<SimilarMovieList> similarMovieListCallback);
}
