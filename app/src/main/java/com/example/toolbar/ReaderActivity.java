package com.example.toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
  
public class ReaderActivity extends Activity {
	private ImageView im1 ;
	private Animation  mAnimation ;
	private Context context ;
	Typeface typeface ;
    @ Override
    public void onCreate ( Bundle savedInstanceState ) {
    	
        super.onCreate ( savedInstanceState );
        context = this ;
        //AdManager.init(this,"893693f61b171f26", "fa396d910a218fa7", 30, false);
        //YoumiOffersManager.init(this, "893693f61b171f26", "fa396d910a218fa7");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView ( R.layout.activity_splash);
        	im1 = (ImageView)findViewById(R.id.splash_image);
        	
        	AssetManager mgr=context.getResources().getAssets();
    		typeface=Typeface.createFromAsset(mgr,"Fonts/Content.TTF");	
    		TextView tx1 = (TextView)findViewById(R.id.splash_tx1);
    		TextView tx2 = (TextView)findViewById(R.id.splash_tx2);
    		tx1.setTypeface(typeface);
    		tx2.setTypeface(typeface);
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
      		 }, 1000); 

    }
}

  