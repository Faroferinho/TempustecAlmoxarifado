package functions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import pages.Profile;

public class Archiver {
	
	static String genericCommand = "INSERT INTO ";

	public Archiver() {
		// TODO Auto-generated constructor stub
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
		String command = genericCommand + "Quinzena(date, totalExpanses) VALUES(";
		
		LocalDateTime thisMoment = LocalDateTime.now();
		String auxDate = thisMoment.toString();
		auxDate = auxDate.substring(0, 19);
		auxDate = auxDate.replaceAll("T", " ");
		command += auxDate + ", ";
		
		System.out.println("command: " + command);
		
	}
	
	protected static void createExpancesReport() {
			String message = "";
			String date;
			
			LocalDateTime ldt = LocalDateTime.now();
			
			date = "" + ldt.getDayOfMonth() + "/" + ldt.getMonthValue() + "/" + ldt.getYear() + " - " + ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond();
			
			message += date + "\n";
			
			
			Email.sendReport("Relatório Quinzenal - " + date, message);
	}
	
	protected static void createCongratulationsReport() {
		String message = "";
		String date;
		
		LocalDateTime ldt = LocalDateTime.now();
		
		date = "" + ldt.getDayOfMonth() + "/" + ldt.getMonthValue() + "/" + ldt.getYear() + " - " + ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond();
		
		message += date + "\n";
		message += "Os gastos essa Quinzena foram exatamente 0, Parabens a todos os envolvidos 😀🥳";
		message += "					- Almoxarifado.";
		
		Email.sendReport("Relatório Quinzenal - " + date, message);
	}

}
