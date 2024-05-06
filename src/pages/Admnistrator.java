package pages;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Almoxarifado;
import main.UserInterface;

public class Admnistrator extends Profile {
	
	protected BufferedImage bttn_listWorkers = Almoxarifado.imgManag.getSprite(475, 60, 165, 60);
	protected BufferedImage bttn_addWorker = Almoxarifado.imgManag.getSprite(475, 300, 165, 60);
	
	int scroll;
	
	public Admnistrator(String RdF){
		super(RdF);	
	}

	@Override
	public void tick() {
		if(mouseStatus) {
			if(Almoxarifado.mY > 200
			&& Almoxarifado.mY < 200 + bttn_changePW.getHeight()) {
				
				if(Almoxarifado.mX > (Almoxarifado.WIDTH / 5) * 3
				&& Almoxarifado.mX < (Almoxarifado.WIDTH / 5) * 3 + bttn_changePW.getWidth()) {
					
					changePassword();
					
				}else if(Almoxarifado.mX > (Almoxarifado.WIDTH / 5) * 3 + 250
					  && Almoxarifado.mX < (Almoxarifado.WIDTH / 5) * 3 + 250 + bttn_changePW.getWidth()) {
					
					if(!isEditing) {						
						isEditing = true;					
						mouseStatus = false;
					}else {
						isEditing = false;					
						mouseStatus = false;
					}
					
				}
				
			} else if(Almoxarifado.mY > 350 
				   && Almoxarifado.mY < 350 + bttn_addWorker.getHeight()) {
				
				if(Almoxarifado.mX > (Almoxarifado.WIDTH / 5) * 3
				&& Almoxarifado.mX < (Almoxarifado.WIDTH / 5) * 3 + bttn_changePW.getWidth()) {
					
					
					
				}else if(Almoxarifado.mX > (Almoxarifado.WIDTH / 5) * 3 + 250
					  && Almoxarifado.mX < (Almoxarifado.WIDTH / 5) * 3 + 250 + bttn_changePW.getWidth()) {
					
				}
				
			}
		}

		editInfo();
	}

	@Override
	public void render(Graphics g) {
		drawUserBasis(g);
		
		g.drawImage(bttn_changePW, (Almoxarifado.WIDTH / 5) * 3, 200, null);
		g.drawImage(bttn_editInfo, (Almoxarifado.WIDTH / 5) * 3 + 250, 200, null);
		g.drawImage(bttn_addWorker, (Almoxarifado.WIDTH / 5) * 3 , 350, null);
		g.drawImage(bttn_listWorkers, (Almoxarifado.WIDTH / 5) * 3 + 250, 350, null);
		
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 , 200);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 + 250, 200);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 , 350);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 + 250, 350);
	}
}
