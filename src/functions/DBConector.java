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
	public int qnttTyps = 0;
	
	public DBConector() {
		String workers = "select * from funcionarios";
		String parts = "select * from pecas";
		String assemblies = "select * from Montagem";
		String archives = "select * from Arquivo";
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
			
			ResultSet queryWorkersResult = statement.executeQuery(workers);
			
			while(queryWorkersResult.next()) {
				qnttWrks++;
			}
			ResultSet queryPartsResult = statement.executeQuery(parts);
			
			while(queryPartsResult.next()) {
				qnttPrts++;
			}
			ResultSet queryAssembliesResult = statement.executeQuery(assemblies);
			
			while(queryAssembliesResult.next()) {
				qnttAssbly++;
			}
			
			ResultSet queryArchivesResult = statement.executeQuery(archives);
			while(queryArchivesResult.next()) {
				qnttArchvs++;
			}
			
			ResultSet queryQTResult = statement.executeQuery(types);
			while(queryQTResult.next()) {
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
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Instale o Driver ''JDBC'' e Tente Novamente", "Erro no Java Data Base Conector", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		String moment = "" + LocalDateTime.now();
		
		moment = moment.substring(0, 19);
		moment.replaceAll("-", "");
		moment.replaceAll("T", " ");
		
		//System.out.println("data e Hora: " + LocalDateTime.now());
		
		String getInfo = "SELECT * FROM Montagem WHERE ID_Montagem = " + ID;
		String textToQuery = "";
		String passToArchive = "INSERT INTO Arquivo(ID_Arquivo, ID_Montagem, ISO, Description, Company, image, cost, Archive_Moment, Archiver_RdF) "
				+ "VALUES (" + (Almoxarifado.quantityArchives+1) + ", ";
		String deleteProject = "DELETE FROM Montagem WHERE ID_Montagem = " + ID;
		String getParts = "SELECT * FROM Pecas WHERE Montagem = " + ID;
		String copyParts = "";
		String deleteParts = "DELETE * FROM Pecas WHERE Montagem = " + ID;
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			ResultSet rslt = statement.executeQuery(getInfo);
			
			while(rslt.next()) {
				String aux = "";
				
				for(int i = 1; i < 7; i++) {
					switch(i) {
					case 1:
						aux = ", \"";
						break;
					case 3:
					case 2:
						aux = "\", \"";
						break;
					case 4:
						aux = "\", ";
						break;
					case 5:
						aux = ", ";
						break;
					case 6:
						aux = "";
					}
					
					textToQuery += rslt.getString(i) + aux;
					
				}
				
			}
			
			passToArchive += textToQuery + ", '" + moment + "', " + Employee.RdF + ")";
			
			//System.out.println("Texto Pego Por Hora: \n" + passToArchive);
			
			statement.executeUpdate(passToArchive);
			
			statement.executeUpdate(deleteProject);
			
			
			rslt = statement.executeQuery(getParts);
			
			
			while(rslt.next()) {
				textToQuery = "";
				copyParts += "INSERT INTO Arquivo_Pecas(ID_Arquivo_Pecas, ID_Parts, Montagem, Description, Quantity, Quantity_Type, Price, "
				+ "Supplier, Status, Archive_Moment, ArchiverRdF) VALUES( ";
						
				
				for(int i = 1; i < 9; i++) {
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
					}
					
					textToQuery += rslt.getString(i) + aux;
				}
				
				copyParts += textToQuery + ", '" + moment + "', " + Almoxarifado.rdf + ");\n";
				
			}
			
			System.out.println("Prompt copiar a lista de peças: \n" + copyParts);
			if(copyParts.length() < 4) {
				statement.executeUpdate(copyParts);
			}
			statement.executeUpdate(deleteParts);
			
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
		}
		
		if(!objective.equals("*")) {
			max  = 2;
		}
		
		return max;
	}
}
