package com.msidimi.t24.view.newsdetail;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.msidimi.t24.R;
import com.msidimi.t24.T24Application;
import com.msidimi.t24.common.Constants;
import com.msidimi.t24.model.News;
import com.msidimi.t24.view.BaseFrag;

import org.json.JSONObject;

/**
 * Created by mucahit on 18/09/16.
 */
public class NewsDetailFrag extends BaseFrag {

    private int newsID;
    private TextView tvNewsDetail;
    private TextView tvNewsTitle;
    private NetworkImageView ivNews;
    private ProgressDialog mProgressDialog;

    public static final NewsDetailFrag newInstance(int newsID) {
        NewsDetailFrag newsDetailFrag = new NewsDetailFrag();
        Bundle bundle = new Bundle(1);
        bundle.putInt("news_id", newsID);
        newsDetailFrag.setArguments(bundle);
        return newsDetailFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsID = getArguments().getInt("news_id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_news_detail, null);
        tvNewsDetail = (TextView) view.findViewById(R.id.tv_news);
        tvNewsTitle = (TextView) view.findViewById(R.id.tv_title);
        ivNews = (NetworkImageView) view.findViewById(R.id.iv_news);
        loadNewsDetail();
        return view;
    }

    private void loadNewsDetail() {
        StringBuilder stringBuilder = new StringBuilder(getString(R.string.news_detail))
                .append(newsID);
        String url = stringBuilder.toString();

        showProgressDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                News news = new News(jsonObject.optJSONObject("data"));
                if (news != null) {
                    StringBuilder newsTitleStringBuilder = new StringBuilder("<b><h3>").append(news.getTitle()).append("</h3></b>");
                    if (Build.VERSION.SDK_INT >= 24) {
                        tvNewsDetail.setText(Html.fromHtml(news.getText(), Html.FROM_HTML_MODE_LEGACY));
                        tvNewsTitle.setText(Html.fromHtml(newsTitleStringBuilder.toString(), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        tvNewsDetail.setText(Html.fromHtml(news.getText()));
                        tvNewsTitle.setText(Html.fromHtml(newsTitleStringBuilder.toString()));
                    }
                    ivNews.setImageUrl(news.getImageUrl(), T24Application.getInstance().getImageLoader());
                }
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (!getActivity().isFinishing()) {
                    handleVolleyError(volleyError);
                    hideProgressDialog();
                }
            }
        });

        T24Application.getInstance().addToRequestQueue(jsonObjectRequest, Constants.API_TAG_GET_NEWS_DETAIL);
    }

    private void hideProgressDialog() {
        if (!getActivity().isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getString(R.string.progress_please_wait));
        }
        mProgressDialog.show();
    }
}
