package ezen.ams.view;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import ezen.ams.domain.Account;
import ezen.ams.domain.AccountRepository;
import ezen.ams.domain.AccountType;
import ezen.ams.domain.JDBCAccountRepository;
import ezen.ams.domain.MinusAccount;

@SuppressWarnings("serial")
public class AmsFrame extends Frame {
	Button checkB, deleteB, searchB, newRegB, allCheckB;
	Label acctypeL, accNumL, AccOwnerL, pwL, loanL, AccListL, depositL, wonL, noticeL;
	TextField accNumTF, AccOwnerTF, pwTF, loanTF, depositTF;
	TextArea bottomTA;
	Choice choice;

	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints con = new GridBagConstraints();
	
	private AccountRepository repository;

	public AmsFrame() {
		this("무제");
	}

	public AmsFrame(String title) {
		super(title);
		
		try {
			repository = new JDBCAccountRepository();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}
		
//		label 8개
		acctypeL = new Label("계좌종류");
		accNumL = new Label("계좌번호");
		AccOwnerL = new Label("예금주명");
		pwL = new Label("비밀번호");
		loanL = new Label("대출금액");
		AccListL = new Label("계좌목록");
		depositL = new Label("입금금액");
		noticeL = new Label("신규등록 시 계좌번호는 자동 생성됩니다.");
		wonL = new Label("(단위 : 원)");
//		choice 3항목
//		choice.add("전체");
//		choice.add("입출금계좌");
//		choice.add("마이너스계좌");
		choice = new Choice();
		AccountType[] accountTypes = AccountType.values();
		for (AccountType accountType : accountTypes) {
			choice.add(accountType.getName());
		}

//		button 5개
		checkB = new Button("조회");
		deleteB = new Button("삭제");
		searchB = new Button("검색");
		newRegB = new Button("신규등록");
		allCheckB = new Button("전체조회");
//		ta 1개 및 tf 5개
		bottomTA = new TextArea();
		accNumTF = new TextField(20);
		AccOwnerTF = new TextField(20);
		pwTF = new TextField(20);
		loanTF = new TextField(20);
		depositTF = new TextField(8);
	}

	/**
	 * 컴포넌트 add 메소드
	 * 
	 * @param component  = 삽입 할 컴포넌트
	 * @param gridx      = 컴포넌트 x 좌표
	 * @param gridy      = 컴포넌트 y 좌표
	 * @param gridwidth  = 격자 가로값
	 * @param gridheight = 격자 세로값
	 */
	public void addCon(Component component, int gridx, int gridy, int gridwidth, int gridheight) {
		con.insets = new Insets(10, 10, 10, 10);
//		con.fill = GridBagConstraints.BOTH;
		con.gridx = gridx;
		con.gridy = gridy;
		con.gridwidth = gridwidth;
		con.gridheight = gridheight;
		con.weighty = 0.0;
		con.weightx = 0.0;
		gridbag.setConstraints(component, con);
		add(component);
		con.anchor = GridBagConstraints.CENTER;
	}

	public void init() {
		setLayout(gridbag);

		addCon(acctypeL, 0, 0, 1, 1);
		addCon(choice, 1, 0, 1, 1);

		addCon(noticeL, 1, 1, 1, 1);

		addCon(accNumL, 0, 2, 1, 1);
		addCon(accNumTF, 1, 2, 1, 1);
		addCon(checkB, 2, 2, 1, 1);
		addCon(deleteB, 3, 2, 1, 1);

		addCon(AccOwnerL, 0, 3, 1, 1);
		addCon(AccOwnerTF, 1, 3, 1, 1);
		addCon(searchB, 2, 3, 1, 1);

		addCon(pwL, 0, 4, 1, 1);
		addCon(pwTF, 1, 4, 1, 1);
		addCon(depositL, 2, 4, 1, 1);
		addCon(depositTF, 3, 4, 3, 1);

		addCon(loanL, 0, 5, 1, 1);
		addCon(loanTF, 1, 5, 1, 1);
		addCon(newRegB, 2, 5, 1, 1);
		addCon(allCheckB, 3, 5, 1, 1);

		addCon(AccListL, 0, 6, 1, 1);
		addCon(wonL, 3, 6, 1, 1);

		addCon(bottomTA, 0, 7, 5, 2);
	}

	// 시작하면 나오는 헤더 메소드
	private void printHeader() {
		String type = "계좌타입";
		String num = "계좌번호";
		String owner = "예금주";
		String pw = "비밀번호";
		String aa = "잔액";
		String bb = "대출금";

		
		      bottomTA.append("-----------------------------------------------------------------------\n");
		       bottomTA.append("계좌종류         계좌번호   예금주   비밀번호    잔액            대출금액\n");
		       bottomTA.append("=======================================================================\n");
		   }

