package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JOptionPane;

import functions.Archiver;
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

	public Profile(String inName, String inRdF, String inCPF) {
		name = inName;
		RdF = String.valueOf(inRdF);
		CPF = String.valueOf(inCPF);
	}
	
	protected byte buttonClick(int mx, int my, boolean type) {
		
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
			if(mx > Almoxarifado.WIDTH - (76 + 165)*2 && mx < Almoxarifado.WIDTH - 76
			&& my > 136 && my < 332) {
				if(mx < Almoxarifado.WIDTH - ((76*2) + 165) && my < 196){
					return 1;
				}else if(mx > Almoxarifado.WIDTH - (76 + 165) && my < 196) {
					return 2;
				}else if(mx < Almoxarifado.WIDTH - ((76*2) + 165) && my > 272) {
					return 3;
				}else if(mx > Almoxarifado.WIDTH - (76 + 165) && my > 272) {
					return 4;
				}
			}
		}
		
		return 0;
	}
	
	protected void changeInformation(int mx, int my, boolean type){
		if(type == false) {
			if(Almoxarifado.mX > 185 && Almoxarifado.mX < Almoxarifado.WIDTH/2) {
				if(Almoxarifado.mY > 165 && Almoxarifado.mY < 185) {
					overName = true;
					if(mouseStatus) {
						editInfo(1);
					}
				}
			}
		}else {
			if(Almoxarifado.mX > 185 && Almoxarifado.mX < Almoxarifado.WIDTH/2) {
				if(Almoxarifado.mY > 145 && Almoxarifado.mY < 165) {
					overName = true;
					if(mouseStatus) {
						editInfo(1);
					}
				}
				
				if(Almoxarifado.mY > 180 && Almoxarifado.mY < 200) {
					overRdF = true;
					if(mouseStatus) {
						editInfo(2);
					}
				}
				
				if(Almoxarifado.mY > 215 && Almoxarifado.mY < 235) {
					overCPF = true;
					if(mouseStatus) {
						editInfo(3);
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
				return;
			}
			
			break;
		}
		
		Archiver.writeOnArchive("alteracao", "o próprio " + column, DBConector.readDB(column, "Funcionarios", "RdF", RdF).replaceAll(" § \n", ""), newText);
		
		DBConector.writeDB("Funcionarios", column, newText, "RdF", RdF);
		
		if(type == 2) {
			RdF = newText;
		}
		
		updateInfo();
	}
	
	protected static void updateInfo() {
		String aux = DBConector.readDB("*", "Funcionarios", "RdF", RdF);
		String[] splitAux = aux.split(" § ");
		
		name = splitAux[1];
		CPF =  cpfFormater(splitAux[2]);
	}
	
	private static String cpfFormater(String CPF) {
		String toReturn = "";
		
		String firstPart = "";
		String secondPart = "";
		String thirdPart = "";
		String fourthPart = "";
		
		if(CPF.length() < 3) {
			toReturn = CPF;
		}else if(CPF.length() > 2 && CPF.length() < 6) {
			firstPart = CPF.substring(0, 3);
			secondPart = CPF.substring(3, CPF.length());
			
			toReturn = firstPart + "." + secondPart;
		}else if(CPF.length() > 5 && CPF.length() < 9) {
			firstPart = CPF.substring(0, 3);
			secondPart = CPF.substring(3, 6);
			thirdPart = CPF.substring(6, CPF.length());
			
			toReturn = firstPart + "." + secondPart + "." + thirdPart;
		}else if(CPF.length() > 8 && CPF.length() < 12) {
			firstPart = CPF.substring(0, 3);
			secondPart = CPF.substring(3, 6);
			thirdPart = CPF.substring(6, 9);
			fourthPart = CPF.substring(9, CPF.length());
			
			toReturn = firstPart + "." + secondPart + "." + thirdPart + "-" + fourthPart;
		}else {
			toReturn = CPF;
		}
		
		return toReturn;
	}
	
	protected static void firstRendering(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(76, 132, 100, 133);
		
		g.setFont(new Font("segoi ui", 0, 20));
		
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
