package functions;

import java.math.BigDecimal;
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
	private static String user = "Almoxarifado";
	private static String password = "Tempustec2023";
	
	public DBConector() {
		
	}
	
	public static String getDB() {
		return urlDBTempustec;
	}
	
	public static String readDB(String query) {
		String returnData = "";
		
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
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				for(int i = 1; i < result.getMetaData().getColumnCount() + 1; i++) {

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
		
		return returnData;
	}
	
	public static String readDB(String objective, String table){
		String query = "select " + objective + " from " + table;
		int maxIndex = checkSize(objective, table);
		String returnData = "";
		//System.out.println("Query: \n" + query);
		
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
	
	public static String readDB(String objective, String table, String column, String key) {
		String query = "SELECT " + objective + " FROM " + table + " WHERE " + column + " = " + key;
		int max = checkSize(objective, table);
		String returnData = "";
		
		//System.out.println("Encontrar no DB: " + query);
		
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
			ResultSet rslt = statement.executeQuery(query);
			
			
			while(rslt.next()) {
				for(int i = 1; i < max; i++) {
					returnData += rslt.getString(i) + " § ";
				}
				returnData += "\n";
			}
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
	
	public static void writeDB(String Table, String objective, String newInfo, String PK, String primaryKey) {
		String query = "UPDATE " + Table + " SET " + objective + " = \"" + newInfo + "\" WHERE " + PK + " = " + primaryKey;
		//System.out.println("Editar Linha: " + query);
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver \"MySQL Connector-J\" e Tente Novamente", "Erro no Java Data Base Conector", 
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			
			statement.executeUpdate(query);
			
			con.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	public static void Archive(String ID) {
		String query = "SELECT * FROM Montagem WHERE ID_Montagem = " + ID;
		String auxInfoFromMontagem = "";
		
		LocalDateTime moment = LocalDateTime.now();
		String auxDateTime = moment.toString();
		auxDateTime = auxDateTime.substring(0, 19);
		auxDateTime = auxDateTime.replaceAll("T", " ");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver \"MySQL Connector-J\" e Tente Novamente", 
					"Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
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
			
			query = "INSERT INTO Arquivo(ID_Montagem, ISO, Description, Company, Image, Cost, Archive_Moment, Archiver_RdF) VALUES (" + auxInfoFromMontagem + auxDateTime + "\", " 
			+ Almoxarifado.userProfile.getRdF() + ");";
			Almoxarifado.quantityArchives++;
			
			statement.executeUpdate(query);
			
			statement.execute("INSERT INTO Arquivo_Pecas(ID_Parts, Montagem, Description, Quantity, Price, Creation_Date, Supplier, Status) SELECT * FROM Pecas WHERE Montagem = " + ID);
			
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
	
	public static void restoreArchived(String ID) {
		String restoreAssembly = "INSERT INTO Montagem(ID_Montagem, ISO, Description, Company, Cost)\r\n"
							   + "SELECT ID_Montagem, ISO, Description, Company, Cost\r\n"
							   + "FROM Arquivo\r\n"
							   + "WHERE ID_Arquivo = " + ID;
		String restoreParts = "INSERT INTO Pecas\r\n"
							+ "SELECT ID_Parts, Montagem, Description, Quantity, Price, Creation_Date, Supplier, Status\r\n"
							+ "FROM Arquivo_Pecas\r\n"
							+ "WHERE Montagem = " + readDB("ID_Montagem", "Arquivo", "ID_Arquivo", ID).replaceAll("[^0-9]", "");
		String deleteArchivedAssembly = "DELETE FROM Arquivo WHERE ID_Arquivo = " + ID;
		String deleteArchivedParts = "DELETE FROM Arquivo_Pecas WHERE Montagem = " + readDB("ID_Montagem", "Arquivo", "ID_Arquivo", ID).replaceAll("[^0-9]", "");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver \"MySQL Connector-J\" e Tente Novamente", "Erro no Java Data Base Conector", 
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			
			//System.out.println(restoreAssembly);
			statement.executeUpdate(restoreAssembly);
			//System.out.println(restoreParts);
			statement.executeUpdate(restoreParts);
			//System.out.println(deleteArchivedAssembly);
			statement.executeUpdate(deleteArchivedAssembly);
			//System.out.println(deleteArchivedParts);
			statement.executeUpdate(deleteArchivedParts);
			
			con.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public static void registerFortnight(String date) {
		String mainBody = "INSERT INTO HISTORICO_CUSTO(Date, Assembly, Cost) VALUES(";
		ArrayList<String> assemblyIDs = new ArrayList<>();
		String dateIndex = readDB("ID_Fortnight", "Quinzena", "Date", "'" + date + "'").replaceAll(" § \n", "");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			
			assemblyIDs = Functions.listToArrayList(readDB("ID_Montagem", "Montagem").split(" § \n"));
			
			for(int i = 0; i < Almoxarifado.quantityAssembly; i++) {
				String query = mainBody + dateIndex + ", ";
				query += assemblyIDs.get(i).replaceAll(" § \n", "") + ", ";
				query += getAssemblyValue(assemblyIDs.get(i).replaceAll(" § \n", "")) + ")";
				
				System.out.println("register Values: " + query);
				
				statement.executeUpdate(query);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static double getAssemblyValue(String ID) {
		String priceQuery = "SELECT price FROM Pecas WHERE Montagem = " + ID;
		String quantityQuery = "SELECT quantity FROM Pecas WHERE Montagem = " + ID;
		
		ArrayList<String> prices = new ArrayList<>();
		ArrayList<String> quantities = new ArrayList<>();
		
		double finalValue = 0;
		
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
				String quantity = rsltQuantity.getString(1); 
				
				for(int i = 0; i < quantity.length(); i++) {
					if(quantity.charAt(i) == ' ') {
						quantity = quantity.substring(0, i);
						break;
					}
				}
				
				quantities.add(quantity);
			}
			
			for(int i = 0; i < prices.size(); i++) {
				BigDecimal firstValue = new BigDecimal("0" + prices.get(i).replaceAll("[^0-9]", ""));
				BigDecimal lastValue = new BigDecimal("0" + quantities.get(i).replaceAll("[^0-9]", ""));
				
				System.out.println(firstValue.toString() + " * " + lastValue.toString() + " = " + firstValue.multiply(lastValue).doubleValue());
				
				finalValue += firstValue.multiply(lastValue).doubleValue();
			}
			System.out.println("");
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return finalValue;
	}
	
	public static double totalValueExpended() {
		double returnValue = 0.0;
		String IDsQuery = "SELECT ID_Montagem FROM Montagem";
		ArrayList<String> identifiers = new ArrayList<>(); 
		ArrayList<Double> prices = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			ResultSet rslt = statement.executeQuery(IDsQuery);
			
			int i = 0;
			
			while(rslt.next()) {
				identifiers.add(rslt.getString(1));
				
				prices.add(getAssemblyValue(rslt.getString(1)));
				returnValue = Functions.sumCurency("" + returnValue, "" + prices.get(i));

				i++;
			}
			
			for(int inc = 0; inc < identifiers.size(); inc++) {
				statement.executeUpdate("UPDATE Montagem SET cost = " + prices.get(inc) + "WHERE ID_Montagem = " + identifiers.get(inc));
				System.out.println("Montagem " + identifiers.get(inc) + " foi atualizada :D");
			}
			
			rslt = statement.executeQuery("SELECT cost FROM Arquivo");
			while(rslt.next()) {
				returnValue = Functions.sumCurency("" + returnValue, "" + rslt.getString(1));
				System.out.println("returnValue recebe " + rslt.getString(1));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return returnValue;
	}
	
	public static int counterOfElements(String table) {
		int returnCounter = 0;
		String query = "SELECT COUNT(*) FROM " + table;
		
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
				returnCounter += rslt.getInt(1);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return returnCounter;
	}
	
	public static int counterOfElements(String table, String condition) {
		int returnCounter = 0;
		String query = "Select * from " + table + " where " + condition;
		
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

	
	public void backupDB() {
		
	}
}
