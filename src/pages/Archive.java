package pages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Almoxarifado;
import main.UserInterface;

public class Archive {
	
	boolean isOnTheRightState = false;
	
	int boxX = 0;
	int boxY = UserInterface.boxHeight + (UserInterface.bttnY*2) + 50;
	int boxWidth = 150;
	int boxHeight = 200;
	
	public static int scroll = 0;
	int ofsetHeight = 0;
	int maximumHeight = 500;
	
	BufferedImage img = Almoxarifado.imgManag.getProjectImage("ArchiveDefaultImg");

	public Archive() {
		// TODO Auto-generated constructor stub
	}
	
	public void tick() {
		
		if(Almoxarifado.state == 4) {
			isOnTheRightState =  true;
		}else {
			isOnTheRightState =  false;
			ofsetHeight = 0;
		}
		
		if(isOnTheRightState) {
			
			if(scroll > 1) {
				ofsetHeight -= UserInterface.spd;
				scroll = 0;
			}else if(scroll < -1 && ofsetHeight < 0) {
				ofsetHeight += UserInterface.spd;
				scroll = 0;
			}
			
			//System.out.println("ofsetHeight: " + ofsetHeight);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(153, 0, 153));
		
		int auxXPositioner = 1;
		int auxYPositioner = 0;
		for(int i = 0; i < Almoxarifado.quantityArchives; i++) {
			boxX = ((Almoxarifado.WIDTH / 4) * auxXPositioner) - (boxWidth/2);
			
			/*
			System.out.println("\nauxXPositioner: " + auxXPositioner + ", AuxYPositioner: " + auxYPositioner);
			
			System.out.println("X: " + boxX + ", Y: " + boxY + ", Width: " + boxWidth + ", Height: " + boxHeight + "\n");
			System.out.println("auxXPositioner: " + auxXPositioner + ", AuxYPositioner: " + auxYPositioner);
			System.out.println("------------------------------------------------------------------------------------------------------");
			*/
			
			auxXPositioner++;
			
			g.drawImage(img, boxX, boxY  + auxYPositioner + ofsetHeight, boxWidth, boxHeight, null);
			
			if(auxXPositioner == 4) {
				auxXPositioner = 1;
				auxYPositioner += boxHeight*1.5;
			}
		}
		//System.out.println("========================================================================================================");
	}

}
