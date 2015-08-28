package com.example.toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AplashActivity extends Activity {
	private ImageView im1 ;
	private Animation  mAnimation ;
	public void onCreat(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//im1 = (ImageView)findViewById(R.id.splash_image);
		//mAnimation = AnimationUtils.loadAnimation(this,R.anim.splash);  
		//mAnimation.setFillAfter(true);
		//im1.startAnimation(mAnimation);  
		/*new Handler().postDelayed(new Runnable(){    
		    public void run() {    
		    	
		    }    
		 }, 3000);  */
	}

}
