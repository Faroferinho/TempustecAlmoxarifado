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
	
	static int ID = 2;
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
	
	double price = 0.0;
	
	public static boolean updateProject = true;
	boolean updateProjectAux = true;
	private boolean isOnTheRightState = false;
	
	public static int scroll;
	private int ofsetHeight = 0;
	
	public boolean mouseStatus = false;
	
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
	
	public void tick() {
		
		if(Almoxarifado.state == 5) {
			isOnTheRightState = true;
		}else {
			isOnTheRightState = false;
			ofsetHeight = 0;
		}
		
		if(updateProject) {
			ofsetHeight = 0;
			String brokenApartInfo[];
			System.out.println("Atualizando a Pagina de Projeto");
			String aux = DBConector.findInDB("*", "montagem", "ID_Montagem", "" + ID);
			System.out.println("aux: " + aux);
			
			brokenApartInfo = aux.split(" § ");
			
			name = brokenApartInfo[1];
			description = brokenApartInfo[2];
			company = brokenApartInfo[3];
			imgAdress = brokenApartInfo[4];
			
			if(imgAdress.equals(null) || imgAdress.equals("null") || imgAdress.equals("")) {
				System.out.println("a imagem está vazia");
				imgAdress = "ProjetoBetaImg";
			}else {
				System.out.println("O nome do arquivo de imagem é: " + imgAdress);
			}
			
			img = Almoxarifado.imgManag.getProjectImage(imgAdress);
			
			rawPartsList = "";
			rawPartsList += DBConector.findInDB("*", "pecas", "montagem", "" + ID);
			System.out.println("RawPartsList: \n" + rawPartsList);
			separetedList = toArrayList(rawPartsList);
			
			updateProjectAux = true;
			
			Almoxarifado.frame.setTitle(Project.name);
			
			updateProject = false;
		}
		
		if(isOnTheRightState) {
			if(scroll > 0) {
				ofsetHeight -= UserInterface.spd;
				scroll = 0;
			}else if(scroll < 0 && ofsetHeight < 0) {
				ofsetHeight += UserInterface.spd;
				scroll = 0;
			}
			
			if(isEditing) {
				//TODO: Sistema de distancia dinamico pro limite da largura do quadrado
				if(Almoxarifado.mX > imgX + 15 + img.getWidth() && Almoxarifado.mX < Almoxarifado.WIDTH/2) {
					if(Almoxarifado.mY > imgY - 10 + ofsetHeight && Almoxarifado.mY < imgY + 30 + ofsetHeight) {
						//System.out.println("sobre o Nome");
						isOverName = true;
						isOverDescription = false;
						isOverCompany = false;
					}
					else if(Almoxarifado.mY > imgY + 50 + ofsetHeight && Almoxarifado.mY < imgY + 90 + ofsetHeight) {
						//System.out.println("sobre a Descrição");
						isOverName = false;
						isOverDescription = false;
						isOverCompany = true;
					}else if(Almoxarifado.mY > imgY + 110 + ofsetHeight && Almoxarifado.mY < imgY + 150 + ofsetHeight) {
						//System.out.println("sobre a Empresa");
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
							
							DBConector.writeDB("UPDATE montagem SET ISO = '" + newName + "' WHERE ID_Montagem = " + ID);
							JOptionPane.showInternalMessageDialog(null, "Nome Atualizado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
							updateProject = true;
							mouseStatus = false;
							
						}
						
					}else if(isOverDescription) {
						
						String newDescription = "";
						
						newDescription += JOptionPane.showInputDialog(null, "Insira uma nova descrição", "Atualizar dados da Montagem", JOptionPane.PLAIN_MESSAGE);
						if(checkDialog(newDescription)) {
							
							JOptionPane.showInternalMessageDialog(null, "Erro ao Atualizar nome", "Erro", JOptionPane.ERROR_MESSAGE);
							mouseStatus = false;
							
						}else {
							
							DBConector.writeDB("UPDATE montagem SET Description = '" + newDescription + "' WHERE ID_Montagem = " + ID);
							JOptionPane.showInternalMessageDialog(null, "Nome Atualizado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
							DBConector.writeDB("UPDATE montagem SET Company = '" + newCompany + "' WHERE ID_Montagem = " + ID);
							JOptionPane.showInternalMessageDialog(null, "Nome Atualizado", "Erro", JOptionPane.INFORMATION_MESSAGE);
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
						System.out.println("Clique foi efetuado no Editar");
						if(isEditing) {
							isEditing = false;
							mouseStatus = false;
						}else {
							isEditing = true;
							mouseStatus = false;
						}
						
					}else if(Almoxarifado.mY > imgY + 64 + (img.getHeight() - 64*2) + ofsetHeight &&
							Almoxarifado.mY < imgY + 64 + (img.getHeight() - 64*2) + ofsetHeight + 64) {
						System.out.println("Clique foi efetuado no Arquivar");
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
		returnArrayList.add("Montagem");
		returnArrayList.add("Descrição");
		returnArrayList.add("Quantidade");
		returnArrayList.add("");
		returnArrayList.add("Preço");
		returnArrayList.add("Fornecedor");
		returnArrayList.add("Status");
		
		for(int i = 0; i < brokenList.length; i++) {
			//System.out.println("Lista no Indice " + (i+1) + ": " + brokenList[i]);
			
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
			toReturn = name;
		}else {
			toReturn = Almoxarifado.partsList.quantityTypes[Integer.parseInt(toTranslate)];
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
		//System.out.println("=================================================================");
			for(int i = 0; i < separetedList.size(); i++) {
				//System.out.println("Valor da lista no Indice " + i + ": " + separetedList.get(i));
				
				g.setFont(new Font("arial", 0, 13));
				
				if(i < 8) {
					newColor = Color.orange;
				}else {
					newColor = Color.white;
				}
				
				switch(i % 8) {
				// 0 -> ID;
				case 1:
					//System.out.println("1 -> Montagem");
					auxTextWidth += (total*5)/100;
					//System.out.println("1, AuxWidth: " + auxWidth);
					break;
				case 2:
					//System.out.println("2 -> Descrição");
					auxTextWidth += (total*13.9)/100;
					//System.out.println("2, AuxWidth: " + auxWidth);
					break;
				case 3:
					//System.out.println("3 -> Quantidade");
					auxTextWidth += (total*33.2)/100;
					//System.out.println("3, AuxWidth: " + auxWidth);
					break;
				case 4:
					//System.out.println("4 -> Tipo de Quantidade, AuxWidth: " + auxWidth);
					auxTextWidth += g.getFontMetrics().stringWidth(" " + separetedList.get(i-1));
					break;
				case 5:
					//System.out.println("5 -> Preço");
					auxTextWidth -= g.getFontMetrics().stringWidth(" " + separetedList.get(i-2));
					auxTextWidth += (total*19)/100;
					//System.out.println("5, AuxWidth: " + auxWidth);
					break;
				case 6:
					//System.out.println("6 -> Fornecedor");
					auxTextWidth += (total*11.8)/100;
					//System.out.println("6, AuxWidth: " + auxWidth);
					break;
				case 7:
					//System.out.println("7 -> Status");
					auxTextWidth += (total*14.6)/100;
					//System.out.println("7, AuxWidth: " + auxWidth);
					break;
				}
				
				if(i != 0 && i % 8 == 0) {
					auxTextHeight += 50;
					auxTextWidth = 0;
					//System.out.println("--------------------------------------------------------");
				}
				
				toDraw = separetedList.get(i);
				if(i > 8 ) {
					if(i % 8 == 1 || i % 8 == 4) {
						//System.out.println(separetedList.get(i));
						toDraw = translateText(separetedList.get(i), i % 8);
					}
					if(!isEliminating) {
						if(Almoxarifado.mX > positionerX + auxTextWidth &&
						Almoxarifado.mX < positionerX + auxTextWidth + g.getFontMetrics().stringWidth(toDraw) &&
						Almoxarifado.mY > positionerY + auxTextHeight + ofsetHeight - g.getFontMetrics().getHeight() &&
						Almoxarifado.mY < positionerY + auxTextHeight + ofsetHeight) {
							newColor = Color.gray;
							if(mouseStatus) {
								System.out.println("Clicou em: " + toDraw);
								PartsList.changePart(separetedList.get((auxTextHeight/50)*8), i % 8);
								updateProject = true;
								mouseStatus = false;
							}
						}
					}
				}
				
				if(isEliminating) {
					System.out.println("Está eliminando ;>");
					if(i > 7) {
						if(Almoxarifado.mY > positionerY + auxTextHeight + ofsetHeight - (g.getFontMetrics().getHeight() + 15) && 
						Almoxarifado.mY < positionerY + auxTextHeight + ofsetHeight + 15) {
							newColor = Color.yellow;
							if(mouseStatus) {
								Almoxarifado.partsList.eliminatePart(Integer.parseInt(separetedList.get((auxTextHeight/50)*8)));
								updateProject = true;
								mouseStatus = false;
							}
						}
					}
				}
				
				
				
				g.setColor(newColor);
				
				g.drawString(toDraw, positionerX + auxTextWidth, positionerY + auxTextHeight + ofsetHeight);
				
			}
			
			//System.out.println("=================================================================");
		}
		g.drawImage(add, (Almoxarifado.WIDTH/3) - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight + 30, null);
		g.drawImage(remove, (Almoxarifado.WIDTH/3)*2 - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight + 30, null);
		UserInterface.isOnButton(g, (Almoxarifado.WIDTH/3) - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight + 30);
		UserInterface.isOnButton(g, (Almoxarifado.WIDTH/3)*2 - (add.getWidth()/2), positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight + 30);
		
		if(mouseStatus) {
			//System.out.println("Clicado Haha");
			if(Almoxarifado.mY > positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight + 30 &&
			Almoxarifado.mY < positionerY + auxTextHeight + g.getFontMetrics().getHeight() + ofsetHeight + 30 + add.getHeight()) {
				//System.out.println("Está na Altura");
				if(Almoxarifado.mX > (Almoxarifado.WIDTH/3) - (add.getWidth()/2) && Almoxarifado.mX < (Almoxarifado.WIDTH/3) + (add.getWidth()/2)) {
					//System.out.println("Clicou em Add");
					PartsList.auxAddingFromMontagem = ID;
					Almoxarifado.partsList.addPart();
					PartsList.auxAddingFromMontagem = 0;
					updateProject = true;
					mouseStatus = false;
				}else if(Almoxarifado.mX > (Almoxarifado.WIDTH/3)*2 - (add.getWidth()/2) && Almoxarifado.mX < (Almoxarifado.WIDTH/3)*2 + (add.getWidth()/2)) {
					//System.out.println("Clicou em Add");
					if(isEliminating == true) {
						isEliminating = false;
					}else {
						isEliminating = true;
					}
					mouseStatus = false;
				}
			}
			
		}
	}
	
	public void render(Graphics g) {
		if(!updateProject) {
			g.setFont(new Font("arial", 0, 20));
			
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
			g.drawString(company, imgX + 15 + img.getWidth(), imgY + 80 + ofsetHeight);
			
			if(isOverDescription) {
				g.setColor(Color.darkGray);
				isOverDescription = false;
			}else {
				g.setColor(Color.white);
			}
			g.drawString(description, imgX + 15 + img.getWidth(), imgY + 140 + ofsetHeight);
			
			
			if(!isEditing) {
				g.drawImage(editProfile, Almoxarifado.WIDTH - 128 - 60, imgY + ofsetHeight, null);
			}else {
				g.drawImage(isEditingProfile, Almoxarifado.WIDTH - 128 - 60, imgY + ofsetHeight, null);
			}
			
			g.drawImage(archiveProfile, Almoxarifado.WIDTH - 128 - 60, imgY + 64 + (img.getHeight() - 64*2) + ofsetHeight, null);
			
			UserInterface.isOnButton(g, Almoxarifado.WIDTH - 128 - 60, imgY + ofsetHeight);
			UserInterface.isOnButton(g, Almoxarifado.WIDTH - 128 - 60, imgY + 64 + (img.getHeight() - 64*2) + ofsetHeight);
			
			nameSize = g.getFontMetrics(new Font("arial", 0, 20)).stringWidth(name);
			descriptionSize = g.getFontMetrics(new Font("arial", 0, 20)).stringWidth(description);
			companySize = g.getFontMetrics(new Font("arial", 0, 20)).stringWidth(company);
			
			drawPartsList(g);
		}
	}

}
