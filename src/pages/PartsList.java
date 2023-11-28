package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JOptionPane;

import functions.DBConector;
import main.Almoxarifado;

public class PartsList {
	
	private boolean isOnTheRightState = false;
	
	public String toSplit = DBConector.readDB("*", "pecas", 9);
	public static String finalPartsTable[][] = new String[Almoxarifado.quantityParts+1][10];
	private static String ProjectsList[] = new String[Almoxarifado.quantityParts];
	
	private int ofsetHeight;
	public static int scroll;
	private static int spd = 8;
	
	public boolean mouseStatus = false;
	
	private static boolean wasChanged = false;
	

	public PartsList() {
		System.out.println("To Split: \n" + toSplit);
		finalPartsTable = listBreaker(toSplit);
		for(int i = 1; i < Almoxarifado.quantityParts+1; i++) {
			ProjectsList[i-1] = finalPartsTable[i][1];
			finalPartsTable[i][1] = DBConector.findInDB("ISO", "Montagem", "ID_Montagem", ProjectsList[i-1]);
		}
		
		finalPartsTable[0][0] = "ID";
		finalPartsTable[0][1] = "Montagem";
		finalPartsTable[0][2] = "Descrição";
		finalPartsTable[0][3] = "Quantidade";
		finalPartsTable[0][4] = "";
		finalPartsTable[0][5] = "Preço";
		finalPartsTable[0][6] = "Fornecedor";
		finalPartsTable[0][7] = "Status";
		finalPartsTable[0][8] = "Aplicação";
	}
	
	private String[][] listBreaker(String toSplit){
		
		String internalString[] = new String[Almoxarifado.quantityParts];
		internalString = toSplit.split("\n");
		
		String returnString[][] = new String[Almoxarifado.quantityParts+1][9];
		
		for(int i = 1; i < Almoxarifado.quantityParts+1; i++) {
			for(int j = 0; j < 9; j++) {
				returnString[i] = internalString[i-1].split(" . ");
				/*String aux = " . ";
				if(j == 8) {
					aux = "\n";
				}
				System.out.print(returnString[i][j] + aux);*/
			}
		}
		
		return returnString;
	}
	
