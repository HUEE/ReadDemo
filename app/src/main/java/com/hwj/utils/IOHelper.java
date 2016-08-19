package com.hwj.utils;
//http://javaapk.com ��׿Դ�����ר���ṩ����

import android.content.Context;
import android.content.res.Resources;

import com.hwj.bean.Book;
import com.hwj.bean.Chapter;
import com.hwj.reader.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class IOHelper {
	private static Book book;
	// private static Chapter chapter;
	private static String[] chapterPaths;
	private static Resources res;

	/**
	 * ��ʼ��Book���Ψһ���� �������һ��ֻ�����һ�Ρ�
	 * 
	 * @param context
	 *            ���ڴ��ļ��ж�ȡ��Դ������Ҫͨ��Activity ���ṩ�������Activity���ô˺�����ʱ�򣬻ᴫ�� this��
	 * @return
	 */
	public static Book getBook(Context context) {
		book = Book.getInstance();

		res = context.getResources();
		String booklists[] = res.getStringArray(R.array.booklists);
		chapterPaths = res.getStringArray(R.array.content_path);

		// ����Book �������Ϣ
		book.setAuthor(res.getString(R.string.author));
		book.setBookname(res.getString(R.string.bookname));

		//�����@��if�������Ϊ���ָ�bug��д�ģ�����ʵ��Ӧ�ô��ڡ�
		//�²��ԭ�������������˳���ʱ��Book�����û�б����٣����ٴ����������ʱ��
		//�ָ��������һ���½���Ϣ
		if (book.getChapterList().size() != booklists.length)
			for (int i = 0; i < booklists.length; ++i)
				book.addChapter(booklists[i]);

		return book;
	}

	/**
	 * �õ��½����ݡ�
	 * 
	 * @param order
	 *            Ҫ��ȡ���½ڵ�˳��
	 * @param context
	 *            ͨ��context���õ� Resources ���󣬴Ӷ���ȡ��Դ��
	 * @return
	 */
	public static Chapter getChapter(int order) {
		// Resources res = context.getResources();
		if (order < 0 || order >= chapterPaths.length)
			return null;
		InputStream is;
		InputStreamReader isr;
		BufferedReader br;
		StringBuffer strBuffer = new StringBuffer();
		try {
			String path = chapterPaths[order];
			is = res.getAssets().open(path);
			
			isr = new InputStreamReader(is, "GBK");
			br = new BufferedReader(isr);

			String str;
			while ((str = br.readLine()) != null)
				strBuffer.append(str + '\n');
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Chapter chapter = new Chapter();
		chapter.setOrder(order);
		chapter.setTitle(book.getChapterName(order));
		chapter.setContent(strBuffer.toString());

		return chapter;
	}
}
