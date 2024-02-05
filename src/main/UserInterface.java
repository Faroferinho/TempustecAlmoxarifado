package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class UserInterface {
	
	public static int bttnX[] = new int[5];
	public static int bttnY = 30;
	
	public static int boxWidthBig = 215;
	public static int boxWidthSmall = 165;
	public static int boxHeight = 60;
	
	public BufferedImage iconProfile;
	public BufferedImage iconParts;
	public BufferedImage iconAssembly;
	public BufferedImage iconArchive;
	public BufferedImage iconExit;
	public BufferedImage iconProfileActivated;
	public BufferedImage iconPartsActivated;
	public BufferedImage iconAssemblyActivated;
	public BufferedImage iconArchiveActivated;
	
	public static int spd = 24;
	
	public static int maximunHeight = Almoxarifado.HEIGHT - (bttnY + boxHeight + 12) - 40;

	public UserInterface() {
		bttnX[0] = (Almoxarifado.WIDTH / (bttnX.length + 1)) - (boxWidthBig / 2);
		bttnX[1] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 2) - (boxWidthBig / 2);
		bttnX[2] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 3) - (boxWidthBig / 2);
		bttnX[3] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 4) - (boxWidthBig / 2);
		bttnX[4] = ((Almoxarifado.WIDTH / (bttnX.length + 1)) * 5) - (boxWidthBig / 2);
		
		iconProfile = Almoxarifado.imgManag.getSprite(215, 0, boxWidthBig, boxHeight);
		iconParts = Almoxarifado.imgManag.getSprite(0, 60, boxWidthBig, boxHeight);
		iconAssembly = Almoxarifado.imgManag.getSprite(215, 60, boxWidthBig, boxHeight);
		iconArchive = Almoxarifado.imgManag.getSprite(0, 120, boxWidthBig, boxHeight);
		iconExit = Almoxarifado.imgManag.getSprite(215, 120, boxWidthBig, boxHeight);

		iconProfileActivated = Almoxarifado.imgManag.getSprite(0, 390, boxWidthBig, boxHeight);
		iconPartsActivated = Almoxarifado.imgManag.getSprite(215, 390, boxWidthBig, boxHeight);
		iconAssemblyActivated = Almoxarifado.imgManag.getSprite(0, 450, boxWidthBig, boxHeight);
		iconArchiveActivated = Almoxarifado.imgManag.getSprite(215, 450, boxWidthBig, boxHeight);
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
		
		if(my < bttnY + boxHeight && my > bttnY) {
			if(mx > bttnX[0] && mx < bttnX[0] + boxWidthBig) {
				if(Almoxarifado.type.equals("1")) {
					Almoxarifado.admProfile.reset = true;
				}else {
					Almoxarifado.workProfile.reset = true;
				}
				return 1;
			}else if(mx > bttnX[1] && mx < bttnX[1] + boxWidthBig) {
				return 2;
			}else if(mx > bttnX[2] && mx < bttnX[2] + boxWidthBig) {
				return 3;
			}else if(mx > bttnX[3] && mx < bttnX[3] + boxWidthBig) {
				return 4;
			}else if(mx > bttnX[4] && mx < bttnX[4] + boxWidthBig) {
				return 5;
			}
		}
		
		return -1;
		
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
		
		isOnBigButton(g, bttnX[0], bttnY);
		isOnBigButton(g, bttnX[1], bttnY);
		isOnBigButton(g, bttnX[2], bttnY);
		isOnBigButton(g, bttnX[3], bttnY);
		isOnBigButton(g, bttnX[4], bttnY);
		
	}

}
