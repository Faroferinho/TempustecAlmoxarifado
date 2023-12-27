package pages;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Almoxarifado;

public class Employee extends Profile{

	public BufferedImage editButton;
	public BufferedImage doneButton;
	public BufferedImage passwordButton;

	
	public Employee(String Name, String RdF, String CPF) {
		super(Name, RdF, CPF);
		
		editButton = Almoxarifado.imgManag.getSprite(128, 128, 128, 64);
		doneButton = Almoxarifado.imgManag.getSprite(128, 128 + 64, 128, 64);
		passwordButton = Almoxarifado.imgManag.getSprite(64*6, 64*2, 128, 64);
	}
	
	public void tick() {	
		if(reset) {
			reset = false;
			isEditing = false;
		}
		
		if(Almoxarifado.state == 1) {
			isOnTheRightState = true;
			Almoxarifado.frame.setTitle("Perfil de " + Profile.name);
		}else {
			reset = true;
			isOnTheRightState = false;
			
		}
		
		if(isOnTheRightState) {
			if(mouseStatus == true) {
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
					}else {
						isEditing = false;
					}
					break;
				case 2:
					editInfo(4);
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

				g.drawImage(editButton, Almoxarifado.WIDTH / 4 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(passwordButton, Almoxarifado.WIDTH / 4*3 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				
			}else if(isEditing == true){
				firstRendering(g);
				
				g.drawImage(doneButton, Almoxarifado.WIDTH / 4 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(passwordButton, Almoxarifado.WIDTH / 4*3 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
			}
		}
	}
}
