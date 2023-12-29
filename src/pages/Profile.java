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
					return 1;
				}else if(mx > (Almoxarifado.WIDTH / 4) * 3 - 80 && mx < (Almoxarifado.WIDTH / 4)*3 + 80) {
					return 2;
				}
			}
		}else {
			if(my > Almoxarifado.HEIGHT / 2 + 105 && my < Almoxarifado.HEIGHT / 2 + 190) {
				if(isEditing == false) {
					if(mx > (Almoxarifado.WIDTH / 5) - 80 && mx < (Almoxarifado.WIDTH / 5) + 80) {
						//Editar Perfil;
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
						return 1;
					}else if(mx > (Almoxarifado.WIDTH / 4) * 3 - 80 && mx < (Almoxarifado.WIDTH / 3) * 2 + 80) {
						return 4;
					}
				}
			}
		}
		
		return 0;
	}
	
	public void changeInformation(int mx, int my, boolean type){
		if(type == false) {
			if(Almoxarifado.mX > 185 && Almoxarifado.mX < Almoxarifado.WIDTH/2) {
				if(Almoxarifado.mY > 165 && Almoxarifado.mY < 185) {
					overName = true;
					if(mouseStatus) {
						editInfo(1);
						mouseStatus = false;
					}
				}
			}
		}else {
			if(Almoxarifado.mX > 185 && Almoxarifado.mX < Almoxarifado.WIDTH/2) {
				if(Almoxarifado.mY > 145 && Almoxarifado.mY < 165) {
					overName = true;
					if(mouseStatus) {
						editInfo(1);
						mouseStatus = false;
					}
				}
				
				if(Almoxarifado.mY > 180 && Almoxarifado.mY < 200) {
					overRdF = true;
					if(mouseStatus) {
						editInfo(2);
						mouseStatus = false;
					}
				}
				
				if(Almoxarifado.mY > 215 && Almoxarifado.mY < 235) {
					overCPF = true;
					if(mouseStatus) {
						editInfo(3);
						mouseStatus = false;
					}
				}
			}
		}
	}
	
	protected void editInfo(int type) {
		String column = "";
		String newText = "";
		
		switch(type) {
		case 1:
			newText += JOptionPane.showInputDialog(null, "Insira o novo nome", "Configurando Perfil", JOptionPane.PLAIN_MESSAGE);
			column = "Name";
			break;
		case 2:
			newText += JOptionPane.showInputDialog(null, "Insira o novo Registro de Funcionario", "Configurando Perfil", JOptionPane.PLAIN_MESSAGE);
			column = "RdF";
			break;
		case 3:
			newText += JOptionPane.showInputDialog(null, "Insira o novo CPF", "Configurando Perfil", JOptionPane.PLAIN_MESSAGE);
			column = "CPF";
			break;
		case 4:
			newText += JOptionPane.showInputDialog(null, "Insira o nova Senha", "Configurando Perfil", JOptionPane.PLAIN_MESSAGE);
			column = "Password";
			
			int confirm = JOptionPane.showConfirmDialog(null, "Você tem certeza que deseja mudar a Senha?", "", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(confirm != 0) {
				System.out.println("Confimação Negada");
				return;
			}
			
			break;
		}
		
		DBConector.editLine("Funcionarios", column, newText, "RdF", RdF);
		
		if(type == 2) {
			RdF = newText;
		}
		
		updateInfo();
	}
	
	public static void updateInfo() {
		String aux = DBConector.findInDB("*", "Funcionarios", "RdF", RdF);
		String[] splitAux = aux.split(" § ");
		
		name = splitAux[1];
		CPF = splitAux[2];
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
