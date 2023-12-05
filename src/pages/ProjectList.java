package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Almoxarifado;
import main.UserInterface;

public class ProjectList {
	
	static int imgX;
	static int imgY;
	static int spaceBetween = Almoxarifado.WIDTH/8;
	static int boxWidth = 150;
	static int boxHeight = 200;
	int initX =  (int) (Almoxarifado.WIDTH/2 - (boxWidth * 1.5 + spaceBetween));
	int initY = UserInterface.bttnY*2 + UserInterface.boxWidth + 64;
	
	public static int scroll;
	private static int ofsetHeight;
	
	BufferedImage img = Almoxarifado.imgManag.getSprite(256, 192, boxWidth, boxHeight);
	
	public ProjectList(){
		
	}
	
	public void tick() {
		//System.out.println("Entrou no Tick()");
		if(scroll > 1) {
			System.out.println("Scroll pra baixo, ofsetHeight: " + ofsetHeight);
			ofsetHeight -= UserInterface.spd;
			scroll = 0;
		}else if(scroll < -1 && ofsetHeight < 0) {
			System.out.println("Scroll pra cima, ofsetHeight: " + ofsetHeight);
			ofsetHeight += UserInterface.spd;
			scroll = 0;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("calibri", 0, 40));
		g.drawString("Lista de Projetos: ", Almoxarifado.WIDTH/8 - 30, initY - (25 + 64) + ofsetHeight);
		
		g.setColor(Color.blue);
		
		int auxX = 0;
		int auxY = 0;
		for(int i = 0; i < 9; i++) {
			imgX = initX + (spaceBetween + boxWidth) * auxX;
			imgY = initY + auxY + ofsetHeight;
			
			auxX++;
			
			if(auxX == 3) {
				auxX = 0;
				auxY += boxHeight + spaceBetween;
				//System.out.println("Index: " + i);
			}
			
			g.fillRect(imgX, imgY, boxWidth, boxHeight);
			g.drawImage(img, imgX, imgY, boxWidth, boxHeight, null);
		}
	}

}
