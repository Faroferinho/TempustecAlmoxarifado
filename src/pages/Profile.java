package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JOptionPane;

import functions.DBConector;
import main.Almoxarifado;

public class Profile {
	
	public boolean reset;
	public boolean isOnTheRightState = false;
	
	public static String name;
	public static String RdF;
	public static String CPF;
	
	public static int nameSize;
	public static int RdFSize;
	public static int CPFSize;

	public boolean mouseStatus = false;
	public boolean isEditing = false;
	
	public static boolean overName = false;
	public static boolean overRdF = false;
	public static boolean overCPF = false;
	
	public boolean mouseAuxRun = false;
	public boolean mouseAuxEdit = false;
	public boolean mouseAuxRead = false;
	public boolean mouseAuxSign = false;

	public Profile(String inName, String inRdF, String inCPF) {
		name = inName;
		RdF = String.valueOf(inRdF);
		CPF = String.valueOf(inCPF);
	}
	
	public byte buttonClick(int mx, int my, boolean type) {
		
		if(type == false) {
			if(my > Almoxarifado.HEIGHT / 2 + 105 && my < Almoxarifado.HEIGHT / 2 + 190) {
				if(mx > Almoxarifado.WIDTH / 4 - 80 && mx < Almoxarifado.WIDTH / 4 + 80) {
					//Editar Perfil
					System.out.println("Botão Clicado");
					return 1;
				}else if(mx > (Almoxarifado.WIDTH / 4) * 3 - 80 && mx < (Almoxarifado.WIDTH / 4)*3 + 80) {
					System.out.println("Botão Clicado");
					return 2;
				}
			}
		}else {
			if(my > Almoxarifado.HEIGHT / 2 + 105 && my < Almoxarifado.HEIGHT / 2 + 190) {
				if(isEditing == false) {
					if(mx > (Almoxarifado.WIDTH / 5) - 80 && mx < (Almoxarifado.WIDTH / 5) + 80) {
						//Editar Perfil;
						//System.out.println("Botão Clicado");
						return 1;
					}else if(mx > (Almoxarifado.WIDTH / 5) * 2 - 80 && mx < (Almoxarifado.WIDTH / 5) * 2 + 80) {
						//Listar Pessoas;
						return 2;
					}else if(mx > (Almoxarifado.WIDTH / 5) * 3 - 80 && mx < (Almoxarifado.WIDTH / 5) * 3 + 80) {
						//Cadastro de Funcionarios;
						return 3;
					}else if(mx > (Almoxarifado.WIDTH / 5) * 4 - 80 && mx < (Almoxarifado.WIDTH / 5) * 4 + 80) {
						//Cadastro de Funcionarios;
						return 4;
					}
				}
				
				if(isEditing == true) {
					if(mx > (Almoxarifado.WIDTH / 3) - 80 && mx < (Almoxarifado.WIDTH / 3) + 80) {
						System.out.println("Botão Clicado");
						return 1;
					}else if(mx > (Almoxarifado.WIDTH / 4) * 3 - 80 && mx < (Almoxarifado.WIDTH / 3) * 2 + 80) {
						System.out.println("Botão Clicado");
						return 4;
					}
				}
			}
		}
		
		return 0;
	}
	
	public void changeInformation(int mx, int my, boolean type){
		if(type == false) {
			if(mx > 170 && mx < 200 + nameSize) {
				if(my > 135 && my < 170) {
					//System.out.println("Está no nome");
					overName = true;
					if(mouseAuxEdit) {
						editInfo(1);
					}
				}
			}
		}else {
			if(mx > 170 && mx < 512) {
				if(my > 135 && my < 170) {
					//System.out.println("Está no nome");
					overName = true;
					if(mouseAuxEdit) {
						editInfo(1);
					}
				}else if(my > 170 && my < 210){
					overRdF = true;
					if(mouseAuxEdit) {
						editInfo(2);
					}
				}else if(my > 210 && my < 250) {
					//System.out.println("Está no CPF");
					overCPF = true;
					if(mouseAuxEdit) {
						editInfo(3);
					}
				}
			}
		}
	}
	
	public void editInfo(int type) {
		
		switch(type) {
		case 1:
			String newName = "";
			newName += JOptionPane.showInputDialog("Insira o Novo Nome");
			System.out.println("O Novo nome é: " + newName);
			
			if(newName != "null" && newName != "") {
				DBConector.editLine("funcionarios", "name", newName, "RdF", RdF);
				name = newName;
			}
			
			break;
		case 2:
			String newRdF = "";
			newRdF += JOptionPane.showInputDialog("Insira o Novo Registro de Funcionario");
			System.out.println("O Novo nome é: " + newRdF);
			
			if(!(newRdF.isBlank()) || !(newRdF.isEmpty())) {
				DBConector.editLine("funcionarios", "RdF", newRdF, "RdF", RdF);
				RdF = newRdF;
			}
			break;
		case 3:
			String newCPF = "";
			newCPF += JOptionPane.showInputDialog("Insira o Novo CPF");
			System.out.println("O Novo nome é: " + newCPF);
			
			if(!(newCPF.isBlank()) || !(newCPF.isEmpty())) {
				DBConector.editLine("funcionarios", "CPF", newCPF, "RdF", RdF);
				RdF = newCPF;
			}
			break;
		case 4:
			String newPassword = "";
			
			newPassword += JOptionPane.showInputDialog(null, "Qual Será a nova senha?", "Insira a nova Senha", JOptionPane.PLAIN_MESSAGE);
			
			System.out.println("Valor da Nova Senha: " + newPassword);
			
			if(newPassword.equals("") || newPassword.equals("null")) {
				JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
			}else {
				int confirmation = JOptionPane.showConfirmDialog(null, "Confirma a Mudança?", "", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				System.out.println("Confirmação: " + confirmation);
				if(confirmation == 0) {
					DBConector.editLine("funcionarios", "password", newPassword, "RdF", RdF);
				}else {
					JOptionPane.showMessageDialog(null, "Operação Cancelada", "", JOptionPane.WARNING_MESSAGE);
				}
				
			}
			break;
		}
		
	}
	
	public String drawTable(String objective) {
		
		switch(objective) {
		case "Nome":
			System.out.println("é nome");
			objective = "name";
			break;
		case "Tipo de Funcionario":
			objective = "type";
			break;
		case "Registro de Funcionario":
			objective = "RdF";
			break;
		}
		
		String toDraw = "";
		
		int index = 0;
		if(objective != "*") {
			index = 2;
		}else {
			index = 6;
		}
				
		toDraw += DBConector.readDB(objective, "funcionarios", index);
		
		System.out.println(toDraw);
		
		return toDraw;
	}
	
	public static void firstRendering(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(76, 132, 100, 133);
		
		g.setFont(new Font("impact", 0, 20));
		
		if(overName) {
			g.setColor(Color.darkGray);
			overName = false;
		}else {
			g.setColor(Color.white);
		}
		g.drawString(name, 185, 165);
		
		if(overRdF) {
			g.setColor(Color.darkGray);
			overRdF = false;
		}else {
			g.setColor(Color.white);
		}
		g.drawString(RdF, 185, 200);
		
		if(overCPF) {
			g.setColor(Color.darkGray);
			overCPF = false;
		}else {
			g.setColor(Color.white);
		}
		g.drawString(CPF, 185, 235);
		
		
		
		nameSize = g.getFontMetrics().stringWidth(name);
	}

	
}
