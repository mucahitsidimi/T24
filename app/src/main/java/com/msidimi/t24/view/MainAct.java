package com.msidimi.t24.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.msidimi.t24.R;
import com.msidimi.t24.T24Application;
import com.msidimi.t24.common.Constants;
import com.msidimi.t24.listener.OnNewsClickListener;
import com.msidimi.t24.model.Category;
import com.msidimi.t24.model.News;
import com.msidimi.t24.view.adapter.CategoriesSpinnerAdapter;
import com.msidimi.t24.view.adapter.NewsAdapter;
import com.msidimi.t24.view.newsdetail.NewsDetailAct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainAct extends BaseAct implements OnNewsClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private RecyclerView rvNews;
    private NewsAdapter adapter;
    private Runnable mHandlerTask;
    private Handler mHandler;
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private ArrayList<News> slideNewsArrayList;
    private SliderLayout mSlider;
    private Spinner spinner;
    private int newsPagingIndex = 2;
    private boolean mLoading;
    private boolean hasMoreNews = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initViews();
        getCategories();
        mHandler = new Handler();
        mHandlerTask = new Runnable() {
            @Override
            public void run() {
                refreshNews();
                mHandler.postDelayed(mHandlerTask, Constants.NEWS_REFRESH_TIME);
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRepeatingTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRepeatingTask();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSlider = (SliderLayout) findViewById(R.id.slider);
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        rvNews = (RecyclerView) findViewById(R.id.rv_news);
        rvNews.setLayoutManager(mLinearLayoutManager);
        rvNews.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItem = mLinearLayoutManager.getItemCount();
                int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                if (!mLoading && lastVisibleItem == totalItem - 1 && hasMoreNews) {
                    getNews();
                }
            }
        });
    }

    /**
     * Every 2 minutes the news are refreshing. For refreshing we are cleaning the arraylist and paging counter.
     */
    private void refreshNews() {
        newsArrayList.clear();
        newsPagingIndex = 2;
        getSliderNews();
        getNews();
    }

    /**
     * Getting news categories from service and setting spinner adapter.
     */
    private void getCategories() {
        String url = getString(R.string.categories);

        showProgressDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                ArrayList<Category> categoriesArrayList = new ArrayList<>();
                JSONArray newsJsonArray = jsonObject.optJSONArray("data");
                for (int index = 0; index < newsJsonArray.length(); index++) {
                    try {
                        JSONObject item = (JSONObject) newsJsonArray.get(index);
                        categoriesArrayList.add(new Category(item));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                spinner.setAdapter(new CategoriesSpinnerAdapter(MainAct.this, categoriesArrayList));
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (!isFinishing()) {
                    handleVolleyError(volleyError);
                    hideProgressDialog();
                }
            }
        });

        T24Application.getInstance().addToRequestQueue(jsonObjectRequest, Constants.API_TAG_GET_CATEGORIES);
    }

    /**
     * Getting sliders news (page=1)
     */
    private void getSliderNews() {
        StringBuilder stringBuilder = new StringBuilder(getString(R.string.main_news))
                .append(1);
        String url = stringBuilder.toString();

        showProgressDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray newsJsonArray = jsonObject.optJSONArray("data");
                slideNewsArrayList = new ArrayList<>();
                for (int index = 0; index < newsJsonArray.length(); index++) {
                    try {
                        JSONObject item = (JSONObject) newsJsonArray.get(index);
                        slideNewsArrayList.add(new News(item));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                hideProgressDialog();
                loadSlider();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (!isFinishing()) {
                    handleVolleyError(volleyError);
                    hideProgressDialog();
                }
            }
        });

        T24Application.getInstance().addToRequestQueue(jsonObjectRequest, Constants.API_TAG_GET_SLIDER_NEWS);
    }

    /**
     * Setting slider news.
     */
    private void loadSlider() {
        if (mSlider != null)
            mSlider.removeAllSliders();

        for (int index = 0; index < slideNewsArrayList.size(); index++) {
            News item = slideNewsArrayList.get(index);
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(item.getTitle())
                    .image(item.getImageUrl())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putInt(Constants.IK_NEWS_ID, item.getId());
            textSliderView.getBundle().putInt(Constants.IK_NEWS_POSITION, index);
            mSlider.addSlider(textSliderView);
        }

        mSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
        mSlider.setDuration(Constants.SLIDER_DURATION);
        mSlider.addOnPageChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        spinner = (Spinner) menu.findItem(R.id.menu_spinner).getActionView();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
                getSelectedCategoryNews((int) id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            refreshNews();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Get selected category news and set adapter.
     */
    private void getSelectedCategoryNews(int id) {
        StringBuilder stringBuilder = new StringBuilder(getString(R.string.category_news))
                .append(id);
        String url = stringBuilder.toString();

        mLoading = true;
        showProgressDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (newsArrayList != null)
                    newsArrayList.clear();
                adapter = null;
                JSONArray newsJsonArray = jsonObject.optJSONArray("data");
                for (int index = 0; index < newsJsonArray.length(); index++) {
                    try {
                        JSONObject item = (JSONObject) newsJsonArray.get(index);
                        newsArrayList.add(new News(item));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter = new NewsAdapter(MainAct.this, newsArrayList, MainAct.this);
                    rvNews.setAdapter(adapter);
                }
                mLoading = false;
                newsPagingIndex++;
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (!isFinishing()) {
                    handleVolleyError(volleyError);
                    hideProgressDialog();
                }
                mLoading = false;
            }
        });

        T24Application.getInstance().addToRequestQueue(jsonObjectRequest, Constants.API_TAG_GET_NEWS);
    }

    public void getNews() {
        StringBuilder stringBuilder = new StringBuilder(getString(R.string.main_news))
                .append(newsPagingIndex);
        String url = stringBuilder.toString();

        mLoading = true;
        showProgressDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONArray newsJsonArray = jsonObject.optJSONArray("data");
                if (newsJsonArray != null && newsJsonArray.length() > 0) {
                    for (int index = 0; index < newsJsonArray.length(); index++) {
                        try {
                            JSONObject item = (JSONObject) newsJsonArray.get(index);
                            newsArrayList.add(new News(item));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter = new NewsAdapter(MainAct.this, newsArrayList, MainAct.this);
                            rvNews.setAdapter(adapter);
                        }
                    }
                    hasMoreNews = true;
                    mLoading = false;
                    newsPagingIndex++;
                }else{
                    hasMoreNews = false;
                }
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (!isFinishing()) {
                    handleVolleyError(volleyError);
                    hideProgressDialog();
                }
                mLoading = false;
            }
        });

        T24Application.getInstance().addToRequestQueue(jsonObjectRequest, Constants.API_TAG_GET_NEWS);
    }

    /**
     * start auto refresh
     */
    public void startRepeatingTask() {
        mHandlerTask.run();
    }

    /**
     * stop refreshing
     */
    public void stopRepeatingTask() {
        mHandler.removeCallbacks(mHandlerTask);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onNewsClick(News news, int position) {
        Intent intent = new Intent(MainAct.this, NewsDetailAct.class);
        intent.putExtra(Constants.IK_NEWS_POSITION, position);
        intent.putParcelableArrayListExtra(Constants.IK_NEWS_ARRAY_LIST, newsArrayList);
        startActivity(intent);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int position = slider.getBundle().getInt(Constants.IK_NEWS_POSITION, -1);
        Intent intent = new Intent(MainAct.this, NewsDetailAct.class);
        intent.putExtra(Constants.IK_NEWS_POSITION, position);
        intent.putParcelableArrayListExtra(Constants.IK_NEWS_ARRAY_LIST, slideNewsArrayList);
        startActivity(intent);
    }
}
