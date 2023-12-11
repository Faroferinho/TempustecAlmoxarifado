package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class UserInterface {
	
	public static int bttnX[] = new int[5];
	public static int bttnY = 30;
	public static int spaceBetween[] = new int[5];
	
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
		for(int i = 0; i < 5; i++) {
			bttnX[i] = + i * boxWidth;
			spaceBetween[i] = 128 + 32*i;
		}
		
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
		g.fillRect(bttnX[0] + 30, bttnY + boxHeight + 12, Almoxarifado.WIDTH - 30*2, maximunHeight);
		
		g.setColor(Color.black);
		g.fillRect(bttnX[0]+ 36, bttnY + boxHeight + 18, Almoxarifado.WIDTH - 36*2, maximunHeight-12);
	}
	
	public void limitScrollToWorkspaceArea(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Almoxarifado.WIDTH, 10);
		g.fillRect(0, Almoxarifado.HEIGHT - 10, Almoxarifado.WIDTH, 10);
		
		g.setColor(Color.lightGray);
		g.fillRect(10, 10, Almoxarifado.WIDTH-20, 20);
		g.fillRect(10, Almoxarifado.HEIGHT-20, Almoxarifado.WIDTH-20, 10);
		
		g.setColor(Color.gray);
		g.fillRect(20, 20, Almoxarifado.WIDTH-40, 25 + boxHeight);
		g.fillRect(20, bttnY + boxHeight + 442, Almoxarifado.WIDTH-40, 20);
		
		g.setColor(Color.orange);
		g.fillRect(bttnX[0] + 30, bttnY + boxHeight + 12, Almoxarifado.WIDTH - 30*2, 6);
		g.fillRect(bttnX[0] + 30, bttnY + boxHeight + 436, Almoxarifado.WIDTH - 30*2, 6);
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
			//System.out.println("Mouse Clicado UI");
			checkMouse();
			Almoxarifado.mPressed = false;
		}
	}
	
	public void render(Graphics g) {
		//TODO: Criar eventos Customizados para mudar a imagem com Interface;
		g.drawImage(iconProfile, spaceBetween[0] + bttnX[0], bttnY, null);
		g.drawImage(iconParts, spaceBetween[1] + bttnX[1], bttnY, null);
		g.drawImage(iconAssembly, spaceBetween[2] + bttnX[2], bttnY, null);
		g.drawImage(iconArchive, spaceBetween[3] + bttnX[3], bttnY, null);
		g.drawImage(iconExit, spaceBetween[4] + bttnX[4], bttnY, null);
		
		switch(Almoxarifado.state) {
		case 1:
			g.drawImage(iconProfileActivated, spaceBetween[0] + bttnX[0], bttnY, null);
			break;
		case 2:
			g.drawImage(iconPartsActivated, spaceBetween[1] + bttnX[1], bttnY, null);
			break;
		case 3:
		case 5:
			g.drawImage(iconAssemblyActivated, spaceBetween[2] + bttnX[2], bttnY, null);
			break;
		case 4:
			g.drawImage(iconArchiveActivated, spaceBetween[3] + bttnX[3], bttnY, null);
			break;
		}
		
		isOnButton(g, spaceBetween[0] + bttnX[0], bttnY);
		isOnButton(g, spaceBetween[1] + bttnX[1], bttnY);
		isOnButton(g, spaceBetween[2] + bttnX[2], bttnY);
		isOnButton(g, spaceBetween[3] + bttnX[3], bttnY);
		isOnButton(g, spaceBetween[4] + bttnX[4], bttnY);
		
	}

}
