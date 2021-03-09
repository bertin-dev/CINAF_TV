package com.cinaf.android_tv.ui.detail;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cinaf.android_tv.R;
import com.cinaf.android_tv.data.models.Actor;
import com.cinaf.android_tv.ui.base.BindableCardView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PersonPresenter_casting extends Presenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(new MovieCardView_2(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ((MovieCardView_2) viewHolder.view).bind((Actor) item);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }


    public static class MovieCardView_2 extends BindableCardView<Actor> {

        @BindView(R.id.poster_iv)
        ImageView mPosterIV;

        @BindView(R.id.title_tv)
        TextView title_tv;

        @BindView(R.id.sub_title)
        TextView sub_title;

        public MovieCardView_2(Context context) {
            super(context);
            ButterKnife.bind(this);
        }

        @Override
        protected void bind(Actor actor) {
            Glide.with(getContext())
                    .load(actor.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mPosterIV);

            title_tv.setText(actor.getName());
            sub_title.setText(actor.getType());
        }


        @Override
        protected int getLayoutResource() {
            return R.layout.card_casting;
        }
    }

}
