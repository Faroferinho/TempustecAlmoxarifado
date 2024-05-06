package pages;

import java.awt.Graphics;

import main.Almoxarifado;
import main.UserInterface;

public class Employee extends Profile{
	
	public Employee(String RdF){
		super(RdF);
	}

	@Override
	public void tick() {
		if(mouseStatus) {
			if(Almoxarifado.mY > (Almoxarifado.HEIGHT - 70 - bttn_changePW.getHeight()) 
			&& Almoxarifado.mY < (Almoxarifado.HEIGHT - 70 - bttn_changePW.getHeight()) + 60) {
				if(Almoxarifado.mX > (Almoxarifado.WIDTH/3) - (bttn_changePW.getWidth() / 2) 
				&& Almoxarifado.mX < (Almoxarifado.WIDTH/3) - (bttn_changePW.getWidth() / 2) + 165) {
					changePassword();
					mouseStatus = false;
				}else if(Almoxarifado.mX > (Almoxarifado.WIDTH/3)*2 - (bttn_changePW.getWidth() / 2) 
				&& Almoxarifado.mX < (Almoxarifado.WIDTH/3)*2 - (bttn_changePW.getWidth() / 2) + 165) {
					if(!isEditing) {						
						isEditing = true;					
						mouseStatus = false;
					}else {
						isEditing = false;					
						mouseStatus = false;
					}
				}
			}
		}
		
		editInfo();
	}
					

	@Override
	public void render(Graphics g) {
		drawUserBasis(g);
		
		g.drawImage(bttn_changePW, (Almoxarifado.WIDTH/3) - (bttn_changePW.getWidth() / 2), Almoxarifado.HEIGHT - 70 - bttn_changePW.getHeight(), null);
		g.drawImage(bttn_editInfo, (Almoxarifado.WIDTH/3)*2 - (bttn_changePW.getWidth() / 2), Almoxarifado.HEIGHT - 70 - bttn_changePW.getHeight(), null);
		
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH/3) - (bttn_changePW.getWidth() / 2), Almoxarifado.HEIGHT - 70 - bttn_changePW.getHeight());
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH/3)*2 - (bttn_changePW.getWidth() / 2), Almoxarifado.HEIGHT - 70 - bttn_changePW.getHeight());
	}
}