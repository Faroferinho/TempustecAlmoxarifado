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
	BufferedImage ArchiveProfile = Almoxarifado.imgManag.getSprite(0, 64*4, 128, 64);
	
	double price = 0.0;
	int partsList[];
	
	public static boolean updateProject = true;
	
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
			
			breakingList(rawPartsList);
			
			updateProject = false;
		}
	}
	
	public String[][] breakingList(String toSplit){
		String textAux[] = toSplit.split(" § ");
		System.out.println("Tamanho do Arry Auxiliar: " + (textAux.length-1));
		String separetedMatrix[][] = new String[8][(textAux.length-1)/8];
		
		int incremX = 0;
		int incremY = 0;
		for(int i = 1; i < textAux.length; i++) {
			if((i) % 8 == 0) {
				System.out.println("O Numero " + i + " é multiplo de 8");
				incremX = 0;
				incremY++;
			}
			separetedMatrix[incremX][incremY] = textAux[i-1];
			
			System.out.println("================================================================");
			System.out.println("separetedMatrix: " + separetedMatrix[incremX][incremY]);
			System.out.println("Posição X: " + incremX);
			System.out.println("Posição Y: " + incremY);
			System.out.println("================================================================");
			
			incremX++;
			
			
			
		}
		
		
		return separetedMatrix;
	}
	
	public void drawPartsList(Graphics g) {
		
	}
	
	public void render(Graphics g) {
		if(!updateProject) {
			g.setColor(Color.white);
			g.setFont(new Font("arial", 0, 20));
			
			int imgX = UserInterface.bttnX[0] + 50;
			int imgY = Almoxarifado.HEIGHT - UserInterface.maximunHeight - 20;
			g.drawImage(img, imgX, imgY, null);
			g.drawString(name, imgX + 15 + img.getWidth(), imgY + 20);
			g.drawString(company, imgX + 15 + img.getWidth(), imgY + 80);
			g.drawString(description, imgX + 15 + img.getWidth(), imgY + 140);
			
			g.drawImage(editProfile, Almoxarifado.WIDTH - 128 - 60, imgY, null);
			g.drawImage(ArchiveProfile, Almoxarifado.WIDTH - 128 - 60, imgY + 64 + (img.getHeight() - 64*2), null);
			
			g.setColor(Color.yellow);
			g.setFont(new Font("arial", 1, 18));
			g.drawString("Lista de Peças: ", imgX + 25, img.getHeight() + imgY + 18*2);
			
			drawPartsList(g);
		}
	}

}
