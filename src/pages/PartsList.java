package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JOptionPane;

import functions.DBConector;
import main.Almoxarifado;

public class PartsList {
	
	private boolean isOnTheRightState = false;
	
	public String toSplit = DBConector.readDB("*", "pecas", 10);
	public String finalPartsTable[][] = new String[Almoxarifado.quantityParts+1][9];
	private static String ProjectsList[] = new String[Almoxarifado.quantityParts];
	
	private int ofsetHeight;
	public static int scroll;
	private static int spd = 8;
	
	public boolean mouseStatus = false;
	
	private static boolean wasChanged = false;
	

	public PartsList() {
		finalPartsTable = listBreaker(toSplit);
		for(int i = 1; i < Almoxarifado.quantityParts+1; i++) {
			ProjectsList[i-1] = finalPartsTable[i][1];
			finalPartsTable[i][1] = DBConector.findInDB("ISO", "Montagem", "ID_Montagem", ProjectsList[i-1]);
		}
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

	private static String newTextReapiting() {
		String nT = "newText";
		do {
			nT += JOptionPane.showInputDialog("Escreva o novo texto");
		}while(nT.equals("newText"));
		
		return nT;
	}
	
	private static String[] getNames() {
		
		String auxListOfNames = "";
		String listOfNames[] = new String[Almoxarifado.quantityParts];
		
		for(int i = 0; i < Almoxarifado.quantityParts; i++) {
			auxListOfNames += DBConector.findInDB("ISO", "Montagem", "ID_Montagem", String.valueOf(i));

		}
		
		listOfNames = auxListOfNames.split("\n");
		
		return listOfNames;
	}
	
	private static void changePart(String index,int column) {
		System.out.println("Coluna: " + column);
		String newText = "newText";
		
		String columnName =  "";
		String auxString = "";
		switch(column) {
		case 1:
			columnName += "Montagem";
			auxString += "'";
			
			Object[] possibilities = getNames();
			newText += (String) JOptionPane.showInputDialog(null, "Qual o Tipo do Funcionario", "", JOptionPane.PLAIN_MESSAGE, null, possibilities, possibilities[1]);
			
			break;
		case 2:
			columnName += "Description";
			auxString += "'";
			newText = newTextReapiting();
			break;
		case 3:
			columnName += "Quantity";
			newText = newTextReapiting();
			break;
		case 4:
			columnName += ", Quantity_Type";
			newText += " " + newTextReapiting();
			break;
		case 5:
			columnName += "Price";
			newText = newTextReapiting();
			break;
		case 6:
			columnName += "Supplier";
			auxString += "'";
			newText = newTextReapiting();
			break;
		case 7:
			columnName += "Status";
			newText = newTextReapiting();
			break;
		case 8:
			columnName += "Aplication";
			auxString += "'";
			newText = newTextReapiting();
			break;
		}
		
		newText = newText.substring(7);
		System.out.println("newText: " + newText);
		

		
		if(newText.equals("null")) {
			System.out.println("texto está nulo");
			return;
		}else if(newText.equals("Nova Montagem")) {
			
		}
		
		String query = "UPDATE pecas Set " + columnName + " = " + auxString + newText + auxString  + " WHERE ID_Parts = " + index;
		System.out.println(query);
		
		DBConector.writeDB(query);
		
		wasChanged = true;
	}
	
	public void tick() {
		if(Almoxarifado.state == 2) {
			isOnTheRightState = true;
		}else {
			isOnTheRightState = false;
		}
		
		if(isOnTheRightState) {
			
			if(scroll > 1 && ofsetHeight > -Almoxarifado.quantityParts*7 ) {
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
			if(wasChanged == true) {
				System.out.println("Foi feita uma mudança");
				toSplit = DBConector.readDB("*", "pecas", 10);
				finalPartsTable = listBreaker(toSplit);
				wasChanged = false;
			}
		}
	}
	
	public void render(Graphics g) {
		if(isOnTheRightState) {
			
			g.setFont(new Font("arial", 1, 15));

			int auxHeight = 120 + ofsetHeight;
			int auxWidth = 70;
			
			finalPartsTable[0][0] = "ID";
			finalPartsTable[0][1] = "Montagem";
			finalPartsTable[0][2] = "Descrição";
			finalPartsTable[0][3] = "Quantidade";
			finalPartsTable[0][4] = "";
			finalPartsTable[0][5] = "Preço";
			finalPartsTable[0][6] = "Fornecedor";
			finalPartsTable[0][7] = "Status";
			finalPartsTable[0][8] = "Aplicação";
			
			
			
			
			
			for(int i = 0; i < Almoxarifado.quantityParts+1; i++) {

				for(int j = 0; j < 9; j++) {
					
					switch(j) {
					case 1:
						auxWidth += g.getFontMetrics().stringWidth(finalPartsTable[0][0]) * 4 ;
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
				
				auxWidth = 70;
				auxHeight += 50;
			}
			
			
			
		}
	}
}
