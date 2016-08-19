package com.hwj.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hwj.Base.BaseActivity;
import com.hwj.booksRead.SetupInfo;
import com.hwj.reader.R;
import com.hwj.utils.DbHelper;
import com.hwj.utils.SystemBarTintManager;

import butterknife.BindView;
import butterknife.OnClick;

@SuppressLint("NewApi")
public class SetingActivity extends BaseActivity {
    private Toolbar mToolbar;
    @BindView(R.id.set_font)
    TextView set_font;
    @BindView(R.id.set_light)
    TextView set_ligth;
    @BindView(R.id.set_backlight)
    TextView set_backlight;
    @BindView(R.id.set_think)
    TextView set_think;
    @BindView(R.id.set_update)
    TextView set_updata;
    @BindView(R.id.set_share)
    TextView set_share;
    @BindView(R.id.set_about)
    TextView set_about;
    @BindView(R.id.set_demo)
    TextView set_demo;
    RelativeLayout set_content;
    View pop_font;
    View pop_light;
    int tmpInt;
    PopupWindow popFont;
    PopupWindow popLight;
    private SeekBar seekbar;
    Context context;
    @BindView(R.id.fontcolor1)
    Button fontcolor1;
    @BindView(R.id.fontcolor2)
    Button fontcolor2;
    @BindView(R.id.fontcolor3)
    Button fontcolor3;
    @BindView(R.id.fontcolor4)
    Button fontcolor4;
    @BindView(R.id.fontsize_smaller)
    Button fontsize_smaller;
    @BindView(R.id.fontsize_bigger)
    Button fontsize_bigger;
    private RelativeLayout set_ok;
    private RelativeLayout set_light_ok;

    DbHelper db;
    SetupInfo setup = null;
    int fontsize = 4;
    float scale;
    final String[] font = new String[]{"38", "40", "41", "43", "45", "47",
            "49", "51", "53", "55", "57", "60", "64", "65"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        db = new DbHelper(this);
        setup = db.getSetupInfo();
        fontsize = setup.fontsize;
        db.close();
        scale = this.getResources().getDisplayMetrics().density;
        init();

        mToolbar = (Toolbar) findViewById(R.id.common_toolbar);
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //mToolbar.setNavigationIcon(R.drawable.slide_momo);
        setTitle(getString(R.string.setting));
        // mToolbar.setTitle(resId)
        setSystemBarTintDrawable(getResources().getDrawable(R.drawable.sr_primary));


    }

    @Override
    protected int setLayout() {
        return R.layout.setting;
    }

