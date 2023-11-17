package functions;

import java.io.File;
import java.io.FileInputStream;

public class Record {
	
	public File history = new File(ClassLoader.getSystemResource("changeLog.txt").getFile());
	public FileInputStream log;

}
