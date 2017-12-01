package com.ue.smartmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jake.smart.R;

import name.gudong.statebackground.OneDrawable;

class SmartMenuAdapter extends BaseAdapter implements View.OnClickListener {
    private int[] images;
    private String[] texts;
    private AdapterView.OnItemClickListener listener;

    public SmartMenuAdapter(int[] images, AdapterView.OnItemClickListener itemClickListener) {
        this.images = images;
        this.listener = itemClickListener;
    }

    public SmartMenuAdapter(String[] texts, AdapterView.OnItemClickListener itemClickListener) {
        this.texts = texts;
        this.listener = itemClickListener;
    }

    @Override
    public int getCount() {
        if (images != null) {
            return images.length;
        }
        if (texts != null) {
            return texts.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View targetView;
        Context context = viewGroup.getContext();
        if (images != null) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(30, 30));

            Drawable icon = OneDrawable.createBgDrawableWithDarkMode(context, images[i], 0.4f);
            imageView.setImageDrawable(icon);

            targetView = imageView;
        } else {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            textView.setText(texts[i]);
            textView.setTextSize(16f);
            textView.setTextColor(ContextCompat.getColor(context, R.color.white));

            targetView = textView;
        }
        targetView.setOnClickListener(this);
        targetView.setTag(i);

        return targetView;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            int tag = (int) view.getTag();
            listener.onItemClick(null, view, tag, tag);
        }
    }
}