package com.example.intcore_demo.helper;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.intcore_demo.R;

public class NewsDataBinding {


    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_account_circle_black_24dp))
                .into(view);
    }
}