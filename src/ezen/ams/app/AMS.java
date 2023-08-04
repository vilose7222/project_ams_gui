package ezen.ams.app;

import ezen.ams.view.AmsFrame;

public class AMS {

	
	public static void main(String[] args) {

		AmsFrame frame = new AmsFrame("EZEN_BANK_AMS");
		frame.init();
		frame.pack();
		frame.setResizable(false);
		frame.addEventListener();
		frame.setVisible(true);
	}

}
