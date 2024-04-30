package functions;

import java.awt.Color;

import javax.swing.JFrame;

public class InputWindows{
	public static final int OK_BUTTON = 0;
	public static final int OK_CANCEL_BUTTON = 1;
	public static final int YES_NO_BUTTON = 2;
	public static final int YES_NO_CANCEL_BUTTON = 3;

	public InputWindows() {
		
	}
	
	public static boolean confirm(String text) {
		JFrame newWindow = new JFrame();
		newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newWindow.setResizable(false);
		newWindow.setSize(320,240);
		newWindow.setVisible(true);
		newWindow.getContentPane().setBackground(new Color(0,0,0));
		;
		
		return  true;
	}

}
