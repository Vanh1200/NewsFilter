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
import com.vanh1200.newsfilter.Fragment.FavoriteFragment;
import com.vanh1200.newsfilter.Model.News;
import com.vanh1200.newsfilter.R;

import java.util.ArrayList;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<News> mArrNews;
    private onClickSpecificIcon onClickSpecificIcon;
    private RequestOptions option;

    public FavoriteListAdapter(Context mContext, ArrayList<News> mArrNews, onClickSpecificIcon clickSpecificIcon) {
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bindData(mArrNews.get(position));
        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickSpecificIcon != null){
                    mArrNews.get(position).setLiked(!mArrNews.get(position).isLiked());
                    onClickSpecificIcon.onClickFavoriteIcon(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mArrNews.size() == 0)
            onClickSpecificIcon.onChangeScreen(FavoriteFragment.NO_FAVORITES);
        else
            onClickSpecificIcon.onChangeScreen(FavoriteFragment.LIST_FAVORITES);
        return mArrNews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivThumbnail;
        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvPubDate;
        private ImageView ivFavorite;
        private ImageView ivDownload;
        public ViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title_news);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPubDate = itemView.findViewById(R.id.tv_pub_date);
            ivDownload = itemView.findViewById(R.id.iv_download);
            ivFavorite = itemView.findViewById(R.id.iv_like);

        }
        public void bindData(News news){
            tvTitle.setText(news.getTitle());
            tvDescription.setText(news.getDescription());
            tvPubDate.setText(news.getPubDate());
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
        void onClickDownloadIcon(int position, boolean status);
        void onClickShareIcon(int position);
    }



}