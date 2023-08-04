package ezen.ams.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * RDB를 통해 은행계좌 목록 저장 및 관리(검색, 수정, 삭제) 구현체
 * 
 * @author 윤동진
 * @since 1.0
 * 
 */
public class JDBCAccountRepository implements AccountRepository {

	// 나중에 properties 파일로 변경 할 것임..
	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private static String userid = "hr";
	private static String password = "hr";

	private Connection con; // Connection은 나중에 메소드 단위로 들어간다..(현재는 2티어단위이고 미들웨어 개념에서는 맞지않음)

	public JDBCAccountRepository() throws Exception {
		Class.forName(driver);
		con = DriverManager.getConnection(url, userid, password);
	}

	/**
	 * 전체 계좌 목록 수 반환
	 * 
	 * @return 목록 수
	 */
	public int getCount() {
		int count = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT count(*) cnt").append(" FROM account");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("cnt");
			}
		} catch (Exception e) {
			// 컴파일예외를 런타임 예외로 변환해서 던져준다. throws를 하면 오버라이딩 규칙에 어긋나서
			throw new RuntimeException(e.getMessage());
//			throw new RuntimeException("계좌 등록 시 아래와 같은 에러가 발생하였습니다.")
		} finally {
			try {
				if (con != null)
					con.close();
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
		}
		return count;
	}

	/**
	 * 
	 * @return 전체 계좌 목록
	 */
	public List<Account> getAccounts() {
		List<Account> list = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT account_type, account_num, account_owner,  passwd, rest_money, borrow_money")
				.append(" FROM account");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, password);
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			list = new ArrayList<Account>();
			while (rs.next()) {
				String accountType = rs.getString("account_type");
				String accountNum = rs.getString("account_num");
				String accountOwner = rs.getString("account_owner");
				int passwd = rs.getInt("passwd");
				int restMoney = rs.getInt("rest_money");
				int borrowMoney = rs.getInt("borrow_money");

				Account account = new Account();
				account.setAccountNum(accountNum);
				account.setAccountOwner(accountOwner);
				account.setPasswd(passwd);
				account.setRestMoney(restMoney);

				if (account instanceof MinusAccount) {
					((MinusAccount) account).setBorrowMoney(borrowMoney);
					accountType = "ACCOUNT";
				} else {
					accountType = "MINUS_ACCOUNT";
				}

				if (borrowMoney != 0) {
					MinusAccount ma = new MinusAccount();
					ma.setAccountNum(accountNum);
					ma.setAccountOwner(accountOwner);
					ma.setPasswd(passwd);
					ma.setRestMoney(restMoney);
					ma.setBorrowMoney(borrowMoney);
					list.add(ma);
				} else {
					account = new Account();
					account.setAccountNum(accountNum);
					account.setAccountOwner(accountOwner);
					account.setPasswd(passwd);
					account.setRestMoney(restMoney);
					list.add(account);
				}
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	/**
	 * 신규계좌 등록
	 * 
	 * @param account 신규계좌
	 * @return 등록 여부
	 */
	public boolean addAccount(Account account) {
		boolean ok = false;
		StringBuilder sb = new StringBuilder();
		sb.append(" INSERT INTO account(account_type, account_num, account_owner,  passwd, rest_money, borrow_money )")
				.append(" VALUES (?, account_num_seq.NEXTVAL, ?, ?, ?, ?)");
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			// Class.forName(driver);
			con = DriverManager.getConnection(url, userid, password);
			pstmt = con.prepareStatement(sb.toString()); // execute도 준비 끝남.
			String accountType;
			if (account instanceof MinusAccount) {
				MinusAccount ma = (MinusAccount) account;
				accountType = "MINUS_ACCOUNT";
				pstmt.setString(1, accountType);
				pstmt.setLong(5, ma.getBorrowMoney());
			} else {
				accountType = "Account";
				pstmt.setLong(5, 0);
			}
			pstmt.setString(1, accountType);
			pstmt.setString(2, account.getAccountOwner());
			pstmt.setInt(3, account.getPasswd());
			pstmt.setLong(4, account.getRestMoney());
			pstmt.executeUpdate();
			ok = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ok;
	}

	// String accountType = "ACCOUNT";
	// if (account instanceof MinusAccount) {
	// accountType = "MINUS_ACCOUNT";
	// }

	/**
	 * 
	 * @param accountNum 검색 계좌번호
	 * @return 검색 된 계좌
	 */
	public Account searchAccount(String accountNum) {
		Account account = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT account_type, account_num, account_owner,  passwd, rest_money, borrow_money")
				.append(" FROM account").append(" WHERE account_num = ? ");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, password);
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, accountNum);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String accountType = rs.getString("account_type");
				accountNum = rs.getString("account_num");
				String accountOwner = rs.getString("account_owner");
				int passwd = rs.getInt("passwd");
				int restMoney = rs.getInt("rest_money");
				int borrowMoney = rs.getInt("borrow_money");
				if (borrowMoney != 0) {
					MinusAccount ma = new MinusAccount();
					ma.setAccountNum(accountNum);
					ma.setAccountOwner(accountOwner);
					ma.setPasswd(passwd);
					ma.setRestMoney(restMoney);
					ma.setBorrowMoney(borrowMoney);
					account = ma;
				} else {
					account = new Account();
					account.setAccountNum(accountNum);
					account.setAccountOwner(accountOwner);
					account.setPasswd(passwd);
					account.setRestMoney(restMoney);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
		return account;
	}

	/**
	 * 
	 * @param accountOwner 검색 예금주명
	 * @return 검색 된 계좌목록(배열)
	 */

	public List<Account> searchAccountByOwner(String accountOwner) {
		List<Account> list = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT account_type, account_num, account_owner,  passwd, rest_money, borrow_money")
				.append(" FROM account").append(" WHERE account_owner = ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, password);
			pstmt = con.prepareStatement(sb.toString());

			pstmt.setString(1, accountOwner);
			rs = pstmt.executeQuery();
			list = new ArrayList<Account>();

			while (rs.next()) {
				String accountType = rs.getString("account_type");
				String accountNum = rs.getString("account_num");
				String accOwner = rs.getString("account_owner");
				int passwd = rs.getInt("passwd");
				int restMoney = rs.getInt("rest_money");
				int borrowMoney = rs.getInt("borrow_money");

				if (borrowMoney != 0) {
					MinusAccount ma = new MinusAccount();
					ma.setAccountNum(accountNum);
					ma.setAccountOwner(accOwner);
					ma.setPasswd(passwd);
					ma.setRestMoney(restMoney);
					ma.setBorrowMoney(borrowMoney);
					list.add(ma);
				} else {
					Account account = new Account();
					account.setAccountNum(accountNum);
					account.setAccountOwner(accOwner);
					account.setPasswd(passwd);
					account.setRestMoney(restMoney);
					list.add(account);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
		return list;
	}

	public boolean removeAccount(String accountNum) {
		boolean ok = false;
		StringBuilder sb = new StringBuilder();
		sb.append(" DELETE FROM account").append(" WHERE account_num = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DriverManager.getConnection(url, userid, password);
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, accountNum);
			pstmt.executeUpdate();
			ok = true;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (con != null)
					con.close();
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return ok;
	}

	// Connection 닫아주는 메소드
	public void close() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {

		AccountRepository accountRepository = new JDBCAccountRepository();
//	 int count = accountRepository.getCount();
//	 System.out.println(count);

//		List<Account> allList = accountRepository.getAccounts();
//		for (Account account : allList) {
//			System.out.println(account);
//		}
		Account account = new Account();
		account = accountRepository.searchAccount("1002");
		System.out.println(account);

	}

}
