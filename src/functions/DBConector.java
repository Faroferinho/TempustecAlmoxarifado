package functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import javax.swing.JOptionPane;

import main.Almoxarifado;
import pages.Employee;

public class DBConector {
	
	//Poderia trocar o user pelo perfil do usuário em um futuro distante;
	private static String urlDBTempustec = "jdbc:mysql://localhost:3306/Tempustec";
	private static String user = "root";
	private static String password = "1234";
	
	public int qnttWrks = 0;
	public int qnttPrts = 0;
	public int qnttAssbly = 0;
	public int qnttArchvs = 0;
	public int qnttArchvParts = 0;
	public int qnttTyps = 0;
	
	public DBConector() {
		String workers = "select * from funcionarios";
		String parts = "select * from pecas";
		String assemblies = "select * from Montagem";
		String archives = "select * from Arquivo";
		String archiveParts = "select * from Arquivo_Pecas";
		String types = "select * from Tipo_Quantidade";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver ''JDBC'' e Tente Novamente", 
			"Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			
			ResultSet query = statement.executeQuery(workers);
			
			while(query.next()) {
				qnttWrks++;
			}
			query = statement.executeQuery(parts);
			
			while(query.next()) {
				qnttPrts++;
			}
			query = statement.executeQuery(assemblies);
			
			while(query.next()) {
				qnttAssbly++;
			}
			
			query = statement.executeQuery(archives);
			while(query.next()) {
				qnttArchvs++;
			}
			
			query = statement.executeQuery(archiveParts);
			while(query.next()) {
				qnttArchvParts++;
			}
			
			query = statement.executeQuery(types);
			while(query.next()) {
				qnttTyps++;
			}
			
			con.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public static String readDB(String objective, String table){
		
		String query = "select " + objective + " from " + table;
		
		//System.out.println(query);
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver ''JDBC'' e Tente Novamente", 
			"Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		

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
		
		//System.out.println(returnData);
		
		return returnData;
	}
	
	public static void writeDB(String objective) {
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver ''JDBC'' e Tente Novamente",
			"Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			
			System.out.println(objective);
			
			statement.executeUpdate(objective);
			
			con.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public static void editLine(String Table, String objective, String newInfo, String PK,String PrimaryKey) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver ''JDBC'' e Tente Novamente", "Erro no Java Data Base Conector", 
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		String query = "UPDATE " + Table + " SET " + objective + " = '" + newInfo + "' WHERE " + PK + " = " + PrimaryKey;
		
		System.out.println(query);
		
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
			JOptionPane.showMessageDialog(null, "Instale o Driver ''JDBC'' e Tente Novamente", "Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		String query = "SELECT " + objective + " FROM " + table + " WHERE " + column + " = " + key;
		String answer = "";
		int max = checkSize(objective, table);
		
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
		return answer;
	}

	public static void Archive(String ID) {
		
		System.out.println("ID: " + ID);
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver ''JDBC'' e Tente Novamente", 
					"Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		String query = "SELECT * FROM Montagem WHERE ID_Montagem = " + ID;
		String auxInfoFromMontagem = "";
		
		LocalDateTime moment = LocalDateTime.now();
		String auxDateTime = moment.toString();
		auxDateTime = auxDateTime.substring(0, 19);
		auxDateTime = auxDateTime.replaceAll("T", " ");
		
		System.out.println("Data e Hora: " + auxDateTime);
		
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
						auxString = ", '";
						break;
					case 2:
					case 3:
					case 4:
						auxString = "', '";
						break;
					case 5:
						auxString = "', ";
						break;
					}
					
					auxInfoFromMontagem += rslt.getString(i) + auxString;
				}
			}
			
			query = "INSERT INTO Arquivo VALUES (" + Almoxarifado.quantityArchives + ", " + auxInfoFromMontagem + auxDateTime + "', " 
			+ Almoxarifado.rdf + ");";
			
			System.out.println(query);
			
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
						aux = ", '";
						break;
					case 3:
					case 7:
						aux = "', ";
						break;
					case 8:
						aux = "); \n";
					}
					
					partsQuery += rslt.getString(i) + aux;
				}
				System.out.println(partsQuery);
				Almoxarifado.quantityArchiveParts++;
			}
			statement.executeUpdate(partsQuery);
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Almoxarifado.quantityArchives++;
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
			max = 9;
		}else if(table.equals("arquivo_pecas") || table.equals("Arquivo_pecas") || table.equals("Arquivo_Pecas") || table.equals("ARQUIVO_Pecas")) {
			max = 10;
		}
		
		if(!objective.equals("*")) {
			max  = 2;
		}
		
		return max;
	}
}
