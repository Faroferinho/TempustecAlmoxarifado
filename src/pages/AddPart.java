package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import functions.DBConector;
import functions.Functions;
import functions.Insertions;
import main.Almoxarifado;
import main.UserInterface;

public class AddPart extends Insertions {
	
	public AddPart() {
		// TODO Auto-generated constructor stub
		quantity = 5;
		border = 15;
		
		title = "Cadastro de Peça:";
		
		labels.add("O.S. da Peça:");
		values.add(DBConector.readDB("ISO", "Montagem ORDER BY LENGTH(ISO) DESC LIMIT 1").replaceAll(" § ", ""));
		labels.add("Descrição: ");
		values.add(DBConector.readDB("Description", "Pecas ORDER BY LENGTH(Description) DESC LIMIT 1").replaceAll(" § ", ""));
		labels.add("Quantidade das Peças: ");
		values.add(DBConector.readDB("Quantity", "Pecas ORDER BY LENGTH(Quantity) DESC LIMIT 1").replaceAll(" § ", ""));
		labels.add("Custo da Peça: ");
		values.add(DBConector.readDB("MAX(Price)", "Pecas").replaceAll(" § ", ""));
		labels.add("Fornecedor que vendeu a peça: ");
		values.add(DBConector.readDB("Supplier", "Pecas ORDER BY LENGTH(Supplier) DESC LIMIT 1").replaceAll(" § ", ""));
		
		for(int i = 0; i < quantity; i++) {
			textBoxes.add(new Rectangle(80, 200 + 90 * i, values.get(i).length() * 8 + 24, 40));
			values.set(i, "");
		}
	}

	@Override
	protected void writeQuery() {
		// TODO Auto-generated method stub
		if(click) {
			selected = -1;
			isWriting = false;
			for(int i = 0; i < quantity; i++) {
				if(Functions.isOnBox(textBoxes.get(i))) {
					//System.out.println("Ciclou na caixa de texto " + i);
					selected = i;
					isWriting = true;
				}
			}
		}
	}

	@Override
	protected void writeOnTextBoxes(Graphics g) {
		// TODO Auto-generated method stub
		
		
		g.setColor(Color.magenta);
		
	}

	@Override
	protected void verifyValues() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		if(isWriting && selected == -1) {
			selected = 0;
		}
		
		writeQuery();
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
		g.setColor(Color.white);
		g.setFont(new Font("segoi ui", 1, 40));
		g.drawString(title, Almoxarifado.WIDTH/2 - g.getFontMetrics().stringWidth(title)/2, 160);
		
		
		for(int i = 0; i < quantity; i++) {
			g.setFont(new Font("segoi ui", 1, 18));
			g.drawString(labels.get(i), textBoxes.get(i).x + 12, textBoxes.get(i).y - 5);
			
			UserInterface.createTextBox(g, textBoxes.get(i), border);
			
			g.setFont(new Font("segoi ui", 0, 15));
			g.drawString(values.get(i), textBoxes.get(i).x + 12, textBoxes.get(i).y + g.getFontMetrics().getHeight() + 8);
			
			g.setColor(Color.white);
		}
		
		g.drawImage(okImage, (Almoxarifado.WIDTH / 3) - okImage.getWidth()/2, 600, null);
		g.drawImage(cancelImage, (Almoxarifado.WIDTH / 3) * 2 - cancelImage.getWidth()/2, 600, null);
		
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 3) - okImage.getWidth()/2, 600);
		UserInterface.isOnSmallButton(g, (Almoxarifado.WIDTH / 3) * 2 - okImage.getWidth()/2, 600);
		
		writeOnTextBoxes(g);
	}

}
