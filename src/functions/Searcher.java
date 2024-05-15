package functions;

public abstract class Searcher {
	
	private static boolean direction = true;

	public Searcher() {
		// TODO Auto-generated constructor stub
	}
	
	public static String searchEngine(String match, String coluna, String table) {
		String query = "SELECT * FROM " + table + " WHERE\n"
					 + coluna + " LIKE \"" + match + "%\" OR\n"
		 			 + coluna + " LIKE \"%" + match + "%\" OR\n"
 			 		 + coluna + " LIKE \"%" + match + "\"\n"
 			 		 + "LIMIT 10";
		
		return query;
	}
	
	public static String orderByColumn(String column, String table) {
		String query = "SELECT * FROM " + table + " Order By ";
		
		switch(table) {
		case "Funcionarios":
			query += getColumnNameWorkers(column);
			break;
		case "Pecas":
			query += getColumnNameParts(column);
			break;
		case "Montagem":
			query += getColumnNameAssemblies(column);
			break;
		case "Arquivo":
			query += getColumnNameArchive(column);
			break;
		}
		
		if(direction) {
			query += " ASC";
		}else {
			query += " DESC";
		}
		
		//System.out.println("Organizar: \n" + query);
		return query;
	}
	
	public static boolean getDirection() {
		return direction;
	}
	
	public static boolean alternateDirecion() {
		if(direction) {
			direction = false;
		}else {
			direction = true;
		}
		
		return getDirection();
	}
	
	private static String getColumnNameWorkers(String column) {
		String columnIdentificator = "";
		
		switch(column) {
		case "RdF":
		case "CPF":
			columnIdentificator = column;
			break;
		case "Nome":
			columnIdentificator = "Name";
			break;
		case "Tipo":
			columnIdentificator = "Type";
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
		case "IDs":
			columnIdentificator = "ID_Montagem";
			break;
		case "O.S.":
			columnIdentificator = "ISO";
			break;
		case "Descrições":
			columnIdentificator = "Description";
			break;
		case "Empresas":
			columnIdentificator = "Company";
			break;
		}
		
		return columnIdentificator;
	}
	
	private static String getColumnNameArchive(String column) {
		String columnIdentificator = "";
		
		switch(column) {
		case "IDs":
			columnIdentificator = "ID_Arquivo";
			break;
		case "O.S.":
			columnIdentificator = "ISO";
			break;
		case "Quantidades":
			columnIdentificator = "";
			break;
		case "Datas":
			columnIdentificator = "Archive_Moment";
			break;
		case "Usuários":
			columnIdentificator = "Archiver_RdF";
			break;
		}
		
		return columnIdentificator;
	}

}
