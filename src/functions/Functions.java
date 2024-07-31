package functions;

import java.awt.Rectangle;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import main.Almoxarifado;

public class Functions {
	
	public static String partsToOrder = "";

	public Functions() {
		// TODO Auto-generated constructor stub
	}
	
	public static ArrayList<String> listToArrayList(String list[]) {
		ArrayList<String> toReturn = new ArrayList<>();
		
		List<String> auxList = Arrays.asList(list);
		
		toReturn = new ArrayList<>(auxList);
		
		return toReturn;
	}
	
	public static String randomGreetingsGen() {
		String greeting = "";
		Random rand = new Random();
		int time = LocalDateTime.now().getHour();
		
		if(time > 11 && time < 18) {
			greeting = "Boa Tarde";
		}else {
			greeting = "Bom Dia";
		}
		
		switch(rand.nextInt(11)) {
		case 1:
			greeting += ", Como tem passado?";
			break;
		case 2:
			greeting += ", Espero que este e-mail te encontre bem!";
			break;
		case 3:
			greeting += " Estimado/a.";
			break;
		case 4:
			greeting += " e Saudações.";
			break;
		case 5:
			greeting += " Cara Equipe.";
			break;
		case 6:
			greeting += " Caros Colegas.";
			break;
		case 7: 
			greeting += " a Toda Equipe.";
			break;
		case 8:
			greeting += " e Saudações a Todos";
			break;
		case 9:
			greeting += " a todos.";
			break;
		case 10:
			greeting += " Estimada Equipe";
			break;
		default:
			greeting += ".";
			break;
		}
		
		return greeting;
	}

	public static boolean emptyString(String s) {
		if(s.equals("") || s.equals(null) || s.equals("null")) {
			//System.out.println("é nula");
			return true;
		}
		//System.out.println("não é nula");
		return false;
	}
	
	public static boolean isOnBox(int x, int y, int w, int h) {
		if(Almoxarifado.mX > x && Almoxarifado.mX < x + w) {
			if(Almoxarifado.mY > y && Almoxarifado.mY < y + h) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isOnBox(Rectangle r) {
		return isOnBox(r.x, r.y, r.width, r.height);
	}
	
	public static double diferenceCurency(String v1, String v2) {
		double returnValue = 0;

		if(emptyString(v1.replaceAll("[^0-9]", ""))) {
			v1 = "0";
		}else if(emptyString(v2.replaceAll("[^0-9]", ""))) {
			v2 = "0";
		}
		
		BigDecimal firstValue = new BigDecimal(v1);
		BigDecimal secondValue = new BigDecimal(v2);
		BigDecimal subResult = firstValue.subtract(secondValue);
		
		returnValue += subResult.doubleValue();

		return returnValue;
	}
	
	public static BigDecimal sumCurency(String v1, String v2) {
		BigDecimal returnValue = new BigDecimal(0);
		
		if(emptyString(v1.replaceAll("[^0-9]", ""))) {
			v1 = "0";
		}else if(emptyString(v2.replaceAll("[^0-9]", ""))) {
			v2 = "0";
		}
		
		BigDecimal firstValue = new BigDecimal(v1);
		BigDecimal secondValue = new BigDecimal(v2);
		returnValue = firstValue.add(secondValue);
		
		return returnValue;
	}
	
	public static HashMap<String, Integer> getInstances(String column, String table) {
		HashMap<String, Integer> toReturn = new HashMap<>();
		
		String queryFound[] = DBConector.readDB(column, table).split(" § \n");
		
		for(int i = 0; i < queryFound.length; i++) {
			if(!toReturn.containsKey(queryFound[i])) {
				toReturn.put(queryFound[i], 1);
			}else {
				toReturn.put(queryFound[i], toReturn.get(queryFound[i]) + 1);
			}
		}
		
		return toReturn;
	}
	
	public static String findBestInstance(String query, String column, String table) {
		int lastValue = 0;
		String toReturn = "";
		HashMap<String, Integer> allValues = getInstances(column, table);
		
		for(String currElement : allValues.keySet()) {
			if(currElement.toLowerCase().contains(query.toLowerCase())) {
				if(allValues.get(currElement) > lastValue) {
					toReturn = currElement;
					lastValue = allValues.get(currElement);
				}
			}
		}
		
		//System.out.println("O Texto mais proximo é " + toReturn);
		
		return toReturn;
	}
	
	public static void generatePurchaseInquery() {
		if(DBConector.getDB().equals("jdbc:mysql://localhost:3306/Tempustec")) {
			if(Almoxarifado.getState() != 0) {
				if(!Almoxarifado.userProfile.getRdF().equals("")) {
					String date = "";
					String emailHeader = "Pedido de Peça, ";
					String emailBody = randomGreetingsGen() + "\n";
					String orderList = "";
					String idsList[] = partsToOrder.split(" § \n"); 
					LocalDateTime ldt = LocalDateTime.now();
					
					date = "" + ldt.getDayOfMonth() + "/" + ldt.getMonthValue() + "/" + ldt.getYear() + " - " + ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond();
					emailHeader += date;
					
					emailBody += "O Usuário " + Almoxarifado.userProfile.getName() + " fez um pedido de peças, seguem os dados: \n"
							+ "--------------------------------------------------------------------------------\n";
					for(int i = 0; i < idsList.length; i++) {
						if(!partsToOrder.equals("")) {
							if(DBConector.readDB("status", "Pecas", "ID_Parts", idsList[i]).equals("0 § \n")) {			
								orderList += "	 - Montagem: " + DBConector.readDB("ISO", "Montagem", "ID_Montagem", DBConector.readDB("Montagem", "Pecas", "ID_Parts", idsList[i]).replaceAll(" § \n", "")).replaceAll(" § \n", "") 
										  +  " - " + DBConector.readDB("Description", "Montagem", "ID_Montagem", DBConector.readDB("Montagem", "Pecas", "ID_Parts", idsList[i]).replaceAll(" § \n", "")).replaceAll(" § \n", "") + "\n"
										  +  "	 - Descrição: " + DBConector.readDB("Description", "Pecas", "ID_Parts", idsList[i]).replaceAll(" § \n", "") + "\n"
										  +  "	 - Quantidade: " + DBConector.readDB("Quantity", "Pecas", "ID_Parts", idsList[i]).replaceAll(" § \n", "") + " \n"
										  +  "--------------------------------------------------------------------------------\n";
							}
						}
					}
					
					if(!orderList.equals("")) {
						int confirm = JOptionPane.showConfirmDialog(null, "Deseja enviar o requisito de compra de peças?", "Enviar Email?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
						
						if(confirm != 0) {
							return;
						}
						Email.sendReport(emailHeader, emailBody + orderList);
					}
				}
			}
		}
	}

	public static boolean isOnBox(double x, double y, double width, double height) {
		return isOnBox((int)(x), (int)(y), (int)(width), (int)(height));
	}
	
}
