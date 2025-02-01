package schoolManagement;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class School extends Frame implements ActionListener {

	MenuBar mbar;
	Menu m_file, m_add, m_print, m_search;
	MenuItem mi_close, mi_class, mi_student, mi_subject, mi_classrank, mi_allrank, mi_stusearch, mi_classsearch;
	Button popclose, bt_ok, bt_reset;
	Label lb_pop, lb_tnum, lb_tname;
	TextField tf_tnum, tf_tname;
	Dialog dialog;

	static String sql = "";
	static int count = 0;

	static String tnum = "A Class";
	static String tname = "";

	static int snum = 0;
	static String sname = "";
	static int sage = 0;
	static String saddr = "";
	static String stel = "";

	static int skor = 0;
	static int smat = 0;
	static int seng = 0;
	static int savg = 0;
	static Connection conn;

	public School() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		mbar = new MenuBar();
		this.setMenuBar(mbar);

		m_file = new Menu("파일");
		m_add = new Menu("등록");
		m_print = new Menu("출력");
		m_search = new Menu("검색");
		mbar.add(m_file);
		mbar.add(m_add);
		mbar.add(m_print);
		mbar.add(m_search);

		mi_close = new MenuItem("프로그램 종료");
		m_file.add(mi_close);

		mi_class = new MenuItem("반 등록");
		mi_student = new MenuItem("학생 등록");
		mi_subject = new MenuItem("과목 입력");
		m_add.add(mi_class);
		m_add.add(mi_student);
		m_add.add(mi_subject);

		mi_classrank = new MenuItem("반별 석차");
		mi_allrank = new MenuItem("전체 석차");
		m_print.add(mi_classrank);
		m_print.add(mi_allrank);

		mi_stusearch = new MenuItem("학생 검색");
		mi_classsearch = new MenuItem("반 검색");

		Label front = new Label("학사 관리 프로그램 v3.0", Label.CENTER);
		this.add(front);

		mi_close.addActionListener(this);
		mi_class.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		try {
			if (ob == mi_close) {
				System.exit(0);
			} else if (ob == mi_class) {
				classInput();
			}
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		this.validate();

	}

	public void classInput() throws Exception {
		this.removeAll();

		this.setLayout(new GridLayout(3, 2, 10, 10));

		lb_tnum = new Label("배정될 반");
		tf_tnum = new TextField();
		tf_tnum.setEditable(false);
		lb_tname = new Label("담임선생님 이름");
		tf_tname = new TextField();

		this.add(lb_tnum);
		this.add(tf_tnum);
		this.add(lb_tname);
		this.add(tf_tname);

		bt_ok = new Button("등록");
		bt_reset = new Button("초기화");

		this.add(bt_ok);
		this.add(bt_reset);

		sql = "select * from teacher order by tnum";

		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			if (rs.getString("tnum").charAt(0) == tnum.charAt(0)) {
				tnum = ((char) (tnum.charAt(0) + 1)) + " Class";
			}
		}

		tf_tnum.setText(tnum);
		this.validate();

		bt_ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sql = "insert into teacher values(?,?)";
					PreparedStatement ps = conn.prepareStatement(sql);
					tname = tf_tname.getText();
					ps.setString(1, tnum);
					ps.setString(2, tname);

					count = ps.executeUpdate();

					popUp();

					ps.close();
					classInput();

				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		bt_reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tf_tname.setText("");
			}
		});

		ps.close();
		rs.close();

	}

	public void popUp() {
		dialog = new Dialog(this, "처리 완료");
		dialog.setSize(300, 100);
		dialog.setLayout(new BorderLayout());
		dialog.setLocationRelativeTo(this);

		lb_pop = new Label(count + "건이 처리되었습니다.", Label.CENTER);
		popclose = new Button("확인");
		dialog.add(lb_pop, "Center");
		dialog.add(popclose, "South");
		dialog.setVisible(true);
		popclose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		});
	}

	public static void main(String[] args) throws Exception {

		Class.forName("oracle.jdbc.driver.OracleDriver");

		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "scott";
		String pwd = "1234";

		conn = DriverManager.getConnection(url, user, pwd);

		School s = new School();
		s.setSize(500, 300);
		s.setVisible(true);

	}

}
