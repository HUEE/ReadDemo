package com.hwj.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.hwj.Base.BaseActivity;
import com.hwj.reader.R;

import butterknife.BindView;
import butterknife.OnClick;

@SuppressLint("NewApi")
public class SuggestActivity extends BaseActivity {
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ed1)
    EditText ed1;
    @BindView(R.id.main_click)
    RelativeLayout commit;
    String str = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayout() {
        return R.layout.suggest;
    }

    @Override
    protected void init() {
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(getString(R.string.suggst));
    }

    @OnClick(R.id.main_click)
    void myClick() {
        str = ed1.getText().toString();
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:h450783795@163.com"));
        data.putExtra(Intent.EXTRA_SUBJECT, "DABING_SUGGEST");
        data.putExtra(Intent.EXTRA_TEXT, str);
        startActivity(data);
    }

}

