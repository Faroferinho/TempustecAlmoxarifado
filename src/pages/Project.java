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
	private int quantityParts;
	
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
			
			rawPartsList = DBConector.findInDB("*", "pecas", "Montagem", "" + ID);
			System.out.println("rawPartsList: \n" + rawPartsList);
			
			updateProject = false;
		}
	}
	
	public String[][] breakingList(String toSplit){
		String separetedMatix[][] = null;
		
		for(int i = 0; i < 1; i++) {
			for(int j = 0; j < 8; j++) {
				
			}
		}
		
		return separetedMatix;
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
