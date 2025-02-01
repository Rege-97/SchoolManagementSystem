package schoolManagement;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import oracle.net.jdbc.TNSAddress.Address;

public class School extends Frame implements ActionListener {

	MenuBar mbar;
	Menu m_file, m_add, m_print, m_search;
	MenuItem mi_close, mi_class, mi_student, mi_subject, mi_classrank, mi_allrank, mi_stusearch, mi_classsearch;
	Button popclose, bt_ok, bt_reset;
	Label lb_pop, lb_tnum, lb_tname, lb_sname, lb_sage, lb_saddr, lb_stel, lb_skor, lb_smat, lb_seng;
	TextField tf_tnum, tf_tname, tf_sname, tf_sage, tf_saddr, tf_stel, tf_skor, tf_smat, tf_seng;
	Dialog dialog;
	Font f_title;
	Panel p_empty;

	static String menu = "";

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

	static String[] str = { "석차", "학사번호", "학생이름", "나이", "주소", "전화번호", "", "반번호", "담임선생님", "국어점수", "수학점수", "영어점수",
			"평균" };

	public School() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					conn.close();
					System.exit(0);
				} catch (Exception e1) {
				}
			}
		});
		menu = "main";
		f_title = new Font("Default Font", Font.BOLD, 15);
		bt_ok = new Button("등록");
		bt_reset = new Button("초기화");

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
		m_search.add(mi_stusearch);
		m_search.add(mi_classsearch);

		Label front = new Label("학사 관리 프로그램 v3.0", Label.CENTER);
		front.setFont(f_title);
		this.add(front);

		mi_close.addActionListener(this);
		mi_class.addActionListener(this);
		mi_student.addActionListener(this);
		mi_subject.addActionListener(this);
		mi_classrank.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		try {
			if (ob == mi_close) {
				conn.close();
				System.exit(0);
			} else if (ob == mi_class) {
				classInput();
			} else if (ob == mi_student) {
				studentInput();
			} else if (ob == mi_subject) {
				subjectInput();
			} else if(ob==mi_classrank) {
				classRank();
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		this.validate();
	}

	public void classInput() throws Exception {
		this.removeAll();
		menu = "classInput";
		this.setLayout(new BorderLayout());

		Panel p1 = new Panel(new FlowLayout());
		this.add(p1, "North");
		Label lb_title_ci = new Label("반 등록", Label.CENTER);
		lb_title_ci.setPreferredSize(new Dimension(100, 50));
		lb_title_ci.setFont(f_title);
		p1.add(lb_title_ci);

		Panel p2 = new Panel(new GridLayout(5, 2, 10, 10));
		this.add(p2);

		lb_tnum = new Label("배정될 반", Label.CENTER);
		tf_tnum = new TextField();
		tf_tnum.setEditable(false);
		lb_tname = new Label("담임선생님 이름", Label.CENTER);
		tf_tname = new TextField();

		for (int i = 0; i < 2; i++) {
			p_empty = new Panel();
			p2.add(p_empty);
		}

		p2.add(lb_tnum);
		p2.add(tf_tnum);
		p2.add(lb_tname);
		p2.add(tf_tname);

		for (int i = 0; i < 2; i++) {
			p_empty = new Panel();
			p2.add(p_empty);
		}

		p2.add(bt_ok);
		p2.add(bt_reset);

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

		if (menu.equals("classInput")) {
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
		}

		ps.close();
		rs.close();

	}

	public void studentInput() throws Exception {
		this.removeAll();
		menu = "studentInput";

		this.setLayout(new BorderLayout());

		Panel p1 = new Panel(new FlowLayout());
		this.add(p1, "North");
		Label lb_title_si = new Label("학생 등록", Label.CENTER);
		lb_title_si.setPreferredSize(new Dimension(100, 50));
		lb_title_si.setFont(f_title);
		p1.add(lb_title_si);

		Panel p2 = new Panel(new GridLayout(6, 2, 10, 10));
		this.add(p2);

		lb_sname = new Label("학생 이름", Label.CENTER);
		tf_sname = new TextField();
		lb_sage = new Label("학생 나이", Label.CENTER);
		tf_sage = new TextField();
		lb_saddr = new Label("학생 주소", Label.CENTER);
		tf_saddr = new TextField();
		lb_stel = new Label("학생 전화번호", Label.CENTER);
		tf_stel = new TextField();
		lb_tnum = new Label("배정 반", Label.CENTER);
		Choice c_tnum = new Choice();

		sql = "select tnum from teacher order by tnum";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			c_tnum.add(rs.getString("tnum"));
		}

		p2.add(lb_sname);
		p2.add(tf_sname);
		p2.add(lb_sage);
		p2.add(tf_sage);
		p2.add(lb_saddr);
		p2.add(tf_saddr);
		p2.add(lb_stel);
		p2.add(tf_stel);
		p2.add(lb_tnum);
		p2.add(c_tnum);
		p2.add(bt_ok);
		p2.add(bt_reset);

		this.validate();

		if (menu.equals("studentInput")) {
			bt_ok.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					sname = tf_sname.getText();
					sage = Integer.parseInt(tf_sage.getText());
					saddr = tf_saddr.getText();
					stel = tf_stel.getText();
					tnum = c_tnum.getSelectedItem();
					try {
						sql = "insert into student values(sq_student_snum.nextval,?,?,?,?,?)";
						PreparedStatement ps1 = conn.prepareStatement(sql);

						ps1.setString(1, sname);
						ps1.setInt(2, sage);
						ps1.setString(3, saddr);
						ps1.setString(4, stel);
						ps1.setString(5, tnum);

						count = ps1.executeUpdate();

						sql = "select * from student " + "where sname=? " + "order by snum";

						PreparedStatement ps2 = conn.prepareStatement(sql);
						ps2.setString(1, sname);

						ResultSet rs = ps2.executeQuery();
						while (rs.next()) {
							snum = rs.getInt("snum");
						}
						sql = "insert into subject(snum) values(?)";
						PreparedStatement ps3 = conn.prepareStatement(sql);
						ps3.setInt(1, snum);

						ps3.executeUpdate();

						popUp();

						ps1.close();
						rs.close();
						ps2.close();
						ps3.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});

			bt_reset.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					tf_sname.setText("");
					tf_sage.setText("");
					tf_saddr.setText("");
					tf_stel.setText("");
					tf_tnum.setText("");

				}
			});
		}

	}

	public void subjectInput() throws Exception {
		this.removeAll();
		menu = "subjectInput";

		this.setLayout(new BorderLayout());

		Panel p1 = new Panel(new FlowLayout());
		this.add(p1, "North");
		Label lb_title_sui = new Label("과목 점수 입력", Label.CENTER);
		lb_title_sui.setPreferredSize(new Dimension(100, 50));
		lb_title_sui.setFont(f_title);
		p1.add(lb_title_sui);

		Panel p2 = new Panel();

		List list = new List();
		sql = "select * " + "from student st,subject sb " + "where st.snum = sb.snum and sb.savg=0 "
				+ "order by st.snum";

		PreparedStatement ps1 = conn.prepareStatement(sql);
		ResultSet rs = ps1.executeQuery();

		Choice c_snum = new Choice();
		TextArea ta = new TextArea();

		ta.setPreferredSize(new Dimension(1145, 130));

		while (rs.next()) {
			snum = rs.getInt("snum");
			sname = rs.getString("sname");
			sage = rs.getInt("sage");
			saddr = rs.getString("saddr");
			stel = rs.getString("stel");
			tnum = rs.getString("tnum");
			ta.append("  " + snum + "\t\t" + sname + "\t\t" + sage + "\t\t" + saddr + "\t\t" + stel + "\t\t\t" + tnum
					+ "\n");
			c_snum.add(snum + "번 " + sname);
		}
		Panel p2_1 = new Panel(new GridLayout(1, 12));
		p2_1.setPreferredSize(new Dimension(1145, 30));
		for (int i = 1; i < 13; i++) {
			if (i <= 7) {
				Label lb = new Label(str[i]);
				p2_1.add(lb);
			} else {
				Label lb = new Label("");
				p2_1.add(lb);

			}
		}

		p2.add(p2_1);
		p2.add(ta);

		this.add(p2, "Center");

		Panel p3 = new Panel(new GridLayout(5, 2, 10, 10));
		this.add(p3, "South");
		Label lb_s = new Label("학생 선택", Label.CENTER);
		lb_skor = new Label("국어 점수", Label.CENTER);
		tf_skor = new TextField();
		lb_smat = new Label("수학 점수", Label.CENTER);
		tf_smat = new TextField();
		lb_seng = new Label("영어 점수", Label.CENTER);
		tf_seng = new TextField();

		p3.add(lb_s);
		p3.add(c_snum);
		p3.add(lb_skor);
		p3.add(tf_skor);
		p3.add(lb_smat);
		p3.add(tf_smat);
		p3.add(lb_seng);
		p3.add(tf_seng);

		p3.add(bt_ok);
		p3.add(bt_reset);

		this.validate();
		ps1.close();
		rs.close();

		if (menu.equals("subjectInput")) {
			bt_ok.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						sql = "update subject set skor=?,smat=?,seng=?, savg=? where snum= ?";
						PreparedStatement ps = conn.prepareStatement(sql);
						snum = Integer.parseInt(c_snum.getSelectedItem().substring(0, 1));
						skor = Integer.parseInt(tf_skor.getText());
						smat = Integer.parseInt(tf_smat.getText());
						seng = Integer.parseInt(tf_seng.getText());
						savg = (skor + smat + seng) / 3;

						ps.setInt(1, skor);
						ps.setInt(2, smat);
						ps.setInt(3, seng);
						ps.setInt(4, savg);
						ps.setInt(5, snum);

						count = ps.executeUpdate();

						popUp();
						subjectInput();
						ps.close();

					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
			});
			
			bt_reset.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					tf_skor.setText("");
					tf_smat.setText("");
					tf_seng.setText("");
				}
			});

		}

	}
	
	public void classRank() {
		this.removeAll();
		menu = "subjectInput";

		this.setLayout(new BorderLayout());

		Panel p1 = new Panel(new FlowLayout());
		this.add(p1, "North");
		Label lb_title_cr = new Label("반별 석차 출력", Label.CENTER);
		lb_title_cr.setPreferredSize(new Dimension(100, 50));
		lb_title_cr.setFont(f_title);
		p1.add(lb_title_cr);
		
		
		
		
		
		
		
		
		
		
		
		
		this.validate();
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

	@Override
	public Insets insets() {
		Insets i = new Insets(45, 20, 20, 20);
		return i;
	}

	public static void main(String[] args) throws Exception {

		Class.forName("oracle.jdbc.driver.OracleDriver");

		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "scott";
		String pwd = "1234";

		conn = DriverManager.getConnection(url, user, pwd);

		School s = new School();
		s.setSize(1200, 400);
		s.setVisible(true);

	}

}
