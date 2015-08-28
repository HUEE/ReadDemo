package com.example.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ClassesFragment extends Fragment {
	private View view;//����ҳ��
	Button bt2 ;
	//private BookActivity bok ;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_mycenter,container, false);
        }
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
}
