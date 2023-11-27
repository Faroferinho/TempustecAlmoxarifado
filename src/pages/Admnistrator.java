package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.Random;

import javax.swing.JOptionPane;

import functions.DBConector;
import main.Almoxarifado;

public class Admnistrator extends Profile {

	public boolean isListing = false;
	public boolean isSigning = false;
	
	public String getString = "";
	public String auxToDraw[] = new String[Almoxarifado.quantityWorkers];
	public String toDraw[] = new String[Almoxarifado.quantityWorkers];
	public int toDrawIndex = 0;
	public int listHeight = 0;

	public BufferedImage editButton;
	public BufferedImage editDoneButton;
	public BufferedImage listButton;
	public BufferedImage signInButton;
	public BufferedImage passwordButton;
	
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
						
						Object[] possibilities = {"Nome", "Registro de Funcionario", "CPF", "Tipo de Funcionario", "*"};
						
						String s = (String) JOptionPane.showInputDialog(null, "Deseja listar com base em qual index", "", JOptionPane.PLAIN_MESSAGE, null, possibilities, possibilities[4]);
											
						if(s != null) {
							System.out.println("Opção: " + s);
							getString = drawTable(s);
							auxToDraw = getString.split("\n");
						}else {
							System.out.println("não tem s");
							isListing = false;
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
		
		String getRdF = drawTable("RdF");
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
	
	private void editInfoFromList(int i) {
		String[] auxReadRdF = new String[Almoxarifado.quantityWorkers];
		String auxToSplit = drawTable("RdF");
		
		auxReadRdF = auxToSplit.split("\n");
		
		String editRdF = auxReadRdF[i];
		System.out.println("RdF: " + editRdF);
		
		Object[] possibilities = {"Nome", "Registro de Funcionario", "CPF", "Senha", "Tipo de Funcionario", "Tudo"};
		String query = "";
		query += (String) JOptionPane.showInputDialog(null, "O que Deseja Alterar?", "", JOptionPane.PLAIN_MESSAGE, null, possibilities, possibilities[5]);
		switch(query) {
		case "Nome":
			query = "name";
			break;
		case "Registro de Funcionario":
			query = "RdF";
			break;
		case "Senha":
			query = "Password";
			break;
		case "Tipo de Funcionario":
			query = "type";
			break;
		case "Tudo":
			query = "*";
			break;
		case "null":
			return;
		}
		
		
		String newInfo = "";
		newInfo = JOptionPane.showInputDialog("Deseja Sobrescrever com qual informação?");
		if(newInfo == null || newInfo == ""){
				return;
		}
		
		DBConector.editLine("funcionarios", query, newInfo, editRdF);
		
		isListing = false;
		
	}
	
	public void render(Graphics g) {
		
		if(isOnTheRightState) {
		
			if(isEditing == false && isListing == false) {
				firstRendering(g);
				
				g.drawImage(editButton, Almoxarifado.WIDTH / 5 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(listButton, (Almoxarifado.WIDTH / 5) * 2 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(signInButton, (Almoxarifado.WIDTH / 5) * 3 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(passwordButton, (Almoxarifado.WIDTH / 5) * 4 - 64, Almoxarifado.HEIGHT / 2 + 120, null);

			}else if(isEditing == true){
				
				firstRendering(g);
				
				g.drawImage(editDoneButton, Almoxarifado.WIDTH / 3 - 64, Almoxarifado.HEIGHT / 2 + 120, null);
				g.drawImage(passwordButton, (Almoxarifado.WIDTH / 3) * 2 - 64, Almoxarifado.HEIGHT / 2 + 120, null);

				
			}else if(isListing == true) {
				g.setFont(new Font("arial", 1, 18));
				Color nC;
				
				for(int i = 0; i < Almoxarifado.quantityWorkers; i++) {
					if(Almoxarifado.mX > 60 && Almoxarifado.mX < 60 + g.getFontMetrics().stringWidth(auxToDraw[i]) && Almoxarifado.mY > (110 + 30*i) && Almoxarifado.mY < (130 + 30*i) + 10) {
						nC = new Color(100, 100, 100);
						if(mouseAuxRead) {
							//System.out.println("Indice Selecionado é: " + i);
							editInfoFromList(i);
						}
					}else {
						nC = Color.white;
					}
					g.setColor(nC);
					g.drawString(auxToDraw[i], 60, (130 + 30*i));
				}
				
			}
		}
	}
}
