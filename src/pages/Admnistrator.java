package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import functions.Archiver;
import functions.DBConector;
import functions.Functions;
import main.Almoxarifado;
import main.UserInterface;

public class Admnistrator extends Profile {

	public boolean isListing = false;
	private boolean isSigning = false;
	private boolean isRemoving = false;
	
	private BufferedImage editButton;
	private BufferedImage editDoneButton;
	private BufferedImage listButton;
	private BufferedImage signInButton;
	private BufferedImage passwordButton;
	private BufferedImage deleteButton;
	
	String separetedInfo[][];
	
	public int scroll = 0;
	private int auxHeight = 0;
	
	int indexToEliminate = -1;
	
	int listMaxHeight = 0;
	
	public Admnistrator(String Name, String RdF, String CPF) {
		super(Name, RdF, CPF);
		
		editButton = Almoxarifado.imgManag.getSprite(475, 0, 165, 60);
		editDoneButton = Almoxarifado.imgManag.getSprite(0, 510, 165, 60);
		listButton = Almoxarifado.imgManag.getSprite(475, 60, 165, 60);
		signInButton = Almoxarifado.imgManag.getSprite(475, 60*5, 165, 60);
		passwordButton = Almoxarifado.imgManag.getSprite(475, 120, 165, 60);
		deleteButton = Almoxarifado.imgManag.getSprite(475, 60*7, 165, 60);
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
			Almoxarifado.frame.setTitle("Perfil de " + Profile.name);
		}else {
			reset = true;
			isOnTheRightState = false;
			
		}
		
