package pages;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Almoxarifado;

public class Employee extends Profile{

	public BufferedImage editButton;
	public BufferedImage editDoneButton;
	
	public Employee(String Name, String RdF, String CPF) {
		super(Name, RdF, CPF);
		
		editButton = Almoxarifado.imgManag.getSprite(128, 128, 128, 64);
		editDoneButton = Almoxarifado.imgManag.getSprite(128, 128 + 64, 128, 64);
	}
	
	public void tick() {	
		if(reset) {
			reset = false;
			isEditing = false;
		}
		
		if(Almoxarifado.state == 1) {
			isOnTheRightState = true;
		}else {
			reset = true;
			isOnTheRightState = false;
			
		}
		
		if(isOnTheRightState) {
			if(mouseStatus == true) {
				//System.out.println("Mouse Status = " + mouseStatus);
				mouseAuxRun = true;
				mouseAuxEdit = true;
				mouseStatus = false;
			}else {
				mouseAuxRun = false;
				mouseAuxEdit = false;
				mouseAuxSign = false;
			}
			
			if(mouseAuxRun) {
				switch(buttonClick(Almoxarifado.mX, Almoxarifado.mY, false)) {
				case 1:
					if(isEditing == false) {
						isEditing = true;
						System.out.println("Está Editando");
						
					}else {
						isEditing = false;
					}
					break;
				case 2:
					
					break;
				case 3:
					
				default:
					break;
				}
			}
			
			if(isEditing) {
				changeInformation(Almoxarifado.mX, Almoxarifado.mY, false);
				
			}
		}
	}
	
	public void render(Graphics g) {
		
		if(isOnTheRightState) {
		
			if(isEditing == false) {
				firstRendering(g);

				
				g.drawImage(editButton, Almoxarifado.WIDTH / 2 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				
			}else if(isEditing == true){
				firstRendering(g);
				
				
				g.drawImage(editDoneButton, Almoxarifado.WIDTH / 2 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				
			}
		}
	}
}
