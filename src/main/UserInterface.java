package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class UserInterface {
	
	public static int bttnX[] = new int[5];
	public static int bttnY = 35;
	public static int spaceBetween[] = new int[5];
	
	public static int boxWidth = 170;
	public static int boxHeight = 40;

	public UserInterface() {
		for(int i = 0; i < 5; i++) {
			bttnX[i] = + i * boxWidth;
			spaceBetween[i] = 45 + 20*i;
		}
	}
	
	public void clearBox(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(spaceBetween[0] + bttnX[0]+6, 100 +6, spaceBetween[4] + bttnX[4] + boxWidth - 45-12, 430-12);
	}
	
	public byte setFunction(int mx, int my){
		
		if(my < bttnY + boxHeight && my > bttnY) {
			if(mx > spaceBetween[0] + bttnX[0] && mx < spaceBetween[0] + bttnX[0] + boxWidth) {
				System.out.println(1);
				if(Almoxarifado.type.equals("1\n")) {
					Almoxarifado.admProfile.reset = true;
				}else {
					Almoxarifado.workProfile.reset = true;
				}
				return 1;
			}else if(mx > spaceBetween[1] + bttnX[1] && mx < spaceBetween[1] + bttnX[1] + boxWidth) {
				System.out.println(2);
				return 2;
			}else if(mx > spaceBetween[2] + bttnX[2] && mx < spaceBetween[2] + bttnX[2] + boxWidth) {
				System.out.println(3);
				return 3;
			}else if(mx > spaceBetween[3] + bttnX[3] && mx < spaceBetween[3] + bttnX[3] + boxWidth) {
				System.out.println(4);
				return 4;
			}else if(mx > spaceBetween[4] + bttnX[4] && mx < spaceBetween[4] + bttnX[4] + boxWidth) {
				System.out.println(5);
				return 5;
			}
		}
		
		return -1;
		
		}
	
	public void checkMouse() {
		if(setFunction(Almoxarifado.mX, Almoxarifado.mY) == 1) {
			Almoxarifado.state = 1;
		}else if(setFunction(Almoxarifado.mX, Almoxarifado.mY) == 2) {
			Almoxarifado.state = 2;
		}else if(setFunction(Almoxarifado.mX, Almoxarifado.mY) == 3) {
			Almoxarifado.state = 3;
		}else if(setFunction(Almoxarifado.mX, Almoxarifado.mY) == 4) {
			Almoxarifado.state = 4;
		}else if(setFunction(Almoxarifado.mX, Almoxarifado.mY) == 5) {
			System.exit(0);
		}
	}
	
	public void tick() {
		if(Almoxarifado.mPressed) {
			//System.out.println("Mouse Clicado UI");
			checkMouse();
			Almoxarifado.mPressed = false;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.orange);
		
		g.fillRect(spaceBetween[0] + bttnX[0], bttnY, boxWidth, boxHeight);
		g.fillRect(spaceBetween[1] + bttnX[1], bttnY, boxWidth, boxHeight);
		g.fillRect(spaceBetween[2] + bttnX[2], bttnY, boxWidth, boxHeight);
		g.fillRect(spaceBetween[3] + bttnX[3], bttnY, boxWidth, boxHeight);
		g.setColor(Color.red);
		g.fillRect(spaceBetween[4] + bttnX[4], bttnY, boxWidth, boxHeight);
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", 1, 22));
		g.drawString("Perfil", spaceBetween[0] + bttnX[0] + boxWidth / 2 - g.getFontMetrics().stringWidth("Perfil") / 2, bttnY + g.getFontMetrics().getHeight());
		g.drawString("Peças", spaceBetween[1] + bttnX[1] + boxWidth / 2 - g.getFontMetrics().stringWidth("Peças") / 2, bttnY + g.getFontMetrics().getHeight());
		g.drawString("Projeto", spaceBetween[2] + bttnX[2] + boxWidth / 2 - g.getFontMetrics().stringWidth("Projeto") / 2, bttnY + g.getFontMetrics().getHeight());
		g.drawString("Arquivo", spaceBetween[3] + bttnX[3] + boxWidth / 2 - g.getFontMetrics().stringWidth("Arquivo") / 2, bttnY + g.getFontMetrics().getHeight());
		g.drawString("Sair", spaceBetween[4] + bttnX[4] + boxWidth / 2 - g.getFontMetrics().stringWidth("Sair") / 2, bttnY + g.getFontMetrics().getHeight());
		
		
		g.setColor(Color.orange);
		g.fillRect(spaceBetween[0] + bttnX[0], 100, spaceBetween[4] + bttnX[4] + boxWidth - 45, 430);
		
		g.setColor(Color.black);
		g.fillRect(spaceBetween[0] + bttnX[0]+6, 100 +6, spaceBetween[4] + bttnX[4] + boxWidth - 45-12, 430-12);
		
		
	}

}
