package com.example.intcore_demo.login;

import android.view.View;

public interface CustomItemClickListener<T> {
    void onItemClick(View v, T object);
}
