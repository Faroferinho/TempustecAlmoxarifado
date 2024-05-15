package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import functions.BidimensionalList;
import functions.DBConector;
import functions.Functions;
import functions.Searcher;
import main.Almoxarifado;
import main.UserInterface;

public class Archive implements BidimensionalList{
	
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
	int initialImgY = 225;
	int auxHeight = 0;
	
	String columnsOrder[] = {"IDs" , "O.S.", "Datas", "Usuários"};
	ArrayList<String> ids = new ArrayList<>();
	ArrayList<String> names = new ArrayList<>();
	ArrayList<String> images = new ArrayList<>();
	ArrayList<Integer> quantities = new ArrayList<>();
	ArrayList<String> dates = new ArrayList<>();
	ArrayList<String> RdFs = new ArrayList<>();
		
	BufferedImage img = Almoxarifado.imgManag.getProjectImage("ArquivoBeta");
	BufferedImage restoreButton = Almoxarifado.imgManag.getSprite(475, 540, 165, 60);
	
	public String orderColumn = "IDs";

	public Archive() {
		System.out.println("Carregou Arquivo: " + LocalDateTime.now());
	}
	
	void restoreAssembly(int ID) {
		mouseStatus = false;
		
		int confirmation = JOptionPane.showConfirmDialog(null, "Realmente deseja restaurar esse projeto?", "Confirmar Restauração", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
		
		if(confirmation == 0) {
			DBConector.restoreArchived("" + ID);
		}
		
		System.out.println("===================================================");
		System.out.println("Quantidade de Arquivos: " + Almoxarifado.quantityArchives);
		System.out.println("Quantidade de Montagens: " + Almoxarifado.quantityAssembly);
		System.out.println("Quantidade de Peças: " + Almoxarifado.quantityParts);
		System.out.println("---------------------------------------------------");
		
		Almoxarifado.quantityArchives = DBConector.counterOfElements("Arquivo");
		Almoxarifado.quantityAssembly = DBConector.counterOfElements("Montagem");
		Almoxarifado.quantityParts = DBConector.counterOfElements("Pecas");

		System.out.println("Quantidade de Arquivos: " + Almoxarifado.quantityArchives);
		System.out.println("Quantidade de Montagens: " + Almoxarifado.quantityAssembly);
		System.out.println("Quantidade de Peças: " + Almoxarifado.quantityParts);
		System.out.println("===================================================");
		
		restart = true;
		ProjectList.updateProjectList = true;
		ProjectList.updateProjectListPL = true;
		PartsList.wasChanged = true;
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
			
			if(restart) {
				maximumHeight = ((Almoxarifado.quantityArchives - 2) * 250);
				thumbHeight = (int) ((UserInterface.maximunHeight - 16) - (((UserInterface.maximunHeight - 32) * (maximumHeight / 16)) / 100));
				System.out.println("thumbHeight: " + thumbHeight);
				
				infoGathered = "";
				infoGathered = DBConector.readDB(Searcher.orderByColumn(orderColumn, "Arquivo"));
				Almoxarifado.frame.setTitle("Almoxarifado - Lista de Projetos Arquivados");
				
				infoGathered = infoGathered.replace("\n", "");
				String auxInfo[] = infoGathered.split(" § ");
				
				ids.clear();
				quantities.clear();
				names.clear();
				images.clear();
				dates.clear();
				RdFs.clear();
								
				for(int swepper = 0; swepper < auxInfo.length; swepper++) {
					switch(swepper % 9) {
					case 0:
						ids.add(auxInfo[swepper]);
						break;
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
				
			
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("segoi ui", 1, 40));
		g.drawString("Projetos Arquivados: ", 100, 180 + offsetHeight);
		
		int positionerX = 200 + g.getFontMetrics().stringWidth("Projetos Arquivados: ");
		for(int i = 0; i < columnsOrder.length; i++) {
			
			g.setColor(Color.orange);
			g.setFont(new Font("segoe ui", 1, 12));
			
			if(i != 0) {
				positionerX += g.getFontMetrics().stringWidth(columnsOrder[i - 1]) + 80;
			}
			
			if(Functions.isOnBox(positionerX, 180 + offsetHeight - g.getFontMetrics().getHeight(), g.getFontMetrics().stringWidth(columnsOrder[i]), g.getFontMetrics().getHeight())) {
				g.setColor(new Color(255, 00, 102));
				if(mouseStatus) {
					orderColumn = getColumn(i);
					restart = true;
					mouseStatus = false;
				}
			}
			
			g.drawString(columnsOrder[i], positionerX, 180 + offsetHeight);
		}
		
		for(int i = 0; i < names.size(); i++) {
			g.setColor(Color.white);
			g.drawImage(img, imgX, initialImgY + auxHeight + offsetHeight, null);
			
			g.setFont(new Font("segoi ui", 1, 17));
			g.drawString(names.get(i), imgX + 170, initialImgY + 25 + offsetHeight + auxHeight);
			
			g.setFont(new Font("segoi ui", 0, 14));
			g.drawString(dates.get(i), imgX + 170, initialImgY + 60 + offsetHeight + auxHeight);
			g.drawString("Quantidade de Peças: " + quantities.get(i), imgX + 170, initialImgY + 95 + offsetHeight + auxHeight);
			g.drawString("Registro do Arquivador: " + RdFs.get(i), imgX + 170, initialImgY + 130 + offsetHeight + auxHeight);
			
			g.drawImage(restoreButton, Almoxarifado.WIDTH - 75 - restoreButton.getWidth(), initialImgY + 60 + offsetHeight + auxHeight, null);
			UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH - 75 - restoreButton.getWidth(), initialImgY + 60 + offsetHeight + auxHeight);
			
			if(mouseStatus) {
				if(Functions.isOnBox(Almoxarifado.WIDTH - 75 - restoreButton.getWidth(), initialImgY + 60 + offsetHeight + auxHeight, 165, 60)) {
					restoreAssembly(Integer.parseInt(ids.get(i)));
				}
			}
			
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

	@Override
	public String getColumn(int i) {
		return columnsOrder[i];
	}
		
}
