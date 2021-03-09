package com.cinaf.android_tv.ui.base;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.PageRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowHeaderPresenter;

import com.cinaf.android_tv.R;


public class IconHeaderItemPresenter extends RowHeaderPresenter {

    private float mUnselectedAlpha;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        mUnselectedAlpha = viewGroup.getResources()
                .getFraction(R.fraction.lb_browse_header_unselect_alpha, 1, 1);
        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_icon_header, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object o) {
        HeaderItem headerItem = ((PageRow) o).getHeaderItem();
        View rootView = viewHolder.view;
        rootView.setAlpha(mUnselectedAlpha);
        setIconDrawable(headerItem.getName(), rootView);
        TextView label = (TextView) rootView.findViewById(R.id.header_label);
        label.setText(headerItem.getName());
    }

    public void setIconDrawable(String name, View rootView) {
        Resources res = rootView.getResources();
        String[] menu_list = res.getStringArray(R.array.menu_list);
        TypedArray resources = res.obtainTypedArray(R.array.icons);
        int drawableResource = 0;

            for (int i = 0; i < menu_list.length; i++) {
                if (menu_list[i].equals(name)) {
                    drawableResource = resources.getResourceId(i, 0);
                    break;
                }
            }

        resources.recycle();

        if (drawableResource != 0) {
            ImageView iconView = (ImageView) rootView.findViewById(R.id.header_icon);
            Drawable icon = res.getDrawable(drawableResource, null);
            iconView.setImageDrawable(icon);
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        // no op
    }

    // TODO: TEMP - remove me when leanback onCreateViewHolder no longer sets the mUnselectAlpha,AND
    // also assumes the xml inflation will return a RowHeaderView
    @Override
    protected void onSelectLevelChanged(ViewHolder holder) {
        // this is a temporary fix
        holder.view.setAlpha(mUnselectedAlpha + holder.getSelectLevel() *
                (1.0f - mUnselectedAlpha));
    }

}

