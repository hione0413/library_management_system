package lms.book;

public class Test {
	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		sb.append("select b.book_id, g.genre, b.book_name, p.publisher, b.writer, b.book_same_amount, d.book_regist_date, s.book_state");
		sb.append(" from lib_book b, lib_genre g, lib_publisher p, lib_book_detail d, lib_book_state s");
		sb.append(" where b.book_name='���������'");
		sb.append(" and b.genre=g.genre_id"); //�з�
		sb.append(" and b.publisher=p.publisher_id"); //���ǻ�
		sb.append(" and b.book_id=d.book_id"); //���� �����
		sb.append(" and d.book_state=s.book_state_id"); //���� ����

		System.out.println(sb.toString());
	}
}
