package com.android.grabmoviedb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.grabmoviedb.fragments.FavouriteListFragment;
import com.android.grabmoviedb.fragments.MovieDetailFragment;
import com.android.grabmoviedb.fragments.MovieListFragment;
import com.android.grabmoviedb.model.MovieResult;
import com.android.grabmoviedb.utils.AppUtils;
import com.bumptech.glide.Glide;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView mBackdropImage;
    private MovieResult movieResult;
    private boolean fromfavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieResult = getIntent().getParcelableExtra(MovieListFragment.MOVIE_OVERVIEW_KEY);
        fromfavourite = getIntent().getBooleanExtra(FavouriteListFragment.FROM_FAVOURITE_KEY, false);
        loadFragment();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mBackdropImage = (ImageView) findViewById(R.id.backdrop_image);
        toolbar.setTitle(movieResult.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadBackdropImage(movieResult.getBackdropPath());

    }

    private void loadFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MovieListFragment.MOVIE_OVERVIEW_KEY, movieResult);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, MovieDetailFragment.TAG).commit();
    }

    private void loadBackdropImage(String url) {
        Glide.with(this).load(AppUtils.createImageUrl(this, url))
                .error(R.drawable.placeholder)
                .into(mBackdropImage);
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.TAB_POS, 1);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (fromfavourite)
                startHomeActivity();
            else
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (fromfavourite)
            startHomeActivity();
        super.onBackPressed();
    }
}
