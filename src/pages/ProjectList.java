package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class ProjectList {
	
	static boolean isOnTheRightState = false;
	
	static int imgX;
	static int imgY;
	static int spaceBetween = Almoxarifado.WIDTH/8;
	static int boxWidth = 150;
	static int boxHeight = 200;
	int initX =  (int) (Almoxarifado.WIDTH/2 - (boxWidth * 1.5 + spaceBetween));
	int initY = UserInterface.bttnY*2 + UserInterface.boxHeight*2;
	int boxBorder = 30;
	
	String namesToSplit = DBConector.readDB("ISO", "montagem");
	String names[];
	String descriptionsToSplit = DBConector.readDB("description", "montagem");
	String descriptions[];
	
	public static int scroll;
	private static int ofsetHeight;
	public int maximumHeight = 0;
	public boolean mouseStatus = false;
	
	private boolean changeState = false;
	private int changeStateIndex = -1;
	
	private boolean configState = false;
	private int configStateIndex = -1;
	
	public static boolean updateProjectList;
	public static boolean updateProjectListPL;
	
	BufferedImage img = Almoxarifado.imgManag.getSprite(256, 192, boxWidth, boxHeight);
	BufferedImage moreOptions = Almoxarifado.imgManag.getSprite(407, 193, 6, 20);
	
	public ProjectList(){
		names = spliting(namesToSplit);
		descriptions = spliting(descriptionsToSplit);
	}
	
	private String[] spliting(String toSplit) {
		String[] auxSpliting = new String[Almoxarifado.quantityAssembly];
		auxSpliting = toSplit.split(" § \n");
		
		return auxSpliting;
	}
	
	private static boolean verifyString(String toVerif) {
		if(toVerif.equals("") || toVerif.equals("null") || toVerif.equals(" ")) {
			return true;
		}
		return false;
	}
	
	private void createNewAssembly(){
		mouseStatus = false;
		String querry = "INSERT INTO montagem (ISO, description, company) VALUES( \"";
		String newAssemblyInfo = "";
		
		newAssemblyInfo += JOptionPane.showInputDialog(null, "Insira o Valor da OS", "Cadastro de Nova Montagem", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(newAssemblyInfo)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		querry += "OS " + newAssemblyInfo + "\", \"";
		newAssemblyInfo = "";
		
		newAssemblyInfo += JOptionPane.showInputDialog(null, "Insira uma descrição", "Cadastro de Nova Montagem", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(newAssemblyInfo)) {
			JOptionPane.showMessageDialog(null, "O Valor será nulo", "", JOptionPane.WARNING_MESSAGE);
			newAssemblyInfo = "---------------------";
		}
		
		querry += newAssemblyInfo + "\", \"";
		newAssemblyInfo = "";
		
		newAssemblyInfo += JOptionPane.showInputDialog(null, "De Qual Empresa?", "Cadastro de Nova Montagem", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(newAssemblyInfo)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		querry += newAssemblyInfo + "\")";
		newAssemblyInfo = "";
		
		JOptionPane.showMessageDialog(null, "Cadastro Efetuado Com sucesso", "Cadastro Concluido", JOptionPane.INFORMATION_MESSAGE, null);
		
		DBConector.writeDB(querry);
		Almoxarifado.quantityAssembly++;
		updateProjectList = true;
	}
	
	public void tick() {		
		if(Almoxarifado.state == 3) {
			isOnTheRightState = true;
			Almoxarifado.frame.setTitle("Lista de Projetos");
		}else {
			isOnTheRightState = false;
			ofsetHeight = 0;
		}
		
		if(isOnTheRightState == true) {
		
			if(scroll > 1 && ofsetHeight > maximumHeight * -1) {
				ofsetHeight -= UserInterface.spd;
				scroll = 0;
			}else if(scroll < -1 && ofsetHeight < 0) {
				ofsetHeight += UserInterface.spd;
				scroll = 0;
			}
			
			if(changeState == true) {
				if(changeStateIndex != Almoxarifado.quantityAssembly) {
					int confirmationOfChangeState = JOptionPane.showConfirmDialog(null, "Realmente deseja mudar de Pagina",
					"Confirmação de Mudança de Pagina", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null);
					if(confirmationOfChangeState == 0) {
						Project.ID = Integer.parseInt(DBConector.findInDB("ID_Montagem", "Montagem", "ISO", 
								"\"" + names[changeStateIndex] + "\"").replace(" § \n", ""));
						Project.updateProject = true;
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
				namesToSplit = DBConector.readDB("ISO", "montagem");
				descriptionsToSplit = DBConector.readDB("description", "montagem");
				
				names = spliting(namesToSplit);
				descriptions = spliting(descriptionsToSplit);
				
				PartsList.restartAssemblyList = true;
				updateProjectList = false;
			}
			
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("calibri", 1, 40));
		g.drawString("Lista de Projetos: ", Almoxarifado.WIDTH/8 - 30, initY - (25) + ofsetHeight);
		
		g.setFont(new Font("arial", 0, 12));
		
		int auxX = 0;
		int auxY = 0;
		for(int i = 0; i < Almoxarifado.quantityAssembly + 1; i++) {
			imgX = initX + (spaceBetween + boxWidth) * auxX;
			imgY = initY + auxY + ofsetHeight;
			
			if(mouseStatus) {
				if(Almoxarifado.mX > imgX + boxWidth - (boxBorder + boxBorder/2) && Almoxarifado.mX < imgX + boxWidth - (boxBorder/2)
				&& Almoxarifado.mY > imgY + boxBorder/2 && Almoxarifado.mY < imgY + boxBorder + boxBorder/2) {
					if(changeState == false) {
						configState = true;
						configStateIndex = i;
						mouseStatus = false;
					}
				}
				if(Almoxarifado.mX > imgX && Almoxarifado.mX < imgX + boxWidth
				  && Almoxarifado.mY > imgY && Almoxarifado.mY < imgY + boxHeight) {
					if(configState == false) {
						changeState = true;
						changeStateIndex = i;
						mouseStatus = false;
					}
				}
			}
			
			auxX++;
			
			if(auxX == 3) {
				auxX = 0;
				auxY += boxHeight + spaceBetween;
			}
			
			g.setColor(Color.white);
			
			g.fillRect(imgX, imgY, boxWidth, boxHeight);
			g.drawImage(img, imgX, imgY, boxWidth, boxHeight, null);
			if(i != Almoxarifado.quantityAssembly) {
				g.drawImage(moreOptions, imgX + boxWidth - boxBorder, imgY + boxBorder - boxBorder/3, null);
				g.drawString(names[i], imgX - 15, imgY + boxHeight + 15);
				g.drawString(descriptions[i], imgX - 15, imgY + boxHeight + 15*2);
			}
			
			if(configStateIndex == i) {
				if(configState) {
					g.setColor(Color.white);
					g.fillRect(imgX + img.getWidth() - boxBorder/2, imgY + boxBorder - boxBorder/3, 60, 60);
					
					g.setColor(Color.black);
					g.drawRect(imgX + img.getWidth() - boxBorder/2, imgY + boxBorder - boxBorder/3, 59, 20);
					g.drawString("Abrir", imgX + img.getWidth() + 5 - boxBorder/2, imgY + boxBorder - boxBorder/3 + 15);
					g.drawRect(imgX + img.getWidth() - boxBorder/2, imgY + boxBorder - boxBorder/3 + 20, 59, 20);
					g.drawString("Alterar", imgX + img.getWidth() + 5 - boxBorder/2, imgY + boxBorder - boxBorder/3 + 35);
					g.drawRect(imgX + img.getWidth() - boxBorder/2, imgY + boxBorder - boxBorder/3 + 40, 59, 20);
					g.drawString("Arquivar", imgX + img.getWidth() + 5 - boxBorder/2, imgY + boxBorder - boxBorder/3 + 55);
					
					
					if(mouseStatus) {
						if(Almoxarifado.mX > imgX + img.getWidth() - boxBorder/2 && Almoxarifado.mX < imgX + img.getWidth() - boxBorder/2 + 60) {
							if(Almoxarifado.mY > imgY + boxBorder - boxBorder/3	&& Almoxarifado.mY < imgY + boxBorder - boxBorder/3 + 20) {
								changeStateIndex = configStateIndex;
								changeState = true;
							}else if(Almoxarifado.mY > imgY + boxBorder - boxBorder/3 + 40 && Almoxarifado.mY < imgY + boxBorder - boxBorder/3 + 60) {
								int toVerif = JOptionPane.showConfirmDialog(null, "Realmente deseja Arquivar o Projeto", "Arquivo do Projeto", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
								
								if(toVerif != 0) {
									JOptionPane.showMessageDialog(null, "Arquivo Cancelado", "", JOptionPane.PLAIN_MESSAGE);
									configStateIndex = -1;
									configState = false;
									mouseStatus = false;
								}else {									
									DBConector.Archive(PartsList.getKey(names[configStateIndex]));
									updateProjectList = true;
									PartsList.restartAssemblyList = true;
								}
								
								
							}
						}else {
							configStateIndex = -1;
							configState = false;
							mouseStatus = false;
						}
					}
				}
			}	
		}
		maximumHeight = imgX + auxY;
	}
}
