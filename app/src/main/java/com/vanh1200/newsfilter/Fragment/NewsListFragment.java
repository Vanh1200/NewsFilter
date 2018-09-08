package com.vanh1200.newsfilter.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vanh1200.newsfilter.Activity.MainActivity;
import com.vanh1200.newsfilter.Adapter.NewsListAdapter;
import com.vanh1200.newsfilter.Adapter.ViewPagerAdapter;
import com.vanh1200.newsfilter.Model.News;
import com.vanh1200.newsfilter.Network.XMLAsync;
import com.vanh1200.newsfilter.R;

import java.util.ArrayList;

public class NewsListFragment extends Fragment implements NewsListAdapter.onClickSpecificIcon, XMLAsync.onResultListenerCallBack, MainActivity.onKeywordListener {
    private static final String TAG = "NewsListFragment";
    private ArrayList<News> arrNews = new ArrayList<>();
    private RecyclerView rcvNewsList;
    private NewsListAdapter newsListAdapter;
    private XMLAsync xmlAsync;

    public NewsListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        MainActivity mainActivity = (MainActivity) getActivity(); // to get Query from searchView at MainActivity
        mainActivity.setKeywordListener(this);
        initList(view);
        return view;
    }


    private void initList(View view) {
        rcvNewsList = view.findViewById(R.id.rcv_news_list);
        newsListAdapter = new NewsListAdapter(getActivity(), arrNews, this);
        rcvNewsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvNewsList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rcvNewsList.setAdapter(newsListAdapter);
    }

    private void excuteGetDataFromKeyWord(String keyword) {
        Log.d(TAG, "getDataFromKeyWord: " + keyword);
        if(keyword!=null){
            xmlAsync = new XMLAsync(this);
            String url = MainActivity.API.replace("keyword", keyword);
            xmlAsync.execute(url);
            Log.d(TAG, "getDataFromKeyWord: URL" + url);
        }
    }

    @Override
    public void onClickFavoriteIcon(int position, boolean status) {
        MainActivity mainActivity = (MainActivity) getActivity();
        FavoriteFragment fragment = (FavoriteFragment) mainActivity.getArrFragment().get(1);
//        FavoriteFragment fragment = (FavoriteFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 1);
        if(fragment != null){
            News news = arrNews.get(position);
            if(!status){
                fragment.addItem(news, 0 );
                Toast.makeText(getActivity(), "Added to favorite", Toast.LENGTH_SHORT).show();
            }
            else{
                fragment.deleteItem(news);
                Toast.makeText(getActivity(), "Removed from favorite", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Log.d(TAG, "onClickFavoriteIcon: " + "can not find Favorite fragment");
    }

    @Override
    public void onClickDownloadIcon(int position, boolean status) {
        Toast.makeText(getActivity(), "You clicked download", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickShareIcon(int position) {
        Toast.makeText(getActivity(), "You clicked share", Toast.LENGTH_SHORT).show();
    }

    public void editItem(News news){
        int position = arrNews.indexOf(news);
        arrNews.set(position, news);
        newsListAdapter.notifyItemChanged(position);
    }


    @Override
    public void onParsedResultCallback(ArrayList<News> arrNews) {
        Log.d(TAG, "onParsedResultCallback: " +arrNews.size());
        this.arrNews.clear();
        this.arrNews.addAll(arrNews);
        newsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSubmittedKeyword(String keyword) {
        excuteGetDataFromKeyWord(keyword);
    }
}
