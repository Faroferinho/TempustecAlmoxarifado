package functions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import pages.Profile;

public class Archiver {

	public Archiver() {
		// TODO Auto-generated constructor stub
	}
	
	public static void writeOnArchive(String action, String ID, String basis, String changes) {
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
				toArchive += "entrou no modo de listagem de " + ID;
				break;
			case "cadastro":
				toArchive += "inseriu um novo cadastro de " + ID + " no DB de " + basis + " = " + changes;
				break;
			case "alteracao":
				toArchive += "alterou " + ID + ", passou de " + basis + " para " + changes;
				break;
			case "remocao":
				toArchive += "removeu o " + ID + " de ID " + basis;
				break;
			case "mudarPag":
				toArchive += "modou para o projeto " + ID;
				break;
			case "arquivo":
				toArchive += "arquivou " + ID;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createReport(String ID) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("Report.txt"));
			String message = "";
			String date;
			
			LocalDateTime ldt = LocalDateTime.now();
			
			date = "" + ldt.getDayOfMonth() + "/" + ldt.getMonthValue() + "/" + ldt.getYear() + " - " + ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond();
			
			message += date + "\n";
			message += "Arquivo de Montagem da " + DBConector.findInDB("ISO", "Montagem", "ID_Montagem", ID).replace(" § \n", "") +
					"(" + DBConector.findInDB("ISO", "Montagem", "ID_Montagem", ID).replace(" § \n", "") + "), Informações da Montagem: \n";
			message += " - ID: " + ID + ";\n - Preço: " + DBConector.findInDB("cost", "Montagem", "ID_Montagem", ID).replace(" § \n", "") + ";\n - Quantidade de Peças" + 
					DBConector.counterOfElements(ID, "pecas", "parts") + ";\n";
			message += "					- Almoxarifado.";
			
			writer.write(message);
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
