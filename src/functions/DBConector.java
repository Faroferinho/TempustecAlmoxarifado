package functions;

import java.sql.*;

//import java.sql.*;

public class DBConector {
	
	//Poderia trocar o user pelo perfil do usuário em um futuro distante;
	public static String urlDBTempustec = "jdbc:mysql://localhost:3306/Tempustec";
	public static String user = "Conras";
	public static String password = "1234";
	
	public int qnttWrks = 0;
	public int qnttPrts = 0;
	public int qnttAssbly = 0;
	
	public DBConector() {
		String workers = "select * from funcionarios";
		String parts = "select * from pecas";
		String assemblies = "select * from Montagem";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			
			con.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public static String readDB(String objective, String table, int maxIndex){
		
		String query = "select " + objective + " from " + table;
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		String returnData = "";
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				for(int i = 1; i < maxIndex; i++) {
					
					if(maxIndex == 2) {
						returnData += result.getString(i);
					}else {
						returnData += result.getString(i) + " . ";
					}
				}
				returnData += "\n";
			}
			con.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		return returnData;
	}
	
	public static void writeDB(String objective) {
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public static void editLine(String Table, String objective, String newInfo, String PrimaryKey) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String query = "UPDATE " + Table + " SET " + objective + " = '" + newInfo + "' WHERE RdF = " + PrimaryKey;
		
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
		}
		
		String query = "SELECT " + objective + " FROM " + table + " WHERE " + column + " = " + key;
		
		String answer = "";
		
		try {
			Connection con = DriverManager.getConnection(urlDBTempustec, user, password);
			Statement statement = con.createStatement();
			ResultSet rslt = statement.executeQuery(query);
			
			
			while(rslt.next()) {
				if(objective == "*") {
					for(int i = 0; i < 6; i++) {
						answer += rslt.getString(i) + " . ";
					}
				}else {
					answer += rslt.getString(1) + "\n";
				}
			}
			
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return answer;
	}
}
