package functions;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;

public class Log {
	
	public static File history = new File("C:/Users/Tempustec/eclipse-workspace/AlmoxarifadoTempustec0.2/res/log.txt");
	public FileHandler fh;
	
	public void writeOnLog(String name, String action) {
		LocalDateTime currentTime = LocalDateTime.now();
		String textToWrite = name + " fez " + action + " at " + currentTime + "/ln";
		try {
			history.setReadable(true);
			history.setWritable(true);
			FileReader fr = new FileReader(history);
			FileWriter fw = new FileWriter(history);
			
			System.out.println("\rFR: " + fr.read());
			
			
			fr.close();
			fw.close();
			
			//fw.write(textToWrite, sizeOfHistory, textToWrite.length());
			
			System.out.println("A guarda de Informações foi Concluida com Exito");
			System.out.println(textToWrite);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Deu erro");
			e.printStackTrace();
		}
		
		
	}

}
