package com.hwj.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hwj.Base.BaseActivity;
import com.hwj.adapter.MyViewPagerAdapter;
import com.hwj.reader.R;
import com.hwj.utils.SuperAwesomeCardFragment;
import com.hwj.widget.PagerSlidingTabStrip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity {
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip mPagerSlidingTabStrip;
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.slide_persion)
    Button bt_persion;
    @BindView(R.id.slide_happy)
    Button bt_happy;
    @BindView(R.id.slide_momo)
    Button bt_momo;
    @BindView(R.id.slide_more)
    Button bt_more;
    @BindView(R.id.slide_left)
    ImageButton bt_left;
    @BindView(R.id.slide_right)
    ImageButton bt_right;
    private ActionBarDrawerToggle mDrawerToggle;
    private ShareActionProvider mShareActionProvider;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private MyViewPagerAdapter myAdapter;
    private String bookPath = "/sdcard/reader/";
    private Context context;
    int curPos = 0;
    final String[] page = new String[]{"a0.txt", "a1.txt", "a2.txt", "a3.txt", "a4.txt", "a5.txt",
            "a6.txt", "a7.txt", "a8.txt", "a9.txt", "a10.txt", "a11.txt"};
    final String[] page2 = new String[]{"b0.txt", "b1.txt", "b2.txt", "b3.txt", "b4.txt", "b5.txt",
            "b6.txt", "b7.txt", "b8.txt", "b9.txt", "b10.txt", "b11.txt"};
    final int[] ram = new int[]{0x7f050000, 0x7f050001, 0x7f050004, 0x7f050005, 0x7f050006,
            0x7f050007, 0x7f050008, 0x7f050009, 0x7f05000a, 0x7f05000b, 0x7f050002, 0x7f050003};
    final int[] ram2 = new int[]{0x7f05000c, 0x7f05000d, 0x7f05000e, 0x7f050011, 0x7f050012,
            0x7f050013, 0x7f050014, 0x7f050015, 0x7f050016, 0x7f050017, 0x7f050018, 0x7f05000F};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        for (int i = 0; i <= 11; i++) {
            copyFile(bookPath + page[i], ram[i]);
            copyFile(bookPath + page2[i], ram2[i]);
        }
        initViews();
    }

    @OnClick({R.id.slide_left, R.id.slide_right, R.id.slide_more, R.id.slide_momo,
            R.id.slide_happy, R.id.slide_persion, R.id.slide_imag1})
    void myClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.slide_persion:
                //	Toast.makeText(MainActivity.this, "--->1", 1);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent5 = new Intent();
                intent5.setClass(MainActivity.this, AuthorInfo.class);
                startActivity(intent5);
                Toast.makeText(MainActivity.this, "--->1", 1);
                break;
            case R.id.slide_happy:
                curPos = 0;
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                mToolbar.setTitle("他们最幸福");

                mViewPager.setCurrentItem(0);
                break;
            case R.id.slide_momo:
                curPos = 1;
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                mToolbar.setTitle("乖，摸摸头");
                mViewPager.setCurrentItem(1);
                break;
            case R.id.slide_more:
                curPos = 2;
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                mToolbar.setTitle("更多内容");
                mViewPager.setCurrentItem(2);
                break;
            case R.id.slide_left:

                Intent intent1 = new Intent();
                intent1.setClass(context, SetingActivity.class);
                startActivity(intent1);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.slide_right:

                Intent intent = new Intent();
                intent.setClass(context, AboutUsActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            default:
                break;
        }
    }


    private void initViews() {

        // toolbar.setLogo(R.drawable.ic_launcher);
        //mToolbar.setTitle("他们最幸福");// 标题的文字需在setSupportActionBar之前，不然会无效
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

		/* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Intent intent1 = new Intent();
                        intent1.setClass(context, SetingActivity.class);
                        startActivity(intent1);
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.action_share:

                        Toast.makeText(MainActivity.this, "action_share", 0).show();
                        break;
                    case R.id.action_back:
                        Intent intent_think = new Intent();
                        intent_think.setClass(MainActivity.this, SuggestActivity.class);
                        startActivity(intent_think);


                        break;
                    case R.id.action_about:
                        Intent intent = new Intent();
                        intent.setClass(context, AboutUsActivity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "action_share", 0).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* findView */
        fragments = new ArrayList<Fragment>();// ��ʼ������
        fragments.add(new HomeFragment());
        fragments.add(new ClassesFragment());
        fragments.add(new ShopCarFragment());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        myAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(myAdapter);

        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                //colorChange(arg0);
                setTitle(myAdapter.getPageTitle(arg0));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(getString(R.string.app_name));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (null != mViewPager) {
                    setTitle(myAdapter.getPageTitle(curPos));

                }
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.closeDrawer(Gravity.LEFT);

        //mViewPager.setOn

        //initTabsValue();
    }

    protected boolean copyFile(String dst, int ram) {
        try {

            File outFile = new File(dst);
            if (!outFile.exists()) {
                File destDir = new File("/sdcard/reader");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                InputStream inStream = getResources().openRawResource(ram);
                outFile.createNewFile();
                FileOutputStream fs = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024 * 1024];// 1MB
                int byteread = 0;
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private void initTabsValue() {
        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.BLUE);
        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(Color.parseColor("#4876FF"));
        // tab底线高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1, getResources().getDisplayMetrics()));
        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics()));
        // 选中的文字颜色
        mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(Color.BLACK);
    }

    /**
     * 界面颜色的更改
     */
    @SuppressLint("NewApi")
    private void colorChange(int position) {
        // 用来提取颜色的Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                SuperAwesomeCardFragment.getBackgroundBitmapPosition(position));
        // Palette的部分
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            /**
             * 提取完之后的回调方法
             */
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                /* 界面颜色UI统一性处理,看起来更Material一些 */
                mPagerSlidingTabStrip.setBackgroundColor(vibrant.getRgb());
                mPagerSlidingTabStrip.setTextColor(vibrant.getTitleTextColor());
                // 其中状态栏、游标、底部导航栏的颜色需要加深一下，也可以不加，具体情况在代码之后说明
                mPagerSlidingTabStrip.setIndicatorColor(colorBurn(vibrant.getRgb()));

                mToolbar.setBackgroundColor(vibrant.getRgb());
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    // 很明显，这两货是新API才有的。
                    //window.setStatusBarColor(colorBurn(vibrant.getRgb()));
                    //window.setNavigationBarColor(colorBurn(vibrant.getRgb()));
                }
            }
        });
    }

    /**
     * 颜色加深处理
     *
     * @param RGBValues RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *                  Android中我们一般使用它的16进制，
     *                  例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *                  red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *                  所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        /* ShareActionProvider配置 */
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu
                .findItem(R.id.action_share));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        mShareActionProvider.setShareIntent(intent);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // switch (item.getItemId()) {
        // case R.id.action_settings:
        // Toast.makeText(MainActivity.this, "action_settings", 0).show();
        // break;
        // case R.id.action_share:
        // Toast.makeText(MainActivity.this, "action_share", 0).show();
        // break;
        // default:
        // break;
        // }
        return super.onOptionsItemSelected(item);
    }

	/* ***************FragmentPagerAdapter***************** */

}
