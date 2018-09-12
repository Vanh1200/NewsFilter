package com.vanh1200.newsfilter.Activity;

import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.vanh1200.newsfilter.Adapter.NewsListAdapter;
import com.vanh1200.newsfilter.Adapter.ViewPagerAdapter;
import com.vanh1200.newsfilter.Fragment.FavoriteFragment;
import com.vanh1200.newsfilter.Fragment.NewsListFragment;
import com.vanh1200.newsfilter.Fragment.SavedFragment;
import com.vanh1200.newsfilter.Model.News;
import com.vanh1200.newsfilter.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    public static final String API="https://news.google.de/news/feeds?pz=1&cf=vi_vn&ned=vi_vn&hl=vi_vn&q=(keyword)";
    private static final String TAG = "MainActivity";
    private ArrayList<android.support.v4.app.Fragment> arrFragment;
    private ViewPagerAdapter adapter;
    private SearchView searchView;

    public onKeywordListener keywordListener;

    public void setKeywordListener(onKeywordListener keywordListener) {
        this.keywordListener = keywordListener;
    }

    public ArrayList<android.support.v4.app.Fragment> getArrFragment() {
        return arrFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setPagerAdapter();
    }


    private void setPagerAdapter() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        //init Fragment with Singleton
        arrFragment = new ArrayList<>();
        arrFragment.add(NewsListFragment.getInstance());
        arrFragment.add(FavoriteFragment.getInstance());
        arrFragment.add(SavedFragment.getInstance());

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(arrFragment.get(0), "News");
        adapter.addFragment(arrFragment.get(1), "Favorite");
        adapter.addFragment(arrFragment.get(2), "Saved");
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        searchView = (SearchView) menu.findItem(R.id.tb_search).getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        keywordListener.onSubmittedKeyword(query);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public interface onKeywordListener{
        void onSubmittedKeyword(String keyword);
    }
}
