package com.vanh1200.newsfilter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.request.RequestOptions;
import com.vanh1200.newsfilter.Activity.WebviewActivity;
import com.vanh1200.newsfilter.Fragment.NewsListFragment;
import com.vanh1200.newsfilter.Model.News;
import com.vanh1200.newsfilter.Network.DownloadImageAsync;
import com.vanh1200.newsfilter.R;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder>{
    private static final String TAG = "NewsListAdapter";
    private Context mContext;
    private ArrayList<News> mArrNews;
    private onClickSpecificIcon onClickSpecificIcon;
    private ViewHolder viewHolder;
    private RequestOptions option;
    public static final String KEY_WEB_URL = "key_web_url";


    public NewsListAdapter(Context mContext, ArrayList<News> mArrNews, onClickSpecificIcon onClick) {
        this.mContext = mContext;
        this.mArrNews = mArrNews;
        this.onClickSpecificIcon = onClick;

        option = new RequestOptions().placeholder(R.drawable.default_thumbnail)
                .error(R.drawable.default_thumbnail)
                .centerCrop();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_layout, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bindData(mArrNews.get(position));
        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickSpecificIcon != null){
                    if(mArrNews.get(position).isLiked()){
                        holder.ivFavorite.setImageResource(R.drawable.like);
                        mArrNews.get(position).setLiked(false);
                        onClickSpecificIcon.onClickFavoriteIcon(position, true);
                    }else{
                        holder.ivFavorite.setImageResource(R.drawable.liked);
                        mArrNews.get(position).setLiked(true);
                        onClickSpecificIcon.onClickFavoriteIcon(position, false);
                    }
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
        return mArrNews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements DownloadImageAsync.OnResultDownloaded {
        private ImageView ivThumbnail;
        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvPubDate;
        private ImageView ivFavorite;
        private ImageView ivDownload;
        private ImageView ivShare;
        private TextView tvPublisher;
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
//            DownloadImageAsync imageAsync = new DownloadImageAsync(this);
//            imageAsync.execute(news.getImage());

        }

        @Override
        public void onDownloadedImage(Bitmap bitmap) {
            ivThumbnail.setImageBitmap(bitmap);
        }
    }

    public interface onClickSpecificIcon{
        void onClickFavoriteIcon(int position, boolean status);
        void onClickDownloadIcon(int position, boolean status);
        void onClickShareIcon(int position);
    }
}
