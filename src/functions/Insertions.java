package functions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Almoxarifado;

public abstract class Insertions {
	
	protected int selected = -1;
	protected int quantity;
	protected int border;
	
	public boolean click = false;
	
	public boolean isWriting = false;
	private int writerIndex = 0;
	
	private boolean blink;
	private int blinkAux = 0;
	
	protected String title = "";
	protected ArrayList<String> labels = new ArrayList<String>();
	protected ArrayList<Rectangle> textBoxes = new ArrayList<Rectangle>();
	protected ArrayList<String> values = new ArrayList<String>();
	
	protected final BufferedImage okImage = Almoxarifado.imgManag.getSprite(0, 570, 165, 60);
	protected final BufferedImage cancelImage = Almoxarifado.imgManag.getSprite(165, 570, 165, 60);
	
	public String genericString = "----------";
	public int genericNumeral = 0;
	
	protected boolean hasRecomendation = false;
	protected String recomendation = "";
	
	protected int middleScreenX = (Almoxarifado.WIDTH)/2;

	public Insertions() {
		// TODO Auto-generated constructor stub
	}
	
	public void writer(KeyEvent e) {
		
		String textInserted = "";
		
		if(selected > -1) {
			textInserted = values.get(selected);
			
			if(writerIndex == 0) {
				if(!((e.getExtendedKeyCode() > -1 && e.getExtendedKeyCode() < 21) || 
					 (e.getExtendedKeyCode() > 126 && e.getExtendedKeyCode() < 160))) {
					textInserted += e.getKeyChar();
				}else {
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && textInserted.length() > 0) {
						textInserted = textInserted.substring(0, textInserted.length() - 1);
					}
					
					if(e.getKeyCode() == KeyEvent.VK_TAB) {
						selected++;
						if(selected > values.size() - 1) {
							selected = 0;
						}
						return;
					}
				}
			}else {
				textInserted = advancedWriter(textInserted, e);
			}
		}
		
		values.set(selected, textInserted);
		showRecomendations();
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
	
	protected void drawCursor(Graphics g) {
		
		blinkAux++;
		
		if(blinkAux%21 == 0) {
			if(blink) {
				blink = false;
			}else {
				blink = true;
			}
		}
		
		g.setColor(Color.black);
		if(blink) {
			g.fillRect((int)(textBoxes.get(selected).getX()) + 12 + g.getFontMetrics().stringWidth(values.get(selected)), (int)(textBoxes.get(selected).getY()) + 9, 1, 24);
		}
	}
	
	protected abstract void writeTextOnBox();
	
	protected abstract void writeQuery();
	
	protected abstract void fillDefaultValues();
	
	protected abstract void showRecomendations();
	
	protected abstract void okButtonClick();
	
	protected abstract void cancelButtonClick();
	
	public abstract void tick();
	
	public abstract void render(Graphics g);

}
