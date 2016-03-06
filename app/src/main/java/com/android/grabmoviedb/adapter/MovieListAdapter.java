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
import com.android.grabmoviedb.model.MovieResult;
import com.android.grabmoviedb.utils.AppUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp pc on 04-03-2016.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private Context mContext;
    private List<MovieResult> mMovieResultList = new ArrayList<>();
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private OnItemClickListener mOnItemClickListener;
    private boolean fromMovieDetail;


    public MovieListAdapter(Context context, boolean fromMovieDetail) {
        mContext = context;
        this.fromMovieDetail = fromMovieDetail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = fromMovieDetail ? R.layout.movie_detail_image_list_item : R.layout.image_list_item;
        View view = LayoutInflater.from(mContext).inflate(
                layoutId, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieResult data = mMovieResultList.get(position);
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

    public void addItems(List<MovieResult> movieResultList) {
        mMovieResultList.addAll(movieResultList);
        notifyItemRangeChanged(0, getItemCount());
        //notifyDataSetChanged();
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
