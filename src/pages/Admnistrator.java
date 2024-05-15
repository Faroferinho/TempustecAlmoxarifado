package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import functions.DBConector;
import functions.Functions;
import main.Almoxarifado;
import main.UserInterface;
import functions.BidimensionalList;

public class Admnistrator extends Profile implements BidimensionalList{
	
	protected BufferedImage bttn_listWorkers = Almoxarifado.imgManag.getSprite(475, 60, 165, 60);
	protected BufferedImage bttn_addWorker = Almoxarifado.imgManag.getSprite(475, 300, 165, 60);
	
	private BufferedImage bttn_removeWorker = Almoxarifado.imgManag.getSprite(475, 420, 165, 60);
	
	int scroll;
	
	private boolean isListing = false;
	private String[][] brokenDownList;
	
	private boolean addingWorker = false;
	private boolean removingWorker = false;
	
	public String orderColumn = "RdF";
	
	public Admnistrator(String RdF){
		super(RdF);
		fillMultiArray();
	}
	
	public void fillMultiArray() {
		brokenDownList = new String[Almoxarifado.quantityWorkers + 1][4];
		
		String crudeRdFs = DBConector.readDB("RdF", "Funcionarios");
		String crudeNames = DBConector.readDB("Name", "Funcionarios");
		String crudeCPFs = DBConector.readDB("CPF", "Funcionarios");
		String crudeTypes = DBConector.readDB("Type", "Funcionarios");
		
		String listRdFs[] = crudeRdFs.split("\n");
		String listNames[] = crudeNames.split("\n");
		String listCPFs[] = crudeCPFs.split("\n");
		String listType[] = crudeTypes.split("\n");
		
		brokenDownList[0][0] = "RdF";
		brokenDownList[0][1] = "Nome";
		brokenDownList[0][2] = "CPF";
		brokenDownList[0][3] = "Tipo";
		
		for(int i = 1; i < Almoxarifado.quantityWorkers + 1; i++) {
			System.out.println("Linha " + i);
			
			brokenDownList[i][0] = listRdFs[i-1].replaceAll(" § ", "");
			brokenDownList[i][1] = listNames[i-1].replaceAll(" § ", "");
			brokenDownList[i][2] = formatCPF(listCPFs[i-1].replaceAll(" § ", ""));
			brokenDownList[i][3] = formatType(listType[i-1].replaceAll(" § ", ""));
		}
	}
	
	private String formatType(String typeNumb) {
		String workerType = "";
		
		switch(typeNumb) {
		case "1":
			workerType += "Administrador";
			break;
		default:
			workerType += "Colaborador";
			break;
		}
		
		return workerType;
	}
	
	@Override
	public String getColumn(int i) {
		System.out.println("Valor da Coluna " + brokenDownList[firstLine][i]);
		return brokenDownList[firstLine][i];
	}

	@Override
	public void tick() {
		Almoxarifado.frame.setTitle("Almoxarifado - " + name);
		
		if(!isListing) {
			if(mouseStatus) {
				if(Almoxarifado.mY > 200
				&& Almoxarifado.mY < 200 + bttn_changePW.getHeight()) {
					
					if(Almoxarifado.mX > (Almoxarifado.WIDTH / 5) * 3
					&& Almoxarifado.mX < (Almoxarifado.WIDTH / 5) * 3 + bttn_changePW.getWidth()) {
						
						changePassword();
						
					}else if(Almoxarifado.mX > (Almoxarifado.WIDTH / 5) * 3 + 250
						  && Almoxarifado.mX < (Almoxarifado.WIDTH / 5) * 3 + 250 + bttn_changePW.getWidth()) {
						
						if(!isEditing) {						
							isEditing = true;					
							mouseStatus = false;
						}else {
							isEditing = false;					
							mouseStatus = false;
						}
						
					}
					
				} else if(Almoxarifado.mY > 350 
					   && Almoxarifado.mY < 350 + bttn_addWorker.getHeight()) {
					
					if(Almoxarifado.mX > (Almoxarifado.WIDTH / 5) * 3
					&& Almoxarifado.mX < (Almoxarifado.WIDTH / 5) * 3 + bttn_changePW.getWidth()) {
						
						addingWorker = true;
						
					}else if(Almoxarifado.mX > (Almoxarifado.WIDTH / 5) * 3 + 250
						  && Almoxarifado.mX < (Almoxarifado.WIDTH / 5) * 3 + 250 + bttn_changePW.getWidth()) {
						
						fillMultiArray();
						
						if(!isListing) {						
							isListing = true;					
							mouseStatus = false;
						}
						
					}
				}
			}
			
			if(addingWorker) {
				addingWorker = false;
				Almoxarifado.addWorker.addedFromAdmnistrator = true;
				Almoxarifado.state = 8;
			}
			
		}else {
			if(addingWorker) {
				addingWorker = false;
				Almoxarifado.state = 8;
			}
		}

		editInfo();
	}
	
