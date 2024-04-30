package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.ArrayList;

import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class Archive {
	
	boolean isOnTheRightState = false;
	
	public boolean mouseStatus = false;
	
	public static int scroll = 0;
	public int offsetHeight = 0;
	public int maximumHeight = 0;
	
	boolean toggleScrollBar = false;
	int thumbWidth = 18;
	public int thumbHeight = 0;
	double thumbAuxY = 0;
	public boolean isDragging = false;
	
	public boolean restart = true;
	
	public String infoGathered = "";
	
	int imgX = 75;
	int initialImgY = 175;
	int auxHeight = 0;

	ArrayList<String> names = new ArrayList<>();
	
	ArrayList<String> images = new ArrayList<>();
	
	ArrayList<Integer> quantities = new ArrayList<>();
	
	ArrayList<String> dates = new ArrayList<>();
	
	ArrayList<String> RdFs = new ArrayList<>();
		
	BufferedImage img = Almoxarifado.imgManag.getProjectImage("ArquivoBeta");

	public Archive() {
		System.out.println("Carregou Arquivo: " + LocalDateTime.now());
	}
	
	void restoreAssemby(int ID) {
		
	}
	
	public void scrollPositioner() {
		if(offsetHeight < (maximumHeight * -1)) {
			offsetHeight = maximumHeight * -1;
		}else if(offsetHeight > 0){
			offsetHeight = 0;
		}
		
		int Y = (UserInterface.maximunHeight - 18) - thumbHeight;
		double S = (Double.parseDouble("" + maximumHeight) / Double.parseDouble("" + offsetHeight));
		thumbAuxY = Y / S;
	}
	
	public void tick() {
		
		if(Almoxarifado.state == 4) {
			isOnTheRightState =  true;
		}else {
			isOnTheRightState =  false;
		}
		
		if(isOnTheRightState) {
			
			if(scroll > 0 && offsetHeight > (maximumHeight * -1)) {
				offsetHeight -= UserInterface.spd;
				
				scrollPositioner();
				
				scroll = 0;
			}else if(scroll < 0 && offsetHeight < 0){
				offsetHeight += UserInterface.spd;
				
				scrollPositioner();
				
				scroll = 0;
			}
			
			if(maximumHeight > 550) {
				toggleScrollBar = true;
			}else {
				toggleScrollBar = false;
			}
			
			if(mouseStatus) {
				if(Almoxarifado.mX > Almoxarifado.WIDTH - (36 + 21) && Almoxarifado.mX < Almoxarifado.WIDTH - (36 + 21) + 20
				&& Almoxarifado.mY > UserInterface.bttnY + UserInterface.boxHeight + 20 - (int) (thumbAuxY) 
				&& Almoxarifado.mY < UserInterface.bttnY + UserInterface.boxHeight + 20 - (int) (thumbAuxY) + thumbHeight) {
					isDragging = true;
				}
			}else {
				isDragging = false;
			}
				
			if(restart) {
				maximumHeight = ((Almoxarifado.quantityArchives - 2) * 250);
				thumbHeight = (int) ((UserInterface.maximunHeight - 16) - (((UserInterface.maximunHeight - 32) * (maximumHeight / 16)) / 100));
				System.out.println("thumbHeight: " + thumbHeight);
				
				infoGathered = "";
				infoGathered = DBConector.readDB("*", "Arquivo");
				Almoxarifado.frame.setTitle("Arquivo de Projetos");
				
				infoGathered = infoGathered.replace("\n", "");
				String auxInfo[] = infoGathered.split(" § "); 
								
				for(int swepper = 0; swepper < auxInfo.length; swepper++) {
					switch(swepper % 9) {
					case 1:
						//ID - Quantidade;
						quantities.add(DBConector.readDB("ID_Archive_Parts", "Arquivo_Pecas", "Montagem", auxInfo[swepper]).split(" § \n").length - 1);
						break;
					case 2:
						//ISOs
						names.add(auxInfo[swepper]);
						break;
					case 5:
						//Imagens
						images.add(auxInfo[swepper]);
						break;
					case 7:
						//Datas
						dates.add(auxInfo[swepper]);
						break;
					case 8:
						//RdF dos Arquivadores
						RdFs.add(auxInfo[swepper]);
						break;
					}
				}
				
				restart = false;
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("segoi ui", 1, 40));
		g.drawString("Projetos Arquivados: ", 100, 150 + offsetHeight);
		
		for(int i = 0; i < names.size(); i++) {
			g.drawImage(img, imgX, initialImgY + auxHeight + offsetHeight, null);
			
			g.setFont(new Font("segoi ui", 1, 17));
			g.drawString(names.get(i), imgX + 170, initialImgY + 25 + offsetHeight + auxHeight);
			
			g.setFont(new Font("segoi ui", 0, 14));
			g.drawString(dates.get(i), imgX + 170, initialImgY + 60 + offsetHeight + auxHeight);
			g.drawString("Quantidade de Peças: " + quantities.get(i), imgX + 170, initialImgY + 95 + offsetHeight + auxHeight);
			g.drawString("Registro do Arquivador: " + RdFs.get(i), imgX + 170, initialImgY + 130 + offsetHeight + auxHeight);
			
			auxHeight += 250;
		}
		
		auxHeight = 0;
		
		if(toggleScrollBar) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(Almoxarifado.WIDTH - (36 + 22), UserInterface.bttnY + UserInterface.boxHeight + 18, 20, UserInterface.maximunHeight);
			
			g.setColor(Color.gray);
			if(isDragging) {
				g.setColor(Color.LIGHT_GRAY);
			}
			g.fillRect(Almoxarifado.WIDTH - (36 + 21), UserInterface.bttnY + UserInterface.boxHeight + 20 - (int) (thumbAuxY), thumbWidth, thumbHeight);
		}
		
	}
		
}
