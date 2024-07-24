package functions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import main.Almoxarifado;

public class Archiver {
	
	static String genericCommand = "INSERT INTO ";

	public Archiver() {
		
	}
	
	public static void writeOnArchive(String action, String args0, String args1, String changes) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("archive.txt", true));
			if(action.equals("login")) {
				writer.append("--------------------------------------------------------------------------------------\n");
			}
			
			String toArchive = "O Usuário " + Almoxarifado.userProfile.getRdF() + " ";
			
			switch(action) {
			case "login":
				toArchive += "efetuou login";
				break;
			case "edicao1":
				toArchive += "entrou no modo de edição";
				break;
			case "edicao2":
				toArchive += "saiu do modo de edição";
				break;
			case "listagem":
				toArchive += "entrou no modo de listagem de " + args0;
				break;
			case "cadastro":
				toArchive += "inseriu um novo cadastro de " + args0 + " no DB de " + args1 + " = " + changes;
				break;
			case "alteracao":
				toArchive += "alterou " + args0 + ", passou de " + args1 + " para " + changes;
				break;
			case "remocao":
				toArchive += "removeu o " + args0 + " de ID " + args1;
				break;
			case "mudarPag":
				toArchive += "modou para o projeto " + args0;
				break;
			case "arquivo":
				toArchive += "arquivou " + args0;
				break;
			case "":
				toArchive += "";
				break;
			}
			
			LocalDateTime now = LocalDateTime.now();
			
			toArchive += " - Horario: " + now + "\n";
			
			writer.append(toArchive + "\n");
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logInfo() {
		if(DBConector.getDB().equals("jdbc:mysql://localhost:3306/Tempusteste")) {
			LocalDateTime thisMoment = LocalDateTime.now();
			String auxDate = thisMoment.toString();
			auxDate = auxDate.substring(0, 19);
			auxDate = auxDate.replaceAll("T", " ");
			
			if(fortnightVerificator(auxDate)) {
				BigDecimal currentExpenses = DBConector.totalValueExpended();
				BigDecimal registredExpenses = DBConector.getRegisteredValues();
				
				System.out.println("\n");
				System.out.println("Gasto total - " + currentExpenses.toString());
				System.out.println("Gasto registrado - " + registredExpenses.toString());
				System.out.println("\n");
				
				if(currentExpenses.compareTo(registredExpenses) > 0) {
					//System.out.println("A quantidade registrada atual é maior que a anterior\n");
					int confirmValue = JOptionPane.showConfirmDialog(null, "Confirma o Envio de Relatório?", "", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
					
					if(confirmValue != 0) {
						return;
					}
					createExpancesReport();
					DBConector.registerFortnight();
				}else {
					return;
				}
			}
		}
	}
	
	private static boolean fortnightVerificator(String date) {
		String getDateFromQuinzena = ""; 
		getDateFromQuinzena += DBConector.readDB("MAX(Date)", "Quinzena").replaceAll(" § \n", "").replace(" ", "T");
		
		System.out.println("Ultima data: " + getDateFromQuinzena);
		
		if(Functions.emptyString(getDateFromQuinzena)) {
			System.out.println("Retornando");
			return true;
		}
		
		LocalDateTime lastDate = LocalDateTime.parse(getDateFromQuinzena);
		LocalDateTime currDate = LocalDateTime.parse(date.replace(" ", "T"));
		
		if(currDate.isAfter(lastDate.plusDays(15))) {
			return true;
		}
		
		return false;
	}
	
	private static void createExpancesReport() {
		String header = "Relatório Quinzenal - ";
		String body = "";
		
		LocalDateTime ldt = LocalDateTime.now();
		
		String date = ldt.getDayOfMonth() + "/" + ldt.getMonthValue() + "/" + ldt.getYear() + " - " + ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond();
		
		ArrayList<String> ids = Functions.listToArrayList(DBConector.readDB("SELECT ID_Montagem FROM Montagem").split(" § \n"));
		
		header += date;
		body += date + "\n	" + Functions.randomGreetingsGen() + "\n\n	O Relatório destá quinzena Registrou um total gasto de " + DBConector.totalValueExpended() + ", segue o valor gasto com as montagens:\n";
		body += "	=========================================================================================================\n";
		
		//System.out.println("----------------------------------------------------------------------------------");
		
		for(int i = 0; i < ids.size(); i++) {
			BigDecimal currAssemblyValue;
			BigDecimal lastAssemblyValue;
			
			String currValue = DBConector.readDB( "SELECT cost FROM Montagem WHERE ID_Montagem = " + ids.get(i) ).replaceAll(" § \n", "");
			String lastValue = DBConector.readDB( "SELECT cost FROM Historico_Custo WHERE Assembly = " + ids.get(i) ).replaceAll(" § \n", "");
			
			currAssemblyValue = new BigDecimal( "0" + currValue );
			lastAssemblyValue = new BigDecimal( "0" + lastValue );
			
			/*
			* System.out.println("Valor Atual - " + currAssemblyValue.toString());
			* System.out.println("Valor Anterior - " + lastAssemblyValue.toString());
			* System.out.println("----------------------------------------------------------------------------------");
			*/
					
			if(currAssemblyValue.compareTo(lastAssemblyValue) != 0) {
				body += "	- O.S.: " + DBConector.readDB("SELECT ISO FROM Montagem WHERE ID_Montagem = " + ids.get(i)).replaceAll(" § ", "");
				body += "		- Descrição: " + DBConector.readDB("SELECT Description FROM Montagem WHERE ID_Montagem = " + ids.get(i)).replaceAll(" § ", "");
				body += "		- Empresa: " + DBConector.readDB("SELECT Company FROM Montagem WHERE ID_Montagem = " + ids.get(i)).replaceAll(" § ", "");
				body += "		- Quantidade de Peças: " + DBConector.counterOfElements("Pecas", "Montagem = " + ids.get(i)) + "\n";
				body += "		- Valor total: " + DBConector.readDB("SELECT cost FROM Montagem WHERE ID_Montagem = " + ids.get(i)).replaceAll(" § ", "");
				
				if(i == ids.size() -1) {
					body += "	=========================================================================================================\n";
				}else {
					body += "	---------------------------------------------------------------------------------------------------------\n";					
				}
			}
		}
		
		body += "		Tenha um bom dia!\n"
			 +  "				- Almoxarifado";
		
		System.out.println("\n\n\n\n" + header + "\n" + body);
		//Email.sendReport(header, body);
	}
	
}
