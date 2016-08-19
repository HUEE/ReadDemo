package com.huee.booksRead;

import com.example.toolbar.AboutUsActivity;
import com.example.toolbar.MainActivity;
import com.example.toolbar.R;
import com.example.toolbar.R.id;
import com.example.toolbar.SystemBarTintManager;
import com.huee.booksRead.LoveReaderActivity.ShelfAdapter;
import com.huee.file.DbHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class author extends ActionBarActivity{
	private Toolbar mToolbar;
	private EditText ed1 ;
	private RelativeLayout commit ;
	String str = "" ;

    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView (R.layout.acthor);
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
		setTitle( getString(R.string.persion));
		  // mToolbar.setTitle(resId)
		setSystemBarTintDrawable(getResources().getDrawable(R.drawable.sr_primary));
		
   
    }
    
    public void init(){  	
    	//ed1 = (EditText)this.findViewById(R.id.ed1);
    	//commit = (RelativeLayout)this.findViewById(R.id.main_click);
    	//commit.setOnClickListener(new OnClickListener() {
			
	/*		@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				str=ed1.getText().toString(); 
				Intent data=new Intent(Intent.ACTION_SENDTO); 
				data.setData(Uri.parse("mailto:h450783795@163.com")); 
				data.putExtra(Intent.EXTRA_SUBJECT, "DABING_SUGGEST"); 
				data.putExtra(Intent.EXTRA_TEXT, str); 
				startActivity(data); 


				
			}
		});*/
			
	

    	
    	
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

