package functions;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class Insertions {
	
	protected int selected = -1;
	protected int quantity;
	protected int border;
	
	public boolean click = false;
	
	private String textInserted;
	public boolean isWriting = false;
	private int writerIndex = 0;
	
	protected ArrayList<String> labels = new ArrayList<String>();
	protected ArrayList<Rectangle> textBoxes = new ArrayList<Rectangle>();
	protected ArrayList<String> values = new ArrayList<String>();

	public Insertions() {
		// TODO Auto-generated constructor stub
	}
	
	public void writer(KeyEvent e) {		
		if(selected > -1) {
			textInserted = values.get(selected);
			
			if(writerIndex == 0) {
				if(!((e.getExtendedKeyCode() > -1 && e.getExtendedKeyCode() < 20) || 
					 (e.getExtendedKeyCode() > 126 && e.getExtendedKeyCode() < 160))) {
					textInserted += e.getKeyChar();
				}else {
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && textInserted.length() > 0) {
						textInserted = textInserted.substring(0, textInserted.length()-1);
					}
				}
			}else {
				textInserted = advancedWriter(textInserted, e);
			}
		}
		
		values.set(selected, textInserted);
	}
	
	public String advancedWriter(String originalText, KeyEvent e) {
		String toReturn = originalText.substring(0, originalText.length() - writerIndex);
		
		switch(e.getKeyCode()) {
		case 37:
			writerIndex++;
			
			if(writerIndex < originalText.length()) {
				writerIndex = originalText.length();
			}
			
			break;
		case 38:
			break;
		case 39:
			writerIndex--;
			
			if(writerIndex > 0) {
				writerIndex = 0;
			}
			
			break;
		case 40:
			break;
		}
		
		return toReturn;
	}
	
	protected abstract void writeQuery();
	
	protected abstract void writeOnTextBoxes(Graphics g);
	
	protected abstract void verifyValues();
	
	public abstract void tick();
	
	public abstract void render(Graphics g);

}
