package com.android.grabmoviedb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.grabmoviedb.R;
import com.android.grabmoviedb.database.MovieResultDb;
import com.android.grabmoviedb.utils.AppUtils;
import com.bumptech.glide.Glide;

import io.realm.RealmResults;

/**
 * Created by hp pc on 06-03-2016.
 */
public class FavouriteMovieListAdapter extends RecyclerView.Adapter<FavouriteMovieListAdapter.ViewHolder> {

    private Context mContext;
    private RealmResults<MovieResultDb> mMovieResultList;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private OnItemClickListener mOnItemClickListener;


    public FavouriteMovieListAdapter(Context context, RealmResults<MovieResultDb> movieResultDbList) {
        mContext = context;
        mMovieResultList = movieResultDbList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.image_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieResultDb data = mMovieResultList.get(position);
        Glide.with(mContext).load(AppUtils.createImageUrl(mContext, data.getPosterPath()))
                .error(R.drawable.placeholder)
                .into(holder.moviePosterImage);
        holder.movieView.setTag(data);
        setAnimation(holder.moviePosterImage, position);
    }

    @Override
    public int getItemCount() {
        return mMovieResultList.size();
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void setItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView moviePosterImage;
        protected View movieView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.moviePosterImage = (ImageView) itemView.findViewById(R.id.movie_image);
            movieView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}

