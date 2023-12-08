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
	
	int imgX = UserInterface.bttnX[0] + 50;
	int imgY = Almoxarifado.HEIGHT - UserInterface.maximunHeight - 20;
	
	public Project() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void tick() {
		
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
			
			if(imgAdress.equals(null) || imgAdress.equals("null")) {
				System.out.println("está vazio");
				imgAdress = "ProjetoBetaImg";
			}
			
			img = Almoxarifado.imgManag.getProjectImage(imgAdress);
			
			rawPartsList = DBConector.readDB("*", "pecas", 9);
			System.out.println("rawPartsList: \n" + rawPartsList);
			
			
			brokenApartPartsList = breakingList(rawPartsList);
			
			updateProject = false;
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
	
	public void drawPartsList(Graphics g) {
		int nextX = 0;
		int nextY = 0;
		
		int textInitPositY = 400;
		int auxWidth = 0;
		int auxHeight = 30;
		
		Color nC;
		g.setFont(new Font("arial", 0, auxHeight/2));
		
		System.out.println((brokenApartPartsList.length*brokenApartPartsList[0].length));
		for(int i = 0; i < (brokenApartPartsList.length*brokenApartPartsList[0].length); i++) {
			System.out.println(brokenApartPartsList[0][nextX] + " no indice " + (nextY) + " é: " + brokenApartPartsList[nextY][nextX]);
			
			if(i < 8){
				nC = Color.orange;
			}else {
				nC = Color.white;
			}
			
			switch(brokenApartPartsList[0][nextX]) {
			case "Montagem":
				//System.out.println("1");
				auxWidth += g.getFontMetrics().stringWidth(brokenApartPartsList[0][0]) * 3;
				break;
			case "Descrição":
				//System.out.println("2");
				auxWidth += g.getFontMetrics().stringWidth("A") * 15;
				break;
			case "Quantidade":
				//System.out.println("3");
				auxWidth += Almoxarifado.WIDTH/8;
				break;
			case "":
				auxWidth += g.getFontMetrics().stringWidth("000000");
				break;
			case "Preço":
				//System.out.println("5");
				auxWidth += g.getFontMetrics().stringWidth("quantidade");
				break;
			case "Fornecedor":
				//System.out.println("6");
				auxWidth += g.getFontMetrics().stringWidth("0000.00");
				auxWidth += 15;
				break;
			case "Status":
				//System.out.println("7");
				auxWidth += g.getFontMetrics().stringWidth("quantidade");
				auxWidth += 15;
				break;
			}
			
			g.setColor(nC);
			String toDrawString = brokenApartPartsList[nextY][nextX];
			
			g.drawString(toDrawString, imgX + 50 + auxWidth, textInitPositY + auxHeight);
			
			nextX++;
			
			if(nextX == 8) {
				System.out.println("================================================================================");
				nextY += 1;
				nextX = 0;
				auxWidth = 0;
				auxHeight += 20;
			}
			
		}
		
		
		System.out.println("Saiu do Loop");
	}
	
	public void render(Graphics g) {
		if(!updateProject) {
			g.setColor(Color.white);
			g.setFont(new Font("arial", 0, 20));
			
			g.drawImage(img, imgX, imgY, null);
			g.drawString(name, imgX + 15 + img.getWidth(), imgY + 20);
			g.drawString(company, imgX + 15 + img.getWidth(), imgY + 80);
			g.drawString(description, imgX + 15 + img.getWidth(), imgY + 140);
			
			g.drawImage(editProfile, Almoxarifado.WIDTH - 128 - 60, imgY, null);
			g.drawImage(archiveProfile, Almoxarifado.WIDTH - 128 - 60, imgY + 64 + (img.getHeight() - 64*2), null);
			
			if(Almoxarifado.quantityParts > 0) {
				g.setColor(Color.yellow);
				g.setFont(new Font("arial", 1, 18));
				g.drawString("Lista de Peças: ", imgX + 25, img.getHeight() + imgY + 18*2);
				
				drawPartsList(g);
			}
		}
	}

}
