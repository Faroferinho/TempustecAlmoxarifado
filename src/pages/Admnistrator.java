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
			if(Almoxarifado.mY > (Almoxarifado.HEIGHT - 70 - bttn_changePW.getHeight()) 
			&& Almoxarifado.mY < (Almoxarifado.HEIGHT - 70 - bttn_changePW.getHeight()) + 60) {
				
			}
		}

		editInfo();
	}

	@Override
	public void render(Graphics g) {
		drawUserBasis(g);
		
		g.drawImage(bttn_changePW, (Almoxarifado.WIDTH / 5) * 3 , 200, null);
		g.drawImage(bttn_editInfo, (Almoxarifado.WIDTH / 5) * 3 + 250, 200, null);
		g.drawImage(bttn_addWorker, (Almoxarifado.WIDTH / 5) * 3 , 350, null);
		g.drawImage(bttn_listWorkers, (Almoxarifado.WIDTH / 5) * 3 + 250, 350, null);
		
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 , 200);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 + 250, 200);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 , 350);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 + 250, 350);
	}
}
