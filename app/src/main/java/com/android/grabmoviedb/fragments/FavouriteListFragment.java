package com.android.grabmoviedb.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.grabmoviedb.MovieDetailActivity;
import com.android.grabmoviedb.R;
import com.android.grabmoviedb.adapter.FavouriteMovieListAdapter;
import com.android.grabmoviedb.database.MovieResultDb;
import com.android.grabmoviedb.model.MovieResult;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteListFragment extends Fragment implements FavouriteMovieListAdapter.OnItemClickListener {


    public static String FROM_FAVOURITE_KEY = "fromFavourite";
    private RecyclerView mMovieListRecyclerView;
    private TextView mEmptyText;

    public FavouriteListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMovieListRecyclerView = (RecyclerView) view.findViewById(R.id.frag_movie_list_recycler_view);
        mEmptyText = (TextView) view.findViewById(R.id.emptyText);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mMovieListRecyclerView.setLayoutManager(gridLayoutManager);
        loadFavouriteListFromDb();

    }

    private void loadFavouriteListFromDb() {
        Realm realmDb = Realm.getInstance(getActivity());
        RealmResults<MovieResultDb> resultDbs = realmDb.where(MovieResultDb.class).findAll();
        if (resultDbs != null && !resultDbs.isEmpty()) {
            mEmptyText.setVisibility(View.GONE);
            FavouriteMovieListAdapter movieListAdapter = new FavouriteMovieListAdapter(getActivity(), resultDbs);
            mMovieListRecyclerView.setAdapter(movieListAdapter);
            movieListAdapter.setItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        MovieResultDb movieResult = (MovieResultDb) v.getTag();
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MovieListFragment.MOVIE_OVERVIEW_KEY, getMovieResult(movieResult));
        intent.putExtra(FROM_FAVOURITE_KEY, true);
        startActivity(intent);
        getActivity().finish();
    }

    private MovieResult getMovieResult(MovieResultDb movieResultDb) {
        MovieResult movieResult = new MovieResult();
        movieResult.setAdult(movieResultDb.getAdult());
        movieResult.setBackdropPath(movieResultDb.getBackdropPath());
        movieResult.setId(movieResultDb.getId());
        movieResult.setOriginalLanguage(movieResultDb.getOriginalLanguage());
        movieResult.setOriginalTitle(movieResultDb.getOriginalTitle());
        movieResult.setOverview(movieResultDb.getOverview());
        movieResult.setPopularity(movieResultDb.getPopularity());
        movieResult.setReleaseDate(movieResultDb.getReleaseDate());
        movieResult.setTitle(movieResultDb.getTitle());
        movieResult.setPosterPath(movieResultDb.getPosterPath());
        movieResult.setVideo(movieResultDb.getVideo());
        movieResult.setVoteAverage(movieResultDb.getVoteAverage());
        movieResult.setVoteCount(movieResultDb.getVoteCount());

        return movieResult;
    }
}