	private void alterInfo(int line, int column) {
		String objectiveColumn = "";
		String newInfo = "";
		
		switch(column) {
		case 0:
			objectiveColumn = "RdF";
			break;
		case 1:
			objectiveColumn = "Name";
			break;
		case 2:
			objectiveColumn = "CPF";
			break;
		case 3:
			objectiveColumn = "Type";
			break;
		}
		
		if(column != 3) {
			newInfo += JOptionPane.showInputDialog(null, "Qual será a nova informação", "Atualizar Funcionario", JOptionPane.PLAIN_MESSAGE);
			
			if(Functions.emptyString(newInfo)) {
				return;
			}
		}else {
			String[] options = {"Colaborador", "Administrador"};
			
			newInfo += (String) JOptionPane.showInputDialog(null, "Selecione o novo tipo de Funcionario", "Atualizar Funcionario", JOptionPane.PLAIN_MESSAGE, null, options, 0);
			
			if(Functions.emptyString(newInfo)) {
				return;
			}
			
			switch(newInfo) {
			case "Funcionario":
				newInfo = "0";
				break;
			default:
				newInfo = "1";
				break;
			}
		}
		
		if(column == 2) {
			while(newInfo.length() != 11) {
				newInfo = "";
				newInfo += JOptionPane.showInputDialog(null, "CPF invalido, tente novamente", "Atualizar Funcionario", JOptionPane.PLAIN_MESSAGE);
				
				if(Functions.emptyString(newInfo)) {
					return;
				}
			}
		}
		
		DBConector.writeDB("Funcionarios", objectiveColumn, newInfo, "RdF", brokenDownList[line][0]);
		fillMultiArray();
	}
	
	private void listWorkers(Graphics g) {
		int auxX = 80;
		int auxY = 150;
		
		int limit = Almoxarifado.WIDTH - 160;
		
		g.setFont(new Font("segoi ui", 0, 15));
		g.setColor(Color.white);
		
		for(int i = 0; i < brokenDownList.length; i++) {
			
			for(int j = 0; j < brokenDownList[0].length; j++) {
								
				switch(j) {
				case 1:
					auxX += (limit * 15) / 100;
					break;
				case 2:
					auxX += (limit * 45) / 100;
					break;
				case 3:
					auxX += (limit * 32) / 100;
					break;
				}
				
				if(i == 0) {
					g.setFont(new Font("segoi ui", 1, 18));
					g.setColor(Color.orange);
				}else {
					g.setFont(new Font("segoi ui", 0, 15));
					g.setColor(Color.white);
				}
				
				if(!removingWorker) {
					if(i != 0) {
						if(Almoxarifado.mX > auxX && Almoxarifado.mX < auxX + g.getFontMetrics().stringWidth(brokenDownList[i][j])) {
							if(Almoxarifado.mY > auxY - g.getFontMetrics().getHeight() && Almoxarifado.mY < auxY) {
								g.setColor(Color.gray);
								if(mouseStatus) {
									alterInfo(i, j);
									mouseStatus = false;
								}
							}
						}
					}
				}else {
					if(Almoxarifado.mY > auxY - g.getFontMetrics().getHeight() && Almoxarifado.mY < auxY) {
						g.setColor(Color.red);
						if(mouseStatus) {
							DBConector.writeDB("DELETE FROM Funcionarios WHERE RdF = " + brokenDownList[i][0]);
							Almoxarifado.quantityWorkers--;
							fillMultiArray();
							mouseStatus = false;
						}
					}
				}
				
				//System.out.println("brokenDownList[" + i + "][" + j + "] = " + brokenDownList[i][j]);
				g.drawString(brokenDownList[i][j], auxX, auxY);
			}
			auxX = 80;
			auxY += 50;
		}
		
		g.drawImage(bttn_addWorker, (Almoxarifado.WIDTH / 3) - (bttn_addWorker.getWidth() / 2), auxY, null);
		g.drawImage(bttn_removeWorker, ((Almoxarifado.WIDTH / 3) * 2) - (bttn_removeWorker.getWidth() / 2), auxY, null);
		
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 3) - (bttn_addWorker.getWidth() / 2), auxY);
		UserInterface.isOnSmallButton(g, ((Almoxarifado.WIDTH / 3) * 2) - (bttn_removeWorker.getWidth() / 2), auxY);
		
		if(mouseStatus) {
			if(Almoxarifado.mY > auxY && Almoxarifado.mY < auxY + 60) {
				if(Almoxarifado.mX > (Almoxarifado.WIDTH / 3) - (bttn_addWorker.getWidth() / 2)
				&& Almoxarifado.mX < (Almoxarifado.WIDTH / 3) + (bttn_addWorker.getWidth() / 2)) {
					addingWorker = true;
				}else if(Almoxarifado.mX > ((Almoxarifado.WIDTH / 3) * 2) - (bttn_removeWorker.getWidth() / 2)
					  && Almoxarifado.mX < ((Almoxarifado.WIDTH / 3) * 2) + (bttn_removeWorker.getWidth() / 2)){
					System.out.println("Clicou para remover funcionario");
					if(removingWorker) {
						removingWorker = false;
					}else {
						removingWorker = true;
					}
					mouseStatus = false;
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		if(!isListing) {
			drawUserBasis(g);
		
			g.drawImage(bttn_changePW, (Almoxarifado.WIDTH / 5) * 3, 200, null);
			g.drawImage(bttn_editInfo, (Almoxarifado.WIDTH / 5) * 3 + 250, 200, null);
			g.drawImage(bttn_addWorker, (Almoxarifado.WIDTH / 5) * 3 , 350, null);
			g.drawImage(bttn_listWorkers, (Almoxarifado.WIDTH / 5) * 3 + 250, 350, null);
			
			UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 , 200);
			UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 + 250, 200);
			UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 , 350);
			UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 5) * 3 + 250, 350);
		}else {
			listWorkers(g);
		}
		
	}
}
