package com.vanh1200.newsfilter.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vanh1200.newsfilter.Activity.MainActivity;
import com.vanh1200.newsfilter.Adapter.NewsListAdapter;
import com.vanh1200.newsfilter.Adapter.ViewPagerAdapter;
import com.vanh1200.newsfilter.Model.News;
import com.vanh1200.newsfilter.Network.XMLAsync;
import com.vanh1200.newsfilter.R;
import com.vanh1200.newsfilter.SLQite.FavoriteDAO;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NewsListFragment extends Fragment implements NewsListAdapter.onClickSpecificIcon, XMLAsync.onResultListenerCallBack, MainActivity.onKeywordListener {
    private static final String TAG = "NewsListFragment";
    private ArrayList<News> arrNews = new ArrayList<>();
    private RecyclerView rcvNewsList;
    private RelativeLayout notSearchScreen;
    private RelativeLayout noResultsScreen;
    private NewsListAdapter newsListAdapter;
    private XMLAsync xmlAsync;
    private FavoriteDAO favoriteDAO; // to check favorite item on search results
    public static NewsListFragment instance;

    public NewsListFragment() {
    }

    public static NewsListFragment getInstance() {
        if (instance == null)
            instance = new NewsListFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ((MainActivity) getActivity()).setKeywordListener(this);
        initList(view);
        return view;
    }


    private void initList(View view) {
        rcvNewsList = view.findViewById(R.id.rcv_news_list);
        notSearchScreen = view.findViewById(R.id.default_filter_screen);
        noResultsScreen = view.findViewById(R.id.no_result_screen);
        newsListAdapter = new NewsListAdapter(getActivity(), arrNews, this);
        rcvNewsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvNewsList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rcvNewsList.setAdapter(newsListAdapter);
    }

    private void excuteGetDataFromKeyWord(String keyword) {
        Log.d(TAG, "getDataFromKeyWord: " + keyword);
        if (keyword != null) {
//            try {
//                keyword = URLEncoder.encode(keyword, "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            keyword = Uri.encode(keyword);
            xmlAsync = new XMLAsync(this, getActivity());
            String url = MainActivity.API.replace("keyword", keyword);
            xmlAsync.execute(url);
            Log.d(TAG, "getDataFromKeyWord: URL: " + url);
        }
    }

    @Override
    public void onClickFavoriteIcon(int position, boolean status) {
//        3 way two communicate between fragments
//        FavoriteFragment fragment = (FavoriteFragment) ((MainActivity) getActivity()).getArrFragment().get(1);
//        FavoriteFragment fragment = (FavoriteFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 1);
        FavoriteFragment fragment = FavoriteFragment.getInstance();
        News news = arrNews.get(position);
        if (!status) { // if not like
            fragment.addItem(news, 0);
            Toast.makeText(getActivity(), "Added to favorite", Toast.LENGTH_SHORT).show();
        } else { // if liked
            fragment.deleteItem(news);
            Toast.makeText(getActivity(), "Removed from favorite", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClickDownloadIcon(int position, boolean status) {
        Toast.makeText(getActivity(), "You clicked download", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickShareIcon(int position) {
        Toast.makeText(getActivity(), "You clicked share", Toast.LENGTH_SHORT).show();
    }

    public void editItem(News news) {
        int position = arrNews.indexOf(news);
        if (position != -1) { // if item was found
            arrNews.set(position, news);
            newsListAdapter.notifyItemChanged(position);
        }
    }


    @Override
    public void onParsedResultCallback(ArrayList<News> arrNews) {
        Log.d(TAG, "onParsedResultCallback: " + arrNews.size());
        this.arrNews.clear();
        favoriteDAO = new FavoriteDAO(getActivity());
        arrNews = checkFavoriteItem(arrNews);
        this.arrNews.addAll(arrNews);
        newsListAdapter.notifyDataSetChanged();
        changeScreen(arrNews.size());
    }

    private ArrayList<News> checkFavoriteItem(ArrayList<News> arrNews) {
        for (News news: arrNews) {
            if(favoriteDAO.isContain(news)){
                news.setLiked(true);
                Log.d(TAG, "checkFavoriteItem: like");
            }
        }
        return arrNews;
    }

    private void changeScreen(int size) {
        noResultsScreen.setVisibility(View.INVISIBLE);
        rcvNewsList.setVisibility(View.INVISIBLE);
        notSearchScreen.setVisibility(View.INVISIBLE);
        if (size == 0)
            noResultsScreen.setVisibility(View.VISIBLE);
        else
            rcvNewsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSubmittedKeyword(String keyword) {
        excuteGetDataFromKeyWord(keyword);
    }
}
