package functions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import pages.Profile;

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
			
			String toArchive = "O Usuário " + Profile.RdF + " ";
			
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
		LocalDateTime thisMoment = LocalDateTime.now();
		String auxDate = thisMoment.toString();
		auxDate = auxDate.substring(0, 19);
		auxDate = auxDate.replaceAll("T", " ");
		
		int lastQuinzena = Integer.parseInt(DBConector.readDB("MAX(ID_Fortnight)", "Quinzena").replaceAll(" § \n", ""));
		
		if(fortnightVerificator(auxDate)) {
			String lastValue = "";
			if(!DBConector.readDB("*", "Quinzena").replaceAll(" § \n", "").equals("")) {
				lastValue += DBConector.readDB("totalExpanses", "Quinzena", "ID_Fortnight", DBConector.readDB("MAX(ID_Fortnight)", "Quinzena").replaceAll(" § \n", "")).replaceAll(" § \n", "");
			}else {
				lastValue += "0.0";
			}
			
			String command = genericCommand + "Quinzena(date, totalExpanses) VALUES('";
			double totalValue = DBConector.totalValueExpended();
			
			command += auxDate + "', ";
			command += totalValue + ")";
			
			System.out.println("LogInfo: " + command);
			DBConector.writeDB(command);
			
			DBConector.registerFortnight(auxDate);
			
			if(totalValue != Double.parseDouble(lastValue)) {
				createExpancesReport(Functions.diferenceCurency("" + totalValue, lastValue), lastQuinzena);
			}else {
				createCongratulationsReport();
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
	
	private static void createExpancesReport(double difCost, int lastEntry) {
			String message = "";
			String emailDate;
			
			LocalDateTime ldt = LocalDateTime.now();
			
			String currIDs[] = DBConector.readDB("ID_Montagem", "Montagem").split(" § \n");
			
			emailDate = "" + ldt.getDayOfMonth() + "/" + ldt.getMonthValue() + "/" + ldt.getYear() + " - " + ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond();
			
			message += emailDate + "\n";
			message += "	" + Functions.randomGreetingsGen() + "\n";
			message += "	O Relatório destá quinzena Registrou um total gasto Total de " + difCost + ", segue o valor gasto com as montagens: \n";
			message += "========================================================================================================================\n";
			
			for(int i = 0; i < currIDs.length; i++) {
				double difPrices = 0;
				if(currIDs[i].equals(DBConector.readDB("Assembly", "Historico_Custo", "Assembly", currIDs[i] + " && Date = " + lastEntry).replaceAll(" § \n", ""))) {
					difPrices = Functions.diferenceCurency(
							DBConector.readDB("Cost", "Montagem", "ID_Montagem", currIDs[i]).replaceAll(" § \n", ""), 
							DBConector.readDB("Cost", "Historico_Custo", "Assembly", currIDs[i]+ " && Date = " + lastEntry).replaceAll(" § \n", ""));
				}else {
					difPrices = Double.parseDouble(DBConector.readDB("cost", "Montagem", "ID_Montagem", currIDs[i]).replaceAll(" § \n", ""));
				}
				
				System.out.println("Archiver.difPrices: " + difPrices);
				
				if(difPrices != 0) {
					message += "	ID: " + currIDs[i] + "\n";
					message += "	 - Descrição: " + DBConector.readDB("description", "Montagem", "ID_Montagem", currIDs[i]).replaceAll(" § ", "");
					message += "	 - Empresa: " + DBConector.readDB("company", "Montagem", "ID_Montagem", currIDs[i]).replaceAll(" § ", "");
					message += "	 - Quantidade: " + DBConector.counterOfElements("pecas", "Montagem = " + currIDs[i]) + "\n";
					message += "	 - Valor: " + difPrices + "\n";
					message += "========================================================================================================================\n";
				}
			}
			
			message += "		Tenha um Bom Resto do seu Dia!\n";
			message += "								- Almoxarifado";
			
			System.out.println(message);
			
			//Email.sendReport("Relatório Quinzenal - " + emailDate, message);
	}
	
	private static void createCongratulationsReport() {
		String message = "";
		String date;
		
		LocalDateTime ldt = LocalDateTime.now();
		
		date = "" + ldt.getDayOfMonth() + "/" + ldt.getMonthValue() + "/" + ldt.getYear() + " - " + ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond();
		
		message += date + "\n";
		message += "	Os gastos essa Quinzena foram exatamente 0, Parabens a todos os envolvidos 😀🥳\n";
		message += "														- Almoxarifado.";
		
		//Email.sendReport("Relatório Quinzenal - " + date, message);
	}

}
