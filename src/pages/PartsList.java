package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import javax.swing.JOptionPane;

import functions.Archiver;
import functions.BidimensionalList;
import functions.DBConector;
import functions.Functions;
import functions.Searcher;
import main.Almoxarifado;
import main.UserInterface;

public class PartsList implements BidimensionalList{
	//Instancia as variaveis de controle e de conteudo.
	private boolean isOnTheRightState = false;
	
	public String toSplit = DBConector.readDB("*", "pecas");
	public static String finalPartsTable[][] = new String[Almoxarifado.quantityParts + 1][8];
	static HashMap<String, String> assembliesHM = fillAssembliesName();
	static String[] assembliesID;
	static String[] assembliesSO;
	public static boolean restartAssemblyList = false;
	
	public int offsetHeight;
	public static int scroll;
	public static int maximumHeight = 1;
	
	private boolean toggleScrollBar = false;
	private int thumbWidth = 18;
	public int thumbHeight = 0;
	private double thumbAuxY = 0;
	public boolean isDragging = false;
	
	int total = Almoxarifado.WIDTH - 50*2;
	int characterLimitPerLine = (int) ((total*28)/100);;
	boolean multipleDescriptionLinesMark = false;
	int auxHeight = 125 + offsetHeight;
	int auxWidth = 50;		
	int descriptionOffsetHeight = 1;
	
	//Instancia as variáveis de interação com o usuário.
	public boolean mouseStatus = false;
	
	static boolean wasChanged = true;
	
	boolean isEliminating = false;
	int indexToEliminate = -1;
	
	BufferedImage adicionar = Almoxarifado.imgManag.getSprite(475, 360, 165, 60);
	BufferedImage excluir = Almoxarifado.imgManag.getSprite(475, 480, 165, 60);
	BufferedImage check = Almoxarifado.imgManag.getSprite(452, 371, 21, 21);
	BufferedImage checkBox = Almoxarifado.imgManag.getSprite(455, 395, 18, 18);
	
	private String orderColumn = "ID";
	
	public boolean isWriting = false;
	public boolean isSearching = false;
	private String textToSearch = "";
	private int indexToWrite = 0;
	BufferedImage searchIcon = Almoxarifado.imgManag.getSprite(450, 2, 24, 24);
	BufferedImage bipartitionLine = Almoxarifado.imgManag.getSprite(442, 0, 3, 35);
	
	boolean blink = false;
	int blinkAux = 0;

	/**
	 * Cria a lista de Peças para ser desenhada posteriormente.
	 */
	public PartsList() {
		finalPartsTable = listBreaker(toSplit);
		
		System.out.println("Carregou Lista de Peças: " + LocalDateTime.now());
	}
	
