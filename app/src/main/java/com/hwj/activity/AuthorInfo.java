package com.hwj.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hwj.Base.BaseActivity;
import com.hwj.reader.R;

import butterknife.BindView;

@SuppressLint("NewApi")
public class AuthorInfo extends BaseActivity {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayout() {
        return R.layout.acthor;
    }

    @Override
    protected void init() {
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setTitle(getString(R.string.persion));
    }

}

