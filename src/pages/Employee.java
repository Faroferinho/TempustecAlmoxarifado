package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Almoxarifado;

public class Employee extends Profile{

	
	
	public Employee(String Name, String RdF, String CPF) {
		super(Name, RdF, CPF);
		
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
				g.setColor(Color.darkGray);
				g.fillRect(Almoxarifado.WIDTH / 2 - 125, Almoxarifado.HEIGHT / 2 + 120, 250, 60);
				g.setColor(Color.white);
				g.setFont(new Font("arial", 1, 22));
				g.drawString("Editar Perfil", Almoxarifado.WIDTH / 2 - g.getFontMetrics().stringWidth("Editar Perfil") / 2, Almoxarifado.HEIGHT/ 2 + 157);
				
			}else if(isEditing == true){
				firstRendering(g);
				g.setColor(Color.yellow);
				g.fillRect(Almoxarifado.WIDTH / 2 - 125, Almoxarifado.HEIGHT / 2 + 120, 250, 60);
				g.setColor(Color.white);
				g.setFont(new Font("arial", 1, 22));
				g.drawString("Concluir Edição", Almoxarifado.WIDTH / 2 - g.getFontMetrics().stringWidth("Concluir Edição") / 2, Almoxarifado.HEIGHT/ 2 + 157);
				
			}
		}
	}
}
