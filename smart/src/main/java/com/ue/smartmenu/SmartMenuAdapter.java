package com.ue.smartmenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import name.gudong.statebackground.OneDrawable;

class SmartMenuAdapter extends BaseAdapter implements View.OnClickListener {
    private int[] images;
    private String[] texts;
    private int textBgColorInt;

    private AdapterView.OnItemClickListener listener;

    public SmartMenuAdapter(int[] images, AdapterView.OnItemClickListener itemClickListener) {
        this.images = images;
        this.listener = itemClickListener;
    }

    public SmartMenuAdapter(@ColorInt int bgColor, String[] texts, AdapterView.OnItemClickListener itemClickListener) {
        this.texts = texts;
        this.listener = itemClickListener;

        int r = Color.red(bgColor) + 65;
        int g = Color.green(bgColor) + 65;
        int b = Color.blue(bgColor) + 65;

        if (r > 255) {
            r -= 255;
        }
        if (g > 255) {
            g -= 255;
        }
        if (b > 255) {
            b -= 255;
        }

        this.textBgColorInt = Color.rgb(r, g, b);
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

            Drawable icon = OneDrawable.createBgDrawableWithDarkMode(context, images[i], 0.4f);
            imageView.setImageDrawable(icon);

            targetView = imageView;
        } else {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setText(texts[i]);
            textView.setTextSize(16f);
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundDrawable(getShapeStateListDrawable(textBgColorInt));

            targetView = textView;
        }
        targetView.setOnClickListener(this);
        targetView.setTag(i);

        return targetView;
    }

    private StateListDrawable getShapeStateListDrawable(@ColorInt int fillColor) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(fillColor);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, shapeDrawable);
        stateListDrawable.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
        return stateListDrawable;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            int tag = (int) view.getTag();
            listener.onItemClick(null, view, tag, tag);
        }
    }
}