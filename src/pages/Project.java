package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import functions.DBConector;
import main.Almoxarifado;
import main.UserInterface;

public class Project {
	
	static int ID = 53;
	static String name = "";
	static String description = "";
	static String company = "";
	static String imgAdress = "";
	
	private String rawPartsList = "";
	private ArrayList<String> separetedList = new ArrayList<>();
	
	BufferedImage img;
	BufferedImage editProfile = Almoxarifado.imgManag.getSprite(128, 64*2, 128, 64);
	BufferedImage archiveProfile = Almoxarifado.imgManag.getSprite(0, 64*4, 128, 64);
	BufferedImage isEditingProfile = Almoxarifado.imgManag.getSprite(128, 64*3, 128, 64);
	BufferedImage add = Almoxarifado.imgManag.getSprite(0, 128, 128, 64);
	BufferedImage remove = Almoxarifado.imgManag.getSprite(512, 128, 128, 64);
	BufferedImage checkBox = Almoxarifado.imgManag.getSprite(414, 193, 32, 32);
	BufferedImage check = Almoxarifado.imgManag.getSprite(406, 226, 41, 39);
	
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
		System.out.println("ID: " + ID);
		
		String brokenApartInfo[];
		String aux = DBConector.findInDB("*", "montagem", "ID_Montagem", "" + ID);
		
		brokenApartInfo = aux.split(" § ");
		
		name = brokenApartInfo[1];
		description = brokenApartInfo[2];
		company = brokenApartInfo[3];
		imgAdress = brokenApartInfo[4];
		
		if(imgAdress.equals(null) || imgAdress.equals("null") || imgAdress.equals("")) {
			imgAdress = "ProjetoBetaImg";
		}
		
		img = Almoxarifado.imgManag.getProjectImage(imgAdress);
		
		rawPartsList = "";
		rawPartsList += DBConector.findInDB("*", "pecas", "montagem", "" + ID);
		separetedList = toArrayList(rawPartsList);
		
		price = 0;
		double finalPrice = 0;
		int auxQuantity = 0;
		
		for(int i = 10; i < separetedList.size(); i++) {
			if(i % 8 == 3) {
				auxQuantity += Integer.parseInt(separetedList.get(i));
			}
			if(i % 8 == 5) {
				finalPrice += Double.parseDouble(separetedList.get(i));
			}
			if(i % 8 == 7) {
				price += auxQuantity * finalPrice;
				auxQuantity = 0;
				finalPrice = 0;
			}
		}
		
		DBConector.editLine("Montagem", "cost", "" + price, "ID_Montagem", "" + ID);
		
		Almoxarifado.frame.setTitle(Project.name);
		
		PartsList.assembliesHM = PartsList.fillAssembliesName();
		PartsList.quantityTypes = PartsList.fillQuantityTypes();
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
					if(Almoxarifado.mY > imgY - 10 + ofsetHeight && Almoxarifado.mY < imgY + 30 + ofsetHeight) {
						isOverName = true;
						isOverDescription = false;
						isOverCompany = false;
					}
					else if(Almoxarifado.mY > imgY + 30 + ofsetHeight && Almoxarifado.mY < imgY + 50 + ofsetHeight) {
						isOverName = false;
						isOverDescription = false;
						isOverCompany = true;
					}else if(Almoxarifado.mY > imgY + 70 + ofsetHeight && Almoxarifado.mY < imgY + 100 + ofsetHeight) {
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
		returnArrayList.add("");
		returnArrayList.add("Preço");
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
		}else {
			toReturn = PartsList.quantityTypes[Integer.parseInt(toTranslate)];
		}
		
