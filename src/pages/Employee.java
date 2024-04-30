package pages;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import functions.Archiver;
import main.Almoxarifado;
import main.UserInterface;

public class Employee extends Profile{

	public BufferedImage editButton;
	public BufferedImage doneButton;
	public BufferedImage passwordButton;

	
	public Employee(String Name, String RdF, String CPF) {
		super(Name, RdF, CPF);
		
		editButton = Almoxarifado.imgManag.getSprite(475, 0, 165, 60);
		doneButton = Almoxarifado.imgManag.getSprite(0, 510, 165, 60);
		passwordButton = Almoxarifado.imgManag.getSprite(475, 120, 165, 60);
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
			if(mouseStatus) {
				switch(buttonClick(Almoxarifado.mX, Almoxarifado.mY, false)) {
				case 1:
					if(isEditing == false) {
						Archiver.writeOnArchive("edicao1", "", "", "");
						isEditing = true;					
					}else {
						Archiver.writeOnArchive("edicao2", "", "", "");
						isEditing = false;
					}
					break;
				case 2:
					editInfo(4);
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

				g.drawImage(editButton, (Almoxarifado.WIDTH/4) - (editButton.getWidth()/2), Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(passwordButton, (Almoxarifado.WIDTH/4)*3 - (editButton.getWidth()/2), Almoxarifado.HEIGHT/2 + 120, null);
				UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH/4) - (editButton.getWidth()/2), Almoxarifado.HEIGHT / 2 + 120);
				UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH/4)*3 - (editButton.getWidth()/2), Almoxarifado.HEIGHT/2 + 120);
				
			}else if(isEditing == true){
				firstRendering(g);
				
				g.drawImage(doneButton, (Almoxarifado.WIDTH/4) - (editButton.getWidth()/2), Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(passwordButton, (Almoxarifado.WIDTH/4)*3 - (editButton.getWidth()/2), Almoxarifado.HEIGHT/2 + 120, null);
				UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH/4) - (editButton.getWidth()/2), Almoxarifado.HEIGHT / 2 + 120);
				UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH/4)*3 - (editButton.getWidth()/2), Almoxarifado.HEIGHT/2 + 120);
			}
		}
	}
}
