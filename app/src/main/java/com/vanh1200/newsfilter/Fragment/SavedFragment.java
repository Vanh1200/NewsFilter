package com.vanh1200.newsfilter.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vanh1200.newsfilter.Adapter.FavoriteListAdapter;
import com.vanh1200.newsfilter.Adapter.SavedListAdapter;
import com.vanh1200.newsfilter.Model.News;
import com.vanh1200.newsfilter.R;
import com.vanh1200.newsfilter.SLQite.FavoriteDAO;

import java.util.ArrayList;

public class SavedFragment extends Fragment implements SavedListAdapter.onClickSpecificIcon{
    private static final String TAG = "FavoriteFragment";
    public static final int NO_DOWNLOADS = 0;
    public static final int LIST_DOWNLOADS = 1;
    public static SavedFragment instance;
//    public FavoriteDAO favoriteDAO;

    private RecyclerView rcvSaved;
    private RelativeLayout defaultSavedScreen;
    private ArrayList<News> arrSavedList = new ArrayList<>();
    private SavedListAdapter savedListAdapter;

    public static SavedFragment getInstance() {
        if(instance == null)
            instance = new SavedFragment();
        return instance;
    }

    public SavedFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);
        initRecyclerView(view);
        return view;
    }

//    private void initFavoriteNewsFromDatabase() {
//        favoriteDAO = new FavoriteDAO(getActivity());
//        arrFavoriteList = new ArrayList<>();
//        arrFavoriteList = favoriteDAO.getAllNews();
//    }

    private void initRecyclerView(View view) {
        rcvSaved = view.findViewById(R.id.rcv_news_saved_list);
        defaultSavedScreen = view.findViewById(R.id.default_saved_screen);
        savedListAdapter = new SavedListAdapter(getActivity(), arrSavedList, this);
        rcvSaved.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvSaved.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rcvSaved.setAdapter(savedListAdapter);
    }

    public void addItem(News news, int position) {
        arrSavedList.add(news);
//        favoriteDAO.addNews(news);
        savedListAdapter.notifyItemInserted(position);
        savedListAdapter.notifyItemRangeChanged(position, arrSavedList.size());
    }

    public void deleteItem(News news) {
        int position = arrSavedList.indexOf(news);
        arrSavedList.remove(position);
//        favoriteDAO.deleteNews(news);
        savedListAdapter.notifyItemRemoved(position);
        savedListAdapter.notifyItemRangeChanged(position, arrSavedList.size());
    }

    @Override
    public void onChangeScreen(int which) {
        if (which == NO_DOWNLOADS) {
            defaultSavedScreen.setVisibility(View.VISIBLE);
            rcvSaved.setVisibility(View.INVISIBLE);
        } else if (which == LIST_DOWNLOADS) {
            defaultSavedScreen.setVisibility(View.INVISIBLE);
            rcvSaved.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickFavoriteIcon(int position) {

    }

    @Override
    public void onClickDownloadIcon(int position) {
        NewsListFragment listFragment = NewsListFragment.getInstance();
        News news = arrSavedList.get(position);
        listFragment.editItem(news);

//        FavoriteFragment favoriteFragment = FavoriteFragment.getInstance();
//        favoriteFragment.editItem(News);
    }

    @Override
    public void onClickShareIcon(int position) {

    }
}
