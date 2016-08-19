package com.example.toolbar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.huee.booksRead.BookActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

	private View view;// ����ҳ��
	private RelativeLayout bt1 ;
	private TextView tx_context1 ;
	private TextView tx_context2 ;
	private TextView tx_context3 ;
	private TextView tx_context ;
	private TextView tx_title ;
	TextView tv_title,tv_content;
	//private BookActivity bok ;
	Typeface typeface,typeface2,typeface3;
	private String txtPath = "/sdcard/reader/他们最幸福.txt";
	private String txtPath1 = "/sdcard/reader/乖摸摸头.txt";
	//DbHelper db;
	private Context context ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity() ;
		
		if (view == null) {
			view = inflater
					.inflate(R.layout.fragment_home, container, false);
		}
		AssetManager mgr=context.getResources().getAssets();
		typeface=Typeface.createFromAsset(mgr,"Fonts/Lobster.otf");	
		AssetManager mgr2=context.getResources().getAssets();
		typeface2=Typeface.createFromAsset(mgr2,"Fonts/Content.TTF");	
		AssetManager mgr3=context.getResources().getAssets();
		typeface3=Typeface.createFromAsset(mgr3,"Fonts/Regular.TTF");	
		
		init();
	     /* if (!copyFile2()) {
				Toast.makeText(this, "--->电子书存在！", Toast.LENGTH_SHORT).show();
			}*/
	      
		bt1 = (RelativeLayout)view.findViewById(R.id.main_click);
		//tv_title = (TextView)view.findViewById(R.id.page1_title);
		//tv_content = (TextView)view.findViewById(R.id.pager1_content);
		//tv_title.setTypeface(typeface);
		//tv_content.setTypeface(typeface2);
		//bt1.setTypeface(typeface);
		
		bt1.setOnClickListener(homeOnClickLister);
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);// ���Ƴ�
		}
		
		return view;
	}
	
	private void init(){
		tx_context = (TextView)view.findViewById(R.id.main_page1_context);
		tx_context1 = (TextView)view.findViewById(R.id.main_page1_context1);
		tx_context2 = (TextView)view.findViewById(R.id.main_page1_context2);
		tx_context3 = (TextView)view.findViewById(R.id.main_page1_context3);
		tx_title = (TextView)view.findViewById(R.id.main_page1_title);
		tx_context.setTypeface(typeface2);
		tx_context1.setTypeface(typeface2);
		tx_context2.setTypeface(typeface2);
		tx_context3.setTypeface(typeface2);
		tx_title.setTypeface(typeface3);
	}
	    
		private OnClickListener homeOnClickLister  = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
				case R.id.main_click:
					//bok.changeBookId("1");
					Intent intent = new Intent();
		    	    intent.setClass(getActivity(), BookActivity.class);
		    	   // intent.putExtra("bookid", "1");
		    	   // 
		    	    getActivity().startActivity(intent);
					break ;
				/*case R.id.bt2:
					Intent intent1 = new Intent();
					intent1.setClass(MainListActivity.this,BookActivity.class);
				    intent1.putExtra("bookid", "2");
		    	    startActivity(intent1);
					break ;
			    case R.id.bt3:
					Toast.makeText(MainListActivity.this, "�����ڴ�!!", 0).show() ;
					break ;
			}
				*/
				}
			}
		};
}
