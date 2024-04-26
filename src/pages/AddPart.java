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

public class AddPart extends Insertions {
	
	public AddPart() {
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
		labels.add("Fornecedor da peça: ");
		values.add(DBConector.readDB("Supplier", "Pecas ORDER BY LENGTH(Supplier) DESC LIMIT 1").replaceAll(" § ", ""));
		
		for(int i = 0; i < quantity; i++) {
			textBoxes.add(new Rectangle(80, 200 + 90 * i, values.get(i).length() * 8 + 24, 40));
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
					//System.out.println("Ciclou na caixa de texto " + i);
					selected = i;
					isWriting = true;
				}
			}
		}
	}

	@Override
	protected void writeQuery() {
		String queryInsert = "INSERT INTO pecas (Montagem, Description, Quantity, Price, Creation_Date, Supplier, Status)\nVALUES(";
		String id = "";
		
		if(verifyValues(values.get(0))) {
			id = DBConector.readDB("ID_Montagem", "Montagem WHERE ISO LIKE '%" + values.get(0) + "%'").replaceAll(" § \n", "");
		}else {
			JOptionPane.showMessageDialog(null, "Verifique a OS da Montagem", "Erro ao adicionar a montagem", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		queryInsert += id + ", \"" + values.get(1) + "\", \"" + values.get(2) + "\", \"" + values.get(3) + "\", NOW(), \"" + values.get(4) + "\", 0);";
		
		System.out.println("Query Inserted: \n" + queryInsert);
		
		DBConector.writeDB(queryInsert);
		
		PartsList.wasChanged = true;
		Almoxarifado.quantityParts++;
		Almoxarifado.state = 2;
		
		Functions.partsToOrder += DBConector.readDB("MAX(ID_Parts)", "Pecas");
	}

	@Override
	protected boolean verifyValues(String text) {
		boolean valueExistsInDB = false;
		String textWriten = "'%" + text.replaceAll("[a-zA-Z]", "") + "%'";
		
		if(textWriten.equals("%%")) {
			textWriten = "'%" + text + "%'";
		}
		
		String textFromDB = DBConector.readDB("ID_Montagem", "Montagem WHERE ISO LIKE" + textWriten);
		
		if(!textFromDB.equals(" § \n") || !textFromDB.contains("\n")) {
			valueExistsInDB = true;
		}
		
		return valueExistsInDB;
	}

	@Override
	protected void fillDefaultValues() {
		for(int i = 0; i < quantity; i++) {
			if(values.get(i).equals("")) {
				switch(i % 5) {
				case 1:
				case 4:
					values.set(i, genericString);
					break;
				case 2:
				case 3:
					values.set(i, "" + genericNumeral);
					break;
				case 0:
					values.set(i, "Uso Interno");
					break;
				}
			}
		}
	}

	@Override
	protected void okButtonClick() {
		for(int i = 0; i < quantity; i++) {
			if(values.get(i).equals("")) {
				fillDefaultValues();
			}
		}
		
		writeQuery();
	}

	@Override
	protected void cancelButtonClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		if(isWriting && selected == -1) {
			selected = 0;
		}
		
		if(click && Functions.isOnBox(((Almoxarifado.WIDTH / 3) - okImage.getWidth()/2), 600, 165, 60)) {
			okButtonClick();
		}
		
		switch(selected) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		}
		
		writeTextOnBox();
	}

	@Override
	public void drawErrorMessage(Graphics g) {
		g.setFont(new Font("segoi ui", 0, 10));
		g.setColor(Color.red);
		
		for(int i = 0; i < quantity; i++) {
			g.drawString(" - Preencha este campo", (int)(textBoxes.get(i).getX() + textBoxes.get(i).getWidth()), (int)(textBoxes.get(i).getY()));
		}
		
	}

	@Override
	public void render(Graphics g) {		
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
	}

}
