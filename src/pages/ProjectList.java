package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import functions.Archiver;
import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class ProjectList {
	
	static boolean isOnTheRightState = false;
	
	static int imgX;
	static int imgY;
	static int spaceBetween = Almoxarifado.WIDTH/12;
	static int boxWidth = 150;
	static int boxHeight = 200;
	int initX =  (int) (Almoxarifado.WIDTH/3 - (boxWidth + spaceBetween));
	int initY = UserInterface.bttnY*2 + UserInterface.boxHeight*2;
	int boxBorder = 30;
	
	String idsToSplit = DBConector.readDB("ID_Montagem", "montagem");
	ArrayList<String> ids = new ArrayList<>();
	String namesToSplit = DBConector.readDB("ISO", "montagem");
	ArrayList<String> names = new ArrayList<>();
	String descriptionsToSplit = DBConector.readDB("description", "montagem");
	ArrayList<String> descriptions = new ArrayList<>();
	String companiesToSplit = DBConector.readDB("Company", "montagem");
	ArrayList<String> companies = new ArrayList<>();
	
	public static int scroll;
	public int offsetHeight;
	public int maximumHeight = 0;
	public boolean mouseStatus = false;
	
	private boolean toggleScrollBar = false;
	private int thumbWidth = 18;
	public int thumbHeight = 0;
	private double thumbAuxY = 0;
	public boolean isDragging = false;
	
	private boolean changeState = false;
	private int changeStateIndex = -1;
	
	public static boolean updateProjectList;
	public static boolean updateProjectListPL;
	
	BufferedImage normalTable = Almoxarifado.imgManag.getSprite(300, 180, boxWidth, boxHeight);
	BufferedImage addItems = Almoxarifado.imgManag.getSprite(150, 180, boxWidth, boxHeight);
	BufferedImage bipartitionLine = Almoxarifado.imgManag.getSprite(312, 384, 124, 4);
	
	boolean multipleDescriptionMark = false;
	int sizeOfPartDescription = 0;
	int maxTextSize = 125;
	int auxH = 0;
	
	public ProjectList(){
		resetInfo();
		
		System.out.println("Carregou Lista de Projetos: " + LocalDateTime.now());
	}
	
	private void resetInfo() {
		String infoFromDB = DBConector.readDB("*", "Montagem");
		String[] lines = infoFromDB.split("\n");
		
		ids.clear();
		names.clear();
		descriptions.clear();
		companies.clear();
		
		for(int i = 0; i < lines.length; i++) {
			String currColumns[] = lines[i].split(" § ");
			
			ids.add(currColumns[0]);
			names.add(currColumns[1]);
			descriptions.add(currColumns[2]);
			companies.add(currColumns[3]);
		}
		
	}
	
	private void createNewAssembly(){
		Almoxarifado.state = 7;
	}
	
	public void scrollPositioner() {
		if(offsetHeight < maximumHeight * -1) {
			offsetHeight = maximumHeight * -1;
		}if(offsetHeight > 0) {
			offsetHeight = 0;
		}
		
		int Y = (UserInterface.maximunHeight - 18) - thumbHeight;
		double S = (Double.parseDouble("" + maximumHeight) / Double.parseDouble("" + offsetHeight));
		thumbAuxY = Y / S;
	}
	
	public void tick() {
		if(Almoxarifado.state == 3) {
			isOnTheRightState = true;
			Almoxarifado.frame.setTitle("Lista de Projetos");
		}else {
			isOnTheRightState = false;
			thumbAuxY = 0;
			offsetHeight = 0;
		}
		
		if(isOnTheRightState == true) {
			
			if(mouseStatus) {
				if(Almoxarifado.mX > Almoxarifado.WIDTH - (36 + 21) && Almoxarifado.mX < Almoxarifado.WIDTH - (36 + 21) + thumbWidth) {
					if(Almoxarifado.mY > UserInterface.bttnY + UserInterface.boxHeight + 20 - (int)(thumbAuxY) 
					&& Almoxarifado.mY < UserInterface.bttnY + UserInterface.boxHeight + 20 - (int)(thumbAuxY) + thumbHeight) {
						isDragging = true;
					}
				}
			}else {
				isDragging = false;
			}
		
			if(scroll > 1) {
				offsetHeight -= UserInterface.spd;
				
				scrollPositioner();
				
				scroll = 0;
			}else if(scroll < -1) {
				offsetHeight += UserInterface.spd;
				
				scrollPositioner();
				
				scroll = 0;
			}
			
			if(changeState == true) {
				if(changeStateIndex != Almoxarifado.quantityAssembly) {
					int confirmationOfChangeState = JOptionPane.showConfirmDialog(null, "Realmente deseja mudar de Pagina",
					"Confirmação de Mudança de Pagina", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null);
					if(confirmationOfChangeState == 0) {
						System.out.println("ids: \n" + ids + "\nIndice: " + ids.get(changeStateIndex));
						Project.ID = Integer.parseInt(ids.get(changeStateIndex));
						Project.updateProject = true;
						Archiver.writeOnArchive("mudarPag", "" + Project.ID, "", "");
						Almoxarifado.state = 5;
					}else {
						mouseStatus = false;
						changeState = false;
						return;
					}
				}else {
					int confirmationOfChangeState = JOptionPane.showConfirmDialog(null, "Realmente deseja Criar outra Montagem?", 
					"Confirmação de Criação de Montagem", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null);
					
					if(confirmationOfChangeState == 0) {
						createNewAssembly();
					}else {
						mouseStatus = false;
						changeState = false;
						return;
					}
				}
				
				changeState = false;
			}
			
			if(updateProjectList) {
				// TODO: Verificar se não dar problema isso dai.
				resetInfo();
				
				PartsList.restartAssemblyList = true;
				Almoxarifado.quantityAssembly = DBConector.counterOfElements("Montagem");
				updateProjectList = false;
			}
			
			if(maximumHeight > 450) {
				toggleScrollBar = true;
			}else {
				toggleScrollBar = false;
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("segoe ui", 1, 40));
		g.drawString("Lista de Projetos: ", Almoxarifado.WIDTH/8 - 30, initY - (25) + offsetHeight);
		
		int auxX = 0;
		int auxY = 0;
		for(int i = 0; i < Almoxarifado.quantityAssembly + 1; i++) {
			imgX = initX + (spaceBetween + boxWidth) * auxX;
			imgY = initY + auxY + offsetHeight;
			
			if(mouseStatus) {
				if(Almoxarifado.mX > imgX && Almoxarifado.mX < imgX + boxWidth
				  && Almoxarifado.mY > imgY && Almoxarifado.mY < imgY + boxHeight) {
						changeState = true;
						changeStateIndex = i;
						mouseStatus = false;
				}
			}
			
			auxX++;
			
			if(auxX == 4) {
				auxX = 0;
				auxY += boxHeight + spaceBetween;
			}
			
			g.setColor(Color.white);
			
			
			if(i != Almoxarifado.quantityAssembly) {
				g.drawImage(normalTable, imgX, imgY, boxWidth, boxHeight, null);
				g.setFont(new Font("segoi ui", 1, 17));
				Almoxarifado.drawStringBorder(((Graphics2D) g), names.get(i), imgX + (boxWidth / 2) - (g.getFontMetrics().stringWidth(names.get(i)) / 2), imgY + 28, 1, new Color(46, 46, 46), Color.white);
				g.setFont(new Font("segoi ui", 0, 12));

				auxH = 0;
				if(g.getFontMetrics().stringWidth(descriptions.get(i)) < maxTextSize) {
				
					g.drawString(descriptions.get(i), imgX + 10, imgY + 65);
					g.drawImage(bipartitionLine, imgX + 12, imgY + 80, null);
					g.drawString(companies.get(i), imgX + 10, imgY + 105);
				
				}else {
					ArrayList<Integer> breakLineIndexes = new ArrayList<>();
					int auxLineStandIn = 1;
					breakLineIndexes.add(0);
					
					for(int currChar = 0; currChar < descriptions.get(i).length(); currChar++) {
						if(g.getFontMetrics().stringWidth(descriptions.get(i).substring(breakLineIndexes.get(auxLineStandIn-1), currChar)) > maxTextSize) {
							breakLineIndexes.add(currChar);
							auxLineStandIn++;
						}
					}
					breakLineIndexes.add(descriptions.get(i).length());
					
					for(int lines = 1; lines < breakLineIndexes.size(); lines++) {
						String auxBrokenDesc;
						
						auxBrokenDesc = descriptions.get(i).substring(breakLineIndexes.get(lines - 1),  breakLineIndexes.get(lines));
						
						g.drawString(auxBrokenDesc, imgX + 10, imgY + 65 + auxH);
						auxH += 30;
					}
					
					g.drawImage(bipartitionLine, imgX + 12, imgY + 50 + auxH, null);
					g.drawString(companies.get(i), imgX + 10, imgY + 75 + auxH);
				}
			}else {
				g.drawImage(addItems, imgX, imgY, boxWidth, boxHeight, null);
			}
		}
		if(toggleScrollBar) {
			g.setColor(Color.darkGray);
			g.fillRect(Almoxarifado.WIDTH - (36 + 22), UserInterface.bttnY + UserInterface.boxHeight + 18, 20, UserInterface.maximunHeight - 12);
			
			if(maximumHeight > 0) {
				thumbHeight = (int) ((UserInterface.maximunHeight - 16) - (((UserInterface.maximunHeight - 32) * (maximumHeight / 16)) / 100));
				if(thumbHeight < 30) {
					thumbHeight = 30;
				}
			}
			
			g.setColor(Color.lightGray);
			if(isDragging) {
				g.setColor(Color.gray);
			}
			g.fillRect(Almoxarifado.WIDTH - (36 + 21), UserInterface.bttnY + UserInterface.boxHeight + 20 - (int)(thumbAuxY), thumbWidth, thumbHeight);
		}
		
		maximumHeight = (Math.round(Almoxarifado.quantityAssembly / 4) * (boxHeight + spaceBetween)) - spaceBetween/2;
	}
}
