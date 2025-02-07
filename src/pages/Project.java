package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import functions.Archiver;
import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class Project {
	//Dados da Montagem.
	static int ID = 1;
	static String name = "";
	static String description = "";
	static String company = "";
	static String imgAdress = "";
	
	static double price = 0.0;
	
	private String rawPartsList = "";
	private ArrayList<String> separetedList = new ArrayList<>();
	
	//Adiciona as imagens, como a imagem da montagem, de editar informações
	BufferedImage img;
	BufferedImage editAssembly = Almoxarifado.imgManag.getSprite(475, 60*3, 165, 60);
	BufferedImage archiveAssembly = Almoxarifado.imgManag.getSprite(475, 60*4, 165, 60);
	BufferedImage isEditingAssembly = Almoxarifado.imgManag.getSprite(165, 510, 165, 60);
	BufferedImage add = Almoxarifado.imgManag.getSprite(475, 60*6, 165, 60);
	BufferedImage remove = Almoxarifado.imgManag.getSprite(475, 60*8, 165, 60);
	BufferedImage check = Almoxarifado.imgManag.getSprite(452, 371, 21, 21);
	BufferedImage checkBox = Almoxarifado.imgManag.getSprite(455, 395, 18, 18);
	
	//Instanciando variaveis de manipulação do usuário.'
	public static boolean updateProject = true;
	private boolean isOnTheRightState = false;

	public boolean mouseStatus = false;

	public static int scroll;
	public int offsetHeight = 0;
	public int maximumHeight = 1;
	
	private boolean toggleScrollBar = false;
	private int thumbWidth = 18;
	public int thumbHeight = 0;
	private double thumbAuxY = 0;
	public boolean isDragging = false;
	
	boolean isEditing = false;
	boolean isArchiving = false;
	
	boolean isEliminating = false;
			
	//Variáveis de Posicionamento e Aparencias.
	int imgX = UserInterface.bttnX[0] + 50;
	int imgY = Almoxarifado.HEIGHT - UserInterface.maximunHeight - 20;
	
	int nameSize = 0;
	int descriptionSize = 0;
	int companySize = 0;
	boolean isOverName = false;
	boolean isOverDescription = false;
	boolean isOverCompany = false;
	int positionerX = 52;
	int positionerY = imgY + 224;
	int auxTextWidth = 0;
	int auxTextHeight = 0;
	int total = Almoxarifado.WIDTH - (positionerX*2);
	
	boolean multipleDescriptionMark = false;
	int sizeOfPartDescription = 0;
	int maxTextSize = (int) (((total*40)/100) - ((total*5.5)/100));
	int auxH = 0;
	
	/**
	 * Classe da pagina do projeto em sí, contendo uma lista 
	 * de peças, atributos da montagem e a imagem da montagem.
	 */
	public Project() {
		System.out.println("Carregou Pagina de Projeto: " + LocalDateTime.now());
	}
	
	/**
	 * Checagem da caixa de dialogo, retorna positivo por padrão, 
	 * porém se o resultado da String for igual a vazio ou nulo, 
	 * retorna falso.
	 * 
	 * @param toVerif - String para ser verificada a sua validade.
	 * @return falso se tiver conteudo (diferente de "", "null" e " "), caso contrario true.
	 */
	private boolean checkDialog(String toVerif) {
		if(toVerif.equals("") || toVerif.equals("null") || toVerif.equals(" ")) {
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo de atualizar a lista de Montagens.
	 */
	public void updater() {
		String brokenApartInfo[];
		String aux = DBConector.readDB("*", "montagem", "ID_Montagem", "" + ID);
		
		brokenApartInfo = aux.split(" § ");
		
		name = brokenApartInfo[1];
		description = brokenApartInfo[2];
		company = brokenApartInfo[3];
		imgAdress = brokenApartInfo[4];
		
		if(!checkDialog(imgAdress)) {
			imgAdress = "ProjetoBetaImg";
		}
		
		img = Almoxarifado.imgManag.getProjectImage(imgAdress);
		
		if(imgAdress.equals("ProjetoBetaImg")) {
			img = Almoxarifado.imgManag.getSprite(0, 180, 150, 200);
		}
		
		rawPartsList = "";
		rawPartsList += DBConector.readDB("*", "pecas", "montagem", "" + ID);
		separetedList = toArrayList(rawPartsList);
		
		price = DBConector.getAssemblyValue("" + ID).doubleValue();
		
		DBConector.writeDB("Montagem", "cost", "" + price, "ID_Montagem", "" + ID);
		
		Almoxarifado.frame.setTitle("Almoxarifado - Projeto: " + Project.name);
		
		PartsList.assembliesHM = PartsList.fillAssembliesName();
	}
	
	/**
	 * 
	 */
	public void scrollPositioner() {
		if(offsetHeight < (maximumHeight * -1)) {			
			offsetHeight = maximumHeight * -1;
		}if(offsetHeight > 0) {		
			offsetHeight = 0;
		}
		
		int Y = (UserInterface.maximunHeight - 18) - thumbHeight;
		double S = (Double.parseDouble("" + maximumHeight) / Double.parseDouble("" + offsetHeight));
		thumbAuxY = Y / S;
	}
	
	/**
	 * Verifica e Ativa as funções desejadas pelo usuário na página de Montagens, 
	 */
	public void tick() {
		if(Almoxarifado.getState() == Almoxarifado.assemblyState) {
			isOnTheRightState = true;
		}else {
			isOnTheRightState = false;
			thumbAuxY = 0;
			offsetHeight = 0;
		}
		
		if(updateProject) {
			updater();
			
			updateProject = false;
		}
		
		if(isOnTheRightState) {
			if(maximumHeight > 0) {
				toggleScrollBar = true;
				thumbHeight = (int) ((UserInterface.maximunHeight - 16) - (((UserInterface.maximunHeight - 32) * (maximumHeight / 16)) / 100));
				if(thumbHeight < 30) {
					thumbHeight = 30;
				}
			}else {
				toggleScrollBar = false;
				scroll = 0;
			}
			
			if(scroll > 0) {
				offsetHeight -= UserInterface.spd;
				
				scrollPositioner();
				
				scroll = 0;
			}else if(scroll < 0) {
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
			
			if(isEditing) {
				//TODO: Sistema de distancia dinamico pro limite da largura do quadrado
				if(Almoxarifado.mX > imgX + 15 + img.getWidth() && Almoxarifado.mX < Almoxarifado.WIDTH/2) {
					if(Almoxarifado.mY > imgY - 10 + offsetHeight && Almoxarifado.mY < imgY + 60 + offsetHeight) {
						isOverName = true;
						isOverDescription = false;
						isOverCompany = false;
					}
					else if(Almoxarifado.mY > imgY + 75 + offsetHeight && Almoxarifado.mY < imgY + 125 + offsetHeight) {
						isOverName = false;
						isOverDescription = false;
						isOverCompany = true;
					}else if(Almoxarifado.mY > imgY + 135 + offsetHeight && Almoxarifado.mY < imgY + 200 + offsetHeight) {
						isOverName = false;
						isOverDescription = true;
						isOverCompany = false;
					}
				}else {
					isOverName = false;
					isOverDescription = false;
					isOverCompany = false;
				}
				
				if(mouseStatus) {
					if(isOverName) {
						
						String newName = "";
						newName += JOptionPane.showInputDialog(null, "Insira um novo nome", "Atualizar dados da Montagem", JOptionPane.PLAIN_MESSAGE);
						
						if(!checkDialog(newName)) {
							
							JOptionPane.showInternalMessageDialog(null, "Erro ao Atualizar nome", "Erro", JOptionPane.ERROR_MESSAGE);
							mouseStatus = false;							
							
						}else {
							Archiver.writeOnArchive("alteracao", "o Projeto de ID = " + ID, name, newName);
							DBConector.writeDB("UPDATE montagem SET ISO = \"" + newName + "\" WHERE ID_Montagem = " + ID);
							JOptionPane.showInternalMessageDialog(null, "Nome Atualizado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
							updateProject = true;
							ProjectList.updateProjectList = true;
							mouseStatus = false;
						}
						
						
						
					}else if(isOverDescription) {
						
						String newDescription = "";
						
						newDescription += JOptionPane.showInputDialog(null, "Insira uma nova descrição", "Atualizar dados da Montagem", JOptionPane.PLAIN_MESSAGE);
						if(!checkDialog(newDescription)) {
							
							JOptionPane.showInternalMessageDialog(null, "Erro ao Atualizar a Descrição", "Erro", JOptionPane.ERROR_MESSAGE);
							mouseStatus = false;							
							
						}else {
							Archiver.writeOnArchive("alteracao", "o Projeto de ID = " + ID, description, newDescription);
							DBConector.writeDB("UPDATE montagem SET Description = \"" + newDescription + "\" WHERE ID_Montagem = " + ID);
							JOptionPane.showInternalMessageDialog(null, "Descrição Atualizada", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
							updateProject = true;
							mouseStatus = false;
						}
						
						
					}else if(isOverCompany) {
						String newCompany = "";
						newCompany += JOptionPane.showInputDialog(null, "Insira o novo nome da Empresa", "Atualizar dados da Montagem", JOptionPane.PLAIN_MESSAGE);
						if(!checkDialog(newCompany)) {
							JOptionPane.showInternalMessageDialog(null, "Erro ao Atualizar o Nome da Empresa", "Erro", JOptionPane.ERROR_MESSAGE);
							mouseStatus = false;
							
						}else {
							Archiver.writeOnArchive("alteracao", "O Projeto de ID = " + ID, company, newCompany);
							DBConector.writeDB("UPDATE montagem SET Company = \"" + newCompany + "\" WHERE ID_Montagem = " + ID);
							JOptionPane.showInternalMessageDialog(null, "Empresa Atualizada", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
							updateProject = true;
							mouseStatus = false;
						}
					}
				
				}
				
			}
			
			if(isArchiving) {
				int toVerif = JOptionPane.showConfirmDialog(null, "Realmente deseja Arquivar o Projeto", "Arquivo do Projeto", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				
				if(toVerif != 0) {
					JOptionPane.showMessageDialog(null, "Arquivo Cancelado", "", JOptionPane.PLAIN_MESSAGE);
					isArchiving = false;
					
					return;
				}
				
				DBConector.Archive("" + ID);
				Archiver.writeOnArchive("arquivo", "o Projeto de ID = " + ID, "", "");
				
				PartsList.wasChanged = true;
				ProjectList.updateProjectList = true;
				Almoxarifado.quantityParts = DBConector.counterOfElements("Pecas");
				
				isArchiving = false;
				Almoxarifado.setState(3);
			}
			
			if(mouseStatus) {
				if(Almoxarifado.mX > Almoxarifado.WIDTH - 128 - 100 && Almoxarifado.mX < Almoxarifado.WIDTH - 128 - 100 + 165) {
					if(Almoxarifado.mY >  imgY + offsetHeight && Almoxarifado.mY <  imgY + offsetHeight + 64) {
						if(isEditing) {
							isEditing = false;
						}else {
							isEditing = true;
						}
						mouseStatus = false;
						
					}else if(Almoxarifado.mY > imgY + 64 + (img.getHeight() - 64*2) + offsetHeight
						  && Almoxarifado.mY < imgY + 64 + (img.getHeight() - 64*2) + offsetHeight + 60) {
						isArchiving = true;
						mouseStatus = false;
					}
				}
			}
		}
	}
	
	private ArrayList<String> toArrayList(String toSplit){
		String[] brokenList = toSplit.split("\n");
		ArrayList<String> returnArrayList = new ArrayList<>();
		
		returnArrayList.add("ID");
		returnArrayList.add("");
		returnArrayList.add("Descrição");
		returnArrayList.add("Quantidade");
		returnArrayList.add("Preço");
		returnArrayList.add("Data de Criação");
		returnArrayList.add("Fornecedor");
		returnArrayList.add("Status");
		
		price = 0;
		
		for(int i = 0; i < brokenList.length; i++) {			
			String[] aux = brokenList[i].split(" § ");
			
			for(int j = 0; j < aux.length; j++) {
				returnArrayList.add(aux[j]);
			}
		}
		
				
		return returnArrayList;
		
	}
	
	public String translateText(String toTranslate, int index) {
		String toReturn = null;
		
		if(index == 1) {
			toReturn = "";
		}else if(index == 4) {
			toReturn = "R$" + toTranslate;
		}
		
		return toReturn;
		
	}
	
	public void drawPartsList(Graphics g) {
		positionerX = 52;
		positionerY = imgY + 224;
		auxTextWidth = 0;
		auxTextHeight = 0;
		
		Color newColor;
		
		String toDraw = "";
		if(separetedList.size() > 12) {
			for(int i = 0; i < separetedList.size(); i++) {
				
				int auxCheckBox = 0; 
				
				g.setFont(new Font("segoe ui", 0, 13));
				
				if(i < 8) {
					newColor = Color.orange;
				}else {
					newColor = Color.white;
				}
				
				switch(i % 8) {
				// 0 -> ID
				case 1:
					//1 -> Montagem
					break;
					
				case 2:
					// 2 -> Descrição
					auxTextWidth += (total*5.5)/100;
					break;
					
				case 3:
					// 3 -> Quantidade
					auxTextWidth += (total*35)/100;
					break;
				
				case 4:
					// 4 -> Preço
					auxTextWidth += (total*13)/100;
					break;
				
				case 5:
					//5 -> Data de Criação
					auxTextWidth += (total*10)/100;
					break;
				
				case 6:
					//6 -> Fornecedor
					auxTextWidth += (total*11.8)/100;
					break;
				
				case 7:
					//7 -> Status
					auxTextWidth += (total*20)/100;
					if(i > 10) {
						auxTextWidth += (total*2)/100;
					}
					break;
				}
				
				toDraw = separetedList.get(i);
				
				if(i > 8 ) {
					if(i % 8 == 1 || i % 8 == 4) {
						toDraw = translateText(separetedList.get(i), i % 8);
					}
					
					if(i % 8 == 2) {
						if(g.getFontMetrics().stringWidth(toDraw) > maxTextSize) {
							multipleDescriptionMark = true;
						}
					}else {
						multipleDescriptionMark = false;
					}
					
					if(i % 8 == 7) {
						auxCheckBox = 15;
					}
					
					
					if(!isEliminating) {
						if(multipleDescriptionMark) {
							if(Almoxarifado.mX > positionerX + auxTextWidth 
							&& Almoxarifado.mX < positionerX + auxTextWidth + maxTextSize
							&& Almoxarifado.mY > positionerY + auxTextHeight + offsetHeight - g.getFontMetrics().getHeight() - auxCheckBox
							&& Almoxarifado.mY < positionerY + auxTextHeight + offsetHeight + (g.getFontMetrics().stringWidth(toDraw) / maxTextSize) * 30) {
								newColor = Color.darkGray;
								if(mouseStatus) {
									PartsList.changePart(separetedList.get((auxTextHeight/30)*8), i % 8);
									updateProject = true;
									mouseStatus = false;
								}
							}
						}else {
							if(Almoxarifado.mX > positionerX + auxTextWidth - auxCheckBox 
							&& Almoxarifado.mX < positionerX + auxTextWidth + g.getFontMetrics().stringWidth(toDraw) + auxCheckBox
							&& Almoxarifado.mY > positionerY + auxTextHeight + offsetHeight - g.getFontMetrics().getHeight() - auxCheckBox
							&& Almoxarifado.mY < positionerY + auxTextHeight + offsetHeight + auxCheckBox) {
								newColor = Color.darkGray;
								if(mouseStatus) {
									PartsList.changePart(separetedList.get((auxTextHeight/30)*8), i % 8);
									updateProject = true;
									mouseStatus = false;
								}
							}
						}
					}
				}
				
				if(isEliminating) {
					if(i > 7) {
						if(Almoxarifado.mY > positionerY + auxTextHeight + offsetHeight - (g.getFontMetrics().getHeight() + 15) && 
						Almoxarifado.mY < positionerY + auxTextHeight + offsetHeight + 10) {
							newColor = Color.yellow;
							if(mouseStatus) {
								Almoxarifado.partsList.eliminatePart(Integer.parseInt(separetedList.get((auxTextHeight/30)*8)));
								updateProject = true;
								mouseStatus = false;
							}
						}
					}
				}
				if(i != 0 && i % 8 == 0) {
					auxTextHeight += 30 + auxH;
					auxTextWidth = 0;
					auxH = 0;
				}
				
				g.setColor(newColor);
				
				if(!multipleDescriptionMark) {
					g.drawString(toDraw, positionerX + auxTextWidth, positionerY + auxTextHeight + offsetHeight);
				}else {
					
					ArrayList<Integer> breakLineIndex = new ArrayList<>();
					int auxLineStandIn = 1;
					breakLineIndex.add(0);
					
					for(int currChar = 0; currChar < toDraw.length(); currChar++) {
						if(g.getFontMetrics().stringWidth(toDraw.substring(breakLineIndex.get(auxLineStandIn-1), currChar)) > maxTextSize) {
							breakLineIndex.add(currChar);
							auxLineStandIn++;
						}
					}
					breakLineIndex.add(toDraw.length());
					
					for(int lines = 1; lines < breakLineIndex.size(); lines++) {
						String auxBrokenDesc;
						
						auxBrokenDesc = toDraw.substring(breakLineIndex.get(lines-1), breakLineIndex.get(lines));

						g.drawString(auxBrokenDesc, positionerX + auxTextWidth, positionerY + auxTextHeight + offsetHeight + auxH);
						auxH += 30;
					}
					auxH -=30;
				}
				
				if(i > 10 && i % 8 == 7) {
					g.drawImage(checkBox, positionerX + auxTextWidth - g.getFontMetrics().stringWidth(toDraw), positionerY + auxTextHeight + offsetHeight - checkBox.getHeight(), 20, 20, null);
					if(toDraw.equals("1")) {
						g.drawImage(check, positionerX + auxTextWidth - g.getFontMetrics().stringWidth(toDraw), positionerY + auxTextHeight + offsetHeight - checkBox.getHeight(), 20, 20, null);
					}
				}
			}
		}
		g.drawImage(add, (Almoxarifado.WIDTH/3) - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + offsetHeight, null);
		g.drawImage(remove, (Almoxarifado.WIDTH/3)*2 - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + offsetHeight, null);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH/3) - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + offsetHeight);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH/3)*2 - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + offsetHeight);
		
		maximumHeight = positionerY + auxTextHeight + g.getFontMetrics().getHeight() - (UserInterface.maximunHeight + 26);
		
		if(mouseStatus) {
			if(Almoxarifado.mY > positionerY + auxTextHeight + g.getFontMetrics().getHeight() + offsetHeight
			&& Almoxarifado.mY < positionerY + auxTextHeight + g.getFontMetrics().getHeight() + offsetHeight + add.getHeight()) {
				if(Almoxarifado.mX > (Almoxarifado.WIDTH/3) - (add.getWidth()/2) && Almoxarifado.mX < (Almoxarifado.WIDTH/3) + (add.getWidth()/2)) {
					Almoxarifado.addPart.setIdInsertionValue(ID);
					Almoxarifado.partsList.addPart();
					updateProject = true;
					mouseStatus = false;
				}
				
				if(Almoxarifado.mX > (Almoxarifado.WIDTH/3)*2 - (add.getWidth()/2) && Almoxarifado.mX < (Almoxarifado.WIDTH/3)*2 + (add.getWidth()/2)){
					if(!isEliminating) {
						isEliminating = true;
						mouseStatus = false;
					}else {
						isEliminating = false;
						mouseStatus = false;
					}
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if(!updateProject) {
			g.setFont(new Font("segoe ui", 0, 35));
			
			g.drawImage(img, imgX, imgY + offsetHeight, null);
			
			if(isOverName) {
				g.setColor(Color.darkGray);
				isOverName = false;
			}else {
				g.setColor(Color.white);
			}
			g.drawString(name, imgX + 15 + img.getWidth(), imgY + 30 + offsetHeight);
			g.setFont(new Font("segoe ui", 0, 15));
			
			if(isOverCompany) {
				g.setColor(Color.darkGray);
				isOverCompany = false;
			}else {
				g.setColor(Color.white);
			}
			g.drawString(company, imgX + 15 + img.getWidth(), imgY + img.getHeight()/2 + offsetHeight);
			
			if(isOverDescription) {
				g.setColor(Color.darkGray);
				isOverDescription = false;
			}else {
				g.setColor(Color.white);
			}
			g.drawString(description, imgX + 15 + img.getWidth(), imgY + img.getHeight() + offsetHeight - 3);
			
			g.setColor(Color.white);
			g.drawString("Valor da Montagem: " + String.format("%.2f", price), Almoxarifado.WIDTH/2 + 15, imgY + 20 + offsetHeight);
			
			
			if(!isEditing) {
				g.drawImage(editAssembly, Almoxarifado.WIDTH - 128 - 100, imgY + offsetHeight, null);
			}else {
				g.drawImage(isEditingAssembly, Almoxarifado.WIDTH - 128 - 100, imgY + offsetHeight, null);
			}
			
			g.drawImage(archiveAssembly, Almoxarifado.WIDTH - 128 - 100, imgY + 64 + (img.getHeight() - 64*2) + offsetHeight, null);
			
			UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH - 128 - 100, imgY + offsetHeight);
			UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH - 128 - 100, imgY + 64 + (img.getHeight() - 64*2) + offsetHeight);
			
			nameSize = g.getFontMetrics(new Font("segoe ui", 0, 20)).stringWidth(name);
			descriptionSize = g.getFontMetrics(new Font("segoe ui", 0, 20)).stringWidth(description);
			companySize = g.getFontMetrics(new Font("segoe ui", 0, 20)).stringWidth(company);
			
			if(toggleScrollBar) {
				g.setColor(Color.darkGray);
				g.fillRect(Almoxarifado.WIDTH - (36 + 22), UserInterface.bttnY + UserInterface.boxHeight + 18, 20, UserInterface.maximunHeight - 12);
				
				g.setColor(Color.lightGray);
				if(isDragging) {
					g.setColor(Color.gray);
				}
				g.fillRect(Almoxarifado.WIDTH - (36 + 21), UserInterface.bttnY + UserInterface.boxHeight + 20 - (int) (thumbAuxY), thumbWidth, thumbHeight);
			}
			
			drawPartsList(g);
		}
	}

}
