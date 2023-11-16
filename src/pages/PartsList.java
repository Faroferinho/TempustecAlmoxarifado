package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JOptionPane;

import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class PartsList {
	
	private boolean isOnTheRightState = false;
	
	public String toSplit = DBConector.readDB("*", "pecas", 9);
	public String finalPartsTable[][] = new String[Almoxarifado.quantityParts+1][9];
	
	private int ofsetHeight;
	public static int scroll;
	private static int spd = 8;
	
	public boolean mouseStatus = false;
	
	private static boolean wasChanged = false;
	

	public PartsList() {
		finalPartsTable = listBreaker(toSplit);
	}
	
	private String[][] listBreaker(String toSplit){
		
		String internalString[] = new String[Almoxarifado.quantityParts];
		internalString = toSplit.split("\n");
		
		String returnString[][] = new String[Almoxarifado.quantityParts+1][9];
		
		for(int i = 1; i < Almoxarifado.quantityParts+1; i++) {
			for(int j = 0; j < 7; j++) {
				returnString[i] = internalString[i-1].split(" . ");
				String aux = " . ";
				if(j == 8) {
					aux = "\n";
				}
				System.out.print(returnString[i][j] + aux);
			}
		}
		
		return returnString;
	}
	
	private static void changePart(String index,int column) {
		System.out.println("Coluna: " + column);
		String newText = "newText";
		
		do {
			newText += JOptionPane.showInputDialog("Escreva o novo texto");
		}while(newText.equals("newText"));
		
		
		
		System.out.println("newText: " + newText);
		if(newText.equals("newTextnull")) {
			System.out.println("texto está nulo");
			return;
		}
		
		newText = newText.substring(7);
		System.out.println("newText: " + newText);
		
		String columnName =  "";
		switch(column) {
		case 1:
			columnName += "Montagem";
			break;
		case 2:
			columnName += "Description";
			break;
		case 3:
			columnName += "Quantity";
			
			break;
		case 4:
			columnName += "Quantity_Type";
			break;
		case 5:
			columnName += "Price";
			break;
		case 6:
			columnName += "Supplier";
			break;
		case 7:
			columnName += "Status";
			break;
		case 8:
			columnName += "Aplication";
			break;
		}
		
		String query = "UPDATE pecas Set " + columnName + " = " + newText + " WHERE ID_Parts = " + index;
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
			int auxHeight = UserInterface.bttnY + UserInterface.boxHeight + 32 + ofsetHeight;
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
				
				for(int j = 0; j < 7; j++) {
					
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
					
					
					Color nC = Color.white;
					
					if(i == 0) {
						nC = Color.orange;
					}
					
					if(auxHeight < 120 || auxHeight + g.getFontMetrics().getHeight() > 540) {
						nC = new Color(0,0,0,0);
					}
					
					if(Almoxarifado.mX > auxWidth - 15 && Almoxarifado.mX < auxWidth + g.getFontMetrics().stringWidth(finalPartsTable[i][j]) + 15 
							&& Almoxarifado.mY > auxHeight - 15 && Almoxarifado.mY < auxHeight + 20 && i != 0 && auxHeight > 120 && j != 0) {
						nC = Color.red;
						if(mouseStatus) {
							System.out.println("Você Clicou em " + finalPartsTable[i][j]);
							changePart(finalPartsTable[i][0], j);
							mouseStatus = false;
							
						}
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
