package com.huee.booksRead;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask; 

import bean.Book;
import bean.Chapter;

import com.example.toolbar.R;
import com.huee.file.DbHelper;
import com.huee.file.IOHelper;
import android.annotation.SuppressLint;
import android.app.Activity; 
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface; 
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas; 
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("WrongCall")
public class BookActivity2 extends Activity {
	/** Called when the activity is first created. */
	public final static int OPENMARK = 0;
	public final static int SAVEMARK = 1;
	public final static int TEXTSET = 2;
	/*private String txtPath = "/sdcard/reader1/他们最幸福.txt";
	private String txtPath1 = "/sdcard/reader/乖摸摸头.txt";*/
	private PageWidget mPageWidget;
	private Bitmap mCurPageBitmap, mNextPageBitmap;
	private Canvas mCurPageCanvas, mNextPageCanvas , cavesTemp;
	private BookPageFactory2 pagefactory;
	private static Boolean isExit = false;//�����ж��Ƿ��Ƴ�
	private static Boolean hasTask = false;
	private int whichSize=6;//��ǰ�������С
	private int txtProgress = 0;//��ǰ�Ķ��Ľ���
	private String bookPath = "/sdcard/reader/";
	private SeekBar seekbar ;
	int tmpInt; 
	private Chapter chapter;
	private static Book book1;
	private Button fontcolor1 ;
	private Button fontcolor2 ;
	private Button fontcolor3 ;
	private Button fontcolor4 ;
	private Button fontsize_smaller ;
	private Button fontsize_bigger ;
	
	final String[] font = new String[] {"38","40","41","43","45","47",
			"49","51","53","55","57","60","64"};
	final String[] page = new String[] {"b0.txt","b1.txt","b2.txt","b3.txt","b4.txt","b5.txt",
			"b6.txt","b7.txt","b8.txt","b9.txt","b10.txt","b11.txt"};
	
