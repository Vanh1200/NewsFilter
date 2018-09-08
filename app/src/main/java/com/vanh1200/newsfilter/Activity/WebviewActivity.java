package com.vanh1200.newsfilter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vanh1200.newsfilter.Adapter.NewsListAdapter;
import com.vanh1200.newsfilter.Fragment.NewsListFragment;
import com.vanh1200.newsfilter.R;

public class WebviewActivity extends AppCompatActivity{
    private WebView webView;
    private String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView =findViewById(R.id.wv_news_content);

        Intent intent = getIntent();
        url = intent.getStringExtra(NewsListAdapter.KEY_WEB_URL);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        WebSettings webSettings  = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
    }
}
