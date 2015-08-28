
package com.huee.booksRead;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector; 

import com.example.toolbar.R;
import com.huee.file.IOHelper;

import bean.Chapter;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.widget.Toast;

public class BookPageFactory {

	private File book_file = null;
	private MappedByteBuffer m_mbBuf = null;
	private int m_mbBufLen = 0;
	private int m_mbBufBegin = 0; //50
	private int m_mbBufEnd = 0;
	private String m_strCharsetName = "GBK";
	private Bitmap m_book_bg = null;
	private Bitmap first_book_bg = null;
	private Bitmap pager_imag = null;
	private int mWidth;
	private int mHeight;

	private Vector<String> m_lines = new Vector<String>();

	private int m_fontSize = 40;
	private int r_fontSize = 30;
	private int m_textColor = Color.BLACK;
	private int m_backColor = 0xffff9e85; // 背景颜色
	private int marginWidth = 30; // 左右与边缘的距离
	private int marginHeight = 30; // 上下与边缘的距离
	private int youmiHeight = 0;//广告条的狂度

	private int mLineCount; // 每页可以显示的行数
	private float mVisibleHeight; // 绘制内容的高
	private float mVisibleWidth; // 绘制内容的宽
	private boolean m_isfirstPage, m_islastPage;
	private int b_FontSize = 25;//底部文字大小
	private int e_fontSize = 5;
	private int spaceSize = 20;//行间距大小
	private int curProgress = 0;//当前的进度
	private int pagernum = 5;

	// private int m_nLineSpaceing = 5;
	private Vector<Vector<String>> pagesVe;
	private int chapterLen; // 章节的长度
	private String content;
	final String[] page = new String[] {"a0.txt","a1.txt","a2.txt","a3.txt","a4.txt","a5.txt",
			"a6.txt","a7.txt","a8.txt","a9.txt","a10.txt","a11"};
	private String bookPath = "/sdcard/reader/";
	Context context ;

	final String [] str1 = {
			"序言:有梦为马","第一章：伴我行天涯", "第二章：流浪歌手的情人", "第三章：送你一颗糖",
			"第四章：越狱者", "第五章：西藏往事", "第六章：不用手机的女孩儿", "第七章：想把我唱给你听", "第八章：预约你的墓志铭",
			"第九章：到死之前，我们都是需要发育的孩子", "第十章：艽野羌塘，尘梦凤凰","后记:陪我到可可西里去看海"};
	
	private Chapter chapter; // 需要处理的章节对象
	//private Vector<String> linesVe; // 将章节內容分成行，并将每页按行存储到vector对象中
	
	private boolean isfirstPage;
	private boolean islastPage;
	private Paint mPaint;
	private Paint bPaint;//底部文字绘制
	private Paint spactPaint;//行间距绘制
	private Paint titlePaint;//标题绘制
	private Paint firstTitlePaint ;
	private Paint firstContentPaint ;
	Typeface typeface_title;
	Typeface read_content ;
	Typeface read_button ;
	Typeface typeface_content ;
	

	public BookPageFactory(int w, int h, Context context) {
		// TODO Auto-generated constructor stub
		this.context = context ;
		mWidth = w;
		mHeight = h;
		AssetManager mgr=context.getResources().getAssets();
		read_content=Typeface.createFromAsset(mgr,"Fonts/Content.TTF");
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		//mPaint.setTextSize(30);
		mPaint.setTypeface(read_content);
		mPaint.setTextSize(m_fontSize);
		//mPaint.setColor(m_textColor);
	
		//mPaint.setTextSkewX(0.1f);//设置斜体
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2 - youmiHeight;
		int totalSize = m_fontSize+spaceSize;
		mLineCount = (int) ((mVisibleHeight)/ totalSize); // 可显示的行数
		//底部文字绘制
		
		bPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bPaint.setTextAlign(Align.LEFT);
		bPaint.setTextSize(b_FontSize);
		bPaint.setTypeface(read_content);
		bPaint.setColor(m_textColor);
		//行间距设置
		spactPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		spactPaint.setTextAlign(Align.LEFT);
		spactPaint.setTextSize(spaceSize);
		spactPaint.setColor(m_textColor);
		//章节首页文字标题
		titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		titlePaint.setTextAlign(Align.LEFT);
		titlePaint.setTextSize(30);
		titlePaint.setColor(m_textColor);	
		//章节首页文字标题
		
		typeface_title=Typeface.createFromAsset(mgr,"Fonts/Regular.TTF");
		firstTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		firstTitlePaint.setTextAlign(Align.LEFT);
		firstTitlePaint.setTextSize(60);
		firstTitlePaint.setTypeface(typeface_title);
		firstTitlePaint.setColor(m_textColor);
		//章节首页内容简介
		typeface_content=Typeface.createFromAsset(mgr,"Fonts/Content.TTF");
		firstContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		firstContentPaint.setTextAlign(Align.LEFT);
		firstContentPaint.setTextSize(40);
		firstContentPaint.setTypeface(typeface_content);
		firstContentPaint.setColor(m_textColor);
		
	}

