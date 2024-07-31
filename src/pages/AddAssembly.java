package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JOptionPane;

import functions.DBConector;
import functions.Functions;
import functions.Insertions;
import main.Almoxarifado;
import main.UserInterface;

public class AddAssembly extends Insertions{

	public AddAssembly() {
		quantity = 3;
		border = 15;
		
		title = "Cadastro de Montagem:";
		
		labels.add("Valor da O.S.:");
		values.add(DBConector.readDB("ISO", "Montagem ORDER BY LENGTH(ISO) DESC LIMIT 1").replaceAll(" § ", ""));
		labels.add("Descrição: ");
		values.add(DBConector.readDB("Description", "Montagem ORDER BY LENGTH(Description) DESC LIMIT 1").replaceAll(" § ", ""));
		labels.add("Empresa: ");
		values.add(DBConector.readDB("Company", "Montagem ORDER BY LENGTH(Company) DESC LIMIT 1").replaceAll(" § ", ""));
		
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
					clearIndex();
				}
			}
		}
	}

	@Override
	protected void writeQuery() {
		String queryInsert = "INSERT INTO Montagem (ISO, Description, Company, Cost)\nVALUES(\"";		
		queryInsert += values.get(0);
		queryInsert += "\", \"" + values.get(1) + "\", \"" + values.get(2) + "\",  0);";
		
		System.out.println("Query Inserted: \n" + queryInsert);
		
		DBConector.writeDB(queryInsert);

		ProjectList.updateProjectList = true;
		Almoxarifado.setState(3);
	}

	@Override
	protected void fillDefaultValues() {
		for(int i = 0; i < quantity; i++) {
			if(values.get(i).equals("")) {
				values.set(i, genericString);
			}
		}	
			
	}

	@Override
	protected void showRecomendations() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void okButtonClick() {
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
			Almoxarifado.setState(3);
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
			
			alterTextBoxSize(g, i);
			
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
