package com.vanh1200.newsfilter.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vanh1200.newsfilter.Model.News;
import com.vanh1200.newsfilter.R;

import java.util.ArrayList;

public class SavedListAdapter extends RecyclerView.Adapter<SavedListAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<News> mArrNews;
    private RequestOptions option;

    public SavedListAdapter(Context mContext, ArrayList<News> mArrNews) {
        this.mContext = mContext;
        this.mArrNews = mArrNews;

        option = new RequestOptions().placeholder(R.drawable.default_thumbnail)
                .error(R.drawable.default_thumbnail)
                .centerCrop();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mArrNews.get(position));
    }

    @Override
    public int getItemCount() {
        return mArrNews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivThumbnail;
        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvPubDate;
        private TextView tvPublisher;
        private ImageView ivFavorite;
        private ImageView ivDownload;
        private ImageView ivShare;
        public ViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title_news);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPubDate = itemView.findViewById(R.id.tv_pub_date);
            ivFavorite = itemView.findViewById(R.id.iv_like);
            ivDownload = itemView.findViewById(R.id.iv_download);
            ivShare = itemView.findViewById(R.id.iv_share);
            tvPublisher = itemView.findViewById(R.id.tv_publisher);
        }
        public void bindData(News news){
            tvTitle.setText(news.getTitle());
            tvDescription.setText(news.getDescription());
            tvPubDate.setText(news.getPubDate());

            Glide.with(mContext)
                    .load(news.getImage())
                    .apply(option)
                    .into(ivThumbnail);
        }
    }
}