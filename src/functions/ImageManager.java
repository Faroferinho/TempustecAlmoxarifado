package functions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageManager {
	
	private BufferedImage spritesheet;
	
	public ImageManager(String imgFile) {
		try {
			spritesheet = ImageIO.read(new File("res/" + imgFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height) {
		
		BufferedImage sprite = spritesheet.getSubimage(x, y, width, height);
		
		return sprite;
		
	}
	
}
