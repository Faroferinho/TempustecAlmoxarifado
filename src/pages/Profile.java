package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import functions.DBConector;
import functions.Functions;
import main.Almoxarifado;

public abstract class Profile {
	
	protected String rdf = "";
	protected String name = "";
	protected String CPF = "";
	
	private Color rdfColor = Color.white;
	private Color nameColor = Color.white;
	private Color cpfColor = Color.white;
	
	protected BufferedImage bttn_changePW = Almoxarifado.imgManag.getSprite(475, 120, 165, 60);
	protected BufferedImage bttn_editInfo = Almoxarifado.imgManag.getSprite(475, 0, 165, 60);
	
	private int tryoutCounter = 0;
	
	public boolean mouseStatus = false;
	
	public boolean isEditing = false;
	
	public Profile(String RdF) {
		rdf = RdF;
		name = DBConector.readDB("name", "funcionarios", "RdF", RdF).replaceAll(" § \n", "");
		CPF = DBConector.readDB("CPF", "funcionarios", "RdF", RdF).replaceAll(" § \n", "");
	}
	
	public String getName() {
		return name;
	}

	public String getRdF() {
		return rdf;
	}
	
	public String getCPF() {
		return CPF;
	}
	
	protected void changePassword() {
		String textInserted = "" + JOptionPane.showInputDialog(null, "Confirme com a sua senha atual", "Confirmação de Edição de Senha", JOptionPane.WARNING_MESSAGE);
		
		if(Functions.emptyString(textInserted)) {
			return;
		}
		
		while(tryoutCounter < 3) {
			if(textInserted.equals(DBConector.readDB("password", "funcionarios", "RdF", rdf).replaceAll(" § \n", ""))) {
				tryoutCounter = 0;
				
				String newPW = "" + JOptionPane.showInputDialog(null, "Insira uma nova senha", "Nova Senha", JOptionPane.PLAIN_MESSAGE);
				
				if(Functions.emptyString(newPW)) {
					return;
				}
				
				DBConector.writeDB("Funcionario", "password", newPW, "RdF", rdf);			
			}else{
				JOptionPane.showMessageDialog(null, "Tente Novamente", "Senha Inserida Incorreta", JOptionPane.ERROR_MESSAGE);
				textInserted = "" + JOptionPane.showInputDialog(null, "Confirme com a sua senha atual", "Confirmação de Edição de Senha", JOptionPane.WARNING_MESSAGE);
				tryoutCounter++;
			}
		}
	}
	
	protected void editInfo() {
		if(isEditing) {
			if(Almoxarifado.mX > 80 && Almoxarifado.mX < 600) {
				if(Almoxarifado.mY > 140 && Almoxarifado.mY < 190) {
					nameColor = Color.gray;
					
					rdfColor = Color.white;
					cpfColor = Color.white;
					
					if(mouseStatus) {
						String newName = "" + JOptionPane.showInputDialog(null, "Insira novo Nome", "Alterar Perfil", JOptionPane.PLAIN_MESSAGE);
						alterName(newName);
					}
					
				}else if(Almoxarifado.mY > 220 && Almoxarifado.mY < 250){
					rdfColor = Color.gray;
					
					nameColor = Color.white;
					cpfColor = Color.white;

					if(mouseStatus) {
						String newRdF = "" + JOptionPane.showInputDialog(null, "Insira novo RdF", "Alterar Perfil", JOptionPane.PLAIN_MESSAGE);
						alterRdF(newRdF);
					}
					
				}else if(Almoxarifado.mY > 280 && Almoxarifado.mY < 310) {
					cpfColor = Color.gray;
					
					rdfColor = Color.white;
					nameColor = Color.white;
					
					if(mouseStatus) {
						String newCPF = "" + JOptionPane.showInputDialog(null, "Insira novo CPF", "Alterar Perfil", JOptionPane.PLAIN_MESSAGE);
						alterCPF(newCPF);
					}
				}else {
					nameColor = Color.white;
					rdfColor = Color.white;
					cpfColor = Color.white;
				}
			}else {
				nameColor = Color.white;
				rdfColor = Color.white;
				cpfColor = Color.white;
			}
		}else {
			nameColor = Color.white;
			rdfColor = Color.white;
			cpfColor = Color.white;
		}
		
	}
	
	private void alterRdF(String newText) {
		
		if(Functions.emptyString(newText.replaceAll("[^0-9]", ""))) {
			return;
		}
		
		while(newText.replaceAll("[^0-9]", "").length() > 4) {
			newText = "" + JOptionPane.showInputDialog(null, "Insira um RdF valido", "Alterar Perfil", JOptionPane.ERROR_MESSAGE);
			if(Functions.emptyString(newText.replaceAll("[^0-9]", ""))) {
				return;
			}
		}
		
		DBConector.writeDB("funcionarios", "RdF", newText.replaceAll("[^0-9]", ""), "RdF", rdf);
		
		rdf = newText.replaceAll("[^0-9]", "");
		isEditing = false;
	}
	
	private void alterName(String newText) {
		
		if(Functions.emptyString(newText)) {
			return;
		}
		
		DBConector.writeDB("funcionarios", "name", newText, "RdF", rdf);
		
		name = newText;
		isEditing = false;
	}
	
	private void alterCPF(String newText) {
		
		if(Functions.emptyString(newText)) {
			return;
		}
		
		while(newText.replaceAll("[^0-9]", "").length() != 11) {
			newText = "" + JOptionPane.showInputDialog(null, "Insira um CPF valido", "Alterar Perfil", JOptionPane.ERROR_MESSAGE);
			
			if(Functions.emptyString(newText)) {
				return;
			}
		}
		
		DBConector.writeDB("funcionarios", "CPF", newText.replaceAll("[^0-9]", ""), "RdF", rdf);
		
		CPF = newText.replaceAll("[^0-9]", "");
		isEditing = false;
	}
	
	private String formatCPF(String cpf) {
		return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
	}
	
	protected void drawUserBasis(Graphics g) {
		g.setColor(nameColor);
		g.setFont(new Font("segoi ui", 1, 40));
		g.drawString(name, 80, 180);
		
		g.setFont(new Font("segoi ui", 1, 20));
		g.setColor(rdfColor);
		g.drawString(rdf, 80, 240);
		g.setColor(cpfColor);
		g.drawString(formatCPF(CPF), 80, 300);
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
}
