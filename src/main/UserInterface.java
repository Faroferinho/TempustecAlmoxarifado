package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class UserInterface {
	
	public static int bttnX[] = new int[5];
	public static int bttnY = 30;
	
	public static int boxWidth = 128;
	public static int boxHeight = 64;
	
	public BufferedImage iconProfile;
	public BufferedImage iconParts;
	public BufferedImage iconAssembly;
	public BufferedImage iconArchive;
	public BufferedImage iconExit;
	public BufferedImage iconProfileActivated;
	public BufferedImage iconPartsActivated;
	public BufferedImage iconAssemblyActivated;
	public BufferedImage iconArchiveActivated;
	public static BufferedImage overButton;
	
	public static int spd = 24;
	
	public static int maximunHeight = Almoxarifado.HEIGHT - (bttnY + boxHeight + 12) - 40;

	public UserInterface() {
		bttnX[0] = (Almoxarifado.WIDTH / (bttnX.length + 1)) - (boxWidth / 2);
		bttnX[1] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 2) - (boxWidth / 2);
		bttnX[2] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 3) - (boxWidth / 2);
		bttnX[3] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 4) - (boxWidth / 2);
		bttnX[4] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 5) - (boxWidth / 2);
		
		iconProfile = Almoxarifado.imgManag.getSprite(384, 0, boxWidth, boxHeight);
		iconParts = Almoxarifado.imgManag.getSprite(256, 0, boxWidth, boxHeight);
		iconAssembly = Almoxarifado.imgManag.getSprite(128, 0, boxWidth, boxHeight);
		iconArchive = Almoxarifado.imgManag.getSprite(0, 0, boxWidth, boxHeight);
		iconExit = Almoxarifado.imgManag.getSprite(512, 0, boxWidth, boxHeight);

		iconProfileActivated = Almoxarifado.imgManag.getSprite(384, 64, boxWidth, boxHeight);
		iconPartsActivated = Almoxarifado.imgManag.getSprite(256, 64, boxWidth, boxHeight);
		iconAssemblyActivated = Almoxarifado.imgManag.getSprite(128, 64, boxWidth, boxHeight);
		iconArchiveActivated = Almoxarifado.imgManag.getSprite(0, 64, boxWidth, boxHeight);
		
		overButton = Almoxarifado.imgManag.getSprite(0, 64*3, boxWidth, boxHeight);
		
	}
	
	public void clearBox(Graphics g) {
		g.setColor(Color.orange);
		g.fillRect(30, bttnY + boxHeight + 12, Almoxarifado.WIDTH - 30*2, maximunHeight);
		
		g.setColor(Color.black);
		g.fillRect(36, bttnY + boxHeight + 18, Almoxarifado.WIDTH - 36*2, maximunHeight-12);
	}
	
	public void limitScrollToWorkspaceArea(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Almoxarifado.WIDTH, 10);
		g.fillRect(0, Almoxarifado.HEIGHT-10, Almoxarifado.WIDTH, 10);
		
		g.setColor(Color.lightGray);
		g.fillRect(10, 10, Almoxarifado.WIDTH-20, 10);
		g.fillRect(10, Almoxarifado.HEIGHT-20, Almoxarifado.WIDTH-20, 10);
		
		g.setColor(Color.gray);
		g.fillRect(20, 20, Almoxarifado.WIDTH-40, bttnY + boxHeight - 5);
		g.fillRect(20, Almoxarifado.HEIGHT-40, Almoxarifado.WIDTH-40, 20);
		
		g.setColor(Color.orange);
		g.fillRect(30, bttnY + boxHeight + 12, Almoxarifado.WIDTH - 30*2, 6);
		g.fillRect(30, Almoxarifado.HEIGHT - 46, Almoxarifado.WIDTH - 30*2, 6);
	}
	
	public byte setFunction(int mx, int my){
		
		if(my < bttnY + boxHeight && my > bttnY) {
			if(mx > bttnX[0] && mx < bttnX[0] + boxWidth) {
				if(Almoxarifado.type.equals("1")) {
					Almoxarifado.admProfile.reset = true;
				}else {
					Almoxarifado.workProfile.reset = true;
				}
				return 1;
			}else if(mx > bttnX[1] && mx < bttnX[1] + boxWidth) {
				return 2;
			}else if(mx > bttnX[2] && mx < bttnX[2] + boxWidth) {
				return 3;
			}else if(mx > bttnX[3] && mx < bttnX[3] + boxWidth) {
				return 4;
			}else if(mx > bttnX[4] && mx < bttnX[4] + boxWidth) {
				return 5;
			}
		}
		
		return -1;
		
		}
	
	public static void isOnButton(Graphics g, int pX, int pY) {
		if(Almoxarifado.mX > pX && Almoxarifado.mX < pX + boxWidth) {
			if(Almoxarifado.mY > pY && Almoxarifado.mY < pY + boxHeight) {
				g.drawImage(overButton, pX, pY, null);
			}
		}
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
	
	public void drawTableBorders(Graphics g) {
		
	}

	public void tick() {
		if(Almoxarifado.mPressed) {
			checkMouse();
			Almoxarifado.mPressed = false;
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(iconProfile, bttnX[0], bttnY, null);
		g.drawImage(iconParts, bttnX[1], bttnY, null);
		g.drawImage(iconAssembly, bttnX[2], bttnY, null);
		g.drawImage(iconArchive, bttnX[3], bttnY, null);
		g.drawImage(iconExit, bttnX[4], bttnY, null);
		
		switch(Almoxarifado.state) {
		case 1:
			g.drawImage(iconProfileActivated, bttnX[0], bttnY, null);
			break;
		case 2:
			g.drawImage(iconPartsActivated, bttnX[1], bttnY, null);
			break;
		case 3:
		case 5:
			g.drawImage(iconAssemblyActivated, bttnX[2], bttnY, null);
			break;
		case 4:
			g.drawImage(iconArchiveActivated, bttnX[3], bttnY, null);
			break;
		}
		
		isOnButton(g, bttnX[0], bttnY);
		isOnButton(g, bttnX[1], bttnY);
		isOnButton(g, bttnX[2], bttnY);
		isOnButton(g, bttnX[3], bttnY);
		isOnButton(g, bttnX[4], bttnY);
		
	}

}
