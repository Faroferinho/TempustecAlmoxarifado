package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JOptionPane;

import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class Admnistrator extends Profile {

	public boolean isListing = false;
	public boolean isSigning = false;
	
	public BufferedImage editButton;
	public BufferedImage editDoneButton;
	public BufferedImage listButton;
	public BufferedImage signInButton;
	public BufferedImage passwordButton;
	
	String separetedInfo[][];
	
	public Admnistrator(String Name, String RdF, String CPF) {
		super(Name, RdF, CPF);
		
		editButton = Almoxarifado.imgManag.getSprite(128, 128, 128, 64);
		editDoneButton = Almoxarifado.imgManag.getSprite(128, 128 + 64, 128, 64);
		listButton = Almoxarifado.imgManag.getSprite(64*4, 128, 128, 64);
		signInButton = Almoxarifado.imgManag.getSprite(0, 128, 128, 64);
		passwordButton = Almoxarifado.imgManag.getSprite(64*6, 64*2, 128, 64);
	}
	
	public void tick() {	
		if(reset) {
			reset = false;
			isEditing = false;
			isSigning = false;
			isListing = false;
		}
		
		if(Almoxarifado.state == 1) {
			isOnTheRightState = true;
		}else {
			reset = true;
			isOnTheRightState = false;
			
		}
		
		if(isOnTheRightState) {
			if(mouseStatus == true) {
				//System.out.println("Mouse Status = " + mouseStatus);
				mouseAuxRun = true;
				mouseAuxEdit = true;
				mouseAuxRead = true;
				mouseAuxSign = true;
				mouseStatus = false;
			}else {
				mouseAuxRun = false;
				mouseAuxEdit = false;
				mouseAuxRead = false;
				mouseAuxSign = false;
			}
			
			if(mouseAuxRun) {
				switch(buttonClick(Almoxarifado.mX, Almoxarifado.mY, true)) {
				case 1:
					if(isEditing == false) {
						isEditing = true;
						System.out.println("Está Editando");
						
					}else {
						isEditing = false;
					}
					break;
				case 2:
					if(isListing == false) {
						isListing = true;
						System.out.println("Clicou em Listar");
						
						String getPersonalInfo = "";
						getPersonalInfo += DBConector.readDB("*", "funcionarios");
						System.out.println("Informações dos Colaboradores: \n" + getPersonalInfo);
						
						separetedInfo = informationSorter(getPersonalInfo);
						
						System.out.println("==================================================================");
						
						for(int i = 0; i < separetedInfo.length; i++) {
							for(int j = 0; j < 5; j++) {
								System.out.println(separetedInfo[i][j]);
							}
						}
					}
					break;
				case 3:
					if(isSigning == false) {
						isSigning = true;
					}
					break;
				case 4:
					editInfo(4);
					break;
				default:
					break;
				}
			}
			
			if(isEditing) {
				changeInformation(Almoxarifado.mX, Almoxarifado.mY, true);
				
			}else if(isSigning) {
				int newRdF = generateRdF();
				String query = "INSERT INTO Funcionarios VALUES(" + newRdF + ", '";

				String auxQ = "";
				auxQ += writingQuery("Qual o Nome?");
				if(auxQ.equals("")) {
					JOptionPane.showMessageDialog(null, "Nome Invalido", "Cadastro Cancelado", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				query += auxQ;
				query +="', '";
				
				auxQ = "";
				
				auxQ += writingQuery("Qual é o CPF?");
				if(auxQ.equals("") || auxQ.length() != 11) {
					JOptionPane.showMessageDialog(null, "CPF Invalido", "Cadastro Cancelado", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				query += auxQ;
				query += "', 'Tempustec2023', ";
				
				auxQ = "";
				
				Object[] possibilities = {"Colaborador", "Administrador"};
				auxQ += (String) JOptionPane.showInputDialog(null, "Qual o Tipo do Funcionario", "", JOptionPane.PLAIN_MESSAGE, null, possibilities, possibilities[1]);
				if(auxQ.equals("Administrador")) {
					auxQ = "1";
				}else {
					auxQ = "0";
				}
				
				query += auxQ;
				query += ")";
				
				
				DBConector.writeDB(query);
				
				Almoxarifado.quantityWorkers++;
				
				isSigning = false;
			}
		}
	}
	
	private int generateRdF() {
		Random rand = new Random();	
		int newRdF = rand.nextInt(0, 9999);
		
		String getRdF = DBConector.readDB("RdF", "funcionarios");
		String auxComparator[] = new String[Almoxarifado.quantityWorkers];
		int toCompare[] = new int[Almoxarifado.quantityWorkers];
		
		auxComparator = getRdF.split("\n");
		
		
		for(int i = 0; i < Almoxarifado.quantityWorkers; i++) {
			//System.out.println("O Valor do RdF no indice " + (i+1) +" é de:" + auxComparator[i]);
			toCompare[i] = Integer.parseInt(auxComparator[i]);
			//System.out.println("O Valor do RdF no indice " + (i+1) +" é de:" + toCompare[i]);
			if(newRdF == toCompare[i]) {
				newRdF = generateRdF();
			}
		}
		
		
		return newRdF; 
	}
	
	private String writingQuery(String prompt) {
		String query = "";
		query += JOptionPane.showInputDialog(prompt);
		System.out.println("O nome será: " + query);
		switch(query) {
		case "null":
			System.out.println("Está nulo");
			isSigning = false;
			return "";
		case "":
			System.out.println("Está vazio");
			isSigning = false;
			return "";
		}
		return query;
	}
	
	public String[][] informationSorter(String toSplit){
		String linesToBreakdown[] = toSplit.split("\n");
		String returnString[][] = new String[linesToBreakdown.length + 1][5];
		
		returnString[0][0] = "Registro";
		returnString[0][1] = "Nome";
		returnString[0][2] = "CPF";
		returnString[0][3] = "Senha";
		returnString[0][4] = "Tipo";
		
		for(int i = 0; i < linesToBreakdown.length-1; i++) {
			returnString[i+1] = linesToBreakdown[i].split(" § ");
		}
		return returnString;
	}
	
	private void listPeople(Graphics g) {
		
	}
	
	public void render(Graphics g) {
		
		if(isOnTheRightState) {
		
			if(isEditing == false && isListing == false) {
				firstRendering(g);
				
				g.drawImage(editButton, Almoxarifado.WIDTH / 5 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(listButton, (Almoxarifado.WIDTH / 5) * 2 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(signInButton, (Almoxarifado.WIDTH / 5) * 3 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(passwordButton, (Almoxarifado.WIDTH / 5) * 4 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				UserInterface.isOnButton(g, Almoxarifado.WIDTH / 5 - 64, Almoxarifado.HEIGHT / 2 + 120);
				UserInterface.isOnButton(g, (Almoxarifado.WIDTH / 5) * 2 - 64, Almoxarifado.HEIGHT / 2 + 120);
				UserInterface.isOnButton(g, (Almoxarifado.WIDTH / 5) * 3 - 64, Almoxarifado.HEIGHT / 2 + 120);
				UserInterface.isOnButton(g, (Almoxarifado.WIDTH / 5) * 4 - 64, Almoxarifado.HEIGHT / 2 + 120);

			}else if(isEditing == true){
				
				firstRendering(g);
				
				g.drawImage(editDoneButton, Almoxarifado.WIDTH / 3 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(passwordButton, (Almoxarifado.WIDTH / 3) * 2 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				UserInterface.isOnButton(g, Almoxarifado.WIDTH / 3 - 64, Almoxarifado.HEIGHT / 2 + 120);
				UserInterface.isOnButton(g, (Almoxarifado.WIDTH / 3) * 2 - 64, Almoxarifado.HEIGHT / 2 + 120);

				
			}else if(isListing == true) {
				listPeople(g);
			}
		}
	}
}
