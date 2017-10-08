package com.example.guohouxiao.musicalbum.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guohouxiao.musicalbum.R;
import com.example.guohouxiao.musicalbum.base.BaseActivity;
import com.example.guohouxiao.musicalbum.bean.Place;
import com.example.guohouxiao.musicalbum.utils.FlexUtils;
import com.google.android.flexbox.FlexboxLayout;

/**
 * Created by guohouxiao on 2017/10/3.
 * 搜索
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private static final String TAG = "SearchActivity";

    private static final String GO_PLACE = "goplace";
    private static final String GO_LATITUDE = "golatitude";
    private static final String GO_LONGITUDE = "golongitude";
    private static final String SEARCH_RESULT = "searchresult";

    private TextView tv_back;
    private ImageView iv_search;
    private EditText et_search;
    private FlexboxLayout mFlexboxLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView() {

        tv_back = (TextView) findViewById(R.id.tv_back);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        et_search = (EditText) findViewById(R.id.et_search);
        mFlexboxLayout = (FlexboxLayout) findViewById(R.id.mFlexboxLayout);
        String[] tags = {"迪士尼乐园", "额济纳胡杨林", "平遥古城", "中北大学", "杭州西湖"};
        double[] latitues = {31.141333, 41.961822, 37.204233, 38.014165, 30.220671};
        double[] longitudes = {121.661735, 101.083417, 112.183466, 112.449576, 120.108478};
        for (int i = 0; i < tags.length; i++) {
            Place model = new Place();
            model.setId(i);
            model.setName(tags[i]);
            model.setLatitude(latitues[i]);
            model.setLongitude(longitudes[i]);
            mFlexboxLayout.addView(createNewFlexItemTextView(model));
        }

        tv_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        et_search.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                if (isSoftShowing()) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }
                } else {
                    finish();
                }
                break;
            case R.id.iv_search:
                Intent intent = new Intent(this, SearchResultActivity.class);
                intent.putExtra(SEARCH_RESULT, et_search.getText().toString().trim());
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            ((InputMethodManager) et_search.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(SearchActivity.this
                                    .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra(SEARCH_RESULT, et_search.getText().toString().trim());
            startActivity(intent);
            return true;
        }
        return false;
    }

    private TextView createNewFlexItemTextView(final Place place) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(place.getName());
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(R.color.colorLogo));
        textView.setBackgroundResource(R.drawable.search_bg);
        textView.setTag(place.getId());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, GoActivity.class);
                intent.putExtra(GO_PLACE, place.getName());
                intent.putExtra(GO_LATITUDE, place.getLatitude());
                intent.putExtra(GO_LONGITUDE, place.getLongitude());
                startActivity(intent);
            }
        });
        int padding = FlexUtils.dpToPixel(this, 4);
        int paddingLeftAndRight = FlexUtils.dpToPixel(this, 8);
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight, padding, paddingLeftAndRight, padding);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = FlexUtils.dpToPixel(this, 6);
        int marginTop = FlexUtils.dpToPixel(this, 16);
        layoutParams.setMargins(margin, marginTop, margin, 0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    //判断键盘是否弹出
    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度  
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom  
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom - getSoftButtonsBarHeight() != 0;
    }

    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度  
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度  
        this.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }
}
