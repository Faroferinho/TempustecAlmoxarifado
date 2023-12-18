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
	
	public String toSplit = DBConector.readDB("*", "pecas");
	public static String finalPartsTable[][] = new String[Almoxarifado.quantityParts+1][8];
	static String assemblies[] = fillAssembliesName();
	public String[] quantityTypes = {"Peça", "Metro","Quilo", "Litro", "Barra", "Unidades"};
	public static boolean restartAssemblyList = false;
	
	private int ofsetHeight;
	public static int scroll;
	private static int maximumHeight = 0;
	private static int auxExtraLineCounter = 0;
	
	public boolean mouseStatus = false;
	
	private static boolean wasChanged = false;
	
	int total = 0;
	int characterLimitPerLine = 0;
	boolean multipleDescriptionLinesMark = false;
	
	boolean isEliminating = false;
	int indexToEliminate = -1;
	
	BufferedImage adicionar = Almoxarifado.imgManag.getSprite(0, 2*64, 128, 64);
	BufferedImage excluir = Almoxarifado.imgManag.getSprite(640-128, 2*64, 128, 64);
	
	public static int auxAddingFromMontagem = 0;

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
	
	public static void changePart(String index, int column) {
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
			if(DBConector.findInDB("Status", "pecas", "ID_Parts", index).equals("1 § \n")) {
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
			System.out.println("A: " + i);
			returnArray[i-1] = DBConector.findInDB("ISO", "Montagem", "ID_Montagem", Integer.toString(i));
			returnArray[i-1] = returnArray[i-1].substring(0, returnArray[i-1].length()-3);
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
		int auxInt = 0;
		if(auxAddingFromMontagem == 0) {
			aux += JOptionPane.showInputDialog(null, "Selecione a Montagem", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE, null, assemblies, 0);
			if(verifyString(aux)) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			System.out.println("aux: " + aux);
			for(int i = 1; i < Almoxarifado.quantityAssembly + 1; i++) {
				if(assemblies[i-1].equals(aux)) {
					auxInt = i;
				}
			}
			
		}else {
			auxInt = auxAddingFromMontagem;
		}
		
		querry += auxInt + ", '";
		
		aux = "";
		aux += JOptionPane.showInputDialog(null, "Insira a Descrição:", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.ERROR_MESSAGE);
			return;
		}
		querry += aux + "', ";
		
		aux = "";
		aux += JOptionPane.showInputDialog(null, "Insira a quantidade de Peças (apenas numeros)", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.ERROR_MESSAGE);
			return;
		}
		querry += aux + ", ";
		//TODO: insira uma forma de limitar o usuário a apenas usar numeros aqui;
		
		aux = "";
		aux += JOptionPane.showInputDialog(null, "Selecione um tipo de quantidade", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE, null, quantityTypes, 0);
		auxInt = 0;
		for(int i = 0; i < quantityTypes.length; i++) {
			//System.out.println("Array[i]: " + quantityTypes[i] + " aux: " + aux);
			if(quantityTypes[i].equals(aux)) {
				auxInt = i;
			}
		}
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.ERROR_MESSAGE);
			return;
		}
		querry += auxInt + ", ";
		
		aux = "";
		aux += JOptionPane.showInputDialog(null, "Insira o Valor da Peça (apenas numeros)", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Valor Agora será nulo", "", JOptionPane.WARNING_MESSAGE);
			aux = "null";
		}
		querry += aux + ", '";
		//TODO: insira uma forma de limitar o usuário a apenas usar numeros aqui;
		
		aux = "";
		aux += JOptionPane.showInputDialog(null, "Insira o Fornecedor:", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Valor Agora será nulo", "", JOptionPane.WARNING_MESSAGE);
			aux = "null";
		}
		querry += aux + "', 0)";
		
		System.out.println(querry);
		DBConector.writeDB(querry);
		wasChanged = true;
		Almoxarifado.quantityParts++;
	}
	
	public void eliminatePart(int index){
		//TODO: OTIMIZAR ESSA DESGRAÇA, POR DEUS
		int confirmation = JOptionPane.showConfirmDialog(null, "Você tem *CERTEZA* que você deseja deletar essa peça?", "Confirma a Eliminação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if(confirmation != 0) {
			return;
		}
		
		DBConector.writeDB("DELETE FROM pecas WHERE ID_Parts = " + index);
		Almoxarifado.quantityParts--;
		
		for(int i = 1; i < Almoxarifado.quantityParts+1; i++) {
			int auxVerifID = Integer.parseInt(finalPartsTable[i][0]);
			if(auxVerifID > index) {
				DBConector.writeDB("UPDATE pecas SET ID_Parts = " + (auxVerifID - 1) + " WHERE ID_Parts = " + auxVerifID);
			}
		}
		wasChanged = true;
	}
	
	private String changeAsseblyName(String assemblyID){
		//System.out.println("assembly id: " + assemblyID);
		String toReturn = "";
		
		int aux = Integer.parseInt(assemblyID);
		
		if(aux > assemblies.length) {
			return "";
		}
		
		toReturn = assemblies[aux-1];
		
		return toReturn;
	}
	
	private String changeQuantityType(String quantityType){
		String toReturn = "";
		
		int aux = Integer.parseInt(quantityType);
		toReturn = quantityTypes[aux];
		
		return toReturn;
	}
	
	public void tick() {
		if(Almoxarifado.state == 2) {
			isOnTheRightState = true;
			Almoxarifado.frame.setTitle("Lista de Peças");
		}else {
			isOnTheRightState = false;
			ofsetHeight = 0;
		}
		
		if(isOnTheRightState) {
			if(wasChanged == true) {
				System.out.println("Foi feita uma mudança");
				toSplit = DBConector.readDB("*", "pecas");
				finalPartsTable = listBreaker(toSplit);
				
				wasChanged = false;
			}
			
			if(scroll > 1 && ofsetHeight > maximumHeight) {
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
			total = Almoxarifado.WIDTH - auxWidth*2;
			characterLimitPerLine = (int) ((total*28)/100);
			
			for(int i = 0; i < Almoxarifado.quantityParts+1; i++) {

				for(int j = 0; j < 8; j++) {
					
					int maxMouse = 0;
					
					switch(j) {
					case 1:
						//System.out.println("1");
						auxWidth += (total*5)/100;
						maxMouse = g.getFontMetrics().stringWidth(finalPartsTable[i][j]);
						//System.out.println("1, AuxWidth: " + auxWidth);
						break;
					case 2:
						//System.out.println("2");
						auxWidth += (total*13.9)/100;
						maxMouse = characterLimitPerLine;
						//System.out.println("2, AuxWidth: " + auxWidth);
						break;
					case 3:
						//System.out.println("3");
						auxWidth += (total*33.2)/100;
						maxMouse = g.getFontMetrics().stringWidth(finalPartsTable[i][j]);
						//System.out.println("3, AuxWidth: " + auxWidth);
						break;
					case 4:
						//System.out.println("4, AuxWidth: " + auxWidth);
						auxWidth += g.getFontMetrics().stringWidth(" " + finalPartsTable[i][j-1]);
						maxMouse = g.getFontMetrics().stringWidth(finalPartsTable[i][j]);
						break;
					case 5:
						//System.out.println("5");
						auxWidth -= g.getFontMetrics().stringWidth(" " + finalPartsTable[i][j-2]);
						auxWidth += (total*19)/100;
						maxMouse = g.getFontMetrics().stringWidth(finalPartsTable[i][j]);
						//System.out.println("5, AuxWidth: " + auxWidth);
						break;
					case 6:
						//System.out.println("6");
						auxWidth += (total*11.8)/100;
						maxMouse = g.getFontMetrics().stringWidth(finalPartsTable[i][j]);
						//System.out.println("6, AuxWidth: " + auxWidth);
						break;
					case 7:
						//System.out.println("7");
						auxWidth += (total*14.6)/100;
						maxMouse = g.getFontMetrics().stringWidth(finalPartsTable[i][j]);
						//System.out.println("7, AuxWidth: " + auxWidth);
						break;
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
					if(!isEliminating) {
						if(Almoxarifado.mX > auxWidth - 15 && Almoxarifado.mX < auxWidth + maxMouse + 15
						&& Almoxarifado.mY > auxHeight - 15 && Almoxarifado.mY < auxHeight + (g.getFontMetrics().stringWidth(finalPartsTable[i][j]) / characterLimitPerLine) * 50 
						&& i != 0 && auxHeight > 120 && j != 0) {
						
							nC = Color.red;
							if(mouseStatus) {
								System.out.println("Você Clicou em " + finalPartsTable[i][j]);
								changePart(finalPartsTable[i][0], j);
								mouseStatus = false;
							}
								
						}
					}else {
						if(Almoxarifado.mY > auxHeight - 15 && Almoxarifado.mY < auxHeight + (g.getFontMetrics().stringWidth(finalPartsTable[i][j]) / characterLimitPerLine) * 30 
						&& i != 0 && auxHeight > 120 && j != 0) {
							
							indexToEliminate = i;
							
							if(i == indexToEliminate) {
								nC = Color.yellow;
							}else {
								nC = Color.white;
							}
							
							if(mouseStatus) {
								eliminatePart(indexToEliminate);
								isEliminating = false;
								return;
							}
						}
					}
					
					String auxTextToWrite = (finalPartsTable[i][j]);
					
					if(i > 0 && j == 1) {
						auxTextToWrite = changeAsseblyName(finalPartsTable[i][j]);
					}
					
					if(i > 0 && j == 4) {
						auxTextToWrite = changeQuantityType(finalPartsTable[i][j]);
					}
					
					g.setColor(nC);
					
					if(!multipleDescriptionLinesMark) {
						g.drawString(auxTextToWrite, 0 + auxWidth, 0 + auxHeight);
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
							
							
							g.drawString(auxText + verifFormat, 0 + auxWidth, 0 + auxHeight + 30 * (inc));
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
					if(isEliminating) {
						isEliminating = false;
					}else {
						isEliminating = true;
					}
					mouseStatus = false;
				}
			}
			
			g.drawImage(adicionar, Almoxarifado.WIDTH/3 - adicionar.getWidth()/2, auxHeight, null);
			//TODO: Adicionar Sistema de mudança de imagem, para alterara imagem sendo renderiazada utilizando a variavel isEliminating
			g.drawImage(excluir, Almoxarifado.WIDTH/3*2 - excluir.getWidth()/2, auxHeight, null);
			
			UserInterface.isOnButton(g, Almoxarifado.WIDTH/3 - adicionar.getWidth()/2, auxHeight);
			UserInterface.isOnButton(g, Almoxarifado.WIDTH/3*2 - excluir.getWidth()/2, auxHeight);
			
			maximumHeight = (Almoxarifado.quantityParts + auxExtraLineCounter) * -30;
			auxExtraLineCounter = 0;
		}
	}
}