	// 시작하면 TA에 나오는 메소드
	public void allList() {
		bottomTA.setText("");
		printHeader();
		List<Account> list = repository.getAccounts();
		for (Account account : list) {
			if (account instanceof MinusAccount) {
				bottomTA.append("마이너스 계좌" + account.toString() + "\n");
			} else {
				bottomTA.append("입출금 계좌" +  account.toString() + "\n");
			}
		}

	}

	public void selectChoice(AccountType accountType) {
		switch (accountType) {
		case GENERAL_ACCOUNT:
			loanTF.setEnabled(false);
		case MINUS_ACCOUNT:	
			loanTF.setEnabled(true);
			break;

		}
	}

// 계좌번호 텍스트필드 빼고 모두 블락
	private void remove1(TextField tf) {
		AccOwnerTF.setEnabled(false);
		pwTF.setEnabled(false);
		loanTF.setEnabled(false);
		depositTF.setEnabled(false);
		accNumTF.setEnabled(true);
	}

//예금주만 빼고 모두 블락
	private void remove2(TextField tf) {
		AccOwnerTF.setEnabled(true);
		pwTF.setEnabled(false);
		loanTF.setEnabled(false);
		depositTF.setEnabled(false);
		accNumTF.setEnabled(false);
	}

//모든 텍스트필드 활성화
	private void finish() {
		AccOwnerTF.setEnabled(true);
		pwTF.setEnabled(true);
		loanTF.setEnabled(true);
		depositTF.setEnabled(true);
		accNumTF.setEnabled(true);
	}

//	검색 버튼에서 중복방지 
	private boolean isDuplicated(List<Account> accounts) {
		Set<String> accountOwners = new HashSet<String>();
		for (Account account : accounts) {
			String accountOwner = account.getAccountOwner();
			if (!accountOwners.add(accountOwner)) {
				return true;
			}
		}
		return false;
	}

	// 텍스트필드 가변인자 한번입력으로 초기화
	private void allClearTF(TextField... textFields) {
		for (TextField textField : textFields) {
			textField.setText("");
		}
	}

	// 텍스트필드 초기화
	private void clearTF(TextField textField) {
		textField.setText("");
	}

	// 조회 메소드
	public void searchAcc() {
		String accountNumber = accNumTF.getText();
		Account account = repository.searchAccount(accountNumber);
		if (accountNumber.isEmpty()) {
			JOptionPane.showMessageDialog(this, "조회,삭제는 계좌번호로 인식됩니다.");
			remove1(accNumTF);
			clearTF(accNumTF);
		} else if (account instanceof MinusAccount) {
			MinusAccount mAccount = (MinusAccount) account;
			bottomTA.setText("");
			printHeader();
			bottomTA.append("마이너스계좌" + mAccount.toString());
			JOptionPane.showMessageDialog(this, "고객님의 계좌는 마이너스 계좌입니다.");
			finish();
			clearTF(accNumTF);
		} else if (account instanceof Account) {
			bottomTA.setText("");
			printHeader();
			bottomTA.append("입출금계좌" + account.toString() + "\t");
			JOptionPane.showMessageDialog(this, "고객님의 계좌는 입출금 계좌입니다.");
			finish();
			clearTF(accNumTF);
		} else {
			JOptionPane.showMessageDialog(this, "계좌번호와 일치하는 계좌가 없습니다.");
			remove1(accNumTF);
			clearTF(accNumTF);
		}

	}