	final String [] str = {
			"乖，摸摸头","我有一碗酒，可以慰风尘", "对不起", "普通朋友",
			"不许哭", "唱歌的人不许掉眼泪", "听歌的人不许掉眼泪", "一个叫木头，一个叫马尾", "椰子姑娘漂流记",
			"风马少年", "小因果","我的师弟不是人"};
	int curPostion;
	int downX ,upX ;
	DbHelper db; 
	Context mContext;
	Cursor mCursor;
	BookInfo book = null; 
	SetupInfo setup = null;
	PopupWindow popDown ;
    PopupWindow popUp ;
	PopupWindow pop1 ;
	PopupWindow popFont ;
	PopupWindow popLight ;
	View pop_down ;
	View pop_up ;
	View pop_1 ;
	View pop_font ;
	View pop_light ;
	ListView listview ;
	private Context myContext;
	public String bookid = "2";
	boolean flag = true ;
	@SuppressLint("WrongCall")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 myContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Display display = getWindowManager().getDefaultDisplay();
		//mContext = this ;
		int w = display.getWidth();
		int h = display.getHeight(); 
		System.out.println(w + "\t" + h);
		mCurPageBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		pagefactory = new BookPageFactory2(w, h,myContext); 
		pagefactory.setBgBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.read_bg1));
		pagefactory.setFirstBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.firstpage_background));
		
		LayoutInflater inflater = LayoutInflater.from(this);  
		pop_down  = inflater.inflate(R.layout.pop_down, null);  
		pop_up =  inflater.inflate(R.layout.pop_up, null);  
		pop_1 =  inflater.inflate(R.layout.pop_1, null);  
		pop_font =  inflater.inflate(R.layout.pop_font, null);
		pop_light =  inflater.inflate(R.layout.pop_light, null);  
		popInit();
		initView();
		/* 		
	      if (!copyFile()) {
				Toast.makeText(this, "--->电子书存在！", Toast.LENGTH_SHORT).show();
			}
		*/
		//ȡ�ô��ݵĲ���
		//Intent intent = getIntent();
		 
		//String bookid = intent.getStringExtra("bookid");
		db = new DbHelper(myContext);
		try {
			book = db.getBookInfo(Integer.parseInt(bookid));
			setup = db.getSetupInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		db.close();
				pagefactory.setPagerNum(book.pagernum);
				pagefactory.openbook();
				mPageWidget = new PageWidget(this, w, h);
				setContentView(mPageWidget);
				//db.close();
				//pagefactory.slicePage();
				//pagefactory.openbook(bookPath + page[book.pagernum]);
				//int m_mbBufLen = pagefactory.getBufLen();
				
				if (book.bookmark >= 0) { 
					whichSize = setup.fontsize;
					pagefactory.setFontSize(Integer.parseInt(font[setup.fontsize]));
					
					///pos = String.valueOf(m_mbBufLen*0.1);
					//int begin = m_mbBufLen*100/100;
					pagefactory.setBeginPos(Integer.valueOf(book.bookmark));
					
					pagefactory.nextPage();
					//setContentView(mPageWidget);
					//pagefactory.onDraw(mNextPageCanvas);
					pagefactory.onDraw(mCurPageCanvas);
					mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
					//mPageWidget.invalidate();
					mPageWidget.postInvalidate();
					
				
				//mPageWidget.postInvalidate();
				mPageWidget.setOnTouchListener(new OnTouchListener() {
					@SuppressLint("WrongCall")
					@Override
					
					 public boolean onTouch(View v, MotionEvent e) {  
		                // TODO Auto-generated method stub  		                  
		                int ret= 0 ;  
		    	        if (v == mPageWidget) {  
		                     if (e.getAction() == MotionEvent.ACTION_DOWN) {  
		                        //ֹͣ��������forceFinished(boolean)�෴��Scroller����������x��yλ��ʱ��ֹ������             
		                        mPageWidget.abortAnimation();  
		                        //������ק���Ӧ����ק��  
		                        mPageWidget.calcCornerXY(e.getX(), e.getY());  
		                        //�����ֻ��ڵ�ǰҳ  
		                        mPageWidget.downXY(e.getX(), e.getY());
		                    }  
		                    ret = mPageWidget.doTouchEvent(e);  
		                    
		                    switch(ret){		                    
		                    case 1 :  //左
		                    	//false����ʾ��һҳ   
		                    	int h = pagefactory.nextPage() ;
		                    	if(h ==0 )
		                    	{
		                    		pagefactory.onDraw(mNextPageCanvas);
		                    		break ;
		                    	}
		                    		
								if(h==2)  
								{
									pagefactory.onDraw(mNextPageCanvas);
									break ;
									
									
								}
								else if(h==1)
								{
									pagefactory.setFirstPage(mNextPageCanvas);
									flag = false;
									//pagefactory.setFirstPage(mCurPageCanvas);
								}
	                            //if(pagefactory.islastPage()){
									//Toast.makeText(mContext, "已经是最后一页！",Toast.LENGTH_SHORT).show(); 
									//return false;
								//}
	                           
	                           break ;
		                    case 2 :  //右
		                    	 //true����ʾ��һҳ  
		                    	int f = pagefactory.prePage();
		                    	if(f==0)
		                    	{
		                    		pagefactory.setFirstPage(mNextPageCanvas);
									flag = false;
		                    	}
		                    	else if(f==1){
		                    		pagefactory.setFirstPage(mNextPageCanvas);
		                    		flag = false;
		                    		dialog();
		                    	}
		                    	else if(f==2)
		                    	{
		                    		
								//pagefactory.prePage();                         
		                           // if(pagefactory.isfirstPage()){
									//	Toast.makeText(mContext, "�Ѿ��ǵ�һҳ",Toast.LENGTH_SHORT).show(); 
										//return false;
									//}  
		                            pagefactory.onDraw(mNextPageCanvas);  
		                    	}
		                            break ;
		                    case 3 :  //up
		                    	popDown.dismiss();
								popUp.dismiss();
		                    	TimerTask task = new TimerTask(){  
		                    	    @SuppressLint("WrongCall")
									public void run(){  
		                    	    	if(!flag)
		                    	    	{
		                    	    		pagefactory.setFirstPage(mCurPageCanvas);
		                    	    		mPageWidget.postInvalidate();
		                    	    		flag = true ;
		                    	    	}
		                    	    	else
		                    	    	{
		                    	    	pagefactory.onDraw(mCurPageCanvas);  
					                    /*	cavesTemp = mCurPageCanvas ;
					                    	mCurPageCanvas  =  mNextPageCanvas ; 
					                    	mNextPageCanvas = mCurPageCanvas ;*/
					                     	mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap); 
					                    	 //mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap); 
					                    	 //mCurPageBitmap = mNextPageBitmap;
					                    	// mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);  
					                    	mPageWidget.postInvalidate();
		                    	    	}
		                    	    }  
		                    	};  
		                    	Timer timer = new Timer();
		                    	timer.schedule(task,300);
		                   	 
			                    	break ;
		                    case 4 :  //down
		                    	if(popDown.isShowing()){
									popDown.dismiss();
									popUp.dismiss();
									}
								else{
									popDown.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, 0);
									popUp.showAtLocation(mPageWidget, Gravity.TOP, 0, 0);
									}
		                    	if(popFont.isShowing())
		                    		popFont.dismiss(); 
		                    	if(popLight.isShowing())
		                    		popLight.dismiss(); 
		                    	break ;
		                    
		                    }
		              /*      if(mPageWidget.isChange())//
		                     {
		                    	 pagefactory.onDraw(mCurPageCanvas);  
		                    	cavesTemp = mCurPageCanvas ;
		                    	mCurPageCanvas  =  mNextPageCanvas ; 
		                    	mNextPageCanvas = mCurPageCanvas ;
		                     	mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap); 
		                    	 //mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap); 
		                    	 //mCurPageBitmap = mNextPageBitmap;
		                    	// mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);  
		                    	mPageWidget.postInvalidate();
		                     }
*/
		                    
		                   
		                    return true;  
		                }  
		                return false;  
		            }  
				/*	public boolean onTouch(View v, MotionEvent e) {
						boolean ret = false;
						if (v == mPageWidget) {
							mPageWidget.abortAnimation();
							mPageWidget.calcCornerXY(e.getX(), e.getY());
							pagefactory.onDraw(mCurPageCanvas);
							if(e.getAction() == MotionEvent.ACTION_DOWN)
							{
								downX = (int) e.getX();
								return true;
							}
							else if(e.getAction() == MotionEvent.ACTION_UP)
							{
								upX = (int)e.getX();
								if(Math.abs(upX-downX)<10)
								{
									if(popDown.isShowing()){
										popDown.dismiss();
										popUp.dismiss();
										}
									else{
										popDown.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, 0);
										popUp.showAtLocation(mPageWidget, Gravity.TOP, 0, 0);
										}
								}
								else if(Math.abs(upX-downX)>10)
								{
									popDown.dismiss();
									popUp.dismiss();
									pop1.dismiss();
									
									if (mPageWidget.DragToRight()) {
											try {
												pagefactory.prePage();
											} catch (IOException e1) {
												e1.printStackTrace();
											}
											if (pagefactory.isfirstPage()){
												Toast.makeText(mContext, "�Ѿ��ǵ�һҳ",Toast.LENGTH_SHORT).show(); 
												return false;
											}
											pagefactory.onDraw(mNextPageCanvas);
									}
									else {
											try {
												pagefactory.nextPage();
											} catch (IOException e1) {
												e1.printStackTrace();
											}
											if (pagefactory.islastPage()){
												Toast.makeText(mContext, "�Ѿ������һҳ",Toast.LENGTH_SHORT).show();
												return false;
											}
											pagefactory.onDraw(mNextPageCanvas);
									}
									mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap)	;
									ret = mPageWidget.doTouchEvent(e);
									return ret;
								}
							}
					}
													
						
						return false;
					}*/
						/*	if (e.getAction() == MotionEvent.ACTION_DOWN||e.getAction() == MotionEvent.ACTION_UP) {
								if(e.getAction() == MotionEvent.ACTION_DOWN)
									downX =  e.getX();
								 if(e.getAction() == MotionEvent.ACTION_UP)
								{
										upX = e.getX();
										if(Math.abs(upX-downX)<100)
										{
											if(popDown.isShowing()){
												popDown.dismiss();
												popUp.dismiss();
										}
											else{
												popDown.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, 0);
												popUp.showAtLocation(mPageWidget, Gravity.TOP, 0, 0);
											}
										}
								}
								else{
									popDown.dismiss();
									popUp.dismiss();
									pop1.dismiss();
									mPageWidget.abortAnimation();
									mPageWidget.calcCornerXY(e.getX(), e.getY());
									pagefactory.onDraw(mCurPageCanvas);
									if (mPageWidget.DragToRight()) {
											try {
												pagefactory.prePage();
											} catch (IOException e1) {
												e1.printStackTrace();
											}
											if (pagefactory.isfirstPage()){
												Toast.makeText(mContext, "�Ѿ��ǵ�һҳ",Toast.LENGTH_SHORT).show(); 
												return false;
											}
											pagefactory.onDraw(mNextPageCanvas);
									}
									else {
											try {
												pagefactory.nextPage();
											} catch (IOException e1) {
												e1.printStackTrace();
											}
											if (pagefactory.islastPage()){
												Toast.makeText(mContext, "�Ѿ������һҳ",Toast.LENGTH_SHORT).show();
												return false;
											}
											pagefactory.onDraw(mNextPageCanvas);
									}
									mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap)	;
								}*/
								
			
				});
			}else{
				Toast.makeText(myContext, "1223",Toast.LENGTH_SHORT).show(); 
				BookActivity2.this.finish();
			}
			
		//mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
	}