	/**
	 * Pega a primeira chave de uma HashMap dentro do 
	 * assembliesHM HashMap que bate com o conteudo no 
	 * parametro.
	 * 
	 * @param value - Texto qualquer que possa estar na assembliesHM.
	 * @return Valor da primeira chave encontrada com esse conteudo.
	 */
	private static String getKey(String value) {
		for (Entry<String, String> entry : assembliesHM.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
		
		return "";
	}
	
	/**
	 * Função que pega uma String e transforma ela na 
	 * matiz para ser desenhada. Essa pega a variável 
	 * e separa ela em linhas e então colunas, separando 
	 * as linhas pelo conjunto de caracteres "\n" e as 
	 * colunas pelo conjunto de caracteres " § ".
	 * 
	 * @param toSplit - Lista de informações do banco de dados.
	 * @return Matriz bidimensional com os dados separados em linhas e colunas.
	 */
	private String[][] listBreaker(String toSplit){
		String linesToBreakdown[] = toSplit.split("\n");
		String returnString[][] = new String[linesToBreakdown.length + 1][8];
		
		returnString[0][0] = "ID";
		returnString[0][1] = "Montagem";
		returnString[0][2] = "Descrição";
		returnString[0][3] = "Quantidade";
		returnString[0][4] = "Preço";
		returnString[0][5] = "Data do Pedido";
		returnString[0][6] = "Fornecedor";
		returnString[0][7] = "Status";
		if(!toSplit.equals("")) {
			for(int i = 0; i < linesToBreakdown.length; i++) {
				returnString[i + 1] = linesToBreakdown[i].split(" § ");
			}
		}else {
			returnString = new String[1][8];
			
			returnString[0][0] = "ID";
			returnString[0][1] = "Montagem";
			returnString[0][2] = "Descrição";
			returnString[0][3] = "Quantidade";
			returnString[0][4] = "Preço";
			returnString[0][5] = "Data do Pedido";
			returnString[0][6] = "Fornecedor";
			returnString[0][7] = "Status";
		}
		return returnString;
	}
	
	/**
	 * Função que altera os dados da lista de peças usando 
	 * o index e a column, assim achando o ID e a Coluna 
	 * respectivamente
	 * 
	 * @param index - Alinhamento horizontal da matrix.
	 * @param column - Alinhamento vertical da matrix.
	 */
	public static void changePart(String index, int column) {
		String columnName =  "";
		String auxString = "";
		
		switch(column){
		case 1:
			columnName += "Montagem";
			auxString += JOptionPane.showInputDialog(null, "Selecione a Montagem", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE,
					null, assembliesSO, 0);
			
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Valor não Inserido", "Retornando", JOptionPane.WARNING_MESSAGE);
				return;
			}else {
				if(auxString.equals("")) {
					auxString = autoFill(column);
					JOptionPane.showMessageDialog(null, "Valor será considerado nulo", "Modificação Concluida", JOptionPane.WARNING_MESSAGE);
					break;
				}
			}
			
			auxString = getKey(auxString);
			
			break;
		case 2:
			columnName += "Description";
			auxString += JOptionPane.showInputDialog(null, "Insira a Descrição:", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Valor não Inserido", "Retornando", JOptionPane.WARNING_MESSAGE);
				return;
			}else {
				if(auxString.equals("")) {
					auxString = autoFill(column);
					JOptionPane.showMessageDialog(null, "Valor será considerado nulo", "Modificação Concluida", JOptionPane.WARNING_MESSAGE);
					break;
				}
			}
			
			break;
		case 3:
			columnName += "Quantity";
			auxString += JOptionPane.showInputDialog(null, "Insira a quantidade de Peças (apenas numeros)", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Valor não Inserido", "Retornando", JOptionPane.WARNING_MESSAGE);
				return;
			}else {
				if(auxString.equals("")) {
					auxString = autoFill(column);
					JOptionPane.showMessageDialog(null, "Valor será considerado nulo", "Modificação Concluida", JOptionPane.WARNING_MESSAGE);
					break;
				}
			}
			
			break;
		case 4:
			columnName += "Price";
			auxString += JOptionPane.showInputDialog(null, "Insira o Valor da Peça (apenas numeros)", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			
			auxString = formatNumb(auxString);
			
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Valor não Inserido", "Retornando", JOptionPane.WARNING_MESSAGE);
				return;
			}else {
				if(auxString.equals("")) {
					auxString = autoFill(column);
					JOptionPane.showMessageDialog(null, "Valor será considerado nulo", "Modificação Concluida", JOptionPane.WARNING_MESSAGE);
					break;
				}
			}
			
			break;
		case 5:
			
			break;
		case 6:
			columnName += "Supplier";
			auxString += JOptionPane.showInputDialog(null, "Insira o Fornecedor:", "Modificação da Peça", JOptionPane.PLAIN_MESSAGE);
			
			if(verifyString(auxString)) {
				JOptionPane.showMessageDialog(null, "Valor não Inserido", "Retornando", JOptionPane.WARNING_MESSAGE);
				return;
			}else {
				if(auxString.equals("")) {
					auxString = autoFill(column);
					JOptionPane.showMessageDialog(null, "Valor será considerado nulo", "Modificação Concluida", JOptionPane.WARNING_MESSAGE);
					break;
				}
			}
			
			break;
		case 7:
			columnName += "Status";
			if(DBConector.readDB("Status", "pecas", "ID_Parts", index).equals("1 § \n")) {
				auxString = "0";
			}else {
				auxString = "1";
			}
			break;
		}
		
		auxString = correctQuotation(auxString);
		
		if(!columnName.equals("")) {
			Archiver.writeOnArchive("alteracao", "a peça ID_Parts." + index, DBConector.readDB(columnName, "pecas", "ID_Parts", index).replaceAll(" § \n", ""), auxString);
			DBConector.writeDB("pecas", columnName, auxString, "ID_Parts", index);
		}
		
		wasChanged = true;
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	static String formatNumb(String text) {
		String toReturn = "";
		
		String floatAux = text.replace(",", "§");
		floatAux = floatAux.replace(".", "§");
		
		String letterAux = floatAux.replaceAll("[^0-9§]", "");
		
		toReturn = letterAux.replaceFirst("§", ".");
		toReturn = toReturn.replaceAll("§", "");
		
		return toReturn;
	}
	
	/**
	 * Preenche o Hashmap contendo os valores da lista de Montagens 
	 * com os ids e OS.
	 * 
	 * @return HashMap com Ids e as Ordens de Serviços das montagens.
	 */
	public static HashMap<String, String> fillAssembliesName() {
		HashMap<String, String> returnHashMap = new HashMap<>();
		
		String toBreakID = DBConector.readDB("ID_Montagem", "Montagem");
		String toBreakSO = DBConector.readDB("ISO", "Montagem");
		
		assembliesID = toBreakID.split(" § \n");
		assembliesSO = toBreakSO.split(" § \n");
		
		for(int i = 0; i < Almoxarifado.quantityAssembly; i++) {
			returnHashMap.put(assembliesID[i], assembliesSO[i]);
		}
		
		
		return returnHashMap;
	}

	@Override
	public String getColumn(int i) {
		//System.out.println("Valor da Coluna " + finalPartsTable[firstLine][i]);
		return finalPartsTable[firstLine][i];
	}
	
	/**
	 * Verifica se o valor da String não é valido, respondendo a 
	 * variável de acordo.
	 * @param text - String para ser verificada.
	 * @return true se o valor for igual a null, 
	 * 		   false caso contrario.
	 */
	private static boolean verifyString(String text) {
		if(text.equals("null")) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Valores genericos para serem inseridos ao adicionar texto.
	 * @deprecated Use o metodo fillDefaultValues.
	 * @param index - Colunas da tabela do banco de dados.
	 * @return - String generica adequada para a tabela de peças.
	 */
	private static String autoFill(int index) {
		String toReturn = "";
		
		switch(index) {
		case 2:
		case 6:
			toReturn = "------------";
			break;
		case 1:
		case 4:
			toReturn = "1";
			break;
		default:
			toReturn = "0";
			break;
		}
		
		return toReturn;
	}

	/**
	 * Modifica o texto para que ele seja compativel com o 
	 * metodo {@link functions.DBConector#writeDB(String)}, 
	 * substituindo as aspas duplas por duas aspas simples.
	 * 
	 * @param text - Texto para ser formatado.
	 * @return texto formatado.
	 */
	private static String correctQuotation(String text) {
		text = text.replaceAll("\"", "''");
		
		return text;
		
	}
	
	/**
	 * Verifica se a lista de montagens está positiva e maior 
	 * que zero. No caso de sim o State é alterado para 
	 * addPeças, caso contrário o state muda para a pagina de 
	 * lista de montagem. 
	 */
	public void addPart() {
		if(Almoxarifado.quantityAssembly > 0) {
			Almoxarifado.setState(6);
		}else {
			JOptionPane.showMessageDialog(null, "Para Adicionar uma peça é necessário uma Montagem", "Erro - Não Existe Montagem", JOptionPane.ERROR_MESSAGE, null);
			Almoxarifado.setState(3);
		}
	}
	
	/**
	 * Verifica se o usuário confirma o removimento da peça em 
	 * questão, caso negativo o metodo retorna, caso contrario 
	 * o sistema remove da lista de pecas as peças com esse valor.
	 * @param index - ID da peça para ser removida.
	 */
	public void eliminatePart(int index){
		int confirmation = JOptionPane.showConfirmDialog(null, "Você tem *CERTEZA* que você deseja deletar essa peça?", "Confirma a Eliminação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if(confirmation != 0) {
			return;
		}
		
		DBConector.writeDB("DELETE FROM pecas WHERE ID_Parts = " + index);
		Archiver.writeOnArchive("remocao", "ID_Parts", "" + index, "");
		Almoxarifado.quantityParts--;
		
		DBConector.writeDB("UPDATE pecas SET ID_Parts = (ID_Parts-1) WHERE ID_Parts > " + index);
		
		offsetHeight += 15;
		
		int Y = (UserInterface.maximunHeight - 16) - thumbHeight;
		double S = (Double.parseDouble("" + maximumHeight) / Double.parseDouble("" + offsetHeight));
		thumbAuxY = Y / S;
		
		wasChanged = true;
	}
	
	/**
	 * Posiciona o Scroll.
	 */
	public void scrollPositioner() {
		if(offsetHeight < PartsList.maximumHeight * -1) {
			offsetHeight = PartsList.maximumHeight * -1;
		}if(offsetHeight > 0) {
			offsetHeight = 0;
		}
		
		int Y = (UserInterface.maximunHeight - 18) - thumbHeight;
		double S = (Double.parseDouble("" + maximumHeight) / Double.parseDouble("" + offsetHeight));
		thumbAuxY = Y / S;
	}
	
	/**
	 * Pega o texto para efetuar a pesquisa por uma peça em 
	 * especifico, adicionando caractere por caractere para 
	 * criar o texto que será usado para efetuar a consulta 
	 * do banco de dados.
	 * 
	 * @param e - Evento de pressionar uma tecla do teclado.
	 */
	public void getText(KeyEvent e) {
		String textInserted = "";
		
		textInserted = textToSearch;
		
		if(indexToWrite == 0) {
			if(!((e.getExtendedKeyCode() > -1 && e.getExtendedKeyCode() < 21) || 
				 (e.getExtendedKeyCode() > 126 && e.getExtendedKeyCode() < 160))) {
				textInserted += e.getKeyChar();
			}else {
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && textInserted.length() > 0) {
					textInserted = textInserted.substring(0, textInserted.length() - 1);
				}
			}
		}else {
			textInserted = advancedWriter(textInserted, e);
		}
		
		isSearching = true;
		wasChanged = true;
		
		textToSearch = textInserted;
		
		if(textToSearch.equals("")) {
			orderColumn = getColumn(0);
			isSearching = false;
		}
	}
	
	/**
	 * Tecnica de texto avançada, basicamente vai concatenar o 
	 * valor da string antiga de 0 até o indice, e posteriormente 
	 * concatena também com o restante do texto.
	 * 
	 * @param text
	 * @param e
	 * @return
	 */
	private String advancedWriter(String text, KeyEvent e) {
		String toReturn = "";
		
		return toReturn;
	}
	
	/**
	 * Lógica dessa página, verifica o valor para ser ordenado, 
	 * atualiza o valor da lista de peças, preenche o valor da 
	 * lista de montagens, controla o scroll do mouse e verifica 
	 * se é necessario fazer a atualização das listas.
	 */
	public void tick() {
		if(wasChanged == true) {
			if(!isSearching) {
				toSplit = DBConector.readDB(Searcher.orderByColumn(orderColumn, "Pecas"));
			}else {
				if(!textToSearch.equals("")) {
					System.out.println("O Valor de OrderColumn é " + orderColumn);
					toSplit = DBConector.readDB(Searcher.searchEngine(textToSearch, orderColumn, "Pecas"));
				}
			}
			finalPartsTable = listBreaker(toSplit);
			
			assembliesHM = fillAssembliesName();
						
			wasChanged = false;
		}
		if(Almoxarifado.getState() == Almoxarifado.partsListState) {
			isOnTheRightState = true;
			Almoxarifado.frame.setTitle("Almoxarifado - Lista de Peças");
		}else {
			isOnTheRightState = false;
			thumbAuxY = 0;
			offsetHeight = 0;
		}
		
		if(isOnTheRightState) {
			if(maximumHeight > 15) {
				toggleScrollBar = true;
			}else {
				toggleScrollBar = false;
			}
			
			if(scroll > 1) {
				offsetHeight -= UserInterface.spd;
				
				scrollPositioner();
				
				scroll = 0;
			}else if(scroll < -1 && offsetHeight < 0) {
				offsetHeight += UserInterface.spd;
				
				scrollPositioner();
				
				scroll = 0;
			}
			
			if(mouseStatus) {
				if(Almoxarifado.mX > Almoxarifado.WIDTH - (36 + 21) && Almoxarifado.mX < Almoxarifado.WIDTH - (36 + 21) + 20
				&& Almoxarifado.mY > UserInterface.bttnY + UserInterface.boxHeight + 20 - (int) (thumbAuxY) 
				&& Almoxarifado.mY < UserInterface.bttnY + UserInterface.boxHeight + 20 - (int) (thumbAuxY) + thumbHeight) {
					isDragging = true;
				}else if(Almoxarifado.mX > Almoxarifado.WIDTH - (36 + 22) && Almoxarifado.mX < Almoxarifado.WIDTH - (36 + 22) + thumbWidth
					 && Almoxarifado.mY > UserInterface.bttnY + UserInterface.boxHeight + 18
					 && Almoxarifado.mY < UserInterface.bttnY + UserInterface.boxHeight + UserInterface.maximunHeight + 6) {
					
				}
			}else {
				isDragging = false;
			}
		}
		
		if(restartAssemblyList) {
			assembliesHM = fillAssembliesName();
			wasChanged = true;
			restartAssemblyList = false;
		}
	}
	
	/**
	 * Desenha a lista de montagens, a área de pesquisa e os botões.
	 * 
	 * @param g - Mecanismo de desenho na Janela.
	 */
	public void render(Graphics g) {
		
		g.setColor(Color.white);
		g.setFont(new Font("segoe ui", 1, 40));
		g.drawString("Lista de Peças: ", Almoxarifado.WIDTH/8 - 30, 180 + offsetHeight);
		
		UserInterface.createTextBox(g, new Rectangle(
				Almoxarifado.WIDTH/8 + g.getFontMetrics().stringWidth("Lista de Peças: "), 
				145 + offsetHeight, 
				Almoxarifado.WIDTH - Almoxarifado.WIDTH/4 - g.getFontMetrics().stringWidth("Lista de Peças: "), 
				40
			), 15);
		g.drawImage(searchIcon, Almoxarifado.WIDTH/8 + Almoxarifado.WIDTH - Almoxarifado.WIDTH/4 - 36, 154 + offsetHeight, null);
		g.drawImage(bipartitionLine, Almoxarifado.WIDTH/8 + Almoxarifado.WIDTH - Almoxarifado.WIDTH/4 - 50, 148 + offsetHeight, null);
				
		if(mouseStatus) {
			if(Functions.isOnBox(Almoxarifado.WIDTH/8 + g.getFontMetrics().stringWidth("Lista de Peças: "), 145 + offsetHeight, 
				Almoxarifado.WIDTH - Almoxarifado.WIDTH/4 - g.getFontMetrics().stringWidth("Lista de Peças: "), 40)) {
				isWriting = true;
			}else {
				isWriting = false;
				blinkAux = 0;
			}
		}
		
		blinkAux++;
		if(blinkAux % 24 == 0) {
			if(blink) {
				blink = false;
			}else {
				blink = true;
			}
		}
		
		g.setColor(Color.black);
		g.setFont(new Font("segoi ui", 0, 20));
		g.drawString(textToSearch, 12 + Almoxarifado.WIDTH/8 + g.getFontMetrics(new Font("segoe ui", 1, 40)).stringWidth("Lista de Peças: "), 173 + offsetHeight);
		if(isWriting && blink) {
			g.fillRect(12 + Almoxarifado.WIDTH/8 + g.getFontMetrics(new Font("segoe ui", 1, 40)).stringWidth("Lista de Peças: ") + g.getFontMetrics().stringWidth(textToSearch), 150 + offsetHeight, 1, 30);
		}
		
		if(isOnTheRightState) {
			
			g.setFont(new Font("segoe ui", 0, 12));

			auxHeight = 240 + offsetHeight;
			auxWidth = 50;
			descriptionOffsetHeight = 1;
			
			for(int i = 0; i < finalPartsTable.length; i++) {

				for(int j = 0; j < 8; j++) {
					
					int auxCheckBox = 0;
					int maxMouse = 0;
					
					String auxTextToWrite = (finalPartsTable[i][j]);
					
					Color nC = Color.white;
					
					if(i > 0 && j == 1) {
						auxTextToWrite = assembliesHM.get(finalPartsTable[i][j]);
					}
					
					if(i > 0 && j == 4) {
						auxTextToWrite = "R$ " + finalPartsTable[i][j];
					}
					
					switch(j) {
					case 0:
						//ID
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						break;
					case 1:
						//Montagem
						auxWidth += (total*4.5)/100;
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						break;
					
					case 2:
						//Descrição
						auxWidth += (total*8)/100;
						maxMouse = characterLimitPerLine;
						break;
					
					case 3:
						//Quantidade
						auxWidth += (total*37)/100;
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						break;
					
					case 4:
						//Preço;
						auxWidth += (total*9)/100;
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						break;
					
					case 5:
						//Data do Pedido
						auxWidth += (total*8)/100;
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						break;
					
					case 6:
						//Fornecedor
						auxWidth += (total*11.8)/100;
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						break;
					
					case 7:
						//Status;
						auxWidth += (total*18)/100;
						maxMouse = g.getFontMetrics().stringWidth(auxTextToWrite);
						break;
					}
					
					if(i != 0 && j == 2) {
						if(g.getFontMetrics().stringWidth(finalPartsTable[i][j]) > characterLimitPerLine) {
							multipleDescriptionLinesMark = true;
						}
					}
					
					if(j == 7) {
						auxCheckBox = 15;
					}
						
					if(i == 0) {
						nC = Color.orange;
						if(j == 7) {
							auxWidth -= 5;
						}
					}
					if(!isEliminating) {
						if(Almoxarifado.mX > auxWidth && Almoxarifado.mX < auxWidth + maxMouse
						&& Almoxarifado.mY > auxHeight - 15 - auxCheckBox && Almoxarifado.mY < auxHeight + (g.getFontMetrics().stringWidth(finalPartsTable[i][j]) / characterLimitPerLine) * 50 + auxCheckBox 
						&& i == 0) {
							nC = new Color(255, 00, 102);
							
							if(mouseStatus) {
								orderColumn = getColumn(j);
								wasChanged = true;
								mouseStatus = false;
								Searcher.alternateDirecion();
							}
						}
						
						if(Almoxarifado.mX > auxWidth - auxCheckBox && Almoxarifado.mX < auxWidth + maxMouse + auxCheckBox
						&& Almoxarifado.mY > auxHeight - 15 - auxCheckBox && Almoxarifado.mY < auxHeight + (g.getFontMetrics().stringWidth(finalPartsTable[i][j]) / characterLimitPerLine) * 50 + auxCheckBox 
						&& i != 0 && auxHeight > 120 && j != 0) {
						
							nC = Color.red;
							if(mouseStatus) {
								changePart(finalPartsTable[i][0], j);
								Project.ID = Integer.parseInt(finalPartsTable[i][1]);
								Almoxarifado.project.updater();
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
								int idToEliminate = Integer.parseInt(finalPartsTable[indexToEliminate][0]);
								eliminatePart(idToEliminate);
								isEliminating = false;
								mouseStatus = false;
								return;
							}
						}
						
						if(Almoxarifado.mX > auxWidth - auxCheckBox && Almoxarifado.mX < auxWidth + maxMouse + auxCheckBox
								&& Almoxarifado.mY > auxHeight - 15 - auxCheckBox && Almoxarifado.mY < auxHeight + (g.getFontMetrics().stringWidth(finalPartsTable[i][j]) / characterLimitPerLine) * 50 + auxCheckBox 
								&& i == 0) {
									nC = new Color(255, 00, 102);
									
									if(mouseStatus) {
										System.out.println("Selecionou a coluna " + getColumn(j) + " de Indice " + j);
									}
								}
					}
					
					g.setColor(nC);
					
					if(i == 0) {
						g.drawImage(upIndicator, auxWidth + g.getFontMetrics().stringWidth(auxTextToWrite) + 3, auxHeight - (g.getFontMetrics().getHeight()/3) * 2 - 3,null);
						g.drawImage(downIndicator, auxWidth + g.getFontMetrics().stringWidth(auxTextToWrite) + 3, auxHeight - g.getFontMetrics().getHeight()/3,null);
					}
					
					if(!multipleDescriptionLinesMark) {
						g.drawString(auxTextToWrite, auxWidth, auxHeight);
					}else {
						String auxText = "";
						int quantityOfLines = g.getFontMetrics().stringWidth(finalPartsTable[i][j])/characterLimitPerLine;
						descriptionOffsetHeight += quantityOfLines;
						double quantityOfCharacters = finalPartsTable[i][j].length()/quantityOfLines;
						
						for(int inc = 0; inc < quantityOfLines+1; inc++) {
							int start = (int) (quantityOfCharacters*(inc));
							int end = (int) (quantityOfCharacters*(inc+1));
														
							if(end > finalPartsTable[i][j].length()) {
								end = finalPartsTable[i][j].length();
							}

							auxText = finalPartsTable[i][j].substring(start, end);
							
							char verifFormat = finalPartsTable[i][j].charAt(end-1);
							if(verifFormat != ' ' && verifFormat != ',' && verifFormat != '.' && verifFormat != '-'){
								verifFormat = '-';
							}else {
								verifFormat = ' ';
							}
							
							
							g.drawString(auxText + verifFormat, 0 + auxWidth, 0 + auxHeight + 30 * (inc));
							
						}
					}
					
					multipleDescriptionLinesMark = false;
					
					if(i > 0 && j == 7) {
						g.drawImage(checkBox, auxWidth - 5, (int) (auxHeight + (g.getFontMetrics().getHeight() - checkBox.getHeight()*1.5)), null);
						if(auxTextToWrite.equals("1")) {
							g.drawImage(check, auxWidth - 5, (int) (auxHeight + (g.getFontMetrics().getHeight() - check.getHeight()*1.5)), null);
						}
					}
					
				}
				auxWidth = 50;
				auxHeight += 50*descriptionOffsetHeight;
				descriptionOffsetHeight = 1;
			}

			if(mouseStatus) {
				if(Almoxarifado.mX > Almoxarifado.WIDTH/3 - adicionar.getWidth()/2	&& Almoxarifado.mX < Almoxarifado.WIDTH/3 + adicionar.getWidth()/2
				&& Almoxarifado.mY > auxHeight - 5 && Almoxarifado.mY < auxHeight + 69) {
					addPart();
					mouseStatus = false;
				}else if(Almoxarifado.mX > Almoxarifado.WIDTH/3*2 - excluir.getWidth()/2	&& Almoxarifado.mX < Almoxarifado.WIDTH/3*2 + excluir.getWidth()/2
				&& Almoxarifado.mY > auxHeight - 5 && Almoxarifado.mY < auxHeight + 69) {
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
			
			UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH/3 - adicionar.getWidth()/2, auxHeight);
			UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH/3*2 - excluir.getWidth()/2, auxHeight);
			
			maximumHeight = (auxHeight - offsetHeight) - UserInterface.maximunHeight;
			
			if(toggleScrollBar) {
				
				g.setColor(Color.darkGray);
				g.fillRect(Almoxarifado.WIDTH - (36 + 22), UserInterface.bttnY + UserInterface.boxHeight + 18, 20, UserInterface.maximunHeight - 12);
				
				if(maximumHeight > 0) {
					thumbHeight = (int) ((UserInterface.maximunHeight - 16) - (((UserInterface.maximunHeight - 32) * (maximumHeight / 16)) / 100));
					if(thumbHeight < 30) {
						thumbHeight = 30;
					}
				}
				
				g.setColor(Color.lightGray);
				if(isDragging) {
					g.setColor(Color.gray);
				}
				g.fillRect(Almoxarifado.WIDTH - (36 + 21), UserInterface.bttnY + UserInterface.boxHeight + 20 - (int) (thumbAuxY), thumbWidth, thumbHeight);
			}
		}
	}
}
