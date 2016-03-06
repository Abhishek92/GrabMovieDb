package com.android.grabmoviedb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.grabmoviedb.MainActivity;
import com.android.grabmoviedb.MovieDetailActivity;
import com.android.grabmoviedb.R;
import com.android.grabmoviedb.adapter.MovieListAdapter;
import com.android.grabmoviedb.backend.GrabMovieApiClient;
import com.android.grabmoviedb.database.MovieResultDb;
import com.android.grabmoviedb.model.MovieResult;
import com.android.grabmoviedb.model.SimilarMovieList;
import com.android.grabmoviedb.utils.AppUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment implements MovieListAdapter.OnItemClickListener, View.OnClickListener {

    public static final String TAG = "MovieDetailFragment";
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private MovieResult mMovieResult;
    private ImageView mPosterImageUrl;
    private TextView mOverview;
    private TextView mMovieName;
    private RecyclerView mRelatedMovieListRV;
    private LinearLayout mSimilarMoviesContainer;
    private TextView mReleaseDate;
    private TextView mMarkAsFavourite;
    private MovieListAdapter movieListAdapter;
    private ArrayList<MovieResult> mMovieResultList = new ArrayList<>();
    private int pageNo = 1;
    private boolean loading = true;
    private LinearLayoutManager mLinearLayoutManager;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(MovieListFragment.MOVIE_OVERVIEW_KEY)) {
            mMovieResult = getArguments().getParcelable(MovieListFragment.MOVIE_OVERVIEW_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPosterImageUrl = (ImageView) view.findViewById(R.id.moviePoster);
        mOverview = (TextView) view.findViewById(R.id.movie_overview);
        mMovieName = (TextView) view.findViewById(R.id.movieName);
        mReleaseDate = (TextView) view.findViewById(R.id.releaseDate);
        mMarkAsFavourite = (TextView) view.findViewById(R.id.mark_favourite);
        mSimilarMoviesContainer = (LinearLayout) view.findViewById(R.id.similarMoviesContainer);
        mRelatedMovieListRV = (RecyclerView) view.findViewById(R.id.reletedMovieRv);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        movieListAdapter = new MovieListAdapter(getActivity(), true);
        mRelatedMovieListRV.setLayoutManager(mLinearLayoutManager);
        mMarkAsFavourite.setOnClickListener(this);
        setMovieData();
    }

    private void setMovieData() {
        mOverview.setText(mMovieResult.getOverview());
        mMovieName.setText(mMovieResult.getTitle());
        mReleaseDate.setText(AppUtils.getFormattedDate(mMovieResult.getReleaseDate()));
        checkIsMovieFavourite();
        Glide.with(this).load(AppUtils.createImageUrl(getActivity(), mMovieResult.getPosterPath()))
                .error(R.drawable.placeholder)
                .into(mPosterImageUrl);
        loadMoreItemsOnScroll();
        getSimilarMovieList(pageNo, mMovieResult.getId() + "");
    }

    /**
     * Get list of now playing movies from server
     */
    private void getSimilarMovieList(int pageNo, String movieId) {
        GrabMovieApiClient.getGrabMovieApi().getListOfSimilarMovies(movieId, getString(R.string.api_key), pageNo, new Callback<SimilarMovieList>() {
            @Override
            public void success(SimilarMovieList similarMovieList, Response response) {
                if (similarMovieList != null && similarMovieList.getMovieResults() != null) {
                    loading = true;
                    mMovieResultList = new ArrayList<>(similarMovieList.getMovieResults());
                    setNowPlayingMovieListAdapter();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void setNowPlayingMovieListAdapter() {
        if (!mMovieResultList.isEmpty()) {
            mSimilarMoviesContainer.setVisibility(View.VISIBLE);
            if (pageNo == 1) {
                movieListAdapter.addItems(mMovieResultList);
                mRelatedMovieListRV.setAdapter(movieListAdapter);
                movieListAdapter.notifyItemRangeChanged(0, movieListAdapter.getItemCount());
                movieListAdapter.setItemClickListener(this);
            } else {
                movieListAdapter.addItems(mMovieResultList);
            }
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        MovieResult movieResult = (MovieResult) v.getTag();
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MovieListFragment.MOVIE_OVERVIEW_KEY, movieResult);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mark_favourite) {
            if (mMarkAsFavourite.getText().toString().equals(getString(R.string.favourite_mark_txt)))
                markMovieAsFavourite();
            else
                removeAsFavourite();
        }
    }

    private void checkIsMovieFavourite() {
        Realm realmDb = Realm.getInstance(getActivity());
        RealmQuery<MovieResultDb> query = realmDb.where(MovieResultDb.class).equalTo("id", mMovieResult.getId());
        RealmResults<MovieResultDb> realmResults = query.findAll();
        if (realmResults != null && !realmResults.isEmpty())
            mMarkAsFavourite.setText(R.string.remove_favourite_mark_text);
    }

    private void removeAsFavourite() {
        Realm realmDb = Realm.getInstance(getActivity());
        RealmQuery<MovieResultDb> query = realmDb.where(MovieResultDb.class).equalTo("id", mMovieResult.getId());
        RealmResults<MovieResultDb> realmResults = query.findAll();
        realmDb.beginTransaction();
        if (realmResults != null && !realmResults.isEmpty())
            realmResults.clear();
        realmDb.commitTransaction();

        mMarkAsFavourite.setText(R.string.favourite_mark_txt);
        Toast.makeText(getActivity(), R.string.msg_favourite_removed, Toast.LENGTH_SHORT).show();
        startHomeActivity();
    }

    private void markMovieAsFavourite() {
        Realm realmDb = Realm.getInstance(getActivity());
        realmDb.beginTransaction();
        // Create an object
        MovieResultDb movieResultDb = realmDb.createObject(MovieResultDb.class);
        movieResultDb.setAdult(mMovieResult.getAdult());
        movieResultDb.setBackdropPath(mMovieResult.getBackdropPath());
        movieResultDb.setId(mMovieResult.getId());
        movieResultDb.setOriginalLanguage(mMovieResult.getOriginalLanguage());
        movieResultDb.setOriginalTitle(mMovieResult.getOriginalTitle());
        movieResultDb.setOverview(mMovieResult.getOverview());
        movieResultDb.setPopularity(mMovieResult.getPopularity());
        movieResultDb.setReleaseDate(mMovieResult.getReleaseDate());
        movieResultDb.setTitle(mMovieResult.getTitle());
        movieResultDb.setPosterPath(mMovieResult.getPosterPath());
        movieResultDb.setVideo(mMovieResult.getVideo());
        movieResultDb.setVoteAverage(mMovieResult.getVoteAverage());
        movieResultDb.setVoteCount(mMovieResult.getVoteCount());
        //Commit transaction
        realmDb.commitTransaction();

        mMarkAsFavourite.setText(R.string.remove_favourite_mark_text);
        Toast.makeText(getActivity(), R.string.msg_favourite_marked, Toast.LENGTH_SHORT).show();
        startHomeActivity();

    }

    private void loadMoreItemsOnScroll() {
        mRelatedMovieListRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dx > 0) //check for scroll down
                {
                    visibleItemCount = mLinearLayoutManager.getChildCount();
                    totalItemCount = mLinearLayoutManager.getItemCount();
                    pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            pageNo++;
                            getSimilarMovieList(pageNo, mMovieResult.getId() + "");
                        }
                    }
                }
            }
        });
    }

    private void startHomeActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MainActivity.TAB_POS, 1);
        startActivity(intent);
        getActivity().finish();

    }
}
