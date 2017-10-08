package com.example.guohouxiao.musicalbum.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseActivity;

/**
 * Created by guohouxiao on 2017/9/10.
 * 播放
 */

public class PlayActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView tv_album_name;

    private static final String ALBUM_ID = "album_id";
    private static final String ALBUM_NAME = "album_name";
    private static final String PLAYMODE = "playmode";
    private String album_id;
    private String album_name;
    private String playmode;

    private WebView mWebView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        album_id = getIntent().getStringExtra(ALBUM_ID);
        album_name = getIntent().getStringExtra(ALBUM_NAME);
        playmode = getIntent().getStringExtra(PLAYMODE);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_album_name = (TextView) findViewById(R.id.tv_album_name);
        tv_album_name.setText(album_name);

        mWebView = (WebView) findViewById(R.id.mWebView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl("http://musicalbums.leanapp.cn/?albumId=" + album_id + "&modelId=0" + playmode);
    }

    private class MyWebViewClient extends WebViewClient {

        public MyWebViewClient() {

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {// 网页页面开始加载的时候


            if (progressDialog == null) {
                progressDialog = new ProgressDialog(PlayActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                mWebView.setEnabled(false);// 当加载网页的时候将网页进行隐藏
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {// 网页加载结束的时候
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
                mWebView.setEnabled(true);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { // 网页加载时的连接的网址
            view.loadUrl(url);
            return true;
        }
    }
}
