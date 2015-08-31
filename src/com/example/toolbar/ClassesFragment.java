package com.example.toolbar;

import com.huee.booksRead.BookActivity;
import com.huee.booksRead.BookActivity2;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ClassesFragment extends Fragment {
	private View view;//����ҳ��
	private RelativeLayout bt1 ;
	private TextView tx_context1 ;
	private TextView tx_context2 ;
	private TextView tx_context3 ;
	private TextView tx_context ;
	private TextView tx_title ;
	TextView tv_title,tv_content;
	Typeface typeface,typeface2,typeface3;
	private Context context ;
	//private BookActivity bok ;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	context = getActivity() ;
        if(view==null){
            view=inflater.inflate(R.layout.fragment_mycenter,container, false);
        }
        
        AssetManager mgr=context.getResources().getAssets();
		typeface=Typeface.createFromAsset(mgr,"Fonts/Lobster.otf");	
		AssetManager mgr2=context.getResources().getAssets();
		typeface2=Typeface.createFromAsset(mgr2,"Fonts/Content.TTF");	
		AssetManager mgr3=context.getResources().getAssets();
		typeface3=Typeface.createFromAsset(mgr3,"Fonts/Regular.TTF");	
		bt1 = (RelativeLayout)view.findViewById(R.id.main_click);
		bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
	    	    intent.setClass(getActivity(), BookActivity2.class);
	    	   // intent.putExtra("bookid", "1");
	    	   // 
	    	    getActivity().startActivity(intent);
			}
		});
       /* bt2 = (Button)view.findViewById(R.id.bt2);
        bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), BookActivity2.class);
				// bok.changeBookId("2");
				startActivity(intent);
			}
		});*/
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent!=null){
            parent.removeView(view);//���Ƴ�
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
}
