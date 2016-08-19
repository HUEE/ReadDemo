package com.example.toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
  
public class AboutUsActivity extends ActionBarActivity {
	private ImageView im1 ;
	private Animation  mAnimation ;
	private Context context ;
	private Toolbar mToolbar;
    @ Override
    public void onCreate ( Bundle savedInstanceState ) {
    	
        super.onCreate ( savedInstanceState );
        context = this ;
        //AdManager.init(this,"893693f61b171f26", "fa396d910a218fa7", 30, false);
        //YoumiOffersManager.init(this, "893693f61b171f26", "fa396d910a218fa7");
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				//WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView ( R.layout.activity_about_us);
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
		setTitle( getString(R.string.about));
		  // mToolbar.setTitle(resId)
		setSystemBarTintDrawable(getResources().getDrawable(R.drawable.sr_primary));
		
        /*	im1 = (ImageView)findViewById(R.id.splash_image);
      		mAnimation = AnimationUtils.loadAnimation(this,R.anim.splash);  
      		mAnimation.setFillAfter(true);
      		im1.startAnimation(mAnimation);  
      		new Handler().postDelayed(new Runnable(){    
      		    public void run() {    
      		    	Intent intent = new Intent() ;
      		    	intent.setClass(context, MainActivity.class);
      		    	startActivity(intent);
      		    	finish() ;
      		    	
      		    }    
      		 }, 3000); */

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
}

  