package com.huee.booksRead;
  
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.toolbar.R;
import com.huee.file.DbHelper;
import com.huee.file.Main;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.app.ListActivity;

public class LoveReaderActivity extends Activity {

	private static Boolean isExit = false;//用于判断是否推出
	private static Boolean hasTask = false;
	private Context myContext;
    private ShelfAdapter mAdapter;
    private Button shelf_image_button;
    private ListView shelf_list;
    private Button buttontt;
    int[ ] size = null;//假设数据
    private final int SPLASH_DISPLAY_LENGHT = 5000; //延迟五秒    
    private String txtPath = "/sdcard/reader/他们最幸福.txt";
    private final int MENU_RENAME = Menu.FIRST;
	DbHelper db; 
	List<BookInfo> books;
	int realTotalRow;
	int bookNumber ; //图书的数量
	final String[] font = new String[] {"20","24","26","30","32","36",
			"40","46","50","56","60","66","70"};
    
    @ Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        //AdManager.init(this,"893693f61b171f26", "fa396d910a218fa7", 30, false);
        //YoumiOffersManager.init(this, "893693f61b171f26", "fa396d910a218fa7");
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // setContentView ( R.layout.shelf );
        //addYoumi();
      //有米广告
      		//用AdView.setVisibility(GONE)即可隐藏广告条，用AdView.setVisibility(VISIBLE)即可显示广告
//        new Handler().postDelayed(new Runnable() {   
//        	            // 为了减少代码使用匿名Handler创建一个延时的调用   
//        	            public void run() {   
//        	                Intent i = new Intent(ReaderActivity.this, ReaderActivity.class);   
//        	                // 通过Intent打开最终真正的主界面Main这个Activity   
//        	                ReaderActivity.this.startActivity(i); // 启动Main界面   
//        	                ReaderActivity.this.finish(); // 关闭自己这个开场屏   
//        	            }   
//        	        }, SPLASH_DISPLAY_LENGHT);   
        db = new DbHelper(this);
        if (!copyFile()) {
			Toast.makeText(this, "--->电子书存在！", Toast.LENGTH_SHORT).show();
		}
        myContext = this;
        init ();
        /**************初始化书架图书*********************/
        books = db.getAllBookInfo();//取得所有的图书
        bookNumber = books.size();
        int count = books.size();
        int totalRow = count/3; 
        if (count%3 > 0 ){
        	totalRow = count/3 + 1;
        }
        realTotalRow = totalRow;
        if(totalRow<4){
        	totalRow = 4;
        }
        size = new int[totalRow];
        /***********************************/
        mAdapter = new ShelfAdapter ();//new adapter对象才能用
        shelf_list.setAdapter ( mAdapter );
        //注册ContextView到view中  
    }

    
    private void init () {
        //shelf_image_button = ( Button ) findViewById ( R.id.shelf_image_button );
        //shelf_list = ( ListView ) findViewById ( R.id.shelf_list );
    }

    public class ShelfAdapter extends BaseAdapter {

        public ShelfAdapter () {
        }

        @ Override
        public int getCount () {
            if ( size.length > 3 ) {
                return size.length;
            } else {
                return 3;
            }
        }

        @ Override
        public Object getItem ( int position ) {
            return size[ position ];
        }

        @ Override
        public long getItemId ( int position ) {
            return position;
        }

        @ Override
        public View getView ( int position , View convertView , ViewGroup parent ) {
            LayoutInflater layout_inflater = ( LayoutInflater ) LoveReaderActivity.this.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
            View layout = null ;
            //layout_inflater.inflate ( R.layout.shelf_list_item , null );
            if(position < realTotalRow){
            	int buttonNum = (position+1) * 3;
            	if(bookNumber <= 3){
            		buttonNum = bookNumber;
            	}
            /*    for (int i = 0; i < buttonNum; i++) {
                	if(i == 0){
                		BookInfo book = books.get(position*3);
                		String buttonName = book.bookname;
                		buttonName = buttonName.substring(0,buttonName.indexOf("."));
                		Button button = ( Button ) layout.findViewById ( R.id.button_1 );
                		button.setVisibility(View.VISIBLE);
                		button.setText(buttonName);
                		button.setId(book.id);
                		button.setOnClickListener ( new ButtonOnClick () );
                		button.setOnCreateContextMenuListener(listener);
                	}else if(i == 1){
                		BookInfo book = books.get(position*3+1);
                		String buttonName = book.bookname;
                		buttonName = buttonName.substring(0,buttonName.indexOf("."));
                		Button button = ( Button ) layout.findViewById ( R.id.button_2 );
                		button.setVisibility(View.VISIBLE);
                		button.setText(buttonName);
                		button.setId(book.id);
                		button.setOnClickListener ( new ButtonOnClick () );
                		button.setOnCreateContextMenuListener(listener);
                	}else if(i == 2){
                		BookInfo book = books.get(position*3+2);
                		String buttonName = book.bookname;
                		buttonName = buttonName.substring(0,buttonName.indexOf("."));
                		Button button = ( Button ) layout.findViewById ( R.id.button_3 );
                		button.setVisibility(View.VISIBLE);
                		button.setText(buttonName);
                		button.setId(book.id);
                		button.setOnClickListener ( new ButtonOnClick () );
                		button.setOnCreateContextMenuListener(listener);
                	}
    			}*/
                bookNumber -= 3;
            }
            return layout;
        }
    };

  //添加长按点击   
	OnCreateContextMenuListener listener = new OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			//menu.setHeaderTitle(String.valueOf(v.getId()));   
			menu.add(0, 0, v.getId(), "详细信息");
			menu.add(0, 1, v.getId(), "删除本书");
		}
	};
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
    	switch (item.getItemId()) {
		case 0:
			
			break;
		case 1:
			Dialog dialog = new AlertDialog.Builder(LoveReaderActivity.this).setTitle(
					"提示").setMessage(
					"确认要删除吗？").setPositiveButton(
					"确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							BookInfo book = db.getBookInfo(item.getOrder());
							File dest = new File("/sdcard/reader/"+book.pagernum);
							db.delete(item.getOrder());
							if (dest.exists()) {
								dest.delete();
								Toast.makeText(myContext, "删除成功", Toast.LENGTH_SHORT).show(); 
							}else{
								Toast.makeText(myContext, "磁盘文件删除失败", Toast.LENGTH_SHORT).show(); 
							}
							refreshShelf();
						}
					}).setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).create();// 创建按钮
			dialog.show();
			break;
		default:
			break;
		}
    	return true;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	if(requestCode == 222){
    		String isImport = data.getStringExtra("isImport");
    		if("1".equals(isImport)){
    			refreshShelf();
    		}
    	}
    } 
    //重新加载书架
    public void refreshShelf(){
    	 /**************初始化书架图书*********************/
        books = db.getAllBookInfo();//取得所有的图书
        bookNumber = books.size();
        int count = books.size();
        int totalRow = count/3; 
        if (count%3 > 0 ){
        	totalRow = count/3 + 1;
        }
        realTotalRow = totalRow;
        if(totalRow<4){
        	totalRow = 4;
        }
        size = new int[totalRow];
        /***********************************/
		 mAdapter = new ShelfAdapter ();//new adapter对象才能用
	     shelf_list.setAdapter ( mAdapter );
    }
    public class ButtonOnClick implements OnClickListener {
        @ Override
        public void onClick ( View v ) {
//            switch ( v.getId () ) {
//	            case 1 :
        	Intent intent = new Intent();
    	    intent.setClass(LoveReaderActivity.this, BookActivity.class);
    	    intent.putExtra("bookid", String.valueOf(v.getId ()));
    	    startActivity(intent);
        }
    }
    
    public class ButtonOnLongClick implements OnLongClickListener {
		@Override
		public boolean onLongClick(View v) {
			//Toast.makeText(myContext, "再按一次后退键退出应用程序", Toast.LENGTH_SHORT).show(); 
			
			return true;
		}
    }
    
    protected boolean copyFile() {
		try {
			String dst = txtPath;
			File outFile = new File(dst);
			if (!outFile.exists()) {
				File destDir = new File("/sdcard/reader");
				  if (!destDir.exists()) {
				   destDir.mkdirs();
				  }
				InputStream inStream = getResources().openRawResource(R.raw.t1);
				outFile.createNewFile();
				FileOutputStream fs = new FileOutputStream(outFile);
				byte[] buffer = new byte[1024 * 1024];// 1MB
				int byteread = 0;
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
//				db.insert("test.txt", "0","40");
//				db.close();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
    //添加有米广告
//	private void addYoumi(){
//		//初始化广告视图
//		AdView adView = new AdView(this, Color.GRAY, Color.WHITE,200);
//		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//		//设置广告出现的位置(悬浮于屏幕右下角)
//		params.gravity=Gravity.BOTTOM|Gravity.RIGHT;
//		//将广告视图加入Activity中
//		addContentView(adView, params);
//	}
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
		  //pagefactory.createLog();
		  //System.out.println("TabHost_Index.java onKeyDown");
		  if (keyCode == KeyEvent.KEYCODE_BACK) {
				  finish();
			  }
		  
		  return false;
	}
		public boolean onCreateOptionsMenu(Menu menu) {// 创建菜单
			 super.onCreateOptionsMenu(menu);
	        //通过MenuInflater将XML 实例化为 Menu Object
	        MenuInflater inflater = getMenuInflater();
	      //  inflater.inflate(R.menu.main_menu, menu);
			return true;
		}
		@Override
	    protected void onDestroy() {
	    	super.onDestroy();
	    }
		public boolean onOptionsItemSelected(MenuItem item) {// 操作菜单
			int ID = item.getItemId();
			switch(ID){ 
			/*case R.id.mainexit:
				creatIsExit();
				break;
			case R.id.addbook:
				Intent i = new Intent();
				i.setClass(LoveReaderActivity.this, Main.class);
				startActivityForResult(i, 222 );
				//startActivity(new Intent(LoveReaderActivity.this, Main.class));
				//finish();
				break;*/
			default:
				break;

			}
			return true;
		}
		private void creatIsExit() {
			Dialog dialog = new AlertDialog.Builder(LoveReaderActivity.this).setTitle(
					"提示").setMessage(
					"是否要确认LoverReader？").setPositiveButton(
					"确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
//							dialog.cancel();
//							finish();
							LoveReaderActivity.this.finish();
							android.os.Process.killProcess(android.os.Process.myPid());
							System.exit(0);
						}
					}).setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).create();// 创建按钮
			dialog.show();
		}
}