	private static void changePart(String index,int column) {
		System.out.println("Coluna: " + column);
		System.out.println("Index: " + index);
		
		String columnName =  "";
		String auxString = "";
		int aux = 0;
		
		switch(column){
		case 1:
			columnName += "Montagem";
			String[] assemblies = fillAssembliesName();
			auxString += JOptionPane.showInputDialog(null, "Selecione a Montagem", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE, null, assemblies, 0);
			
			for(int i = 0; i < Almoxarifado.quantityAssembly; i++) {
				if(assemblies[i].equals(auxString)) {
					aux = i;
				}
			}
			
			auxString = Integer.toString(aux);
			break;
		case 2:
			columnName += "Description";
			auxString = JOptionPane.showInputDialog(null, "Insira a Descrição:", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			break;
		case 3:
			columnName += "Quantity";
			auxString += JOptionPane.showInputDialog(null, "Insira a quantidade de Peças (apenas numeros)", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			break;
		case 4:
			columnName += "Quantity_type";
			String[] quantityTypes = {"Peça", "Metro","Quilo", "Litro", "Barra", "Unidades"};
			auxString += JOptionPane.showInputDialog(null, "Selecione um tipo de quantidade", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE, null, quantityTypes, 0);
			aux = 0;
			for(int i = 0; i < quantityTypes.length; i++) {
				//System.out.println("Array[i]: " + quantityTypes[i] + " aux: " + aux);
				if(quantityTypes[i].equals(auxString)) {
					aux = i;
				}
			}
			auxString = Integer.toString(aux);
			break;
		case 5:
			columnName += "Price";
			auxString += JOptionPane.showInputDialog(null, "Insira o Valor da Peça (apenas numeros)", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			break;
		case 6:
			columnName += "Supplier";
			auxString += JOptionPane.showInputDialog(null, "Insira o Fornecedor:", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			break;
		case 7:
			columnName += "Status";
			if(finalPartsTable[Integer.parseInt(index)][column].equals("1")) {
				auxString = "0";
			}else {
				auxString = "1";
			}
			break;
		}
		
		
		DBConector.editLine("pecas", columnName, auxString, "ID_Parts", index);
		
		wasChanged = true;
	}
	
	private static String[] fillAssembliesName() {
		String[] returnArray = new String[Almoxarifado.quantityAssembly];
		
		for(int i = 1; i < Almoxarifado.quantityAssembly+1; i++) {
			returnArray[i-1] = DBConector.findInDB("ISO", "Montagem", "ID_Montagem", Integer.toString(i));
		}
		
		return returnArray;
	}
	
	private boolean verifyString(String toVerif) {
		if(toVerif.equals("") || toVerif.equals("null") || toVerif.equals(" ")) {
			return true;
		}
		return false;
	}

	public void addPart() {
		String querry = "INSERT INTO pecas VALUES( " + (Almoxarifado.quantityParts + 1) + ", ";
		
		String aux = "";
		String[] assemblies = new String[Almoxarifado.quantityAssembly];
		assemblies = fillAssembliesName();
		aux += JOptionPane.showInputDialog(null, "Selecione a Montagem", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE, null, assemblies, 0);
		int auxInt = 0;
		System.out.println("aux: " + aux);
		for(int i = 1; i < Almoxarifado.quantityAssembly + 1; i++) {
			if(assemblies[i-1].equals(aux)) {
				auxInt = i;
			}
		}
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		querry += auxInt + ", '";
		
		aux = "";
		aux += JOptionPane.showInputDialog(null, "Insira a Descrição:", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		querry += aux + "', ";
		
		aux = "";
		aux += JOptionPane.showInputDialog(null, "Insira a quantidade de Peças (apenas numeros)", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		querry += aux + ", ";
		//TODO: insira uma forma de limitar o usuário a apenas usar numeros aqui;
		
		aux = "";
		String[] quantityTypes = {"Peça", "Metro","Quilo", "Litro", "Barra", "Unidades"};
		aux += JOptionPane.showInputDialog(null, "Selecione um tipo de quantidade", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE, null, quantityTypes, 0);
		auxInt = 0;
		for(int i = 0; i < quantityTypes.length; i++) {
			//System.out.println("Array[i]: " + quantityTypes[i] + " aux: " + aux);
			if(quantityTypes[i].equals(aux)) {
				auxInt = i;
			}
		}
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		querry += auxInt + ", ";
		
		aux = "";
		aux += JOptionPane.showInputDialog(null, "Insira o Valor da Peça (apenas numeros)", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		querry += aux + ", '";
		//TODO: insira uma forma de limitar o usuário a apenas usar numeros aqui;
		
		aux = "";
		aux += JOptionPane.showInputDialog(null, "Insira o Fornecedor:", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
			return;
		}
		querry += aux + "', 0)";
		
		System.out.println(querry);
		DBConector.writeDB(querry);
		wasChanged = true;
		Almoxarifado.quantityParts++;
	}
	
	public void tick() {
		if(Almoxarifado.state == 2) {
			isOnTheRightState = true;
		}else {
			isOnTheRightState = false;
		}
		
		if(isOnTheRightState) {
			if(wasChanged == true) {
				System.out.println("Foi feita uma mudança");
				toSplit = DBConector.readDB("*", "pecas", 9);
				finalPartsTable = listBreaker(toSplit);
				
				finalPartsTable[0][0] = "ID";
				finalPartsTable[0][1] = "Montagem";
				finalPartsTable[0][2] = "Descrição";
				finalPartsTable[0][3] = "Quantidade";
				finalPartsTable[0][4] = "";
				finalPartsTable[0][5] = "Preço";
				finalPartsTable[0][6] = "Fornecedor";
				finalPartsTable[0][7] = "Status";
				finalPartsTable[0][8] = "Aplicação";
				
				for(int i = 1; i < Almoxarifado.quantityParts+1; i++) {
					finalPartsTable[i][1] = DBConector.findInDB("ISO", "Montagem", "ID_Montagem", ProjectsList[i-1]);
				}
				
				wasChanged = false;
			}
			
			if(scroll > 1 && ofsetHeight > -(Almoxarifado.quantityParts*21)) {
				System.out.println("ofsetHeight: " + ofsetHeight);
				ofsetHeight -= spd;
				scroll = 0;
			}else if(scroll < -1 && ofsetHeight < 0) {
				ofsetHeight += spd;
				scroll = 0;
			}
			else {
				
			}
			//System.out.println(ofsetHeight);
			//System.out.println("Status do Scroll: " + scroll);
			
		}
	}
	
	public void render(Graphics g) {
		if(isOnTheRightState) {
			
			g.setFont(new Font("arial", 1, 12));

			int auxHeight = 125 + ofsetHeight;
			int auxWidth = 50;			
			
			
			for(int i = 0; i < Almoxarifado.quantityParts+1; i++) {

				for(int j = 0; j < 8; j++) {
					
					switch(j) {
					case 1:
						auxWidth += g.getFontMetrics().stringWidth(finalPartsTable[0][0]) * 5;
						break;
					case 2:
						auxWidth += 70;
						break;
					case 3:
						auxWidth += 275;
						break;
					case 4:
						auxWidth += 25;
						break;
					case 5:
						auxWidth += g.getFontMetrics().stringWidth("9999.99") + 30;
						break;
					case 6:
						auxWidth += 80;
						break;
					case 7:
						auxWidth += g.getFontMetrics().stringWidth(finalPartsTable[0][7]) + 80;
						break;
					case 8:
						auxWidth += g.getFontMetrics().stringWidth(finalPartsTable[0][7]) + 30;
						break;
					}

					int aux = 0;
					if(i == 0 && (j == 1 || j == 7 || j == 8)) {
						aux = (g.getFontMetrics().stringWidth(finalPartsTable[0][j]) / 2) - 7;
					}else if(i == 0 && j == 3) {
						aux = (g.getFontMetrics().stringWidth(finalPartsTable[0][j]) / 2) - 15;
					}

					if(i != 0 && (j == 1 || j == 8)) {
						aux = g.getFontMetrics().stringWidth(finalPartsTable[i][j]) / 2 - 15;
					}
					
					
					Color nC = Color.white;
					
					if(i == 0) {
						nC = Color.orange;
					}
					
				if(Almoxarifado.mX > auxWidth - 15 - aux && Almoxarifado.mX < auxWidth + g.getFontMetrics().stringWidth(finalPartsTable[i][j]) + 15 - aux
							&& Almoxarifado.mY > auxHeight - 15 && Almoxarifado.mY < auxHeight + 20 && i != 0 && auxHeight > 120 && j != 0) {
						nC = Color.red;
						if(mouseStatus) {
							System.out.println("Você Clicou em " + finalPartsTable[i][j]);
							changePart(finalPartsTable[i][0], j);
							mouseStatus = false;
							
						}
					}
					if(auxHeight < 120 || auxHeight + g.getFontMetrics().getHeight() > 540) {
						nC = new Color(0,0,0,0);
					}
					g.setColor(nC);
					
					
					g.drawString(finalPartsTable[i][j], 0 + auxWidth - aux, 0 + auxHeight);
					//System.out.println("Largura Auxiliar: " + auxWidth + ", Altura Auxiliar: " + auxHeight);
					//System.out.println(finalPartsTable[i][j]);
					
					
				}
				
				auxWidth = 50;
				auxHeight += 30;
			}
			
			g.setFont(new Font("arial", 1, 15));
			g.setColor(Color.pink);
			
			if(Almoxarifado.mX > Almoxarifado.WIDTH/2 - g.getFontMetrics().stringWidth("Adicionar Peça")/2 - 5 
					&& Almoxarifado.mX < Almoxarifado.WIDTH/2 + g.getFontMetrics().stringWidth("Adicionar Peça")/2 + 5
					&& Almoxarifado.mY > auxHeight - 20 && Almoxarifado.mY < auxHeight + 5) {
				g.setColor(Color.yellow);
				if(mouseStatus) {
					System.out.println("Você Clicou para Adicionar Peça");
					addPart();
					mouseStatus = false;
					
				}
			}
			
			g.drawString("Adicionar Peça", Almoxarifado.WIDTH/2 - g.getFontMetrics().stringWidth("Adicionar Peça")/2, auxHeight);
			
			
			
		}
	}
}
