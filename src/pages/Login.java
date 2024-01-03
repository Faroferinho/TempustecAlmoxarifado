package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class Login {
	boolean isOnTheRightState = false;
	
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
	
	private boolean blink;
	private int blinkAux = 0;
	
	private int indexCursor = 0;
	private boolean changeCursor = false;
	private int auxCursorPositioner = 0;
	private int indexCursorPositioner = 0;
	
	public Login() {
		imgX = (Almoxarifado.WIDTH/2) - (imgSize/2);
	}
	
	private void submitForm() {
		
		String rawRegisters = DBConector.readDB("CPF", "Funcionarios");
		String rawPasswords = DBConector.readDB("password", "Funcionarios");
		
		String[] registers = rawRegisters.split(" § \n");
		String[] passwords = rawPasswords.split(" § \n");
		
		for(int i = 0; i < registers.length; i++) {
			if(textInBoxCPF.equals(registers[i]) && textInBoxPW.equals(passwords[i])) {
				
				String auxString = "";
				auxString = DBConector.findInDB("*", "Funcionarios", "CPF", textInBoxCPF);
				String[] toConfig = auxString.split(" § ");
				
				Almoxarifado.rdf = toConfig[0];
				Almoxarifado.name = toConfig[1];
				Almoxarifado.cpf = cpfFormater(textInBoxCPF);
				Almoxarifado.type = toConfig[4];
				
				if(Almoxarifado.type.equals("1")) {
					Almoxarifado.admProfile = new Admnistrator(Almoxarifado.name, Almoxarifado.rdf, Almoxarifado.cpf);
				}else {
					Almoxarifado.workProfile = new Employee(Almoxarifado.name, Almoxarifado.rdf, Almoxarifado.cpf);
				}
				
				Almoxarifado.state = 1;
				return;
			}
		}
		
		JOptionPane.showMessageDialog(null, "Conta e/ou Senha Incorretas", "Erro ao efetuar Login", JOptionPane.ERROR_MESSAGE);
		
	}
	
	private String cpfFormater(String CPF) {
		String toReturn = "";
		
		String firstPart = "";
		String secondPart = "";
		String thirdPart = "";
		String fourthPart = "";
		
		if(CPF.length() < 3) {
			toReturn = CPF;
		}else if(CPF.length() > 2 && CPF.length() < 6) {
			firstPart = CPF.substring(0, 3);
			secondPart = CPF.substring(3, CPF.length());
			
			toReturn = firstPart + "." + secondPart;
		}else if(CPF.length() > 5 && CPF.length() < 9) {
			firstPart = CPF.substring(0, 3);
			secondPart = CPF.substring(3, 6);
			thirdPart = CPF.substring(6, CPF.length());
			
			toReturn = firstPart + "." + secondPart + "." + thirdPart;
		}else if(CPF.length() > 8 && CPF.length() < 12) {
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
	
	private String censoringPassword(String pw) {
		String returnString = "";
		
		for(int i = 0; i < pw.length(); i++) {
			returnString += "*";
		}
		
		return returnString;
	}
	
	public void writingOnCanvas(KeyEvent e) {
		if(isOnCPF) {
			if((e.getKeyCode() > 47 && e.getKeyCode() < 58) || (e.getKeyCode() > 95 && e.getKeyCode() < 106)) {
				textInBoxCPF += e.getKeyChar();
				
			} else {
				if(textInBoxCPF.length() > 0 && e.getKeyCode() == 8) {
					textInBoxCPF = textInBoxCPF.substring(0, textInBoxCPF.length()-1);
				}
			}
			
			if(textInBoxCPF.length() > 11) {
				textInBoxCPF = textInBoxCPF.substring(0, textInBoxCPF.length()-1);
			}
		}else if(isOnPW) {
			if((e.getKeyCode() > 20 && e.getKeyCode() < 144) && (e.getKeyCode() != 37 && e.getKeyCode() != 38 
			&& e.getKeyCode() != 39 && e.getKeyCode() != 40)) {
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
		
		if(e.getKeyCode() == KeyEvent.VK_TAB) {
			if(isWriting) {
				if(isOnCPF) {
					isOnPW = true;
					isOnCPF = false;
				}else {
					isOnCPF = true;
					isOnPW = false;
				}
			}
		}
		
		if(e.getKeyCode() > 36 && e.getKeyCode() < 41) {
			System.out.println("Está Movendo com as Setinhas");
			indexCursor = e.getKeyCode();
			changeCursor = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			submitForm();
		}
		
	}
	
	public void tick() {
		if(Almoxarifado.state == 0) {
			isOnTheRightState = true;
		}else {
			isOnTheRightState = false;
		}
		
		if(isOnTheRightState) {
		
			if(Almoxarifado.mPressed) {
				if(Almoxarifado.mX > textBoxX && Almoxarifado.mX < textBoxX + textBoxW) {
					if(Almoxarifado.mY > textBoxY && Almoxarifado.mY < textBoxY + textBoxH) {
						isWriting = true;
						isOnCPF = true;
						isOnPW = false;
					}else if(Almoxarifado.mY > (int) (textBoxY * 1.6) && Almoxarifado.mY < (int) (textBoxY * 1.6) + textBoxH) {
						isWriting = true;
						isOnCPF = false;
						isOnPW = true;
					}else {
						isWriting = false;
					}
				}
				
				if(Almoxarifado.mX > bttnX && Almoxarifado.mX < bttnX + loginBttn.getWidth() 
				&& Almoxarifado.mY > bttnY && Almoxarifado.mY < bttnY + loginBttn.getHeight()) {
					submitForm();
				}
			}
			
		}
	}
	
	public void render(Graphics g) {
		int round = 144;
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.fillRect(0, 0, Almoxarifado.WIDTH, Almoxarifado.HEIGHT);
		g.setColor(Color.orange);
		g.fillRoundRect((Almoxarifado.WIDTH/5), 15, (Almoxarifado.WIDTH/5)*3, Almoxarifado.HEIGHT-30, round, round);
		g.setColor(Color.gray);
		g.fillRoundRect((Almoxarifado.WIDTH/5) + 8, 15 + 8, (Almoxarifado.WIDTH/5)*3 - 16, Almoxarifado.HEIGHT-30 - 16, round, round);
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
		
		if(changeCursor) {
			System.out.println("auxCursorPositioner: " + auxCursorPositioner);
			
			switch(indexCursor) {
			case KeyEvent.VK_UP:
				if(isOnCPF) {
					auxCursorPositioner = g.getFontMetrics().stringWidth(cpfFormater(textInBoxCPF));
				}else if(isOnPW){
					auxCursorPositioner = g.getFontMetrics().stringWidth(censoringPassword(textInBoxPW));
				}
				break;
			
			case KeyEvent.VK_DOWN:
				auxCursorPositioner = 0;
				break;
			
			case KeyEvent.VK_LEFT:
				if(isOnCPF) {
					if(indexCursorPositioner < cpfFormater(textInBoxCPF).length()) {
						indexCursorPositioner++;
						System.out.println("Char: " + cpfFormater(textInBoxCPF).charAt(cpfFormater(textInBoxCPF).length() - indexCursorPositioner));
						auxCursorPositioner += g.getFontMetrics().stringWidth("" + cpfFormater(textInBoxCPF).charAt(cpfFormater(textInBoxCPF).length() - indexCursorPositioner));
					}
				}else if(isOnPW){
					
				}
				break;
			
			case KeyEvent.VK_RIGHT:
				if(isOnCPF) {
					
				}else if(isOnPW){
					
				}
				break;
			
			}
			changeCursor = false;
		}
		
		blinkAux++;
		
		if(blinkAux%23 == 0) {
			if(blink) {
				blink = false;
			}else {
				blink = true;
			}
		}
		
		if(blink && isWriting) {			
			if(isOnCPF) {
				g.fillRect(g.getFontMetrics().stringWidth(cpfFormater(textInBoxCPF)) - auxCursorPositioner + textBoxX + 5, textBoxY + 5, 2, 30);
			}else if(isOnPW) {
				g.fillRect(g.getFontMetrics().stringWidth(censoringPassword(textInBoxPW)) - auxCursorPositioner + textBoxX + 5, (int) (textBoxY * 1.6) + 5, 2, 30);
			}
		}
		
		g.drawImage(loginBttn, bttnX, bttnY, null);
		UserInterface.isOnButton(g, bttnX, bttnY);
	}
	
}
