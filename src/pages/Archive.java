package pages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import functions.DBConector;
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
	
	public boolean mouseStatus = false;
	
	
	public boolean restart = true;
	
	public String infoGathered = "";
	
	
	BufferedImage img = Almoxarifado.imgManag.getProjectImage("ArchiveDefaultImg");

	public Archive() {
	}
	
	void restoreAssemby(int ID) {
		
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
			
			if(restart) {
				infoGathered = "";
				infoGathered = DBConector.readDB("*", "Arquivo");
				Almoxarifado.frame.setTitle("Arquivo de Projetos");
				
				restart = false;
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(153, 0, 153));
		
		int auxXPositioner = 1;
		int auxYPositioner = 0;
		for(int i = 0; i < Almoxarifado.quantityArchives; i++) {
			boxX = ((Almoxarifado.WIDTH / 4) * auxXPositioner) - (boxWidth/2);
			
			auxXPositioner++;
			
			
			if(Almoxarifado.mY > UserInterface.bttnY*2 + UserInterface.boxHeight) {
				if(Almoxarifado.mX > boxX && Almoxarifado.mX < boxX + boxWidth
				&& Almoxarifado.mY > boxY + auxYPositioner + ofsetHeight && Almoxarifado.mY < boxY + auxYPositioner + ofsetHeight + boxHeight) {
					if(mouseStatus) {
						int confirmation = 0;
						confirmation = JOptionPane.showConfirmDialog(null, "Deseja restaurar este arquivo?", "Restauração Iminente", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if(confirmation == 0) {
							JOptionPane.showMessageDialog(null, "Operação Concluida", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
							restoreAssemby(i + 1);
						}else if(confirmation == 1) {
							JOptionPane.showMessageDialog(null, "Cancelamento", "Falha na Operação", JOptionPane.ERROR_MESSAGE);
						}
						mouseStatus = false;
					}
				}
			}
				
			
			g.drawString("Indice de clique: " + i, boxX, boxY  + auxYPositioner + ofsetHeight);
						
			g.drawImage(img, boxX, boxY  + auxYPositioner + ofsetHeight, boxWidth, boxHeight, null);
			
			if(auxXPositioner == 4) {
				auxXPositioner = 1;
				auxYPositioner += boxHeight*1.5;
			}
		}
	}
}
