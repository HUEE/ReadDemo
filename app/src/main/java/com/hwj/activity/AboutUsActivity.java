package com.hwj.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hwj.Base.BaseActivity;
import com.hwj.reader.R;

import butterknife.BindView;

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void init() {
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(getString(R.string.about));
    }

}

  