    @Override
    protected void init() {
        LayoutInflater inflater = LayoutInflater.from(this);
        set_content = (RelativeLayout) inflater.inflate(R.layout.setting, null);
        pop_font = inflater.inflate(R.layout.pop_set_font, null);
        pop_light = inflater.inflate(R.layout.pop_set_light, null);
        set_demo = (TextView) pop_font.findViewById(R.id.set_demo);
        set_demo.setTextSize((Integer.parseInt(font[fontsize])) / scale + 0.5f);
        popFont = new PopupWindow(pop_font, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
        popFont.setAnimationStyle(R.style.PopupAnimation);
        popLight = new PopupWindow(pop_light, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
        popLight.setAnimationStyle(R.style.PopupAnimation);
        popFont.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.trans_bg));
        popLight.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.trans_bg));
    }


    public void setLight() {

        //popLight.showAtLocation(set_content, Gravity.BOTTOM, 0, 0);

        seekbar = (SeekBar) pop_light.findViewById(R.id.seekBar);
        set_light_ok = (RelativeLayout) pop_light.findViewById(R.id.set_light_ok);
        seekbar.setMax(255);
        if (isAutoBrightness(getContentResolver())) {
            stopAutoBrightness(SetingActivity.this);
        }
        int normal = android.provider.Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 255);
        seekbar.setProgress(normal * 100 / 255);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
                saveScreenBrightness(tmpInt);

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                // TODO Auto-generated method stubt
                tmpInt = seekbar.getProgress();
                Settings.System.putInt(getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, tmpInt);
                tmpInt = Settings.System.getInt(getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, -1);
                WindowManager.LayoutParams wl = getWindow().getAttributes();

                float tmpFloat = (float) ((float) tmpInt / 255.0);
                if (tmpFloat > 0 && tmpFloat <= 1) {
                    wl.screenBrightness = tmpFloat;
                }
                getWindow().setAttributes(wl);
            }
        });
    }

    public static boolean isAutoBrightness(ContentResolver aContentResolver) {
        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(aContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }

    private void saveScreenBrightness(int paramInt) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    public static void setBrightness(Activity activity, int brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        Log.d("lxy", "set  lp.screenBrightness == " + lp.screenBrightness);
        activity.getWindow().setAttributes(lp);
    }

    public static void stopAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }


    public static void startAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }


    @OnClick({R.id.set_demo, R.id.set_share, R.id.set_think, R.id.set_backlight,
            R.id.set_light, R.id.set_font, R.id.set_about, R.id.fontcolor1, R.id.fontcolor2,
            R.id.fontcolor3, R.id.fontcolor4, R.id.fontsize_bigger, R.id.fontsize_smaller,
            R.id.set_ok, R.id.set_light_ok})
    void myOnClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.set_font:
                popFont.showAtLocation(set_content, Gravity.CENTER, 0, 0);
                break;
            case R.id.set_light:
                popLight.showAtLocation(set_content, Gravity.CENTER, 0, 0);
                setLight();
                break;
            case R.id.set_backlight:

                break;
            case R.id.set_think:
                Intent intent_think = new Intent();
                intent_think.setClass(SetingActivity.this, SuggestActivity.class);
                startActivity(intent_think);

                break;
            case R.id.set_update:
                Toast.makeText(SetingActivity.this, "已经是最新版本！！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_share:
                Intent intent_share = new Intent(Intent.ACTION_SEND);
                intent_share.setType("text/*");
                startActivity(intent_share);
                break;
            case R.id.set_about:
                Intent intent_about = new Intent();
                intent_about.setClass(SetingActivity.this, AboutUsActivity.class);
                startActivity(intent_about);
                break;

            case R.id.fontcolor1:

                set_demo.setTextColor(context.getResources().getColorStateList(R.color.tx_color1));
                Toast.makeText(SetingActivity.this, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fontcolor2:
                set_demo.setTextColor(context.getResources().getColorStateList(R.color.tx_color2));
                Toast.makeText(SetingActivity.this, "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fontcolor3:
                set_demo.setTextColor(context.getResources().getColorStateList(R.color.tx_color3));
                Toast.makeText(SetingActivity.this, "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fontcolor4:
                set_demo.setTextColor(context.getResources().getColorStateList(R.color.tx_color4));
                Toast.makeText(SetingActivity.this, "4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fontsize_bigger:

                if (fontsize >= 12)
                    Toast.makeText(SetingActivity.this, "已经为最大字体", Toast.LENGTH_SHORT).show();
                else {
                    fontsize++;
                    set_demo.setTextSize((Integer.parseInt(font[fontsize])) / scale + 0.5f);
                }
                break;
            case R.id.fontsize_smaller:

                if (fontsize <= 0)
                    Toast.makeText(SetingActivity.this, "已经为最小字体", Toast.LENGTH_SHORT).show();
                else {
                    fontsize--;
                    set_demo.setTextSize((Integer.parseInt(font[fontsize])) / scale + 0.5f);
                }

                break;
            case R.id.set_ok:
                try {
                    //db.update(book.id, String.valueOf(book.pagernum), String.valueOf(msg.arg2));
                    db.updateSetup(1, String.valueOf(fontsize), "0", "0");
                    db.updateSetup(2, String.valueOf(fontsize), "0", "0");
                    db.close();
                    popFont.dismiss();
                    //mCursor = db.select();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.set_light_ok:
                popLight.dismiss();
            default:
                break;
        }
    }


    protected void setSystemBarTintDrawable(Drawable tintDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            if (tintDrawable != null) {
                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setTintDrawable(tintDrawable);
            } else {
                mTintManager.setStatusBarTintEnabled(false);
                mTintManager.setTintDrawable(null);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (popFont.isShowing() || popLight.isShowing()) {
                    popFont.dismiss();
                    popLight.dismiss();
                } else {
                    this.finish();
                }
        }
        return true;
    }
}

