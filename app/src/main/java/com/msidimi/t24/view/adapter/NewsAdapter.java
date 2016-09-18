package com.msidimi.t24.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.msidimi.t24.R;
import com.msidimi.t24.T24Application;
import com.msidimi.t24.listener.OnNewsClickListener;
import com.msidimi.t24.model.News;

import java.util.ArrayList;

/**
 * Created by mucahit on 18/09/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final Context mContext;
    private final ArrayList<News> newsArrayList;
    private final OnNewsClickListener onNewsClickListener;

    public NewsAdapter(Context context, ArrayList<News> newsArrayList, OnNewsClickListener onNewsClickListener) {
        this.mContext = context;
        this.newsArrayList = newsArrayList;
        this.onNewsClickListener = onNewsClickListener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, final int position) {
        final News newsItem = newsArrayList.get(position);
        holder.tvTitle.setText(newsItem.getTitle());
        holder.networkImageView.setImageUrl(newsItem.getImageUrl(), T24Application.getInstance().getImageLoader());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsClickListener.onNewsClick(newsItem, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (newsArrayList != null)
            return newsArrayList.size();
        else
            return 0;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public NetworkImageView networkImageView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            networkImageView = (NetworkImageView) itemView.findViewById(R.id.iv_news);
        }
    }
}
