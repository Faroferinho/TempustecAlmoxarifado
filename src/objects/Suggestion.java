package objects;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import functions.TextSuggestor;

public class Suggestion extends JLabel{

	private static final long serialVersionUID = 9042740092370050480L;
	
	private final TextSuggestor auto;
	private final JWindow popUpWindow;
	private final JTextField textField;
	
	private boolean isFocused = false;
	
	private final Color labelColor;
	private final Color focusedBorderColor;

	public Suggestion(String text, Color borderColor, Color textColor, TextSuggestor suggestor){
super(text);
		
		labelColor = textColor;
		focusedBorderColor = borderColor;
		
		auto = suggestor;
		popUpWindow = auto.getAutoPopUpWindow();
		textField = auto.getTextField();
		initializeComponent();
	}
	
	private void initializeComponent() {
		setFocusable(true);
		setForeground(labelColor);
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				replaceText();
				
				popUpWindow.setVisible(false);
			}
		});
		
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "Enter Released");
		getActionMap().put("Enter Released", new AbstractAction() {
			
			private static final long serialVersionUID = 5486497934199834320L;

			@Override
			public void actionPerformed(ActionEvent e) {
				replaceText();
				
				popUpWindow.setVisible(false);
			}
			
		});;
	}
	
	private void replaceText(){
		String aux = textField.getText();
		int startOfLastWord = 0;
		
		//System.out.println("Clicou em " + getText());
		
		for(int i = 0; i < aux.length(); i++) {
			if(aux.charAt(i) == ' ') {
				startOfLastWord = (i + 1);
			}
		}
		
		textField.setText(aux.substring(0, startOfLastWord) + getText() + ' ');
	}
	
	public void setIsFocused(boolean focused) {
		if(focused) {
			setBorder(new LineBorder(focusedBorderColor));
		}else {
			setBorder(new LineBorder(new Color(0,0,0,0)));
		}
		repaint();
		isFocused = focused;
	}
	
	public boolean isFocused() {
		return isFocused;
	}

}
