package ezen.ams.domain;

import java.io.Serializable;

import ezen.ams.exception.NotBalanceException;

/*
 * 은행 계좌 추상화
 */
public class Account /* extends Object */ implements Serializable {
	// 필드 캡슐화(은닉화)
//	인스턴스변수들(필드)
	private String accountNum; // 보통 접근제한자 private로 지정 해 주는 것이 좋다 (캡슐화)
	private String accountOwner;
	private int passwd;
	private long restMoney;

//	스태틱변수(정적,클래스 변수)들...
// final로 상수 만들기( 상수는 생성 시 초기화 필수)	
	public static final String BANK_NAME = "이젠은행";
//	private static int accountId = 1000; 
	private static int accountId;

//	초기화 블록
//	스태틱초기화블록
	static {
		// System.out.println("초기화 블록 실행됨...");
		// 주로 애플리케이션 환경 설정파일의 내용을 읽어오는 코드...
		accountId = 1000;
	}

//	public static String bankName; = "이젠은행";
	// 생성자는 인스턴스변수를 적는거라 거긴 안됨 퍼블릭이라 세터게터도 안넣음
	// static은 클래스이름으로 바로 접근 가능

	public Account() {
	}

	public Account(String accountOwner, int passwd, long restMoney) {
		// System.out.println("생성자 실행됨");
		this.accountNum = (accountId++) + "";
		this.accountOwner = accountOwner;
		this.passwd = passwd;
		this.restMoney = restMoney;
	}

	// 캡슐화 로 인한 메소드 생성 필요 (자동완성 source-setter/getter)

	// setter/getter 메소드

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public int getPasswd() {
		return passwd;
	}

	public void setPasswd(int passwd) {
		this.passwd = passwd;
	}

	public void setRestMoney(long restMoney) {
		this.restMoney = restMoney;
	}

	/*
	 * 입금 기능 메소드
	 */
	public long deposit(long money) throws NotBalanceException {
		// 데이터검증 및 예외처리
		if (money <= 0) {
			throw new NotBalanceException("입금 금액은 0이거나 음수 일 수 없습니다.");
		} else if (money >= 100000000) {
			throw new NotBalanceException("1억원 이상 입금 할 수 없습니다.");

		}
		return restMoney += money;
	}

	/*
	 * 출금 기능 메소드
	 */
	public long withdraw(long money) throws NotBalanceException {
		// 데이터검증 및 예외처리
		if (money <= 0) {
			throw new NotBalanceException("출금 금액은 0이거나 음수 일 수 없습니다.");
		} else if (money >= 100000000) {
			throw new NotBalanceException("1억원 이상 출금 할 수 없습니다.");
		} else if (money > restMoney) {
			throw new NotBalanceException("잔액이 부족하여 출금 할 수 없습니다.");
		}
		return restMoney -= money;

	}

	/*
	 * 잔액조회 메소드
	 * 
	 */
	public long getRestMoney() {
		return restMoney;
	}

	/*
	 * 비밀번호확인
	 */
	public boolean checkPasswd(int pwd) {
		return passwd == pwd;

	}

	// toString 오버라이딩
	@Override
	public String toString() {
		return "\t" + accountNum + "\t" + accountOwner + "\t" + "  ****" + "\t" + "  " + restMoney;
	}

	// equals 오버라이딩
	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());

	}

	// 스태틱(클래스)메소드
	public static int getAccountId() {
		return accountId;
	}
}