//	static {
//		AdManager.init("6922e1ee73dac5b3", "2eec7a7b5e83c490", 31, false);
//	}
	
	public void changeBookId(String str)
	{
		this.bookid = str ;
	}
	
	
	public void popInit(){		 
        // ����PopupWindow����  
        popDown = new PopupWindow(pop_down, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);
        popDown.setAnimationStyle(R.style.PopupAnimation);
       // popDown.setFocusable(true);
        
        popUp = new PopupWindow(pop_up, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);
        popUp.setAnimationStyle(R.style.PopdownAnimation);
        //popUp.setFocusable(true);
        
        pop1 = new PopupWindow(pop_1, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
        pop1.setAnimationStyle(R.style.PopupAnimation);
        
        //pop1.setFocusable(true);
       
        
        popFont= new PopupWindow(pop_font, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);
        popFont.setAnimationStyle(R.style.PopupAnimation);
        
        popLight= new PopupWindow(pop_light, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);
        popLight.setAnimationStyle(R.style.PopupAnimation);
}
	
	@SuppressLint("WrongCall")
	public void initView(){
		List<Map<String,String>> list = new ArrayList<Map<String, String>>();
		listview = (ListView)pop_1.findViewById(R.id.pop_bookPagerList);

		
		for(int i = 0 ; i< 11 ;i++)
		{
			Map<String ,String> map = new HashMap<String ,String>(); 
			map.put("title",str[i] );
			list.add(map);
		}
		SimpleAdapter sim = new SimpleAdapter(
				myContext,
				list,
				R.layout.pop_pagerselect,
				new String[] {"title"},
				new int[] {R.id.pager_item});
		listview.setAdapter(sim);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("WrongCall")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
						pop1.dismiss();
						pagefactory.setPagerNum(arg2);
						pagefactory.openbook();
						pagefactory.nextPage();
						pagefactory.onDraw(mNextPageCanvas);
						pagefactory.onDraw(mCurPageCanvas);
						mPageWidget.postInvalidate();
						//pagefactory.sellect	BookPag();										
			}
		});
		ImageButton imbt1 = (ImageButton)pop_down.findViewById(R.id.a);
		ImageButton imbt2 = (ImageButton)pop_down.findViewById(R.id.b);
		ImageButton imbt3 = (ImageButton)pop_down.findViewById(R.id.c);
		ImageButton imbt4 = (ImageButton)pop_down.findViewById(R.id.d);
		imbt1.setOnClickListener(OnClick);
		imbt2.setOnClickListener(OnClick);
		imbt3.setOnClickListener(OnClick);
		imbt4.setOnClickListener(OnClick);

	}
	

	@SuppressLint("WrongCall")
	View.OnClickListener OnClick =new  View.OnClickListener() {
		
		@SuppressLint("WrongCall")
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch(arg0.getId()){
			case R.id.a:
				itemDig();
				/*popDown.dismiss();
				popUp.dismiss();
				pop1.showAtLocation(mPageWidget, Gravity.CENTER, 0, 0);*/
				//Toast.makeText(MainActivity.this, "--->image1", 0).show();
				break ;
			case R.id.b:
				popDown.dismiss();
				popUp.dismiss();
				setFont();
				//Toast.makeText(myContext, "--->image2", 0).show();
				break ;
			case R.id.c:
				popDown.dismiss();
				popUp.dismiss();
				setLight();
				//Toast.makeText(MainActivity.this, "--->image3", 0).show();
				break ;
			case R.id.d:
				//Toast.makeText(MainActivity.this, "--->image4", 0).show();
				popDown.dismiss();
				popUp.dismiss();
				pagefactory.nextChapter();
				pagefactory.setFirstPage(mCurPageCanvas);
				mPageWidget.postInvalidate();
				break ;
			case R.id.fontcolor1:
				pagefactory.setFontColor(3);
				pagefactory.onDraw(mCurPageCanvas);
				mPageWidget.postInvalidate();
				break ;
			case R.id.fontcolor2:
				pagefactory.setFontColor(3);
				pagefactory.onDraw(mCurPageCanvas);
				mPageWidget.postInvalidate();
				break ;
			case R.id.fontcolor3:
				pagefactory.setFontColor(3);
				pagefactory.onDraw(mCurPageCanvas);
				mPageWidget.postInvalidate();
				break ;
			case R.id.fontcolor4:
				pagefactory.setFontColor(3);
				pagefactory.onDraw(mCurPageCanvas);
				mPageWidget.postInvalidate();
				break ;
			case R.id.fontsize_smaller:
				whichSize = whichSize-1;
				if(whichSize<0)
					whichSize = 0 ;
				pagefactory.setFontSize(Integer.parseInt(font[whichSize]));
				int pos = pagefactory.getCurPostionBeg();
				pagefactory.setBeginPos(pos);
				pagefactory.nextPage();
				setContentView(mPageWidget);
				pagefactory.onDraw(mCurPageCanvas);
				//mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
				mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
				mPageWidget.invalidate();
				break ;
			case R.id.fontsize_bigger:
				whichSize = whichSize+1;
				if(whichSize>12)
					whichSize = 12 ;
				pagefactory.setFontSize(Integer.parseInt(font[whichSize]));
				int pos1 = pagefactory.getCurPostionBeg();
				pagefactory.setBeginPos(pos1);
				pagefactory.nextPage();
				setContentView(mPageWidget);
				pagefactory.onDraw(mCurPageCanvas);
				//mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
				mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
				mPageWidget.invalidate();
				break ;
				
			}
			
		}
	};
	public void setLight()
	{
		popDown.dismiss();
		popUp.dismiss();
		popLight.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, 0);
		
		seekbar = (SeekBar) pop_light.findViewById(R.id.seekBar) ;
		seekbar.setMax(255); 
		if(!isAutoBrightness(getContentResolver())){
			stopAutoBrightness(this.getParent());         	  
		}
		int normal = android.provider.Settings.System.getInt(getContentResolver(),  
                Settings.System.SCREEN_BRIGHTNESS, 255);
		seekbar.setProgress(normal*100/255);				
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				//tmpInt = seekbar.getProgress(); 
				//if (tmpInt < 80) {  
                    //tmpInt = 80;  
               // }  
				
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stubt
				tmpInt = seekbar.getProgress() ;
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
	public void setFont()
	{
		
		popDown.dismiss();
		popUp.dismiss();
		popFont.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, 0);
		fontcolor1 = (Button)pop_font.findViewById(R.id.fontcolor1);
		fontcolor2 = (Button)pop_font.findViewById(R.id.fontcolor2);
		fontcolor3 = (Button)pop_font.findViewById(R.id.fontcolor3);
		fontcolor4 = (Button)pop_font.findViewById(R.id.fontcolor4);
		fontsize_smaller = (Button)pop_font.findViewById(R.id.fontsize_smaller);
		fontsize_bigger = (Button)pop_font.findViewById(R.id.fontsize_bigger);
		
		fontcolor1.setOnClickListener(OnClick);
		fontcolor2.setOnClickListener(OnClick);
		fontcolor3.setOnClickListener(OnClick);
		fontcolor4.setOnClickListener(OnClick);
		fontsize_smaller.setOnClickListener(OnClick);
		fontsize_bigger.setOnClickListener(OnClick);
		
		
		
	}
	
    public static boolean isAutoBrightness(ContentResolver aContentResolver) {          	  
	    boolean automicBrightness = false;        
	    try{           
	    	automicBrightness = Settings.System.getInt(aContentResolver,                    
	    	Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;     
	    }   
	    catch(SettingNotFoundException e){           
	    	e.printStackTrace();    
	      }        
	    return automicBrightness;  
    }
    
    public static int getScreenBrightness(Activity activity) {         	  
        int nowBrightnessValue = 0;            
        ContentResolver resolver = activity.getContentResolver();            
        try{               
        nowBrightnessValue = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);          
          }  
        catch(Exception e) {              
         e.printStackTrace();          
         }            
        return nowBrightnessValue;  
        }  
    
    public static void setBrightness(Activity activity, int brightness) {         	  
        // Settings.System.putInt(activity.getContentResolver(),           
       // Settings.System.SCREEN_BRIGHTNESS_MODE,          
       // Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);           
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
    
	public void selsctPage(int k)
	{
		pop1.dismiss();
		popDown.dismiss();
		popUp.dismiss();
		pagefactory.setPagerNum(k);
		pagefactory.setFirstPage(mCurPageCanvas);
		pagefactory.openbook();
		pagefactory.setBeginPos(-1);
		//pagefactory.nextPage();
		//pagefactory.onDraw(mNextPageCanvas);
		//pagefactory.onDraw(mCurPageCanvas);
		mPageWidget.postInvalidate();
	}
	
	public void itemDig(){
		/*com.tdx.ui.ActionSheetDialog dialog = */
		new com.example.toolbar.widget.ActionSheetDialog(BookActivity2.this)
		.builder()
		.setTitle("请选择章节")
		.setCancelable(false)
		.setCanceledOnTouchOutside(false)
		.addSheetItem(str[0], com.example.toolbar.widget.ActionSheetDialog.SheetItemColor.Blue,
				new com.example.toolbar.widget.ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Toast.makeText(BookActivity2.this,
								"item" + which, Toast.LENGTH_SHORT)
								.show();
						selsctPage(0);
					}
				})
		.addSheetItem(str[1], com.example.toolbar.widget.ActionSheetDialog.SheetItemColor.Blue,
				new com.example.toolbar.widget.ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Toast.makeText(BookActivity2.this,
								"item" + which, Toast.LENGTH_SHORT)
								.show();
						selsctPage(1);
					}
				})
		.addSheetItem(str[2], com.example.toolbar.widget.ActionSheetDialog.SheetItemColor.Blue,
				new com.example.toolbar.widget.ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Toast.makeText(BookActivity2.this,
								"item" + which, Toast.LENGTH_SHORT)
								.show();
						selsctPage(2);
					}
				})
		.addSheetItem(str[3], com.example.toolbar.widget.ActionSheetDialog.SheetItemColor.Blue,
				new com.example.toolbar.widget.ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Toast.makeText(BookActivity2.this,
								"item" + which, Toast.LENGTH_SHORT)
								.show();
						selsctPage(3);
					}
				})
		.addSheetItem(str[4], com.example.toolbar.widget.ActionSheetDialog.SheetItemColor.Blue,
				new com.example.toolbar.widget.ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Toast.makeText(BookActivity2.this,
								"item" + which, Toast.LENGTH_SHORT)
								.show();
						selsctPage(4);
					}
				})
		.addSheetItem(str[5], com.example.toolbar.widget.ActionSheetDialog.SheetItemColor.Blue,
				new com.example.toolbar.widget.ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Toast.makeText(BookActivity2.this,
								"item" + which, Toast.LENGTH_SHORT)
								.show();
						selsctPage(5);
					}
				})
		.addSheetItem(str[6],com.example.toolbar.widget.ActionSheetDialog.SheetItemColor.Blue,
				new com.example.toolbar.widget.ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Toast.makeText(BookActivity2.this,
								"item" + which, Toast.LENGTH_SHORT)
								.show();
						selsctPage(6);
					}
				})
		.addSheetItem(str[7],com.example.toolbar.widget.ActionSheetDialog.SheetItemColor.Blue,
				new com.example.toolbar.widget.ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Toast.makeText(BookActivity2.this,
								"item" + which, Toast.LENGTH_SHORT)
								.show();
						selsctPage(7);
					}
				})
		.addSheetItem(str[8],com.example.toolbar.widget.ActionSheetDialog.SheetItemColor.Blue,
				new com.example.toolbar.widget.ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Toast.makeText(BookActivity2.this,
								"item" + which, Toast.LENGTH_SHORT)
								.show();
						selsctPage(8);
					}
				})
		.addSheetItem(str[9], com.example.toolbar.widget.ActionSheetDialog.SheetItemColor.Blue,
				new com.example.toolbar.widget.ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Toast.makeText(BookActivity2.this,
								"item" + which, Toast.LENGTH_SHORT)
								.show();
						selsctPage(9);
					}
				}).show();
	}
	
	public void dialog(){
		new com.example.toolbar.widget.AlertDialog(BookActivity2.this).builder().setTitle("继续阅读")
		.setMsg("当前章节已阅读完毕，继续阅读其他章节！！")
		.setPositiveButton("上一章", new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				pagefactory.preChapter();
				pagefactory.setFirstPage(mCurPageCanvas);
				mPageWidget.postInvalidate();
			}
		}).setNegativeButton("下一章", new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				pagefactory.nextChapter();
				pagefactory.setFirstPage(mCurPageCanvas);
				mPageWidget.postInvalidate();
				
			}
		}).show();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {// �����˵�
		 super.onCreateOptionsMenu(menu);
        //ͨ��MenuInflater��XML ʵ����Ϊ Menu Object
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	/* protected boolean copyFile() {
			try {
				String dst = txtPath;
				File outFile = new File(dst);
				if (!outFile.exists()) {
					File destDir = new File("/sdcard/reader1");
					  if (!destDir.exists()) {
					   destDir.mkdirs();
					  }
					InputStream inStream = getResources().openRawResource(
							R.raw.t1);
					outFile.createNewFile();
					FileOutputStream fs = new FileOutputStream(outFile);
					byte[] buffer = new byte[1024 * 1024];// 1MB
					int byteread = 0;
					while ((byteread = inStream.read(buffer)) != -1) {
						fs.write(buffer, 0, byteread);
					}
					inStream.close();
					fs.close();
//					db.insert("test.txt", "0","40");
//					db.close();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	 */
	@SuppressLint("WrongCall")
	public boolean onOptionsItemSelected(MenuItem item) {// �����˵�
		int ID = item.getItemId();
		switch (ID) { 
		case R.id.exitto:
			addBookMark();
			//dialog.cancel();
			finish();
			//creatIsExit();
			break;
		case R.id.fontsize:
			new AlertDialog.Builder(this)
			.setTitle("��ѡ��")
			.setIcon(android.R.drawable.ic_dialog_info)                
			.setSingleChoiceItems(font, whichSize, 
			  new DialogInterface.OnClickListener() {
			     public void onClick(DialogInterface dialog, int which) {
			    	 dialog.dismiss();
			    	 setFontSize(Integer.parseInt(font[which]));
			    	 whichSize = which;
			    	 //Toast.makeText(mContext, "��ѡ�е���"+font[which], Toast.LENGTH_SHORT).show();
			       // dialog.dismiss();
			     }
			  }
			)
			.setNegativeButton("ȡ��", null)
			.show();
			break;
		case R.id.nowprogress:
			LayoutInflater inflater = getLayoutInflater();
			   final View layout = inflater.inflate(R.layout.bar,
			     (ViewGroup) findViewById(R.id.seekbar));
			   SeekBar seek = (SeekBar)layout.findViewById(R.id.seek);
			   final TextView textView = (TextView)layout.findViewById(R.id.textprogress);
			   txtProgress = pagefactory.getCurProgress();
			   seek.setProgress(txtProgress);
			   textView.setText(String.format(getString(R.string.progress), txtProgress+"%"));
			   seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				   int progressBar = 0;
					@SuppressLint("WrongCall")
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						int progressBar = seekBar.getProgress();
						int m_mbBufLen = pagefactory.getBufLen();
						int pos = m_mbBufLen*progressBar/100;
						if(progressBar == 0){
							pos = 1;
						}
						pagefactory.setBeginPos(Integer.valueOf(pos));
						pagefactory.prePage();
						//setContentView(mPageWidget);
						pagefactory.onDraw(mCurPageCanvas);
						mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
						//mPageWidget.invalidate();
						mPageWidget.postInvalidate();
					} 
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						//Toast.makeText(mContext, "StartTouch", Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						if(fromUser){
							textView.setText(String.format(getString(R.string.progress), progress+"%"));
						}
					}
				});
			   new AlertDialog.Builder(this).setTitle("��ת").setView(layout)
			     .setPositiveButton("ȷ��", 
			    		 new DialogInterface.OnClickListener() {
						     public void onClick(DialogInterface dialog, int which) {
						    	 //Toast.makeText(mContext, "��ѡ�е���", Toast.LENGTH_SHORT).show();
						        dialog.dismiss();
						     }
						  }
			    		 ).show();
			break;
		default:
			break;

		}
		return true;
	}
	
	private void setFontSize(int size){
		pagefactory.setFontSize(size);
		int pos = pagefactory.getCurPostionBeg();
		pagefactory.setBeginPos(pos);
		pagefactory.nextPage();
		setContentView(mPageWidget);
		pagefactory.onDraw(mNextPageCanvas);
		//mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
		mPageWidget.setBitmaps(mNextPageBitmap, mNextPageBitmap);
		mPageWidget.invalidate();
		//mPageWidget.postInvalidate();
	}
	
	private void creatIsExit() {
		Dialog dialog = new AlertDialog.Builder(BookActivity2.this).setTitle(
				"��ʾ").setMessage(
				"�Ƿ�ȷ���˳���ȷ���������").setPositiveButton(
				"ȷ��",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
						finish();
					}
				}).setNegativeButton("ȡ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create();// ������ť
		dialog.show();
	}
	Timer tExit = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            isExit = false;
            hasTask = true;
        }
    };
    
	  @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		  switch(keyCode){
			   case KeyEvent.KEYCODE_BACK:
					  addBookMark();
					  startAutoBrightness(this);
					  this.finish();
						break ;
			/*	case KeyEvent.KEYCODE_MENU:
					if(popDown.isShowing()&popUp.isShowing()){
						popDown.dismiss();
						popUp.dismiss();			
						}
					else{
						popDown.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, 0);
						popUp.showAtLocation(mPageWidget, Gravity.TOP, 0, 0);
					}
					break ;*/
		    }  
		  return false;
	}
	  
