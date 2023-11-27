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

	public UserInterface() {
		for(int i = 0; i < 5; i++) {
			bttnX[i] = + i * boxWidth;
			spaceBetween[i] = 128 + 32*i;
		}
		
		iconProfile = Almoxarifado.imgManag.getSprite(384, 0, 128, 64);
		iconParts = Almoxarifado.imgManag.getSprite(256, 0, 128, 64);
		iconAssembly = Almoxarifado.imgManag.getSprite(128, 0, 128, 64);
		iconArchive = Almoxarifado.imgManag.getSprite(0, 0, 128, 64);
		iconExit = Almoxarifado.imgManag.getSprite(512, 0, 128, 64);

		iconProfileActivated = Almoxarifado.imgManag.getSprite(384, 64, 128, 64);
		iconPartsActivated = Almoxarifado.imgManag.getSprite(256, 64, 128, 64);
		iconAssemblyActivated = Almoxarifado.imgManag.getSprite(128, 64, 128, 64);
		iconArchiveActivated = Almoxarifado.imgManag.getSprite(0, 64, 128, 64);
		
	}
	
	public void clearBox(Graphics g) {
		g.setColor(Color.orange);
		g.fillRect(bttnX[0] + 30, bttnY + boxHeight + 12, Almoxarifado.WIDTH - 30*2, 430);
		
		g.setColor(Color.black);
		g.fillRect(bttnX[0]+ 36, bttnY + boxHeight + 18, Almoxarifado.WIDTH - 36*2, 430-12);
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
			g.drawImage(iconAssemblyActivated, spaceBetween[2] + bttnX[2], bttnY, null);
			break;
		case 4:
			g.drawImage(iconArchiveActivated, spaceBetween[3] + bttnX[3], bttnY, null);
			break;
		}
		
	}

}
