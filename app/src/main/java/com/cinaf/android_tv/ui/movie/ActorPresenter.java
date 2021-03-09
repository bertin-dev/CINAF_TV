package com.cinaf.android_tv.ui.movie;

import android.view.ViewGroup;

import androidx.leanback.widget.Presenter;

import com.cinaf.android_tv.data.models.Actor;


public class ActorPresenter extends Presenter {

    public ActorPresenter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(new ActorCardView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ((ActorCardView) viewHolder.view).bind((Actor) item);
    }



    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