		return toReturn;
		
	}
	
	public void drawPartsList(Graphics g) {
		int positionerX = 52;
		int positionerY = imgY + 224;
		int auxTextWidth = 0;
		int auxTextHeight = 0;
		int total = Almoxarifado.WIDTH - (positionerX*2);
		
		Color newColor;
		
		String toDraw = "";
		if(separetedList.size() > 12) {
			for(int i = 0; i < separetedList.size(); i++) {
				
				int auxCheckBox = 0; 
				
				g.setFont(new Font("arial", 0, 13));
				
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
					auxTextWidth += (total*11)/100;
					break;
					
				case 3:
					// 3 -> Quantidade
					auxTextWidth += (total*33.2)/100;
					break;
				
				case 4:
					// 4 -> Tipo de Quantidade
					auxTextWidth += g.getFontMetrics().stringWidth(" " + separetedList.get(i-1));
					break;
				
				case 5:
					//5 -> Preço
					auxTextWidth -= g.getFontMetrics().stringWidth(" " + separetedList.get(i-2));
					auxTextWidth += (total*19)/100;
					break;
				
				case 6:
					//6 -> Fornecedor
					auxTextWidth += (total*11.8)/100;
					break;
				
				case 7:
					//7 -> Status
					auxTextWidth += (total*17)/100;
					if(i > 10) {
						auxTextWidth += (total*2)/100;
					}
					break;
				}
				
				if(i != 0 && i % 8 == 0) {
					auxTextHeight += 30;
					auxTextWidth = 0;
				}
				
				toDraw = separetedList.get(i);
				if(i > 8 ) {
					if(i % 8 == 1 || i % 8 == 4) {
						toDraw = translateText(separetedList.get(i), i % 8);
					}
					if(!isEliminating) {
						if(Almoxarifado.mX > positionerX + auxTextWidth - auxCheckBox &&
						Almoxarifado.mX < positionerX + auxTextWidth + g.getFontMetrics().stringWidth(toDraw) + auxCheckBox &&
						Almoxarifado.mY > positionerY + auxTextHeight + ofsetHeight - g.getFontMetrics().getHeight() - auxCheckBox &&
						Almoxarifado.mY < positionerY + auxTextHeight + ofsetHeight + auxCheckBox) {
							newColor = Color.darkGray;
							if(mouseStatus) {
								PartsList.changePart(separetedList.get((auxTextHeight/30)*8), i % 8);
								updateProject = true;
								mouseStatus = false;
							}
						}
					}
					
					if(i % 8 == 7) {
						auxCheckBox = 15;
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
				
				
				
				g.setColor(newColor);
				
				g.drawString(toDraw, positionerX + auxTextWidth, positionerY + auxTextHeight + ofsetHeight);
				
				if(i > 10 && i % 8 == 7) {
					g.drawImage(checkBox, positionerX + auxTextWidth - g.getFontMetrics().stringWidth(toDraw), positionerY + auxTextHeight + ofsetHeight + (g.getFontMetrics().getHeight() - checkBox.getHeight()), 20, 20, null);
					if(toDraw.equals("1")) {
						g.drawImage(check, positionerX + auxTextWidth - g.getFontMetrics().stringWidth(toDraw), positionerY + auxTextHeight + ofsetHeight + (g.getFontMetrics().getHeight() - checkBox.getHeight()), 20, 20, null);
					}
				}
			}
		}
		g.drawImage(add, (Almoxarifado.WIDTH/3) - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight, null);
		g.drawImage(remove, (Almoxarifado.WIDTH/3)*2 - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight, null);
		UserInterface.isOnButton(g, (Almoxarifado.WIDTH/3) - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight);
		UserInterface.isOnButton(g, (Almoxarifado.WIDTH/3)*2 - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight);
		
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
			g.setFont(new Font("arial", 0, 17));
			
			g.drawImage(img, imgX, imgY + ofsetHeight, null);
			
			if(isOverName) {
				g.setColor(Color.darkGray);
				isOverName = false;
			}else {
				g.setColor(Color.white);
			}
			g.drawString(name, imgX + 15 + img.getWidth(), imgY + 20 + ofsetHeight);
			
			if(isOverCompany) {
				g.setColor(Color.darkGray);
				isOverCompany = false;
			}else {
				g.setColor(Color.white);
			}
			g.drawString(company, imgX + 15 + img.getWidth(), imgY + 60 + ofsetHeight);
			
			if(isOverDescription) {
				g.setColor(Color.darkGray);
				isOverDescription = false;
			}else {
				g.setColor(Color.white);
			}
			g.drawString(description, imgX + 15 + img.getWidth(), imgY + 100 + ofsetHeight);
			
			g.setColor(Color.white);
			g.drawString("Valor da Montagem: " + String.format("%.2f", price), Almoxarifado.WIDTH/2 + 15, imgY + 20 + ofsetHeight);
			
			
			if(!isEditing) {
				g.drawImage(editProfile, Almoxarifado.WIDTH - 128 - 100, imgY + ofsetHeight, null);
			}else {
				g.drawImage(isEditingProfile, Almoxarifado.WIDTH - 128 - 100, imgY + ofsetHeight, null);
			}
			
			g.drawImage(archiveProfile, Almoxarifado.WIDTH - 128 - 100, imgY + 64 + (img.getHeight() - 64*2) + ofsetHeight, null);
			
			UserInterface.isOnButton(g, Almoxarifado.WIDTH - 128 - 100, imgY + ofsetHeight);
			UserInterface.isOnButton(g, Almoxarifado.WIDTH - 128 - 100, imgY + 64 + (img.getHeight() - 64*2) + ofsetHeight);
			
			nameSize = g.getFontMetrics(new Font("arial", 0, 20)).stringWidth(name);
			descriptionSize = g.getFontMetrics(new Font("arial", 0, 20)).stringWidth(description);
			companySize = g.getFontMetrics(new Font("arial", 0, 20)).stringWidth(company);
			
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
