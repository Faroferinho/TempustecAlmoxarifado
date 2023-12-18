package functions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageManager {
	
	private BufferedImage spritesheet;
	
	public BufferedImage TempustecIcon = this.getProjectImage("Tempustec Logo Icone 1");
	
	public ImageManager(String imgFile) {
		try {

			spritesheet = ImageIO.read(new File("C:/Users/User/eclipse-workspace/AlmoxarifadoTempustec/res/Spritesheet.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height) {
		
		BufferedImage sprite = spritesheet.getSubimage(x, y, width, height);
		
		return sprite;
		
	}
	
	public BufferedImage getProjectImage(String fileName) {
		BufferedImage returnImg = null;
		
		try {
			returnImg = ImageIO.read(new File("C:/Users/User/eclipse-workspace/AlmoxarifadoTempustec/res/" + fileName + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Não pude pegar o arquivo");
			e.printStackTrace();
		}
		
		return returnImg;
	}
	
}