	// 이름 검색 메소드
	public void nameSearch() {
		String accountOwner = AccOwnerTF.getText();
		List<Account> accounts = repository.searchAccountByOwner(accountOwner);
		Account acc = new Account();
		boolean duplicated = isDuplicated(accounts); // boolean값으로 이름 중복 확인

		if (duplicated) {
			JOptionPane.showMessageDialog(this, "예금주 명이 중복됩니다. 계좌번호 입력 후 조회 버튼을 이용하세요 ");
			remove1(accNumTF);
			clearTF(AccOwnerTF);
			return;
		}
		if (accountOwner.isEmpty()) {
			JOptionPane.showMessageDialog(this, "검색은 예금주 명으로 인식됩니다. ");
			remove2(AccOwnerTF);
			clearTF(AccOwnerTF);
		} else if (!accounts.isEmpty()) {
			bottomTA.setText("");
			printHeader();
			for (Account account : accounts) {
				String accountString = account.toString();
				if (accountString.contains("borrowMoney")) {
					MinusAccount mAccount = (MinusAccount) account;
					bottomTA.append("마이너스계좌" + mAccount.toString() + "\t");
					JOptionPane.showMessageDialog(this, "고객님의 계좌는 마이너스 계좌입니다.");
				} else {
					bottomTA.append("입출금계좌" + account.toString() + "\t");
					JOptionPane.showMessageDialog(this, "고객님의 계좌는 입출금 계좌입니다.");
				}
			}
			finish();
			clearTF(AccOwnerTF);
		} else {
			JOptionPane.showMessageDialog(this, "예금주명과 일치하는 계좌가 없습니다.");
			remove2(AccOwnerTF);
			clearTF(AccOwnerTF);
		}
	}

//계좌 삭제 메소드
	public void removeAcc() {
		String accountNumber = accNumTF.getText();
		boolean isRemoved = repository.removeAccount(accountNumber);
		if (accountNumber.isEmpty()) {
			JOptionPane.showMessageDialog(this, "조회,삭제는 계좌번호만 인식합니다");
			remove1(accNumTF);
			clearTF(accNumTF);

//		boolean isRemoved = AMS.repository.removeAccount(accountNumber);
		} else if (isRemoved) {
			allList();
			JOptionPane.showMessageDialog(this, "계좌가 삭제되었습니다.");
			allClearTF(accNumTF, AccOwnerTF, pwTF, loanTF, depositTF);
		} else {
			JOptionPane.showMessageDialog(this, "일치하는 계좌가 없습니다.");
			remove1(accNumTF);
			clearTF(accNumTF);
		}
	}

//신규등록
	public void addAccount() {
		Account account = null;

		String accountOwner = AccOwnerTF.getText();
		int password = Integer.parseInt(pwTF.getText());
		long inputMoney = Long.parseLong(depositTF.getText());

		String selectedItem = choice.getSelectedItem();
		// 입출금 계좌 등록
		if (selectedItem.equals(AccountType.GENERAL_ACCOUNT.getName())) {
			account = new Account(accountOwner, password, inputMoney);
		} else if (selectedItem.equals(AccountType.MINUS_ACCOUNT.getName())) {
			long loanMoney = Long.parseLong(loanTF.getText());
			account = new MinusAccount(accountOwner, password, inputMoney, loanMoney);
		}

		repository.addAccount(account);
		allList();
		JOptionPane.showMessageDialog(this, "정상 등록 처리되었습니다.");
		allClearTF(accNumTF, AccOwnerTF, pwTF, loanTF, depositTF);

		
		
//		try {
//			FileOutputStream fileName = new FileOutputStream("Account.ser");
//			ObjectOutputStream out = new ObjectOutputStream(fileName);
//			out.writeObject(account);
//			System.out.println(account);
//			out.close();
//			loadFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

//	public void loadFile() {
//		Account account;
//		String file = "Account.ser";
//		try {
//			FileInputStream fileIn = new FileInputStream(file);
//			ObjectInputStream In = new ObjectInputStream(fileIn);
//			account = (Account) In.readObject();
//			In.close();
//			System.out.println(In);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}

	// 종료메소드
	public void exit() {
		setVisible(false);
		dispose();
		System.exit(0);
		((JDBCAccountRepository)repository).close();
	}

//이벤트리스너
	public void addEventListener() {
		class ActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object eventSource = e.getSource();
				if (eventSource == newRegB) {
					addAccount();
				} else if (eventSource == allCheckB) {
					allList();
					
				} else if (eventSource == searchB)
					nameSearch();
			}
		}

		deleteB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeAcc();
			}
		});

		checkB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchAcc();
			}
		});

		searchB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nameSearch();
			}
		});
		ActionListener actionListener = new ActionHandler();
		// 윈도우 종료 처리
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();

			}
		});
		newRegB.addActionListener(actionListener);
		choice.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (choice.getSelectedItem().equals("입출금계좌")) {
						selectChoice(AccountType.GENERAL_ACCOUNT);
					} else if (choice.getSelectedItem().equals("마이너스계좌")) {
						selectChoice(AccountType.MINUS_ACCOUNT);
					}
				}
			}
		});

		allCheckB.addActionListener(actionListener);
		// 창 열릴때 처리
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				allList();

			}
		});

	}

	/**
	 * 편의 상 아래에 메인메소드에서 구현
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		AmsFrame frame = new AmsFrame("EZEN_BANK_AMS");
		frame.init();
		frame.pack();
		frame.setResizable(false); // 리사이징못하게하는
		frame.setVisible(true);
		frame.addEventListener();
	}
}
