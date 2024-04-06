package com.example.fms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.hospitalfood.R;

public class AdminPagerAdapter extends PagerAdapter {
    private final Context mContext;
    private final int[] mLayouts = {
            R.layout.activity_admin,
            R.layout.activity_registration,
            R.layout.activity_pending_orders,
            R.layout.activity_served_breakfast_orders
    };

    public AdminPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mLayouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(mLayouts[position], container, false);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
