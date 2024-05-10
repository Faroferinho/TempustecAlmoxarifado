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
		
		int auxX = 0;
		int auxY = 0;
		
		for(int i = 0; i < quantity; i++) {
			
			if(auxX > 1) {
				auxX = 0;
				auxY++;
			}
			
			textBoxes.add(new Rectangle(80 + ((middleScreenX - 80) * auxX), 200 + 160 * auxY, values.get(i).length() * 8 + 24, 40));
			values.set(i, "");
			
			auxX++;
		}
	}

	@Override
	protected void writeTextOnBox() {
		if(click) {
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

	@Override
	protected void writeQuery() {
		String queryInsert = "INSERT INTO pecas (Montagem, Description, Quantity, Price, Creation_Date, Supplier, Status)\nVALUES(\"";
		
		String id = "";
		String desc = values.get(1);
		String qntt = values.get(2);
		String price = values.get(3);
		String sppr = values.get(4);
		
		if(verifyValues(values.get(0))) {
			id = DBConector.readDB("ID_Montagem", "Montagem WHERE ISO LIKE '%" + values.get(0) + "%'").replaceAll(" § \n", "");
		}else {
			JOptionPane.showMessageDialog(null, "Verifique a OS da Montagem", "Erro ao adicionar a montagem", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		desc = desc.replaceAll("\"", "''").replaceAll("[§\r\n]", "");
		qntt = qntt.replaceAll("\"", "''").replaceAll("[§\r\n]", "");
		price = PartsList.formatNumb(price);
		sppr = sppr.replaceAll("\"", "''").replaceAll("[§\r\n]", "");
		
		queryInsert += id + "\", \"" + desc + "\", \"" + qntt + "\", " + price + ", NOW(), \"" + sppr + "\", 0);";
		
		System.out.println("Query Inserted: \n" + queryInsert);
		
		DBConector.writeDB(queryInsert);
		
		Almoxarifado.quantityParts++;
		Functions.partsToOrder += DBConector.readDB("MAX(ID_Parts)", "Pecas");
	}

	protected boolean verifyValues(String text) {
		boolean valueExistsInDB = false;
		String textWriten = "'%" + text.replaceAll("[a-zA-Z]", "") + "%'";
		
		if(textWriten.equals("'%%'")) {
			textWriten = "'%" + text + "%'";
		}
		
		String textFromDB = DBConector.readDB("ID_Montagem", "Montagem WHERE ISO LIKE" + textWriten);
		if(!Functions.emptyString(textFromDB.replaceAll("[§\n]", ""))) {
			System.out.println("Valor Existe no DB");
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
		
		for(int i = 0; i < quantity; i++) {
			values.set(i, "");
		}
		
		PartsList.wasChanged = true;
		Almoxarifado.state = 2;
	}

	@Override
	protected void cancelButtonClick() {
		int toVerif = JOptionPane.showConfirmDialog(null, "Você gostaria cancelar a ação?", "Confirmar o Cancelamento", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if(toVerif == 0) {
			Almoxarifado.state = 2;
		}
	}
	
	protected void showRecomendations() {
		String column = "";
		String table = "pecas";
		
		switch(selected) {
		case 0:
			column = "ISO";
			table = "Montagem";
			break;
		case 1:
			column = "Description";
			break;
		case 2:
			column = "quantity";
			break;
		case 3:
			column = "Price";
			break;
		case 4:
			column = "supplier";
			break;
		default:
			return;
		}
		
		recomendation = Functions.findBestInstance(values.get(selected), column, table);
		
		if(!Functions.emptyString(recomendation)) {
			hasRecomendation = true;
			//System.out.print("recomendation está vazia?");
		}else {
			hasRecomendation = false;
		}
		
		//System.out.println("Recomendações: " + Functions.findBestInstance(values.get(selected), column, table));
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
					System.out.println("Clicou na área");
					values.set(selected, recomendation);
				}else {
					if(!recomendation.equals("")) {
						isWriting = false;
						selected = -1;
					}else {
						hasRecomendation = false;
					}
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