/*	  @Override
	    public boolean onTouchEvent(MotionEvent event) {
	    	if(event.getAction()==MotionEvent.ACTION_DOWN) 	{
				if(popDown.isShowing()){
					popDown.dismiss();
					popUp.dismiss();
				}
				else{
					popDown.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, 0);
					popUp.showAtLocation(mPageWidget, Gravity.TOP, 0, 0);
			}			
			if(pop1.isShowing())
				pop1.dismiss();
	    }
			return true;
}*/
	  
	  
	  
	//�����ǩ
	@SuppressLint("WrongCall")
	public void addBookMark(){
		Message msg = new Message();
		msg.what = SAVEMARK;
		msg.arg1 = whichSize;
		curPostion = pagefactory.getCurPostion();
		msg.arg2 = curPostion;
		mhHandler.sendMessage(msg);
	}
	
	@SuppressLint("WrongCall")
	Handler mhHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case TEXTSET:
				pagefactory.changBackGround(msg.arg1);
				pagefactory.onDraw(mCurPageCanvas);
				mPageWidget.postInvalidate();
				break;

			case OPENMARK:
				try {
					mCursor = db.select();

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (mCursor.getCount() > 0) {
					mCursor.moveToPosition(mCursor.getCount() - 1);
					String pos = mCursor.getString(2);
					String tmp = mCursor.getString(1);
					 
					pagefactory.setBeginPos(Integer.valueOf(pos));
					pagefactory.prePage();
					pagefactory.onDraw(mNextPageCanvas);
					mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					mPageWidget.invalidate();
					db.close(); 
				}
				break;

			case SAVEMARK:
				try {
					db.update(book.id, String.valueOf(book.pagernum), String.valueOf(msg.arg2));
					db.updateSetup(setup.id,String.valueOf(msg.arg1), "0", "0");
					//mCursor = db.select();
				} catch (Exception e) {
					e.printStackTrace();
				}
//				System.out.println(mCursor.getCount());
//				if (mCursor.getCount() > 0) {
//					mCursor.moveToPosition(mCursor.getCount()-1);
//					db.update(book.id, book.bookname, String.valueOf(msg.arg2),String.valueOf(msg.arg1));
//				} else {
//					db.insert("", String.valueOf(msg.arg2),String.valueOf(msg.arg1));
//				}
				db.close();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
}