		if(isOnTheRightState) {
			if(mouseStatus) {
				if(!isListing && !isSigning && !isRemoving && !isEditing) {
					switch(buttonClick(Almoxarifado.mX, Almoxarifado.mY, true)) {
					case 1:
						//Editar Perfil
						System.out.println("Entrando no isEditing");
						isEditing = true;
						break;
					case 2:
						//Listar Funcionario
						System.out.println("Clique 2");
						isListing = true;
						break;
					case 3:
						//Adicionar Funcionario
						isSigning = true;
						System.out.println("Clique 3");
						break;
					case 4:
						//Mudar Senha
						System.out.println("Clique 4");
						editInfo(4);
						break;
					}
					mouseStatus = false;
				}
				
			}
			
			if(isEditing) {
				if(mouseStatus) {
					if(Functions.isOnBox(Almoxarifado.WIDTH - (76 + 165) * 2, 136, UserInterface.boxWidthSmall, UserInterface.boxHeight)){
						System.out.println("Saindo do isEditing");
						isEditing = false;
						mouseStatus = false;
					}else if(Functions.isOnBox(Almoxarifado.WIDTH - (76 + 165), 136 * 2, UserInterface.boxWidthSmall, UserInterface.boxHeight)) {
						editInfo(4);
						mouseStatus = false;
					}
				}
			}
			
			if(isListing) {
				getInfo();
				
				if(mouseStatus) {
					if(Functions.isOnBox(((Almoxarifado.WIDTH / 3) - (deleteButton.getWidth() / 3)) * 2, listMaxHeight, UserInterface.boxWidthSmall, UserInterface.boxHeight)) {
						addWorker();
						mouseStatus = false;
					}
				}
				
				
			}
		}
	}
	
	private void addWorker() {
		String addWorker = "INSERT INTO FUNCIONARIOS VALUES(" + generateRdF() + ", ";
		String auxText = "" + writingQuery("Insira o Nome do Funcionario", "Cadastro de Funcionarios");
		
		if(nullVerificator(auxText)) {
			return;
		}
		
	}
	
	private boolean nullVerificator(String toVerif) {
		System.out.println("Texto: " + toVerif);
		if(toVerif.equals("null")) {
			JOptionPane.showMessageDialog(null, "", "", JOptionPane.WARNING_MESSAGE, null);
			return true;
		}else {
			return false;
		}
	}
	
	private void getInfo() {		
		String getPersonalInfo = "";
		getPersonalInfo += DBConector.readDB("*", "funcionarios");
		
		separetedInfo = informationSorter(getPersonalInfo);
	}
	
	private int generateRdF() {
		Random rand = new Random();	
		int newRdF = rand.nextInt(0, 9999);
		
		String getRdF = DBConector.readDB("RdF", "funcionarios");
		String auxComparator[] = new String[Almoxarifado.quantityWorkers];
		
		auxComparator = getRdF.split(" § \n");
		
		
		for(int i = 0; i < Almoxarifado.quantityWorkers; i++) {
			int toCompare = Integer.parseInt(auxComparator[i]);
			if(newRdF == toCompare) {
				newRdF = generateRdF();
			}
		}
		
		
		return newRdF; 
	}
	
	private String writingQuery(String prompt, String title) {
		String query = "";
		query += JOptionPane.showInputDialog(null, prompt, title, JOptionPane.PLAIN_MESSAGE);
		
		if(verifString(query)) {
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
		
		for(int i = 0; i < linesToBreakdown.length; i++) {
			returnString[i+1] = linesToBreakdown[i].split(" § ");
		}
		return returnString;
	}
	
	private void remove(int x) {		
		int confirmation = JOptionPane.showConfirmDialog(null, "Você tem *CERTEZA* que você deseja deletar essa peça?", "Confirma a Eliminação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if(confirmation != 0) {
			return;
		}
		
		DBConector.writeDB("DELETE FROM Funcionarios WHERE RdF = " + x);
		Almoxarifado.quantityWorkers--;
		
		getInfo();
	}
	
	private void changeInfo(int column, int index){
		String infoChanger = "";
		
		String columnName = "";
		String UpdaterName = "";
		
		switch(column) {
		case 1:
			columnName = "o Nome";
			UpdaterName = "name";
			infoChanger += JOptionPane.showInputDialog(null, "Você deseja Alterar " + columnName, "Alteração de Perfil", JOptionPane.WARNING_MESSAGE);
			break;
		case 2:
			columnName = "o CPF";
			UpdaterName = "CPF";
			infoChanger += JOptionPane.showInputDialog(null, "Você deseja Alterar " + columnName, "Alteração de Perfil", JOptionPane.WARNING_MESSAGE);
			
			Pattern letter = Pattern.compile("[a-zA-z]");
	        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
	        
	        Matcher hasLetter = letter.matcher(infoChanger);
	        Matcher hasSpecial = special.matcher(infoChanger);
	        
	        if(hasLetter.find() || hasSpecial.find()) {
	        	infoChanger = infoChanger.replaceAll("[^0-9]", "");
	        }
	        
	        if(infoChanger.length() < 10 || infoChanger.length() > 12) {
	        	JOptionPane.showMessageDialog(null, "Valor Inserido Invalido", "Erro ao Efetuar Alteração", JOptionPane.ERROR_MESSAGE);
	        	return;
	        }
			break;
		case 3:
			columnName = "a Senha";
			UpdaterName = "password";

			int i = 0;
			
			String verificator = "";
			while(i < 3) {
				if(i == 0) {
					verificator += JOptionPane.showInputDialog(null, "Insira a Senha", "Confirmação de Identidade", JOptionPane.PLAIN_MESSAGE);
				}else {
					verificator += JOptionPane.showInputDialog(null, "Senha Incorreta, Numero de Tentativas: " + (3-i), "Confirmação de Identidade", JOptionPane.PLAIN_MESSAGE);
				}
				
				if(verificator.equals(separetedInfo[index][column])) {
					infoChanger += JOptionPane.showInputDialog(null, "Você deseja Alterar " + columnName, "Alteração de Perfil", JOptionPane.WARNING_MESSAGE);
					i = 4;
				}else if(verificator.equals("null")){
					return;
				}else {
					i++;
				}
				
			}
			
			if(i == 0) {
				return;
			}
			
			break;
		case 4:
			columnName = "o Tipo";
			UpdaterName = "type";
			String Options[] = {"Administrador", "Colaborador"};
			infoChanger += JOptionPane.showInputDialog(null, "Você deseja Alterar " + columnName, "Alteração de Perfil", JOptionPane.WARNING_MESSAGE, null, Options, 0);
			break;
		}
		
		if(verifString(infoChanger)) {
			JOptionPane.showMessageDialog(null, "Edição Não Concluida", "Falha na Alteração", JOptionPane.ERROR_MESSAGE);
			return;
		}else {
			if(column == 4) {
				if(infoChanger.equals("Administrador")) {
					infoChanger = "1";
				}else {
					infoChanger = "0";
				}
			}
			DBConector.writeDB("funcionarios", UpdaterName, infoChanger, "RdF", separetedInfo[index][0]);
			Archiver.writeOnArchive("alteracao", columnName + " de User." + separetedInfo[index][0], separetedInfo[index][column], infoChanger);
			getInfo();
		}
		JOptionPane.showMessageDialog(null, "Edição Concluida com Sucesso", "Sucesso!!!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private boolean verifString(String toVerif){
		if(toVerif.equals(null) || toVerif.equals("null") || toVerif.equals("") || toVerif.equals(" ")) {
			return true;
		}
		return false;
	}
	
	private void listPeople(Graphics g) {
		
		int x = 0;
		int y = 0;
		
		int initialX = UserInterface.bttnX[0] + 50;
		int initialY = (UserInterface.bttnY*2) + UserInterface.boxHeight + 15;
		int auxX = 0;
		int auxY = 0;
		int total = Almoxarifado.WIDTH - (initialX*2);
		
		
		g.setColor(Color.white);
		g.setFont(new Font("segoe ui", 0, 14));
		
		Color nC = Color.white;
		
		for(int i = 0; i < separetedInfo.length * separetedInfo[0].length; i++) {
			
			if(y > 0) {
				nC = Color.white;
			}else {
				nC = Color.orange;
			}
			
			
			switch(x) {
			case 1:
				auxX += (total*10)/100;
				break;
			case 2:
				auxX += (total*35)/100;
				break;
			case 3:
				auxX += (total*20)/100;
				break;
			case 4:
				auxX += (total*25)/100;
				break;
			}
			
			String auxTextToDraw = separetedInfo[y][x];
			
			if(x > 0 && y > 0) {
				if(x > 1) {
					auxTextToDraw = textFormater(separetedInfo[y][x], x);
				}
				if(!isRemoving) {
					if(Almoxarifado.mX > initialX + auxX && Almoxarifado.mX < initialX + auxX + g.getFontMetrics().stringWidth(auxTextToDraw)
					&& Almoxarifado.mY > initialY + auxY - g.getFontMetrics().getHeight() && Almoxarifado.mY < initialY + auxY) {
						nC = Color.gray;
						if(mouseStatus) {
							changeInfo(x, y);
							mouseStatus = false;
						}
					}
				}else {
					if(Almoxarifado.mY > initialY + auxY - g.getFontMetrics().getHeight() && Almoxarifado.mY < initialY + auxY) {
						indexToEliminate = y;
						if(y == indexToEliminate) {
							nC = Color.yellow;
						}else {
							nC = Color.white;
						}
						if(mouseStatus) {
							Archiver.writeOnArchive("remocao", "funcionario", separetedInfo[y][0], "");
							remove(Integer.parseInt(separetedInfo[y][0]));
							mouseStatus = false;
						}
					}
				}
			}
			g.setColor(nC);
			g.drawString(auxTextToDraw, initialX + auxX, initialY + auxY + auxHeight);
			
			x++;
			
			
			if(x == 5) {
				x = 0;
				y++;
				auxY += g.getFontMetrics().getHeight() + 5;
				auxX = 0;
			}
		}
		
		listMaxHeight = initialY + auxY + auxHeight;
		g.drawImage(deleteButton, ((Almoxarifado.WIDTH / 3) - (deleteButton.getWidth() / 3)), initialY + auxY + auxHeight, null);
		UserInterface.isOnSmallButton(g, ((Almoxarifado.WIDTH / 3) - (deleteButton.getWidth() / 3)), initialY + auxY + auxHeight);
		g.drawImage(signInButton, ((Almoxarifado.WIDTH / 3)*2 - (deleteButton.getWidth() / 3)*2), initialY + auxY + auxHeight, null);
		UserInterface.isOnSmallButton(g, ((Almoxarifado.WIDTH / 3)*2 - (deleteButton.getWidth() / 3)*2), initialY + auxY + auxHeight);

		if(mouseStatus) {
			if(Almoxarifado.mX > (Almoxarifado.WIDTH / 3) - (deleteButton.getWidth() / 2)
			&& Almoxarifado.mX < (Almoxarifado.WIDTH / 3) + (deleteButton.getWidth() / 2)
			&& Almoxarifado.mY > initialY + auxY + auxHeight && Almoxarifado.mY < initialY + auxY + auxHeight + 64) {
				if(isRemoving == true) {
					isRemoving = false;
					mouseStatus = false;
				}else {
					isRemoving = true;
					mouseStatus = false;
				}
			}
			if(Almoxarifado.mX > (Almoxarifado.WIDTH / 3)*2 - (deleteButton.getWidth() / 2)
			&& Almoxarifado.mX < (Almoxarifado.WIDTH / 3)*2 + (deleteButton.getWidth() / 2)
			&& Almoxarifado.mY > initialY + auxY + auxHeight && Almoxarifado.mY < initialY + auxY + auxHeight + 64) {
				isSigning = true;
			}
		}
	}
	
	private String textFormater(String text, int index) {
		String returner = "";
		
		switch(index) {
		case 2:
			String aux1 = text.substring(0, 3);
			String aux2 = text.substring(3, 6);
			String aux3 = text.substring(6, 9);
			String aux4 = text.substring(9, 11);
			
			returner = aux1 + "." + aux2 + "." + aux3 + "-" + aux4;
			
			break;
		case 3:
			String aux = "";
			for(int i = 0; i < text.length(); i++) {
				aux += '.';
			}
			
			returner = aux;
			break;
		case 4:
			if(text.equals("1")) {
				returner = "Admnistrador";
			}else {
				returner = "Colaborador";
			}
			break;
		}
		
		return returner;
	}
	
	public void render(Graphics g) {
		
		if(isOnTheRightState) {
		
			if(isEditing == false && isListing == false) {
				firstRendering(g);
				
				g.drawImage(editButton, Almoxarifado.WIDTH - (76 + 165)*2, 136, null);
				g.drawImage(listButton, Almoxarifado.WIDTH - (76 + 165), 136, null);
				g.drawImage(signInButton, Almoxarifado.WIDTH - (76 + 165)*2, 136 * 2, null);
				g.drawImage(passwordButton, Almoxarifado.WIDTH - (76 + 165), 136*2, null);
				UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH - (76 + 165), 136);
				UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH - (76 + 165)*2, 136);
				UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH - (76 + 165), 136*2);
				UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH - (76 + 165)*2, 136*2);

			}else if(isEditing == true){
				
				firstRendering(g);
				
				g.drawImage(editDoneButton, Almoxarifado.WIDTH - (76 + 165)*2, 136, null);
				g.drawImage(passwordButton, Almoxarifado.WIDTH - (76 + 165), 136*2, null);
				UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH - (76 + 165)*2, 136);
				UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH - (76 + 165), 136*2);

				
			}else if(isListing == true) {
				listPeople(g);
			}
		}
	}
}
