package bean;


//http://javaapk.com ��׿Դ�����ר���ṩ����
import java.util.ArrayList;
import java.util.List;

/**
 * �������������Ϣ�����������ߣ��½����ơ�
 * ����������ڱ������н���Ҫʵ����һ�Σ���˽�����Ϊ������
 * ��������Ψһ�����������ʱ����ʼ�����ڹر����֮ǰһ���ǲ��ᷢ���ˡ�
 * @author MJZ
 *
 */
public class Book {
	private String bookname;
	private String author;
	private List<String> chapterList = new ArrayList<String>();
	
	private static Book book = new Book();
	
	private Book(){}
	
	public static Book getInstance(){
		return book;
	}

	public String getBookname() {
		return bookname;
	}

	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List getChapterList() {
		return chapterList;
	}

	public void setChapterList(List chapterList) {
		this.chapterList = chapterList;
	}
	
	public String getChapterName(int order){
		return (String) chapterList.get(order);
	}
	
	public void addChapter(String chapterName){
		chapterList.add(chapterName);
	}
}
