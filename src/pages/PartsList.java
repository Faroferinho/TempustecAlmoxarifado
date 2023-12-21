package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import javax.swing.JOptionPane;

import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class PartsList {
	
	private boolean isOnTheRightState = false;
	
	public String toSplit = DBConector.readDB("*", "pecas");
	public static String finalPartsTable[][] = new String[Almoxarifado.quantityParts+1][8];
	static HashMap<String, String> assembliesHM = fillAssembliesName();
	static String[] assembliesID;
	static String[] assembliesSO;
	public static String quantityTypes[] = fillQuantityTypes();
	public static boolean restartAssemblyList = false;
	
	private int ofsetHeight;
	public static int scroll;
	private static int maximumHeight = 0;
	private static int auxExtraLineCounter = 0;
	
	public boolean mouseStatus = false;
	
	private static boolean wasChanged = false;
	
	private static int maximumIndexQT = 0;
	
	int total = 0;
	int characterLimitPerLine = 0;
	boolean multipleDescriptionLinesMark = false;
	
	boolean isEliminating = false;
	int indexToEliminate = -1;
	
	BufferedImage adicionar = Almoxarifado.imgManag.getSprite(0, 2*64, 128, 64);
	BufferedImage excluir = Almoxarifado.imgManag.getSprite(640-128, 2*64, 128, 64);
	BufferedImage checkBox = Almoxarifado.imgManag.getSprite(414, 193, 32, 32);
	BufferedImage check = Almoxarifado.imgManag.getSprite(406, 226, 41, 39);
	
	public static int auxAddingFromMontagem = 0;

	public PartsList() {
		//System.out.println("To Split: \n" + toSplit);
		finalPartsTable = listBreaker(toSplit);
	}
	
	private static String getKey(String value) {
		System.out.println("HashMap: \n" + assembliesHM);
		
		for (Entry<String, String> entry : assembliesHM.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
		
		return "";
	}
	
	private static String newQuantityType(){
		
		String newName = JOptionPane.showInputDialog(null, "Qual o Nome do Novo tipo de Unidade?", "Cadastro de novo tipo de Medida", JOptionPane.PLAIN_MESSAGE);
		
		DBConector.writeDB("INSERT INTO Tipo_Quantidade VALUES(" + (quantityTypes.length - 1) + ", '" + newName + "')");
		
		Almoxarifado.cnctr.qnttTyps++;
		
		wasChanged = true;
		
		return "" + (quantityTypes.length-1);
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
			auxString += JOptionPane.showInputDialog(null, "Selecione a Montagem", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE,
					null, assembliesSO, 0);
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			System.out.println("auxString: " + auxString);
			
			auxString = getKey(auxString);
			
			break;
		case 2:
			columnName += "Description";
			auxString += JOptionPane.showInputDialog(null, "Insira a Descrição:", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Valor Agora é Nulo", "", JOptionPane.WARNING_MESSAGE);
				auxString = "---------------";
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
			auxString += JOptionPane.showInputDialog(null, "Selecione um tipo de quantidade", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE, null, quantityTypes, 0);
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			aux = 0;
			
			if(auxString.equals(quantityTypes[quantityTypes.length-1])) {
				auxString = newQuantityType();
				break;
			}
			
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
			
			System.out.println("auxString: " + auxString);
			
			auxString = auxString.replaceFirst("[,]", ".");
			auxString = auxString.replaceAll("[,]", "");
			
			System.out.println("auxString: " + auxString);
			
			break;
		case 6:
			columnName += "Supplier";
			auxString += JOptionPane.showInputDialog(null, "Insira o Fornecedor:", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Valor Agora é Nulo", "", JOptionPane.WARNING_MESSAGE);
				auxString = "---------------";
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
	
	private static HashMap<String, String> fillAssembliesName() {
		HashMap<String, String> returnHashMap = new HashMap<>();
		
		String toBreakID = DBConector.readDB("ID_Montagem", "Montagem");
		String toBreakSO = DBConector.readDB("ISO", "Montagem");
		
		assembliesID = toBreakID.split(" § \n");
		assembliesSO = toBreakSO.split(" § \n");
		
		for(int i = 0; i < Almoxarifado.quantityAssembly; i++) {
			System.out.println("ID: " + assembliesID[i] + ", SO: " + assembliesSO[i]);
			returnHashMap.put(assembliesID[i], assembliesSO[i]);
		}
		
		
		return returnHashMap;
	}
	
	private static String[] fillQuantityTypes() {
		maximumIndexQT = Almoxarifado.cnctr.qnttTyps;
		
		String returnArrayString[] = new String[maximumIndexQT + 1];
		String auxText = DBConector.readDB("Value_Tipo_Quantidade", "Tipo_Quantidade");
		
		returnArrayString = auxText.split(" § ");
		returnArrayString[maximumIndexQT] = "Adicionar Outro...";
		
		return returnArrayString;
	}
	
	private static boolean verifyString(String toVerif) {
		if(toVerif.equals("") || toVerif.equals("null") || toVerif.equals(" ")) {
			return true;
		}
		return false;
	}

	public void addPart() {
		String querry = "INSERT INTO pecas (Montagem, Description, Quantity, Quantity_type, Price, Supplier, Status) VALUES( ";

		String aux = "";
		int auxInt = 0;
		if(auxAddingFromMontagem == 0) {
			aux += JOptionPane.showInputDialog(null, "Selecione a Montagem", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE, null,
					assembliesSO, 0);
			if(verifyString(aux)) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			System.out.println("aux: " + aux);
			auxInt = Integer.parseInt(getKey(aux));
		}else {
			auxInt = auxAddingFromMontagem;
		}
		
		querry += auxInt + ", '";
		
		aux = "";
		aux += JOptionPane.showInputDialog(null, "Insira a Descrição:", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "O Valor Agora é Nulo", "", JOptionPane.WARNING_MESSAGE);
			aux = "---------------";
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
			aux = "0";
		}
		querry += aux + ", '";
		//TODO: insira uma forma de limitar o usuário a apenas usar numeros aqui;
		
		aux = "";
		aux += JOptionPane.showInputDialog(null, "Insira o Fornecedor:", "Cadastro de Nova Peça", JOptionPane.PLAIN_MESSAGE);
		if(verifyString(aux)) {
			JOptionPane.showMessageDialog(null, "Valor Agora será nulo", "", JOptionPane.WARNING_MESSAGE);
			aux = "---------------";
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
	
	private String changeQuantityType(String quantityType){
		String toReturn = "";
		int aux = Integer.parseInt(quantityType);
		if(aux < quantityTypes.length + 1) {
			toReturn = quantityTypes[aux];
		}else {
			return toReturn;
		}
		
		
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
				
				assembliesHM = fillAssembliesName();
				quantityTypes = fillQuantityTypes();
				
				maximumIndexQT = Almoxarifado.cnctr.qnttTyps;
				
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
				assembliesHM = fillAssembliesName();
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
					
					int auxCheckBox = 0;
					int maxMouse = 0;
					
					String auxTextToWrite = (finalPartsTable[i][j]);
					
					if(i > 0 && j == 1) {
						auxTextToWrite = assembliesHM.get(finalPartsTable[i][j]);
					}
					
					if(i > 0 && j == 4) {
						auxTextToWrite = changeQuantityType(finalPartsTable[i][j]);
					}
					
					switch(j) {
					case 1:
						//System.out.println("1");
						auxWidth += (total*5)/100;
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
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
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						//System.out.println("3, AuxWidth: " + auxWidth);
						break;
					case 4:
						//System.out.println("4, AuxWidth: " + auxWidth);
						auxWidth += g.getFontMetrics().stringWidth(" " + finalPartsTable[i][j-1]);
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						break;
					case 5:
						//System.out.println("5");
						auxWidth -= g.getFontMetrics().stringWidth(" " + finalPartsTable[i][j-2]);
						auxWidth += (total*19)/100;
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						//System.out.println("5, AuxWidth: " + auxWidth);
						break;
					case 6:
						//System.out.println("6");
						auxWidth += (total*11.8)/100;
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						//System.out.println("6, AuxWidth: " + auxWidth);
						break;
					case 7:
						//System.out.println("7");
						auxWidth += (total*13)/100;
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						//System.out.println("7, AuxWidth: " + auxWidth);
						break;
					}
					
					if(i != 0 && j == 2) {
						if(g.getFontMetrics().stringWidth(finalPartsTable[i][j]) > characterLimitPerLine) {
							//System.out.println("O Texto é grande demais para o Espaço delimitado");
							multipleDescriptionLinesMark = true;
						}
					}
					
					if(j == 7) {
						auxCheckBox = 15;
					}
					
					Color nC = Color.white;
						
					if(i == 0) {
						nC = Color.orange;
						if(j == 7) {
							auxWidth -= 5;
						}
					}
					if(!isEliminating) {
						if(Almoxarifado.mX > auxWidth - auxCheckBox && Almoxarifado.mX < auxWidth + maxMouse + auxCheckBox
						&& Almoxarifado.mY > auxHeight - 15 - auxCheckBox && Almoxarifado.mY < auxHeight + (g.getFontMetrics().stringWidth(finalPartsTable[i][j]) / characterLimitPerLine) * 50 + auxCheckBox 
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
								mouseStatus = false;
								return;
							}
						}
					}
					
					g.setColor(nC);
					
					if(!multipleDescriptionLinesMark) {
						g.drawString(auxTextToWrite, auxWidth, auxHeight);
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
					

					if(i > 0 && j == 7) {
						g.drawImage(checkBox, auxWidth, auxHeight + ofsetHeight + (g.getFontMetrics().getHeight() - checkBox.getHeight()), null);
						if(auxTextToWrite.equals("1")) {
							g.drawImage(check, auxWidth, auxHeight + ofsetHeight + (g.getFontMetrics().getHeight() - check.getHeight()), null);
						}
					}
					
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
