package functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import main.Almoxarifado;

public class DBConector {
	
	//Poderia trocar o user pelo perfil do usuário em um futuro distante;
	private static String urlDBTempustec = "jdbc:mysql://localhost:3306/Tempustec";
	private static String user = "root";
	private static String password = "1234";
	
	public DBConector() {
		
	}
	
	public static String readDB(String objective, String table){
		
		String query = "select " + objective + " from " + table;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver \"MySQL Connector-J\" e Tente Novamente", 
			"Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		System.out.println("Query: \n" + query);
		String returnData = "";
		
		int maxIndex = checkSize(objective, table);
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				for(int i = 1; i < maxIndex; i++) {

					returnData += result.getString(i) + " § ";

				}
				returnData += "\n";
			}
			con.close();
		} catch(SQLException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao Efetuar Ação", "Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		//System.out.println("Informação do DB: \n" + returnData);
		
		return returnData;
	}
	
	public static void writeDB(String objective) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver \"MySQL Connector-J\" e Tente Novamente",
			"Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			
			statement.executeUpdate(objective);
			
			con.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public static void editLine(String Table, String objective, String newInfo, String PK, String PrimaryKey) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver \"MySQL Connector-J\" e Tente Novamente", "Erro no Java Data Base Conector", 
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		String query = "UPDATE " + Table + " SET " + objective + " = \"" + newInfo + "\" WHERE " + PK + " = " + PrimaryKey;
		//System.out.println("Editar Linha: " + query);
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			
			statement.executeUpdate(query);
			
			con.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public static String findInDB(String objective, String table, String column, String key) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver \"MySQL Connector-J\" e Tente Novamente", "Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		String query = "SELECT " + objective + " FROM " + table + " WHERE " + column + " = " + key;
		String answer = "";
		int max = checkSize(objective, table);
		
		//System.out.println("Encontrar no DB: " + query);
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			ResultSet rslt = statement.executeQuery(query);
			
			
			while(rslt.next()) {
				for(int i = 1; i < max; i++) {
					answer += rslt.getString(i) + " § ";
				}
				answer += "\n";
			}
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//System.out.println("Resultado do Encontrar no DB: \n" + answer);
		
		return answer;
	}

	public static void Archive(String ID) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver \"MySQL Connector-J\" e Tente Novamente", 
					"Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		String query = "SELECT * FROM Montagem WHERE ID_Montagem = " + ID;
		String auxInfoFromMontagem = "";
		
		LocalDateTime moment = LocalDateTime.now();
		String auxDateTime = moment.toString();
		auxDateTime = auxDateTime.substring(0, 19);
		auxDateTime = auxDateTime.replaceAll("T", " ");
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			ResultSet rslt = statement.executeQuery(query);
			
			while(rslt.next()) {
				String auxString = "";
				for(int i = 1; i < checkSize("*", "Montagem"); i++) {
					
					switch(i) {
					case 1:
					case 6:
						auxString = ", \"";
						break;
					case 2:
					case 3:
					case 4:
						auxString = "\", \"";
						break;
					case 5:
						auxString = "\", ";
						break;
					}
					
					auxInfoFromMontagem += rslt.getString(i) + auxString;
				}
			}
			
			query = "INSERT INTO Arquivo VALUES (" + Almoxarifado.quantityArchives + ", " + auxInfoFromMontagem + auxDateTime + "\", " 
			+ Almoxarifado.rdf + ");";
			Almoxarifado.quantityArchives++;
			
			//System.out.println(query);
			
			statement.executeUpdate(query);
			
			query = "SELECT * FROM Pecas WHERE Montagem = " + ID;
			rslt = statement.executeQuery(query);
			
			String partsQuery = "";
			while(rslt.next()) {
				partsQuery += "INSERT INTO Arquivo_Pecas VALUES (" + Almoxarifado.quantityArchiveParts + ", ";
				for(int i = 1; i < checkSize("*", "Pecas"); i++) {
					String aux = "";
					
					switch(i) {
					case 1:
					case 4:
					case 5:
						aux = ", ";
						break;
					case 2:
					case 6:
						aux = ", \"";
						break;
					case 3:
					case 7:
						aux = "\", ";
						break;
					case 8:
						aux = ") \n";
					}
					
					partsQuery += rslt.getString(i) + aux;
				}
				
				Almoxarifado.quantityArchiveParts++;
			}
			if(!partsQuery.equals("")) {
				String[] brokenQuery;
				
				brokenQuery = partsQuery.split(" \n");
				
				for(int i = 0; i < brokenQuery.length; i++) {
					statement.executeUpdate(brokenQuery[i]);
					Almoxarifado.quantityParts--;
				}
			}
			
			query = "DELETE FROM Montagem WHERE ID_Montagem = " + ID;
			statement.executeUpdate(query);
			
			query = "DELETE FROM Pecas WHERE Montagem = " + ID;
			statement.executeUpdate(query);
			
			Almoxarifado.quantityAssembly--;
			
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static double getAssemblyValue(String ID) {
		String priceQuery = "SELECT price FROM Pecas WHERE Montagem = " + ID;
		String quantityQuery = "SELECT quantity FROM Pecas WHERE Montagem = " + ID;
		
		ArrayList<String> prices = new ArrayList<>();
		ArrayList<String> quantities = new ArrayList<>();
		
		double value = 0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver \"MySQL Connector-J\" e Tente Novamente", "Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			
			ResultSet rsltPrice = statement.executeQuery(priceQuery);
			
			while(rsltPrice.next()) {
				prices.add(rsltPrice.getString(1));
			}
			
			ResultSet rsltQuantity = statement.executeQuery(quantityQuery);
			
			while(rsltQuantity.next()) {
				quantities.add(rsltQuantity.getString(1).replaceAll("[^0-9.]", ""));
			}
			
			for(int i = 0; i < prices.size(); i++) {
				System.out.println("Preço: " + prices.get(i));
				System.out.println("Quantidade: " + quantities.get(i));
				value += Double.parseDouble(prices.get(i)) * Double.parseDouble(quantities.get(i));
			}
			
			System.out.println("Valor da Montagem: " + value);
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return value;
	}
	
	public static double totalValueExpended() {
		String command = "SELECT COST FROM Montagem";
		double returnValue = 0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao Conectar com o Driver, Contacte alguém especializado", "Erro ao Conectar ao Driver", JOptionPane.PLAIN_MESSAGE, null);
		}
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			ResultSet rslt = statement.executeQuery(command);
			
			int i = 0;
			System.out.println("=========================================================");
			while(rslt.next()) {
				i++;
				System.out.println(i + "º Valor: " + rslt.getString(1));
				returnValue += rslt.getDouble(1);
				System.out.println("=========================================================");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return returnValue;
	}
	
	public static int counterOfElements(String where, String condition) {
		int returnCounter = 0;
		String query = "Select * from " + where + " where " + condition;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			ResultSet rslt = statement.executeQuery(query);
			
			while(rslt.next()) {
				returnCounter++;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return returnCounter;
	}
	
	public static int counterOfElements(String where) {
		int returnCounter = 0;
		String query = "Select * from " + where;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			ResultSet rslt = statement.executeQuery(query);
			
			while(rslt.next()) {
				returnCounter++;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return returnCounter;
	}
	
	private static int checkSize(String objective, String table) {
		int max = 0;
		
		if(table.equals("funcionarios") || table.equals("Funcionarios") || table.equals("FUNCIONARIOS")) {
			max = 6;
		}else if(table.equals("montagem") || table.equals("Montagem") || table.equals("MONTAGEM")) {
			max = 7;
		}else if(table.equals("pecas") || table.equals("Pecas") || table.equals("PECAS")) {
			max = 9;
		}else if(table.equals("arquivo") || table.equals("Arquivo") || table.equals("ARQUIVO")) {
			max = 10;
		}else if(table.equals("arquivo_pecas") || table.equals("Arquivo_pecas") || table.equals("Arquivo_Pecas") || table.equals("ARQUIVO_Pecas")) {
			max = 10;
		}
		
		if(!objective.equals("*")) {
			max  = 2;
		}
		
		return max;
	}
	
}
