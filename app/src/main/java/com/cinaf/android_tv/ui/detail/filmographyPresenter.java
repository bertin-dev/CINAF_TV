package com.cinaf.android_tv.ui.detail;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cinaf.android_tv.R;
import com.cinaf.android_tv.data.models.Actor;
import com.cinaf.android_tv.data.models.Poster;
import com.cinaf.android_tv.ui.base.BindableCardView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class filmographyPresenter extends Presenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(new FilmographyCardView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ((FilmographyCardView) viewHolder.view).bind((Poster) item);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }


    public static class FilmographyCardView extends BindableCardView<Poster> {

        @BindView(R.id.poster_iv)
        ImageView mPosterIV;

        public FilmographyCardView(Context context) {
            super(context);
            ButterKnife.bind(this);
        }

        @Override
        protected void bind(Poster poster) {
            Glide.with(getContext())
                    .load(poster.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mPosterIV);

        }

        public ImageView getPosterIV() {
            return mPosterIV;
        }

        @Override
        protected int getLayoutResource() {
            return R.layout.card_filmography;
        }
    }

}
