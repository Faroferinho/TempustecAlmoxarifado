package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.Almoxarifado;
import main.UserInterface;

public class Login {
	
	BufferedImage tempustecLogo = Almoxarifado.imgManag.getProjectImage("Tempustec Logo Icone");
	int imgX = (Almoxarifado.WIDTH/2) - (tempustecLogo.getWidth()/2);
	int imgY = 30;
	int imgSize = 180;
	
	int textBoxX = Almoxarifado.WIDTH/4;
	int textBoxY = Almoxarifado.HEIGHT/5*2;
	int textBoxW = Almoxarifado.WIDTH/2;
	int textBoxH = 40;
	
	BufferedImage loginBttn = Almoxarifado.imgManag.getSprite(128, 256, 128, 64);
	int bttnX = (Almoxarifado.WIDTH/2) - (loginBttn.getWidth()/2);
	int bttnY = Almoxarifado.HEIGHT/16*13;
	
	String textInBoxCPF = "";
	String textInBoxPW = "";
	boolean isOnCPF = false;
	boolean isOnPW = false;
	public boolean isWriting = false;
	
	public Login() {
		imgX = (Almoxarifado.WIDTH/2) - (imgSize/2);
	}
	
	private String cpfFormater(String CPF) {
		String toReturn = "";
		
		String firstPart = "";
		String secondPart = "";
		String thirdPart = "";
		String fourthPart = "";
		
		if(CPF.length() < 3) {
			//System.out.println("Menor que 3");
			toReturn = CPF;
		}else if(CPF.length() > 2 && CPF.length() < 6) {
			//System.out.println("Entre 3 e 6");
			firstPart = CPF.substring(0, 3);
			secondPart = CPF.substring(3, CPF.length());
			
			toReturn = firstPart + "." + secondPart;
		}else if(CPF.length() > 5 && CPF.length() < 9) {
			//System.out.println("Entre 6 e 9");
			firstPart = CPF.substring(0, 3);
			secondPart = CPF.substring(3, 6);
			thirdPart = CPF.substring(6, CPF.length());
			
			toReturn = firstPart + "." + secondPart + "." + thirdPart;
		}else if(CPF.length() > 8 && CPF.length() < 12) {
			//System.out.println("Entre 9 e 12");
			firstPart = CPF.substring(0, 3);
			secondPart = CPF.substring(3, 6);
			thirdPart = CPF.substring(6, 9);
			fourthPart = CPF.substring(9, CPF.length());
			
			toReturn = firstPart + "." + secondPart + "." + thirdPart + "-" + fourthPart;
		}else {
			toReturn = CPF;
		}
		
		return toReturn;
	}
	
	public void writingOnCanvas(KeyEvent e) {
		if(isOnCPF) {
			if(e.getKeyCode() > 47 && e.getKeyCode() < 58) {
				textInBoxCPF += e.getKeyChar();
			} else {
				if(textInBoxCPF.length() > 0 && e.getKeyCode() == 8) {
					textInBoxCPF = textInBoxCPF.substring(0, textInBoxCPF.length()-1);
				}
		
			}
			
			if(textInBoxCPF.length() > 14) {
				textInBoxCPF = textInBoxCPF.substring(0, textInBoxCPF.length()-1);
			}
		}else if(isOnPW) {
			if((e.getKeyCode() > 20 && e.getKeyCode() < 144) && (e.getKeyCode() != 37 && e.getKeyCode() != 38 && e.getKeyCode() != 39 && e.getKeyCode() != 40)) {
				textInBoxPW += e.getKeyChar();
			} else {
				if(textInBoxPW.length() > 0 && e.getKeyCode() == 8) {
				textInBoxPW = textInBoxPW.substring(0, textInBoxPW.length()-1);
				}
		
			}
			
			if(textInBoxPW.length() > 30) {
				textInBoxPW = textInBoxPW.substring(0, textInBoxPW.length()-1);
			}
		}
		
		
	}
	
	private String censoringPassword(String pw) {
		String returnString = "";
		
		for(int i = 0; i < pw.length(); i++) {
			returnString += "*";
		}
		
		return returnString;
	}
	
	public void tick() {
		if(Almoxarifado.mPressed) {
			System.out.println("Clicado");
			if(Almoxarifado.mX > textBoxX && Almoxarifado.mX < textBoxX + textBoxW) {
				//System.out.println("Dentro do Espaço Lateral");
				if(Almoxarifado.mY > textBoxY && Almoxarifado.mY < textBoxY + textBoxH) {
					System.out.println("Area de Texto CPF");
					isWriting = true;
					isOnCPF = true;
					isOnPW = false;
				}else if(Almoxarifado.mY > (int) (textBoxY * 1.6) && Almoxarifado.mY < (int) (textBoxY * 1.6) + textBoxH) {
					System.out.println("Area de Texto Senha");
					isWriting = true;
					isOnCPF = false;
					isOnPW = true;
				}else {
					isWriting = false;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.fillRect(0, 0, Almoxarifado.WIDTH, Almoxarifado.HEIGHT);
		g.setColor(Color.orange);
		g.fillRoundRect((Almoxarifado.WIDTH/5), 15, (Almoxarifado.WIDTH/5)*3, Almoxarifado.HEIGHT-30, 18, 18);
		g.setColor(Color.gray);
		g.fillRoundRect((Almoxarifado.WIDTH/5) + 8, 15 + 8, (Almoxarifado.WIDTH/5)*3 - 16, Almoxarifado.HEIGHT-30 - 16, 18, 18);
		g.drawImage(tempustecLogo, imgX, imgY, imgSize, imgSize, null);
		
		g.setColor(Color.white);
		g.fillRoundRect(textBoxX, textBoxY, textBoxW, textBoxH, 15, 15);
		g.fillRoundRect(textBoxX, (int) (textBoxY * 1.6), textBoxW, textBoxH, 15, 15);
		g.setFont(new Font(Font.SANS_SERIF, 0, 20));
		g.drawString("CPF:", textBoxX + 15, textBoxY - 3);
		g.drawString("Senha: ", textBoxX + 15, (int) (textBoxY * 1.6) - 3);
		g.setColor(Color.black);
		g.drawRoundRect(textBoxX, textBoxY, textBoxW, textBoxH, 15, 15);
		g.drawRoundRect(textBoxX, (int) (textBoxY * 1.6), textBoxW, textBoxH, 15, 15);
		
		g.drawString(cpfFormater(textInBoxCPF), textBoxX + 5, textBoxY + textBoxH - (g.getFontMetrics().getHeight()/2));
		g.drawString(censoringPassword(textInBoxPW), textBoxX + 5, (int) (textBoxY * 1.6) + textBoxH - (g.getFontMetrics().getHeight()/4));
		
		g.drawImage(loginBttn, bttnX, bttnY, null);
		UserInterface.isOnButton(g, bttnX, bttnY);
	}
	
}
