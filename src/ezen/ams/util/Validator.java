package ezen.ams.util;

import java.util.regex.Pattern;

/**
 * 입력데이터 유효성 검증 공통 기능 정의 유틸리티 + 정규표현식 사용
 * @author 윤동진
 */
public class Validator {

	/**
	 * 입력 데이터를 공백으로 뒀는 지 체크
	 * 
	 * @param input 입력데이터
	 * @return
	 */
	public static boolean hasText(String input) {
		if (input != null && input.trim().length() != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 입력데이터가 숫자인지 체크
	 * 
	 * @param number 입력문자열
	 * @return 유효여부
	 */


	public static boolean isNumber(String number) {
		return Pattern.matches("[0-9]+", number);

	}

	/**
	 * 입력데이터에 특수문자 존재여부 체크
	 * 
	 * @param id 사용자 id
	 * @return 유효여부 영문숫자조합으로 8~10자 아이디
	 */
	public static boolean validId(String id) {
		return id.matches("\\w{8,10}");
	}

	/** 이메일 유효 여부 */
	public static boolean validEmail(String email) {
		return email.matches("\\w+@\\w+\\.\\w+(\\.\\w+)?");
	}

//	 테스트용 main
	public static void main(String[] args) {
		String string = "공백이나 스페이스면 false";
		System.out.println(Validator.hasText(string));

		String number = "484984"; // 숫자 유효성검사
		boolean ok = Validator.isNumber(number);
		System.out.println(ok);
		ok = Validator.validId("bangry313"); // id유효성검사
		System.out.println(ok);
		boolean eok = Validator.validEmail("vilose7222gmail.com");
		System.out.println(eok);
	}

}
