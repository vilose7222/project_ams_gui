package ezen.ams.exception;

/**
 * 사용자가 정의하는 예외 클래스
 * 
 * @author 윤동진 상속을 응용
 */
//NotBalanceException= 대표적인 compile checked exception
public class NotBalanceException extends Exception {
//	String message;  //기존에 익셉션은 스트링 메세지 를 보유함
	private int errorCode;

	public NotBalanceException() {
		super("예기 치 않은 오류가 발생하였습니다. 관리자에게 문의하세요..");
	}
	public NotBalanceException(String message) {
		super(message);
	}
	
	public NotBalanceException(String message, int errorCode) {
		super(message); // 부모를 호출하기위해서
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return getMessage() + " [에러코드 : "+errorCode+"]";
	}

}
