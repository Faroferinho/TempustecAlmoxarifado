package functions;

public abstract class Searcher {
	
	private static boolean direction = false;

	public Searcher() {
		// TODO Auto-generated constructor stub
	}
	
	public static String orderByColumn(String column, String table) {
		String query = "SELECT * FROM " + table + " Order By ";
		
		switch(table) {
		case "Pecas":
			query += getColumnNameParts(column);
			break;
		}
		
		if(direction) {
			query += " ASC";
		}else {
			query += " DESC";
		}
		
		if(direction) {
			direction = false;
		}else {
			direction = true;
		}
		
		System.out.println("Organizar: \n" + query);
		return query;
	}
	
	private static String getColumnNameWorkers(String column) {
		String columnIdentificator = "";
		
		switch(column) {
		case "RdF":
			columnIdentificator = column;
			break;
		}
		
		return columnIdentificator;
	}
	
	private static String getColumnNameParts(String column) {
		String columnIdentificator = "";
		
		switch(column) {
		case "ID":
			columnIdentificator = "ID_Parts";
			break;
		case "Montagem":
			columnIdentificator = "Montagem";
			break;
		case "Descrição":
			columnIdentificator = "Description";
			break;
		case "Quantidade":
			columnIdentificator = "Quantity";
			break;
		case "Preço":
			columnIdentificator = "Price";
			break;
		case "Data do Pedido":
			columnIdentificator = "Creation_Date";
			break;
		case "Fornecedor":
			columnIdentificator = "Supplier";
			break;
		case "Status":
			columnIdentificator = "Status";
			break;
		}
		
		return columnIdentificator;
	}
	
	private static String getColumnNameAssemblies(String column) {
		String columnIdentificator = "";
		
		switch(column) {
		case "ID":
			columnIdentificator = "ID_";
			break;
		}
		
		return columnIdentificator;
	}
	
	private static String getColumnNameArchive(String column) {
		String columnIdentificator = "";
		
		switch(column) {
		case "ID":
			columnIdentificator = "ID_";
			break;
		}
		
		return columnIdentificator;
	}

}
