package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class Project {
	
	int ID = 1;
	String name = "";
	String description = "";
	String company = "";
	String imgAdress = "";
	
	BufferedImage img;
	BufferedImage editProfile = Almoxarifado.imgManag.getSprite(128, 64*2, 128, 64);
	BufferedImage ArchiveProfile = Almoxarifado.imgManag.getSprite(0, 64*4, 128, 64);
	
	double price = 0.0;
	int partsList[];
	
	boolean updateProject = true;
	
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
			
			updateProject = false;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("arial", 0, 20));
		
		g.drawImage(img, UserInterface.bttnX[0] + 50, Almoxarifado.HEIGHT - UserInterface.maximunHeight - 20, null);
		g.drawString(name, UserInterface.bttnX[0] + 65 + img.getWidth(), Almoxarifado.HEIGHT - UserInterface.maximunHeight + 10);
		g.drawString(company, UserInterface.bttnX[0] + 65 + img.getWidth(), Almoxarifado.HEIGHT - UserInterface.maximunHeight + 50);
		g.drawString(description, UserInterface.bttnX[0] + 65 + img.getWidth(), Almoxarifado.HEIGHT - UserInterface.maximunHeight + 90);
		g.drawImage(editProfile, Almoxarifado.WIDTH - 128 - 60, Almoxarifado.HEIGHT - UserInterface.maximunHeight - 20, null);
		g.drawImage(ArchiveProfile, Almoxarifado.WIDTH - 128 - 60, Almoxarifado.HEIGHT - UserInterface.maximunHeight - 20 + 64*2, null);
	}

}
