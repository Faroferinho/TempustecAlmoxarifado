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
	
	private int idInsertionValue = 0;

	public Insertions() {
		// TODO Auto-generated constructor stub
	}
	
	public void writer(KeyEvent e) {
		
		String textInserted = "";
		
		if(selected > -1) {
			textInserted = values.get(selected);
			
			positionCursor(textInserted, e);
			
			if(writerIndex == 0) {
				if(!((e.getExtendedKeyCode() > -1 && e.getExtendedKeyCode() < 21) ||
					 (e.getExtendedKeyCode() > 36 && e.getExtendedKeyCode() < 41) ||
					 (e.getExtendedKeyCode() > 126 && e.getExtendedKeyCode() < 160))) {
					textInserted += e.getKeyChar();
				}else {
					
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && textInserted.length() > 0) {
						textInserted = textInserted.substring(0, textInserted.length() - 1);
					}
					
					if(e.getKeyCode() == KeyEvent.VK_TAB) {
						recomendation = "";
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
		values.set(selected, textInserted.replaceAll("\"", "''"));
		showRecomendations();
	}
	
	private void positionCursor(String text,KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			// Esquerda
			if(writerIndex < (text.length())) {
				writerIndex++;
			}
			break;
		case KeyEvent.VK_UP:
			// Cima
			writerIndex = text.length();
			break;
		case KeyEvent.VK_RIGHT:
			// Direita
			if(writerIndex > 0) {
				writerIndex--;
			}
			break;
		case KeyEvent.VK_DOWN:
			// Baixo
			writerIndex = 0;
			break;
		}
		System.out.println("writerIndex vale " + writerIndex);
	}
	
	public String advancedWriter(String originalText, KeyEvent e) {
		String firstHalf = originalText.substring(0, originalText.length() - writerIndex);
		String secondHalf = originalText.substring(originalText.length() - writerIndex, originalText.length());
		String toReturn = "";
		
		if(!((e.getExtendedKeyCode() > -1  && e.getExtendedKeyCode() < 21) ||
			 (e.getExtendedKeyCode() > 36  && e.getExtendedKeyCode() < 41) ||
			 (e.getExtendedKeyCode() > 126 && e.getExtendedKeyCode() < 160))) {
			firstHalf += e.getKeyChar();
		}else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			firstHalf = originalText.substring(0, originalText.length() - writerIndex - 1);
		}else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			secondHalf = originalText.substring(originalText.length() - writerIndex + 1, originalText.length());
			writerIndex--;
		}
		
		toReturn = firstHalf + secondHalf;
		return toReturn;
	}
	
	protected void clearIndex() {
		if(selected != -1) {
			if(values.get(selected).isEmpty()) {
				writerIndex = 0;				
			}
		}
	}
	
	protected void setIndex(Graphics g) {
		if(selected != -1) {
			
			if(click) {
				
				for(int i = 1; i < values.get(selected).length(); i++) {
					
					if(Functions.isOnBox(textBoxes.get(selected).getX() + g.getFontMetrics().stringWidth(values.get(selected).substring(0, i - 1)) + 12,
						textBoxes.get(selected).getY(),
						g.getFontMetrics().charWidth(values.get(selected).charAt(i-1)),
						textBoxes.get(selected).getHeight()
					)) {
						writerIndex = values.get(selected).length() - i;
						System.out.println("writerIndex - " + writerIndex);
					}
				
				}
			
			}
		
		}
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
		if(blink && selected != -1) {
			g.fillRect((int)(textBoxes.get(selected).getX()) + 12 + g.getFontMetrics().stringWidth(values.get(selected).substring(0, values.get(selected).length() - writerIndex)), (int)(textBoxes.get(selected).getY()) + 9, 1, 24);
		}
	}
	
	protected void alterTextBoxSize(Graphics g, int index) {
		if(g.getFontMetrics().stringWidth(values.get(index)) > (textBoxes.get(index).width - 20)) {
			textBoxes.get(index).setSize(g.getFontMetrics().stringWidth(values.get(index)) + 20, textBoxes.get(index).height);
		}
	}
	
	protected void clearAllValues() {
		for(int i = 0; i < quantity; i++) {
			values.set(i, "");
		}
		selected = -1;
		setIdInsertionValue(0);
		isWriting = false;
	}
	
	public int getIdInsertionValue() {
		return idInsertionValue;
	}
	
	public void setIdInsertionValue(int AssemblyID) {
		idInsertionValue = AssemblyID;
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
