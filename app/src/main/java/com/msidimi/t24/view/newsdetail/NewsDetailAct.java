package com.msidimi.t24.view.newsdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.msidimi.t24.R;
import com.msidimi.t24.common.Constants;
import com.msidimi.t24.model.News;
import com.msidimi.t24.view.BaseAct;

import java.util.ArrayList;

/**
 * Created by mucahit on 18/09/16.
 */
public class NewsDetailAct extends BaseAct {

    private ViewPager viewPager;
    private ArrayList<News> newsArrayList;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_news_detail);

        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(Constants.IK_NEWS_ARRAY_LIST))
                newsArrayList = getIntent().getExtras().getParcelableArrayList(Constants.IK_NEWS_ARRAY_LIST);
            if (getIntent().getExtras().containsKey(Constants.IK_NEWS_POSITION))
                position = getIntent().getExtras().getInt(Constants.IK_NEWS_POSITION, 0);

            viewPager = (ViewPager) findViewById(R.id.view_pager);
            viewPager.setOffscreenPageLimit(1);
            if (newsArrayList != null) {
                setViewPagerAdapter();
                viewPager.setCurrentItem(position);
            }
        }
    }

    private void setViewPagerAdapter() {
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new NewsDetailFrag().newInstance(newsArrayList.get(position).getId());
            }

            @Override
            public int getCount() {
                if (newsArrayList != null)
                    return newsArrayList.size();
                else
                    return 0;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
