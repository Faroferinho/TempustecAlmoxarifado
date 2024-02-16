package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import functions.Archiver;
import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class Project {
	
	static int ID = 1;
	static String name = "";
	static String description = "";
	static String company = "";
	static String imgAdress = "";
	
	private String rawPartsList = "";
	private ArrayList<String> separetedList = new ArrayList<>();
	
	BufferedImage img;
	BufferedImage editProfile = Almoxarifado.imgManag.getSprite(475, 60*3, 165, 60);
	BufferedImage archiveProfile = Almoxarifado.imgManag.getSprite(475, 60*4, 165, 60);
	BufferedImage isEditingProfile = Almoxarifado.imgManag.getSprite(165, 510, 165, 60);
	BufferedImage add = Almoxarifado.imgManag.getSprite(475, 60*6, 165, 60);
	BufferedImage remove = Almoxarifado.imgManag.getSprite(475, 60*8, 165, 60);
	BufferedImage check = Almoxarifado.imgManag.getSprite(452, 371, 21, 21);
	BufferedImage checkBox = Almoxarifado.imgManag.getSprite(455, 395, 18, 18);
	
	static double price = 0.0;
	
	public static boolean updateProject = true;
	private boolean isOnTheRightState = false;

	public boolean mouseStatus = false;

	public static int scroll;
	public int ofsetHeight = 0;
	public int maximumHeight = 1;
	
	private boolean toggleScrollBar = false;
	private int thumbWidth = 18;
	public int thumbHeight = 0;
	private double thumbAuxY = 0;
	public boolean isDragging = false;
	
	int imgX = UserInterface.bttnX[0] + 50;
	int imgY = Almoxarifado.HEIGHT - UserInterface.maximunHeight - 20;
	
	boolean isEditing = false;
	boolean isArchiving = false;
	
	int nameSize = 0;
	int descriptionSize = 0;
	int companySize = 0;
	boolean isOverName = true;
	boolean isOverDescription = false;
	boolean isOverCompany = false;
	
	boolean isEliminating = false;
	
	int positionerX = 52;
	int positionerY = imgY + 224;
	int auxTextWidth = 0;
	int auxTextHeight = 0;
	int total = Almoxarifado.WIDTH - (positionerX*2);
	
	boolean multipleDescriptionMark = false;
	int sizeOfPartDescription = 0;
	int maxTextSize = (int) (((total*40)/100) - ((total*5.5)/100));
	int auxH = 0;
	
	public Project() {
		// TODO Auto-generated constructor stub
		
	}
	
	private boolean checkDialog(String toVerif) {
		if(toVerif.equals("") || toVerif.equals("null") || toVerif.equals(" ")) {
			return true;
		}
		return false;
	}
	
	public void updater() {		
		String brokenApartInfo[];
		String aux = DBConector.readDB("*", "montagem", "ID_Montagem", "" + ID);
		
		brokenApartInfo = aux.split(" § ");
		
		name = brokenApartInfo[1];
		description = brokenApartInfo[2];
		company = brokenApartInfo[3];
		imgAdress = brokenApartInfo[4];
		
		if(imgAdress.equals(null) || imgAdress.equals("null") || imgAdress.equals("")) {
			imgAdress = "ProjetoBetaImg";
		}
		
		img = Almoxarifado.imgManag.getProjectImage(imgAdress);
		
		if(imgAdress.equals("ProjetoBetaImg")) {
			img = Almoxarifado.imgManag.getSprite(0, 180, 150, 200);
		}
		
		rawPartsList = "";
		rawPartsList += DBConector.readDB("*", "pecas", "montagem", "" + ID);
		separetedList = toArrayList(rawPartsList);
		
		price = DBConector.getAssemblyValue("" + ID);
		
		DBConector.writeDB("Montagem", "cost", "" + price, "ID_Montagem", "" + ID);
		
		Almoxarifado.frame.setTitle(Project.name);
		
		PartsList.assembliesHM = PartsList.fillAssembliesName();
	}
	
	public void scrollPositioner() {
		if(ofsetHeight < (maximumHeight * -1)) {			
			ofsetHeight = maximumHeight * -1;
		}if(ofsetHeight > 0) {		
			ofsetHeight = 0;
		}
		
		int Y = (UserInterface.maximunHeight - 18) - thumbHeight;
		double S = (Double.parseDouble("" + maximumHeight) / Double.parseDouble("" + ofsetHeight));
		thumbAuxY = Y / S;
	}
	
	public void tick() {
		if(Almoxarifado.state == 5) {
			isOnTheRightState = true;
		}else {
			isOnTheRightState = false;
			thumbAuxY = 0;
			ofsetHeight = 0;
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
				ofsetHeight -= UserInterface.spd;
				
				scrollPositioner();
				
				scroll = 0;
			}else if(scroll < 0) {
				ofsetHeight += UserInterface.spd;
				
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
					if(Almoxarifado.mY > imgY - 10 + ofsetHeight && Almoxarifado.mY < imgY + 60 + ofsetHeight) {
						isOverName = true;
						isOverDescription = false;
						isOverCompany = false;
					}
					else if(Almoxarifado.mY > imgY + 75 + ofsetHeight && Almoxarifado.mY < imgY + 125 + ofsetHeight) {
						isOverName = false;
						isOverDescription = false;
						isOverCompany = true;
					}else if(Almoxarifado.mY > imgY + 135 + ofsetHeight && Almoxarifado.mY < imgY + 200 + ofsetHeight) {
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
						
						if(checkDialog(newName)) {
							
							JOptionPane.showInternalMessageDialog(null, "Erro ao Atualizar nome", "Erro", JOptionPane.ERROR_MESSAGE);
							mouseStatus = false;
							
						}else {
							Archiver.writeOnArchive("alteracao", "o Projeto de ID = " + ID, name, newName);
							DBConector.writeDB("UPDATE montagem SET ISO = \"" + newName + "\" WHERE ID_Montagem = " + ID);
							JOptionPane.showInternalMessageDialog(null, "Nome Atualizado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
							updateProject = true;
							mouseStatus = false;
							ProjectList.updateProjectList = true;
						}
						
						
						
					}else if(isOverDescription) {
						
						String newDescription = "";
						
						newDescription += JOptionPane.showInputDialog(null, "Insira uma nova descrição", "Atualizar dados da Montagem", JOptionPane.PLAIN_MESSAGE);
						if(checkDialog(newDescription)) {
							
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
						if(checkDialog(newCompany)) {
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
				Almoxarifado.state = 3;
			}
			
			if(mouseStatus) {
				if(Almoxarifado.mX > Almoxarifado.WIDTH - 128 - 60 && Almoxarifado.mX < Almoxarifado.WIDTH - 60) {
					if(Almoxarifado.mY >  imgY + ofsetHeight && Almoxarifado.mY <  imgY + ofsetHeight + 64) {
						if(isEditing) {
							isEditing = false;
							mouseStatus = false;
						}else {
							isEditing = true;
							mouseStatus = false;
						}
						
					}else if(Almoxarifado.mY > imgY + 64 + (img.getHeight() - 64*2) + ofsetHeight &&
					Almoxarifado.mY < imgY + 64 + (img.getHeight() - 64*2) + ofsetHeight + 64) {
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
							&& Almoxarifado.mY > positionerY + auxTextHeight + ofsetHeight - g.getFontMetrics().getHeight() - auxCheckBox
							&& Almoxarifado.mY < positionerY + auxTextHeight + ofsetHeight + (g.getFontMetrics().stringWidth(toDraw) / maxTextSize) * 30) {
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
							&& Almoxarifado.mY > positionerY + auxTextHeight + ofsetHeight - g.getFontMetrics().getHeight() - auxCheckBox
							&& Almoxarifado.mY < positionerY + auxTextHeight + ofsetHeight + auxCheckBox) {
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
						if(Almoxarifado.mY > positionerY + auxTextHeight + ofsetHeight - (g.getFontMetrics().getHeight() + 15) && 
						Almoxarifado.mY < positionerY + auxTextHeight + ofsetHeight + 10) {
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
					g.drawString(toDraw, positionerX + auxTextWidth, positionerY + auxTextHeight + ofsetHeight);
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

						g.drawString(auxBrokenDesc, positionerX + auxTextWidth, positionerY + auxTextHeight + ofsetHeight + auxH);
						auxH += 30;
					}
					auxH -=30;
				}
				
				if(i > 10 && i % 8 == 7) {
					g.drawImage(checkBox, positionerX + auxTextWidth - g.getFontMetrics().stringWidth(toDraw), positionerY + auxTextHeight + ofsetHeight - checkBox.getHeight(), 20, 20, null);
					if(toDraw.equals("1")) {
						g.drawImage(check, positionerX + auxTextWidth - g.getFontMetrics().stringWidth(toDraw), positionerY + auxTextHeight + ofsetHeight - checkBox.getHeight(), 20, 20, null);
					}
				}
			}
		}
		g.drawImage(add, (Almoxarifado.WIDTH/3) - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight, null);
		g.drawImage(remove, (Almoxarifado.WIDTH/3)*2 - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight, null);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH/3) - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH/3)*2 - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight);
		
		maximumHeight = positionerY + auxTextHeight + g.getFontMetrics().getHeight() - (UserInterface.maximunHeight + 26);
		
		if(mouseStatus) {
			if(Almoxarifado.mY > positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight
			&& Almoxarifado.mY < positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight + add.getHeight()) {
				if(Almoxarifado.mX > (Almoxarifado.WIDTH/3) - (add.getWidth()/2) && Almoxarifado.mX < (Almoxarifado.WIDTH/3) + (add.getWidth()/2)) {
					PartsList.auxAddingFromMontagem = ID;
					Almoxarifado.partsList.addPart();
					PartsList.auxAddingFromMontagem = 0;
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
			
			g.drawImage(img, imgX, imgY + ofsetHeight, null);
			
			if(isOverName) {
				g.setColor(Color.darkGray);
				isOverName = false;
			}else {
				g.setColor(Color.white);
			}
			g.drawString(name, imgX + 15 + img.getWidth(), imgY + 30 + ofsetHeight);
			g.setFont(new Font("segoe ui", 0, 15));
			
			if(isOverCompany) {
				g.setColor(Color.darkGray);
				isOverCompany = false;
			}else {
				g.setColor(Color.white);
			}
			g.drawString(company, imgX + 15 + img.getWidth(), imgY + img.getHeight()/2 + ofsetHeight);
			
			if(isOverDescription) {
				g.setColor(Color.darkGray);
				isOverDescription = false;
			}else {
				g.setColor(Color.white);
			}
			g.drawString(description, imgX + 15 + img.getWidth(), imgY + img.getHeight() + ofsetHeight - 3);
			
			g.setColor(Color.white);
			g.drawString("Valor da Montagem: " + String.format("%.2f", price), Almoxarifado.WIDTH/2 + 15, imgY + 20 + ofsetHeight);
			
			
			if(!isEditing) {
				g.drawImage(editProfile, Almoxarifado.WIDTH - 128 - 100, imgY + ofsetHeight, null);
			}else {
				g.drawImage(isEditingProfile, Almoxarifado.WIDTH - 128 - 100, imgY + ofsetHeight, null);
			}
			
			g.drawImage(archiveProfile, Almoxarifado.WIDTH - 128 - 100, imgY + 64 + (img.getHeight() - 64*2) + ofsetHeight, null);
			
			UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH - 128 - 100, imgY + ofsetHeight);
			UserInterface.isOnSmallButton(g, Almoxarifado.WIDTH - 128 - 100, imgY + 64 + (img.getHeight() - 64*2) + ofsetHeight);
			
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