	public void openbook() {
		try {
			String filepath = bookPath+page[pagernum];
			m_mbBufBegin = 0;
			m_mbBufEnd = 0 ;
			//String filepath = "/sdcard/reader/5.txt";
			book_file = new File(filepath);
			long lLen = book_file.length();
			m_mbBufLen = (int) lLen;
			m_mbBuf = new RandomAccessFile(book_file, "r").getChannel().map(
					FileChannel.MapMode.READ_ONLY, 0, lLen);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public boolean isFirstPage() {
		if (pagernum <= 0)
			return true;
		return false;
	}
	
	public boolean isLastPage() {
		if (pagernum >= pagesVe.size() - 1)
			return true;
		return false;
	}
	
	protected int prePage() {
		if(m_mbBufBegin == 0)
		{
			m_mbBufBegin = -1 ;
			m_mbBufEnd = -1 ;
			return 0 ;
		}
		else if (m_mbBufBegin < 0) {			
			//preChapter(); // 如果已经到本书第一章，就不能继续执行翻页代码
			return 1 ;
		}else {
			m_isfirstPage = false;
			m_lines.clear();
			pageUp();
			m_lines = pageDown();
		}
		return 2 ;
	}

	public int nextPage(){
		if(m_mbBufEnd<0)
		{
			m_lines.clear();
			m_mbBufEnd = 0 ;
			m_mbBufBegin = 0;
			m_lines = pageDown();
			return 0 ;
		}
		else if (m_mbBufEnd >= m_mbBufLen) {
			m_mbBufBegin = -1;
			m_mbBufEnd = -1 ;
			pagernum++;
			nextChapter();// 如果已经到本书末尾，那么不能继续执行翻页代码
			return 1 ;
		} else
		{
			//m_islastPage = false;
			m_lines.clear();
			m_mbBufBegin = m_mbBufEnd;
			m_lines = pageDown();
		}
		return 2 ;
	}
	
	/**
	 * 跳到上一章,若返回值为false，则当前章节已经为第一章
	 */
	public void preChapter() {
		//int order = chapter.getOrder();
		//Chapter tempChapter = IOHelper.getChapter(order - 1);
		
		pagernum--;
		//setFirstPage(c);
		String strParagraph = "";
		if (pagernum < 0)
		{
			pagernum = 0 ;
			
		}
		else{
		openbook();
		//byte[] paraBuf = readParagraphForward(m_mbBufEnd); 
		//try {
		//	strParagraph = new String(paraBuf, m_strCharsetName);
		//} catch (UnsupportedEncodingException e) {
		//	// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		//int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,null);
		//m_mbBufEnd = m_mbBufLen-m_mbBufLen%(mLineCount*nSize);
		//if(m_mbBufEnd%2!=0)
			//m_mbBufEnd = m_mbBufEnd+3;
		//nextPage();
		}
		/*chapter = tempChapter;
		content = chapter.getContent();
		chapterLen = content.length();
		// curCharPos = chapterLen;
		m_mbBufBegin = chapterLen;
		m_mbBufEnd = chapterLen;
		slicePage();
		pageNum = pagesVe.size();*/
		//return true;
	}
	
	/**
	 * 跳到下一章，若返回值为false，则当前章节已经为最后一章
	 */
	public boolean nextChapter() {
		pagernum++ ;
		if (pagernum > 10)
		{
			pagernum = 10 ;
			return false;
		}
		openbook();
		//setFirstPage(c);
		//nextPage();
		return true;
	}
	
	// 读取上一段落
	protected byte[] readParagraphBack(int nFromPos) {
		int nEnd = nFromPos;
		int i;
		byte b0, b1;
		if (m_strCharsetName.equals("UTF-16LE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x0a && b1 == 0x00 && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}

		} else if (m_strCharsetName.equals("UTF-16BE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x00 && b1 == 0x0a && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}
		} else {
			i = nEnd - 1;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				if (b0 == 0x0a && i != nEnd - 1) {
					i++;
					break;
				}
				i--;
			}
		}
		if (i < 0)
			i = 0;
		int nParaSize = nEnd - i;
		int j;
		byte[] buf = new byte[nParaSize];
		for (j = 0; j < nParaSize; j++) {
			buf[j] = m_mbBuf.get(i + j);
		}
		return buf;
	}

	// 读取下一段落
	protected byte[] readParagraphForward(int nFromPos) {
		int nStart = nFromPos;
		int i = nStart;
		byte b0, b1;
		// 根据编码格式判断换行
		if (m_strCharsetName.equals("UTF-16LE")) {
			while (i < m_mbBufLen - 1) {
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x0a && b1 == 0x00) {
					break;
				}
			}
		} else if (m_strCharsetName.equals("UTF-16BE")) {
			while (i < m_mbBufLen - 1) {
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x00 && b1 == 0x0a) {
					break;
				}
			}
		} else {
			while (i < m_mbBufLen) {
				b0 = m_mbBuf.get(i++);
				if (b0 == 0x0a) {
					break;
				}
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {
			buf[i] = m_mbBuf.get(nFromPos + i);
		}
		return buf;
	}
	public void sellectBookPag(String str)
	{
		String strParagraph = "" ;
		int begin_pag = m_mbBufBegin ;
		while(m_mbBufEnd < m_mbBufLen)
		{
			byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
			m_mbBufEnd += paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(strParagraph.equals(str))
			{
				m_mbBufBegin = m_mbBufEnd;
				break ;
			}
		}
		if(m_mbBufEnd >= m_mbBufLen)
			m_mbBufBegin = begin_pag ;
		
		
	}

	protected Vector<String> pageDown() {
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while (lines.size() < mLineCount && m_mbBufEnd < m_mbBufLen) {
			byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
			m_mbBufEnd += paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1) {
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");//替换
			} else if (strParagraph.indexOf("\n") != -1) {
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				lines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
				lines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
				if (lines.size() >= mLineCount) {
					break;
				}
			}
			if (strParagraph.length() != 0) {
				try {
					m_mbBufEnd -= (strParagraph + strReturn)
							.getBytes(m_strCharsetName).length;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//m_mbBufBegin = m_mbBufEnd ;
		return lines;
	}

	protected void pageUp() {
		if (m_mbBufBegin < 0)
			m_mbBufBegin = 0;
		Vector<String> lines = new Vector<String>();
		String strParagraph = "";
		while (lines.size() < mLineCount && m_mbBufBegin > 0) {
			Vector<String> paraLines = new Vector<String>();
			byte[] paraBuf = readParagraphBack(m_mbBufBegin);
			m_mbBufBegin -= paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			strParagraph = strParagraph.replaceAll("\r\n", "");
			strParagraph = strParagraph.replaceAll("\n", "");

			if (strParagraph.length() == 0) {
				//paraLines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
				paraLines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
			}
			lines.addAll(0, paraLines);
		}
		while (lines.size() > mLineCount) {
			try {
				m_mbBufBegin += lines.get(0).getBytes(m_strCharsetName).length;
				lines.remove(0);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m_mbBufEnd = m_mbBufBegin;
		return;
	}

	
	/**
	 * 翻到上一页
	 */
/*	public boolean prePage() {
		if (isFirstPage()) {
			if (!preChapter()) // 如果已经到本书第一章，就不能继续执行翻页代码
				return false;
		}
		linesVe = pagesVe.get(--pageNum);
		return true;
	}

	
	public boolean nextPage() {
		if (isLastPage()) {
			if (!nextChapter()) // 如果已经到本书末尾，那么不能继续执行翻页代码
				return false;
		}
		linesVe = pagesVe.get(++pageNum);
		return true;
	}*/

	public void onDraw(Canvas c) {
		if (m_lines.size() == 0)
			m_lines = pageDown();
		if (m_lines.size() > 0) {
			if (m_book_bg == null)
				c.drawColor(m_backColor);
			else
				c.drawBitmap(m_book_bg, 0, 0, null);
			int y = marginHeight + youmiHeight;
//			int titleWidth = (int) titlePaint.measureText("娘子为夫饿了") + 1;
//			int titleHeight = y/2;
//			c.drawText("娘子为夫饿了", (mWidth-titleWidth)/2, titleHeight, titlePaint);
			int i = 0;
			for (String strLine : m_lines) {
				y += m_fontSize;
				//mPaint.setTypeface(Typeface.DEFAULT_BOLD);
				c.drawText(strLine, marginWidth, y, mPaint);
				y+=spaceSize;
				if(i!=m_lines.size()-1){
					c.drawText("", marginWidth, y, spactPaint);
				}
				i++;
			}
		}
		float fPercent = (float) (m_mbBufBegin * 1.0 / m_mbBufLen);
		DecimalFormat df = new DecimalFormat("#0.0");
		String strPercent = df.format(fPercent * 100) + "%";
		
		curProgress = (int)round1(fPercent * 100,0);
		int nPercentWidth = (int) bPaint.measureText("99.9%") + 1;
		c.drawText(strPercent, mWidth - nPercentWidth, mHeight-5, bPaint);
		//c.drawText("100", mWidth - nPercentWidth, mHeight-5, bPaint);
		
		//c.drawText("噬魂天书", mWidth/2, mHeight-5, mPaint);
		//int nTimeWidth = (int)mPaint.measureText("12:12") + 1;
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");      
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间      
		String str = formatter.format(curDate);  
		c.drawText(str, 5, mHeight-5, bPaint);
		int titleWidth = (int) bPaint.measureText("《"+str1[pagernum]+"》") + 1;
		c.drawText("《"+str1[pagernum]+"》", (mWidth-titleWidth)/2, mHeight-5, bPaint);
	}

	private static double round1(double v, int scale) {
		if (scale < 0)
		return v;
		String temp = "#####0.";
		for (int i = 0; i < scale; i++) {
		temp += "0";
		}
		return Double.valueOf(new java.text.DecimalFormat(temp).format(v));
		}

	public void setBgBitmap(Bitmap BG) {
		if (BG.getWidth() != mWidth || BG.getHeight() != mHeight)
			m_book_bg = Bitmap.createScaledBitmap(BG, mWidth, mHeight, true);
		else
			m_book_bg = BG;
	}
	
	public void setFirstBitmap(Bitmap BG) {
		if (BG.getWidth() != mWidth || BG.getHeight() != mHeight)
			first_book_bg = Bitmap.createScaledBitmap(BG, mWidth, mHeight, true);
		else
			first_book_bg = BG;
	}
	
	public void setFirstPage(Canvas c) {	
		if (m_book_bg == null)
			c.drawColor(m_backColor);
		else
			c.drawBitmap(first_book_bg, 0, 0, null);
		Bitmap BG = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.page_happy);
		if (BG.getWidth() != mWidth || BG.getHeight() != mHeight)
			pager_imag = Bitmap.createScaledBitmap(BG, (4*mWidth)/5, (3*mHeight)/7, true);
		String str = "『"+str1[pagernum]+"』" ;
		int h = mHeight/6 ;
		int w = mWidth/20 ;
		while(str.length()>0)
		{			
			int nSize = firstTitlePaint.breakText(str, true, (4*mVisibleWidth)/5,
					null);
			String s = str.substring(0, nSize);
			str = str.substring(nSize);
			c.drawText(s, w, h, firstTitlePaint);
			h = h + spaceSize+m_fontSize;
			w = w +30 ;
		}
		c.drawBitmap(pager_imag, mWidth/10, h+20, null);
		
		String str_content = str1[pagernum] ;
		h = h + (3*mHeight)/7 +spaceSize+m_fontSize+50;
		 w = mWidth/15 ;
		while(str_content.length()>0)
		{			
			int nSize = firstTitlePaint.breakText(str_content, true, (4*mVisibleWidth)/5,
					null);
			String s = str_content.substring(0, nSize);
			str_content = str_content.substring(nSize);
			c.drawText(s, w, h, firstContentPaint);
			h = h + spaceSize+m_fontSize;
		}
		//c.drawText("『"+str1[pagernum]+"』", mWidth/3, (4*mHeight)/5, mPaint);
		
	}
	 
	public boolean isfirstPage() {
		return m_isfirstPage;
	}

	public void setIslastPage(boolean islast){
		m_islastPage = islast;
	}
	public boolean islastPage() {
		return m_islastPage;
	} 
	public int getCurPostion() {
		if(m_mbBufEnd<0)
			return 0;
		return m_mbBufEnd;
	}
	
	public int getCurPostionBeg(){
		return m_mbBufBegin;
	}
	public void setBeginPos(int pos) {
		m_mbBufEnd = pos;
		m_mbBufBegin = pos;
	}
	
	public int getBufLen() {
		return m_mbBufLen;
	}
	
	public int getCurProgress(){
		return curProgress;
	}
	public String getOneLine() {
		return m_lines.toString().substring(0, 10);
	}
	
	public void changBackGround(int color) {
		mPaint.setColor(color);
	}
	
	public void setFontSize(int size) {
		m_fontSize = size;
		mPaint.setTextSize(size);
		int totalSize = m_fontSize+spaceSize;
		mLineCount = (int) (mVisibleHeight / totalSize); // 可显示的行数
	}
	
	public void setPagerNum(int pagernum){
		this.pagernum = pagernum; 
	}
	
}