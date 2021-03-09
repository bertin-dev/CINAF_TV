package com.cinaf.android_tv.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.leanback.widget.Presenter;

import com.cinaf.android_tv.R;
import com.cinaf.android_tv.data.models.Actor;


public class DetailDescriptionPresenter extends Presenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_detail, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Actor actor = (Actor) item;
        if(actor != null){
            DetailViewHolder holder = (DetailViewHolder) viewHolder;
            holder.bindActor(actor);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
