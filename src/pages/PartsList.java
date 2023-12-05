package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class PartsList {
	
	private boolean isOnTheRightState = false;
	
	public String toSplit = DBConector.readDB("*", "pecas", 9);
	public static String finalPartsTable[][] = new String[Almoxarifado.quantityParts+1][8];
	static String assemblies[] = fillAssembliesName();
	public boolean restartAssemblyList = false;
	
	private int ofsetHeight;
	public static int scroll;
	private static int maximumHeight = 0;
	private static int auxExtraLineCounter = 0;
	
	public boolean mouseStatus = false;
	
	private static boolean wasChanged = false;
	
	int characterLimitPerLine = 218;
	boolean multipleDescriptionLinesMark = false;
	
	boolean isEliminating = false;
	int indexToEliminate = -1;
	
	BufferedImage adicionar = Almoxarifado.imgManag.getSprite(0, 2*64, 128, 64);
	BufferedImage excluir = Almoxarifado.imgManag.getSprite(640-128, 2*64, 128, 64);

	public PartsList() {
		System.out.println("To Split: \n" + toSplit);
		finalPartsTable = listBreaker(toSplit);
	}
	
	private String[][] listBreaker(String toSplit){
		String linesToBreakdown[] = toSplit.split("\n");
		String returnString[][] = new String[Almoxarifado.quantityParts+1][8];
		
		returnString[0][0] = "ID";
		returnString[0][1] = "Montagem";
		returnString[0][2] = "Descrição";
		returnString[0][3] = "Quantidade";
		returnString[0][4] = "";
		returnString[0][5] = "Preço";
		returnString[0][6] = "Fornecedor";
		returnString[0][7] = "Status";
		
		for(int i = 0; i < Almoxarifado.quantityParts; i++) {
			returnString[i+1] = linesToBreakdown[i].split(" § ");
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
			auxString += JOptionPane.showInputDialog(null, "Selecione a Montagem", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE, null, assemblies, 0);
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			for(int i = 0; i < Almoxarifado.quantityAssembly; i++) {
				if(assemblies[i].equals(auxString)) {
					aux = i+1;
				}
			}
			
			auxString = Integer.toString(aux);
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			break;
		case 2:
			columnName += "Description";
			auxString += JOptionPane.showInputDialog(null, "Insira a Descrição:", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			break;
		case 3:
			columnName += "Quantity";
			auxString += JOptionPane.showInputDialog(null, "Insira a quantidade de Peças (apenas numeros)", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			break;
		case 4:
			columnName += "Quantity_type";
			String[] quantityTypes = {"Peça", "Metro","Quilo", "Litro", "Barra", "Unidades"};
			auxString += JOptionPane.showInputDialog(null, "Selecione um tipo de quantidade", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE, null, quantityTypes, 0);
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
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
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			break;
		case 6:
			columnName += "Supplier";
			auxString += JOptionPane.showInputDialog(null, "Insira o Fornecedor:", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
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
	
	private static boolean verifyString(String toVerif) {
		if(toVerif.equals("") || toVerif.equals("null") || toVerif.equals(" ")) {
			return true;
		}
		return false;
	}

	public void addPart() {
		String querry = "INSERT INTO pecas VALUES( " + (Almoxarifado.quantityParts + 1) + ", ";
		
		String aux = "";
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
	
	private void eliminatePart(){
		
	}
	
	private String changeAsseblyName(String assemblyID){
		//System.out.println("assembly id: " + assemblyID);
		String toReturn = "";
		
		int aux = Integer.parseInt(assemblyID);
		toReturn = assemblies[aux-1];
		
		return toReturn;
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
				
				wasChanged = false;
			}
			
			if(scroll > 1 && ofsetHeight > maximumHeight) {
				System.out.println("ofsetHeight: " + ofsetHeight);
				ofsetHeight -= UserInterface.spd;
				scroll = 0;
			}else if(scroll < -1 && ofsetHeight < 0) {
				ofsetHeight += UserInterface.spd;
				scroll = 0;
			}
			//System.out.println(ofsetHeight);
			//System.out.println("Status do Scroll: " + scroll);
			
			if(restartAssemblyList) {
				assemblies = fillAssembliesName();
				restartAssemblyList = false;
			}
			
		}
	}
	
	public void render(Graphics g) {
		if(isOnTheRightState) {
			
			g.setFont(new Font("arial", 1, 12));

			int auxHeight = 125 + ofsetHeight;
			int auxWidth = 50;		
			int descriptionOfsetHeight = 1;
			
			
			for(int i = 0; i < Almoxarifado.quantityParts+1; i++) {

				for(int j = 0; j < 8; j++) {
					
					switch(j) {
					case 1:
						auxWidth += g.getFontMetrics().stringWidth(finalPartsTable[0][0]) * 5;
						break;
					case 2:
						auxWidth += 105;
						break;
					case 3:
						auxWidth += 323;
						break;
					case 4:
						auxWidth += 83;
						break;
					case 5:
						auxWidth += g.getFontMetrics().stringWidth("9999.99") + 30;
						break;
					case 6:
						auxWidth += 110;
						break;
					case 7:
						auxWidth += g.getFontMetrics().stringWidth(finalPartsTable[0][7]) + 110;
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
					
					if(i != 0 && j == 2) {
						if(g.getFontMetrics().stringWidth(finalPartsTable[i][j]) > characterLimitPerLine) {
							//System.out.println("O Texto é grande demais para o Espaço delimitado");
							multipleDescriptionLinesMark = true;
						}
					}
						
						
					Color nC = Color.white;
						
					if(i == 0) {
						nC = Color.orange;
					}
					
					if(Almoxarifado.mX > auxWidth - 15 - aux && Almoxarifado.mX < auxWidth + (g.getFontMetrics().stringWidth(finalPartsTable[i][j]) / characterLimitPerLine) * 30 + 15 - aux
						&& Almoxarifado.mY > auxHeight - 15 && Almoxarifado.mY < auxHeight + (g.getFontMetrics().stringWidth(finalPartsTable[i][j]) / characterLimitPerLine) * 30 && i != 0 && auxHeight > 120 && j != 0) {
						if(!isEliminating) {
							nC = Color.red;
							if(mouseStatus) {
								System.out.println("Você Clicou em " + finalPartsTable[i][j]);
								changePart(finalPartsTable[i][0], j);
								mouseStatus = false;
							}
						}else {
							indexToEliminate = i;
							if(mouseStatus) {
								
							}
						}
							
						
					}
					
					String auxTextToWrite = (finalPartsTable[i][j]);
					
					if(i > 0 && j == 1) {
						auxTextToWrite = changeAsseblyName(finalPartsTable[i][j]);
						aux = g.getFontMetrics().stringWidth(auxTextToWrite)/2 - 20;
					}
					
					if(i == indexToEliminate) {
						nC = Color.yellow;
					}
					g.setColor(nC);
					
					if(!multipleDescriptionLinesMark) {
						g.drawString(auxTextToWrite, 0 + auxWidth - aux, 0 + auxHeight);
					}else {
						String auxText = "";
						int quantityOfLines = g.getFontMetrics().stringWidth(finalPartsTable[i][j])/characterLimitPerLine;
						descriptionOfsetHeight += quantityOfLines;
						double quantityOfCharacters = finalPartsTable[i][j].length()/quantityOfLines;
						
						/*System.out.print("Informações: \n"
								+ "Tamanho do Texto: " + g.getFontMetrics().stringWidth(finalPartsTable[i][j]) + " / 323 = " + quantityOfLines + "\n"
								+ "quantityOfCharacters: " + quantityOfCharacters + "\n\n\n");
						*/
						for(int inc = 0; inc < quantityOfLines+1; inc++) {
							int start = (int) (quantityOfCharacters*(inc));
							int end = (int) (quantityOfCharacters*(inc+1));
							
							//System.out.println("Start: " + start + ", End: " + end);
							
							if(end > finalPartsTable[i][j].length()) {
								end = finalPartsTable[i][j].length();
							}

							auxText = finalPartsTable[i][j].substring(start, end);
							//System.out.println(auxText);
							
							
							char verifFormat = finalPartsTable[i][j].charAt(end-1);
							if(verifFormat != ' ' && verifFormat != ',' && verifFormat != '.' && verifFormat != '-'){
								verifFormat = '-';
							}else {
								verifFormat = ' ';
							}
							
							
							g.drawString(auxText + verifFormat, 0 + auxWidth - aux, 0 + auxHeight + 30 * (inc));
						}
						auxExtraLineCounter += descriptionOfsetHeight;
					}
					
					multipleDescriptionLinesMark = false;
					//System.out.println("Largura Auxiliar: " + auxWidth + ", Altura Auxiliar: " + auxHeight);
					//System.out.println(finalPartsTable[i][j]);
					
					
				}
				auxWidth = 50;
				auxHeight += 30*descriptionOfsetHeight;
				descriptionOfsetHeight = 1;
			}

			if(mouseStatus) {
				if(Almoxarifado.mX > Almoxarifado.WIDTH/3 - adicionar.getWidth()/2	&& Almoxarifado.mX < Almoxarifado.WIDTH/3 + adicionar.getWidth()/2
						&& Almoxarifado.mY > auxHeight - 5 && Almoxarifado.mY < auxHeight + 69) {
					System.out.println("Você Clicou para Adicionar Peça");
					addPart();
					mouseStatus = false;
				}else if(Almoxarifado.mX > Almoxarifado.WIDTH/3*2 - excluir.getWidth()/2	&& Almoxarifado.mX < Almoxarifado.WIDTH/3*2 + excluir.getWidth()/2
						&& Almoxarifado.mY > auxHeight - 5 && Almoxarifado.mY < auxHeight + 69) {
					System.out.println("Você Clicou para Excluir Peça");
					isEliminating = true;
					mouseStatus = false;
				}
			}
			
			g.drawImage(adicionar, Almoxarifado.WIDTH/3 - adicionar.getWidth()/2, auxHeight, null);
			g.drawImage(excluir, Almoxarifado.WIDTH/3*2 - excluir.getWidth()/2, auxHeight, null);
			
			maximumHeight = (Almoxarifado.quantityParts + auxExtraLineCounter) * -30;
			auxExtraLineCounter = 0;
		}
	}
}
