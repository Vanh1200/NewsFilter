package com.vanh1200.newsfilter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vanh1200.newsfilter.Activity.WebviewActivity;
import com.vanh1200.newsfilter.Fragment.SavedFragment;
import com.vanh1200.newsfilter.Model.News;
import com.vanh1200.newsfilter.R;

import java.util.ArrayList;

import static com.vanh1200.newsfilter.Adapter.NewsListAdapter.KEY_WEB_URL;

public class SavedListAdapter extends RecyclerView.Adapter<SavedListAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<News> mArrNews;
    private RequestOptions option;
    private onClickSpecificIcon onClickSpecificIcon;

    public SavedListAdapter(Context mContext, ArrayList<News> mArrNews, onClickSpecificIcon clickSpecificIcon) {
        this.mContext = mContext;
        this.mArrNews = mArrNews;
        this.onClickSpecificIcon = clickSpecificIcon;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bindData(mArrNews.get(position));
        holder.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickSpecificIcon != null){
                    mArrNews.get(position).setDownloaded(!mArrNews.get(position).isDownloaded());
                    onClickSpecificIcon.onClickDownloadIcon(position);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebviewActivity.class);
                intent.putExtra(KEY_WEB_URL, mArrNews.get(position).getLink());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mArrNews.size() == 0)
            onClickSpecificIcon.onChangeScreen(SavedFragment.NO_DOWNLOADS);
        else
            onClickSpecificIcon.onChangeScreen(SavedFragment.LIST_DOWNLOADS);

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
            tvPublisher.setText(news.getPublisher());
            if(news.isLiked())
                ivFavorite.setImageResource(R.drawable.liked);
            else
                ivFavorite.setImageResource(R.drawable.like);
            if(news.isDownloaded())
                ivDownload.setImageResource(R.drawable.downloaded);
            else
                ivDownload.setImageResource(R.drawable.download_2);

            Glide.with(mContext)
                    .load(news.getImage())
                    .apply(option)
                    .into(ivThumbnail);
        }
    }
    public interface onClickSpecificIcon{
        void onChangeScreen(int which);
        void onClickFavoriteIcon(int position);
        void onClickDownloadIcon(int position);
        void onClickShareIcon(int position);
    }
}