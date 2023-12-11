package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class Project {
	
	static int ID = 1;
	static String name = "";
	static String description = "";
	static String company = "";
	static String imgAdress = "";
	
	private String rawPartsList = "";
	private String[][] brokenApartPartsList;
	
	BufferedImage img;
	BufferedImage editProfile = Almoxarifado.imgManag.getSprite(128, 64*2, 128, 64);
	BufferedImage archiveProfile = Almoxarifado.imgManag.getSprite(0, 64*4, 128, 64);
	
	double price = 0.0;
	int partsList[];
	
	public static boolean updateProject = true;
	private boolean isOnTheRightState = false;
	
	public static int scroll;
	private int ofsetHeight = 0;
	
	public boolean click = false;
	
	int imgX = UserInterface.bttnX[0] + 50;
	int imgY = Almoxarifado.HEIGHT - UserInterface.maximunHeight - 20;
	
	public Project() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void tick() {
		
		if(Almoxarifado.state == 5) {
			isOnTheRightState = true;
		}else {
			isOnTheRightState = false;
		}
		
		if(updateProject) {
			String brokenApartInfo[];
			System.out.println("Atualizando a Pagina de Projeto");
			String aux = DBConector.findInDB("*", "montagem", "ID_Montagem", "" + ID);
			System.out.println("aux: " + aux);
			
			brokenApartInfo = aux.split(" § ");
			
			name = brokenApartInfo[1];
			description = brokenApartInfo[2];
			company = brokenApartInfo[3];
			imgAdress = brokenApartInfo[4];
			
			if(imgAdress.equals(null) || imgAdress.equals("null") || imgAdress.equals("")) {
				System.out.println("a imagem está vazia");
				imgAdress = "ProjetoBetaImg";
			}else {
				System.out.println("O nome do arquivo de imagem é: " + imgAdress);
			}
			
			img = Almoxarifado.imgManag.getProjectImage(imgAdress);
			
			rawPartsList = DBConector.findInDB("*", "Pecas", "Montagem", "" + ID);
			System.out.println("rawPartsList: \n" + rawPartsList);
			
			
			brokenApartPartsList = breakingList(rawPartsList);
			
			updateProject = false;
		}
		
		if(isOnTheRightState) {
			if(scroll > 0) {
				ofsetHeight -= UserInterface.spd;
				scroll = 0;
			}else if(scroll < 0 && ofsetHeight < 0) {
				ofsetHeight += UserInterface.spd;
				scroll = 0;
			}
		}
	}
	
	public String[][] breakingList(String toSplit){
		String linesToBreakdown[] = toSplit.split("\n");
		String returnString[][] = new String[linesToBreakdown.length][8];
		
		returnString[0][0] = "ID";
		returnString[0][1] = "Montagem";
		returnString[0][2] = "Descrição";
		returnString[0][3] = "Quantidade";
		returnString[0][4] = "";
		returnString[0][5] = "Preço";
		returnString[0][6] = "Fornecedor";
		returnString[0][7] = "Status";
		
		for(int i = 0; i < linesToBreakdown.length-1; i++) {
			returnString[i+1] = linesToBreakdown[i].split(" § ");
		}
		return returnString;
	}
	
	public String translateText(String toTranslate, int index) {
		String toReturn = null;
		
		if(index == 1) {
			toReturn = name;
		}else {
			toReturn = Almoxarifado.partsList.quantityTypes[Integer.parseInt(toTranslate)];
		}
		
		return toReturn;
		
	}
	
	public void drawPartsList(Graphics g) {
		int nextX = 0;
		int nextY = 0;
		
		int textInitPositX = (imgX + 50);
		int textInitPositY = img.getHeight() + imgY + 18*2;
		int auxWidth = 0;
		int auxHeight = 30;
		int sizeOfFont = auxHeight/2;
		int total = (Almoxarifado.WIDTH - (textInitPositX*2));
		
		Color nC;
		g.setFont(new Font("arial", 0, sizeOfFont));
		
		//System.out.println((brokenApartPartsList.length*brokenApartPartsList[0].length));
		for(int i = 0; i < (brokenApartPartsList.length*brokenApartPartsList[0].length); i++) {
			//System.out.println(brokenApartPartsList[0][nextX] + " no indice " + (nextY) + " é: " + brokenApartPartsList[nextY][nextX]);
			
			if(i < 8){
				nC = Color.orange;
			}else {
				nC = Color.white;
			}
			
			switch(brokenApartPartsList[0][nextX]) {
			case "Montagem":
				//System.out.println("1");
				auxWidth += (total*3.8)/100;
				
				//System.out.println("1, AuxWidth: " + auxWidth);
				break;
			case "Descrição":
				//System.out.println("2");
				auxWidth += (total*13.9)/100;
				//System.out.println("2, AuxWidth: " + auxWidth);
				break;
			case "Quantidade":
				//System.out.println("3");
				auxWidth += (total*33.2)/100;
				//System.out.println("3, AuxWidth: " + auxWidth);
				break;
			case "":
				//System.out.println("4, AuxWidth: " + auxWidth);
				if(nextY > 0) {
					auxWidth += g.getFontMetrics().stringWidth(" " + brokenApartPartsList[nextY][nextX-1]);
				}else {
					auxWidth += (total*9.5)/100;
				}
				break;
			case "Preço":
				//System.out.println("5");
				if(nextY > 0) {
					auxWidth -= g.getFontMetrics().stringWidth(" " + brokenApartPartsList[nextY][nextX-2]);
					auxWidth += ((total*9.5)/100)*2;
				}else {
					auxWidth += (total*9.5)/100;
				}
				//System.out.println("5, AuxWidth: " + auxWidth);
				break;
			case "Fornecedor":
				//System.out.println("6");
				auxWidth += (total*11.8)/100;
				//System.out.println("6, AuxWidth: " + auxWidth);
				break;
			case "Status":
				//System.out.println("7");
				auxWidth += (total*14.6)/100;
				//System.out.println("7, AuxWidth: " + auxWidth);
				break;
			}
			
			String toDrawString = brokenApartPartsList[nextY][nextX];
			if(nextY > 0 && nextX > 0) {
				if(nextX == 1 || nextX == 4) {
					toDrawString = translateText(brokenApartPartsList[nextY][nextX], nextX);
				}
				if(Almoxarifado.mX > textInitPositX + auxWidth - 5 
						&& Almoxarifado.mX < textInitPositX + auxWidth + g.getFontMetrics().stringWidth(toDrawString) + 5) {
					if(Almoxarifado.mY > textInitPositY + auxHeight + ofsetHeight - g.getFontMetrics().getHeight() - 2 
							&& Almoxarifado.mY < textInitPositY + auxHeight + ofsetHeight + 2) {
						nC = Color.red;
						if(click) {
							//System.out.println("Clique em: " + toDrawString);
							PartsList.changePart(brokenApartPartsList[nextY][0], nextX);
							updateProject = true;
							click = false;
						}
					}
				}
				
				
			}
			
			g.setColor(nC);
			
			g.drawString(toDrawString, textInitPositX + auxWidth, textInitPositY + auxHeight + ofsetHeight);
			
			nextX++;
			
			if(nextX == 8) {
				//System.out.println("================================================================================");
				nextY += 1;
				nextX = 0;
				auxWidth = 0;
				auxHeight += 20;
			}
			
		}
		
		
		//System.out.println("Saiu do Loop");
	}
	
	public void render(Graphics g) {
		if(!updateProject) {
			g.setColor(Color.white);
			g.setFont(new Font("arial", 0, 20));
			
			g.drawImage(img, imgX, imgY + ofsetHeight, null);
			g.drawString(name, imgX + 15 + img.getWidth(), imgY + 20 + ofsetHeight);
			g.drawString(company, imgX + 15 + img.getWidth(), imgY + 80 + ofsetHeight);
			g.drawString(description, imgX + 15 + img.getWidth(), imgY + 140 + ofsetHeight);
			
			g.drawImage(editProfile, Almoxarifado.WIDTH - 128 - 60, imgY + ofsetHeight, null);
			g.drawImage(archiveProfile, Almoxarifado.WIDTH - 128 - 60, imgY + 64 + (img.getHeight() - 64*2) + ofsetHeight, null);
			
			if(brokenApartPartsList.length > 1) {
				g.setColor(Color.yellow);
				g.setFont(new Font("arial", 1, 18));
				g.drawString("Lista de Peças: ", imgX + 25, img.getHeight() + imgY + 18*2 + ofsetHeight);
				
				drawPartsList(g);
			}
		}
	}

}
