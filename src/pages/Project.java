package pages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import functions.DBConector;

public class Project {
	
	int ID = 1;
	String name = "";
	String description = "";
	String company = "";
	String imgAdress = "";
	BufferedImage img;
	String brokenApartInfo[];
	
	double price = 0.0;
	int partsList[];
	
	boolean updateProject = true;
	
	public Project() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void tick() {
		if(updateProject) {
			System.out.println("Atualizando a Pagina de Projeto");
			String aux = DBConector.findInDB("*", "montagem", "ID_Montagem", "" + ID);
			System.out.println("aux: " + aux);
			
			brokenApartInfo = aux.split(" § ");
			
			name = brokenApartInfo[1];
			description = brokenApartInfo[2];
			company = brokenApartInfo[3];
			imgAdress = brokenApartInfo[4];
			
			updateProject = false;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
	}

}
