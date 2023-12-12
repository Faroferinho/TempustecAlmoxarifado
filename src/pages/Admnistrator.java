package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public int scroll = 0;
	
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
						getInfo();		
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
	
	private void getInfo() {
		System.out.println("Clicou em Listar");
		
		String getPersonalInfo = "";
		getPersonalInfo += DBConector.readDB("*", "funcionarios");
		System.out.println("Informações dos Colaboradores: \n" + getPersonalInfo);
		
		separetedInfo = informationSorter(getPersonalInfo);
		
		System.out.println("==================================================================");
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
		System.out.println("O Texto Escrito é: " + query);
		
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
			do{
				infoChanger += JOptionPane.showInputDialog(null, "Você deseja Alterar " + columnName, "Alteração de Perfil", JOptionPane.WARNING_MESSAGE);
				
				Pattern letter = Pattern.compile("[a-zA-z]");
		        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
		        
		        Matcher hasLetter = letter.matcher(infoChanger);
		        Matcher hasSpecial = special.matcher(infoChanger);
		        
		        if(hasLetter.find() || hasSpecial.find()) {
		        	System.out.println("Tem Texto");
		        	infoChanger = infoChanger.replaceAll("[^0-9]", "");
		        }
			}while(infoChanger.length() != 11);
			break;
		case 3:
			columnName = "a Senha";
			UpdaterName = "password";

			int i = 0;
			
			String verificator = "";
			while(i < 3) {
				if(i == 0) {
					verificator = JOptionPane.showInputDialog(null, "Insira a Senha", "Confirmação de Identidade", JOptionPane.PLAIN_MESSAGE);
				}else {
					verificator = JOptionPane.showInputDialog(null, "Senha Incorreta, Numero de Tentativas: " + (3-i), "Confirmação de Identidade", JOptionPane.PLAIN_MESSAGE);
				}
				
				System.out.println("Senha Incerida: " + verificator + "Senha Correta: " + separetedInfo[index][column]);
				
				if(verificator.equals(separetedInfo[index][column])) {
					infoChanger += JOptionPane.showInputDialog(null, "Você deseja Alterar " + columnName, "Alteração de Perfil", JOptionPane.WARNING_MESSAGE);
					i = 4;
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
		
		System.out.println("infoChanger: " + infoChanger);
		
		
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
			DBConector.editLine("funcionarios", UpdaterName, infoChanger, "RdF", separetedInfo[index][0]);
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
		g.setFont(new Font("arial", 0, 14));
		
		for(int i = 0; i < separetedInfo.length * separetedInfo[0].length; i++) {
			/*System.out.println("auxX: " + x + " auxY: " + y);
			System.out.println("Texto no indice atual: " + separetedInfo[y][x]);*/
			
			if(y > 0) {
				g.setColor(Color.white);
			}else {
				g.setColor(Color.orange);
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
				if(Almoxarifado.mX > initialX + auxX && Almoxarifado.mX < initialX + auxX + g.getFontMetrics().stringWidth(auxTextToDraw)
						&& Almoxarifado.mY > initialY + auxY - g.getFontMetrics().getHeight() && Almoxarifado.mY < initialY + auxY) {
					g.setColor(Color.gray);
					if(mouseStatus) {
						//System.out.println("Você cliclou em: " + auxTextToDraw);
						changeInfo(x, y);
						mouseStatus = false;
					}
				}
				
				if(x > 1) {
					auxTextToDraw = textFormater(separetedInfo[y][x], x);
				}
			}
			
			g.drawString(auxTextToDraw, initialX + auxX, initialY + auxY);
			
			x++;
			
			
			if(x == 5) {
				x = 0;
				y++;
				auxY += g.getFontMetrics().getHeight() + 5;
				auxX = 0;
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
