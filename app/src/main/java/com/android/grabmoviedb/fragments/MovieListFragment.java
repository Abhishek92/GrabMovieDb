package com.android.grabmoviedb.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.grabmoviedb.MovieDetailActivity;
import com.android.grabmoviedb.R;
import com.android.grabmoviedb.adapter.MovieListAdapter;
import com.android.grabmoviedb.backend.GrabMovieApiClient;
import com.android.grabmoviedb.model.MovieResult;
import com.android.grabmoviedb.model.NowPlayingMovieList;
import com.android.grabmoviedb.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements MovieListAdapter.OnItemClickListener {


    public final static String MOVIE_OVERVIEW_KEY = "movie_overview";
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private RecyclerView mMovieListRecyclerView;
    private TextView mEmptyText;
    private ProgressBar mProgressBar;
    private List<MovieResult> mMovieResultList = new ArrayList<>();
    private int pageNo = 1;
    private boolean loading = true;
    private GridLayoutManager mGridLayoutManager;
    private MovieListAdapter movieListAdapter;

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mMovieListRecyclerView = (RecyclerView) view.findViewById(R.id.frag_movie_list_recycler_view);
        mEmptyText = (TextView) view.findViewById(R.id.emptyText);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        movieListAdapter = new MovieListAdapter(getActivity(), false);
        mMovieListRecyclerView.setLayoutManager(mGridLayoutManager);
        loadMoreItemsOnScroll();
        changeVisibility(View.GONE, View.VISIBLE);
        if (AppUtils.isNetworkConnected(getActivity())) {
            getNowPlayingMovieList(pageNo);
        } else {
            changeVisibility(View.VISIBLE, View.GONE);
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get list of now playing movies from server
     */
    private void getNowPlayingMovieList(int pageNo) {
        GrabMovieApiClient.getGrabMovieApi().getListOfMovies(getString(R.string.api_key), pageNo, new Callback<NowPlayingMovieList>() {
            @Override
            public void success(NowPlayingMovieList nowPlayingMovieList, Response response) {
                if (nowPlayingMovieList != null && nowPlayingMovieList.getMovieResults() != null) {
                    loading = true;
                    mMovieResultList = new ArrayList<>(nowPlayingMovieList.getMovieResults());
                    setNowPlayingMovieListAdapter();
                    changeVisibility(View.GONE, View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                changeVisibility(View.VISIBLE, View.GONE);
            }
        });
    }

    private void setNowPlayingMovieListAdapter() {
        if (!mMovieResultList.isEmpty()) {
            if (pageNo == 1) {

                movieListAdapter.addItems(mMovieResultList);
                mMovieListRecyclerView.setAdapter(movieListAdapter);
                movieListAdapter.setItemClickListener(this);
                movieListAdapter.notifyItemRangeChanged(0, movieListAdapter.getItemCount());
            } else {
                movieListAdapter.addItems(mMovieResultList);

            }
        } else
            changeVisibility(View.VISIBLE, View.GONE);
    }

    /**
     * Change visibility of empty text and progress bar.
     *
     * @param progressVisibility
     * @param textVisibility
     */
    private void changeVisibility(int textVisibility, int progressVisibility) {
        mEmptyText.setVisibility(textVisibility);
        mProgressBar.setVisibility(progressVisibility);
    }

    @Override
    public void onItemClick(View v, int position) {
        MovieResult movieResult = (MovieResult) v.getTag();
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MOVIE_OVERVIEW_KEY, movieResult);
        startActivity(intent);
    }

    private void loadMoreItemsOnScroll() {
        mMovieListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mGridLayoutManager.getChildCount();
                    totalItemCount = mGridLayoutManager.getItemCount();
                    pastVisiblesItems = mGridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            pageNo++;
                            getNowPlayingMovieList(pageNo);
                        }
                    }
                }
            }
        });
    }
}
