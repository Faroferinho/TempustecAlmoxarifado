package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import functions.Functions;

public class UserInterface {
	
	public static int bttnX[] = new int[5];
	public static int bttnY = 30;
	
	public static int boxWidthBig = 215;
	public static int boxWidthSmall = 165;
	public static int boxHeight = 60;
	
	public BufferedImage bttnProfile;
	public BufferedImage bttnParts;
	public BufferedImage bttnAssembly;
	public BufferedImage bttnArchive;
	public BufferedImage bttnExit;
	public BufferedImage bttnProfileActivated;
	public BufferedImage bttnPartsActivated;
	public BufferedImage bttnAssemblyActivated;
	public BufferedImage bttnArchiveActivated;
	
	public static int spd = 24;
	
	public static int maximunHeight = Almoxarifado.HEIGHT - (bttnY + boxHeight + 12) - 40;

	public UserInterface() {
		bttnX[0] = (Almoxarifado.WIDTH / (bttnX.length + 1)) - (boxWidthBig / 2);
		bttnX[1] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 2) - (boxWidthBig / 2);
		bttnX[2] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 3) - (boxWidthBig / 2);
		bttnX[3] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 4) - (boxWidthBig / 2);
		bttnX[4] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 5) - (boxWidthBig / 2);
		
		bttnProfile = Almoxarifado.imgManag.getSprite(215, 0, boxWidthBig, boxHeight);
		bttnParts = Almoxarifado.imgManag.getSprite(0, 60, boxWidthBig, boxHeight);
		bttnAssembly = Almoxarifado.imgManag.getSprite(215, 60, boxWidthBig, boxHeight);
		bttnArchive = Almoxarifado.imgManag.getSprite(0, 120, boxWidthBig, boxHeight);
		bttnExit = Almoxarifado.imgManag.getSprite(215, 120, boxWidthBig, boxHeight);

		bttnProfileActivated = Almoxarifado.imgManag.getSprite(0, 390, boxWidthBig, boxHeight);
		bttnPartsActivated = Almoxarifado.imgManag.getSprite(215, 390, boxWidthBig, boxHeight);
		bttnAssemblyActivated = Almoxarifado.imgManag.getSprite(0, 450, boxWidthBig, boxHeight);
		bttnArchiveActivated = Almoxarifado.imgManag.getSprite(215, 450, boxWidthBig, boxHeight);
		
		System.out.println("Carregou UserInterface: " + LocalDateTime.now());
	}
	
	public void clearBox(Graphics g) {
		g.setColor(Color.orange);
		g.fillRoundRect(30, bttnY + boxHeight + 12, Almoxarifado.WIDTH - 30*2, Almoxarifado.HEIGHT - (bttnY + boxHeight + 12 + 30), 11, 11);
		
		g.setColor(Color.black);
		g.fillRect(36, bttnY + boxHeight + 18, Almoxarifado.WIDTH - 36*2, Almoxarifado.HEIGHT - (bttnY + boxHeight + 12 + 30 + 12));
	}
	
	public void limitScrollToWorkspaceArea(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(35, 0, Almoxarifado.WIDTH - 35*2, 10);
		g.fillRect(35, Almoxarifado.HEIGHT - 10, Almoxarifado.WIDTH - 35*2, 30);
		
		g.setColor(Color.lightGray);
		g.fillRect(35, 10, Almoxarifado.WIDTH - 35*2, 13);
		g.fillRect(35, Almoxarifado.HEIGHT - 23, Almoxarifado.WIDTH - 35*2, 13);
		
		g.setColor(Color.gray);
		g.fillRect(35, 23, Almoxarifado.WIDTH - 35*2, bttnY + boxHeight - 10);
		g.fillRect(35, Almoxarifado.HEIGHT - 30, Almoxarifado.WIDTH - 35*2, 7);
		
		g.setColor(Color.orange);
		g.fillRect(35, bttnY + boxHeight + 12, Almoxarifado.WIDTH - 35*2, 6);
		g.fillRect(35, Almoxarifado.HEIGHT - 36, Almoxarifado.WIDTH - 35*2, 6);
	}
	
	public int setFunction(int mx, int my){
		
		if(my > bttnY && my < bttnY + boxHeight) {
			for(int i = 0; i < 5; i++) {
				if(mx > bttnX[i] && mx < bttnX[i] + boxWidthBig) {
					return i + 1;
				}
			}
		}
		
		return -1;
		
	}
	
	public void checkMouse() {
		switch(setFunction(Almoxarifado.mX, Almoxarifado.mY)) {
		case 1:
			Almoxarifado.state = 1;
			break;
		case 2:
			Almoxarifado.state = 2;
			break;
		case 3:
			Almoxarifado.state = 3;
			break;
		case 4:
			Almoxarifado.state = 4;
			break;
		case 5:
			Functions.generatePurchaseInquery();
			System.exit(0);
			break;
		}
	}
	
	public static void isOnBigButton(Graphics g, int pX, int pY) {
		if(Almoxarifado.mX > pX && Almoxarifado.mX < pX + boxWidthBig) {
			if(Almoxarifado.mY > pY && Almoxarifado.mY < pY + boxHeight) {
				g.setColor(new Color(0, 0, 0, 210));
				g.fillRoundRect(pX - 1, pY - 1, boxWidthBig + 2, boxHeight + 2, 60, 60);
			}
		}
	}
	
	public static void isOnSmallButton(Graphics g, int pX, int pY) {
		if(Almoxarifado.mX > pX && Almoxarifado.mX < pX + boxWidthSmall) {
			if(Almoxarifado.mY > pY && Almoxarifado.mY < pY + boxHeight) {
				g.setColor(new Color(0, 0, 0, 210));
				g.fillRect(pX, pY, boxWidthSmall, boxHeight);
			}
		}
	}

	public void tick() {
		if(Almoxarifado.mPressed) {
			checkMouse();
			Almoxarifado.mPressed = false;
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(bttnProfile, bttnX[0], bttnY, null);
		g.drawImage(bttnParts, bttnX[1], bttnY, null);
		g.drawImage(bttnAssembly, bttnX[2], bttnY, null);
		g.drawImage(bttnArchive, bttnX[3], bttnY, null);
		g.drawImage(bttnExit, bttnX[4], bttnY, null);
		
		switch(Almoxarifado.state) {
		case 1:
			g.drawImage(bttnProfileActivated, bttnX[0], bttnY, null);
			break;
		case 2:
			g.drawImage(bttnPartsActivated, bttnX[1], bttnY, null);
			break;
		case 3:
		case 5:
			g.drawImage(bttnAssemblyActivated, bttnX[2], bttnY, null);
			break;
		case 4:
			g.drawImage(bttnArchiveActivated, bttnX[3], bttnY, null);
			break;
		}
		
		isOnBigButton(g, bttnX[0], bttnY);
		isOnBigButton(g, bttnX[1], bttnY);
		isOnBigButton(g, bttnX[2], bttnY);
		isOnBigButton(g, bttnX[3], bttnY);
		isOnBigButton(g, bttnX[4], bttnY);
		
	}

}
