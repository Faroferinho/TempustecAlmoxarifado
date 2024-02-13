package functions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import main.Almoxarifado;
import pages.Profile;

public class Archiver {
	
	static String genericCommand = "INSERT INTO ";

	public Archiver() {
		
	}
	
	public static void writeOnArchive(String action, String args0, String args1, String changes) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("archive.txt", true));
			
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
		
		if(fortnightVerificator(auxDate)) {
			String command = genericCommand + "Quinzena(date, totalExpanses) VALUES('";
			
			String lastValue = DBConector.readDB("totalExpanses", "Quinzena", "ID_Fortnight", DBConector.readDB("MAX(ID_Fortnight)", "Quinzena").replaceAll(" § \n", "")).replaceAll(" § \n", "");
			double totalValue = DBConector.totalValueExpended();
			
			command += auxDate + "', ";
			command += totalValue + ")";
			
			System.out.println("LogInfo: " + command);
			DBConector.writeDB(command);
			
			DBConector.registerFortnight(auxDate);
			
			if(totalValue != Double.parseDouble(lastValue)) {
				createExpancesReport(totalValue - Double.parseDouble(lastValue));
			}else {
				createCongratulationsReport();
			}
		}
	}
	
	private static boolean fortnightVerificator(String date) {
		String getDateFromQuinzena = DBConector.readDB("MAX(Date)", "Quinzena").replaceAll(" § \n", "").replace(" ", "T");
		
		if(Functions.emptyString(getDateFromQuinzena)) {
			return false;
		}
		
		LocalDateTime lastDate = LocalDateTime.parse(getDateFromQuinzena);
		LocalDateTime currDate = LocalDateTime.parse(date.replace(" ", "T"));
		
		if(currDate.isAfter(lastDate.plusDays(15))) {
			System.out.println("Passou 15 dias");
			return true;
		}
		
		return false;
	}
	
	private static void createExpancesReport(double difCost) {
			String message = "";
			String date;
			
			String IDs[] = DBConector.readDB("ID_Montagem", "Montagem").split(" § \n");
			String descriptions[] = DBConector.readDB("description", "Montagem").split(" § \n");
			String companies[] = DBConector.readDB("company", "Montagem").split(" § \n");
			String currPrices[] = DBConector.readDB("cost", "Montagem").split(" § \n");
			String lastPrices[] = DBConector.readDB("cost", "Historico_Custo").split(" § \n");
			
			LocalDateTime ldt = LocalDateTime.now();
			
			date = "" + ldt.getDayOfMonth() + "/" + ldt.getMonthValue() + "/" + ldt.getYear() + " - " + ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond();
			
			message += date + "\n";
			message += "	" + Functions.randomGreetingsGen() + "\n";
			message += "	O Relatório destá quinzena Registrou um total gasto Total de " + difCost + ", segue o valor gasto com as montagens: \n";
			message += "========================================================================================================================\n";
			for(int i = 0; i < Almoxarifado.quantityAssembly; i++) {
				double difPrices = Double.parseDouble(currPrices[i]) - Double.parseDouble(lastPrices[i]);
				
				if(difPrices != 0) {
					message += "	ID: " + IDs[i] + "\n";
					message += "	 - Descrição: " + descriptions[i] + "\n";
					message += "	 - Empresa: " + companies[i] + "\n";
					message += "	 - Quantidade: " + DBConector.counterOfElements("pecas", "Montagem = " + IDs[i]) + "\n";
					message += "	 - Valor: " + difPrices + "\n";
					message += "========================================================================================================================\n";
				}
			}
			message += "		Tenha um Bom Resto do seu Dia!\n";
			message += "								- Almoxarifado";
			
			//System.out.println(message);
			
			Email.sendReport("Relatório Quinzenal - " + date, message);
	}
	
	private static void createCongratulationsReport() {
		String message = "";
		String date;
		
		LocalDateTime ldt = LocalDateTime.now();
		
		date = "" + ldt.getDayOfMonth() + "/" + ldt.getMonthValue() + "/" + ldt.getYear() + " - " + ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond();
		
		message += date + "\n";
		message += "	Os gastos essa Quinzena foram exatamente 0, Parabens a todos os envolvidos 😀🥳\n";
		message += "														- Almoxarifado.";
		
		Email.sendReport("Relatório Quinzenal - " + date, message);
	}

}
