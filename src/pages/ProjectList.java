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
	int boxBorder = 15;
	
	String namesToSplit = DBConector.readDB("ISO", "montagem", 2);
	String names[];
	String descriptionsToSplit = DBConector.readDB("description", "montagem", 2);
	String descriptions[];
	
	public static int scroll;
	private static int ofsetHeight;
	public boolean mouseStatus = false;
	
	private boolean changeState;
	private int changeStateIndex = 0;
	
	private boolean configState;
	private int configStateIndex = 0;
	
	private static boolean updateProjectList;
	public static boolean updateProjectListPL;
	
	BufferedImage img = Almoxarifado.imgManag.getSprite(256, 192, boxWidth, boxHeight);
	BufferedImage moreOptions = Almoxarifado.imgManag.getSprite(407, 193, 6, 20);
	
	public ProjectList(){
		names = spliting(namesToSplit);
		descriptions = spliting(descriptionsToSplit);
	}
	
	private String[] spliting(String toSplit) {
		System.out.println("Quebrando a String: \n" + toSplit);
		
		String[] auxSpliting = new String[Almoxarifado.quantityAssembly];
		auxSpliting = toSplit.split("\n");
		
		
		
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
		String querry = "INSERT INTO montagem (ID_Montagem, ISO, description, company) VALUES(" + (Almoxarifado.quantityAssembly+1) + ", '";
		String newAssemblyInfo = "";
		
		newAssemblyInfo += JOptionPane.showInputDialog(null, "Qual o Nome", "Cadastro de Nova Montagem", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(newAssemblyInfo)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		querry += newAssemblyInfo + "', '";
		newAssemblyInfo = "";
		
		newAssemblyInfo += JOptionPane.showInputDialog(null, "Insira uma descrição", "Cadastro de Nova Montagem", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(newAssemblyInfo)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		querry += newAssemblyInfo + "', '";
		newAssemblyInfo = "";
		
		newAssemblyInfo += JOptionPane.showInputDialog(null, "De Qual Empresa?", "Cadastro de Nova Montagem", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(newAssemblyInfo)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		querry += newAssemblyInfo + "')";
		newAssemblyInfo = "";
		
		JOptionPane.showMessageDialog(null, "Cadastro Efetuado Com sucesso", "Cadastro Concluido", JOptionPane.INFORMATION_MESSAGE, null);
		
		System.out.println("Feito o Castro");
		
		DBConector.writeDB(querry);
		Almoxarifado.quantityAssembly++;
		updateProjectList = true;
	}
	
	public void tick() {
		//System.out.println("Entrou no Tick()");
		
		if(Almoxarifado.state == 3) {
			isOnTheRightState = true;
		}else {
			isOnTheRightState = false;
		}
		
		if(isOnTheRightState == true) {
		
			if(scroll > 1) {
				System.out.println("Scroll pra baixo, ofsetHeight: " + ofsetHeight);
				ofsetHeight -= UserInterface.spd;
				scroll = 0;
			}else if(scroll < -1 && ofsetHeight < 0) {
				System.out.println("Scroll pra cima, ofsetHeight: " + ofsetHeight);
				ofsetHeight += UserInterface.spd;
				scroll = 0;
			}
			
			if(changeState == true) {
				//System.out.println("Mudar para o Perfil " + changeStateIndex);
				if(changeStateIndex != Almoxarifado.quantityAssembly) {
					int confirmationOfChangeState = JOptionPane.showConfirmDialog(null, "Realmente deseja mudar de Pagina", "Confirmação de Mudança de Pagina", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null);
					//System.out.println(confirmationOfChangeState );
					
					if(confirmationOfChangeState == 0) {
						Project.ID = changeStateIndex+1;
						Project.updateProject = true;
						Almoxarifado.state = 5;
					}else {
						mouseStatus = false;
						changeState = false;
						return;
					}
				}else {
					int confirmationOfChangeState = JOptionPane.showConfirmDialog(null, "Realmente deseja Criar outra Montagem?", "Confirmação de Criação de Montagem", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null);
					
					if(confirmationOfChangeState == 0) {
						createNewAssembly();
					}else {
						mouseStatus = false;
						changeState = false;
						return;
					}
				}
				
				changeState = false;
			}else if(configState) {
				System.out.println("Configurações no indice " + configStateIndex);
				configState = false;
			}
			
			if(updateProjectList) {
				namesToSplit = DBConector.readDB("ISO", "montagem", 2);
				descriptionsToSplit = DBConector.readDB("description", "montagem", 2);
				
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
		
		g.setFont(new Font("arial", 0, 15));
		
		int auxX = 0;
		int auxY = 0;
		for(int i = 0; i < Almoxarifado.quantityAssembly + 1; i++) {
			imgX = initX + (spaceBetween + boxWidth) * auxX;
			imgY = initY + auxY + ofsetHeight;
			
			if(mouseStatus) {
				//System.out.println("Clique");
				if(Almoxarifado.mY > boxHeight
				&& Almoxarifado.mY < UserInterface.maximunHeight) {
					//System.out.println("dentro do quadrado");
					if(i != Almoxarifado.quantityAssembly) {
						//System.out.println("Cliclou em qualquer coisa que não seja o ultimo indice");
						if(Almoxarifado.mX > imgX + boxWidth - boxBorder && Almoxarifado.mX < imgX + boxWidth - boxBorder + 12
						&& Almoxarifado.mY > imgY + boxBorder - boxBorder/3 && Almoxarifado.mY < imgX + boxWidth - boxBorder/3 + 25
						&& imgY > 0) {
							System.out.println("Cliclou em Configurações, indice: " + i);
							configState = true;
							configStateIndex = i;
						}
					}
					if(Almoxarifado.mX > imgX && Almoxarifado.mX < imgX + boxWidth
					&& Almoxarifado.mY > imgY && Almoxarifado.mY < imgY + boxHeight) {
						if(!configState) {
							System.out.println("Você clicou em: " + i);
							changeState = true;
							changeStateIndex = i;
						}
					}
				}
			}
			
			auxX++;
			
			if(auxX == 3) {
				auxX = 0;
				auxY += boxHeight + spaceBetween;
				//System.out.println("Index: " + i);
			}
			
			g.fillRect(imgX, imgY, boxWidth, boxHeight);
			g.drawImage(img, imgX, imgY, boxWidth, boxHeight, null);
			if(i != Almoxarifado.quantityAssembly) {
				g.drawImage(moreOptions, imgX + boxWidth - boxBorder, imgY + boxBorder - boxBorder/3, null);
				g.drawString(names[i], imgX - 15, imgY + boxHeight + 15);
				g.drawString(descriptions[i], imgX - 15, imgY + boxHeight + 15*2);
			}
		}
	}
}
