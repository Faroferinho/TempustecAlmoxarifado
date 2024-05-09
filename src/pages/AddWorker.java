package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JOptionPane;

import functions.DBConector;
import functions.Functions;
import functions.Insertions;
import main.Almoxarifado;
import main.UserInterface;

public class AddWorker extends Insertions{
	
	BufferedImage checkbox = Almoxarifado.imgManag.getSprite(455, 395, 18, 18);;
	BufferedImage check = Almoxarifado.imgManag.getSprite(452, 371, 21, 21);
	
	boolean addedFromAdmnistrator = false;
	boolean SignIn = false;

	public AddWorker() {
		quantity = 2;
		border = 15;
		
		title = "Cadastro de Funcionario:";
		
		labels.add("Nome:");
		values.add(DBConector.readDB("Name", "Funcionarios ORDER BY LENGTH(Name) DESC LIMIT 1").replaceAll(" § ", ""));
		labels.add("CPF: ");
		values.add(DBConector.readDB("CPF", "Funcionarios ORDER BY LENGTH(CPF) DESC LIMIT 1").replaceAll(" § ", ""));
				
		for(int i = 0; i < quantity; i++) {
			
			textBoxes.add(new Rectangle(80, 200 + 160 * i, values.get(i).length() * 8 + 24, 40));
			values.set(i, "");
			
		}
	}

	@Override
	protected void writeTextOnBox() {
		if(click) {
			selected = -1;
			isWriting = false;
			for(int i = 0; i < quantity; i++) {
				if(Functions.isOnBox(textBoxes.get(i))) {
					System.out.println("Ciclou na caixa de texto " + i);
					selected = i;
					isWriting = true;
					
					recomendation = "";
				}
			}
		}
	}
	
	private String generateRdF() {
		Random rng = new Random();
		String newRdF = "" + rng.nextInt(10000);
		String RdFs[] = DBConector.readDB("RdF", "Funcionarios").split(" § \n");
		
		for(int i = 0; i < RdFs.length; i++) {
			if(newRdF.equals(RdFs[i])) {
				newRdF = generateRdF();
			}
		}
		
		return newRdF;
	}

	@Override
	protected void writeQuery() {
		String query = "INSERT INTO Funcionarios\nVALUES(" + generateRdF() + ", \"";
		query += values.get(0) + "\", \"";
		query += values.get(1) + "\", \"Tempustec2023\", 0);";
		
		System.out.println("Provided Query: " + query);
		
		DBConector.writeDB(query);
		
		Almoxarifado.quantityWorkers++;
		if(addedFromAdmnistrator) {
			((Admnistrator)(Almoxarifado.userProfile)).fillMultiArray();
			addedFromAdmnistrator = false;
		}
		if(SignIn) {
			Almoxarifado.state = 0;
			SignIn = false;
		}else {
			Almoxarifado.state = 1;
		}
	}

	@Override
	protected void fillDefaultValues() {
		if(Functions.emptyString(values.get(0))) {
			values.set(0, "User");
		}if(Functions.emptyString(values.get(1))) {
			values.set(1, "12345678910");
		}
	}

	@Override
	protected void showRecomendations() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void okButtonClick() {
		System.out.println("textValues: \n" + values.get(0) + "\n" + values.get(1));
		
		fillDefaultValues();
		
		writeQuery();
		
		for(int i = 0; i < quantity; i++) {
			values.set(i, "");
		}
	}

	@Override
	protected void cancelButtonClick() {
		int toVerif = JOptionPane.showConfirmDialog(null, "Você gostaria cancelar a ação?", "Confirmar o Cancelamento", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if(toVerif == 0) {
			if(Almoxarifado.userProfile.getRdF().equals("")) {
				Almoxarifado.state = 0;				
			}else {
				Almoxarifado.state = 1;
			}
		}
	}

	@Override
	public void tick() {
		if(isWriting && selected == -1) {
			selected = 0;
		}
				
		writeTextOnBox();
		
		if(click) {
			if(Functions.isOnBox(((Almoxarifado.WIDTH / 3) - okImage.getWidth()/2), 600, 165, 60)) {
				okButtonClick();
			}else if(Functions.isOnBox(((Almoxarifado.WIDTH / 3) * 2 - okImage.getWidth()/2), 600, 165, 60)) {
				cancelButtonClick();
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("segoi ui", 1, 40));
		g.drawString(title, middleScreenX - g.getFontMetrics().stringWidth(title)/2, 160);
		
		
		for(int i = 0; i < quantity; i++) {
			g.setFont(new Font("segoi ui", 1, 18));
			g.drawString(labels.get(i), textBoxes.get(i).x + 12, textBoxes.get(i).y - 5);
			
			UserInterface.createTextBox(g, textBoxes.get(i), border);
			
			g.setFont(new Font("segoi ui", 0, 15));
			g.drawString(values.get(i), textBoxes.get(i).x + 12, textBoxes.get(i).y + g.getFontMetrics().getHeight() + 8);
			
			g.setColor(Color.white);
		}
		
		if(hasRecomendation && selected != -1) {	
			Rectangle newRectangle = new Rectangle((int)(textBoxes.get(selected).getX()), (int)(textBoxes.get(selected).getY() + textBoxes.get(selected).getHeight()), 
					(int)(textBoxes.get(selected).getWidth()), (int)(textBoxes.get(selected).getHeight()));
			
			UserInterface.createTextBox(g, newRectangle, 0);
			g.drawString(recomendation, (int)(textBoxes.get(selected).getX() + 5), (int)(textBoxes.get(selected).getY() + textBoxes.get(selected).getHeight() + g.getFontMetrics().getHeight() + 5));
			
			if(click) {
				if(Functions.isOnBox(newRectangle)) {
					values.set(selected, recomendation);
				}
			}
		}
		
		g.drawImage(okImage, (Almoxarifado.WIDTH / 3) - okImage.getWidth()/2, 600, null);
		g.drawImage(cancelImage, (Almoxarifado.WIDTH / 3) * 2 - cancelImage.getWidth()/2, 600, null);
		
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 3) - okImage.getWidth()/2, 600);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 3) * 2 - okImage.getWidth()/2, 600);
		
		if(isWriting) {
			drawCursor(g);
		}
		
	}

}
