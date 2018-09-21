package com.vanh1200.newsfilter.Fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vanh1200.newsfilter.Adapter.FavoriteListAdapter;
import com.vanh1200.newsfilter.Model.News;
import com.vanh1200.newsfilter.R;
import com.vanh1200.newsfilter.SLQite.FavoriteDAO;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements FavoriteListAdapter.onClickSpecificIcon {
    private static final String TAG = "FavoriteFragment";
    public static final int NO_FAVORITES = 0;
    public static final int LIST_FAVORITES = 1;
    public static FavoriteFragment instance;
    public FavoriteDAO favoriteDAO;

    private RecyclerView rcvFavorite;
    private RelativeLayout defaultFavoriteScreen;
    private ArrayList<News> arrFavoriteList;
    private FavoriteListAdapter favoriteListAdapter;

    public FavoriteFragment() {
    }

    public static FavoriteFragment getInstance() {
        if (instance == null) {
            instance = new FavoriteFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        initFavoriteNewsFromDatabase();
        initRecyclerView(view);
        return view;
    }

    private void initFavoriteNewsFromDatabase() {
        favoriteDAO = new FavoriteDAO(getActivity());
        arrFavoriteList = new ArrayList<>();
        arrFavoriteList = favoriteDAO.getAllNews();
    }

    private void initRecyclerView(View view) {
        rcvFavorite = view.findViewById(R.id.rcv_news_favorite_list);
        defaultFavoriteScreen = view.findViewById(R.id.default_favorite_screen);
        favoriteListAdapter = new FavoriteListAdapter(getActivity(), arrFavoriteList, this);
        rcvFavorite.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvFavorite.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rcvFavorite.setAdapter(favoriteListAdapter);
    }


    public void addItem(News news, int position) {
        arrFavoriteList.add(news);
        favoriteDAO.addNews(news);
        favoriteListAdapter.notifyItemInserted(position);
        favoriteListAdapter.notifyItemRangeChanged(position, arrFavoriteList.size());
    }

    public void deleteItem(News news) {
        int position = arrFavoriteList.indexOf(news);
        arrFavoriteList.remove(position);
        favoriteDAO.deleteNews(news);
        favoriteListAdapter.notifyItemRemoved(position);
        favoriteListAdapter.notifyItemRangeChanged(position, arrFavoriteList.size());
    }

    @Override
    public void onChangeScreen(int which) {
        if (which == NO_FAVORITES) {
            defaultFavoriteScreen.setVisibility(View.VISIBLE);
            rcvFavorite.setVisibility(View.INVISIBLE);
        } else if (which == LIST_FAVORITES) {
            defaultFavoriteScreen.setVisibility(View.INVISIBLE);
            rcvFavorite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickFavoriteIcon(int position) {
//        NewsListFragment listFragment = (NewsListFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 0);
//        SavedFragment savedFragment = (SavedFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 2);
        NewsListFragment listFragment = NewsListFragment.getInstance();
        SavedFragment savedFragment = SavedFragment.getInstance();

        //handle at newsListFragment
        News news = arrFavoriteList.get(position);
        listFragment.editItem(news);

        //handle at savedFragment: not handled yet

        //final, remove from list;
//        arrFavoriteList.remove(position);
//        favoriteListAdapter.notifyItemRemoved(position);
//        favoriteListAdapter.notifyItemRangeChanged(position, arrFavoriteList.size());
        deleteItem(arrFavoriteList.get(position));
    }

    @Override
    public void onClickDownloadIcon(int position) {

    }

    @Override
    public void onClickShareIcon(int position) {

    }

}
