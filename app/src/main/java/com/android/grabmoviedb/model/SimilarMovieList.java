package com.android.grabmoviedb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp pc on 03-03-2016.
 */
public class SimilarMovieList {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<MovieResult> movieResults = new ArrayList<MovieResult>();
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    /**
     *
     * @return
     * The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }



    /**
     *
     * @return
     * The totalPages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    public List<MovieResult> getMovieResults() {
        return movieResults;
    }

    public void setMovieResults(List<MovieResult> movieResults) {
        this.movieResults = movieResults;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    /**
     *
     * @return
     * The totalResults
     */
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     *
     * @param totalResults
     * The total_results
     */
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
